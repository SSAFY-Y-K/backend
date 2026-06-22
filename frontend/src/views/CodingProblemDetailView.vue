<template>
	<div ref="containerRef" class="flex h-[calc(100vh-3rem)] overflow-hidden">
		<!-- 왼쪽: 문제 설명 (스크롤) -->
		<div
			:class="[
				'flex flex-col overflow-y-auto bg-white',
				showEditor ? 'border-r border-slate-200' : '',
				showEditor && !isResizing ? 'transition-[width] duration-200' : '',
			]"
			:style="showEditor ? { width: `${leftPaneWidth}%` } : { width: '100%' }"
		>
			<div v-if="loading" class="flex flex-1 items-center justify-center text-xs text-slate-400">
				불러오는 중...
			</div>
			<div v-else-if="error" class="flex flex-1 items-center justify-center text-xs text-red-400">
				{{ error }}
			</div>

			<div
				v-else-if="problem"
				v-memo="[problem, showEditor, showReportForm, reportContent, reporting, showAdminPanel, reports, testCases, editingTc, savingTc, authStore.userId, authStore.isAdmin]"
				class="space-y-0"
			>
				<!-- 헤더 -->
				<div class="border-b border-slate-100 px-6 py-4">
					<div class="mb-3 flex items-center justify-between">
						<button
							class="text-xs text-slate-400 transition hover:text-slate-600"
							@click="$router.push({ name: 'problem' })"
						>
							← 목록으로
						</button>
						<div class="flex items-center gap-2">
							<button
								v-if="authStore.isAdmin"
								class="text-xs text-red-400 transition hover:text-red-600"
								@click="handleDelete"
							>
								문제 삭제
							</button>
						</div>
					</div>
					<div class="mb-1.5 flex items-center gap-2">
						<span :class="['rounded px-2 py-0.5 text-[10px] font-bold text-white', difficultyColor[problem.difficulty]]">
							{{ difficultyLabel[problem.difficulty] ?? problem.difficulty }}
						</span>
						<span class="text-[11px] text-slate-400">{{ problem.category }}</span>
					</div>
					<h2 class="text-base font-bold text-slate-800">{{ problem.title }}</h2>
					<div class="mt-3 flex gap-5">
						<div class="flex items-center gap-1.5">
							<svg class="h-3.5 w-3.5 text-orange-400" viewBox="0 0 16 16" fill="none" stroke="currentColor" stroke-width="1.6">
								<circle cx="8" cy="8" r="6"/>
								<path d="M8 4.5V8l2.5 1.5" stroke-linecap="round"/>
							</svg>
							<span class="text-[11px] text-slate-500">시간 제한</span>
							<span class="text-[11px] font-semibold text-slate-700">{{ problem.timeLimit }}ms</span>
						</div>
						<div class="flex items-center gap-1.5">
							<svg class="h-3.5 w-3.5 text-blue-400" viewBox="0 0 16 16" fill="none" stroke="currentColor" stroke-width="1.6">
								<rect x="3" y="2" width="10" height="12" rx="1.5"/>
								<path d="M5 6h6M5 9h4" stroke-linecap="round"/>
							</svg>
							<span class="text-[11px] text-slate-500">메모리 제한</span>
							<span class="text-[11px] font-semibold text-slate-700">{{ problem.memoryLimit }}MB</span>
						</div>
					</div>
				</div>

				<!-- 문제 설명 -->
				<div class="border-b border-slate-100 px-6 py-4">
					<h4 class="mb-2 text-xs font-semibold uppercase tracking-wide text-slate-400">문제 설명</h4>
					<p class="whitespace-pre-wrap text-sm leading-relaxed text-slate-700">{{ problem.description }}</p>
				</div>

				<!-- 입력 형식 -->
				<div class="border-b border-slate-100 px-6 py-4">
					<h4 class="mb-2 text-xs font-semibold uppercase tracking-wide text-slate-400">입력 형식</h4>
					<p class="whitespace-pre-wrap text-sm text-slate-700">{{ problem.inputDescription }}</p>
				</div>

				<!-- 출력 형식 -->
				<div class="border-b border-slate-100 px-6 py-4">
					<h4 class="mb-2 text-xs font-semibold uppercase tracking-wide text-slate-400">출력 형식</h4>
					<p class="whitespace-pre-wrap text-sm text-slate-700">{{ problem.outputDescription }}</p>
				</div>

				<!-- 제약 조건 -->
				<div v-if="problem.constraintText" class="border-b border-slate-100 px-6 py-4">
					<h4 class="mb-2 text-xs font-semibold uppercase tracking-wide text-slate-400">제약 조건</h4>
					<p class="whitespace-pre-wrap text-sm text-slate-700">{{ problem.constraintText }}</p>
				</div>

				<!-- 샘플 테스트케이스 -->
				<div v-if="problem.sampleTestCases?.length" class="border-b border-slate-100 px-6 py-4">
					<h4 class="mb-3 text-xs font-semibold uppercase tracking-wide text-slate-400">샘플 테스트케이스</h4>
					<div class="space-y-3">
						<div
							v-for="(tc, i) in problem.sampleTestCases"
							:key="i"
							:class="['grid gap-2', showEditor ? 'grid-cols-1 xl:grid-cols-2' : 'grid-cols-2']"
						>
							<div>
								<p class="mb-1 text-[10px] font-medium text-slate-400">입력 {{ i + 1 }}</p>
								<pre class="rounded-md bg-slate-900 p-3 text-xs text-green-300">{{ tc.inputData }}</pre>
							</div>
							<div>
								<p class="mb-1 text-[10px] font-medium text-slate-400">출력 {{ i + 1 }}</p>
								<pre class="rounded-md bg-slate-900 p-3 text-xs text-green-300">{{ tc.expectedOutput }}</pre>
							</div>
						</div>
					</div>
				</div>

				<!-- 오류 신고 (로그인한 사용자) -->
				<div v-if="authStore.userId" class="border-b border-slate-100 px-6 py-4">
					<button
						class="text-xs text-slate-400 transition hover:text-red-500"
						@click="showReportForm = !showReportForm"
					>
						{{ showReportForm ? '취소' : '오류 신고' }}
					</button>
					<div v-if="showReportForm" class="mt-3 space-y-2">
						<textarea
							v-model="reportContent"
							placeholder="문제나 테스트케이스의 오류 내용을 입력해주세요."
							rows="3"
							class="w-full resize-none rounded-md border border-slate-200 px-3 py-2 text-xs text-slate-700 outline-none focus:border-blue-400"
						></textarea>
						<button
							class="h-7 rounded-md bg-red-500 px-4 text-xs font-semibold text-white transition hover:bg-red-600 disabled:opacity-50"
							:disabled="reporting || !reportContent.trim()"
							@click="handleReport"
						>
							{{ reporting ? '제출 중...' : '신고 제출' }}
						</button>
					</div>
				</div>

				<!-- 관리자 패널 -->
				<div v-if="authStore.isAdmin" class="px-6 py-4">
					<button
						class="mb-3 text-xs font-semibold text-orange-500 transition hover:text-orange-600"
						@click="loadAdminPanel"
					>
						{{ showAdminPanel ? '▲ 관리 패널 닫기' : '▼ 관리 패널 열기' }}
					</button>

					<div v-if="showAdminPanel" class="space-y-5">
						<!-- 신고 목록 -->
						<div>
							<h4 class="mb-2 text-xs font-semibold uppercase tracking-wide text-slate-400">신고 목록</h4>
							<div v-if="reports.length === 0" class="text-xs text-slate-400">신고 없음</div>
							<div v-for="r in reports" :key="r.reportId" class="mb-2 rounded-md border border-slate-200 p-3">
								<div class="mb-1 flex items-center justify-between">
									<span class="text-[10px] font-semibold text-slate-500">{{ r.nickname }}</span>
									<div class="flex items-center gap-2">
										<span :class="['text-[10px] font-bold', r.status === 'RESOLVED' ? 'text-emerald-500' : 'text-orange-500']">
											{{ r.status === 'RESOLVED' ? '해결됨' : '대기중' }}
										</span>
										<button
											v-if="r.status === 'PENDING'"
											class="text-[10px] text-blue-500 hover:underline"
											@click="handleResolve(r.reportId)"
										>
											해결 처리
										</button>
									</div>
								</div>
								<p class="whitespace-pre-wrap text-xs text-slate-700">{{ r.content }}</p>
							</div>
						</div>

						<!-- 테스트케이스 수정 -->
						<div>
							<h4 class="mb-2 text-xs font-semibold uppercase tracking-wide text-slate-400">테스트케이스 수정</h4>
							<div v-for="tc in testCases" :key="tc.testCaseId" class="mb-3 rounded-md border border-slate-200 p-3">
								<div class="mb-2 flex items-center justify-between">
									<span class="text-[10px] font-semibold text-slate-500">
										케이스 {{ tc.caseOrder }} — {{ tc.isSample ? '샘플' : '히든' }}
									</span>
									<button
										v-if="editingTc?.testCaseId !== tc.testCaseId"
										class="text-[10px] text-blue-500 hover:underline"
										@click="startEdit(tc)"
									>
										수정
									</button>
									<div v-else class="flex gap-2">
										<button class="text-[10px] text-slate-400 hover:underline" @click="editingTc = null">취소</button>
										<button
											class="text-[10px] font-semibold text-emerald-600 hover:underline disabled:opacity-50"
											:disabled="savingTc"
											@click="saveEdit"
										>
											{{ savingTc ? '저장 중...' : '저장' }}
										</button>
									</div>
								</div>

								<!-- 보기 모드 -->
								<template v-if="editingTc?.testCaseId !== tc.testCaseId">
									<div class="grid grid-cols-2 gap-2 text-[10px]">
										<div>
											<p class="mb-0.5 text-slate-400">입력</p>
											<pre class="whitespace-pre-wrap rounded bg-slate-900 p-2 text-green-300">{{ tc.inputData }}</pre>
										</div>
										<div>
											<p class="mb-0.5 text-slate-400">기댓값</p>
											<pre class="whitespace-pre-wrap rounded bg-slate-900 p-2 text-green-300">{{ tc.expectedOutput }}</pre>
										</div>
									</div>
								</template>

								<!-- 수정 모드 -->
								<template v-else>
									<div class="grid grid-cols-2 gap-2">
										<div>
											<p class="mb-0.5 text-[10px] text-slate-400">입력</p>
											<textarea
												v-model="editingTc.inputData"
												rows="3"
												class="w-full resize-none rounded border border-slate-300 bg-slate-50 px-2 py-1 font-mono text-xs outline-none focus:border-blue-400"
											></textarea>
										</div>
										<div>
											<p class="mb-0.5 text-[10px] text-slate-400">기댓값</p>
											<textarea
												v-model="editingTc.expectedOutput"
												rows="3"
												class="w-full resize-none rounded border border-slate-300 bg-slate-50 px-2 py-1 font-mono text-xs outline-none focus:border-blue-400"
											></textarea>
										</div>
									</div>
								</template>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

		<!-- 오른쪽: 에디터 -->
		<div
			v-if="showEditor"
			class="group flex w-3 cursor-col-resize items-center justify-center bg-slate-100/80"
			@pointerdown.prevent="startResize"
		>
			<div :class="['h-16 w-1 rounded-full transition-colors', isResizing ? 'bg-blue-400' : 'bg-slate-300 group-hover:bg-slate-400']"></div>
		</div>
		<div v-show="showEditor" class="flex min-w-0 flex-1 flex-col bg-slate-900">
			<!-- 툴바 -->
			<div class="flex items-center justify-between border-b border-slate-700 px-4 py-2">
				<div class="flex items-center gap-2">
					<select
						v-model="language"
						class="h-7 rounded border border-slate-600 bg-slate-800 px-2 text-xs text-slate-200 outline-none focus:border-blue-400"
					>
						<option value="PYTHON">Python</option>
						<option value="JAVA">Java</option>
						<option value="CPP">C++</option>
					</select>
					<button
						class="text-[10px] text-slate-500 transition hover:text-slate-300"
						@click="code = ''"
					>
						초기화
					</button>
				</div>
			</div>

			<!-- 코드 에디터 -->
			<textarea
				ref="editorRef"
				v-model="code"
				placeholder="코드를 입력하세요..."
				spellcheck="false"
				autocorrect="off"
				autocapitalize="off"
				class="flex-1 resize-none bg-slate-900 px-4 py-3 font-mono text-sm leading-relaxed text-green-300 outline-none placeholder:text-slate-600"
				@keydown.tab.prevent="handleTab"
			></textarea>

			<!-- 하단: 제출 이력 + 결과 + 버튼 -->
			<div class="border-t border-slate-700 px-4 py-3">
				<!-- 제출 이력 -->
				<div v-if="history.length > 0" class="mb-3">
					<button
						class="mb-1.5 flex items-center gap-1 text-[10px] text-slate-500 transition hover:text-slate-300"
						@click="showHistory = !showHistory"
					>
						<svg
							:class="['h-2.5 w-2.5 transition-transform', showHistory ? 'rotate-90' : '']"
							viewBox="0 0 6 10" fill="currentColor"
						>
							<path d="M1 1l4 4-4 4"/>
						</svg>
						제출 이력 ({{ history.length }})
					</button>
					<div v-if="showHistory" class="space-y-1 rounded-md bg-slate-800 p-2">
						<div
							v-for="(h, i) in history"
							:key="i"
							class="flex items-center justify-between rounded px-2 py-1 text-[10px]"
						>
							<div class="flex items-center gap-2">
								<span :class="verdictColor[h.verdict]">{{ verdictLabel[h.verdict] ?? h.verdict }}</span>
								<span class="text-slate-500">{{ h.language }}</span>
							</div>
							<div class="flex gap-2 text-slate-500">
								<span v-if="h.execTimeMs">{{ h.execTimeMs }}ms</span>
								<span>{{ h.time }}</span>
							</div>
						</div>
					</div>
				</div>

				<!-- 실행 결과 -->
				<div v-if="runResults" class="mb-3 space-y-2">
					<p class="text-[10px] font-semibold uppercase tracking-wide text-slate-400">실행 결과</p>
					<div
						v-for="r in runResults"
						:key="r.caseOrder"
						:class="['rounded-lg border p-2.5', r.correct ? 'border-emerald-700 bg-emerald-900/30' : 'border-slate-600 bg-slate-800']"
					>
						<div class="mb-1.5 flex items-center justify-between">
							<span class="text-[10px] font-semibold text-slate-300">케이스 {{ r.caseOrder }}</span>
							<div class="flex items-center gap-2">
								<span v-if="r.execTimeMs" class="text-[10px] text-slate-500">{{ r.execTimeMs }}ms</span>
								<span :class="['text-[10px] font-bold', r.correct ? 'text-emerald-400' : (r.verdict === 'CE' || r.verdict === 'RE' ? 'text-red-400' : 'text-orange-400')]">
									{{ verdictLabel[r.verdict] ?? r.verdict }}
								</span>
							</div>
						</div>
						<div v-if="r.errorMessage" class="mt-1 whitespace-pre-wrap text-[10px] text-red-400">{{ r.errorMessage }}</div>
						<div v-else class="grid grid-cols-2 gap-2 text-[10px]">
							<div>
								<p class="mb-0.5 text-slate-500">출력</p>
								<pre class="whitespace-pre-wrap break-all rounded bg-slate-900 p-1.5 text-green-300">{{ r.actualOutput || '(없음)' }}</pre>
							</div>
							<div>
								<p class="mb-0.5 text-slate-500">기댓값</p>
								<pre class="whitespace-pre-wrap break-all rounded bg-slate-900 p-1.5 text-slate-400">{{ r.expectedOutput }}</pre>
							</div>
						</div>
					</div>
				</div>

				<!-- 최근 채점 결과 -->
				<div v-if="result" :class="['mb-3 rounded-lg border p-3', resultStyle.border]">
					<p :class="['text-xs font-bold', resultStyle.text]">{{ verdictLabel[result.verdict] ?? result.verdict }}</p>
					<div class="mt-1.5 flex flex-wrap gap-3 text-[10px] text-slate-400">
						<span v-if="result.execTimeMs">실행 시간: {{ result.execTimeMs }}ms</span>
						<span v-if="result.memoryKb">메모리: {{ result.memoryKb }}KB</span>
					</div>
					<p v-if="result.errorMessage" class="mt-1.5 whitespace-pre-wrap text-[11px] text-red-400">{{ result.errorMessage }}</p>
				</div>

				<!-- 버튼 영역 -->
				<div class="flex gap-2">
					<button
						class="flex h-9 flex-1 items-center justify-center gap-2 rounded-md bg-slate-700 text-xs font-semibold text-white transition hover:bg-slate-600 disabled:opacity-50"
						:disabled="running || submitting || !code.trim()"
						@click="handleRun"
					>
						<svg v-if="running" class="h-3.5 w-3.5 animate-spin" viewBox="0 0 24 24" fill="none">
							<circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
							<path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v4a4 4 0 00-4 4H4z"/>
						</svg>
						{{ running ? '실행 중...' : '실행' }}
					</button>
					<button
						class="flex h-9 flex-1 items-center justify-center gap-2 rounded-md bg-blue-600 text-xs font-semibold text-white transition hover:bg-blue-700 disabled:opacity-50"
						:disabled="submitting || running || !code.trim()"
						@click="handleSubmit"
					>
						<svg v-if="submitting" class="h-3.5 w-3.5 animate-spin" viewBox="0 0 24 24" fill="none">
							<circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"/>
							<path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v4a4 4 0 00-4 4H4z"/>
						</svg>
						{{ submitting ? '채점 중...' : '제출하기' }}
					</button>
				</div>
			</div>
		</div>
	</div>
</template>

<script setup>
import { onBeforeUnmount, onMounted, ref, computed } from "vue";
import { useRoute } from "vue-router";
import { getCodingProblemDetail, submitCode, runCode, deleteCodingProblem, submitReport, getReports, resolveReport, getAllTestCases, updateTestCase } from "@/api/index.js";
import { useAuthStore } from "@/stores/auth";
import { useRouter } from "vue-router";

const authStore = useAuthStore();

const route = useRoute();
const router = useRouter();

const problem = ref(null);
const loading = ref(true);
const error = ref(null);
const language = ref("PYTHON");
const code = ref("");
const submitting = ref(false);
const running = ref(false);
const result = ref(null);
const runResults = ref(null);
const history = ref([]);
const showHistory = ref(false);
const editorRef = ref(null);
const showEditor = ref(true);
const containerRef = ref(null);
const leftPaneWidth = ref(50);
const isResizing = ref(false);

// 오류 신고
const showReportForm = ref(false);
const reportContent = ref("");
const reporting = ref(false);

// 관리자 패널
const reports = ref([]);
const testCases = ref([]);
const showAdminPanel = ref(false);
const editingTc = ref(null); // { testCaseId, inputData, expectedOutput }
const savingTc = ref(false);

const difficultyColor = { EASY: "bg-emerald-500", MEDIUM: "bg-orange-500", HARD: "bg-red-500" };
const difficultyLabel = { EASY: "초급", MEDIUM: "중급", HARD: "상급" };
const verdictLabel = {
	AC: "정답입니다!", WA: "오답입니다", TLE: "시간 초과",
	MLE: "메모리 초과", CE: "컴파일 오류", RE: "런타임 오류",
};
const verdictColor = {
	AC: "text-emerald-400 font-semibold",
	WA: "text-orange-400 font-semibold",
	TLE: "text-orange-400 font-semibold",
	MLE: "text-orange-400 font-semibold",
	CE: "text-red-400 font-semibold",
	RE: "text-red-400 font-semibold",
};

const resultStyle = computed(() => {
	if (!result.value) return {};
	const v = result.value.verdict;
	if (v === "AC") return { border: "border-emerald-700 bg-emerald-900/40", text: "text-emerald-400" };
	if (v === "CE" || v === "RE") return { border: "border-red-700 bg-red-900/40", text: "text-red-400" };
	return { border: "border-orange-700 bg-orange-900/40", text: "text-orange-400" };
});

const clampPaneWidth = (value) => Math.min(75, Math.max(25, value));

const updatePaneWidth = (clientX) => {
	const container = containerRef.value;
	if (!container) return;
	const rect = container.getBoundingClientRect();
	if (rect.width <= 0) return;
	const ratio = ((clientX - rect.left) / rect.width) * 100;
	leftPaneWidth.value = clampPaneWidth(ratio);
};

const handleResizeMove = (event) => {
	if (!isResizing.value) return;
	updatePaneWidth(event.clientX);
};

const stopResize = () => {
	if (!isResizing.value) return;
	isResizing.value = false;
	window.removeEventListener("pointermove", handleResizeMove);
	window.removeEventListener("pointerup", stopResize);
	document.body.style.cursor = "";
	document.body.style.userSelect = "";
};

const startResize = (event) => {
	if (!showEditor.value) return;
	isResizing.value = true;
	updatePaneWidth(event.clientX);
	document.body.style.cursor = "col-resize";
	document.body.style.userSelect = "none";
	window.addEventListener("pointermove", handleResizeMove);
	window.addEventListener("pointerup", stopResize);
};

const handleTab = () => {
	const el = editorRef.value;
	const start = el.selectionStart;
	const end = el.selectionEnd;
	const indent = "    "; // 4 spaces
	code.value = code.value.substring(0, start) + indent + code.value.substring(end);
	// 커서를 들여쓰기 뒤로 이동
	requestAnimationFrame(() => {
		el.selectionStart = el.selectionEnd = start + indent.length;
	});
};

const loadProblem = async () => {
	loading.value = true;
	error.value = null;
	try {
		const res = await getCodingProblemDetail(route.params.id);
		problem.value = res.data.data;
	} catch {
		error.value = "문제를 불러오지 못했습니다.";
	} finally {
		loading.value = false;
	}
};

const handleDelete = async () => {
	if (!confirm("이 문제를 삭제할까요?")) return;
	try {
		await deleteCodingProblem(route.params.id);
		router.push({ name: "problem" });
	} catch {
		alert("삭제에 실패했습니다.");
	}
};

const handleReport = async () => {
	if (!reportContent.value.trim()) return;
	reporting.value = true;
	try {
		await submitReport(route.params.id, { userId: authStore.userId, content: reportContent.value });
		reportContent.value = "";
		showReportForm.value = false;
		alert("신고가 접수되었습니다.");
	} catch {
		alert("신고 제출에 실패했습니다.");
	} finally {
		reporting.value = false;
	}
};

const loadAdminPanel = async () => {
	showAdminPanel.value = !showAdminPanel.value;
	if (!showAdminPanel.value) return;
	const [r, tc] = await Promise.all([
		getReports(route.params.id),
		getAllTestCases(route.params.id),
	]);
	reports.value = r.data.data;
	testCases.value = tc.data.data;
};

const handleResolve = async (reportId) => {
	await resolveReport(reportId);
	reports.value = reports.value.map(r => r.reportId === reportId ? { ...r, status: "RESOLVED" } : r);
};

const startEdit = (tc) => {
	editingTc.value = { testCaseId: tc.testCaseId, inputData: tc.inputData, expectedOutput: tc.expectedOutput };
};

const saveEdit = async () => {
	if (!editingTc.value) return;
	savingTc.value = true;
	try {
		await updateTestCase(editingTc.value.testCaseId, {
			inputData: editingTc.value.inputData,
			expectedOutput: editingTc.value.expectedOutput,
		});
		testCases.value = testCases.value.map(tc =>
			tc.testCaseId === editingTc.value.testCaseId ? { ...tc, ...editingTc.value } : tc
		);
		editingTc.value = null;
	} catch {
		alert("저장에 실패했습니다.");
	} finally {
		savingTc.value = false;
	}
};

const handleRun = async () => {
	if (!code.value.trim()) return;
	running.value = true;
	runResults.value = null;
	try {
		const res = await runCode(route.params.id, {
			language: language.value,
			sourceCode: code.value,
		});
		runResults.value = res.data.data;
	} catch {
		alert("실행에 실패했습니다.");
	} finally {
		running.value = false;
	}
};

const handleSubmit = async () => {
	if (!code.value.trim()) return;
	submitting.value = true;
	result.value = null;
	try {
		const res = await submitCode(route.params.id, {
			userId: authStore.userId,
			language: language.value,
			sourceCode: code.value,
		});
		result.value = res.data.data;
		// 이력에 추가 (최신순, 최대 10개)
		const now = new Date();
		const timeStr = `${String(now.getHours()).padStart(2, "0")}:${String(now.getMinutes()).padStart(2, "0")}`;
		history.value.unshift({
			verdict: result.value.verdict,
			language: language.value,
			execTimeMs: result.value.execTimeMs,
			time: timeStr,
		});
		if (history.value.length > 10) history.value.pop();
		showHistory.value = true;
	} catch {
		alert("제출에 실패했습니다.");
	} finally {
		submitting.value = false;
	}
};

onMounted(loadProblem);
onBeforeUnmount(stopResize);
</script>

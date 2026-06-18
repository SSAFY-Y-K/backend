<template>
	<div class="flex h-[calc(100vh-3rem)] overflow-hidden">
		<!-- 왼쪽: 문제 설명 (스크롤) -->
		<div class="flex w-1/2 flex-col overflow-y-auto border-r border-slate-200 bg-white">
			<div v-if="loading" class="flex flex-1 items-center justify-center text-xs text-slate-400">
				불러오는 중...
			</div>
			<div v-else-if="error" class="flex flex-1 items-center justify-center text-xs text-red-400">
				{{ error }}
			</div>

			<div v-else-if="problem" class="space-y-0">
				<!-- 헤더 -->
				<div class="border-b border-slate-100 px-6 py-4">
					<button
						class="mb-3 text-xs text-slate-400 transition hover:text-slate-600"
						@click="$router.push({ name: 'problem' })"
					>
						← 목록으로
					</button>
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
				<div v-if="problem.sampleTestCases?.length" class="px-6 py-4">
					<h4 class="mb-3 text-xs font-semibold uppercase tracking-wide text-slate-400">샘플 테스트케이스</h4>
					<div class="space-y-3">
						<div v-for="(tc, i) in problem.sampleTestCases" :key="i" class="grid grid-cols-2 gap-2">
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
			</div>
		</div>

		<!-- 오른쪽: 에디터 -->
		<div class="flex w-1/2 flex-col bg-slate-900">
			<!-- 툴바 -->
			<div class="flex items-center justify-between border-b border-slate-700 px-4 py-2">
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

				<!-- 최근 채점 결과 -->
				<div v-if="result" :class="['mb-3 rounded-lg border p-3', resultStyle.border]">
					<p :class="['text-xs font-bold', resultStyle.text]">{{ verdictLabel[result.verdict] ?? result.verdict }}</p>
					<div class="mt-1.5 flex flex-wrap gap-3 text-[10px] text-slate-400">
						<span v-if="result.execTimeMs">실행 시간: {{ result.execTimeMs }}ms</span>
						<span v-if="result.memoryKb">메모리: {{ result.memoryKb }}KB</span>
					</div>
					<p v-if="result.errorMessage" class="mt-1.5 whitespace-pre-wrap text-[11px] text-red-400">{{ result.errorMessage }}</p>
				</div>

				<!-- 제출 버튼 -->
				<button
					class="flex h-9 w-full items-center justify-center gap-2 rounded-md bg-blue-600 text-xs font-semibold text-white transition hover:bg-blue-700 disabled:opacity-50"
					:disabled="submitting || !code.trim()"
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
</template>

<script setup>
import { onMounted, ref, computed } from "vue";
import { useRoute } from "vue-router";
import { getCodingProblemDetail, submitCode } from "@/api/index.js";

const route = useRoute();

const problem = ref(null);
const loading = ref(true);
const error = ref(null);
const language = ref("PYTHON");
const code = ref("");
const submitting = ref(false);
const result = ref(null);
const history = ref([]);
const showHistory = ref(false);
const editorRef = ref(null);

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
		problem.value = res.data ?? res;
	} catch {
		error.value = "문제를 불러오지 못했습니다.";
	} finally {
		loading.value = false;
	}
};

const handleSubmit = async () => {
	if (!code.value.trim()) return;
	submitting.value = true;
	result.value = null;
	try {
		const res = await submitCode(route.params.id, {
			userId: 1,
			language: language.value,
			sourceCode: code.value,
		});
		result.value = res.data ?? res;
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
</script>

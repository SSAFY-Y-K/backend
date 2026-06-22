<template>
	<div class="max-w-2xl">
		<div class="mb-3 flex items-center gap-2 rounded-lg border border-purple-100 bg-purple-50 px-4 py-2.5">
			<svg class="h-4 w-4 shrink-0 text-purple-400" viewBox="0 0 16 16" fill="none" stroke="currentColor" stroke-width="1.6">
				<circle cx="8" cy="8" r="6" />
				<path d="M8 5v3.5l2 1.5" stroke-linecap="round" />
			</svg>
			<p class="text-xs text-purple-700">코딩 문제는 AI가 자동으로 생성합니다.</p>
		</div>
		<p class="mb-3 px-1 text-xs font-semibold text-red-500">
			생성 요청 후 1분 이상 소요될 수 있습니다.
		</p>

		<div class="rounded-lg border border-slate-200 bg-white p-5 shadow-sm">
			<div class="space-y-4">
				<div>
					<label class="mb-1 block text-xs font-medium text-slate-500">알고리즘 카테고리</label>
					<select v-model="codingCategory" class="h-9 w-full rounded-md border border-slate-200 bg-slate-50 px-3 text-xs text-slate-700 outline-none transition focus:border-blue-400 focus:bg-white">
						<option value="구현">구현</option>
						<option value="dp">DP (동적 프로그래밍)</option>
						<option value="graph">그래프</option>
						<option value="정렬">정렬</option>
						<option value="이분탐색">이분 탐색</option>
						<option value="greedy">그리디</option>
						<option value="bfs">BFS / DFS</option>
						<option value="string">문자열</option>
					</select>
				</div>

				<div>
					<label class="mb-2 block text-xs font-medium text-slate-500">난이도</label>
					<div class="flex gap-3">
						<label class="flex cursor-pointer items-center gap-2 text-xs text-slate-700">
							<input v-model="codingLevel" type="radio" value="EASY" class="accent-purple-600" />
							초급
						</label>
						<label class="flex cursor-pointer items-center gap-2 text-xs text-slate-700">
							<input v-model="codingLevel" type="radio" value="MEDIUM" class="accent-purple-600" />
							중급
						</label>
						<label class="flex cursor-pointer items-center gap-2 text-xs text-slate-700">
							<input v-model="codingLevel" type="radio" value="HARD" class="accent-purple-600" />
							상급
						</label>
					</div>
				</div>
			</div>

			<div
				v-if="generating"
				class="mt-5 overflow-hidden rounded-xl border border-purple-200 bg-linear-to-r from-purple-50 via-white to-fuchsia-50"
			>
				<div class="flex items-start gap-4 px-4 py-4">
					<div class="relative mt-0.5 flex h-10 w-10 shrink-0 items-center justify-center">
						<div class="absolute inset-0 rounded-full border-2 border-purple-200"></div>
						<div class="absolute inset-0 rounded-full border-2 border-transparent border-t-purple-500 animate-spin"></div>
						<div class="h-2 w-2 rounded-full bg-purple-500 shadow-[0_0_18px_rgba(168,85,247,0.45)]"></div>
					</div>
					<div class="min-w-0 flex-1">
						<div class="flex items-center gap-2">
							<p class="text-sm font-bold text-slate-800">AI가 코딩 문제를 생성하고 있습니다</p>
							<div class="flex items-center gap-1">
								<span class="h-1.5 w-1.5 rounded-full bg-purple-400 animate-bounce [animation-delay:-0.3s]"></span>
								<span class="h-1.5 w-1.5 rounded-full bg-purple-400 animate-bounce [animation-delay:-0.15s]"></span>
								<span class="h-1.5 w-1.5 rounded-full bg-purple-400 animate-bounce"></span>
							</div>
						</div>
						<p class="mt-1 text-xs font-medium text-purple-700">
							{{ generatingMessage }}
						</p>
						<div class="mt-3 grid grid-cols-3 gap-2 text-[11px] text-slate-500">
							<div class="rounded-lg border border-white/80 bg-white/80 px-3 py-2 shadow-sm">
								<p class="font-semibold text-slate-700">진행 시간</p>
								<p class="mt-0.5 text-xs font-bold text-purple-600">{{ elapsedLabel }}</p>
							</div>
							<div class="rounded-lg border border-white/80 bg-white/80 px-3 py-2 shadow-sm">
								<p class="font-semibold text-slate-700">작업 단계</p>
								<p class="mt-0.5 text-xs font-bold text-slate-600">{{ currentPhase }}</p>
							</div>
							<div class="rounded-lg border border-white/80 bg-white/80 px-3 py-2 shadow-sm">
								<p class="font-semibold text-slate-700">안내</p>
								<p class="mt-0.5 text-xs font-bold text-red-500">1분 이상 가능</p>
							</div>
						</div>
						<div class="mt-3 h-2 overflow-hidden rounded-full bg-purple-100">
							<div class="h-full w-1/3 rounded-full bg-linear-to-r from-purple-400 via-fuchsia-400 to-purple-500 animate-pulse"></div>
						</div>
						<p class="mt-2 text-[11px] text-slate-500">
							문제 생성, 테스트케이스 구성, 정답 검증까지 순차적으로 진행됩니다. 창을 닫지 말고 잠시만 기다려주세요.
						</p>
					</div>
				</div>
			</div>

			<div class="mt-5 flex justify-end border-t border-slate-100 pt-4">
				<button
					class="h-9 rounded-md bg-purple-600 px-5 text-xs font-semibold text-white transition hover:bg-purple-700 disabled:opacity-50"
					:disabled="generating"
					@click="handleGenerateCoding"
				>
					{{ generating ? 'AI 생성 중...' : 'AI 코딩 문제 생성' }}
				</button>
			</div>
		</div>

		<div v-if="generateResult" class="mt-3 rounded-lg border border-emerald-200 bg-emerald-50 p-4 text-xs text-emerald-700">
			문제가 생성되었습니다! &nbsp;
			<RouterLink
				:to="{ name: 'coding-problem-detail', params: { id: generateResult.problemId } }"
				class="font-semibold underline"
			>
				{{ generateResult.title }} 풀기 →
			</RouterLink>
		</div>
	</div>
</template>

<script setup>
import { computed, onUnmounted, ref } from "vue";
import { generateCodingProblem } from "@/api/index.js";

const codingLevel = ref("MEDIUM");
const codingCategory = ref("구현");
const generating = ref(false);
const generateResult = ref(null);
const elapsedSeconds = ref(0);

const phaseMessages = [
	"문제 구조를 설계하는 중",
	"제약 조건과 난이도를 조정하는 중",
	"테스트케이스를 생성하고 검증하는 중",
	"정답 코드와 출력 결과를 확인하는 중",
];

let generatingTimerId = null;

const currentPhase = computed(() => {
	const index = Math.floor(elapsedSeconds.value / 6) % phaseMessages.length;
	return phaseMessages[index];
});

const elapsedLabel = computed(() => {
	const minutes = Math.floor(elapsedSeconds.value / 60);
	const seconds = elapsedSeconds.value % 60;

	if (minutes === 0) {
		return `${seconds}초`;
	}

	return `${minutes}분 ${seconds.toString().padStart(2, "0")}초`;
});

const generatingMessage = computed(() =>
	elapsedSeconds.value >= 60
		? "예상보다 조금 더 걸리고 있습니다. 검증 단계까지 진행 중일 수 있어요."
		: "AI 서버가 응답을 준비 중입니다. 잠시만 기다려주세요.",
);

const startGeneratingTimer = () => {
	elapsedSeconds.value = 0;
	generatingTimerId = window.setInterval(() => {
		elapsedSeconds.value += 1;
	}, 1000);
};

const stopGeneratingTimer = () => {
	if (generatingTimerId !== null) {
		window.clearInterval(generatingTimerId);
		generatingTimerId = null;
	}
};

const handleGenerateCoding = async () => {
	generating.value = true;
	generateResult.value = null;
	startGeneratingTimer();
	try {
		const res = await generateCodingProblem({ difficulty: codingLevel.value, category: codingCategory.value });
		generateResult.value = res.data.data;
	} catch {
		alert("문제 생성에 실패했습니다. AI 서버가 실행 중인지 확인하세요.");
	} finally {
		generating.value = false;
		stopGeneratingTimer();
	}
};

onUnmounted(() => {
	stopGeneratingTimer();
});
</script>

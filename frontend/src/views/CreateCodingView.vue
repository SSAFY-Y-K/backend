<template>
	<div class="max-w-2xl">
		<div class="mb-3 flex items-center gap-2 rounded-lg border border-purple-100 bg-purple-50 px-4 py-2.5">
			<svg class="h-4 w-4 shrink-0 text-purple-400" viewBox="0 0 16 16" fill="none" stroke="currentColor" stroke-width="1.6">
				<circle cx="8" cy="8" r="6" />
				<path d="M8 5v3.5l2 1.5" stroke-linecap="round" />
			</svg>
			<p class="text-xs text-purple-700">코딩 문제는 AI가 자동으로 생성합니다.</p>
		</div>

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
import { ref } from "vue";
import { generateCodingProblem } from "@/api/index.js";

const codingLevel = ref("MEDIUM");
const codingCategory = ref("구현");
const generating = ref(false);
const generateResult = ref(null);

const handleGenerateCoding = async () => {
	generating.value = true;
	generateResult.value = null;
	try {
		const res = await generateCodingProblem({ difficulty: codingLevel.value, category: codingCategory.value });
		generateResult.value = res.data.data;
	} catch {
		alert("문제 생성에 실패했습니다. AI 서버가 실행 중인지 확인하세요.");
	} finally {
		generating.value = false;
	}
};
</script>

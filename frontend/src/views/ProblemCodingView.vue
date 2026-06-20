<template>
	<div>
		<div class="mb-3 flex gap-2">
			<select
				v-model="filterDifficulty"
				class="h-7 rounded-md border border-slate-200 bg-white px-2 text-xs text-slate-600 outline-none focus:border-purple-400"
			>
				<option value="">전체 난이도</option>
				<option value="EASY">초급</option>
				<option value="MEDIUM">중급</option>
				<option value="HARD">상급</option>
			</select>
			<select
				v-model="filterCategory"
				class="h-7 rounded-md border border-slate-200 bg-white px-2 text-xs text-slate-600 outline-none focus:border-purple-400"
			>
				<option value="">전체 유형</option>
				<option v-for="cat in categoryOptions" :key="cat" :value="cat">{{ cat }}</option>
			</select>
			<span class="ml-auto text-[10px] text-slate-400 self-center">{{ filteredCodingProblems.length }}문제</span>
		</div>

		<div v-if="codingLoading" class="py-10 text-center text-xs text-slate-400">불러오는 중...</div>

		<div v-else-if="codingError" class="py-10 text-center text-xs text-red-400">{{ codingError }}</div>

		<div v-else-if="filteredCodingProblems.length === 0" class="flex flex-col items-center justify-center py-16">
			<div class="flex h-14 w-14 items-center justify-center rounded-full bg-purple-50">
				<svg class="h-7 w-7 text-purple-300" viewBox="0 0 28 28" fill="none" stroke="currentColor" stroke-width="1.6">
					<path d="M8 10l-4 4 4 4M20 10l4 4-4 4" stroke-linecap="round" stroke-linejoin="round" />
					<path d="M16 6l-4 16" stroke-linecap="round" />
				</svg>
			</div>
			<p class="mt-3 text-sm font-semibold text-slate-600">등록된 코딩 문제가 없습니다</p>
			<p class="mt-1 text-xs text-slate-400">만들기 탭에서 AI로 문제를 생성해보세요</p>
		</div>

		<div v-else class="space-y-2">
			<div
				v-for="problem in filteredCodingProblems"
				:key="problem.problemId"
				class="flex cursor-pointer items-center justify-between rounded-lg border border-slate-200 bg-white p-3 shadow-sm transition hover:shadow-md"
				@click="$router.push({ name: 'coding-problem-detail', params: { id: problem.problemId } })"
			>
				<div class="min-w-0">
					<p class="text-xs font-semibold text-slate-800">{{ problem.title }}</p>
					<p class="mt-0.5 text-[10px] text-slate-400">{{ problem.category }}</p>
				</div>
				<div class="ml-3 flex shrink-0 items-center gap-2">
					<span v-if="problem.totalSubmissions > 0" class="text-[10px] text-slate-400">
						정답률 {{ Math.round((problem.acCount / problem.totalSubmissions) * 100) }}%
					</span>
					<span :class="['rounded px-1.5 py-0.5 text-[10px] font-bold text-white', difficultyColor[problem.difficulty]]">
						{{ difficultyLabel[problem.difficulty] ?? problem.difficulty }}
					</span>
				</div>
			</div>
		</div>
	</div>
</template>

<script setup>
import { computed, onMounted, ref } from "vue";
import { getCodingProblems } from "@/api/index.js";

const codingProblems = ref([]);
const codingLoading = ref(false);
const codingError = ref(null);
const filterDifficulty = ref("");
const filterCategory = ref("");

const categoryOptions = computed(() => {
	const set = new Set(codingProblems.value.map((p) => p.category).filter(Boolean));
	return [...set].sort();
});

const filteredCodingProblems = computed(() => {
	return codingProblems.value.filter((p) => {
		if (filterDifficulty.value && p.difficulty !== filterDifficulty.value) return false;
		if (filterCategory.value && p.category !== filterCategory.value) return false;
		return true;
	});
});

onMounted(async () => {
	codingLoading.value = true;
	codingError.value = null;
	try {
		const res = await getCodingProblems();
		codingProblems.value = res.data.data ?? [];
	} catch {
		codingError.value = "문제를 불러오지 못했습니다.";
	} finally {
		codingLoading.value = false;
	}
});

const difficultyColor = { EASY: "bg-emerald-500", MEDIUM: "bg-orange-500", HARD: "bg-red-500" };
const difficultyLabel = { EASY: "초급", MEDIUM: "중급", HARD: "상급" };
</script>

<template>
	<section class="min-h-full bg-slate-50">
		<div class="mx-auto max-w-[1600px] px-6 py-6">
			<header class="mb-6">
				<h1 class="text-3xl font-bold tracking-tight text-slate-900">문제 풀이</h1>
				<p class="mt-2 text-sm text-slate-500">
					자격증과 난이도에 맞는 문제를 골라 바로 풀어보세요.
				</p>
			</header>

			<div class="mb-6 flex flex-col gap-3 md:flex-row md:items-center">
				<input
					v-model="keyword"
					type="search"
					placeholder="문제 검색..."
					class="h-10 w-full rounded-md border border-slate-300 bg-white px-4 text-sm text-slate-900 outline-none transition placeholder:text-slate-400 focus:border-blue-500 focus:ring-2 focus:ring-blue-100 md:w-72"
				/>

				<select
					v-model="selectedCert"
					class="h-10 rounded-md border border-slate-300 bg-white px-3 text-sm font-medium text-slate-700 outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-100 md:w-40"
				>
					<option value="all">모든 자격증</option>
					<option v-for="cert in certifications" :key="cert" :value="cert">
						{{ cert }}
					</option>
				</select>

				<select
					v-model="selectedLevel"
					class="h-10 rounded-md border border-slate-300 bg-white px-3 text-sm font-medium text-slate-700 outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-100 md:w-36"
				>
					<option value="all">모든 난이도</option>
					<option v-for="level in levels" :key="level" :value="level">
						{{ level }}
					</option>
				</select>

				<button
					type="button"
					class="inline-flex h-10 items-center justify-center rounded-md border border-blue-300 bg-white px-4 text-sm font-medium text-blue-600 transition hover:bg-blue-50"
					@click="resetFilters"
				>
					필터 초기화
				</button>
			</div>

			<div class="grid gap-4 lg:grid-cols-2">
				<article
					v-for="problem in filteredProblems"
					:key="problem.id"
					class="rounded-lg border border-slate-200 bg-white p-5 shadow-sm"
				>
					<div class="flex h-full flex-col gap-5 sm:flex-row sm:items-center sm:justify-between">
						<div class="min-w-0">
							<h2 class="text-lg font-semibold text-slate-900">
								{{ problem.title }}
							</h2>

							<div class="mt-3 flex flex-wrap gap-2">
								<span
									class="inline-flex items-center rounded-full border border-blue-200 px-2.5 py-0.5 text-xs font-medium text-blue-600"
								>
									{{ problem.certification }}
								</span>
								<span
									:class="[
										'inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-semibold text-white',
										levelColorMap[problem.level],
									]"
								>
									{{ problem.level }}
								</span>
								<span
									class="inline-flex items-center rounded-full bg-slate-100 px-2.5 py-0.5 text-xs font-medium text-slate-600"
								>
									{{ problem.type }}
								</span>
							</div>

							<p class="mt-5 text-sm text-slate-500">
								{{ problem.solvedCount }}명 풀이 · 정답률 {{ problem.correctRate }}%
							</p>
						</div>

						<button
							type="button"
							class="inline-flex h-9 shrink-0 items-center justify-center rounded-md bg-blue-600 px-5 text-sm font-semibold text-white shadow-sm transition hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-200"
						>
							풀기
						</button>
					</div>
				</article>
			</div>

			<div
				v-if="filteredProblems.length === 0"
				class="rounded-lg border border-dashed border-slate-300 bg-white px-6 py-12 text-center"
			>
				<p class="text-sm font-medium text-slate-700">조건에 맞는 문제가 없습니다.</p>
				<button
					type="button"
					class="mt-4 inline-flex h-10 items-center justify-center rounded-md border border-blue-300 px-4 text-sm font-medium text-blue-600 transition hover:bg-blue-50"
					@click="resetFilters"
				>
					전체 문제 보기
				</button>
			</div>
		</div>
	</section>
</template>

<script setup>
import { computed, ref } from "vue";

const keyword = ref("");
const selectedCert = ref("all");
const selectedLevel = ref("all");

const problems = [
	{
		id: 1,
		title: "데이터베이스 정규화 개념 문제",
		certification: "정보처리기사",
		level: "중급",
		type: "객관식",
		solvedCount: 234,
		correctRate: 67,
	},
	{
		id: 2,
		title: "Python 리스트 컴프리헨션 구현",
		certification: "정보처리기사",
		level: "초급",
		type: "코딩",
		solvedCount: 156,
		correctRate: 82,
	},
	{
		id: 3,
		title: "AWS EC2 인스턴스 유형 선택",
		certification: "AWS SA",
		level: "중급",
		type: "객관식",
		solvedCount: 89,
		correctRate: 54,
	},
	{
		id: 4,
		title: "SQL JOIN 쿼리 작성",
		certification: "SQLD",
		level: "상급",
		type: "SQL 쿼리",
		solvedCount: 45,
		correctRate: 41,
	},
];

const levelColorMap = {
	초급: "bg-emerald-600",
	중급: "bg-orange-500",
	상급: "bg-red-500",
};

const certifications = computed(() => {
	return [...new Set(problems.map((problem) => problem.certification))];
});

const levels = computed(() => {
	return [...new Set(problems.map((problem) => problem.level))];
});

const filteredProblems = computed(() => {
	const normalizedKeyword = keyword.value.trim().toLowerCase();

	return problems.filter((problem) => {
		const matchesKeyword =
			normalizedKeyword.length === 0 ||
			problem.title.toLowerCase().includes(normalizedKeyword) ||
			problem.certification.toLowerCase().includes(normalizedKeyword);
		const matchesCert =
			selectedCert.value === "all" || problem.certification === selectedCert.value;
		const matchesLevel =
			selectedLevel.value === "all" || problem.level === selectedLevel.value;

		return matchesKeyword && matchesCert && matchesLevel;
	});
});

const resetFilters = () => {
	keyword.value = "";
	selectedCert.value = "all";
	selectedLevel.value = "all";
};
</script>

<template>
	<section class="min-h-full p-4">
		<!-- 뒤로가기 -->
		<div class="mb-4 flex items-center gap-2">
			<button
				v-if="category"
				class="text-xs text-slate-400 transition hover:text-slate-600"
				@click="category = null"
			>
				← 뒤로
			</button>
			<h3 class="text-sm font-bold text-slate-700">{{ pageTitle }}</h3>
		</div>

		<!-- Step 1: 자격증 문제 vs 코딩 문제 -->
		<div v-if="!category" class="grid grid-cols-2 gap-4">
			<button
				class="group flex flex-col items-center justify-center gap-4 rounded-xl border-2 border-slate-200 bg-white p-8 shadow-sm transition hover:border-blue-300 hover:shadow-md"
				@click="category = 'cert'"
			>
				<div class="flex h-20 w-20 items-center justify-center rounded-full border-4 border-blue-100 bg-blue-50 transition group-hover:border-blue-200">
					<svg class="h-10 w-10 text-blue-500" viewBox="0 0 40 40" fill="none" stroke="currentColor" stroke-width="1.8">
						<rect x="6" y="4" width="28" height="32" rx="3" />
						<path d="M12 14h16M12 20h16M12 26h10" stroke-linecap="round" />
						<circle cx="30" cy="30" r="6" fill="white" stroke="currentColor" stroke-width="1.8" />
						<path d="M27.5 30l2 2 3-3" stroke-linecap="round" stroke-linejoin="round" />
					</svg>
				</div>
				<div class="text-center">
					<h3 class="text-base font-bold text-slate-800">자격증 문제</h3>
					<p class="mt-1 text-xs text-slate-400">객관식 · 주관식 문제를 풀어요</p>
				</div>
			</button>

			<button
				class="group flex flex-col items-center justify-center gap-4 rounded-xl border-2 border-slate-200 bg-white p-8 shadow-sm transition hover:border-purple-300 hover:shadow-md"
				@click="category = 'coding'"
			>
				<div class="flex h-20 w-20 items-center justify-center rounded-full border-4 border-purple-100 bg-purple-50 transition group-hover:border-purple-200">
					<svg class="h-10 w-10 text-purple-500" viewBox="0 0 40 40" fill="none" stroke="currentColor" stroke-width="1.8">
						<path d="M14 15l-6 5 6 5M26 15l6 5-6 5" stroke-linecap="round" stroke-linejoin="round" />
						<path d="M22 10l-4 20" stroke-linecap="round" />
					</svg>
				</div>
				<div class="text-center">
					<h3 class="text-base font-bold text-slate-800">코딩 문제</h3>
					<p class="mt-1 text-xs text-slate-400">알고리즘 코딩 문제를 풀어요</p>
				</div>
			</button>
		</div>

		<!-- Step 2a: 자격증 문제 목록 -->
		<div v-else-if="category === 'cert'">
			<div class="mb-4">
				<select class="h-7 rounded-md border border-slate-200 bg-white px-2 text-xs text-slate-600 outline-none focus:border-blue-400">
					<option value="">전체 자격증</option>
					<option>정보처리기사</option>
					<option>AWS SA</option>
					<option>SQLD</option>
					<option>정보보안기사</option>
				</select>
			</div>

			<div class="mb-3 grid grid-cols-3 gap-3">
				<div
					v-for="card in featuredCards"
					:key="card.id"
					class="overflow-hidden rounded-lg border border-slate-200 bg-white shadow-sm transition hover:shadow-md"
				>
					<div class="border-b border-slate-100 p-3">
						<h3 class="text-xs font-bold text-slate-800">{{ card.title }}</h3>
						<p class="mt-0.5 text-[10px] text-slate-400">{{ card.cert }}</p>
					</div>
					<div class="flex items-center justify-between p-2.5">
						<span class="flex items-center gap-1 text-[10px] text-slate-500">
							<span :class="['h-1.5 w-1.5 rounded-full', card.dotColor]"></span>
							{{ card.type }}
						</span>
						<RouterLink
							:to="{ name: 'problem-detail', params: { id: card.id } }"
							class="rounded border border-blue-200 px-2 py-0.5 text-[10px] text-blue-600 no-underline transition hover:bg-blue-50"
						>
							풀기
						</RouterLink>
					</div>
				</div>
			</div>

			<div class="grid grid-cols-3 gap-3">
				<div
					v-for="card in normalCards"
					:key="card.id"
					class="overflow-hidden rounded-lg border border-slate-200 bg-white shadow-sm transition hover:shadow-md"
				>
					<div class="border-b border-slate-100 p-3">
						<h3 class="text-xs font-bold text-slate-800">{{ card.title }}</h3>
						<p class="mt-0.5 text-[10px] text-slate-400">{{ card.cert }}</p>
						<p class="mt-1 text-[10px] leading-relaxed text-slate-500 line-clamp-2">{{ card.description }}</p>
					</div>
					<div class="flex items-center justify-between p-2.5">
						<span :class="['rounded px-1.5 py-0.5 text-[10px] font-bold text-white', levelColor[card.level]]">
							{{ card.level }}
						</span>
						<RouterLink
							:to="{ name: 'problem-detail', params: { id: card.id } }"
							class="rounded border border-blue-200 px-2 py-0.5 text-[10px] text-blue-600 no-underline transition hover:bg-blue-50"
						>
							풀기
						</RouterLink>
					</div>
				</div>
			</div>
		</div>

		<!-- Step 2b: 코딩 문제 목록 -->
		<div v-else-if="category === 'coding'">
			<div v-if="codingLoading" class="py-10 text-center text-xs text-slate-400">불러오는 중...</div>

			<div v-else-if="codingError" class="py-10 text-center text-xs text-red-400">{{ codingError }}</div>

			<div v-else-if="codingProblems.length === 0" class="flex flex-col items-center justify-center py-16">
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
					v-for="problem in codingProblems"
					:key="problem.problemId"
					class="flex cursor-pointer items-center justify-between rounded-lg border border-slate-200 bg-white p-3 shadow-sm transition hover:shadow-md"
					@click="$router.push({ name: 'coding-problem-detail', params: { id: problem.problemId } })"
				>
					<div class="min-w-0">
						<p class="text-xs font-semibold text-slate-800">{{ problem.title }}</p>
						<p class="mt-0.5 text-[10px] text-slate-400">{{ problem.category }}</p>
					</div>
					<span :class="['ml-3 shrink-0 rounded px-1.5 py-0.5 text-[10px] font-bold text-white', difficultyColor[problem.difficulty]]">
						{{ difficultyLabel[problem.difficulty] ?? problem.difficulty }}
					</span>
				</div>
			</div>
		</div>
	</section>
</template>

<script setup>
import { computed, ref, watch } from "vue";
import { getCodingProblems } from "@/api/index.js";

const category = ref(null);
const codingProblems = ref([]);
const codingLoading = ref(false);
const codingError = ref(null);

const pageTitle = computed(() => {
	if (!category.value) return "문제 풀기";
	if (category.value === "cert") return "자격증 문제";
	if (category.value === "coding") return "코딩 문제";
	return "문제 풀기";
});

watch(category, async (val) => {
	if (val !== "coding") return;
	codingLoading.value = true;
	codingError.value = null;
	try {
		const res = await getCodingProblems();
		codingProblems.value = res.data ?? res;
	} catch {
		codingError.value = "문제를 불러오지 못했습니다.";
	} finally {
		codingLoading.value = false;
	}
});

const difficultyColor = { EASY: "bg-emerald-500", MEDIUM: "bg-orange-500", HARD: "bg-red-500" };
const difficultyLabel = { EASY: "초급", MEDIUM: "중급", HARD: "상급" };

const levelColor = { 초급: "bg-emerald-500", 중급: "bg-orange-500", 상급: "bg-red-500" };

const featuredCards = [
	{ id: 1, title: "데이터베이스 정규화 개념", cert: "정보처리기사", type: "객관식", dotColor: "bg-blue-400" },
	{ id: 2, title: "AWS VPC 구성 설계", cert: "AWS SA", type: "객관식", dotColor: "bg-green-400" },
	{ id: 3, title: "SQL 집계 함수 활용", cert: "SQLD", type: "주관식", dotColor: "bg-orange-400" },
];

const normalCards = [
	{ id: 4, title: "운영체제 프로세스 관리", cert: "정보처리기사", level: "초급", description: "프로세스 스케줄링 및 동기화 관련 문제입니다." },
	{ id: 5, title: "EC2 인스턴스 유형 선택", cert: "AWS SA", level: "중급", description: "워크로드별 EC2 인스턴스 유형 선택 기준을 묻는 문제입니다." },
	{ id: 6, title: "네트워크 보안 프로토콜", cert: "정보보안기사", level: "상급", description: "TLS/SSL 핸드셰이크 과정과 보안 취약점 관련 문제입니다." },
];
</script>

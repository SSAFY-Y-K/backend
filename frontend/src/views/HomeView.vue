<template>
  <section class="min-h-full bg-slate-50 p-4">
    <div class="mx-auto max-w-6xl space-y-5">
      <header
        class="flex flex-col gap-3 rounded-xl border border-slate-200 bg-white p-5 shadow-sm sm:flex-row sm:items-end sm:justify-between"
      >
        <div>
          <p
            class="text-xs font-semibold uppercase tracking-wide text-blue-500"
          >
            PASSIT
          </p>
          <h1 class="mt-1 text-xl font-bold text-slate-900">
            오늘 풀 문제를 빠르게 찾아보세요
          </h1>
          <p class="mt-1 text-sm text-slate-500">
            자격증 문제와 코딩 문제를 한 곳에서 이어서 학습할 수 있습니다.
          </p>
        </div>
        <div class="flex gap-2">
          <RouterLink
            :to="{ name: 'problem' }"
            class="rounded-md border border-slate-200 bg-white px-3 py-2 text-xs font-semibold text-slate-600 no-underline transition hover:border-blue-200 hover:text-blue-600"
          >
            문제 둘러보기
          </RouterLink>
          <RouterLink
            :to="{ name: 'create' }"
            class="rounded-md bg-blue-600 px-3 py-2 text-xs font-semibold text-white no-underline transition hover:bg-blue-700"
          >
            문제 만들기
          </RouterLink>
        </div>
      </header>

      <div class="grid grid-cols-2 gap-3 lg:grid-cols-4">
        <article
          v-for="summary in summaries"
          :key="summary.label"
          class="rounded-lg border border-slate-200 bg-white p-4 shadow-sm"
        >
          <p class="text-xs font-medium text-slate-400">{{ summary.label }}</p>
          <p class="mt-2 text-2xl font-bold text-slate-900">
            {{ summary.value }}
          </p>
          <p class="mt-1 text-[11px] text-slate-400">{{ summary.caption }}</p>
        </article>
      </div>

      <div
        v-if="isLoading"
        class="rounded-lg border border-slate-200 bg-white py-10 text-center text-xs text-slate-400"
      >
        홈 데이터를 불러오는 중입니다...
      </div>

      <div
        v-else-if="loadError"
        class="rounded-lg border border-red-100 bg-red-50 px-4 py-3 text-xs text-red-500"
      >
        {{ loadError }}
      </div>

      <div class="grid gap-5 lg:grid-cols-[1.3fr_1fr]">
        <section class="rounded-lg border border-slate-200 bg-white shadow-sm">
          <div
            class="flex items-center justify-between border-b border-slate-100 px-4 py-3"
          >
            <div>
              <h2 class="text-sm font-bold text-slate-800">최근 자격증 문제</h2>
              <p class="mt-0.5 text-[11px] text-slate-400">
                객관식과 주관식 문제를 바로 풀 수 있습니다.
              </p>
            </div>
            <RouterLink
              :to="{ name: 'problem-certification' }"
              class="text-[11px] font-semibold text-blue-500 no-underline hover:text-blue-600"
            >
              전체 보기
            </RouterLink>
          </div>

          <div
            v-if="recentCertificationProblems.length === 0"
            class="px-4 py-10 text-center text-xs text-slate-400"
          >
            등록된 자격증 문제가 없습니다.
          </div>

          <div v-else class="divide-y divide-slate-100">
            <RouterLink
              v-for="problem in recentCertificationProblems"
              :key="`cert-${problem.problemId}`"
              :to="{
                name: 'problem-detail',
                params: { id: problem.problemId },
                query: { problemType: problem.problemType },
              }"
              class="flex items-center gap-3 px-4 py-3 text-slate-700 no-underline transition hover:bg-slate-50"
            >
              <span
                :class="[
                  'h-2.5 w-2.5 shrink-0 rounded-full',
                  problem.problemType === 'SHORT_ANSWER'
                    ? 'bg-emerald-500'
                    : 'bg-blue-500',
                ]"
              ></span>
              <div class="min-w-0 flex-1">
                <p class="truncate text-xs font-semibold">
                  {{
                    problem.problemTitle ??
                    problem.title ??
                    `자격증 문제 #${problem.problemId}`
                  }}
                </p>
                <p class="mt-0.5 text-[10px] text-slate-400">
                  {{ getCertificationName(problem.certId) }}
                </p>
              </div>
              <span
                :class="[
                  'rounded-full border px-2 py-1 text-[10px] font-semibold',
                  problem.problemType === 'SHORT_ANSWER'
                    ? 'border-emerald-100 bg-emerald-50 text-emerald-600'
                    : 'border-blue-100 bg-blue-50 text-blue-600',
                ]"
              >
                {{
                  problemTypeLabels[problem.problemType] ?? problem.problemType
                }}
              </span>
            </RouterLink>
          </div>
        </section>

        <section class="rounded-lg border border-slate-200 bg-white shadow-sm">
          <div
            class="flex items-center justify-between border-b border-slate-100 px-4 py-3"
          >
            <div>
              <h2 class="text-sm font-bold text-slate-800">최근 코딩 문제</h2>
              <p class="mt-0.5 text-[11px] text-slate-400">
                난이도와 카테고리별로 연습해보세요.
              </p>
            </div>
            <RouterLink
              :to="{ name: 'problem-coding' }"
              class="text-[11px] font-semibold text-blue-500 no-underline hover:text-blue-600"
            >
              전체 보기
            </RouterLink>
          </div>

          <div
            v-if="recentCodingProblems.length === 0"
            class="px-4 py-10 text-center text-xs text-slate-400"
          >
            등록된 코딩 문제가 없습니다.
          </div>

          <div v-else class="divide-y divide-slate-100">
            <RouterLink
              v-for="problem in recentCodingProblems"
              :key="`coding-${problem.problemId}`"
              :to="{
                name: 'coding-problem-detail',
                params: { id: problem.problemId },
              }"
              class="flex items-center gap-3 px-4 py-3 text-slate-700 no-underline transition hover:bg-slate-50"
            >
              <span
                :class="[
                  'rounded px-1.5 py-0.5 text-[10px] font-bold text-white',
                  difficultyColor[problem.difficulty] ?? 'bg-slate-400',
                ]"
              >
                {{
                  difficultyLabels[problem.difficulty] ??
                  problem.difficulty ??
                  "-"
                }}
              </span>
              <div class="min-w-0">
                <p class="truncate text-xs font-semibold">
                  {{ problem.title }}
                </p>
                <p class="mt-0.5 text-[10px] text-slate-400">
                  {{ problem.category ?? "카테고리 없음" }}
                </p>
              </div>
            </RouterLink>
          </div>
        </section>
      </div>

      <section class="rounded-lg border border-slate-200 bg-white shadow-sm">
        <div
          class="flex items-center justify-between border-b border-slate-100 px-4 py-3"
        >
          <div>
            <h2 class="text-sm font-bold text-slate-800">자격증 바로가기</h2>
            <p class="mt-0.5 text-[11px] text-slate-400">
              자주 푸는 자격증을 선택해 문제 목록으로 이동합니다.
            </p>
          </div>
        </div>

        <div
          v-if="certificationCards.length === 0"
          class="px-4 py-10 text-center text-xs text-slate-400"
        >
          등록된 자격증이 없습니다.
        </div>

        <div v-else class="grid gap-3 p-4 sm:grid-cols-2 lg:grid-cols-4">
          <RouterLink
            v-for="certification in certificationCards"
            :key="certification.certId"
            :to="{
              name: 'problem-certification',
              query: { cert_id: certification.certId },
            }"
            class="rounded-lg border border-slate-200 bg-slate-50 p-4 text-slate-700 no-underline transition hover:border-blue-200 hover:bg-blue-50"
          >
            <div
              class="flex h-9 w-9 items-center justify-center rounded-md bg-blue-600 text-xs font-bold text-white"
            >
              {{ getInitials(certification.name) }}
            </div>
            <p class="mt-3 truncate text-sm font-bold">
              {{ certification.name }}
            </p>
            <p class="mt-1 text-[11px] text-slate-400">
              {{ certification.problemCount }}개 문제
            </p>
          </RouterLink>
        </div>
      </section>
    </div>
  </section>
</template>

<script setup>
import { getMyStats } from "@/api/index.js";
import { publicApi } from "@/api/client";
import { useAuthStore } from "@/stores/auth";
import { useCertificationStore } from "@/stores/certification";
import { computed, onMounted, ref } from "vue";

const authStore = useAuthStore();
const certificationStore = useCertificationStore();

const certificationProblems = ref([]);
const certificationProblemCount = ref(null);
const certificationCount = ref(null);
const codingProblemCount = ref(null);
const codingProblems = ref([]);
const myStats = ref(null);
const isLoading = ref(false);
const loadError = ref("");

const problemTypeLabels = {
  MULTIPLE_CHOICE: "객관식",
  SHORT_ANSWER: "주관식",
};

const difficultyLabels = {
  EASY: "초급",
  MEDIUM: "중급",
  HARD: "고급",
};

const difficultyColor = {
  EASY: "bg-emerald-500",
  MEDIUM: "bg-orange-500",
  HARD: "bg-red-500",
};

const certifications = computed(() => certificationStore.getCertifications());

const recentCertificationProblems = computed(() =>
  certificationProblems.value.slice(0, 8),
);

const recentCodingProblems = computed(() => codingProblems.value.slice(0, 8));

const certificationCards = computed(() =>
  certifications.value.slice(0, 8),
);

const summaries = computed(() => [
  {
    label: "자격증 문제",
    value: certificationProblemCount.value ?? "-",
    caption: "전체 등록 문제",
  },
  {
    label: "코딩 문제",
    value: codingProblemCount.value ?? "-",
    caption: "전체 등록 문제",
  },
  {
    label: "자격증",
    value: certificationCount.value ?? "-",
    caption: "선택 가능한 시험",
  },
  {
    label: "내 풀이",
    value: myStats.value?.solvedCount ?? "-",
    caption: authStore.hasAccessToken ? "마이페이지 통계" : "로그인 후 확인",
  },
]);

const getCertificationName = (certId) => {
  return (
    certifications.value.find(
      (certification) => certification.certId === certId,
    )?.name ?? "자격증 문제"
  );
};

const getInitials = (name = "") => {
  const words = name.trim().split(/\s+/).filter(Boolean);
  if (words.length === 0) return "P";
  if (words.length === 1) return words[0].slice(0, 2).toUpperCase();
  return words
    .slice(0, 2)
    .map((word) => word[0])
    .join("")
    .toUpperCase();
};

const fetchCertificationProblems = async () => {
  const response = await publicApi.get("/problem/certification/recent", {
    params: {
      limit: 8,
    },
  });

  certificationProblems.value = response.data ?? [];
};

const fetchCertificationCount = async () => {
  const response = await publicApi.get("/certification/count");
  certificationCount.value = response.data;
};

const fetchCertificationProblemCount = async () => {
  const response = await publicApi.get("/problem/certification/count");
  certificationProblemCount.value = response.data;
};

const fetchCodingProblemCount = async () => {
  const response = await publicApi.get("/problems/algorithm/count");
  codingProblemCount.value = response.data;
};

const fetchRecentCodingProblems = async () => {
  const response = await publicApi.get("/problems/algorithm/recent", {
    params: {
      limit: 8,
    },
  });

  codingProblems.value = response.data ?? [];
};

const fetchMyStats = async () => {
  if (!authStore.hasAccessToken) return;

  try {
    const response = await getMyStats();
    myStats.value = response.data;
  } catch {
    myStats.value = null;
  }
};

onMounted(async () => {
  isLoading.value = true;
  loadError.value = "";

  try {
    await Promise.all([
      fetchCertificationCount(),
      fetchCertificationProblemCount(),
      fetchCodingProblemCount(),
      fetchCertificationProblems(),
      fetchRecentCodingProblems(),
      fetchMyStats(),
    ]);
  } catch {
    loadError.value =
      "홈 데이터를 불러오지 못했습니다. 잠시 후 다시 시도해주세요.";
  } finally {
    isLoading.value = false;
  }
});
</script>

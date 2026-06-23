<template>
  <section class="min-h-full p-4" style="background:linear-gradient(150deg,#f8fafc 0%,#f1f5f9 45%,#eef2ff 100%)">
    <div class="mx-auto max-w-6xl space-y-5">

      <!-- ── Hero ───────────────────────────────────────────── -->
      <header class="animate-fade-in-up relative overflow-hidden rounded-2xl bg-gradient-to-br from-[#0d1e3a] via-[#1d3461] to-[#2563eb] shadow-xl shadow-blue-900/30">
        <!-- 배경 레이어 -->
        <div class="pointer-events-none absolute inset-0">
          <div class="absolute inset-0 opacity-[0.06]" style="background-image:radial-gradient(circle,white 1.5px,transparent 1.5px);background-size:24px 24px"></div>
          <div class="absolute -right-12 -top-12 h-64 w-64 rounded-full bg-blue-400/10 blur-xl"></div>
          <div class="absolute -bottom-16 right-1/3 h-48 w-48 rounded-full bg-violet-400/10 blur-xl"></div>
          <div class="absolute -left-8 top-1/2 h-32 w-32 -translate-y-1/2 rounded-full bg-cyan-400/10 blur-lg"></div>
        </div>

        <div class="relative flex flex-col gap-6 p-7 sm:flex-row sm:items-center sm:justify-between">
          <!-- 왼쪽: 텍스트 + 버튼 -->
          <div class="max-w-sm">
            <div class="mb-3 flex items-center gap-2">
              <span class="flex h-5 w-5 items-center justify-center rounded-md bg-white/20 text-[10px] font-extrabold text-white ring-1 ring-white/25">P</span>
              <p class="text-[11px] font-bold uppercase tracking-widest text-blue-200/80">PASSIT · 학습 플랫폼</p>
            </div>
            <h1 class="text-2xl font-extrabold leading-tight text-white sm:text-3xl">
              오늘 풀 문제를<br />빠르게 찾아보세요
            </h1>
            <p class="mt-2.5 text-sm leading-relaxed text-blue-200/80">
              자격증 문제와 코딩 문제를 한 곳에서 이어서 학습하세요.
            </p>
            <div class="mt-5 flex gap-2.5">
              <RouterLink
                :to="{ name: 'problem' }"
                class="rounded-xl border border-white/25 bg-white/10 px-4 py-2 text-sm font-semibold text-white no-underline backdrop-blur-sm transition hover:bg-white/20"
              >문제 둘러보기</RouterLink>
              <RouterLink
                :to="{ name: 'create' }"
                class="rounded-xl bg-white px-4 py-2 text-sm font-bold text-blue-700 no-underline shadow-md transition hover:bg-blue-50"
              >만들기 →</RouterLink>
            </div>
          </div>

          <!-- 오른쪽: 미니 통계 카드 2×2 -->
          <div class="grid shrink-0 grid-cols-2 gap-2 sm:w-56">
            <template v-if="isLoading">
              <div v-for="i in 4" :key="i" class="h-16 rounded-xl bg-white/10 animate-pulse"></div>
            </template>
            <template v-else>
              <div
                v-for="(s, i) in heroStats"
                :key="s.label"
                class="animate-fade-in-up rounded-xl bg-white/10 p-3 text-center ring-1 ring-white/10 backdrop-blur-sm"
                :style="{ animationDelay: `${200 + i * 60}ms` }"
              >
                <p class="text-lg font-bold tabular-nums text-white">{{ s.value }}</p>
                <p class="mt-0.5 text-[10px] text-blue-200/70">{{ s.label }}</p>
              </div>
            </template>
          </div>
        </div>
      </header>

      <!-- ── 상세 통계 카드 ──────────────────────────────────── -->
      <div class="grid grid-cols-2 gap-3 lg:grid-cols-4">
        <template v-if="isLoading">
          <div v-for="i in 4" :key="i" class="skeleton h-28 rounded-xl"></div>
        </template>
        <article
          v-else
          v-for="(summary, i) in summaries"
          :key="summary.label"
          class="card-lift animate-fade-in-up relative overflow-hidden rounded-xl border border-slate-200/80 bg-white p-4 shadow-sm"
          :style="{ animationDelay: `${80 + i * 60}ms` }"
        >
          <div :class="['absolute inset-x-0 top-0 h-0.5', ['bg-blue-500','bg-purple-500','bg-amber-500','bg-emerald-500'][i]]"></div>
          <!-- 아이콘 -->
          <div :class="['flex h-9 w-9 items-center justify-center rounded-xl', summary.iconBg]">
            <svg :class="['h-[18px] w-[18px]', summary.iconColor]" viewBox="0 0 20 20" fill="none" stroke="currentColor" stroke-width="1.65" v-html="summary.icon"></svg>
          </div>
          <p class="mt-3 text-xs font-medium text-slate-400">{{ summary.label }}</p>
          <p :class="['mt-1 text-2xl font-bold tabular-nums', ['text-blue-600','text-purple-600','text-amber-600','text-emerald-600'][i]]">
            {{ summary.value }}
          </p>
          <!-- 장식용 스파크라인 -->
          <div class="mt-2.5 flex h-4 items-end gap-px">
            <div
              v-for="(h, j) in summary.sparkline"
              :key="j"
              :class="['flex-1 rounded-[2px] opacity-25 transition-opacity group-hover:opacity-40', summary.barColor]"
              :style="{ height: h + '%' }"
            ></div>
          </div>
        </article>
      </div>

      <div v-if="!isLoading && loadError" class="rounded-xl border border-red-100 bg-red-50 px-4 py-3 text-xs text-red-500">
        {{ loadError }}
      </div>

      <!-- ── Quick Start ─────────────────────────────────────── -->
      <div class="animate-fade-in-up grid gap-4 sm:grid-cols-2" style="animation-delay:280ms">
        <!-- 자격증 문제 -->
        <RouterLink
          :to="{ name: 'problem-certification' }"
          class="group relative overflow-hidden rounded-2xl bg-gradient-to-br from-blue-500 to-blue-700 p-6 text-white no-underline shadow-lg shadow-blue-500/20 transition hover:-translate-y-0.5 hover:shadow-blue-500/30"
        >
          <div class="pointer-events-none absolute -right-6 -top-6 h-28 w-28 rounded-full bg-white/10 transition group-hover:scale-110"></div>
          <div class="pointer-events-none absolute -bottom-8 -left-4 h-20 w-20 rounded-full bg-blue-400/20"></div>
          <div class="relative">
            <div class="flex h-11 w-11 items-center justify-center rounded-xl bg-white/20 ring-1 ring-white/20">
              <svg class="h-5 w-5 text-white" viewBox="0 0 20 20" fill="none" stroke="currentColor" stroke-width="1.6">
                <rect x="4" y="2" width="12" height="16" rx="2"/>
                <path d="M7 7h6M7 11h6M7 14h4" stroke-linecap="round"/>
              </svg>
            </div>
            <h3 class="mt-3 text-base font-extrabold">자격증 문제 풀기</h3>
            <p class="mt-1 text-sm text-blue-100/80">객관식·주관식으로 자격증 합격을 노려보세요</p>
            <div class="mt-4 flex items-center gap-1 text-sm font-semibold text-blue-100 transition group-hover:gap-2">
              지금 시작하기
              <svg class="h-4 w-4 transition-transform group-hover:translate-x-0.5" viewBox="0 0 16 16" fill="none" stroke="currentColor" stroke-width="1.8">
                <path d="M3 8h10M9 4l4 4-4 4" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </div>
          </div>
        </RouterLink>

        <!-- 코딩 문제 -->
        <RouterLink
          :to="{ name: 'problem-coding' }"
          class="group relative overflow-hidden rounded-2xl bg-gradient-to-br from-violet-500 to-purple-700 p-6 text-white no-underline shadow-lg shadow-purple-500/20 transition hover:-translate-y-0.5 hover:shadow-purple-500/30"
        >
          <div class="pointer-events-none absolute -right-6 -top-6 h-28 w-28 rounded-full bg-white/10 transition group-hover:scale-110"></div>
          <div class="pointer-events-none absolute -bottom-8 -left-4 h-20 w-20 rounded-full bg-violet-400/20"></div>
          <div class="relative">
            <div class="flex h-11 w-11 items-center justify-center rounded-xl bg-white/20 ring-1 ring-white/20">
              <svg class="h-5 w-5 text-white" viewBox="0 0 20 20" fill="none" stroke="currentColor" stroke-width="1.6">
                <path d="M7.5 7.5l-4 4 4 4M12.5 7.5l4 4-4 4" stroke-linecap="round" stroke-linejoin="round"/>
                <path d="M11 4.5l-2 11" stroke-linecap="round"/>
              </svg>
            </div>
            <h3 class="mt-3 text-base font-extrabold">코딩 문제 풀기</h3>
            <p class="mt-1 text-sm text-purple-100/80">알고리즘 실력을 단계적으로 키워보세요</p>
            <div class="mt-4 flex items-center gap-1 text-sm font-semibold text-purple-100 transition group-hover:gap-2">
              지금 시작하기
              <svg class="h-4 w-4 transition-transform group-hover:translate-x-0.5" viewBox="0 0 16 16" fill="none" stroke="currentColor" stroke-width="1.8">
                <path d="M3 8h10M9 4l4 4-4 4" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </div>
          </div>
        </RouterLink>
      </div>

      <!-- ── 최근 문제 ───────────────────────────────────────── -->
      <div class="grid gap-5 lg:grid-cols-[1.3fr_1fr]">

        <!-- 자격증 -->
        <section class="animate-fade-in-up overflow-hidden rounded-xl border border-slate-200/80 bg-white shadow-sm" style="animation-delay:380ms">
          <div class="flex items-center justify-between border-b border-slate-100 bg-gradient-to-r from-blue-50/60 to-transparent px-4 py-3">
            <div class="flex items-center gap-2.5">
              <div class="h-4 w-1 rounded-full bg-blue-500"></div>
              <div>
                <h2 class="text-sm font-bold text-slate-800">최근 자격증 문제</h2>
                <p class="mt-0.5 text-[11px] text-slate-400">객관식 · 주관식 풀기</p>
              </div>
            </div>
            <RouterLink :to="{ name: 'problem-certification' }" class="rounded-lg bg-blue-50 px-2.5 py-1 text-[11px] font-semibold text-blue-600 no-underline transition hover:bg-blue-100">
              전체 보기
            </RouterLink>
          </div>
          <div v-if="recentCertificationProblems.length === 0" class="px-4 py-10 text-center text-xs text-slate-400">
            등록된 자격증 문제가 없습니다.
          </div>
          <div v-else class="divide-y divide-slate-50">
            <RouterLink
              v-for="(problem, i) in recentCertificationProblems"
              :key="`cert-${problem.problemId}`"
              :to="{ name: 'problem-detail', params: { id: problem.problemId }, query: { problemType: problem.problemType } }"
              class="group flex items-center gap-3 px-4 py-2.5 text-slate-700 no-underline transition hover:bg-blue-50/40"
            >
              <span class="flex h-5 w-5 shrink-0 items-center justify-center rounded-md bg-slate-100 text-[10px] font-bold text-slate-400 transition group-hover:bg-blue-100 group-hover:text-blue-600">
                {{ i + 1 }}
              </span>
              <div :class="['h-1.5 w-1.5 shrink-0 rounded-full', problem.problemType === 'SHORT_ANSWER' ? 'bg-emerald-400' : 'bg-blue-400']"></div>
              <div class="min-w-0 flex-1">
                <p class="truncate text-xs font-semibold">
                  {{ problem.problemTitle ?? problem.title ?? `자격증 문제 #${problem.problemId}` }}
                </p>
                <p class="mt-0.5 text-[10px] text-slate-400">{{ getCertificationName(problem.certId) }}</p>
              </div>
              <span :class="['shrink-0 rounded-full border px-2 py-0.5 text-[10px] font-semibold', problem.problemType === 'SHORT_ANSWER' ? 'border-emerald-100 bg-emerald-50 text-emerald-600' : 'border-blue-100 bg-blue-50 text-blue-600']">
                {{ problemTypeLabels[problem.problemType] ?? problem.problemType }}
              </span>
              <svg class="h-3.5 w-3.5 shrink-0 text-slate-300 transition group-hover:translate-x-0.5 group-hover:text-blue-400" viewBox="0 0 14 14" fill="none" stroke="currentColor" stroke-width="1.5">
                <path d="M5 3l4 4-4 4" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </RouterLink>
          </div>
        </section>

        <!-- 코딩 -->
        <section class="animate-fade-in-up overflow-hidden rounded-xl border border-slate-200/80 bg-white shadow-sm" style="animation-delay:460ms">
          <div class="flex items-center justify-between border-b border-slate-100 bg-gradient-to-r from-purple-50/60 to-transparent px-4 py-3">
            <div class="flex items-center gap-2.5">
              <div class="h-4 w-1 rounded-full bg-purple-500"></div>
              <div>
                <h2 class="text-sm font-bold text-slate-800">최근 코딩 문제</h2>
                <p class="mt-0.5 text-[11px] text-slate-400">난이도별 알고리즘 연습</p>
              </div>
            </div>
            <RouterLink :to="{ name: 'problem-coding' }" class="rounded-lg bg-purple-50 px-2.5 py-1 text-[11px] font-semibold text-purple-600 no-underline transition hover:bg-purple-100">
              전체 보기
            </RouterLink>
          </div>
          <div v-if="recentCodingProblems.length === 0" class="px-4 py-10 text-center text-xs text-slate-400">
            등록된 코딩 문제가 없습니다.
          </div>
          <div v-else class="divide-y divide-slate-50">
            <RouterLink
              v-for="(problem, i) in recentCodingProblems"
              :key="`coding-${problem.problemId}`"
              :to="{ name: 'coding-problem-detail', params: { id: problem.problemId } }"
              class="group flex items-center gap-3 px-4 py-2.5 text-slate-700 no-underline transition hover:bg-purple-50/40"
            >
              <span class="flex h-5 w-5 shrink-0 items-center justify-center rounded-md bg-slate-100 text-[10px] font-bold text-slate-400 transition group-hover:bg-purple-100 group-hover:text-purple-600">
                {{ i + 1 }}
              </span>
              <span :class="['shrink-0 rounded-md px-1.5 py-0.5 text-[10px] font-bold text-white', difficultyColor[problem.difficulty] ?? 'bg-slate-400']">
                {{ difficultyLabels[problem.difficulty] ?? problem.difficulty ?? '-' }}
              </span>
              <div class="min-w-0 flex-1">
                <p class="truncate text-xs font-semibold">{{ problem.title }}</p>
                <p class="mt-0.5 text-[10px] text-slate-400">{{ problem.category ?? '카테고리 없음' }}</p>
              </div>
              <svg class="h-3.5 w-3.5 shrink-0 text-slate-300 transition group-hover:translate-x-0.5 group-hover:text-purple-400" viewBox="0 0 14 14" fill="none" stroke="currentColor" stroke-width="1.5">
                <path d="M5 3l4 4-4 4" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </RouterLink>
          </div>
        </section>
      </div>

      <!-- ── 자격증 바로가기 ─────────────────────────────────── -->
      <section class="animate-fade-in-up overflow-hidden rounded-xl border border-slate-200/80 bg-white shadow-sm" style="animation-delay:530ms">
        <div class="flex items-center justify-between border-b border-slate-100 bg-gradient-to-r from-amber-50/60 to-transparent px-4 py-3">
          <div class="flex items-center gap-2.5">
            <div class="h-4 w-1 rounded-full bg-amber-500"></div>
            <div>
              <h2 class="text-sm font-bold text-slate-800">자격증 바로가기</h2>
              <p class="mt-0.5 text-[11px] text-slate-400">자주 푸는 자격증으로 바로 이동합니다.</p>
            </div>
          </div>
        </div>
        <div v-if="certificationCards.length === 0" class="px-4 py-10 text-center text-xs text-slate-400">
          등록된 자격증이 없습니다.
        </div>
        <div v-else class="grid gap-3 p-4 sm:grid-cols-2 lg:grid-cols-4">
          <RouterLink
            v-for="(certification, i) in certificationCards"
            :key="certification.certId"
            :to="{ name: 'problem-certification', query: { cert_id: certification.certId } }"
            class="card-lift animate-fade-in-up group relative overflow-hidden rounded-xl border border-slate-200/80 bg-slate-50/50 p-4 text-slate-700 no-underline transition hover:border-transparent hover:bg-white hover:shadow-lg"
            :style="{ animationDelay: `${560 + i * 45}ms` }"
          >
            <div :class="['flex h-10 w-10 items-center justify-center rounded-xl bg-gradient-to-br text-sm font-bold text-white shadow-sm transition group-hover:scale-110', certColors[i % certColors.length]]">
              {{ getInitials(certification.name) }}
            </div>
            <p class="mt-3 truncate text-sm font-bold text-slate-800">{{ certification.name }}</p>
            <p class="mt-0.5 text-[11px] text-slate-400">{{ certification.problemCount }}개 문제</p>
            <!-- hover arrow -->
            <div class="absolute right-3 top-3 opacity-0 transition group-hover:opacity-100">
              <svg class="h-3.5 w-3.5 text-slate-400" viewBox="0 0 14 14" fill="none" stroke="currentColor" stroke-width="1.5">
                <path d="M3 7h8M7 3l4 4-4 4" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </div>
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
import { computed, onMounted, ref, watch } from "vue";

const useCountUp = (targetRef, duration = 900) => {
  const displayed = ref(null);
  watch(targetRef, (newVal) => {
    if (newVal === null || newVal === undefined) { displayed.value = null; return; }
    if (typeof newVal !== "number") { displayed.value = newVal; return; }
    const startVal = typeof displayed.value === "number" ? displayed.value : 0;
    const startTime = Date.now();
    const tick = () => {
      const elapsed = Date.now() - startTime;
      const progress = Math.min(elapsed / duration, 1);
      const eased = 1 - (1 - progress) ** 3;
      displayed.value = Math.round(startVal + (newVal - startVal) * eased);
      if (progress < 1) requestAnimationFrame(tick);
    };
    requestAnimationFrame(tick);
  });
  return displayed;
};

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

const animCertProbCount = useCountUp(certificationProblemCount);
const animCodingProbCount = useCountUp(codingProblemCount);
const animCertCount = useCountUp(certificationCount);
const solvedCountRaw = computed(() => myStats.value?.solvedCount ?? null);
const animSolvedCount = useCountUp(solvedCountRaw);

const certColors = [
  "from-blue-500 to-blue-700",
  "from-violet-500 to-purple-700",
  "from-emerald-500 to-emerald-700",
  "from-amber-500 to-amber-600",
  "from-pink-500 to-rose-600",
  "from-cyan-500 to-cyan-700",
  "from-orange-500 to-orange-600",
  "from-teal-500 to-teal-600",
];

const problemTypeLabels = { MULTIPLE_CHOICE: "객관식", SHORT_ANSWER: "주관식" };
const difficultyLabels  = { EASY: "초급", MEDIUM: "중급", HARD: "고급" };
const difficultyColor   = { EASY: "bg-emerald-500", MEDIUM: "bg-orange-500", HARD: "bg-red-500" };

const certifications          = computed(() => certificationStore.getCertifications());
const recentCertificationProblems = computed(() => certificationProblems.value.slice(0, 8));
const recentCodingProblems    = computed(() => codingProblems.value.slice(0, 8));
const certificationCards      = computed(() => certifications.value.slice(0, 8));

// 히어로 미니 통계
const heroStats = computed(() => [
  { label: "자격증 문제", value: animCertProbCount.value ?? "-" },
  { label: "코딩 문제",   value: animCodingProbCount.value ?? "-" },
  { label: "자격증",      value: animCertCount.value ?? "-" },
  { label: "내 풀이",     value: animSolvedCount.value ?? "-" },
]);

// 상세 통계 카드 (아이콘 + 스파크라인)
const summaries = computed(() => [
  {
    label: "자격증 문제", value: animCertProbCount.value ?? "-", caption: "전체 등록 문제",
    iconBg: "bg-blue-50", iconColor: "text-blue-500",
    icon: `<rect x="4" y="2" width="12" height="16" rx="2"/><path d="M7 7h6M7 11h6M7 14h4" stroke-linecap="round"/>`,
    barColor: "bg-blue-500", sparkline: [25, 40, 30, 60, 45, 75, 100],
  },
  {
    label: "코딩 문제", value: animCodingProbCount.value ?? "-", caption: "전체 등록 문제",
    iconBg: "bg-purple-50", iconColor: "text-purple-500",
    icon: `<path d="M7.5 7.5l-4 4 4 4M12.5 7.5l4 4-4 4" stroke-linecap="round" stroke-linejoin="round"/><path d="M11 4.5l-2 11" stroke-linecap="round"/>`,
    barColor: "bg-purple-500", sparkline: [15, 50, 35, 70, 55, 80, 100],
  },
  {
    label: "자격증", value: animCertCount.value ?? "-", caption: "선택 가능한 시험",
    iconBg: "bg-amber-50", iconColor: "text-amber-500",
    icon: `<circle cx="10" cy="8" r="4"/><path d="M6 12.5l-2 5 6-1.5 6 1.5-2-5" stroke-linecap="round" stroke-linejoin="round"/>`,
    barColor: "bg-amber-500", sparkline: [40, 40, 60, 60, 80, 80, 100],
  },
  {
    label: "내 풀이", value: animSolvedCount.value ?? "-", caption: authStore.hasAccessToken ? "마이페이지 통계" : "로그인 후 확인",
    iconBg: "bg-emerald-50", iconColor: "text-emerald-500",
    icon: `<circle cx="10" cy="10" r="7"/><path d="M7 10l2.5 2.5L13 8" stroke-linecap="round" stroke-linejoin="round"/>`,
    barColor: "bg-emerald-500", sparkline: [10, 30, 20, 55, 40, 70, 100],
  },
]);

const getCertificationName = (certId) =>
  certifications.value.find((c) => c.certId === certId)?.name ?? "자격증 문제";

const getInitials = (name = "") => {
  const words = name.trim().split(/\s+/).filter(Boolean);
  if (words.length === 0) return "P";
  if (words.length === 1) return words[0].slice(0, 2).toUpperCase();
  return words.slice(0, 2).map((w) => w[0]).join("").toUpperCase();
};

const fetchCertificationProblems = async () => {
  const res = await publicApi.get("/problem/certification/recent", { params: { limit: 8 } });
  certificationProblems.value = res.data ?? [];
};
const fetchCertificationCount = async () => {
  certificationCount.value = (await publicApi.get("/certification/count")).data;
};
const fetchCertificationProblemCount = async () => {
  certificationProblemCount.value = (await publicApi.get("/problem/certification/count")).data;
};
const fetchCodingProblemCount = async () => {
  codingProblemCount.value = (await publicApi.get("/problems/algorithm/count")).data;
};
const fetchRecentCodingProblems = async () => {
  const res = await publicApi.get("/problems/algorithm/recent", { params: { limit: 8 } });
  codingProblems.value = res.data ?? [];
};
const fetchMyStats = async () => {
  if (!authStore.hasAccessToken) return;
  try { myStats.value = (await getMyStats()).data; } catch { myStats.value = null; }
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
    loadError.value = "홈 데이터를 불러오지 못했습니다. 잠시 후 다시 시도해주세요.";
  } finally {
    isLoading.value = false;
  }
});
</script>

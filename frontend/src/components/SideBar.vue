<template>
  <aside
    class="flex w-52 shrink-0 flex-col overflow-y-auto border-r border-slate-100 bg-white"
  >
    <div class="p-3">
      <p class="mb-2 text-[10px] font-semibold uppercase tracking-wide text-slate-400">
        탐색
      </p>
      <nav class="space-y-0.5">
        <RouterLink
          v-for="item in primaryLinks"
          :key="item.name"
          :to="{ name: item.name }"
          class="flex items-center gap-2 rounded px-2 py-1.5 text-xs text-slate-600 no-underline transition hover:bg-slate-50"
          active-class="bg-blue-50 text-blue-600"
        >
          <span :class="['h-2 w-2 shrink-0 rounded-full', item.color]"></span>
          <span class="truncate">{{ item.label }}</span>
        </RouterLink>
      </nav>
    </div>

    <div class="mx-3 border-t border-slate-100"></div>

    <div class="p-3">
      <div class="mb-2 flex items-center justify-between">
        <p class="text-[10px] font-semibold uppercase tracking-wide text-slate-400">
          자격증
        </p>
        <RouterLink
          :to="{ name: 'problem-certification' }"
          class="text-[10px] font-semibold text-blue-500 no-underline hover:text-blue-600"
        >
          전체
        </RouterLink>
      </div>

      <div v-if="certifications.length === 0" class="px-2 py-2 text-[11px] text-slate-400">
        등록된 자격증이 없습니다.
      </div>

      <div v-else class="space-y-0.5">
        <RouterLink
          v-for="certification in certifications"
          :key="certification.certId"
          :to="{
            name: 'problem-certification',
            query: { cert_id: certification.certId },
          }"
          class="flex items-center gap-2 rounded px-2 py-1.5 text-xs text-slate-600 no-underline transition hover:bg-slate-50"
        >
          <span class="h-2 w-2 shrink-0 rounded-full bg-yellow-400"></span>
          <span class="flex-1 truncate">{{ certification.name }}</span>
        </RouterLink>
      </div>
    </div>

    <div class="mx-3 border-t border-slate-100"></div>

    <div class="p-3">
      <div class="mb-2 flex items-center justify-between">
        <p class="text-[10px] font-semibold uppercase tracking-wide text-slate-400">
          최근 문제
        </p>
        <button
          type="button"
          class="text-[10px] font-semibold text-slate-400 transition hover:text-slate-600"
          @click="fetchRecentProblems"
        >
          새로고침
        </button>
      </div>

      <div v-if="recentLoading" class="px-2 py-2 text-[11px] text-slate-400">
        불러오는 중...
      </div>

      <div v-else-if="recentProblems.length === 0" class="px-2 py-2 text-[11px] text-slate-400">
        표시할 문제가 없습니다.
      </div>

      <div v-else class="space-y-0.5">
        <RouterLink
          v-for="problem in recentProblems"
          :key="problem.key"
          :to="problem.to"
          class="flex items-center gap-2 rounded px-2 py-1.5 text-xs text-slate-600 no-underline transition hover:bg-slate-50"
        >
          <span :class="['h-2.5 w-2.5 shrink-0 rounded-sm', problem.color]"></span>
          <span class="truncate">{{ problem.title }}</span>
        </RouterLink>
      </div>
    </div>

    <div class="mx-3 border-t border-slate-100"></div>

    <div class="p-3">
      <p class="mb-2 text-[10px] font-semibold uppercase tracking-wide text-slate-400">
        만들기
      </p>
      <div class="space-y-0.5">
        <RouterLink
          :to="{ name: 'create-coding' }"
          class="flex items-center gap-2 rounded px-2 py-1.5 text-xs text-slate-600 no-underline transition hover:bg-slate-50"
        >
          <span class="h-2 w-2 shrink-0 rounded-full bg-purple-400"></span>
          <span class="truncate">코딩 문제 만들기</span>
        </RouterLink>
        <RouterLink
          :to="{ name: 'create-certification-write' }"
          class="flex items-center gap-2 rounded px-2 py-1.5 text-xs text-slate-600 no-underline transition hover:bg-slate-50"
        >
          <span class="h-2 w-2 shrink-0 rounded-full bg-blue-400"></span>
          <span class="truncate">자격증 문제 만들기</span>
        </RouterLink>
      </div>
    </div>

    <div
      v-if="authStore.hasAccessToken"
      class="mt-auto space-y-1 border-t border-slate-100 p-3"
    >
      <RouterLink
        :to="{ name: 'mypage' }"
        class="flex items-center gap-2 rounded px-2 py-1.5 text-xs text-slate-500 no-underline transition hover:bg-slate-50 hover:text-slate-700"
      >
        <svg
          class="h-3.5 w-3.5 shrink-0"
          viewBox="0 0 14 14"
          fill="none"
          stroke="currentColor"
          stroke-width="1.4"
        >
          <circle cx="7" cy="4.5" r="2.5" />
          <path d="M2 12c0-2.8 2.2-5 5-5s5 2.2 5 5" stroke-linecap="round" />
        </svg>
        <span class="truncate">마이페이지</span>
      </RouterLink>
      <button
        type="button"
        class="flex w-full items-center gap-2 rounded px-2 py-1.5 text-left text-xs text-slate-500 transition hover:bg-slate-50 hover:text-slate-700"
        @click="onLogout"
      >
        <svg
          class="h-3.5 w-3.5 shrink-0"
          viewBox="0 0 14 14"
          fill="none"
          stroke="currentColor"
          stroke-width="1.4"
        >
          <path
            d="M5 2.5H3.5A1.5 1.5 0 0 0 2 4v6a1.5 1.5 0 0 0 1.5 1.5H5"
            stroke-linecap="round"
          />
          <path
            d="M8 4l3 3-3 3"
            stroke-linecap="round"
            stroke-linejoin="round"
          />
          <path d="M11 7H5" stroke-linecap="round" />
        </svg>
        <span>로그아웃</span>
      </button>
    </div>

    <div v-else class="mt-auto border-t border-slate-100 p-3">
      <RouterLink
        :to="{ name: 'login' }"
        class="block rounded-md bg-blue-600 px-3 py-2 text-center text-xs font-semibold text-white no-underline transition hover:bg-blue-700"
      >
        로그인
      </RouterLink>
    </div>
  </aside>
</template>

<script setup>
import { getCodingProblems, logout } from "@/api/index.js";
import { publicApi } from "@/api/client";
import { useAuthStore } from "@/stores/auth";
import { useCertificationStore } from "@/stores/certification";
import { computed, onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import { toast } from "vue3-toastify";

const authStore = useAuthStore();
const certificationStore = useCertificationStore();
const router = useRouter();

const recentProblems = ref([]);
const recentLoading = ref(false);

const primaryLinks = [
  { name: "home", label: "홈", color: "bg-blue-500" },
  { name: "problem", label: "문제", color: "bg-emerald-500" },
  { name: "community", label: "커뮤니티", color: "bg-orange-400" },
];

const certifications = computed(() => certificationStore.getCertifications().slice(0, 8));

const fetchCertificationProblems = async () => {
  const response = await publicApi("/problem", {
    params: {
      cert_id: null,
      problem_id: null,
    },
  });

  return (response.data?.problems ?? []).slice(0, 4).map((problem) => ({
    key: `cert-${problem.problemId}`,
    title: problem.problemTitle ?? problem.title ?? `자격증 문제 #${problem.problemId}`,
    color: problem.problemType === "SHORT_ANSWER" ? "bg-emerald-500" : "bg-blue-500",
    to: {
      name: "problem-detail",
      params: { id: problem.problemId },
      query: { problemType: problem.problemType },
    },
  }));
};

const fetchCodingProblems = async () => {
  const response = await getCodingProblems();

  return (response.data?.data ?? []).slice(0, 4).map((problem) => ({
    key: `coding-${problem.problemId}`,
    title: problem.title ?? `코딩 문제 #${problem.problemId}`,
    color: "bg-purple-500",
    to: {
      name: "coding-problem-detail",
      params: { id: problem.problemId },
    },
  }));
};

const fetchRecentProblems = async () => {
  recentLoading.value = true;

  try {
    const [certProblems, codingProblems] = await Promise.all([
      fetchCertificationProblems(),
      fetchCodingProblems(),
    ]);

    recentProblems.value = [...certProblems, ...codingProblems].slice(0, 6);
  } catch {
    recentProblems.value = [];
  } finally {
    recentLoading.value = false;
  }
};

const onLogout = async () => {
  try {
    await logout();
    toast.info("로그아웃 되었습니다.");
  } catch {
    toast.info("로그아웃 되었습니다.");
  } finally {
    authStore.clearAccessToken();
    router.push({ name: "home" });
  }
};

onMounted(fetchRecentProblems);
</script>

<template>
  <aside class="flex w-60 shrink-0 flex-col overflow-y-auto border-r border-slate-200 bg-white">

    <!-- 네브바 연결 그라디언트 라인 -->
    <div class="h-0.5 w-full shrink-0 bg-gradient-to-r from-[#1d3461] via-blue-500 to-slate-100"></div>

    <!-- ── 탐색 ──────────────────────────────────── -->
    <div class="p-3 pt-4">
      <p class="mb-2 px-1 text-[10px] font-bold uppercase tracking-widest text-slate-400">탐색</p>
      <nav class="space-y-0.5">
        <RouterLink
          v-for="item in primaryLinks"
          :key="item.name"
          :to="{ name: item.name }"
          class="group flex items-center gap-3 rounded-lg border-l-2 border-l-transparent px-3 py-2.5 text-sm text-slate-600 no-underline transition hover:bg-slate-50 hover:text-slate-800"
          active-class="border-l-blue-500 bg-blue-50/80 font-medium text-blue-700"
        >
          <svg
            class="h-4 w-4 shrink-0 text-slate-400 transition group-hover:text-slate-600"
            viewBox="0 0 16 16" fill="none" stroke="currentColor" stroke-width="1.5"
            v-html="item.svgPath"
          ></svg>
          <span class="truncate">{{ item.label }}</span>
        </RouterLink>
      </nav>
    </div>

    <div class="mx-3 border-t border-slate-100"></div>

    <!-- ── 자격증 ────────────────────────────────── -->
    <div class="p-3">
      <div class="mb-2 flex items-center justify-between px-1">
        <p class="text-[10px] font-bold uppercase tracking-widest text-slate-400">자격증</p>
        <RouterLink
          :to="{ name: 'problem-certification' }"
          class="rounded-md bg-slate-100 px-1.5 py-0.5 text-[10px] font-semibold text-slate-500 no-underline transition hover:bg-blue-100 hover:text-blue-600"
        >전체</RouterLink>
      </div>

      <div v-if="certifications.length === 0" class="px-3 py-2 text-xs text-slate-400">
        등록된 자격증이 없습니다.
      </div>
      <div v-else class="space-y-0.5">
        <RouterLink
          v-for="cert in certifications"
          :key="cert.certId"
          :to="{ name: 'problem-certification', query: { cert_id: cert.certId } }"
          class="group flex items-center gap-2.5 rounded-lg px-2.5 py-2 text-slate-600 no-underline transition hover:bg-slate-50"
        >
          <span :class="['flex h-6 w-6 shrink-0 items-center justify-center rounded-md text-[9px] font-extrabold text-white shadow-sm transition group-hover:scale-105', getCertColor(cert.name)]">
            {{ getInitials(cert.name) }}
          </span>
          <span class="flex-1 truncate text-xs text-slate-600 group-hover:text-slate-800">{{ cert.name }}</span>
        </RouterLink>
      </div>
    </div>

    <div class="mx-3 border-t border-slate-100"></div>

    <!-- ── 코딩 카테고리 ─────────────────────────── -->
    <div class="p-3">
      <div class="mb-2 flex items-center justify-between px-1">
        <p class="text-[10px] font-bold uppercase tracking-widest text-slate-400">코딩 카테고리</p>
        <RouterLink
          :to="{ name: 'problem-coding' }"
          class="rounded-md bg-slate-100 px-1.5 py-0.5 text-[10px] font-semibold text-slate-500 no-underline transition hover:bg-purple-100 hover:text-purple-600"
        >전체</RouterLink>
      </div>

      <!-- 로딩 스켈레톤 -->
      <div v-if="categoryLoading" class="space-y-1.5 px-1">
        <div v-for="i in 5" :key="i" class="skeleton h-7 rounded-lg"></div>
      </div>
      <div v-else-if="codingCategories.length === 0" class="px-3 py-2 text-xs text-slate-400">
        등록된 카테고리가 없습니다.
      </div>
      <div v-else class="space-y-0.5">
        <RouterLink
          v-for="category in codingCategories"
          :key="category.name"
          :to="{ name: 'problem-coding', query: { category: category.name } }"
          class="group flex items-center gap-2.5 rounded-lg px-2.5 py-2 text-slate-600 no-underline transition hover:bg-slate-50"
        >
          <span :class="['h-2 w-2 shrink-0 rounded-full transition group-hover:scale-125', getCategoryColor(category.name)]"></span>
          <span class="flex-1 truncate text-xs group-hover:text-slate-800">{{ category.name }}</span>
          <span class="rounded-full bg-slate-100 px-1.5 py-0.5 text-[10px] font-semibold text-slate-500 transition group-hover:bg-purple-100 group-hover:text-purple-600">
            {{ category.count }}
          </span>
        </RouterLink>
      </div>
    </div>

    <div class="mx-3 border-t border-slate-100"></div>

    <!-- ── 만들기 ────────────────────────────────── -->
    <div class="p-3">
      <p class="mb-2 px-1 text-[10px] font-bold uppercase tracking-widest text-slate-400">만들기</p>
      <div class="space-y-2">
        <RouterLink
          :to="{ name: 'create-coding' }"
          class="flex items-center gap-2.5 rounded-xl border border-purple-100 bg-purple-50/60 px-3 py-2.5 no-underline transition hover:border-purple-300 hover:bg-purple-50 hover:shadow-sm"
        >
          <div class="flex h-7 w-7 shrink-0 items-center justify-center rounded-lg bg-purple-500/10">
            <svg class="h-4 w-4 text-purple-600" viewBox="0 0 16 16" fill="none" stroke="currentColor" stroke-width="1.5">
              <path d="M5.5 5.5L3 8l2.5 2.5M10.5 5.5L13 8l-2.5 2.5" stroke-linecap="round" stroke-linejoin="round"/>
              <path d="M9.5 3.5l-3 9" stroke-linecap="round"/>
            </svg>
          </div>
          <span class="text-xs font-semibold text-purple-700">코딩 문제 만들기</span>
        </RouterLink>

        <RouterLink
          :to="{ name: 'create-certification-write' }"
          class="flex items-center gap-2.5 rounded-xl border border-blue-100 bg-blue-50/60 px-3 py-2.5 no-underline transition hover:border-blue-300 hover:bg-blue-50 hover:shadow-sm"
        >
          <div class="flex h-7 w-7 shrink-0 items-center justify-center rounded-lg bg-blue-500/10">
            <svg class="h-4 w-4 text-blue-600" viewBox="0 0 16 16" fill="none" stroke="currentColor" stroke-width="1.5">
              <rect x="2" y="1.5" width="9.5" height="11" rx="1.5"/>
              <path d="M4.5 5h5M4.5 7.5h3.5" stroke-linecap="round"/>
              <circle cx="12" cy="13" r="2.5"/>
              <path d="M10 12l-1.5 3.5M14 12l1.5 3.5" stroke-linecap="round"/>
            </svg>
          </div>
          <span class="text-xs font-semibold text-blue-700">자격증 문제 만들기</span>
        </RouterLink>
      </div>
    </div>

    <!-- ── 하단 (로그인 상태) ──────────────────── -->
    <div v-if="authStore.hasAccessToken" class="mt-auto border-t border-slate-100 p-3 space-y-0.5">
      <RouterLink
        :to="{ name: 'mypage' }"
        class="flex items-center gap-2.5 rounded-lg px-3 py-2.5 no-underline transition hover:bg-slate-50"
      >
        <div class="flex h-7 w-7 shrink-0 items-center justify-center rounded-full bg-blue-100 ring-2 ring-blue-50">
          <svg class="h-3.5 w-3.5 text-blue-600" viewBox="0 0 14 14" fill="none" stroke="currentColor" stroke-width="1.5">
            <circle cx="7" cy="4.5" r="2.5"/>
            <path d="M2 12c0-2.8 2.2-5 5-5s5 2.2 5 5" stroke-linecap="round"/>
          </svg>
        </div>
        <span class="text-xs font-medium text-slate-600">마이페이지</span>
      </RouterLink>

      <button
        type="button"
        class="flex w-full items-center gap-2.5 rounded-lg px-3 py-2.5 text-left transition hover:bg-red-50"
        @click="onLogout"
      >
        <div class="flex h-7 w-7 shrink-0 items-center justify-center rounded-full bg-slate-100 transition group-hover:bg-red-100">
          <svg class="h-3.5 w-3.5 text-slate-500" viewBox="0 0 14 14" fill="none" stroke="currentColor" stroke-width="1.5">
            <path d="M5 2.5H3.5A1.5 1.5 0 0 0 2 4v6a1.5 1.5 0 0 0 1.5 1.5H5" stroke-linecap="round"/>
            <path d="M8 4l3 3-3 3" stroke-linecap="round" stroke-linejoin="round"/>
            <path d="M11 7H5" stroke-linecap="round"/>
          </svg>
        </div>
        <span class="text-xs font-medium text-slate-500 transition hover:text-red-600">로그아웃</span>
      </button>
    </div>

    <!-- ── 하단 (비로그인) ─────────────────────── -->
    <div v-else class="mt-auto border-t border-slate-100 p-3 space-y-2">
      <RouterLink
        :to="{ name: 'login' }"
        class="block rounded-xl bg-gradient-to-r from-blue-600 to-blue-700 px-3 py-2.5 text-center text-xs font-bold text-white no-underline shadow-sm shadow-blue-500/30 transition hover:from-blue-700 hover:to-blue-800"
      >
        로그인
      </RouterLink>
      <RouterLink
        :to="{ name: 'signup' }"
        class="block rounded-xl border border-slate-200 px-3 py-2 text-center text-xs font-medium text-slate-500 no-underline transition hover:border-blue-200 hover:bg-blue-50 hover:text-blue-600"
      >
        회원가입
      </RouterLink>
    </div>
  </aside>
</template>

<script setup>
import { logout } from "@/api/index.js";
import { publicApi } from "@/api/client";
import { useAuthStore } from "@/stores/auth";
import { useCertificationStore } from "@/stores/certification";
import { computed, onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import { toast } from "vue3-toastify";

const authStore = useAuthStore();
const certificationStore = useCertificationStore();
const router = useRouter();

const codingCategories = ref([]);
const categoryLoading = ref(false);

// 아이콘 포함 기본 탐색 링크
const primaryLinks = [
  {
    name: "home",
    label: "홈",
    svgPath: `<path d="M2 8.5L8 2.5l6 6" stroke-linecap="round" stroke-linejoin="round"/><path d="M3.5 7V13.5H6.5V10H9.5V13.5H12.5V7" stroke-linecap="round" stroke-linejoin="round"/>`,
  },
  {
    name: "problem",
    label: "문제",
    svgPath: `<rect x="3" y="1" width="10" height="14" rx="1.5"/><path d="M6 5.5h4M6 8.5h4M6 11.5h2.5" stroke-linecap="round"/>`,
  },
  {
    name: "community",
    label: "커뮤니티",
    svgPath: `<path d="M13.5 2H2.5A1.5 1.5 0 0 0 1 3.5v6A1.5 1.5 0 0 0 2.5 11H4v3l4-3h5.5a1.5 1.5 0 0 0 1.5-1.5v-6A1.5 1.5 0 0 0 13.5 2z" stroke-linecap="round" stroke-linejoin="round"/>`,
  },
];

const certifications = computed(() => certificationStore.getCertifications().slice(0, 8));

// 자격증 이름 첫 글자 기반 결정적 색상
const getCertColor = (name = "") => {
  const palette = ["bg-amber-400", "bg-orange-500", "bg-rose-400", "bg-teal-500", "bg-sky-500", "bg-violet-500"];
  return palette[(name.charCodeAt(0) ?? 0) % palette.length];
};

// 카테고리 이름 첫 글자 기반 결정적 색상
const getCategoryColor = (name = "") => {
  const palette = ["bg-purple-400", "bg-violet-500", "bg-indigo-400", "bg-fuchsia-400", "bg-blue-400"];
  return palette[(name.charCodeAt(0) ?? 0) % palette.length];
};

// 이니셜 추출
const getInitials = (name = "") => {
  const words = name.trim().split(/\s+/).filter(Boolean);
  if (words.length === 0) return "?";
  if (words.length === 1) return words[0].slice(0, 2).toUpperCase();
  return words.slice(0, 2).map((w) => w[0]).join("").toUpperCase();
};

const fetchCodingCategories = async () => {
  const response = await publicApi.get("/problems/category-count");
  codingCategories.value = (response.data ?? [])
    .map((item) => ({ name: item.category, count: item.count }))
    .filter((item) => item.name)
    .sort((a, b) => a.name.localeCompare(b.name));
};

const loadCodingCategories = async () => {
  categoryLoading.value = true;
  try {
    await fetchCodingCategories();
  } catch {
    codingCategories.value = [];
  } finally {
    categoryLoading.value = false;
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

onMounted(loadCodingCategories);
</script>

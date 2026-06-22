<template>
  <section class="min-h-full p-4">
    <!-- Tab filter + 글쓰기 -->
    <div class="mb-3 flex items-center justify-between">
      <div
        class="flex gap-0 overflow-hidden rounded-md border border-slate-200 bg-white"
      >
        <button
          v-for="tab in tabs"
          :key="tab.id"
          :class="[
            'px-4 py-1.5 text-sm font-medium transition',
            activeTab === tab.id
              ? 'bg-blue-600 text-white'
              : 'text-slate-500 hover:bg-slate-50',
          ]"
          @click="activeTab = tab.id"
        >
          {{ tab.label }}
        </button>
      </div>

      <button
        class="h-7 rounded-md bg-blue-600 px-3 text-sm font-semibold text-white transition hover:bg-blue-700"
        @click="handleWriteClick"
      >
        + 글쓰기
      </button>
    </div>

    <!-- 검색 -->
    <div class="mb-3 flex gap-2">
      <input
        v-model="searchInput"
        type="text"
        placeholder="제목 또는 내용 검색"
        class="h-8 flex-1 rounded-md border border-slate-200 bg-white px-3 text-sm text-slate-700 outline-none focus:border-blue-400"
        @keydown.enter="handleSearch"
      />
      <button
        class="h-8 rounded-md bg-slate-100 px-3 text-sm text-slate-600 hover:bg-slate-200"
        @click="handleSearch"
      >
        검색
      </button>
    </div>

    <!-- 로딩 -->
    <div v-if="loading" class="py-10 text-center text-sm text-slate-400">
      불러오는 중...
    </div>

    <!-- 에러 -->
    <div v-else-if="error" class="py-10 text-center text-sm text-red-400">
      {{ error }}
    </div>

    <!-- 게시글 목록 -->
    <div v-else class="space-y-2">
      <div
        v-if="filteredPosts.length === 0"
        class="py-10 text-center text-sm text-slate-400"
      >
        게시글이 없습니다.
      </div>
      <div
        v-for="post in filteredPosts"
        :key="post.postId"
        class="flex cursor-pointer gap-3 rounded-lg border border-slate-200 bg-white p-3 shadow-sm transition hover:shadow-md"
        @click="
          $router.push({
            name: 'community-detail',
            params: { id: post.postId },
          })
        "
      >
        <div
          class="flex h-8 w-8 shrink-0 items-center justify-center rounded-full bg-blue-100 text-sm font-bold text-blue-600"
        >
          {{ post.title.charAt(0) }}
        </div>
        <div class="min-w-0 flex-1">
          <p class="text-sm font-semibold leading-snug text-slate-800">
            {{ post.title }}
          </p>
          <div class="mt-1 flex items-center gap-3 text-xs text-slate-400">
            <span class="font-medium text-slate-500">{{
              post.nickname ?? "알 수 없음"
            }}</span>
            <span>{{ categoryLabel(post.category) }}</span>
            <span>조회 {{ post.viewCount }}</span>
            <span>{{ formatDate(post.createdAt) }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 페이지네이션 -->
    <div
      v-if="totalCount > pageSize"
      class="mt-4 flex items-center justify-center gap-1"
    >
      <button
        :disabled="currentPage === 0"
        class="h-7 rounded px-2 text-sm text-slate-500 hover:bg-slate-100 disabled:opacity-30"
        @click="goPage(currentPage - 1)"
      >
        ‹
      </button>
      <button
        v-for="p in totalPages"
        :key="p"
        :class="[
          'h-7 w-7 rounded text-sm',
          currentPage === p - 1
            ? 'bg-blue-600 text-white'
            : 'text-slate-500 hover:bg-slate-100',
        ]"
        @click="goPage(p - 1)"
      >
        {{ p }}
      </button>
      <button
        :disabled="currentPage === totalPages - 1"
        class="h-7 rounded px-2 text-sm text-slate-500 hover:bg-slate-100 disabled:opacity-30"
        @click="goPage(currentPage + 1)"
      >
        ›
      </button>
    </div>

    <!-- 글쓰기 모달 -->
    <div
      v-if="showForm"
      class="fixed inset-0 z-50 flex items-center justify-center bg-black/40 p-4"
    >
      <div
        class="w-full max-w-lg rounded-lg border border-slate-200 bg-white shadow-xl"
      >
        <div
          class="flex items-center justify-between border-b border-slate-100 px-5 py-3"
        >
          <h3 class="text-base font-bold text-slate-700">새 글 작성</h3>
          <button
            class="text-slate-400 hover:text-slate-600"
            @click="closeForm"
          >
            ✕
          </button>
        </div>
        <div class="space-y-3 p-5">
          <select
            v-model="form.category"
            class="h-9 w-full rounded-md border border-slate-200 bg-slate-50 px-3 text-sm text-slate-700 outline-none focus:border-blue-400"
          >
            <option value="">카테고리 선택</option>
            <option value="REVIEW">합격후기</option>
            <option value="TIP">공부팁</option>
            <option value="QNA">질문</option>
          </select>
          <input
            v-model="form.title"
            type="text"
            placeholder="제목"
            class="h-9 w-full rounded-md border border-slate-200 bg-slate-50 px-3 text-sm text-slate-700 outline-none focus:border-blue-400"
          />
          <textarea
            v-model="form.content"
            rows="6"
            placeholder="내용을 입력하세요"
            class="w-full resize-none rounded-md border border-slate-200 bg-slate-50 px-3 py-2 text-sm text-slate-700 outline-none focus:border-blue-400"
          ></textarea>
        </div>
        <div class="flex justify-end gap-2 border-t border-slate-100 px-5 py-3">
          <button
            class="h-8 rounded-md border border-slate-200 px-4 text-sm text-slate-600 hover:bg-slate-50"
            @click="closeForm"
          >
            취소
          </button>
          <button
            class="h-8 rounded-md bg-blue-600 px-4 text-sm font-semibold text-white hover:bg-blue-700 disabled:opacity-50"
            :disabled="submitting"
            @click="submitPost"
          >
            {{ submitting ? "등록 중..." : "등록" }}
          </button>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref, watch } from "vue";
import { useRouter } from "vue-router";
import { getPosts, createPost } from "@/api/index.js";
import { useAuthStore } from "@/stores/auth";
import { toast } from "vue3-toastify";

const authStore = useAuthStore();
const router = useRouter();

const handleWriteClick = () => {
  if (!authStore.hasAccessToken) {
    router.push({ name: "login", query: { redirect: "/community" } });
    return;
  }
  showForm.value = true;
};

const tabs = [
  { id: "all", label: "전체" },
  { id: "REVIEW", label: "합격후기" },
  { id: "TIP", label: "공부팁" },
  { id: "QNA", label: "질문" },
];

const activeTab = ref("all");
const posts = ref([]);
const totalCount = ref(0);
const currentPage = ref(0);
const pageSize = 10;
const loading = ref(true);
const error = ref(null);
const showForm = ref(false);
const submitting = ref(false);
const searchInput = ref("");
const keyword = ref("");

const form = reactive({ category: "", title: "", content: "" });

const totalPages = computed(() =>
  Math.max(1, Math.ceil(totalCount.value / pageSize)),
);

const filteredPosts = computed(() => {
  if (activeTab.value === "all") return posts.value;
  return posts.value.filter((p) => p.category === activeTab.value);
});

watch(activeTab, () => {
  currentPage.value = 0;
  loadPosts();
});

const categoryLabel = (cat) =>
  ({ REVIEW: "합격후기", TIP: "공부팁", QNA: "질문" })[cat] ?? cat;

const formatDate = (dateStr) => {
  if (!dateStr) return "";
  return new Date(dateStr).toLocaleDateString("ko-KR", {
    month: "2-digit",
    day: "2-digit",
  });
};

const loadPosts = async () => {
  loading.value = true;
  error.value = null;
  try {
    const res = await getPosts({
      keyword: keyword.value || undefined,
      page: currentPage.value,
      size: pageSize,
    });
    const data = res.data.data;
    posts.value = data.posts ?? [];
    totalCount.value = data.totalCount ?? 0;
  } catch {
    error.value = "게시글을 불러오지 못했습니다.";
  } finally {
    loading.value = false;
  }
};

const handleSearch = () => {
  keyword.value = searchInput.value.trim();
  currentPage.value = 0;
  loadPosts();
};

const goPage = (page) => {
  currentPage.value = page;
  loadPosts();
};

const closeForm = () => {
  showForm.value = false;
  form.category = "";
  form.title = "";
  form.content = "";
};

const submitPost = async () => {
  if (!form.category || !form.title.trim() || !form.content.trim()) return;
  submitting.value = true;
  try {
    await createPost({ userId: authStore.userId, certId: null, ...form });
    closeForm();
    await loadPosts();
    toast.success("게시글이 등록되었습니다.");
  } catch {
    toast.error("게시글 등록에 실패했습니다.");
  } finally {
    submitting.value = false;
  }
};

onMounted(loadPosts);
</script>

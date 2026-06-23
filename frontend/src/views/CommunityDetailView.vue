<template>
  <section class="min-h-full p-4">
    <button
      class="mb-4 text-sm text-slate-400 transition hover:text-slate-600"
      @click="$router.push({ name: 'community' })"
    >
      ← 목록으로
    </button>

    <!-- 로딩 -->
    <div v-if="loading" class="py-10 text-center text-sm text-slate-400">
      불러오는 중...
    </div>

    <!-- 에러 -->
    <div v-else-if="error" class="py-10 text-center text-sm text-red-400">
      {{ error }}
    </div>

    <!-- 게시글 상세 -->
    <div v-else-if="post" class="max-w-2xl space-y-4">
      <div class="animate-fade-in-up rounded-lg border border-slate-200 bg-white p-5 shadow-sm">
        <!-- 카테고리 + 제목 -->
        <div class="mb-3">
          <span
            :class="[
              'rounded-full px-2.5 py-0.5 text-xs font-semibold',
              post.category === 'REVIEW' ? 'bg-emerald-100 text-emerald-700' :
              post.category === 'TIP'    ? 'bg-blue-100    text-blue-700'    :
              post.category === 'QNA'    ? 'bg-amber-100   text-amber-700'   :
                                           'bg-slate-100   text-slate-600',
            ]"
          >
            {{ categoryLabel(post.category) }}
          </span>
          <h2 class="mt-2 text-lg font-bold text-slate-800">
            {{ post.title }}
          </h2>
        </div>

        <!-- 메타 -->
        <div
          class="mb-4 flex items-center gap-3 border-b border-slate-100 pb-3 text-xs text-slate-400"
        >
          <span class="font-medium text-slate-500">{{
            post.nickname ?? "알 수 없음"
          }}</span>
          <span>조회 {{ post.viewCount }}</span>
          <span>{{ formatDate(post.createdAt) }}</span>
        </div>

        <!-- 본문 -->
        <p class="whitespace-pre-wrap text-sm leading-relaxed text-slate-700">
          {{ post.content }}
        </p>
      </div>

      <!-- 수정/삭제 버튼: 내 글이거나 관리자일 때 표시 -->
      <div
        v-if="post.userId === authStore.userId || authStore.isAdmin"
        class="flex justify-end gap-2"
      >
        <button
          class="h-8 rounded-md border border-slate-200 px-4 text-sm text-slate-600 hover:bg-slate-50"
          @click="startEdit"
        >
          수정
        </button>
        <button
          class="h-8 rounded-md border border-red-200 px-4 text-sm text-red-500 hover:bg-red-50"
          @click="handleDelete"
        >
          삭제
        </button>
      </div>

      <!-- 댓글 섹션 -->
      <div class="animate-fade-in-up rounded-lg border border-slate-200 bg-white p-4 shadow-sm" style="animation-delay:150ms">
        <h3 class="mb-3 text-sm font-bold text-slate-700">
          댓글 {{ comments.length }}
        </h3>

        <!-- 댓글 목록 -->
        <div class="space-y-3">
          <div
            v-if="comments.length === 0"
            class="py-4 text-center text-sm text-slate-400"
          >
            첫 번째 댓글을 남겨보세요
          </div>
          <div
            v-for="(c, i) in comments"
            :key="c.commentId"
            class="animate-fade-in-up flex items-start gap-2"
            :style="{ animationDelay: `${200 + i * 45}ms` }"
          >
            <div
              class="flex h-7 w-7 shrink-0 items-center justify-center rounded-full bg-blue-50 text-xs font-bold text-blue-500"
            >
              {{ (c.nickname ?? "?").charAt(0) }}
            </div>
            <div class="flex-1">
              <div class="flex items-center gap-2">
                <span class="text-sm font-semibold text-slate-700">{{
                  c.nickname ?? "알 수 없음"
                }}</span>
                <span class="text-xs text-slate-400">{{
                  formatDate(c.createdAt)
                }}</span>
                <button
                  v-if="c.userId === authStore.userId || authStore.isAdmin"
                  class="ml-auto text-xs text-slate-400 hover:text-red-400"
                  @click="handleDeleteComment(c.commentId)"
                >
                  삭제
                </button>
              </div>
              <p class="mt-0.5 text-sm text-slate-600">{{ c.content }}</p>
            </div>
          </div>
        </div>

        <!-- 댓글 입력 -->
        <div
          v-if="authStore.hasAccessToken"
          class="mt-3 flex gap-2 border-t border-slate-100 pt-3"
        >
          <input
            v-model="commentInput"
            type="text"
            placeholder="댓글을 입력하세요"
            class="h-8 flex-1 rounded-md border border-slate-200 bg-slate-50 px-3 text-sm text-slate-700 outline-none focus:border-blue-400"
            @keydown.enter="submitComment"
          />
          <button
            class="h-8 rounded-md bg-blue-600 px-3 text-sm font-semibold text-white hover:bg-blue-700 disabled:opacity-50"
            :disabled="commentSubmitting"
            @click="submitComment"
          >
            등록
          </button>
        </div>
        <p
          v-else
          class="mt-3 border-t border-slate-100 pt-3 text-center text-xs text-slate-400"
        >
          댓글을 작성하려면
          <RouterLink to="/login" class="text-blue-500">로그인</RouterLink>이
          필요합니다.
        </p>
      </div>
    </div>

    <!-- 수정 모달 -->
    <div
      v-if="showEditForm"
      class="fixed inset-0 z-50 flex items-center justify-center bg-black/40 p-4"
    >
      <div
        class="w-full max-w-lg rounded-lg border border-slate-200 bg-white shadow-xl"
      >
        <div
          class="flex items-center justify-between border-b border-slate-100 px-5 py-3"
        >
          <h3 class="text-base font-bold text-slate-700">게시글 수정</h3>
          <button
            class="text-slate-400 hover:text-slate-600"
            @click="showEditForm = false"
          >
            ✕
          </button>
        </div>
        <div class="space-y-3 p-5">
          <select
            v-model="editForm.category"
            class="h-9 w-full rounded-md border border-slate-200 bg-slate-50 px-3 text-sm text-slate-700 outline-none focus:border-blue-400"
          >
            <option value="REVIEW">합격후기</option>
            <option value="TIP">공부팁</option>
            <option value="QNA">질문</option>
          </select>
          <input
            v-model="editForm.title"
            type="text"
            class="h-9 w-full rounded-md border border-slate-200 bg-slate-50 px-3 text-sm text-slate-700 outline-none focus:border-blue-400"
          />
          <textarea
            v-model="editForm.content"
            rows="6"
            class="w-full resize-none rounded-md border border-slate-200 bg-slate-50 px-3 py-2 text-sm text-slate-700 outline-none focus:border-blue-400"
          ></textarea>
        </div>
        <div class="flex justify-end gap-2 border-t border-slate-100 px-5 py-3">
          <button
            class="h-8 rounded-md border border-slate-200 px-4 text-sm text-slate-600 hover:bg-slate-50"
            @click="showEditForm = false"
          >
            취소
          </button>
          <button
            class="h-8 rounded-md bg-blue-600 px-4 text-sm font-semibold text-white hover:bg-blue-700"
            @click="handleUpdate"
          >
            저장
          </button>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { onMounted, reactive, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import {
  getPostDetail,
  updatePost,
  deletePost,
  getComments,
  createComment,
  deleteComment,
} from "@/api/index.js";
import { useAuthStore } from "@/stores/auth";
import { toast } from "vue3-toastify";

const authStore = useAuthStore();
const route = useRoute();
const router = useRouter();

const post = ref(null);
const loading = ref(true);
const error = ref(null);
const showEditForm = ref(false);
const editForm = reactive({ category: "", title: "", content: "" });

const comments = ref([]);
const commentInput = ref("");
const commentSubmitting = ref(false);

const loadComments = async () => {
  try {
    const res = await getComments(route.params.id);
    comments.value = res.data.data ?? [];
  } catch {}
};

const submitComment = async () => {
  if (!commentInput.value.trim() || commentSubmitting.value) return;
  commentSubmitting.value = true;
  try {
    await createComment(route.params.id, {
      userId: authStore.userId,
      content: commentInput.value.trim(),
    });
    commentInput.value = "";
    await loadComments();
  } catch {
    toast.error("댓글 등록에 실패했습니다.");
  } finally {
    commentSubmitting.value = false;
  }
};

const handleDeleteComment = async (commentId) => {
  if (!confirm("댓글을 삭제할까요?")) return;
  try {
    toast.success("댓글을 삭제했습니다.");
    await deleteComment(route.params.id, commentId);
    await loadComments();
  } catch {
    toast.error("댓글 삭제에 실패했습니다.");
  }
};

const categoryLabel = (cat) =>
  ({ REVIEW: "합격후기", TIP: "공부팁", QNA: "질문" })[cat] ?? cat;

const formatDate = (dateStr) => {
  if (!dateStr) return "";
  return new Date(dateStr).toLocaleDateString("ko-KR", {
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
  });
};

const loadPost = async () => {
  loading.value = true;
  error.value = null;
  try {
    const res = await getPostDetail(route.params.id);
    post.value = res.data.data;
  } catch {
    error.value = "게시글을 불러오지 못했습니다.";
  } finally {
    loading.value = false;
  }
};

const startEdit = () => {
  editForm.category = post.value.category;
  editForm.title = post.value.title;
  editForm.content = post.value.content;
  showEditForm.value = true;
};

const handleUpdate = async () => {
  try {
    await updatePost(route.params.id, editForm);
    showEditForm.value = false;
    await loadPost();
    toast.success("게시글이 수정되었습니다.");
  } catch {
    toast.error("게시글 수정에 실패했습니다.");
  }
};

const handleDelete = async () => {
  if (!confirm("게시글을 삭제할까요?")) return;
  try {
    await deletePost(route.params.id);
    toast.success("게시글이 삭제되었습니다.");
    router.push({ name: "community" });
  } catch {
    toast.error("게시글 삭제에 실패했습니다.");
  }
};

onMounted(async () => {
  await loadPost();
  await loadComments();
});
</script>

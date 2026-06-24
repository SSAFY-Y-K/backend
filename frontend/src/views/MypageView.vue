<template>
  <section class="min-h-full p-4">
    <div class="mx-auto max-w-2xl space-y-4">
      <!-- 프로필 카드 -->
      <div class="animate-fade-in-up rounded-lg border border-slate-200 bg-white p-5 shadow-sm">
        <div
          v-if="profileLoading"
          class="py-4 text-center text-xs text-slate-400"
        >
          불러오는 중...
        </div>
        <div v-else-if="profile">
          <div class="flex items-center justify-between">
            <div class="flex items-center gap-4">
              <div
                class="flex h-12 w-12 items-center justify-center rounded-full bg-blue-100 text-base font-bold text-blue-600"
              >
                {{ profile.nickname?.charAt(0) }}
              </div>
              <div>
                <p class="text-sm font-bold text-slate-800">
                  {{ profile.nickname }}
                </p>
                <p class="text-xs text-slate-400">@{{ profile.username }}</p>
                <span
                  v-if="profile.role === 'ADMIN'"
                  class="mt-1 inline-block rounded bg-orange-100 px-1.5 py-0.5 text-[10px] font-semibold text-orange-600"
                >
                  관리자
                </span>
              </div>
            </div>
            <div class="flex gap-2">
              <button
                class="h-8 rounded-md border border-slate-200 px-3 text-xs text-slate-600 hover:bg-slate-50"
                @click="showEditProfile = true"
              >
                닉네임 수정
              </button>
              <button
                class="h-8 rounded-md border border-red-200 px-4 text-xs text-red-500 transition hover:bg-red-50"
                @click="handleLogout"
              >
                로그아웃
              </button>
            </div>
          </div>

          <!-- 통계 -->
          <div
            v-if="stats"
            class="mt-4 grid grid-cols-4 gap-3 border-t border-slate-100 pt-4"
          >
            <div class="text-center">
              <p class="text-lg font-bold text-blue-600">
                {{ stats.totalSubmissions }}
              </p>
              <p class="text-[10px] text-slate-400">총 제출</p>
            </div>
            <div class="text-center">
              <p class="text-lg font-bold text-emerald-600">
                {{ stats.acCount }}
              </p>
              <p class="text-[10px] text-slate-400">정답</p>
            </div>
            <div class="text-center">
              <p class="text-lg font-bold text-slate-700">
                {{ stats.attemptedProblems }}
              </p>
              <p class="text-[10px] text-slate-400">시도 문제</p>
            </div>
            <div class="text-center">
              <p class="text-lg font-bold text-purple-600">
                {{ stats.solvedProblems }}
              </p>
              <p class="text-[10px] text-slate-400">해결 문제</p>
            </div>
          </div>
        </div>
      </div>

      <!-- 닉네임 수정 모달 -->
      <div
        v-if="showEditProfile"
        class="fixed inset-0 z-50 flex items-center justify-center bg-black/40 p-4"
      >
        <div
          class="w-full max-w-sm rounded-lg border border-slate-200 bg-white shadow-xl"
        >
          <div
            class="flex items-center justify-between border-b border-slate-100 px-5 py-3"
          >
            <h3 class="text-sm font-bold text-slate-700">닉네임 수정</h3>
            <button
              class="text-slate-400 hover:text-slate-600"
              @click="showEditProfile = false"
            >
              ✕
            </button>
          </div>
          <div class="p-5">
            <input
              v-model="newNickname"
              type="text"
              placeholder="새 닉네임"
              class="h-9 w-full rounded-md border border-slate-200 bg-slate-50 px-3 text-xs text-slate-700 outline-none focus:border-blue-400"
            />
          </div>
          <div
            class="flex justify-end gap-2 border-t border-slate-100 px-5 py-3"
          >
            <button
              class="h-8 rounded-md border border-slate-200 px-4 text-xs text-slate-600"
              @click="showEditProfile = false"
            >
              취소
            </button>
            <button
              class="h-8 rounded-md bg-blue-600 px-4 text-xs font-semibold text-white hover:bg-blue-700"
              @click="handleUpdateProfile"
            >
              저장
            </button>
          </div>
        </div>
      </div>

      <!-- 내 게시글 -->
      <div class="animate-fade-in-up rounded-lg border border-slate-200 bg-white shadow-sm" style="animation-delay:120ms">
        <div
          class="flex items-center justify-between border-b border-slate-100 px-4 py-3"
        >
          <h3 class="text-xs font-bold text-slate-700">내 게시글</h3>
          <span class="text-[10px] text-slate-400">{{ myPosts.length }}개</span>
        </div>
        <div
          v-if="postsLoading"
          class="py-6 text-center text-xs text-slate-400"
        >
          불러오는 중...
        </div>
        <div
          v-else-if="myPosts.length === 0"
          class="py-6 text-center text-xs text-slate-400"
        >
          작성한 게시글이 없습니다.
        </div>
        <div v-else class="divide-y divide-slate-50">
          <div
            v-for="post in myPosts"
            :key="post.postId"
            class="flex cursor-pointer items-center justify-between px-4 py-2.5 transition hover:bg-slate-50"
            @click="
              $router.push({
                name: 'community-detail',
                params: { id: post.postId },
              })
            "
          >
            <div class="min-w-0 flex-1">
              <p class="truncate text-xs font-medium text-slate-700">
                {{ post.title }}
              </p>
              <div class="mt-0.5 flex gap-2 text-[10px] text-slate-400">
                <span>{{ categoryLabel(post.category) }}</span>
                <span>조회 {{ post.viewCount }}</span>
              </div>
            </div>
            <span class="ml-3 shrink-0 text-[10px] text-slate-400">{{
              formatDate(post.createdAt)
            }}</span>
          </div>
        </div>
      </div>

      <!-- 내 제출 이력 -->
      <div class="animate-fade-in-up rounded-lg border border-slate-200 bg-white shadow-sm" style="animation-delay:220ms">
        <div
          class="flex items-center justify-between border-b border-slate-100 px-4 py-3"
        >
          <h3 class="text-xs font-bold text-slate-700">제출 이력</h3>
          <span class="text-[10px] text-slate-400">최근 20개</span>
        </div>
        <div
          v-if="submissionsLoading"
          class="py-6 text-center text-xs text-slate-400"
        >
          불러오는 중...
        </div>
        <div
          v-else-if="mySubmissions.length === 0"
          class="py-6 text-center text-xs text-slate-400"
        >
          제출 이력이 없습니다.
        </div>
        <div v-else class="divide-y divide-slate-50">
          <div
            v-for="s in mySubmissions"
            :key="s.submissionId"
            class="flex cursor-pointer items-center justify-between px-4 py-2.5 transition hover:bg-slate-50"
            @click="
              $router.push({
                name: 'coding-problem-detail',
                params: { id: s.problemId },
              })
            "
          >
            <div class="min-w-0 flex-1">
              <p class="truncate text-xs font-medium text-slate-700">
                {{ s.problemTitle ?? `문제 #${s.problemId}` }}
              </p>
              <div class="mt-0.5 flex gap-2 text-[10px] text-slate-400">
                <span>{{ s.language }}</span>
                <span v-if="s.execTimeMs">{{ s.execTimeMs }}ms</span>
              </div>
            </div>
            <div class="ml-3 flex shrink-0 flex-col items-end gap-0.5">
              <span :class="['text-[11px] font-bold', verdictColor[s.verdict]]">
                {{ verdictLabel[s.verdict] ?? s.verdict }}
              </span>
              <button
                class="rounded border border-slate-200 px-2 py-0.5 text-[10px] font-medium text-slate-500 transition hover:border-blue-200 hover:bg-blue-50 hover:text-blue-600"
                @click.stop="openSubmissionSource(s)"
              >
                코드 보기
              </button>
              <span class="text-[10px] text-slate-400">{{
                formatDate(s.submittedAt)
              }}</span>
            </div>
          </div>
        </div>
      </div>

      <div
        v-if="showSubmissionSourceModal"
        class="fixed inset-0 z-50 flex items-center justify-center bg-black/45 p-4"
        @click.self="closeSubmissionSourceModal"
      >
        <div class="flex max-h-[80vh] w-full max-w-3xl flex-col overflow-hidden rounded-lg border border-slate-200 bg-white shadow-2xl">
          <div class="flex items-center justify-between border-b border-slate-100 px-5 py-3">
            <div>
              <h3 class="text-sm font-bold text-slate-800">내 제출 코드</h3>
              <p class="mt-0.5 text-[11px] text-slate-400">
                {{ selectedSubmissionMeta.problemTitle ?? `문제 #${selectedSubmissionMeta.problemId ?? ""}` }}
              </p>
            </div>
            <button
              class="text-sm text-slate-400 transition hover:text-slate-600"
              @click="closeSubmissionSourceModal"
            >
              닫기
            </button>
          </div>

          <div class="flex-1 overflow-y-auto p-5">
            <div v-if="submissionSourceLoading" class="py-12 text-center text-xs text-slate-400">
              제출 코드를 불러오는 중입니다.
            </div>
            <div v-else-if="submissionSourceError" class="py-12 text-center text-xs text-red-500">
              {{ submissionSourceError }}
            </div>
            <div v-else-if="selectedSubmissionSource" class="space-y-3">
              <div class="flex items-center gap-2 text-[11px] text-slate-500">
                <span class="rounded bg-slate-100 px-2 py-1 font-medium text-slate-600">
                  {{ selectedSubmissionSource.language ?? selectedSubmissionMeta.language ?? "-" }}
                </span>
                <span>제출 ID {{ selectedSubmissionSource.submissionId }}</span>
              </div>
              <pre class="overflow-x-auto rounded-lg bg-slate-950 p-4 text-xs leading-relaxed text-green-300">{{ selectedSubmissionSource.sourceCode }}</pre>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import {
  getMyProfile,
  getMyPosts,
  getMySubmissions,
  getMyStats,
  getSubmissionSource,
  updateProfile,
  logout,
} from "@/api/index.js";
import { useAuthStore } from "@/stores/auth";
import { toast } from "vue3-toastify";

const router = useRouter();
const authStore = useAuthStore();

const profile = ref(null);
const myPosts = ref([]);
const mySubmissions = ref([]);
const stats = ref(null);
const profileLoading = ref(true);
const postsLoading = ref(true);
const submissionsLoading = ref(true);
const showEditProfile = ref(false);
const newNickname = ref("");
const showSubmissionSourceModal = ref(false);
const submissionSourceLoading = ref(false);
const submissionSourceError = ref("");
const selectedSubmissionSource = ref(null);
const selectedSubmissionMeta = ref({});

const handleUpdateProfile = async () => {
  if (!newNickname.value.trim()) return;
  try {
    await updateProfile({ nickname: newNickname.value.trim() });
    showEditProfile.value = false;
    const res = await getMyProfile();
    profile.value = res.data.data ?? null;
    newNickname.value = "";
    toast.success("닉네임이 변경되었습니다.");
  } catch {
    toast.error("닉네임 변경에 실패했습니다.");
  }
};

const categoryLabel = (cat) =>
  ({ REVIEW: "합격후기", TIP: "공부팁", QNA: "질문" })[cat] ?? cat;
const verdictLabel = {
  AC: "정답",
  WA: "오답",
  TLE: "시간초과",
  MLE: "메모리초과",
  CE: "컴파일오류",
  RE: "런타임오류",
};
const verdictColor = {
  AC: "text-emerald-500",
  WA: "text-orange-500",
  TLE: "text-orange-500",
  MLE: "text-orange-500",
  CE: "text-red-500",
  RE: "text-red-500",
};

const formatDate = (dateStr) => {
  if (!dateStr) return "";
  return new Date(dateStr).toLocaleDateString("ko-KR", {
    month: "2-digit",
    day: "2-digit",
  });
};

const handleLogout = async () => {
  try {
    await logout();
    toast.info("로그아웃 되었습니다.");
  } finally {
    authStore.clearAccessToken();
    router.push({ name: "home" });
  }
};

const closeSubmissionSourceModal = () => {
  showSubmissionSourceModal.value = false;
  submissionSourceLoading.value = false;
  submissionSourceError.value = "";
  selectedSubmissionSource.value = null;
  selectedSubmissionMeta.value = {};
};

const openSubmissionSource = async (submission) => {
  showSubmissionSourceModal.value = true;
  submissionSourceLoading.value = true;
  submissionSourceError.value = "";
  selectedSubmissionSource.value = null;
  selectedSubmissionMeta.value = submission;

  try {
    const res = await getSubmissionSource(submission.submissionId);
    selectedSubmissionSource.value = res.data?.data ?? null;
  } catch {
    submissionSourceError.value = "제출 코드를 불러오지 못했습니다.";
  } finally {
    submissionSourceLoading.value = false;
  }
};

onMounted(async () => {
  const [profileRes, postsRes, submissionsRes, statsRes] =
    await Promise.allSettled([
      getMyProfile(),
      getMyPosts(),
      getMySubmissions(),
      getMyStats(),
    ]);

  if (profileRes.status === "fulfilled")
    profile.value = profileRes.value?.data?.data ?? null;
  if (postsRes.status === "fulfilled")
    myPosts.value = postsRes.value?.data?.data ?? [];
  if (submissionsRes.status === "fulfilled")
    mySubmissions.value = submissionsRes.value?.data?.data ?? [];
  if (statsRes.status === "fulfilled")
    stats.value = statsRes.value?.data?.data ?? null;

  profileLoading.value = false;
  postsLoading.value = false;
  submissionsLoading.value = false;
});
</script>

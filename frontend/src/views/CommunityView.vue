<template>
	<section class="min-h-full p-4">
		<!-- Tab filter + 글쓰기 -->
		<div class="mb-3 flex items-center justify-between">
			<div class="flex gap-0 overflow-hidden rounded-md border border-slate-200 bg-white">
				<button
					v-for="tab in tabs"
					:key="tab.id"
					:class="[
						'px-4 py-1.5 text-xs font-medium transition',
						activeTab === tab.id ? 'bg-blue-600 text-white' : 'text-slate-500 hover:bg-slate-50',
					]"
					@click="activeTab = tab.id"
				>
					{{ tab.label }}
				</button>
			</div>

			<button
				class="h-7 rounded-md bg-blue-600 px-3 text-xs font-semibold text-white transition hover:bg-blue-700"
				@click="showForm = true"
			>
				+ 글쓰기
			</button>
		</div>

		<!-- 로딩 -->
		<div v-if="loading" class="py-10 text-center text-xs text-slate-400">불러오는 중...</div>

		<!-- 에러 -->
		<div v-else-if="error" class="py-10 text-center text-xs text-red-400">{{ error }}</div>

		<!-- 게시글 목록 -->
		<div v-else class="space-y-2">
			<div v-if="filteredPosts.length === 0" class="py-10 text-center text-xs text-slate-400">
				게시글이 없습니다.
			</div>
			<div
				v-for="post in filteredPosts"
				:key="post.postId"
				class="flex cursor-pointer gap-3 rounded-lg border border-slate-200 bg-white p-3 shadow-sm transition hover:shadow-md"
				@click="$router.push({ name: 'community-detail', params: { id: post.postId } })"
			>
				<div class="flex h-8 w-8 shrink-0 items-center justify-center rounded-full bg-blue-100 text-xs font-bold text-blue-600">
					{{ post.title.charAt(0) }}
				</div>
				<div class="min-w-0 flex-1">
					<p class="text-xs font-semibold leading-snug text-slate-800">{{ post.title }}</p>
					<div class="mt-1 flex items-center gap-3 text-[10px] text-slate-400">
						<span class="font-medium text-slate-500">{{ post.nickname ?? '알 수 없음' }}</span>
						<span>{{ categoryLabel(post.category) }}</span>
						<span>조회 {{ post.viewCount }}</span>
						<span>{{ formatDate(post.createdAt) }}</span>
					</div>
				</div>
			</div>
		</div>

		<!-- 글쓰기 모달 -->
		<div v-if="showForm" class="fixed inset-0 z-50 flex items-center justify-center bg-black/40 p-4">
			<div class="w-full max-w-lg rounded-lg border border-slate-200 bg-white shadow-xl">
				<div class="flex items-center justify-between border-b border-slate-100 px-5 py-3">
					<h3 class="text-sm font-bold text-slate-700">새 글 작성</h3>
					<button class="text-slate-400 hover:text-slate-600" @click="closeForm">✕</button>
				</div>
				<div class="space-y-3 p-5">
					<select
						v-model="form.category"
						class="h-9 w-full rounded-md border border-slate-200 bg-slate-50 px-3 text-xs text-slate-700 outline-none focus:border-blue-400"
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
						class="h-9 w-full rounded-md border border-slate-200 bg-slate-50 px-3 text-xs text-slate-700 outline-none focus:border-blue-400"
					/>
					<textarea
						v-model="form.content"
						rows="6"
						placeholder="내용을 입력하세요"
						class="w-full resize-none rounded-md border border-slate-200 bg-slate-50 px-3 py-2 text-xs text-slate-700 outline-none focus:border-blue-400"
					></textarea>
				</div>
				<div class="flex justify-end gap-2 border-t border-slate-100 px-5 py-3">
					<button
						class="h-8 rounded-md border border-slate-200 px-4 text-xs text-slate-600 hover:bg-slate-50"
						@click="closeForm"
					>
						취소
					</button>
					<button
						class="h-8 rounded-md bg-blue-600 px-4 text-xs font-semibold text-white hover:bg-blue-700 disabled:opacity-50"
						:disabled="submitting"
						@click="submitPost"
					>
						{{ submitting ? '등록 중...' : '등록' }}
					</button>
				</div>
			</div>
		</div>
	</section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from "vue";
import { getPosts, createPost } from "@/api/index.js";

const tabs = [
	{ id: "all", label: "전체" },
	{ id: "REVIEW", label: "합격후기" },
	{ id: "TIP", label: "공부팁" },
	{ id: "QNA", label: "질문" },
];

const activeTab = ref("all");
const posts = ref([]);
const loading = ref(true);
const error = ref(null);
const showForm = ref(false);
const submitting = ref(false);

const form = reactive({ category: "", title: "", content: "" });

const filteredPosts = computed(() => {
	if (activeTab.value === "all") return posts.value;
	return posts.value.filter((p) => p.category === activeTab.value);
});

const categoryLabel = (cat) => ({ REVIEW: "합격후기", TIP: "공부팁", QNA: "질문" }[cat] ?? cat);

const formatDate = (dateStr) => {
	if (!dateStr) return "";
	return new Date(dateStr).toLocaleDateString("ko-KR", { month: "2-digit", day: "2-digit" });
};

const loadPosts = async () => {
	loading.value = true;
	error.value = null;
	try {
		const res = await getPosts();
		posts.value = res.data ?? res;
	} catch (e) {
		error.value = "게시글을 불러오지 못했습니다.";
	} finally {
		loading.value = false;
	}
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
		await createPost({ userId: 1, certId: null, ...form });
		closeForm();
		await loadPosts();
	} catch {
		alert("등록에 실패했습니다.");
	} finally {
		submitting.value = false;
	}
};

onMounted(loadPosts);
</script>

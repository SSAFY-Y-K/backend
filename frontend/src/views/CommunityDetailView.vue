<template>
	<section class="min-h-full p-4">
		<button
			class="mb-4 text-xs text-slate-400 transition hover:text-slate-600"
			@click="$router.push({ name: 'community' })"
		>
			← 목록으로
		</button>

		<!-- 로딩 -->
		<div v-if="loading" class="py-10 text-center text-xs text-slate-400">불러오는 중...</div>

		<!-- 에러 -->
		<div v-else-if="error" class="py-10 text-center text-xs text-red-400">{{ error }}</div>

		<!-- 게시글 상세 -->
		<div v-else-if="post" class="max-w-2xl space-y-4">
			<div class="rounded-lg border border-slate-200 bg-white p-5 shadow-sm">
				<!-- 카테고리 + 제목 -->
				<div class="mb-3">
					<span class="rounded-full border border-blue-200 px-2 py-0.5 text-[10px] font-medium text-blue-600">
						{{ categoryLabel(post.category) }}
					</span>
					<h2 class="mt-2 text-base font-bold text-slate-800">{{ post.title }}</h2>
				</div>

				<!-- 메타 -->
				<div class="mb-4 flex items-center gap-3 border-b border-slate-100 pb-3 text-[10px] text-slate-400">
					<span class="font-medium text-slate-500">{{ post.nickname ?? '알 수 없음' }}</span>
					<span>조회 {{ post.viewCount }}</span>
					<span>{{ formatDate(post.createdAt) }}</span>
				</div>

				<!-- 본문 -->
				<p class="whitespace-pre-wrap text-xs leading-relaxed text-slate-700">{{ post.content }}</p>
			</div>

			<!-- 수정/삭제 버튼: 내 글일 때만 표시 -->
			<div v-if="post.userId === CURRENT_USER_ID" class="flex justify-end gap-2">
				<button
					class="h-8 rounded-md border border-slate-200 px-4 text-xs text-slate-600 hover:bg-slate-50"
					@click="startEdit"
				>
					수정
				</button>
				<button
					class="h-8 rounded-md border border-red-200 px-4 text-xs text-red-500 hover:bg-red-50"
					@click="handleDelete"
				>
					삭제
				</button>
			</div>
		</div>

		<!-- 수정 모달 -->
		<div v-if="showEditForm" class="fixed inset-0 z-50 flex items-center justify-center bg-black/40 p-4">
			<div class="w-full max-w-lg rounded-lg border border-slate-200 bg-white shadow-xl">
				<div class="flex items-center justify-between border-b border-slate-100 px-5 py-3">
					<h3 class="text-sm font-bold text-slate-700">게시글 수정</h3>
					<button class="text-slate-400 hover:text-slate-600" @click="showEditForm = false">✕</button>
				</div>
				<div class="space-y-3 p-5">
					<select
						v-model="editForm.category"
						class="h-9 w-full rounded-md border border-slate-200 bg-slate-50 px-3 text-xs text-slate-700 outline-none focus:border-blue-400"
					>
						<option value="REVIEW">합격후기</option>
						<option value="TIP">공부팁</option>
						<option value="QNA">질문</option>
					</select>
					<input
						v-model="editForm.title"
						type="text"
						class="h-9 w-full rounded-md border border-slate-200 bg-slate-50 px-3 text-xs text-slate-700 outline-none focus:border-blue-400"
					/>
					<textarea
						v-model="editForm.content"
						rows="6"
						class="w-full resize-none rounded-md border border-slate-200 bg-slate-50 px-3 py-2 text-xs text-slate-700 outline-none focus:border-blue-400"
					></textarea>
				</div>
				<div class="flex justify-end gap-2 border-t border-slate-100 px-5 py-3">
					<button
						class="h-8 rounded-md border border-slate-200 px-4 text-xs text-slate-600 hover:bg-slate-50"
						@click="showEditForm = false"
					>
						취소
					</button>
					<button
						class="h-8 rounded-md bg-blue-600 px-4 text-xs font-semibold text-white hover:bg-blue-700"
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
import { getPostDetail, updatePost, deletePost } from "@/api/index.js";

const CURRENT_USER_ID = 1; // 인증 구현 전 임시

const route = useRoute();
const router = useRouter();

const post = ref(null);
const loading = ref(true);
const error = ref(null);
const showEditForm = ref(false);
const editForm = reactive({ category: "", title: "", content: "" });

const categoryLabel = (cat) => ({ REVIEW: "합격후기", TIP: "공부팁", QNA: "질문" }[cat] ?? cat);

const formatDate = (dateStr) => {
	if (!dateStr) return "";
	return new Date(dateStr).toLocaleDateString("ko-KR", { year: "numeric", month: "2-digit", day: "2-digit" });
};

const loadPost = async () => {
	loading.value = true;
	error.value = null;
	try {
		const res = await getPostDetail(route.params.id);
		post.value = res.data ?? res;
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
	} catch {
		alert("수정에 실패했습니다.");
	}
};

const handleDelete = async () => {
	if (!confirm("게시글을 삭제할까요?")) return;
	try {
		await deletePost(route.params.id);
		router.push({ name: "community" });
	} catch {
		alert("삭제에 실패했습니다.");
	}
};

onMounted(loadPost);
</script>

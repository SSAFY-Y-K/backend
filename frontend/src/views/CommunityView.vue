<template>
	<section class="min-h-full bg-slate-50">
		<div class="mx-auto max-w-[1600px] px-6 py-6">
			<header class="mb-6">
				<h1 class="text-3xl font-bold tracking-tight text-slate-900">커뮤니티</h1>
				<p class="mt-2 text-sm text-slate-500">
					합격 후기, 공부 팁, 질문을 한곳에서 나누는 공간입니다.
				</p>
			</header>

			<div class="grid gap-6 lg:grid-cols-[minmax(0,1fr)_260px]">
				<div class="space-y-4">
					<div class="overflow-hidden rounded-lg border border-slate-200 bg-white shadow-sm">
						<div class="flex gap-0 overflow-x-auto">
							<button
								v-for="tab in tabs"
								:key="tab.id"
								type="button"
								:class="[
									'border-b-2 px-4 py-3 text-sm font-medium transition',
									activeTab === tab.id
										? 'border-blue-600 text-blue-600'
										: 'border-transparent text-slate-500 hover:text-slate-700',
								]"
								@click="activeTab = tab.id"
							>
								{{ tab.label }}
							</button>
						</div>
					</div>

					<article
						v-for="post in filteredPosts"
						:key="post.id"
						class="rounded-lg border border-slate-200 bg-white px-4 py-4 shadow-sm md:px-5 md:py-5"
					>
						<div class="flex gap-3">
							<div
								class="flex h-10 w-10 shrink-0 items-center justify-center rounded-full bg-blue-600 text-sm font-bold text-white"
							>
								{{ post.avatar }}
							</div>

							<div class="min-w-0 flex-1">
								<div class="flex flex-wrap gap-2">
									<span
										v-for="tag in post.tags"
										:key="tag"
										class="inline-flex items-center rounded-full border border-blue-200 px-2.5 py-0.5 text-xs font-medium text-blue-600"
									>
										{{ tag }}
									</span>
								</div>

								<h2 class="mt-2 text-lg font-semibold text-slate-900">
									{{ post.title }}
								</h2>

								<div
									class="mt-3 flex flex-wrap items-center gap-x-4 gap-y-2 text-sm text-slate-500"
								>
									<span>{{ post.author }}</span>
									<span class="inline-flex items-center gap-1">
										<svg viewBox="0 0 24 24" aria-hidden="true" class="h-4 w-4">
											<circle
												cx="12"
												cy="12"
												r="9"
												fill="none"
												stroke="currentColor"
												stroke-width="1.8"
											/>
											<path
												d="M12 7v5l3 2"
												fill="none"
												stroke="currentColor"
												stroke-linecap="round"
												stroke-linejoin="round"
												stroke-width="1.8"
											/>
										</svg>
										{{ post.time }}
									</span>
									<span class="inline-flex items-center gap-1">좋아요 {{ post.likes }}</span>
									<span class="inline-flex items-center gap-1">댓글 {{ post.comments }}</span>
									<span class="inline-flex items-center gap-1">조회 {{ post.views }}</span>
								</div>
							</div>
						</div>
					</article>

					<div class="pt-2 text-center">
						<button
							type="button"
							class="inline-flex items-center rounded-md border border-blue-200 bg-white px-4 py-2 text-sm font-medium text-blue-600 transition hover:bg-blue-50"
						>
							더 보기
						</button>
					</div>
				</div>

				<aside class="space-y-4">
					<div class="rounded-lg bg-blue-600 px-4 py-4 text-white shadow-sm">
						<h2 class="text-lg font-bold">새 글 작성</h2>
						<p class="mt-3 text-sm leading-6 text-blue-50">
							궁금한 점이나 공부 경험을 남겨서 다른 사람과 공유해보세요.
						</p>
						<button
							type="button"
							class="mt-4 inline-flex w-full items-center justify-center rounded-md border border-white/80 bg-white px-4 py-2 text-sm font-semibold text-blue-600 transition hover:bg-blue-50"
						>
							글쓰기
						</button>
					</div>

					<div class="rounded-lg border border-slate-200 bg-white p-4 shadow-sm">
						<h2 class="text-lg font-semibold text-slate-900">인기 팁</h2>

						<div class="mt-4 space-y-3">
							<div
								v-for="tip in popularTips"
								:key="tip.id"
								class="rounded-md border border-slate-200 px-3 py-3"
							>
								<div class="flex items-center gap-2">
									<span
										class="inline-flex items-center rounded-full border border-slate-300 px-2 py-0.5 text-xs font-medium text-slate-600"
									>
										{{ tip.tag }}
									</span>
								</div>
								<p class="mt-2 text-sm font-semibold text-slate-900">
									{{ tip.title }}
								</p>
								<p class="mt-2 text-xs text-slate-500">
									{{ tip.author }} · 좋아요 {{ tip.likes }}
								</p>
							</div>
						</div>
					</div>
				</aside>
			</div>
		</div>
	</section>
</template>

<script setup>
import { computed, ref } from "vue";

const tabs = [
	{ id: "all", label: "전체 게시글" },
	{ id: "review", label: "합격 후기" },
	{ id: "tip", label: "공부 팁" },
	{ id: "question", label: "질문" },
];

const activeTab = ref("all");

const posts = [
	{
		id: 1,
		tab: "review",
		avatar: "김",
		tags: ["정보처리기사", "합격후기", "실기"],
		title: "정보처리기사 실기 합격 후기 - 2달 준비",
		author: "김코딩",
		time: "2시간 전",
		likes: 45,
		comments: 12,
		views: 234,
	},
	{
		id: 2,
		tab: "question",
		avatar: "이",
		tags: ["AWS SA", "시험후기", "난이도"],
		title: "AWS SAA 덤프 vs 실제 시험 난이도 차이",
		author: "이클라우드",
		time: "5시간 전",
		likes: 28,
		comments: 8,
		views: 156,
	},
	{
		id: 3,
		tab: "tip",
		avatar: "박",
		tags: ["SQLD", "합격후기", "공부법"],
		title: "SQLD 한 달 준비로 합격한 공부법",
		author: "빅데이터",
		time: "1일 전",
		likes: 89,
		comments: 23,
		views: 567,
	},
];

const popularTips = [
	{
		id: 1,
		tag: "정보처리기사",
		title: "운영체제 파트 핵심 정리 5가지",
		author: "최개발",
		likes: 156,
	},
	{
		id: 2,
		tag: "SQLD",
		title: "SQL 문법 헷갈리는 부분 정리",
		author: "강디비",
		likes: 134,
	},
];

const filteredPosts = computed(() => {
	if (activeTab.value === "all") {
		return posts;
	}

	return posts.filter((post) => post.tab === activeTab.value);
});
</script>

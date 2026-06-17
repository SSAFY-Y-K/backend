<template>
	<section class="min-h-full p-4">
		<div>
			<!-- Tab filter -->
			<div class="mb-3 flex items-center justify-between">
				<div class="flex gap-0 overflow-hidden rounded-md border border-slate-200 bg-white">
					<button
						v-for="tab in tabs"
						:key="tab.id"
						:class="[
							'px-4 py-1.5 text-xs font-medium transition',
							activeTab === tab.id
								? 'bg-blue-600 text-white'
								: 'text-slate-500 hover:bg-slate-50',
						]"
						@click="activeTab = tab.id"
					>
						{{ tab.label }}
					</button>
				</div>

				<div class="flex items-center gap-2">
					<select
						class="h-7 rounded-md border border-slate-200 bg-white px-2 text-xs text-slate-600 outline-none focus:border-blue-400"
					>
						<option>정렬</option>
						<option>최신순</option>
					</select>
				</div>
			</div>

			<!-- Posts list -->
			<div class="space-y-2">
				<div
					v-for="post in filteredPosts"
					:key="post.id"
					class="flex cursor-pointer gap-3 rounded-lg border border-slate-200 bg-white p-3 shadow-sm transition hover:shadow-md"
				>
					<div
						class="flex h-8 w-8 shrink-0 items-center justify-center rounded-full bg-blue-100 text-xs font-bold text-blue-600"
					>
						{{ post.avatar }}
					</div>

					<div class="min-w-0 flex-1">
						<p class="text-xs font-semibold leading-snug text-slate-800">{{ post.title }}</p>
						<div class="mt-1 flex items-center gap-3 text-[10px] text-slate-400">
							<span>{{ post.author }}</span>
							<span>{{ post.date }}</span>
						</div>
					</div>
				</div>
			</div>
		</div>
	</section>
</template>

<script setup>
import { computed, ref } from "vue";

const activeTab = ref("all");

const tabs = [
	{ id: "all", label: "자유" },
	{ id: "question", label: "질문" },
];

const posts = [
	{
		id: 1,
		tab: "all",
		avatar: "이",
		title: "정보처리기사 실기 합격 후기 - 2달 준비",
		author: "이개발",
		date: "2025.06.10",
	},
	{
		id: 2,
		tab: "question",
		avatar: "박",
		title: "AWS SAA 덤프 vs 실제 시험 난이도 차이",
		author: "박클라우드",
		date: "2025.06.09",
	},
	{
		id: 3,
		tab: "all",
		avatar: "김",
		title: "SQLD 한 달 준비로 합격한 공부법",
		author: "김데이터",
		date: "2025.06.08",
	},
	{
		id: 4,
		tab: "question",
		avatar: "최",
		title: "정보보안기사 필기 출제 범위 질문",
		author: "최보안",
		date: "2025.06.07",
	},
	{
		id: 5,
		tab: "all",
		avatar: "정",
		title: "자격증 공부 순서 추천해주세요",
		author: "정준비생",
		date: "2025.06.06",
	},
];

const filteredPosts = computed(() => {
	if (activeTab.value === "all") return posts;
	return posts.filter((post) => post.tab === activeTab.value);
});
</script>

<template>
	<section class="min-h-full p-4">
		<div class="grid gap-4 lg:grid-cols-[1fr_200px]">
			<!-- Main posts area -->
			<div>
				<!-- Tab filter + sort -->
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
							<option>처리 팔 ▾</option>
							<option>최신순</option>
							<option>인기순</option>
						</select>
					</div>
				</div>

				<!-- Posts list -->
				<div class="space-y-2">
					<div
						v-for="post in posts"
						:key="post.id"
						class="flex cursor-pointer gap-3 rounded-lg border border-slate-200 bg-white p-3 shadow-sm transition hover:shadow-md"
					>
						<!-- Avatar -->
						<div
							class="flex h-8 w-8 shrink-0 items-center justify-center rounded-full bg-blue-100 text-xs font-bold text-blue-600"
						>
							{{ post.avatar }}
						</div>

						<!-- Content -->
						<div class="min-w-0 flex-1">
							<p class="text-xs font-semibold leading-snug text-slate-800">{{ post.title }}</p>
							<div class="mt-1 flex items-center gap-3 text-[10px] text-slate-400">
								<span class="flex items-center gap-0.5">△ {{ post.likes }}</span>
								<span>댓글 {{ post.comments }}</span>
								<span>포인트 {{ post.points }}</span>
							</div>
						</div>

						<!-- Date -->
						<div class="shrink-0 text-[10px] text-slate-400">{{ post.date }}</div>
					</div>
				</div>
			</div>

			<!-- Right: Popular tips sidebar -->
			<aside>
				<div class="rounded-lg border border-slate-200 bg-white p-3 shadow-sm">
					<h4 class="mb-3 text-xs font-semibold text-slate-700">인기 팁 사이드바</h4>
					<ul class="space-y-2.5">
						<li
							v-for="tip in popularTips"
							:key="tip.id"
							class="flex items-start gap-1.5 cursor-pointer"
						>
							<span class="mt-0.5 shrink-0 text-[10px] text-blue-400">•</span>
							<p class="text-[11px] leading-snug text-slate-600 transition hover:text-blue-600">
								{{ tip.text }}
							</p>
						</li>
					</ul>
				</div>
			</aside>
		</div>
	</section>
</template>

<script setup>
import { ref } from "vue";

const activeTab = ref("all");

const tabs = [
	{ id: "all", label: "자유" },
	{ id: "question", label: "질문" },
];

const posts = [
	{
		id: 1,
		avatar: "이",
		title: "이이이저네 이이이2년 문제 팔린이요!",
		likes: 4.0,
		comments: 39,
		points: 50,
		date: "20여/2 추",
	},
	{
		id: 2,
		avatar: "박",
		title: "도상동 정치고 팔할때에 대해약 우상한 려요?",
		likes: 4.6,
		comments: 38,
		points: 50,
		date: "5여/2 추",
	},
	{
		id: 3,
		avatar: "김",
		title: "인상달의 고물재 문자는 것",
		likes: 4.0,
		comments: 39,
		points: 50,
		date: "20여/2 추",
	},
	{
		id: 4,
		avatar: "최",
		title: "어때도 자들이 자들한 태에라•선티",
		likes: 5.0,
		comments: 34,
		points: 48,
		date: "5여/2 추",
	},
	{
		id: 5,
		avatar: "정",
		title: "꼰야오건 자동 정달에 팔한 참계자",
		likes: 4.2,
		comments: 28,
		points: 42,
		date: "1여/2 추",
	},
];

const popularTips = [
	{ id: 1, text: "팅틸 달여이달 않 같 하는 아이이이이달이달 이달이달이 팔린이달 빠른이?" },
	{ id: 2, text: "자달알 지달하고 탈한 이달달달에에에에에에에에에에 달아하이달 물이달이달이다랄이달이다." },
	{ id: 3, text: "팅킬 달여이달랄이달이달이달이달이달이달이달이달이달이다이달." },
	{ id: 4, text: "팅킬 달여 손 달아이달이달이달이달이달이달이달이달이달이달이달이달이달이다." },
];
</script>

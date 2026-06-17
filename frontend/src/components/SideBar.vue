<template>
	<aside class="flex w-44 shrink-0 flex-col overflow-y-auto border-r border-slate-100 bg-white">
		<!-- Recent problems -->
		<div class="p-3">
			<p class="mb-2 text-[10px] font-semibold uppercase tracking-wide text-slate-400">
				최근 문제목록
			</p>
			<div class="space-y-0.5">
				<RouterLink
					v-for="item in recentProblems"
					:key="item.id"
					:to="{ name: 'problem' }"
					class="flex items-center gap-2 rounded px-2 py-1.5 text-xs text-slate-600 no-underline transition hover:bg-slate-50"
				>
					<span :class="['h-2.5 w-2.5 shrink-0 rounded-sm', item.color]"></span>
					<span class="truncate">{{ item.title }}</span>
				</RouterLink>
			</div>
		</div>

		<div class="mx-3 border-t border-slate-100"></div>

		<!-- Categories -->
		<div class="p-3">
			<p class="mb-2 text-[10px] font-semibold uppercase tracking-wide text-slate-400">
				카테고리
			</p>
			<div class="space-y-0.5">
				<div
					v-for="cat in categories"
					:key="cat.name"
					class="flex cursor-pointer items-center gap-2 rounded px-2 py-1.5 text-xs text-slate-600 transition hover:bg-slate-50"
				>
					<span :class="['h-2 w-2 shrink-0 rounded-full', cat.color]"></span>
					<span class="flex-1 truncate">{{ cat.name }}</span>
					<svg
						v-if="cat.hasChildren"
						class="h-3 w-3 shrink-0 text-slate-300"
						viewBox="0 0 12 12"
						fill="currentColor"
					>
						<path d="M4 5l2 2 2-2" />
					</svg>
				</div>
			</div>
		</div>

		<div class="mx-3 border-t border-slate-100"></div>

		<!-- EDİTör / Additional links -->
		<div class="p-3">
			<p class="mb-2 text-[10px] font-semibold uppercase tracking-wide text-slate-400">
				EDİTör 목
			</p>
			<div class="space-y-0.5">
				<RouterLink
					:to="{ name: 'create' }"
					class="flex items-center gap-2 rounded px-2 py-1.5 text-xs text-slate-600 no-underline transition hover:bg-slate-50"
				>
					<span class="h-2 w-2 shrink-0 rounded-full bg-yellow-400"></span>
					<span class="truncate">문제 만들기</span>
					<svg class="h-3 w-3 shrink-0 text-slate-300" viewBox="0 0 12 12" fill="currentColor">
						<path d="M4 5l2 2 2-2" />
					</svg>
				</RouterLink>
				<RouterLink
					:to="{ name: 'community' }"
					class="flex items-center gap-2 rounded px-2 py-1.5 text-xs text-slate-600 no-underline transition hover:bg-slate-50"
				>
					<span class="h-2 w-2 shrink-0 rounded-full bg-yellow-400"></span>
					<span class="truncate">커뮤니티</span>
				</RouterLink>
				<RouterLink
					:to="{ name: 'mypage' }"
					class="flex items-center gap-2 rounded px-2 py-1.5 text-xs text-slate-600 no-underline transition hover:bg-slate-50"
				>
					<span class="h-2 w-2 shrink-0 rounded-full bg-blue-400"></span>
					<span class="truncate">마이페이지</span>
				</RouterLink>
			</div>
		</div>

		<!-- Bottom profile link -->
		<div class="mt-auto border-t border-slate-100 p-3">
			<RouterLink
				:to="{ name: 'mypage' }"
				class="flex items-center gap-2 rounded px-2 py-1.5 text-xs text-slate-400 no-underline transition hover:bg-slate-50 hover:text-slate-600"
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
				<span>마이페이지</span>
			</RouterLink>
		</div>
	</aside>
</template>

<script setup>
const recentProblems = [
	{ id: 1, title: "정보처리기사 SQL", color: "bg-blue-500" },
	{ id: 2, title: "책재 체형", color: "bg-blue-400" },
	{ id: 3, title: "롯재", color: "bg-green-400" },
	{ id: 4, title: "책재", color: "bg-blue-500" },
];

const categories = [
	{ name: "정보처리기사", color: "bg-yellow-400", hasChildren: true },
	{ name: "AWS SA", color: "bg-yellow-400", hasChildren: false },
	{ name: "SQLD", color: "bg-blue-400", hasChildren: false },
];
</script>

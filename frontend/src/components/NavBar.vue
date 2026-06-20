<template>
	<nav class="flex h-12 shrink-0 items-center gap-3 bg-[#1d3461] px-4 text-white">
		<!-- Logo -->
		<RouterLink
			:to="{ name: 'home' }"
			class="flex shrink-0 items-center gap-2 text-white no-underline"
		>
			<div class="flex h-6 w-6 items-center justify-center rounded bg-white/20 text-[10px] font-bold">
				P
			</div>
			<span class="text-sm font-bold tracking-tight">PASSIT</span>
		</RouterLink>

		<div class="h-4 w-px shrink-0 bg-white/20"></div>

		<!-- Spacer -->
		<div class="flex-1"></div>

		<!-- Right nav -->
		<div class="flex shrink-0 items-center gap-4">
			<RouterLink
				v-for="item in navItems"
				:key="item.name"
				:to="{ name: item.name }"
				active-class="text-white"
				class="text-xs font-medium text-white/70 no-underline transition hover:text-white"
			>
				{{ item.label }}
			</RouterLink>

			<!-- 관리자 전용 링크 -->
			<RouterLink
				v-if="authStore.isAdmin"
				:to="{ name: 'admin-reports' }"
				class="rounded bg-orange-500/80 px-2 py-0.5 text-[11px] font-semibold text-white no-underline transition hover:bg-orange-500"
			>
				신고 관리
			</RouterLink>

			<div class="h-4 w-px bg-white/20"></div>

			<!-- Avatar (마이페이지 링크) -->
			<RouterLink
				v-if="authStore.hasAccessToken"
				:to="{ name: 'mypage' }"
				class="flex h-7 w-7 items-center justify-center rounded-full bg-white/20 no-underline transition hover:bg-white/30"
			>
				<svg class="h-4 w-4 text-white" viewBox="0 0 16 16" fill="none" stroke="currentColor" stroke-width="1.6">
					<circle cx="8" cy="5" r="3" />
					<path d="M2 14c0-3.3 2.7-6 6-6s6 2.7 6 6" stroke-linecap="round" />
				</svg>
			</RouterLink>
			<RouterLink
				v-else
				:to="{ name: 'login' }"
				class="rounded border border-white/30 px-3 py-1 text-xs font-semibold text-white no-underline transition hover:border-white/50 hover:bg-white/10"
			>
				로그인
			</RouterLink>
		</div>
	</nav>
</template>

<script setup>
import { useAuthStore } from "@/stores/auth";

const authStore = useAuthStore();

const navItems = [
	{ name: "home", label: "홈" },
	{ name: "problem", label: "문제" },
	{ name: "create", label: "만들기" },
	{ name: "community", label: "커뮤니티" },
];
</script>

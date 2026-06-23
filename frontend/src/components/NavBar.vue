<template>
	<nav class="flex h-14 shrink-0 items-center gap-4 bg-gradient-to-r from-[#0d1e3a] via-[#1d3461] to-[#1a3a6e] px-5 text-white shadow-lg shadow-slate-900/25">
		<!-- Logo -->
		<RouterLink
			:to="{ name: 'home' }"
			class="flex shrink-0 items-center gap-2.5 text-white no-underline"
		>
			<div class="flex h-8 w-8 items-center justify-center rounded-lg bg-gradient-to-br from-blue-400/50 to-blue-600/50 text-sm font-extrabold ring-1 ring-white/25 shadow-inner">
				P
			</div>
			<span class="text-base font-bold tracking-widest">PASSIT</span>
		</RouterLink>

		<div class="h-5 w-px shrink-0 bg-white/20"></div>

		<!-- Spacer -->
		<div class="flex-1"></div>

		<!-- Right nav -->
		<div class="flex shrink-0 items-center gap-5">
			<RouterLink
				v-for="item in navItems"
				:key="item.name"
				:to="{ name: item.name }"
				active-class="text-white after:scale-x-100"
				class="relative text-sm font-medium text-white/65 no-underline transition hover:text-white after:absolute after:-bottom-0.5 after:inset-x-0 after:h-px after:origin-left after:scale-x-0 after:bg-white/60 after:transition-transform after:duration-200"
			>
				{{ item.label }}
			</RouterLink>

			<!-- 관리자 전용 링크 -->
			<RouterLink
				v-if="authStore.isAdmin"
				:to="{ name: 'admin-reports' }"
				class="rounded bg-orange-500/80 px-2.5 py-1 text-xs font-semibold text-white no-underline transition hover:bg-orange-500"
			>
				신고 관리
			</RouterLink>

			<div class="h-5 w-px bg-white/20"></div>

			<!-- Avatar (마이페이지 링크) -->
			<RouterLink
				v-if="authStore.hasAccessToken"
				:to="{ name: 'mypage' }"
				class="flex h-8 w-8 items-center justify-center rounded-full bg-white/20 no-underline transition hover:bg-white/30"
			>
				<svg class="h-4.5 w-4.5 text-white" viewBox="0 0 16 16" fill="none" stroke="currentColor" stroke-width="1.6">
					<circle cx="8" cy="5" r="3" />
					<path d="M2 14c0-3.3 2.7-6 6-6s6 2.7 6 6" stroke-linecap="round" />
				</svg>
			</RouterLink>
			<RouterLink
				v-else
				:to="{ name: 'login' }"
				class="rounded-md border border-white/30 px-3.5 py-1.5 text-sm font-semibold text-white no-underline transition hover:border-white/50 hover:bg-white/10"
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

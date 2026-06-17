<template>
	<nav class="flex h-12 shrink-0 items-center gap-3 bg-[#1d3461] px-4 text-white">
		<!-- Logo -->
		<div class="flex items-center gap-2 shrink-0">
			<div
				class="flex h-6 w-6 items-center justify-center rounded bg-white/20 text-[10px] font-bold"
			>
				P
			</div>
			<span class="text-sm font-bold tracking-tight">PASSIT</span>
		</div>

		<div class="h-4 w-px shrink-0 bg-white/20"></div>

		<!-- Center search -->
		<div class="flex flex-1 items-center">
			<input
				type="search"
				placeholder="문제 검색..."
				class="h-7 w-full max-w-sm rounded border border-white/15 bg-white/10 px-3 text-xs text-white outline-none placeholder:text-white/40 focus:border-white/30 focus:bg-white/15"
			/>
		</div>

		<!-- Right nav -->
		<div class="flex items-center gap-4 shrink-0">
			<RouterLink
				v-for="item in navItems"
				:key="item.name"
				:to="{ name: item.name }"
				active-class="text-white"
				class="text-xs font-medium text-white/70 no-underline transition hover:text-white"
			>
				{{ item.label }}
			</RouterLink>

			<div class="h-4 w-px bg-white/20"></div>

			<!-- Notification bell -->
			<button class="relative text-white/70 transition hover:text-white">
				<svg
					class="h-4 w-4"
					viewBox="0 0 16 16"
					fill="none"
					stroke="currentColor"
					stroke-width="1.6"
				>
					<path d="M8 1.5a5 5 0 0 1 5 5v2.5l1 2H2l1-2V6.5a5 5 0 0 1 5-5z" />
					<path d="M6.5 13.5a1.5 1.5 0 0 0 3 0" stroke-linecap="round" />
				</svg>
				<span
					class="absolute -right-0.5 -top-0.5 flex h-3 w-3 items-center justify-center rounded-full bg-red-400 text-[8px] font-bold text-white"
				>
					1
				</span>
			</button>

			<!-- Avatar -->
			<button
				class="flex h-7 w-7 items-center justify-center rounded-full bg-orange-400 text-[11px] font-bold text-white shadow-sm"
			>
				김
			</button>
		</div>
	</nav>
</template>

<script setup>
const navItems = [
	{ name: "home", label: "홈" },
	{ name: "problem", label: "문제" },
	{ name: "create", label: "만들기" },
	{ name: "community", label: "커뮤니티" },
];
</script>

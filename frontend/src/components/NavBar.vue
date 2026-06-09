<template>
	<nav class="border-b border-white/15 bg-blue-600 text-white shadow-md">
		<div class="mx-auto flex h-14 max-w-full items-center justify-between px-4">
			<div class="flex min-w-0 items-center gap-3">
				<RouterLink
					:to="{ name: 'home' }"
					class="shrink-0 whitespace-nowrap text-lg font-bold tracking-tight text-white no-underline md:text-xl"
				>
					PASSIT
				</RouterLink>

				<div class="hidden items-center gap-1.5 md:flex">
					<RouterLink
						v-for="item in menuItems"
						:key="item.name"
						:to="{ name: item.name }"
						active-class="bg-white/15 text-white"
						exact-active-class="bg-white/15 text-white"
						class="inline-flex items-center gap-1.5 rounded-md px-3 py-2 text-sm font-medium text-white/90 no-underline transition hover:bg-white/10 hover:text-white"
					>
						<img :src="item.icon" :alt="item.label" class="h-4 w-4 shrink-0" />
						<span>{{ item.label }}</span>
					</RouterLink>
				</div>
			</div>

			<RouterLink
				:to="{ name: 'login' }"
				active-class="bg-white/15 border-white"
				exact-active-class="bg-white/15 border-white"
				class="inline-flex h-9 shrink-0 items-center justify-center rounded-sm border border-white/70 px-4 text-sm font-semibold text-white no-underline transition hover:border-white hover:bg-white/10"
			>
				로그인
			</RouterLink>
		</div>
	</nav>
</template>

<script setup>
const menuItems = [
	{ name: "home", label: "홈", icon: "/home.svg" },
	{ name: "problem", label: "문제 풀기", icon: "/problem.svg" },
	{ name: "create", label: "문제 만들기", icon: "/add.svg" },
	{ name: "community", label: "커뮤니티", icon: "/community.svg" },
];
</script>

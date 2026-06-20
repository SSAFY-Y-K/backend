<template>
	<section class="min-h-full p-4">
		<div class="mb-4 flex items-center gap-2">
			<button
				v-if="isChildRoute"
				class="text-xs text-slate-400 transition hover:text-slate-600"
				@click="goBack"
			>
				← 뒤로
			</button>
			<h3 class="text-sm font-bold text-slate-700">
				{{ pageTitle }}
			</h3>
		</div>

		<RouterView v-if="isChildRoute" />

		<div v-else class="grid grid-cols-2 gap-4">
			<button
				class="group flex flex-col items-center justify-center gap-4 rounded-xl border-2 border-slate-200 bg-white p-8 shadow-sm transition hover:border-blue-300 hover:shadow-md"
				@click="goCertification"
			>
				<div class="flex h-20 w-20 items-center justify-center rounded-full border-4 border-blue-100 bg-blue-50 transition group-hover:border-blue-200">
					<svg class="h-10 w-10 text-blue-500" viewBox="0 0 40 40" fill="none" stroke="currentColor" stroke-width="1.8">
						<rect x="6" y="4" width="28" height="32" rx="3" />
						<path d="M12 14h16M12 20h16M12 26h10" stroke-linecap="round" />
						<circle cx="30" cy="30" r="6" fill="white" stroke="currentColor" stroke-width="1.8" />
						<path d="M27.5 30l2 2 3-3" stroke-linecap="round" stroke-linejoin="round" />
					</svg>
				</div>
				<div class="text-center">
					<h3 class="text-base font-bold text-slate-800">자격증 문제</h3>
					<p class="mt-1 text-xs text-slate-400">객관식 · 주관식 문제를 만들어요</p>
				</div>
			</button>

			<button
				class="group flex flex-col items-center justify-center gap-4 rounded-xl border-2 border-slate-200 bg-white p-8 shadow-sm transition hover:border-purple-300 hover:shadow-md"
				@click="goCoding"
			>
				<div class="flex h-20 w-20 items-center justify-center rounded-full border-4 border-purple-100 bg-purple-50 transition group-hover:border-purple-200">
					<svg class="h-10 w-10 text-purple-500" viewBox="0 0 40 40" fill="none" stroke="currentColor" stroke-width="1.8">
						<path d="M14 15l-6 5 6 5M26 15l6 5-6 5" stroke-linecap="round" stroke-linejoin="round" />
						<path d="M22 10l-4 20" stroke-linecap="round" />
					</svg>
				</div>
				<div class="text-center">
					<h3 class="text-base font-bold text-slate-800">코딩 문제</h3>
					<p class="mt-1 text-xs text-slate-400">AI가 알고리즘 문제를 생성해요</p>
				</div>
			</button>
		</div>
	</section>
</template>

<script setup>
import { computed } from "vue";
import { useRoute, useRouter } from "vue-router";

const route = useRoute();
const router = useRouter();

const isChildRoute = computed(() => route.name !== "create");

const pageTitle = computed(() => {
	if (route.name === "create-certification") return "자격증 문제";
	if (route.name === "create-coding") return "코딩 문제";
	return "문제 만들기";
});

const goBack = () => {
	router.push({ name: "create" });
};

const goCertification = () => {
	router.push({ name: "create-certification" });
};

const goCoding = () => {
	router.push({ name: "create-coding" });
};
</script>

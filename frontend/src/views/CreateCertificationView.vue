<template>
	<div class="w-full">
		<RouterView v-if="isWriteRoute" />

		<div v-else class="grid grid-cols-2 gap-4">
			<button
				class="group flex flex-col items-center justify-center gap-4 rounded-xl border-2 border-slate-200 bg-white p-8 shadow-sm transition hover:border-blue-300 hover:shadow-md"
				@click="goWrite('ai')"
			>
				<div class="flex h-20 w-20 items-center justify-center rounded-full border-4 border-blue-100 bg-blue-50 transition group-hover:border-blue-200">
					<svg class="h-10 w-10 text-blue-500" viewBox="0 0 40 40" fill="none" stroke="currentColor" stroke-width="1.8">
						<circle cx="20" cy="20" r="12" />
						<path d="M14 20h12M20 14v12" stroke-linecap="round" />
						<circle cx="20" cy="20" r="3" fill="currentColor" />
					</svg>
				</div>
				<div class="text-center">
					<h3 class="text-base font-bold text-slate-800">AI 모드</h3>
					<p class="mt-1 text-xs text-slate-400">AI가 자동으로 문제를 만들어요</p>
				</div>
			</button>

			<button
				class="group flex flex-col items-center justify-center gap-4 rounded-xl border-2 border-slate-200 bg-white p-8 shadow-sm transition hover:border-slate-300 hover:shadow-md"
				@click="goWrite('manual')"
			>
				<div class="flex h-20 w-20 items-center justify-center rounded-full border-4 border-slate-100 bg-slate-50 transition group-hover:border-slate-200">
					<svg class="h-10 w-10 text-slate-500" viewBox="0 0 40 40" fill="none" stroke="currentColor" stroke-width="1.8">
						<rect x="8" y="6" width="24" height="28" rx="3" />
						<path d="M14 15h12M14 20h12M14 25h8" stroke-linecap="round" />
					</svg>
				</div>
				<div class="text-center">
					<h3 class="text-base font-bold text-slate-800">직접 모드</h3>
					<p class="mt-1 text-xs text-slate-400">직접 문제를 작성해요</p>
				</div>
			</button>
		</div>
	</div>
</template>

<script setup>
import { computed } from "vue";
import { useRoute, useRouter } from "vue-router";

const route = useRoute();
const router = useRouter();

const isWriteRoute = computed(() => route.name === "create-certification-write");

const goWrite = (mode) => {
	router.push({
		name: "create-certification-write",
		query: { mode },
	});
};
</script>

<template>
	<section class="min-h-full p-4">
		<div class="grid gap-4 lg:grid-cols-[1fr_220px]">
			<!-- Left: Problem content -->
			<div class="space-y-3">
				<!-- Header -->
				<div class="flex items-center justify-between">
					<h3 class="text-sm font-bold text-slate-700">문제</h3>
					<button
						class="flex items-center gap-1 rounded-md border border-slate-200 bg-white px-3 py-1.5 text-xs font-medium text-slate-600 shadow-sm transition hover:bg-slate-50"
					>
						<svg class="h-3 w-3" viewBox="0 0 12 12" fill="none" stroke="currentColor" stroke-width="1.5">
							<path d="M2 6h8M6 2l4 4-4 4" stroke-linecap="round" stroke-linejoin="round" />
						</svg>
						+ 북마크 관련
					</button>
				</div>

				<!-- Problem description card -->
				<div class="rounded-lg border border-slate-200 bg-white p-4 shadow-sm">
					<h4 class="mb-2 text-xs font-semibold text-slate-500">문제 설명</h4>
					<p class="text-xs leading-relaxed text-slate-700">
						팜대 국색 경쟁자 달성과에 관한 다양한 유행별 팜달을 달아본다. 자재내 팜달 경쟁의 달성과에
						관한 다양한 팜달별별별팜 달아다면서 통계에 관한 다양한 적절한 정답에 답하여 한합니다.
					</p>
					<p class="mt-2 text-xs leading-relaxed text-slate-700">
						이하의 팔랑 답하여여 타달당 답답당답 정답하다면하다면 답하여여 한합니다.
					</p>

					<!-- Answer choices list -->
					<div class="mt-4 space-y-1.5">
						<div
							v-for="choice in choices"
							:key="choice.key"
							class="flex items-center gap-2 text-xs text-slate-700"
						>
							<span class="font-semibold text-slate-400">{{ choice.key }}.</span>
							<span>{{ choice.text }}</span>
						</div>
					</div>
				</div>

				<!-- 해설 열람 (collapsible) -->
				<div class="rounded-lg border border-slate-200 bg-white shadow-sm">
					<button
						class="flex w-full items-center justify-between px-4 py-3 text-xs font-semibold text-slate-700 transition hover:bg-slate-50"
						@click="showExplanation = !showExplanation"
					>
						해설 열람
						<svg
							:class="['h-3.5 w-3.5 text-slate-400 transition', showExplanation ? 'rotate-180' : '']"
							viewBox="0 0 14 14"
							fill="currentColor"
						>
							<path d="M3 5l4 4 4-4" />
						</svg>
					</button>
					<div v-if="showExplanation" class="border-t border-slate-100 p-4">
						<p class="text-xs leading-relaxed text-slate-600">
							세부 팔랑들은 달아본다에서의 과한 다고 달성과에 관한 달성과에 관한 적절한 답을 하여
							달성과에 한합니다. 자재내 달성과에 대한 팔랑들에 관한 다양한 적절한 적절한 것다.
							Kg0gaten(이하 이와 이와 관련히여 팔랑팔랑에 팔랑답팔랑합니다.)
						</p>
					</div>
				</div>
			</div>

			<!-- Right: Answer selection -->
			<div class="space-y-3">
				<!-- Answer selector card -->
				<div class="rounded-lg border border-slate-200 bg-white p-4 shadow-sm">
					<div class="space-y-2">
						<label
							v-for="opt in options"
							:key="opt.value"
							:class="[
								'flex cursor-pointer items-center gap-2.5 rounded-md border px-3 py-2 text-xs transition',
								selected === opt.value
									? 'border-blue-400 bg-blue-50 text-blue-700'
									: 'border-slate-200 text-slate-700 hover:border-slate-300 hover:bg-slate-50',
							]"
							@click="selected = opt.value"
						>
							<span
								:class="[
									'flex h-4 w-4 shrink-0 items-center justify-center rounded-full border text-[10px] font-bold transition',
									selected === opt.value
										? 'border-blue-500 bg-blue-500 text-white'
										: 'border-slate-300 text-transparent',
								]"
							>
								O
							</span>
							<span>{{ opt.label }}</span>
						</label>
					</div>

					<!-- Submit button -->
					<button
						class="mt-4 h-8 w-full rounded-md bg-blue-600 text-xs font-semibold text-white transition hover:bg-blue-700"
					>
						제출
					</button>
				</div>
			</div>
		</div>
	</section>
</template>

<script setup>
import { ref } from "vue";

const selected = ref(null);
const showExplanation = ref(false);

const choices = [
	{ key: "A", text: "서재 A" },
	{ key: "B", text: "서재 B" },
	{ key: "C", text: "서재 C" },
	{ key: "D", text: "서재 없다" },
];

const options = [
	{ value: "A", label: "A A" },
	{ value: "B", label: "B S" },
	{ value: "C", label: "C" },
	{ value: "N", label: "N G" },
];
</script>

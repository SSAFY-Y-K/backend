<template>
	<section class="min-h-full bg-gray-50">
		<div class="grid h-full min-h-[calc(100vh-3rem)] grid-cols-[200px_1fr]">
			<!-- Left profile sidebar -->
			<div class="border-r border-slate-200 bg-white p-4">
				<!-- Profile photo -->
				<div class="mb-3 flex justify-center">
					<div
						class="flex h-20 w-20 items-center justify-center overflow-hidden rounded-full bg-gradient-to-br from-orange-300 to-orange-500 ring-2 ring-orange-100"
					>
						<svg viewBox="0 0 80 80" fill="none" class="h-full w-full">
							<rect width="80" height="80" fill="url(#mpGrad)" />
							<circle cx="40" cy="28" r="14" fill="white" fill-opacity="0.85" />
							<path d="M10 74c0-16.6 13.4-30 30-30s30 13.4 30 30" fill="white" fill-opacity="0.85" />
							<defs>
								<linearGradient id="mpGrad" x1="0" y1="0" x2="80" y2="80">
									<stop stop-color="#fb923c" />
									<stop offset="1" stop-color="#ea580c" />
								</linearGradient>
							</defs>
						</svg>
					</div>
				</div>

				<!-- Name badge -->
				<div class="mb-3 rounded-md bg-slate-700 px-3 py-1.5 text-center">
					<p class="text-xs font-bold text-white">파라하시 흉</p>
					<p class="text-[10px] text-slate-400">BeonyStr</p>
				</div>

				<!-- Stats -->
				<div class="mb-3 space-y-1 text-[11px] text-slate-500">
					<div class="flex justify-between">
						<span>파라하치</span>
						<span class="font-semibold text-slate-700">공부↑+</span>
					</div>
					<div class="flex justify-between">
						<span>3일 뿌리 등복 3%의</span>
						<span class="font-semibold text-slate-700">540% 63%</span>
					</div>
				</div>

				<div class="mb-4 border-t border-slate-100"></div>

				<!-- Links -->
				<div class="space-y-1">
					<button class="flex w-full items-center gap-2 rounded px-2 py-1.5 text-xs text-blue-600 transition hover:bg-blue-50">
						최근 풀기 이력
						<svg class="ml-auto h-3 w-3" viewBox="0 0 12 12" fill="none" stroke="currentColor" stroke-width="1.5">
							<path d="M4 9l4-3-4-3" stroke-linecap="round" stroke-linejoin="round" />
						</svg>
					</button>
					<button class="flex w-full items-center gap-2 rounded px-2 py-1.5 text-xs text-slate-500 transition hover:bg-slate-50">
						3일 팔랑팔이 이달 / 1달팔이
					</button>
				</div>

				<div class="mt-4 border-t border-slate-100"></div>

				<!-- Bottom list: 32일 3래 -->
				<div class="mt-3">
					<p class="mb-2 text-[10px] font-semibold text-slate-400">32일 3래</p>
					<div class="space-y-1.5">
						<div
							v-for="item in bottomList"
							:key="item.id"
							class="flex items-center gap-2 text-[11px]"
						>
							<span :class="['h-2 w-2 shrink-0 rounded-sm', item.color]"></span>
							<span class="flex-1 truncate text-slate-600">{{ item.title }}</span>
							<span :class="['rounded px-1 py-0.5 text-[9px] font-bold text-white', item.badge]">
								{{ item.level }}
							</span>
						</div>
					</div>
				</div>
			</div>

			<!-- Right main content -->
			<div class="overflow-y-auto p-4">
				<!-- Profile card -->
				<div class="mb-4 rounded-lg border border-slate-200 bg-white p-4 shadow-sm">
					<h3 class="mb-3 text-xs font-semibold text-slate-500">프로필 카드</h3>
					<div class="flex items-center gap-4">
						<div
							class="flex h-14 w-14 shrink-0 items-center justify-center overflow-hidden rounded-full bg-orange-100"
						>
							<svg viewBox="0 0 56 56" fill="none" class="h-full w-full">
								<rect width="56" height="56" fill="#fed7aa" />
								<circle cx="28" cy="19" r="9" fill="#ea580c" fill-opacity="0.7" />
								<path d="M6 50c0-12 9.8-20 22-20s22 8 22 20" fill="#ea580c" fill-opacity="0.7" />
							</svg>
						</div>
						<div class="flex-1">
							<div class="flex items-center gap-2">
								<p class="text-sm font-bold text-slate-800">닷날예 흉</p>
							</div>
							<p class="text-xs text-slate-400">빌밀온 흉</p>
							<div class="mt-2 flex items-center gap-3">
								<div class="flex-1">
									<div class="mb-1 flex items-center justify-between text-[10px]">
										<span class="text-slate-500">레벨/AP</span>
										<span class="font-semibold text-slate-700">50</span>
									</div>
									<div class="h-2 overflow-hidden rounded-full bg-slate-100">
										<div class="h-full w-1/2 rounded-full bg-blue-500"></div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>

				<!-- Charts row -->
				<div class="mb-4 grid grid-cols-2 gap-4">
					<!-- Chart 1 -->
					<div class="rounded-lg border border-slate-200 bg-white p-3 shadow-sm">
						<div class="mb-2 flex items-center justify-between">
							<h4 class="text-[10px] font-semibold text-slate-500">풀이 통계</h4>
							<span class="text-[10px] text-blue-500">풀이수량</span>
						</div>
						<!-- Bar chart placeholder -->
						<div class="flex items-end gap-1 h-24">
							<div v-for="(bar, i) in chart1Bars" :key="i" class="flex flex-1 flex-col items-center gap-0.5">
								<div
									:class="['w-full rounded-t', bar.color]"
									:style="{ height: bar.height }"
								></div>
								<span class="text-[8px] text-slate-400">{{ bar.label }}</span>
							</div>
						</div>
					</div>

					<!-- Chart 2 -->
					<div class="rounded-lg border border-slate-200 bg-white p-3 shadow-sm">
						<div class="mb-2 flex items-center justify-between">
							<h4 class="text-[10px] font-semibold text-slate-500">풀이 통계</h4>
							<span class="text-[10px] text-blue-500">풀이수량</span>
						</div>
						<!-- Bar chart placeholder -->
						<div class="flex items-end gap-1 h-24">
							<div v-for="(bar, i) in chart2Bars" :key="i" class="flex flex-1 flex-col items-center gap-0.5">
								<div
									:class="['w-full rounded-t', bar.color]"
									:style="{ height: bar.height }"
								></div>
								<span class="text-[8px] text-slate-400">{{ bar.label }}</span>
							</div>
						</div>
					</div>
				</div>

				<!-- History table -->
				<div class="rounded-lg border border-slate-200 bg-white shadow-sm">
					<div class="border-b border-slate-100 px-4 py-2">
						<h4 class="text-xs font-semibold text-slate-600">풀이 히스토리</h4>
					</div>
					<table class="w-full text-[11px]">
						<thead>
							<tr class="border-b border-slate-100 bg-slate-50 text-slate-400">
								<th class="px-4 py-2 text-left font-medium">문제</th>
								<th class="px-3 py-2 text-center font-medium">비율%</th>
								<th class="px-3 py-2 text-center font-medium">풀이%</th>
								<th class="px-3 py-2 text-center font-medium">확인%S</th>
								<th class="px-3 py-2 text-center font-medium">2.30%/족</th>
							</tr>
						</thead>
						<tbody class="divide-y divide-slate-50">
							<tr
								v-for="row in historyRows"
								:key="row.id"
								class="transition hover:bg-slate-50"
							>
								<td class="px-4 py-2">
									<div class="flex items-center gap-2">
										<div class="h-6 w-6 shrink-0 overflow-hidden rounded-full bg-slate-200">
											<svg viewBox="0 0 24 24" fill="none" class="h-full w-full">
												<rect width="24" height="24" fill="#e2e8f0" />
												<circle cx="12" cy="8" r="4" fill="#94a3b8" />
												<path d="M4 20c0-4.4 3.6-8 8-8s8 3.6 8 8" fill="#94a3b8" />
											</svg>
										</div>
										<span class="text-slate-700">{{ row.title }}</span>
									</div>
								</td>
								<td class="px-3 py-2 text-center">
									<span :class="['rounded px-1.5 py-0.5 text-[9px] font-bold text-white', row.levelColor]">
										{{ row.ratio }}
									</span>
								</td>
								<td class="px-3 py-2 text-center text-slate-500">{{ row.solve }}</td>
								<td class="px-3 py-2 text-center text-slate-500">{{ row.check }}</td>
								<td class="px-3 py-2 text-center text-slate-500">{{ row.final }}</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</section>
</template>

<script setup>
const bottomList = [
	{ id: 1, title: "롯재탈재탈재", color: "bg-blue-500", level: "중급", badge: "bg-orange-500" },
	{ id: 2, title: "롯재롯재롯재", color: "bg-blue-400", level: "상급", badge: "bg-red-500" },
	{ id: 3, title: "롯재롯재롯재롯재", color: "bg-green-400", level: "초급", badge: "bg-emerald-500" },
	{ id: 4, title: "롯재 롯재", color: "bg-blue-500", level: "중급", badge: "bg-orange-500" },
	{ id: 5, title: "이5탈탈탈탈재", color: "bg-red-400", level: "상급", badge: "bg-red-500" },
];

const chart1Bars = [
	{ label: "1월", height: "30%", color: "bg-blue-400" },
	{ label: "2월", height: "55%", color: "bg-blue-500" },
	{ label: "3월", height: "45%", color: "bg-blue-400" },
	{ label: "4월", height: "80%", color: "bg-blue-600" },
	{ label: "5월", height: "60%", color: "bg-blue-400" },
	{ label: "6월", height: "90%", color: "bg-blue-700" },
];

const chart2Bars = [
	{ label: "1월", height: "40%", color: "bg-green-400" },
	{ label: "2월", height: "70%", color: "bg-green-500" },
	{ label: "3월", height: "50%", color: "bg-green-400" },
	{ label: "4월", height: "65%", color: "bg-green-600" },
	{ label: "5월", height: "85%", color: "bg-green-500" },
	{ label: "6월", height: "45%", color: "bg-green-400" },
];

const historyRows = [
	{ id: 1, title: "A치 자격적", ratio: "2.58%", solve: "5.9%", check: "35.7%", final: "35.7%", levelColor: "bg-emerald-500" },
	{ id: 2, title: "A+시 자격적", ratio: "2.59%", solve: "5.8%", check: "35.7%", final: "35.7%", levelColor: "bg-orange-500" },
	{ id: 3, title: "팰의 자격적", ratio: "2.56%", solve: "5.5%", check: "35.7%", final: "35.7%", levelColor: "bg-emerald-500" },
	{ id: 4, title: "팰제, 재지", ratio: "2.56%", solve: "2.5%", check: "26.7%", final: "26.7%", levelColor: "bg-red-500" },
	{ id: 5, title: "팰제, 재지", ratio: "2.56%", solve: "2.5%", check: "25.7%", final: "25.7%", levelColor: "bg-orange-500" },
	{ id: 6, title: "팰제, 재지", ratio: "2.56%", solve: "2.5%", check: "25.3%", final: "25.3%", levelColor: "bg-emerald-500" },
];
</script>

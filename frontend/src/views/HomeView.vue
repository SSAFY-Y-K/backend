<template>
	<section class="min-h-full p-4">
		<!-- Recent problems table -->
		<div class="mb-5">
			<h3 class="mb-2 text-xs font-semibold text-slate-500">최근 문제목록</h3>
			<div class="overflow-hidden rounded-lg border border-slate-200 bg-white shadow-sm">
				<table class="w-full text-xs">
					<thead>
						<tr class="border-b border-slate-100 bg-slate-50 text-slate-400">
							<th class="px-4 py-2 text-left font-medium">문제이름</th>
							<th class="px-3 py-2 text-center font-medium">풀이수</th>
							<th class="px-3 py-2 text-center font-medium">정답률</th>
							<th class="px-3 py-2 text-center font-medium">난이도</th>
							<th class="px-3 py-2 text-center font-medium">포인트</th>
						</tr>
					</thead>
					<tbody class="divide-y divide-slate-50">
						<tr
							v-for="row in recentProblems"
							:key="row.id"
							class="transition hover:bg-slate-50/60"
						>
							<td class="px-4 py-2 font-medium text-slate-700">{{ row.title }}</td>
							<td class="px-3 py-2 text-center text-slate-500">{{ row.solvedCount }}</td>
							<td class="px-3 py-2 text-center">
								<span class="font-semibold text-blue-600">{{ row.correctRate }}%</span>
							</td>
							<td class="px-3 py-2 text-center">
								<span
									:class="['rounded px-1.5 py-0.5 text-[10px] font-bold text-white', levelColor[row.level]]"
								>
									{{ row.level }}
								</span>
							</td>
							<td class="px-3 py-2 text-center text-slate-500">{{ row.points }}</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>

		<!-- Second table -->
		<div class="mb-5">
			<div class="overflow-hidden rounded-lg border border-slate-200 bg-white shadow-sm">
				<table class="w-full text-xs">
					<thead>
						<tr class="border-b border-slate-100 bg-slate-50 text-slate-400">
							<th class="px-4 py-2 text-left font-medium">문제이름</th>
							<th class="px-3 py-2 text-center font-medium">풀이수</th>
							<th class="px-3 py-2 text-center font-medium">팰팰팰 정답률</th>
							<th class="px-3 py-2 text-center font-medium">등록</th>
						</tr>
					</thead>
					<tbody class="divide-y divide-slate-50">
						<tr
							v-for="row in extraProblems"
							:key="row.id"
							class="transition hover:bg-slate-50/60"
						>
							<td class="px-4 py-2 font-medium text-slate-700">{{ row.title }}</td>
							<td class="px-3 py-2 text-center text-slate-500">{{ row.solvedCount }}</td>
							<td class="px-3 py-2 text-center text-slate-500">{{ row.correctRate }}%</td>
							<td class="px-3 py-2 text-center text-slate-400">{{ row.status }}</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>

		<!-- Popular cert cards -->
		<div>
			<div class="mb-2 flex items-center justify-between">
				<h3 class="text-xs font-semibold text-slate-500">인기 자격증 카드</h3>
				<button class="text-[10px] font-medium text-blue-500 transition hover:text-blue-600">
					전체 보기
				</button>
			</div>

			<div class="grid grid-cols-3 gap-3">
				<div
					v-for="cert in popularCerts"
					:key="cert.id"
					class="overflow-hidden rounded-lg border border-slate-200 bg-white shadow-sm transition hover:shadow-md"
				>
					<!-- Thumbnail -->
					<div :class="['flex h-20 items-center justify-center', cert.thumbBg]">
						<span class="text-2xl font-black text-white/80">{{ cert.icon }}</span>
					</div>
					<!-- Info -->
					<div class="p-2.5">
						<p class="text-[11px] font-semibold text-slate-700">{{ cert.name }}</p>
						<div class="mt-1 flex items-center justify-between">
							<span class="text-[10px] text-slate-400">{{ cert.count }}문제</span>
							<span
								:class="['rounded px-1.5 py-0.5 text-[10px] font-bold text-white', cert.badge]"
							>
								{{ cert.level }}
							</span>
						</div>
					</div>
				</div>
			</div>
		</div>
	</section>
</template>

<script setup>
const recentProblems = [
	{ id: 1, title: "롯재 체형", solvedCount: "2.39%", correctRate: 3.5, level: "초급", points: "3.53%" },
	{ id: 2, title: "책재 체형", solvedCount: "2.35%", correctRate: 3.5, level: "중급", points: "3.35%" },
	{ id: 3, title: "롯재, 책재", solvedCount: "2.89%", correctRate: 2.5, level: "상급", points: "2.85%" },
	{ id: 4, title: "롯재, 책재", solvedCount: "2.80%", correctRate: 3.5, level: "중급", points: "2.85%" },
];

const extraProblems = [
	{ id: 1, title: "A+B 팰릴", solvedCount: "2t+B", correctRate: 233, status: "233%" },
	{ id: 2, title: "A씨 팰릴팰릴", solvedCount: "JX", correctRate: 233, status: "233%" },
	{ id: 3, title: "A씨 팰릴", solvedCount: "S", correctRate: 233, status: "233%" },
];

const popularCerts = [
	{ id: 1, name: "10% 목매형", count: 125, level: "중급", icon: "%", thumbBg: "bg-gradient-to-br from-yellow-300 to-orange-400", badge: "bg-orange-500" },
	{ id: 2, name: "10% 목매형", count: 89, level: "초급", icon: "#", thumbBg: "bg-gradient-to-br from-green-300 to-emerald-500", badge: "bg-emerald-500" },
	{ id: 3, name: "18% 목해형", count: 56, level: "상급", icon: "∑", thumbBg: "bg-gradient-to-br from-blue-300 to-blue-500", badge: "bg-blue-500" },
];

const levelColor = {
	초급: "bg-emerald-500",
	중급: "bg-orange-500",
	상급: "bg-red-500",
};
</script>

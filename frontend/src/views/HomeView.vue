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
							<th class="px-3 py-2 text-center font-medium">난이도</th>
							<th class="px-3 py-2 text-center font-medium">유형</th>
						</tr>
					</thead>
					<tbody class="divide-y divide-slate-50">
						<tr
							v-for="row in recentProblems"
							:key="row.id"
							class="transition hover:bg-slate-50/60"
						>
							<td class="px-4 py-2 font-medium text-slate-700">{{ row.title }}</td>
							<td class="px-3 py-2 text-center">
								<span :class="['rounded px-1.5 py-0.5 text-[10px] font-bold text-white', levelColor[row.level]]">
									{{ row.level }}
								</span>
							</td>
							<td class="px-3 py-2 text-center text-slate-500">{{ row.type }}</td>
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
							<th class="px-3 py-2 text-center font-medium">유형</th>
							<th class="px-3 py-2 text-center font-medium">등록일</th>
						</tr>
					</thead>
					<tbody class="divide-y divide-slate-50">
						<tr
							v-for="row in extraProblems"
							:key="row.id"
							class="transition hover:bg-slate-50/60"
						>
							<td class="px-4 py-2 font-medium text-slate-700">{{ row.title }}</td>
							<td class="px-3 py-2 text-center text-slate-500">{{ row.type }}</td>
							<td class="px-3 py-2 text-center text-slate-400">{{ row.createdAt }}</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>

		<!-- Popular cert cards -->
		<div>
			<div class="mb-2 flex items-center justify-between">
				<h3 class="text-xs font-semibold text-slate-500">인기 자격증 카드</h3>
				<RouterLink :to="{ name: 'problem' }" class="text-[10px] font-medium text-blue-500 no-underline transition hover:text-blue-600">
					전체 보기
				</RouterLink>
			</div>

			<div class="grid grid-cols-3 gap-3">
				<div
					v-for="cert in popularCerts"
					:key="cert.id"
					class="overflow-hidden rounded-lg border border-slate-200 bg-white shadow-sm transition hover:shadow-md"
				>
					<div :class="['flex h-20 items-center justify-center', cert.thumbBg]">
						<span class="text-2xl font-black text-white/80">{{ cert.icon }}</span>
					</div>
					<div class="p-2.5">
						<p class="text-[11px] font-semibold text-slate-700">{{ cert.name }}</p>
						<p class="mt-1 text-[10px] text-slate-400">{{ cert.count }}문제</p>
					</div>
				</div>
			</div>
		</div>
	</section>
</template>

<script setup>
const recentProblems = [
	{ id: 1, title: "데이터베이스 정규화", level: "초급", type: "객관식" },
	{ id: 2, title: "네트워크 프로토콜", level: "중급", type: "주관식" },
	{ id: 3, title: "AWS EC2 인스턴스 설계", level: "상급", type: "객관식" },
	{ id: 4, title: "SQL JOIN 쿼리 작성", level: "중급", type: "주관식" },
];

const extraProblems = [
	{ id: 1, title: "정보처리기사 운영체제", type: "객관식", createdAt: "2025.05.10" },
	{ id: 2, title: "SQLD 집계 함수", type: "주관식", createdAt: "2025.05.08" },
	{ id: 3, title: "AWS VPC 설계", type: "객관식", createdAt: "2025.05.06" },
];

const popularCerts = [
	{ id: 1, name: "정보처리기사", count: 1250, icon: "<>", thumbBg: "bg-gradient-to-br from-yellow-300 to-orange-400" },
	{ id: 2, name: "SQLD", count: 890, icon: "DB", thumbBg: "bg-gradient-to-br from-green-300 to-emerald-500" },
	{ id: 3, name: "AWS SA", count: 560, icon: "∑", thumbBg: "bg-gradient-to-br from-blue-300 to-blue-500" },
];

const levelColor = {
	초급: "bg-emerald-500",
	중급: "bg-orange-500",
	상급: "bg-red-500",
};
</script>

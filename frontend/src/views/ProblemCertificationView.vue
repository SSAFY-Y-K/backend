<template>
	<div>
		<div class="mb-4">
			<select class="h-7 rounded-md border border-slate-200 bg-white px-2 text-xs text-slate-600 outline-none focus:border-blue-400">
				<option value="">전체 자격증</option>
				<option>정보처리기사</option>
				<option>AWS SA</option>
				<option>SQLD</option>
				<option>정보보안기사</option>
			</select>
		</div>

		<div class="mb-3 grid grid-cols-3 gap-3">
			<div
				v-for="card in featuredCards"
				:key="card.id"
				class="overflow-hidden rounded-lg border border-slate-200 bg-white shadow-sm transition hover:shadow-md"
			>
				<div class="border-b border-slate-100 p-3">
					<h3 class="text-xs font-bold text-slate-800">{{ card.title }}</h3>
					<p class="mt-0.5 text-[10px] text-slate-400">{{ card.cert }}</p>
				</div>
				<div class="flex items-center justify-between p-2.5">
					<span class="flex items-center gap-1 text-[10px] text-slate-500">
						<span :class="['h-1.5 w-1.5 rounded-full', card.dotColor]"></span>
						{{ card.type }}
					</span>
					<RouterLink
						:to="{ name: 'problem-detail', params: { id: card.id } }"
						class="rounded border border-blue-200 px-2 py-0.5 text-[10px] text-blue-600 no-underline transition hover:bg-blue-50"
					>
						풀기
					</RouterLink>
				</div>
			</div>
		</div>

		<div class="grid grid-cols-3 gap-3">
			<div
				v-for="card in normalCards"
				:key="card.id"
				class="overflow-hidden rounded-lg border border-slate-200 bg-white shadow-sm transition hover:shadow-md"
			>
				<div class="border-b border-slate-100 p-3">
					<h3 class="text-xs font-bold text-slate-800">{{ card.title }}</h3>
					<p class="mt-0.5 text-[10px] text-slate-400">{{ card.cert }}</p>
					<p class="mt-1 text-[10px] leading-relaxed text-slate-500 line-clamp-2">{{ card.description }}</p>
				</div>
				<div class="flex items-center justify-between p-2.5">
					<span :class="['rounded px-1.5 py-0.5 text-[10px] font-bold text-white', levelColor[card.level]]">
						{{ card.level }}
					</span>
					<RouterLink
						:to="{ name: 'problem-detail', params: { id: card.id } }"
						class="rounded border border-blue-200 px-2 py-0.5 text-[10px] text-blue-600 no-underline transition hover:bg-blue-50"
					>
						풀기
					</RouterLink>
				</div>
			</div>
		</div>
	</div>
</template>

<script setup>
const levelColor = { 초급: "bg-emerald-500", 중급: "bg-orange-500", 상급: "bg-red-500" };

const featuredCards = [
	{ id: 1, title: "데이터베이스 정규화 개념", cert: "정보처리기사", type: "객관식", dotColor: "bg-blue-400" },
	{ id: 2, title: "AWS VPC 구성 설계", cert: "AWS SA", type: "객관식", dotColor: "bg-green-400" },
	{ id: 3, title: "SQL 집계 함수 활용", cert: "SQLD", type: "주관식", dotColor: "bg-orange-400" },
];

const normalCards = [
	{ id: 4, title: "운영체제 프로세스 관리", cert: "정보처리기사", level: "초급", description: "프로세스 스케줄링 및 동기화 관련 문제입니다." },
	{ id: 5, title: "EC2 인스턴스 유형 선택", cert: "AWS SA", level: "중급", description: "워크로드별 EC2 인스턴스 유형 선택 기준을 묻는 문제입니다." },
	{ id: 6, title: "네트워크 보안 프로토콜", cert: "정보보안기사", level: "상급", description: "TLS/SSL 핸드셰이크 과정과 보안 취약점 관련 문제입니다." },
];
</script>

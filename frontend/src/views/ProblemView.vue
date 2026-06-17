<template>
	<section class="min-h-full p-4">
		<!-- Search bar -->
		<div class="mb-3 flex items-center gap-2">
			<div class="relative flex-1">
				<svg
					class="pointer-events-none absolute left-3 top-1/2 h-3.5 w-3.5 -translate-y-1/2 text-slate-400"
					viewBox="0 0 14 14"
					fill="none"
					stroke="currentColor"
					stroke-width="1.6"
				>
					<circle cx="6" cy="6" r="4" />
					<path d="M9 9l3 3" stroke-linecap="round" />
				</svg>
				<input
					type="search"
					placeholder="검색..."
					class="h-8 w-full rounded-md border border-slate-200 bg-white pl-8 pr-3 text-xs text-slate-700 outline-none transition placeholder:text-slate-400 focus:border-blue-400 focus:ring-1 focus:ring-blue-100"
				/>
			</div>
			<button
				class="h-8 rounded-md bg-blue-600 px-4 text-xs font-semibold text-white transition hover:bg-blue-700"
			>
				검색
			</button>
		</div>

		<!-- Filter row -->
		<div class="mb-4 flex items-center justify-between">
			<div class="flex items-center gap-2">
				<!-- Cert dropdown -->
				<select
					class="h-7 rounded-md border border-slate-200 bg-white px-2 text-xs text-slate-600 outline-none focus:border-blue-400"
				>
					<option>인증</option>
					<option>정보처리기사</option>
					<option>AWS SA</option>
					<option>SQLD</option>
				</select>

				<!-- Level dropdown -->
				<select
					class="h-7 rounded-md border border-slate-200 bg-white px-2 text-xs text-slate-600 outline-none focus:border-blue-400"
				>
					<option>난이도 ▾</option>
					<option>초급</option>
					<option>중급</option>
					<option>상급</option>
				</select>

				<span class="text-xs text-slate-400">어</span>

				<!-- Toggle -->
				<div class="flex h-5 w-9 items-center rounded-full bg-slate-200 px-0.5 cursor-pointer">
					<div class="h-4 w-4 rounded-full bg-white shadow-sm"></div>
				</div>
			</div>

			<div class="flex items-center gap-2">
				<span class="text-xs text-slate-500">난이도</span>
				<!-- Toggle on -->
				<div class="flex h-5 w-9 items-center justify-end rounded-full bg-blue-600 px-0.5 cursor-pointer">
					<div class="h-4 w-4 rounded-full bg-white shadow-sm"></div>
				</div>
			</div>
		</div>

		<!-- Featured cards (top row - larger) -->
		<div class="mb-3 grid grid-cols-3 gap-3">
			<div
				v-for="card in featuredCards"
				:key="card.id"
				class="overflow-hidden rounded-lg border border-slate-200 bg-white shadow-sm transition hover:shadow-md"
			>
				<!-- Card top area -->
				<div class="border-b border-slate-100 p-3">
					<h3 class="text-xs font-bold text-slate-800">{{ card.title }}</h3>
					<p class="mt-0.5 text-[10px] text-slate-400">{{ card.provider }}</p>
				</div>
				<!-- Footer -->
				<div class="flex items-center justify-between p-2.5">
					<div class="flex items-center gap-2 text-[10px] text-slate-500">
						<span class="flex items-center gap-1">
							<span :class="['h-1.5 w-1.5 rounded-full', card.dotColor]"></span>체제
						</span>
						<span>관련 검색</span>
					</div>
					<div class="flex gap-1">
						<button
							class="rounded border border-slate-200 px-2 py-0.5 text-[10px] text-slate-500 transition hover:bg-slate-50"
						>
							관련 정보
						</button>
						<button
							class="rounded border border-slate-200 px-2 py-0.5 text-[10px] text-slate-500 transition hover:bg-slate-50"
						>
							관련 검색
						</button>
					</div>
				</div>
			</div>
		</div>

		<!-- Normal cards (bottom rows) -->
		<div class="grid grid-cols-3 gap-3">
			<div
				v-for="card in normalCards"
				:key="card.id"
				class="overflow-hidden rounded-lg border border-slate-200 bg-white shadow-sm transition hover:shadow-md"
			>
				<div class="border-b border-slate-100 p-3">
					<h3 class="text-xs font-bold text-slate-800">{{ card.title }}</h3>
					<p class="mt-0.5 text-[10px] text-slate-400">{{ card.provider }}</p>
					<p class="mt-1 text-[10px] text-slate-500 leading-relaxed line-clamp-2">
						{{ card.description }}
					</p>
				</div>
				<div class="flex items-center justify-between p-2.5">
					<div class="flex items-center gap-1 text-[10px] text-slate-400">
						<span
							:class="['rounded px-1.5 py-0.5 font-bold text-white', levelColor[card.level]]"
						>
							{{ card.level }}
						</span>
						<span>관련 정보</span>
					</div>
					<div class="flex gap-1">
						<button
							class="rounded border border-slate-200 px-2 py-0.5 text-[10px] text-slate-500 transition hover:bg-slate-50"
						>
							관련 정보
						</button>
						<button
							class="rounded border border-slate-200 px-2 py-0.5 text-[10px] text-slate-500 transition hover:bg-slate-50"
						>
							관련 검색
						</button>
					</div>
				</div>
			</div>
		</div>
	</section>
</template>

<script setup>
const featuredCards = [
	{ id: 1, title: "문제 카드 문제 카드 1", provider: "ProdExam R8", dotColor: "bg-blue-400" },
	{ id: 2, title: "문제 카드 문제 카드 2", provider: "ProdExam R8", dotColor: "bg-green-400" },
	{ id: 3, title: "문제 카드 문제 카드 3", provider: "ProdExam R8", dotColor: "bg-orange-400" },
];

const normalCards = [
	{ id: 4, title: "문제 카드 4", provider: "ProdExam R8", level: "초급", description: "데이터베이스 기본 개념과 정규화에 대한 문제입니다." },
	{ id: 5, title: "문제 카드 5", provider: "ProdExam R8", level: "중급", description: "AWS 클라우드 서비스 설계 관련 문제입니다." },
	{ id: 6, title: "문제 카드 6", provider: "ShapExam R8", level: "상급", description: "네트워크 보안 및 프로토콜 분석 문제입니다." },
];

const levelColor = {
	초급: "bg-emerald-500",
	중급: "bg-orange-500",
	상급: "bg-red-500",
};
</script>

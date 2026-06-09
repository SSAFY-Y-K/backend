<template>
	<section class="min-h-full bg-slate-50">
		<div class="mx-auto max-w-[1600px] px-6 py-8">
			<header class="mb-10 text-center">
				<h1 class="text-4xl font-bold tracking-tight text-slate-900 md:text-5xl">
					IT 자격증 준비의 새로운 방법
				</h1>
				<p class="mx-auto mt-4 max-w-2xl text-base text-slate-500">
					AI가 만든 문제로 효율적으로 학습하고, 커뮤니티에서 합격 경험을 나눠보세요.
				</p>

				<div class="mt-6 flex flex-col justify-center gap-3 sm:flex-row">
					<RouterLink
						:to="{ name: 'problem' }"
						class="inline-flex h-11 items-center justify-center rounded-md bg-blue-600 px-6 text-sm font-semibold text-white no-underline shadow-sm transition hover:bg-blue-700"
					>
						문제 풀러 가기
					</RouterLink>
					<RouterLink
						:to="{ name: 'create' }"
						class="inline-flex h-11 items-center justify-center rounded-md border border-blue-300 bg-white px-6 text-sm font-semibold text-blue-600 no-underline transition hover:bg-blue-50"
					>
						AI로 문제 만들기
					</RouterLink>
				</div>
			</header>

			<section class="mb-10">
				<div class="mb-4 flex items-center justify-between gap-3">
					<h2 class="text-xl font-bold text-slate-900">인기 자격증</h2>
					<RouterLink
						:to="{ name: 'problem' }"
						class="text-sm font-medium text-blue-600 no-underline transition hover:text-blue-700"
					>
						전체 보기
					</RouterLink>
				</div>

				<div class="grid gap-4 md:grid-cols-2 xl:grid-cols-4">
					<article
						v-for="cert in popularCertifications"
						:key="cert.name"
						class="rounded-lg border border-slate-200 bg-white p-5 shadow-sm"
					>
						<div class="flex items-center gap-4">
							<div
								:class="[
									'flex h-12 w-12 shrink-0 items-center justify-center rounded-lg text-lg font-bold text-white',
									cert.color,
								]"
							>
								{{ cert.icon }}
							</div>
							<div class="min-w-0">
								<h3 class="truncate text-lg font-semibold text-slate-900">
									{{ cert.name }}
								</h3>
								<p class="mt-1 text-sm text-slate-500">{{ cert.count }}개의 문제</p>
							</div>
						</div>
					</article>
				</div>
			</section>

			<section>
				<div class="mb-4 flex items-center justify-between gap-3">
					<h2 class="text-xl font-bold text-slate-900">최근 등록된 문제</h2>
					<RouterLink
						:to="{ name: 'problem' }"
						class="text-sm font-medium text-blue-600 no-underline transition hover:text-blue-700"
					>
						문제 더 보기
					</RouterLink>
				</div>

				<div class="space-y-3">
					<article
						v-for="problem in recentProblems"
						:key="problem.id"
						class="rounded-lg border border-slate-200 bg-white p-4 shadow-sm"
					>
						<div class="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
							<div class="min-w-0">
								<h3 class="text-lg font-semibold text-slate-900">
									{{ problem.title }}
								</h3>

								<div class="mt-3 flex flex-wrap items-center gap-2 text-sm text-slate-500">
									<span
										class="inline-flex items-center rounded-full border border-blue-200 px-2.5 py-0.5 text-xs font-medium text-blue-600"
									>
										{{ problem.certification }}
									</span>
									<span
										:class="[
											'inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-semibold text-white',
											levelColorMap[problem.level],
										]"
									>
										{{ problem.level }}
									</span>
									<span>{{ problem.solvedCount }}명 풀이</span>
								</div>
							</div>

							<RouterLink
								:to="{ name: 'problem' }"
								class="inline-flex h-9 shrink-0 items-center justify-center rounded-md border border-blue-300 px-4 text-sm font-semibold text-blue-600 no-underline transition hover:bg-blue-50"
							>
								풀기
							</RouterLink>
						</div>
					</article>
				</div>
			</section>
		</div>
	</section>
</template>

<script setup>
const popularCertifications = [
	{
		name: "정보처리기사",
		count: 1250,
		icon: "<>",
		color: "bg-blue-500",
	},
	{
		name: "AWS Solutions Architect",
		count: 890,
		icon: "AWS",
		color: "bg-orange-500",
	},
	{
		name: "SQLD",
		count: 756,
		icon: "DB",
		color: "bg-emerald-600",
	},
	{
		name: "정보보안기사",
		count: 634,
		icon: "S",
		color: "bg-red-500",
	},
];

const recentProblems = [
	{
		id: 1,
		title: "SQL 쿼리 최적화 문제",
		certification: "SQLD",
		level: "중급",
		solvedCount: 45,
	},
	{
		id: 2,
		title: "네트워크 프로토콜 이해",
		certification: "정보처리기사",
		level: "초급",
		solvedCount: 123,
	},
	{
		id: 3,
		title: "AWS VPC 설계",
		certification: "AWS SA",
		level: "상급",
		solvedCount: 28,
	},
];

const levelColorMap = {
	초급: "bg-emerald-600",
	중급: "bg-orange-500",
	상급: "bg-red-500",
};
</script>

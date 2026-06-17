<template>
	<section class="min-h-full p-4">
		<!-- Header -->
		<div class="mb-4 flex items-center justify-between">
			<h3 class="text-sm font-bold text-slate-700">문제</h3>
			<button
				class="flex items-center gap-1 rounded-md border border-slate-200 bg-white px-3 py-1.5 text-xs font-medium text-slate-600 shadow-sm transition hover:bg-slate-50"
			>
				+ 작성 완료
			</button>
		</div>

		<!-- Mode selection -->
		<div v-if="!selectedMode" class="grid grid-cols-2 gap-4">
			<button
				class="group flex flex-col items-center justify-center gap-4 rounded-xl border-2 border-slate-200 bg-white p-8 shadow-sm transition hover:border-blue-300 hover:shadow-md"
				@click="selectedMode = 'ai'"
			>
				<!-- AI icon -->
				<div
					class="flex h-20 w-20 items-center justify-center rounded-full bg-blue-50 border-4 border-blue-100 transition group-hover:border-blue-200"
				>
					<svg
						class="h-10 w-10 text-blue-500"
						viewBox="0 0 40 40"
						fill="none"
						stroke="currentColor"
						stroke-width="1.8"
					>
						<circle cx="20" cy="20" r="12" />
						<path d="M14 20h12M20 14v12" stroke-linecap="round" />
						<circle cx="20" cy="20" r="3" fill="currentColor" />
					</svg>
				</div>
				<div class="text-center">
					<h3 class="text-base font-bold text-slate-800">AI 모드</h3>
					<p class="mt-1 text-xs text-slate-400">최제 최오 최무한 한자</p>
				</div>
			</button>

			<button
				class="group flex flex-col items-center justify-center gap-4 rounded-xl border-2 border-slate-200 bg-white p-8 shadow-sm transition hover:border-blue-300 hover:shadow-md"
				@click="selectedMode = 'manual'"
			>
				<!-- Document icon -->
				<div
					class="flex h-20 w-20 items-center justify-center rounded-full bg-slate-50 border-4 border-slate-100 transition group-hover:border-slate-200"
				>
					<svg
						class="h-10 w-10 text-slate-500"
						viewBox="0 0 40 40"
						fill="none"
						stroke="currentColor"
						stroke-width="1.8"
					>
						<rect x="8" y="6" width="24" height="28" rx="3" />
						<path d="M14 15h12M14 20h12M14 25h8" stroke-linecap="round" />
					</svg>
				</div>
				<div class="text-center">
					<h3 class="text-base font-bold text-slate-800">직접 모드</h3>
					<p class="mt-1 text-xs text-slate-400">직접 모도마서 한 팬오</p>
				</div>
			</button>
		</div>

		<!-- Form (after mode selected) -->
		<div v-else class="max-w-2xl">
			<!-- Mode tab + close -->
			<div class="mb-4 flex items-center gap-0 overflow-hidden rounded-lg border border-slate-200 bg-white">
				<button
					:class="[
						'px-4 py-2 text-xs font-semibold transition',
						selectedMode === 'ai' ? 'bg-blue-600 text-white' : 'text-slate-500 hover:bg-slate-50',
					]"
					@click="selectedMode = 'ai'"
				>
					AI 오르되
				</button>
				<button
					:class="[
						'px-4 py-2 text-xs font-semibold transition',
						selectedMode === 'manual' ? 'bg-blue-600 text-white' : 'text-slate-500 hover:bg-slate-50',
					]"
					@click="selectedMode = 'manual'"
				>
					지금 모드
				</button>
				<div class="flex-1"></div>
				<button
					class="px-3 py-2 text-xs text-slate-400 transition hover:text-slate-600"
					@click="selectedMode = null"
				>
					✕
				</button>
			</div>

			<!-- Form card -->
			<div class="rounded-lg border border-slate-200 bg-white p-5 shadow-sm">
				<h3 class="mb-4 text-sm font-bold text-slate-700">문제 작성 폼</h3>

				<div class="space-y-4">
					<!-- 문제 작성 -->
					<div>
						<label class="mb-1 block text-xs font-medium text-slate-500">문제 작성</label>
						<input
							type="text"
							placeholder="글제 제목 제목이라시시시시시."
							class="h-9 w-full rounded-md border border-slate-200 bg-slate-50 px-3 text-xs text-slate-700 outline-none transition placeholder:text-slate-300 focus:border-blue-400 focus:bg-white focus:ring-1 focus:ring-blue-100"
						/>
					</div>

					<!-- 빌리본 참고 -->
					<div>
						<label class="mb-1 block text-xs font-medium text-slate-500">빌리본 참고</label>
						<input
							type="text"
							placeholder="비빌 사단달달달 팡달팡달 달아봅시다."
							class="h-9 w-full rounded-md border border-slate-200 bg-slate-50 px-3 text-xs text-slate-700 outline-none transition placeholder:text-slate-300 focus:border-blue-400 focus:bg-white focus:ring-1 focus:ring-blue-100"
						/>
					</div>

					<!-- Rich text toolbar -->
					<div>
						<div class="mb-1 flex items-center gap-1 rounded-t-md border border-slate-200 bg-slate-50 px-2 py-1.5">
							<button class="rounded px-1.5 py-0.5 text-xs font-bold text-slate-600 hover:bg-slate-200">B</button>
							<button class="rounded px-1.5 py-0.5 text-xs italic text-slate-600 hover:bg-slate-200">I</button>
							<button class="rounded px-1.5 py-0.5 text-xs font-bold underline text-slate-600 hover:bg-slate-200">U</button>
							<div class="mx-1 h-3 w-px bg-slate-300"></div>
							<button class="rounded px-1.5 py-0.5 text-xs text-slate-600 hover:bg-slate-200">♀</button>
							<button class="rounded px-1.5 py-0.5 text-xs text-slate-600 hover:bg-slate-200">☞</button>
							<div class="mx-1 h-3 w-px bg-slate-300"></div>
							<button class="rounded px-1.5 py-0.5 text-xs text-slate-600 hover:bg-slate-200">≡</button>
							<button class="rounded px-1.5 py-0.5 text-xs text-slate-600 hover:bg-slate-200">≡</button>
							<button class="rounded px-1.5 py-0.5 text-xs text-slate-600 hover:bg-slate-200">≡</button>
						</div>
						<textarea
							rows="5"
							placeholder="작성 참하는 문제 팔랑팔랑이라이라 방법을 달아봅시다."
							class="w-full resize-none rounded-b-md border border-t-0 border-slate-200 bg-white px-3 py-2 text-xs text-slate-700 outline-none transition placeholder:text-slate-300 focus:border-blue-400 focus:ring-1 focus:ring-blue-100"
						></textarea>
					</div>

					<!-- 작성 참고 -->
					<div>
						<label class="mb-1 block text-xs font-medium text-slate-500">작성 참고</label>
						<select
							class="h-9 w-full rounded-md border border-slate-200 bg-slate-50 px-3 text-xs text-slate-500 outline-none transition focus:border-blue-400 focus:bg-white"
						>
							<option>자격증 선택...</option>
							<option>정보처리기사</option>
							<option>AWS SA</option>
							<option>SQLD</option>
						</select>
					</div>
				</div>
			</div>
		</div>
	</section>
</template>

<script setup>
import { ref } from "vue";

const selectedMode = ref(null);
</script>

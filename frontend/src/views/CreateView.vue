<template>
	<section class="min-h-full p-4">
		<!-- Breadcrumb / back -->
		<div class="mb-4 flex items-center gap-2">
			<button
				v-if="category"
				class="text-xs text-slate-400 transition hover:text-slate-600"
				@click="goBack"
			>
				← 뒤로
			</button>
			<h3 class="text-sm font-bold text-slate-700">
				{{ pageTitle }}
			</h3>
		</div>

		<!-- Step 1: 자격증 문제 vs 코딩 문제 -->
		<div v-if="!category" class="grid grid-cols-2 gap-4">
			<button
				class="group flex flex-col items-center justify-center gap-4 rounded-xl border-2 border-slate-200 bg-white p-8 shadow-sm transition hover:border-blue-300 hover:shadow-md"
				@click="category = 'cert'"
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
				@click="category = 'coding'"
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

		<!-- Step 2a: 자격증 문제 → AI / 직접 선택 -->
		<div v-else-if="category === 'cert' && !mode" class="grid grid-cols-2 gap-4">
			<button
				class="group flex flex-col items-center justify-center gap-4 rounded-xl border-2 border-slate-200 bg-white p-8 shadow-sm transition hover:border-blue-300 hover:shadow-md"
				@click="mode = 'ai'"
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
				@click="mode = 'manual'"
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

		<!-- Step 2b: 자격증 문제 폼 (AI or 직접) -->
		<div v-else-if="category === 'cert' && mode" class="max-w-2xl">
			<!-- Mode toggle -->
			<div class="mb-4 flex items-center overflow-hidden rounded-lg border border-slate-200 bg-white">
				<button
					:class="['px-4 py-2 text-xs font-semibold transition', mode === 'ai' ? 'bg-blue-600 text-white' : 'text-slate-500 hover:bg-slate-50']"
					@click="mode = 'ai'"
				>
					AI 모드
				</button>
				<button
					:class="['px-4 py-2 text-xs font-semibold transition', mode === 'manual' ? 'bg-blue-600 text-white' : 'text-slate-500 hover:bg-slate-50']"
					@click="mode = 'manual'"
				>
					직접 모드
				</button>
				<div class="flex-1"></div>
				<button class="px-3 py-2 text-xs text-slate-400 transition hover:text-slate-600" @click="mode = null">✕</button>
			</div>

			<div class="rounded-lg border border-slate-200 bg-white p-5 shadow-sm">
				<!-- 자격증 선택 -->
				<div class="mb-4">
					<label class="mb-1 block text-xs font-medium text-slate-500">자격증</label>
					<select class="h-9 w-full rounded-md border border-slate-200 bg-slate-50 px-3 text-xs text-slate-700 outline-none transition focus:border-blue-400 focus:bg-white">
						<option value="">자격증 선택</option>
						<option>정보처리기사</option>
						<option>AWS SA</option>
						<option>SQLD</option>
						<option>정보보안기사</option>
					</select>
				</div>

				<!-- 문제 유형 (객관식 / 주관식) -->
				<div class="mb-4">
					<label class="mb-2 block text-xs font-medium text-slate-500">문제 유형</label>
					<div class="flex gap-3">
						<label class="flex cursor-pointer items-center gap-2 text-xs text-slate-700">
							<input v-model="problemType" type="radio" value="multiple" class="accent-blue-600" />
							객관식
						</label>
						<label class="flex cursor-pointer items-center gap-2 text-xs text-slate-700">
							<input v-model="problemType" type="radio" value="short" class="accent-blue-600" />
							주관식
						</label>
					</div>
				</div>

				<!-- AI 모드: 참고 자료 입력 -->
				<div v-if="mode === 'ai'" class="mb-4">
					<label class="mb-1 block text-xs font-medium text-slate-500">참고 자료 (선택)</label>
					<textarea
						rows="4"
						placeholder="문제 출제 범위나 기준 자료를 입력하세요."
						class="w-full resize-none rounded-md border border-slate-200 bg-slate-50 px-3 py-2 text-xs text-slate-700 outline-none transition placeholder:text-slate-300 focus:border-blue-400 focus:bg-white focus:ring-1 focus:ring-blue-100"
					></textarea>
				</div>

				<!-- 직접 모드: 문제 내용 작성 -->
				<div v-if="mode === 'manual'" class="space-y-4">
					<div>
						<label class="mb-1 block text-xs font-medium text-slate-500">문제 제목</label>
						<input
							type="text"
							placeholder="문제 제목을 입력하세요"
							class="h-9 w-full rounded-md border border-slate-200 bg-slate-50 px-3 text-xs text-slate-700 outline-none transition placeholder:text-slate-300 focus:border-blue-400 focus:bg-white focus:ring-1 focus:ring-blue-100"
						/>
					</div>

					<div>
						<label class="mb-1 block text-xs font-medium text-slate-500">문제 내용</label>
						<div class="flex items-center gap-1 rounded-t-md border border-slate-200 bg-slate-50 px-2 py-1.5">
							<button class="rounded px-1.5 py-0.5 text-xs font-bold text-slate-600 hover:bg-slate-200">B</button>
							<button class="rounded px-1.5 py-0.5 text-xs italic text-slate-600 hover:bg-slate-200">I</button>
							<button class="rounded px-1.5 py-0.5 text-xs font-bold underline text-slate-600 hover:bg-slate-200">U</button>
						</div>
						<textarea
							rows="5"
							placeholder="문제 내용을 입력하세요"
							class="w-full resize-none rounded-b-md border border-t-0 border-slate-200 bg-white px-3 py-2 text-xs text-slate-700 outline-none transition placeholder:text-slate-300 focus:border-blue-400 focus:ring-1 focus:ring-blue-100"
						></textarea>
					</div>

					<div v-if="problemType === 'multiple'" class="space-y-2">
						<label class="mb-1 block text-xs font-medium text-slate-500">보기</label>
						<input
							v-for="n in 4"
							:key="n"
							type="text"
							:placeholder="`${n}번 보기`"
							class="h-9 w-full rounded-md border border-slate-200 bg-slate-50 px-3 text-xs text-slate-700 outline-none transition placeholder:text-slate-300 focus:border-blue-400 focus:bg-white focus:ring-1 focus:ring-blue-100"
						/>
					</div>

					<div>
						<label class="mb-1 block text-xs font-medium text-slate-500">정답 및 해설</label>
						<textarea
							rows="3"
							placeholder="정답 및 해설을 입력하세요"
							class="w-full resize-none rounded-md border border-slate-200 bg-slate-50 px-3 py-2 text-xs text-slate-700 outline-none transition placeholder:text-slate-300 focus:border-blue-400 focus:bg-white focus:ring-1 focus:ring-blue-100"
						></textarea>
					</div>
				</div>

				<div class="mt-5 flex justify-end border-t border-slate-100 pt-4">
					<button class="h-9 rounded-md bg-blue-600 px-5 text-xs font-semibold text-white transition hover:bg-blue-700">
						{{ mode === 'ai' ? 'AI 문제 생성' : '문제 등록' }}
					</button>
				</div>
			</div>
		</div>

		<!-- Step 2c: 코딩 문제 폼 (AI 전용) -->
		<div v-else-if="category === 'coding'" class="max-w-2xl">
			<div class="mb-3 flex items-center gap-2 rounded-lg border border-purple-100 bg-purple-50 px-4 py-2.5">
				<svg class="h-4 w-4 shrink-0 text-purple-400" viewBox="0 0 16 16" fill="none" stroke="currentColor" stroke-width="1.6">
					<circle cx="8" cy="8" r="6" />
					<path d="M8 5v3.5l2 1.5" stroke-linecap="round" />
				</svg>
				<p class="text-xs text-purple-700">코딩 문제는 AI가 자동으로 생성합니다.</p>
			</div>

			<div class="rounded-lg border border-slate-200 bg-white p-5 shadow-sm">
				<div class="space-y-4">
					<div>
						<label class="mb-1 block text-xs font-medium text-slate-500">자격증 / 주제</label>
						<select class="h-9 w-full rounded-md border border-slate-200 bg-slate-50 px-3 text-xs text-slate-700 outline-none transition focus:border-blue-400 focus:bg-white">
							<option value="">자격증 선택</option>
							<option>정보처리기사</option>
							<option>AWS SA</option>
							<option>SQLD</option>
							<option>정보보안기사</option>
						</select>
					</div>

					<div>
						<label class="mb-1 block text-xs font-medium text-slate-500">난이도</label>
						<div class="flex gap-3">
							<label class="flex cursor-pointer items-center gap-2 text-xs text-slate-700">
								<input v-model="codingLevel" type="radio" value="easy" class="accent-purple-600" />
								초급
							</label>
							<label class="flex cursor-pointer items-center gap-2 text-xs text-slate-700">
								<input v-model="codingLevel" type="radio" value="medium" class="accent-purple-600" />
								중급
							</label>
							<label class="flex cursor-pointer items-center gap-2 text-xs text-slate-700">
								<input v-model="codingLevel" type="radio" value="hard" class="accent-purple-600" />
								상급
							</label>
						</div>
					</div>

					<div>
						<label class="mb-1 block text-xs font-medium text-slate-500">추가 요구사항 (선택)</label>
						<textarea
							rows="3"
							placeholder="예: 정렬 알고리즘 관련 문제로 만들어주세요"
							class="w-full resize-none rounded-md border border-slate-200 bg-slate-50 px-3 py-2 text-xs text-slate-700 outline-none transition placeholder:text-slate-300 focus:border-blue-400 focus:bg-white focus:ring-1 focus:ring-blue-100"
						></textarea>
					</div>
				</div>

				<div class="mt-5 flex justify-end border-t border-slate-100 pt-4">
					<button class="h-9 rounded-md bg-purple-600 px-5 text-xs font-semibold text-white transition hover:bg-purple-700">
						AI 코딩 문제 생성
					</button>
				</div>
			</div>
		</div>
	</section>
</template>

<script setup>
import { computed, ref } from "vue";

const category = ref(null);
const mode = ref(null);
const problemType = ref("multiple");
const codingLevel = ref("medium");

const pageTitle = computed(() => {
	if (!category.value) return "문제 만들기";
	if (category.value === "cert" && !mode.value) return "자격증 문제";
	if (category.value === "cert" && mode.value) return "자격증 문제 작성";
	if (category.value === "coding") return "코딩 문제";
	return "문제 만들기";
});

const goBack = () => {
	if (category.value === "cert" && mode.value) {
		mode.value = null;
	} else {
		category.value = null;
		mode.value = null;
	}
};
</script>

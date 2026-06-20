<template>
	<div class="max-w-2xl">
		<div class="mb-4">
			<div class="grid w-full grid-cols-2 overflow-hidden rounded-lg border border-slate-200 bg-white">
				<button
					:class="['px-4 py-2 text-xs font-semibold transition', mode === 'ai' ? 'bg-blue-600 text-white' : 'text-slate-500 hover:bg-slate-50']"
					@click="setMode('ai')"
				>
					AI 모드
				</button>
				<button
					:class="['px-4 py-2 text-xs font-semibold transition', mode === 'manual' ? 'bg-blue-600 text-white' : 'text-slate-500 hover:bg-slate-50']"
					@click="setMode('manual')"
				>
					직접 모드
				</button>
			</div>
			<button class="mt-2 text-xs text-slate-400 transition hover:text-slate-600" @click="goModeSelect">← 모드 선택</button>
		</div>

		<div class="rounded-lg border border-slate-200 bg-white p-5 shadow-sm">
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

			<div v-if="mode === 'ai'" class="mb-4">
				<label class="mb-1 block text-xs font-medium text-slate-500">참고 자료 (선택)</label>
				<textarea
					rows="4"
					placeholder="문제 출제 범위나 기준 자료를 입력하세요."
					class="w-full resize-none rounded-md border border-slate-200 bg-slate-50 px-3 py-2 text-xs text-slate-700 outline-none transition placeholder:text-slate-300 focus:border-blue-400 focus:bg-white focus:ring-1 focus:ring-blue-100"
				></textarea>
			</div>

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
						class="w-full resize-none rounded-md border border-slate-200 bg-slate-50 px-3 py-2 text-xs text-slate-700 outline-none transition placeholder:text-slate-300 focus:border-blue-400 focus:ring-1 focus:ring-blue-100"
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
</template>

<script setup>
import { computed, ref } from "vue";
import { useRoute, useRouter } from "vue-router";

const route = useRoute();
const router = useRouter();

const problemType = ref("multiple");

const mode = computed(() => {
	return route.query.mode === "manual" ? "manual" : "ai";
});

const setMode = (nextMode) => {
	router.replace({
		name: "create-certification-write",
		query: { ...route.query, mode: nextMode },
	});
};

const goModeSelect = () => {
	router.push({ name: "create-certification" });
};
</script>

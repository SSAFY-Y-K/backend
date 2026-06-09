<template>
	<section class="min-h-full bg-slate-50">
		<div class="mx-auto max-w-4xl px-6 py-6">
			<header class="mb-6">
				<h1 class="text-3xl font-bold tracking-tight text-slate-900">문제 만들기</h1>
				<p class="mt-2 text-sm text-slate-500">
					AI로 문제 초안을 만들거나 직접 문제를 작성해 등록하세요.
				</p>
			</header>

			<div class="mb-6 grid gap-4 md:grid-cols-2">
				<button
					v-for="mode in createModes"
					:key="mode.id"
					type="button"
					:class="[
						'rounded-lg border bg-white p-6 text-center shadow-sm transition hover:border-blue-300 hover:bg-blue-50',
						selectedMode === mode.id ? 'border-blue-500 ring-2 ring-blue-100' : 'border-slate-200',
					]"
					@click="selectedMode = mode.id"
				>
					<div
						:class="[
							'mx-auto flex h-12 w-12 items-center justify-center rounded-lg text-lg font-bold text-white',
							mode.color,
						]"
					>
						{{ mode.icon }}
					</div>
					<h2 class="mt-4 text-lg font-bold text-slate-900">{{ mode.title }}</h2>
					<p class="mt-2 text-sm text-slate-500">{{ mode.description }}</p>
				</button>
			</div>

			<form
				class="rounded-lg border border-slate-200 bg-white p-6 shadow-sm"
				@submit.prevent="handleSubmit"
			>
				<div class="mb-6">
					<h2 class="text-xl font-bold text-slate-900">문제 정보</h2>
				</div>

				<div class="space-y-5">
					<select
						v-model="certification"
						class="h-11 w-full rounded-md border border-slate-300 bg-white px-3 text-sm text-slate-700 outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-100"
					>
						<option value="">자격증 선택</option>
						<option v-for="cert in certifications" :key="cert" :value="cert">
							{{ cert }}
						</option>
					</select>

					<select
						v-model="level"
						class="h-11 w-full rounded-md border border-slate-300 bg-white px-3 text-sm text-slate-700 outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-100"
					>
						<option value="">난이도</option>
						<option value="초급">초급</option>
						<option value="중급">중급</option>
						<option value="상급">상급</option>
					</select>

					<div>
						<p class="mb-3 text-sm font-medium text-slate-700">문제 유형</p>
						<div class="flex flex-wrap gap-4">
							<label
								v-for="type in problemTypes"
								:key="type"
								class="flex items-center gap-2 text-sm text-slate-700"
							>
								<input
									v-model="problemType"
									type="radio"
									:value="type"
									class="h-4 w-4 border-slate-300 text-blue-600 focus:ring-blue-500"
								/>
								{{ type }}
							</label>
						</div>
					</div>

					<div class="border-t border-slate-200 pt-5">
						<input
							v-model="title"
							type="text"
							placeholder="문제 제목"
							class="h-11 w-full rounded-md border border-slate-300 px-3 text-sm text-slate-900 outline-none transition placeholder:text-slate-400 focus:border-blue-500 focus:ring-2 focus:ring-blue-100"
						/>
					</div>

					<textarea
						v-model="content"
						rows="6"
						placeholder="문제 내용"
						class="w-full resize-none rounded-md border border-slate-300 px-3 py-3 text-sm text-slate-900 outline-none transition placeholder:text-slate-400 focus:border-blue-500 focus:ring-2 focus:ring-blue-100"
					></textarea>

					<div v-if="problemType === '객관식'" class="space-y-3">
						<p class="text-sm font-medium text-slate-700">보기</p>
						<input
							v-for="(choice, index) in choices"
							:key="index"
							v-model="choices[index]"
							type="text"
							:placeholder="`${index + 1}번 보기`"
							class="h-10 w-full rounded-md border border-slate-300 px-3 text-sm text-slate-900 outline-none transition placeholder:text-slate-400 focus:border-blue-500 focus:ring-2 focus:ring-blue-100"
						/>
					</div>

					<textarea
						v-model="answer"
						rows="4"
						placeholder="정답 및 해설"
						class="w-full resize-none rounded-md border border-slate-300 px-3 py-3 text-sm text-slate-900 outline-none transition placeholder:text-slate-400 focus:border-blue-500 focus:ring-2 focus:ring-blue-100"
					></textarea>

					<div
						v-if="selectedMode === 'ai'"
						class="rounded-md border border-blue-100 bg-blue-50 px-4 py-3"
					>
						<label for="sourceText" class="text-sm font-medium text-blue-900">
							AI 참고 내용
						</label>
						<textarea
							id="sourceText"
							v-model="sourceText"
							rows="4"
							placeholder="문제를 만들 기준 자료나 출제 범위를 입력하세요."
							class="mt-2 w-full resize-none rounded-md border border-blue-200 bg-white px-3 py-3 text-sm text-slate-900 outline-none transition placeholder:text-slate-400 focus:border-blue-500 focus:ring-2 focus:ring-blue-100"
						></textarea>
					</div>

					<div class="flex flex-col-reverse gap-3 border-t border-slate-200 pt-5 sm:flex-row sm:justify-end">
						<button
							type="button"
							class="inline-flex h-10 items-center justify-center rounded-md border border-slate-300 bg-white px-5 text-sm font-semibold text-slate-700 transition hover:bg-slate-50"
							@click="resetForm"
						>
							초기화
						</button>
						<button
							type="submit"
							class="inline-flex h-10 items-center justify-center rounded-md bg-blue-600 px-5 text-sm font-semibold text-white shadow-sm transition hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-200"
						>
							{{ selectedMode === "ai" ? "AI 문제 생성" : "문제 등록" }}
						</button>
					</div>
				</div>
			</form>
		</div>
	</section>
</template>

<script setup>
import { ref } from "vue";

const createModes = [
	{
		id: "ai",
		title: "AI로 문제 생성",
		description: "기출 문제나 학습 자료를 바탕으로 문제 초안을 생성합니다.",
		icon: "AI",
		color: "bg-purple-600",
	},
	{
		id: "manual",
		title: "직접 문제 작성",
		description: "문제를 직접 작성하고 정답과 해설을 입력합니다.",
		icon: "DOC",
		color: "bg-blue-500",
	},
];

const certifications = ["정보처리기사", "AWS SA", "SQLD", "정보보안기사"];
const problemTypes = ["객관식", "주관식", "코딩"];

const selectedMode = ref("ai");
const certification = ref("");
const level = ref("");
const problemType = ref("객관식");
const title = ref("");
const content = ref("");
const choices = ref(["", "", "", ""]);
const answer = ref("");
const sourceText = ref("");

const resetForm = () => {
	certification.value = "";
	level.value = "";
	problemType.value = "객관식";
	title.value = "";
	content.value = "";
	choices.value = ["", "", "", ""];
	answer.value = "";
	sourceText.value = "";
};

const handleSubmit = () => {
	console.log("create problem submit", {
		mode: selectedMode.value,
		certification: certification.value,
		level: level.value,
		problemType: problemType.value,
		title: title.value,
		content: content.value,
		choices: choices.value,
		answer: answer.value,
		sourceText: sourceText.value,
	});
};
</script>

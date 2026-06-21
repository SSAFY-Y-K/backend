<template>
	<div class="max-w-2xl">
		<div class="mb-4">
			<div
				class="grid w-full grid-cols-2 overflow-hidden rounded-lg border border-slate-200 bg-white"
			>
				<button
					:class="[
						'px-4 py-2 text-xs font-semibold transition',
						mode === AI
							? 'bg-blue-600 text-white'
							: 'text-slate-500 hover:bg-slate-50',
					]"
					@click="setMode(AI)"
				>
					AI 모드
				</button>
				<button
					:class="[
						'px-4 py-2 text-xs font-semibold transition',
						mode === MANUAL
							? 'bg-blue-600 text-white'
							: 'text-slate-500 hover:bg-slate-50',
					]"
					@click="setMode(MANUAL)"
				>
					직접 모드
				</button>
			</div>
			<button
				class="mt-2 text-xs text-slate-400 transition hover:text-slate-600"
				@click="goModeSelect"
			>
				← 모드 선택
			</button>
		</div>

		<!-- 자격증 선택 드롭박스 -->
		<div class="rounded-lg border border-slate-200 bg-white p-5 shadow-sm">
			<div class="mb-4">
				<label class="mb-1 block text-xs font-medium text-slate-500"
					>자격증</label
				>
				<select
					v-model="commonForm.certId"
					class="h-9 w-full rounded-md border border-slate-200 bg-slate-50 px-3 text-xs text-slate-700 outline-none transition focus:border-blue-400 focus:bg-white"
				>
					<option value="" selected disabled>자격증 선택</option>
					<option
						v-for="certification in certificationStore.getCertifications()"
						:key="certification.certId"
						:value="certification.certId"
					>
						{{ certification.name }}
					</option>
				</select>
			</div>

			<!-- 문제 유형 선택 라디오 버튼 -->
			<div class="mb-4">
				<label class="mb-2 block text-xs font-medium text-slate-500"
					>문제 유형</label
				>
				<div class="flex gap-3">
					<label
						class="flex cursor-pointer items-center gap-2 text-xs text-slate-700"
					>
						<input
							v-model="commonForm.problemType"
							type="radio"
							:value="MULTIPLE_CHOICE"
							class="accent-blue-600"
							@change="resetForm"
						/>
						객관식
					</label>
					<label
						class="flex cursor-pointer items-center gap-2 text-xs text-slate-700"
					>
						<input
							v-model="commonForm.problemType"
							type="radio"
							:value="SHORT_ANSWER"
							class="accent-blue-600"
							@change="resetForm"
						/>
						주관식
					</label>
				</div>
			</div>

			<CertificationAiForm v-if="mode === AI" v-model="aiForm.referenceText" />

			<CertificationManualForm
				v-if="mode === MANUAL"
				v-model:title="manualForm.title"
				v-model:question="manualForm.question"
				v-model:choice1-content="manualForm.choice1Content"
				v-model:choice2-content="manualForm.choice2Content"
				v-model:choice3-content="manualForm.choice3Content"
				v-model:choice4-content="manualForm.choice4Content"
				v-model:answer-number="manualForm.answerNumber"
				v-model:answer="manualForm.answer"
				:is-multiple-choice="isMultipleChoice"
			/>

			<div class="mt-5 flex justify-end border-t border-slate-100 pt-4">
				<button
					class="h-9 rounded-md bg-blue-600 px-5 text-xs font-semibold text-white transition hover:bg-blue-700 disabled:bg-gray-400"
					:disabled="isLoading"
					@click="onSubmit"
				>
					{{
						mode === AI
							? isLoading
								? "진행 중..."
								: "AI 문제 생성"
							: isLoading
								? "진행 중..."
								: "문제 등록"
					}}
				</button>
			</div>
		</div>
	</div>

	<div
		v-if="showSuccessModal"
		class="fixed inset-0 z-50 flex items-center justify-center bg-black/30"
	>
		<div class="w-full max-w-xs rounded-lg bg-white p-5 text-center shadow-lg">
			<p class="text-sm font-semibold text-slate-800">문제가 생성되었습니다.</p>

			<button
				type="button"
				class="mt-4 h-9 rounded-md bg-blue-600 px-4 text-xs font-semibold text-white"
				@click="closeSuccessModal"
			>
				확인
			</button>
		</div>
	</div>

	<div
		v-if="showFailureModal"
		class="fixed inset-0 z-50 flex items-center justify-center bg-black/30"
	>
		<div class="w-full max-w-xs rounded-lg bg-white p-5 text-center shadow-lg">
			<p class="text-sm font-semibold text-slate-800">
				문제 생성에 실패했습니다.
			</p>

			<button
				type="button"
				class="mt-4 h-9 rounded-md bg-blue-600 px-4 text-xs font-semibold text-white"
				@click="closeFailureModal"
			>
				확인
			</button>
		</div>
	</div>
</template>

<script setup>
import { authApi } from "@/api/client";
import CertificationAiForm from "@/components/certification/CertificationAiForm.vue";
import CertificationManualForm from "@/components/certification/CertificationManualForm.vue";
import { useCertificationStore } from "@/stores/certification";
import { computed, ref } from "vue";
import { useRoute, useRouter } from "vue-router";

const AI = "ai";
const MANUAL = "manual";

const MULTIPLE_CHOICE = "MULTIPLE_CHOICE";
const SHORT_ANSWER = "SHORT_ANSWER";

const route = useRoute();
const router = useRouter();
const certificationStore = useCertificationStore();

const isLoading = ref(false);
const showSuccessModal = ref(false);
const showFailureModal = ref(false);

const commonForm = ref({
	certId: "",
	problemType: MULTIPLE_CHOICE,
});

const aiForm = ref({
	referenceText: "",
});

const manualForm = ref({
	title: "",
	question: "",
	choice1Content: "",
	choice2Content: "",
	choice3Content: "",
	choice4Content: "",
	answerNumber: 1,
	answer: "",
});

const isMultipleChoice = computed(() => {
	return commonForm.value.problemType === MULTIPLE_CHOICE;
});

const mode = computed(() => {
	return route.query.mode === MANUAL ? MANUAL : AI;
});

const resetForm = () => {
	commonForm.value.certId = "";
	aiForm.value.referenceText = "";
	manualForm.value.title = "";
	manualForm.value.question = "";
	manualForm.value.choice1Content = "";
	manualForm.value.choice2Content = "";
	manualForm.value.choice3Content = "";
	manualForm.value.choice4Content = "";
	manualForm.value.answerNumber = 1;
	manualForm.value.answer = "";
};

const setMode = (nextMode) => {
	router.replace({
		name: "create-certification-write",
		query: { ...route.query, mode: nextMode },
	});
};

const goModeSelect = () => {
	router.push({ name: "create-certification" });
};

const closeSuccessModal = async () => {
	showSuccessModal.value = false;

	await router.push({ name: "create" });
};

const closeFailureModal = () => {
	showFailureModal.value = false;
};

const onSubmit = async () => {
	isLoading.value = true;
	showSuccessModal.value = false;
	showFailureModal.value = false;

	try {
		let response = null;

		// AI 문제 생성
		if (mode.value === AI) {
			response = await authApi.post(
				`/problem/create/${commonForm.value.problemType === MULTIPLE_CHOICE ? "multiple-choice" : "short-answer"}/ai`,
				{
					certId: commonForm.value.certId,
					prolbemType: commonForm.value.problemType,
					referenceText: aiForm.value.referenceText,
				},
			);
		} else {
			// 객관식 문제 생성
			if (commonForm.value.problemType === MULTIPLE_CHOICE) {
				response = await authApi.post("/problem/create/multiple-choice", {
					certId: commonForm.value.certId,
					title: manualForm.value.title,
					question: manualForm.value.question,
					choice1Content: manualForm.value.choice1Content,
					choice2Content: manualForm.value.choice2Content,
					choice3Content: manualForm.value.choice3Content,
					choice4Content: manualForm.value.choice4Content,
					answerNumber: manualForm.value.answerNumber,
				});
				// 주관식 문제 생성
			} else {
				response = await authApi.post("/problem/create/short-answer", {
					certId: commonForm.value.certId,
					title: manualForm.value.title,
					question: manualForm.value.question,
					answer: manualForm.value.answer,
				});
			}
		}

		if (response.status === 201) {
			showSuccessModal.value = true;
		} else {
			showFailureModal.value = true;
		}
	} catch {
		showFailureModal.value = true;
	} finally {
		isLoading.value = false;
	}
};
</script>

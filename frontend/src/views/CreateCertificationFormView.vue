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
          class="h-9 w-full rounded-md border bg-slate-50 px-3 text-xs text-slate-700 outline-none transition focus:bg-white"
          :class="
            errorField.certId
              ? 'border-red-500'
              : ['border-slate-200', 'focus:border-blue-400']
          "
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

      <!-- AI 문제 생성 컴포넌트 -->
      <CertificationAiForm v-if="mode === AI" v-model="aiForm.referenceText" />

      <!-- 직접 문제 생성 컴포넌트 -->
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
        :error-field="errorField"
      />

      <!-- AI 생성 중 애니메이션 -->
      <div
        v-if="isLoading && mode === AI"
        class="mt-5 overflow-hidden rounded-xl border border-blue-200 bg-linear-to-r from-blue-50 via-white to-sky-50"
      >
        <div class="flex items-start gap-4 px-4 py-4">
          <div class="relative mt-0.5 flex h-10 w-10 shrink-0 items-center justify-center">
            <div class="absolute inset-0 rounded-full border-2 border-blue-200"></div>
            <div class="absolute inset-0 rounded-full border-2 border-transparent border-t-blue-500 animate-spin"></div>
            <div class="h-2 w-2 rounded-full bg-blue-500 shadow-[0_0_18px_rgba(59,130,246,0.45)]"></div>
          </div>
          <div class="min-w-0 flex-1">
            <div class="flex items-center gap-2">
              <p class="text-sm font-bold text-slate-800">AI가 자격증 문제를 생성하고 있습니다</p>
              <div class="flex items-center gap-1">
                <span class="h-1.5 w-1.5 rounded-full bg-blue-400 animate-bounce [animation-delay:-0.3s]"></span>
                <span class="h-1.5 w-1.5 rounded-full bg-blue-400 animate-bounce [animation-delay:-0.15s]"></span>
                <span class="h-1.5 w-1.5 rounded-full bg-blue-400 animate-bounce"></span>
              </div>
            </div>
            <p class="mt-1 text-xs font-medium text-blue-700">
              {{ aiGeneratingMessage }}
            </p>
            <div class="mt-3 grid grid-cols-3 gap-2 text-[11px] text-slate-500">
              <div class="rounded-lg border border-white/80 bg-white/80 px-3 py-2 shadow-sm">
                <p class="font-semibold text-slate-700">진행 시간</p>
                <p class="mt-0.5 text-xs font-bold text-blue-600">{{ elapsedLabel }}</p>
              </div>
              <div class="rounded-lg border border-white/80 bg-white/80 px-3 py-2 shadow-sm">
                <p class="font-semibold text-slate-700">작업 단계</p>
                <p class="mt-0.5 text-xs font-bold text-slate-600">{{ currentPhase }}</p>
              </div>
              <div class="rounded-lg border border-white/80 bg-white/80 px-3 py-2 shadow-sm">
                <p class="font-semibold text-slate-700">안내</p>
                <p class="mt-0.5 text-xs font-bold text-red-500">잠시만 기다려요</p>
              </div>
            </div>
            <div class="mt-3 h-2 overflow-hidden rounded-full bg-blue-100">
              <div class="h-full w-1/3 rounded-full bg-linear-to-r from-blue-400 via-sky-400 to-blue-500 animate-pulse"></div>
            </div>
            <p class="mt-2 text-[11px] text-slate-500">
              자료 분석, 문제 구성, 정답 검증까지 순차적으로 진행됩니다. 창을 닫지 말고 잠시만 기다려주세요.
            </p>
          </div>
        </div>
      </div>

      <div class="mt-5 flex justify-end border-t border-slate-100 pt-4">
        <button
          class="h-9 rounded-md bg-blue-600 px-5 text-xs font-semibold text-white transition hover:bg-blue-700 disabled:opacity-50"
          :disabled="isLoading"
          @click="onSubmit"
        >
          {{
            mode === AI
              ? isLoading
                ? "AI 생성 중..."
                : "AI 문제 생성"
              : isLoading
                ? "등록 중..."
                : "문제 등록"
          }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { authApi } from "@/api/client";
import CertificationAiForm from "@/components/certification/CertificationAiForm.vue";
import CertificationManualForm from "@/components/certification/CertificationManualForm.vue";
import { useCertificationStore } from "@/stores/certification";
import { computed, onUnmounted, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import { toast } from "vue3-toastify";

const AI = "ai";
const MANUAL = "manual";

const MULTIPLE_CHOICE = "MULTIPLE_CHOICE";
const SHORT_ANSWER = "SHORT_ANSWER";

const route = useRoute();
const router = useRouter();
const certificationStore = useCertificationStore();

const isLoading = ref(false);
const elapsedSeconds = ref(0);

const aiPhaseMessages = [
  "참고 자료를 분석하는 중",
  "문제 유형과 난이도를 설정하는 중",
  "선택지와 정답을 구성하는 중",
  "문제 품질을 검증하는 중",
];

let aiTimerId = null;

const currentPhase = computed(() => {
  const index = Math.floor(elapsedSeconds.value / 6) % aiPhaseMessages.length;
  return aiPhaseMessages[index];
});

const elapsedLabel = computed(() => {
  const minutes = Math.floor(elapsedSeconds.value / 60);
  const seconds = elapsedSeconds.value % 60;
  if (minutes === 0) return `${seconds}초`;
  return `${minutes}분 ${seconds.toString().padStart(2, "0")}초`;
});

const aiGeneratingMessage = computed(() =>
  elapsedSeconds.value >= 60
    ? "예상보다 조금 더 걸리고 있습니다. 검증 단계까지 진행 중일 수 있어요."
    : "AI 서버가 응답을 준비 중입니다. 잠시만 기다려주세요.",
);

const startAiTimer = () => {
  elapsedSeconds.value = 0;
  aiTimerId = window.setInterval(() => {
    elapsedSeconds.value += 1;
  }, 1000);
};

const stopAiTimer = () => {
  if (aiTimerId !== null) {
    window.clearInterval(aiTimerId);
    aiTimerId = null;
  }
};

onUnmounted(() => {
  stopAiTimer();
});

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
  answerNumber: "",
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

  Object.keys(manualForm.value).forEach((key) => {
    manualForm.value[key] = "";
  });
};

const setMode = (nextMode) => {
  if (nextMode !== mode.value) {
    resetErrorField();
  }

  router.replace({
    name: "create-certification-write",
    query: { ...route.query, mode: nextMode },
  });
};

const errorField = ref({
  certId: false,
  problemType: false,
  title: false,
  question: false,
  choice1Content: false,
  choice2Content: false,
  choice3Content: false,
  choice4Content: false,
  answerNumber: false,
  answer: false,
});

const goModeSelect = () => {
  router.push({ name: "create-certification" });
};

const isBlank = (value) => typeof value === "string" && value.trim() === "";

const resetErrorField = () => {
  Object.keys(errorField.value).forEach(
    (key) => (errorField.value[key] = false),
  );
};

const validateForm = () => {
  resetErrorField();

  let result = true;
  if (isBlank(commonForm.value.certId)) {
    errorField.value.certId = true;
    result = false;
  }

  if (mode.value === AI) {
    return result;
  }

  if (isBlank(manualForm.value.title)) {
    errorField.value.title = true;
    result = false;
  }
  if (isBlank(manualForm.value.question)) {
    errorField.value.question = true;
    result = false;
  }

  if (commonForm.value.problemType === MULTIPLE_CHOICE) {
    if (isBlank(manualForm.value.choice1Content)) {
      errorField.value.choice1Content = true;
      result = false;
    }
    if (isBlank(manualForm.value.choice2Content)) {
      errorField.value.choice2Content = true;
      result = false;
    }
    if (isBlank(manualForm.value.choice3Content)) {
      errorField.value.choice3Content = true;
      result = false;
    }
    if (isBlank(manualForm.value.choice4Content)) {
      errorField.value.choice4Content = true;
      result = false;
    }
    if (isBlank(manualForm.value.answerNumber)) {
      errorField.value.answerNumber = true;
      result = false;
    }
  } else if (commonForm.value.problemType === SHORT_ANSWER) {
    if (isBlank(manualForm.value.answer)) {
      errorField.value.answer = true;
      result = false;
    }
  }

  return result;
};

const onSubmit = async () => {
  const isValidAll = validateForm();
  if (!isValidAll) {
    toast.error("모든 필수 입력란을 채워주세요.");
    return;
  }
  isLoading.value = true;
  if (mode.value === AI) startAiTimer();

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
      // 직접 문제 생성
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

    toast.success("문제를 생성하였습니다.");
    router.push({
      name: "create",
    });
  } catch {
    toast.error("문제 생성에 실패하였습니다.");
  } finally {
    isLoading.value = false;
    stopAiTimer();
  }
};
</script>

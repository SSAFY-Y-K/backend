<template>
  <section class="min-h-full bg-slate-50 p-4">
    <div class="mx-auto max-w-3xl">
      <button
        class="mb-4 text-xs font-semibold text-slate-400 transition hover:text-slate-700"
        @click="router.back()"
      >
        ← 문제 목록으로
      </button>

      <Loading
        v-model:active="isLoading"
        :is-full-page="false"
        :can-cancel="false"
        loader="spinner"
      >
      </Loading>

      <div class="space-y-6">
        <!-- 객관식 문제 컴포넌트 -->
        <article
          v-if="problem.problemType === MULTIPLE_CHOICE"
          class="overflow-hidden rounded-lg border border-slate-200 bg-white shadow-sm"
        >
          <header class="bg-linear-to-br from-blue-50 via-white to-sky-50 p-5">
            <div class="mb-4 flex flex-wrap items-center gap-2">
              <span
                class="rounded-full border border-blue-100 bg-white/80 px-3 py-1 text-[11px] font-semibold text-blue-600 shadow-sm"
              >
                {{ problem.certificationName }}
              </span>
              <span
                class="rounded-full border border-blue-100 bg-blue-50 px-3 py-1 text-[11px] font-semibold text-blue-600"
              >
                객관식
              </span>
              <span class="ml-auto text-[11px] font-semibold text-slate-300">
                {{ `#${problem.problemId}` }}
              </span>
            </div>

            <h1 class="text-lg font-bold leading-7 text-slate-900">
              {{ problem.title }}
            </h1>
          </header>

          <div class="space-y-5 p-5">
            <section>
              <h2
                class="mb-3 text-xs font-bold uppercase tracking-wide text-slate-400"
              >
                문제
              </h2>
              <p class="whitespace-pre-wrap text-sm leading-7 text-slate-700">
                {{ problem.question }}
              </p>
            </section>

            <section>
              <h2
                class="mb-3 text-xs font-bold uppercase tracking-wide text-slate-400"
              >
                선택지
              </h2>

              <div class="space-y-2">
                <button
                  type="button"
                  :class="[
                    'flex w-full items-start gap-3 rounded-lg border p-3 text-left text-sm transition',
                    isGraded && Number(problem.answerNumber) === 1
                      ? 'border-green-500 bg-white text-slate-700'
                      : isGraded && userAnswer === 1
                        ? 'border-red-500 bg-white text-slate-700'
                        : userAnswer === 1
                          ? 'border-blue-300 bg-blue-50 text-blue-700'
                          : 'border-slate-200 bg-white text-slate-700 hover:border-slate-300 hover:bg-slate-50',
                  ]"
                  @click="userAnswer = 1"
                  :disabled="isGraded"
                >
                  <span
                    :class="[
                      'flex h-6 w-6 shrink-0 items-center justify-center rounded-full text-xs font-bold',
                      userAnswer === 1
                        ? 'bg-blue-600 text-white'
                        : 'bg-slate-100 text-slate-500',
                    ]"
                  >
                    1
                  </span>
                  <span class="leading-6">{{ problem.choice1Content }}</span>
                </button>

                <button
                  type="button"
                  :class="[
                    'flex w-full items-start gap-3 rounded-lg border p-3 text-left text-sm transition',
                    isGraded && Number(problem.answerNumber) === 2
                      ? 'border-green-500 bg-white text-slate-700'
                      : isGraded && userAnswer === 2
                        ? 'border-red-500 bg-white text-slate-700'
                        : userAnswer === 2
                          ? 'border-blue-300 bg-blue-50 text-blue-700'
                          : 'border-slate-200 bg-white text-slate-700 hover:border-slate-300 hover:bg-slate-50',
                  ]"
                  @click="userAnswer = 2"
                  :disabled="isGraded"
                >
                  <span
                    :class="[
                      'flex h-6 w-6 shrink-0 items-center justify-center rounded-full text-xs font-bold',
                      userAnswer === 2
                        ? 'bg-blue-600 text-white'
                        : 'bg-slate-100 text-slate-500',
                    ]"
                  >
                    2
                  </span>
                  <span class="leading-6">{{ problem.choice2Content }}</span>
                </button>

                <button
                  type="button"
                  :class="[
                    'flex w-full items-start gap-3 rounded-lg border p-3 text-left text-sm transition',
                    isGraded && Number(problem.answerNumber) === 3
                      ? 'border-green-500 bg-white text-slate-700'
                      : isGraded && userAnswer === 3
                        ? 'border-red-500 bg-white text-slate-700'
                        : userAnswer === 3
                          ? 'border-blue-300 bg-blue-50 text-blue-700'
                          : 'border-slate-200 bg-white text-slate-700 hover:border-slate-300 hover:bg-slate-50',
                  ]"
                  @click="userAnswer = 3"
                  :disabled="isGraded"
                >
                  <span
                    :class="[
                      'flex h-6 w-6 shrink-0 items-center justify-center rounded-full text-xs font-bold',
                      userAnswer === 3
                        ? 'bg-blue-600 text-white'
                        : 'bg-slate-100 text-slate-500',
                    ]"
                  >
                    3
                  </span>
                  <span class="leading-6">{{ problem.choice3Content }}</span>
                </button>

                <button
                  type="button"
                  :class="[
                    'flex w-full items-start gap-3 rounded-lg border p-3 text-left text-sm transition',
                    isGraded && Number(problem.answerNumber) === 4
                      ? 'border-green-500 bg-white text-slate-700'
                      : isGraded && userAnswer === 4
                        ? 'border-red-500 bg-white text-slate-700'
                        : userAnswer === 4
                          ? 'border-blue-300 bg-blue-50 text-blue-700'
                          : 'border-slate-200 bg-white text-slate-700 hover:border-slate-300 hover:bg-slate-50',
                  ]"
                  @click="userAnswer = 4"
                  :disabled="isGraded"
                >
                  <span
                    :class="[
                      'flex h-6 w-6 shrink-0 items-center justify-center rounded-full text-xs font-bold',
                      userAnswer === 4
                        ? 'bg-blue-600 text-white'
                        : 'bg-slate-100 text-slate-500',
                    ]"
                  >
                    4
                  </span>
                  <span class="leading-6">{{ problem.choice4Content }}</span>
                </button>
              </div>
            </section>
          </div>
        </article>

        <!-- 주관식 문제 컴포넌트 -->
        <article
          v-if="problem.problemType === SHORT_ANSWER"
          class="overflow-hidden rounded-lg border border-slate-200 bg-white shadow-sm"
        >
          <header
            class="bg-linear-to-br from-emerald-50 via-white to-teal-50 p-5"
          >
            <div class="mb-4 flex flex-wrap items-center gap-2">
              <span
                class="rounded-full border border-emerald-100 bg-white/80 px-3 py-1 text-[11px] font-semibold text-emerald-600 shadow-sm"
              >
                {{ problem.certificationName }}
              </span>
              <span
                class="rounded-full border border-emerald-100 bg-emerald-50 px-3 py-1 text-[11px] font-semibold text-emerald-600"
              >
                주관식
              </span>
              <span class="ml-auto text-[11px] font-semibold text-slate-300">
                {{ `#${problem.problemId}` }}
              </span>
            </div>

            <h1 class="text-lg font-bold leading-7 text-slate-900">
              {{ problem.title }}
            </h1>
          </header>

          <div class="space-y-5 p-5">
            <section>
              <h2
                class="mb-3 text-xs font-bold uppercase tracking-wide text-slate-400"
              >
                문제
              </h2>
              <p class="whitespace-pre-wrap text-sm leading-7 text-slate-700">
                {{ problem.question }}
              </p>
            </section>

            <section>
              <label
                for="short-answer"
                class="mb-2 block text-xs font-bold uppercase tracking-wide text-slate-400"
              >
                답안 입력
              </label>
              <input
                id="short-answer"
                type="text"
                v-model="userAnswer"
                :disabled="isGraded"
                class="h-11 w-full rounded-md border bg-slate-50 px-3 text-sm text-slate-700 outline-none transition focus:bg-white"
                :class="
                  isGraded
                    ? isCorrect
                      ? 'border-green-500'
                      : 'border-red-500'
                    : ['border-slate-200', 'focus:border-blue-400']
                "
              />
            </section>
          </div>
        </article>

        <!-- 채점 결과 컴포넌트 -->
        <article
          v-if="isGraded"
          :class="[
            'rounded-lg border p-6 shadow-sm',
            isCorrect
              ? 'border-emerald-100 bg-emerald-50'
              : 'border-orange-100 bg-orange-50',
          ]"
        >
          <p
            :class="[
              'text-base font-bold',
              isCorrect ? 'text-emerald-600' : 'text-orange-700',
            ]"
          >
            {{ isCorrect ? "정답입니다!" : "오답입니다." }}
          </p>
          <p class="mt-2 text-sm text-slate-600">
            정답:
            {{
              problem.problemType === MULTIPLE_CHOICE
                ? `${problem.answerNumber}번`
                : problem.answer
            }}
          </p>
        </article>

        <div class="flex justify-end gap-2">
          <button
            class="rounded-md border border-slate-200 bg-white px-4 py-2 text-xs font-semibold text-slate-500 transition hover:border-slate-300 hover:bg-slate-50 hover:text-slate-700"
            @click="router.back()"
          >
            목록
          </button>
          <button
            class="rounded-md bg-blue-600 px-5 py-2 text-xs font-semibold text-white transition hover:bg-blue-700 disabled:bg-blue-900"
            @click="onClick"
            :disabled="isGraded"
          >
            정답 확인
          </button>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { authApi } from "@/api/client";
import { computed, onMounted, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import { Component as Loading } from "vue-loading-overlay";
import { toast } from "vue3-toastify";

const MULTIPLE_CHOICE = "MULTIPLE_CHOICE";
const SHORT_ANSWER = "SHORT_ANSWER";

const isLoading = ref(false);
const router = useRouter();
const route = useRoute();

const problem = ref({
  problemId: 0,
  certId: 0,
  certificationName: "",
  problemType: "",
  problemTitle: "",
  question: "",
  choice1Content: "",
  choice2Content: "",
  choice3Content: "",
  choice4Content: "",
  answerNumber: "",
});

const userAnswer = ref("");

const isGraded = ref(false);

const isCorrect = computed(() => {
  if (problem.value.problemType === MULTIPLE_CHOICE) {
    return Number(userAnswer.value) === Number(problem.value.answerNumber);
  }

  return (
    String(userAnswer.value).trim() === String(problem.value.answer).trim()
  );
});

const fetchProblem = async () => {
  isLoading.value = true;
  const problemType = route.query.problemType;
  problem.value.problemType = problemType;
  isGraded.value = false;

  try {
    const response = await authApi(
      `/problem/${problemType === MULTIPLE_CHOICE ? "multiple-choice" : "short-answer"}/${route.params.id}`,
    );

    problem.value = response.data;
  } catch (error) {
  } finally {
    isLoading.value = false;
  }
};

onMounted(async () => {
  await fetchProblem();
});

const onClick = () => {
  // 사용자가 정답 선택 안함
  if (
    problem.value.problemType === MULTIPLE_CHOICE &&
    userAnswer.value === ""
  ) {
    toast.error("정답을 선택해주세요.");
    return;
  }

  if (
    problem.value.problemType === SHORT_ANSWER &&
    String(userAnswer.value).trim() === ""
  ) {
    toast.error("정답을 입력해주세요.");
    return;
  }

  isGraded.value = true;
};
</script>

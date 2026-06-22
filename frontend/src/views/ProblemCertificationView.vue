<template>
  <div>
    <div class="mb-4">
      <select
        class="h-7 rounded-md border border-slate-200 bg-white px-2 text-xs text-slate-600 outline-none focus:border-blue-400"
        v-model="searchCertification"
        @change="onChange"
      >
        <option value="all" selected>전체 자격증</option>
        <option
          v-for="certification in certificationStore.getCertifications()"
          :key="certification.certId"
          :value="certification.certId"
        >
          {{ certification.name }}
        </option>
      </select>
    </div>

    <div class="vl-parent relative min-h-60">
      <Loading
        v-model:active="isLoading"
        :is-full-page="false"
        :can-cancel="false"
        :opacity="0.45"
        color="#2563eb"
        :height="48"
        :width="48"
        loader="spinner"
      />

      <div class="mb-3 grid grid-cols-1 gap-4 sm:grid-cols-2 xl:grid-cols-3">
        <div
          v-for="problem in problems"
          :key="problem.problemId"
          class="group flex min-h-44 flex-col overflow-hidden rounded-xl border border-slate-200 bg-white shadow-sm ring-1 ring-slate-100 transition hover:-translate-y-0.5 hover:border-blue-200 hover:shadow-lg hover:shadow-blue-100/60"
        >
          <div
            class="relative flex flex-1 flex-col justify-between overflow-hidden bg-linear-to-br from-blue-50 via-white to-sky-50 p-4"
          >
            <div
              class="absolute -right-8 -top-8 h-24 w-24 rounded-full bg-blue-100/70 transition group-hover:scale-110"
            ></div>
            <div
              class="absolute right-9 top-9 h-10 w-10 rounded-full bg-white/70"
            ></div>

            <div class="relative flex items-start justify-between gap-3">
              <span
                class="rounded-full border border-blue-100 bg-white/80 px-2.5 py-1 text-[11px] font-semibold text-blue-600 shadow-sm"
              >
                {{ getCertificationName(problem) }}
              </span>
              <span class="text-[10px] font-semibold text-slate-300">
                #{{ problem.problemId }}
              </span>
            </div>

            <h3
              class="relative mt-6 line-clamp-2 text-sm font-bold leading-5 text-slate-800"
            >
              {{ problem.problemTitle }}
            </h3>
          </div>
          <div
            class="flex items-center justify-between border-t border-slate-100 p-3"
          >
            <span
              :class="[
                'inline-flex items-center gap-1.5 rounded-full border px-2.5 py-1 text-[11px] font-semibold',
                problemTypeBadgeClasses[problem.problemType] ??
                  'border-slate-100 bg-slate-50 text-slate-500',
              ]"
            >
              <span
                :class="[
                  'h-1.5 w-1.5 rounded-full',
                  problemTypeDotClasses[problem.problemType] ?? 'bg-slate-400',
                ]"
              ></span>
              {{
                problemTypeLabels[problem.problemType] ?? problem.problemType
              }}
            </span>
            <RouterLink
              :to="{
                name: 'problem-detail',
                params: { id: problem.problemId },
                query: { problemType: problem.problemType },
              }"
              class="rounded-md bg-blue-600 px-3 py-1.5 text-[10px] font-semibold text-white no-underline transition hover:bg-blue-700"
            >
              풀기
            </RouterLink>
          </div>
        </div>
      </div>

      <div v-if="!isLast" class="mt-5 flex justify-center">
        <button
          @click="fetchProblems"
          type="button"
          class="rounded-md border border-blue-200 bg-white px-4 py-2 text-xs font-semibold text-blue-600 transition hover:-translate-y-0.5 hover:border-blue-400 hover:bg-blue-50 hover:shadow-md disabled:bg-slate-200 hover:shadow-blue-100"
          :disabled="isLoading"
        >
          더보기
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { publicApi } from "@/api/client";
import { useCertificationStore } from "@/stores/certification";
import { onMounted, ref, watch } from "vue";
import { useRoute } from "vue-router";
import { Component as Loading } from "vue-loading-overlay";

const certificationStore = useCertificationStore();
const route = useRoute();

const isLoading = ref(false);

// 페이징 용
const minProblemId = ref(null);

const searchCertification = ref(route.query.cert_id ?? "all");

const problems = ref([]);

const isLast = ref(false);

const problemTypeLabels = {
  MULTIPLE_CHOICE: "객관식",
  SHORT_ANSWER: "주관식",
};

const problemTypeBadgeClasses = {
  MULTIPLE_CHOICE: "border-blue-100 bg-blue-50 text-blue-600",
  SHORT_ANSWER: "border-emerald-100 bg-emerald-50 text-emerald-600",
};

const problemTypeDotClasses = {
  MULTIPLE_CHOICE: "bg-blue-500",
  SHORT_ANSWER: "bg-emerald-500",
};

const getCertificationName = (problem) => {
  return (
    certificationStore
      .getCertifications()
      .find((certification) => certification.certId === problem.certId)?.name ??
    "자격증 문제"
  );
};

const fetchProblems = async () => {
  isLoading.value = true;
  try {
    const response = await publicApi("/problem", {
      params: {
        cert_id:
          searchCertification.value === "all"
            ? null
            : searchCertification.value,
        problem_id: minProblemId.value,
      },
    });

    const findProblems = response.data?.problems ?? [];
    if (findProblems.length > 0) {
      minProblemId.value = response.data?.minProblemId ?? minProblemId.value;
      problems.value.push(...findProblems);
    }
    isLast.value = response.data?.isLast ?? false;
  } catch (error) {
  } finally {
    isLoading.value = false;
  }
};

onMounted(async () => {
  await fetchProblems();
});

const onChange = async () => {
  isLast.value = false;
  minProblemId.value = null;
  problems.value = [];
  await fetchProblems();
};

watch(
  () => route.query.cert_id,
  async (certId) => {
    const nextCertification = certId ?? "all";
    if (searchCertification.value === nextCertification) return;

    searchCertification.value = nextCertification;
    await onChange();
  },
);
</script>

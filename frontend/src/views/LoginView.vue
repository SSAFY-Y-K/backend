<template>
  <div
    class="flex min-h-full items-center justify-center bg-gradient-to-br from-slate-100 via-slate-100 to-blue-50 px-4 py-12"
  >
    <div class="animate-scale-in w-full max-w-sm">
      <div
        class="overflow-hidden rounded-xl border border-slate-200/80 bg-white shadow-xl shadow-slate-300/40 ring-1 ring-slate-900/5"
      >
        <!-- 브랜드 헤더 -->
        <div class="bg-gradient-to-br from-[#1d3461] via-[#1e4090] to-[#2563eb] px-6 py-6 text-center">
          <div class="mx-auto flex h-11 w-11 items-center justify-center rounded-xl bg-white/20 text-base font-extrabold text-white ring-1 ring-white/25">
            P
          </div>
          <p class="mt-2 text-sm font-bold tracking-widest text-white">PASSIT</p>
          <p class="mt-0.5 text-[11px] text-blue-200">자격증 &amp; 코딩 학습 플랫폼</p>
        </div>
        <div class="p-6">
          <form class="space-y-4" @submit.prevent="onSubmit">
            <div>
              <label
                class="mb-1 block text-xs font-medium text-slate-600"
                for="username"
                >아이디</label
              >
              <input
                v-model="form.username"
                ref="username-input"
                id="username"
                type="text"
                placeholder="아이디를 입력하세요."
                class="h-9 w-full rounded-md border bg-white px-3 text-xs text-slate-800 outline-none transition focus:ring-1 focus:ring-blue-100"
                :class="
                  errorField.username
                    ? 'border-red-500'
                    : ['border-slate-200', 'focus:border-blue-400']
                "
              />
              <p
                v-if="errorField.username"
                class="mt-1 text-[11px] text-red-500"
              >
                {{ errorField.username }}
              </p>
            </div>

            <div>
              <label
                class="mb-1 block text-xs font-medium text-slate-600"
                for="password"
                >비밀번호</label
              >
              <input
                v-model="form.password"
                ref="password-input"
                type="password"
                placeholder="비밀번호를 입력하세요."
                class="h-9 w-full rounded-md border bg-white px-3 text-xs text-slate-800 outline-none transition focus:ring-1 focus:ring-blue-100"
                :class="
                  errorField.password
                    ? 'border-red-500'
                    : ['border-slate-200', 'focus:border-blue-400']
                "
              />
              <p
                v-if="errorField.password"
                class="mt-1 text-[11px] text-red-500"
              >
                {{ errorField.password }}
              </p>
            </div>

            <button
              type="submit"
              class="vl-parent relative flex h-9 w-full items-center justify-center overflow-hidden rounded-md bg-gradient-to-r from-blue-600 to-blue-700 text-xs font-semibold text-white shadow-sm shadow-blue-500/25 transition hover:from-blue-700 hover:to-blue-800 disabled:from-gray-400 disabled:to-gray-400 disabled:shadow-none"
              :disabled="isLoading"
            >
              <p v-if="!isLoading">로그인</p>
              <Loading
                v-model:active="isLoading"
                :is-full-page="false"
                :can-cancel="false"
                background-color="transparent"
                color="#ffffff"
                :height="18"
                :width="18"
                loader="spinner"
              />
            </button>
            <p v-if="errorField.all" class="mt-1 text-[11px] text-red-500">
              {{ errorField.all }}
            </p>
          </form>

          <div
            class="mt-4 flex items-center justify-center gap-2 text-xs text-slate-400"
          >
            <RouterLink
              :to="{ name: 'signup' }"
              class="text-blue-500 no-underline hover:text-blue-600"
            >
              회원가입
            </RouterLink>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { publicApi } from "@/api/client";
import { useRouter } from "vue-router";
import { useRoute } from "vue-router";
import { useAuthStore } from "@/stores/auth";
import { ref, useTemplateRef } from "vue";
import { toast } from "vue3-toastify";
import { Component as Loading } from "vue-loading-overlay";

const router = useRouter();
const route = useRoute();

const authStore = useAuthStore();

const isLoading = ref(false);

const usernameRef = useTemplateRef("username-input");
const passwordRef = useTemplateRef("password-input");

const form = ref({
  username: "",
  password: "",
});

const errorField = ref({
  username: "",
  password: "",
  all: "",
});

const resetErrorField = () => {
  errorField.value.username = "";
  errorField.value.password = "";
  errorField.value.all = "";
};

const onSubmit = async () => {
  resetErrorField();

  if (form.value.username.trim() === "") {
    errorField.value.username = "아이디를 입력해주세요.";
    usernameRef.value.focus();
    return;
  } else if (form.value.password.trim() === "") {
    errorField.value.password = "비밀번호를 입력해주세요.";
    passwordRef.value.focus();
    return;
  }

  try {
    isLoading.value = true;
    const response = await publicApi.post("/auth/login", form.value);
    // 액세스 토큰 저장
    const accessToken = response.data.accessToken;
    authStore.setAccessToken(accessToken);

    isLoading.value = false;

    toast.success("로그인 되었습니다.");
    // 리다이렉트
    const redirectPath = route.query.redirect;
    if (typeof redirectPath === "string" && redirectPath.startsWith("/")) {
      router.push(redirectPath);
    } else {
      router.push({
        name: "home",
      });
    }
  } catch (error) {
    if (error.response?.status === 401) {
      errorField.value.all = "아이디 또는 비밀번호가 잘못되었습니다.";
    } else {
      errorField.value.all =
        "알수 없는 예외가 발생했습니다. 다시 시도해주세요.";
    }
    isLoading.value = false;
  }
};
</script>

<template>
  <div
    class="flex min-h-full items-center justify-center bg-gradient-to-br from-slate-100 via-slate-100 to-blue-50 px-4 py-12"
  >
    <div class="animate-scale-in w-full max-w-sm">
      <div
        class="overflow-hidden rounded-xl border border-slate-200/80 bg-white shadow-xl shadow-slate-300/40 ring-1 ring-slate-900/5"
      >
        <!-- 브랜드 헤더 -->
        <div class="bg-gradient-to-br from-[#1d3461] via-[#1e4090] to-[#2563eb] px-6 py-5 text-center">
          <div class="mx-auto flex h-11 w-11 items-center justify-center rounded-xl bg-white/20 text-base font-extrabold text-white ring-1 ring-white/25">
            P
          </div>
          <p class="mt-2 text-sm font-bold tracking-widest text-white">PASSIT</p>
          <p class="mt-0.5 text-[11px] text-blue-200">회원가입</p>
        </div>
        <div class="p-6">
          <form @submit.prevent="onSubmit" class="space-y-4" novalidate>
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
                placeholder="아이디를 입력해주세요."
                class="h-9 w-full rounded-md border bg-white px-3 text-xs text-slate-800 outline-none transition focus:ring-1 focus:ring-blue-100"
                :class="
                  errorField.username
                    ? 'border-red-500'
                    : ['border-slate-200', 'focus:border-blue-400']
                "
                required
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
              <div class="relative">
                <input
                  v-model="form.password"
                  ref="password-input"
                  id="password"
                  type="password"
                  placeholder="비밀번호"
                  class="h-9 w-full rounded-md border bg-white px-3 pr-8 text-xs text-slate-800 outline-none transition focus:ring-1 focus:ring-blue-100"
                  :class="
                    errorField.password
                      ? 'border-red-500'
                      : ['border-slate-200', 'focus:border-blue-400']
                  "
                  required
                />

                <p
                  v-if="errorField.password"
                  class="mt-1 text-[11px] text-red-500"
                >
                  {{ errorField.password }}
                </p>
              </div>
            </div>

            <div>
              <label
                class="mb-1 block text-xs font-medium text-slate-600"
                for="confirmPassword"
                >비밀번호 확인</label
              >
              <input
                v-model="form.confirmPassword"
                ref="confirmPassword-input"
                id="confirmPassword"
                type="password"
                placeholder="비밀번호를 다시 입력해주세요."
                class="h-9 w-full rounded-md border bg-white px-3 text-xs text-slate-800 outline-none transition focus:ring-1 focus:ring-blue-100"
                :class="
                  errorField.confirmPassword
                    ? 'border-red-500'
                    : ['border-slate-200', 'focus:border-blue-400']
                "
                required
              />
              <p
                v-if="errorField.confirmPassword"
                class="mt-1 text-[11px] text-red-500"
              >
                {{ errorField.confirmPassword }}
              </p>
            </div>

            <div>
              <label
                class="mb-1 block text-xs font-medium text-slate-600"
                for="nickname"
                >닉네임</label
              >
              <input
                v-model="form.nickname"
                ref="nickname-input"
                type="text"
                id="nickname"
                placeholder="닉네임"
                class="h-9 w-full rounded-md border bg-white px-3 text-xs text-slate-800 outline-none transition focus:border-blue-400 focus:ring-1 focus:ring-blue-100"
                :class="
                  errorField.nickname
                    ? 'border-red-500'
                    : ['border-slate-200', 'focus:border-blue-400']
                "
                required
              />
              <p
                v-if="errorField.nickname"
                class="mt-1 text-[11px] text-red-500"
              >
                {{ errorField.nickname }}
              </p>
            </div>

            <button
              type="submit"
              class="vl-parent relative flex h-9 w-full items-center justify-center overflow-hidden rounded-md bg-gradient-to-r from-blue-600 to-blue-700 text-xs font-semibold text-white shadow-sm shadow-blue-500/25 transition hover:from-blue-700 hover:to-blue-800 disabled:from-gray-400 disabled:to-gray-400 disabled:shadow-none"
              :disabled="isLoading"
            >
              <p v-if="!isLoading">회원가입</p>
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
          </form>

          <div class="mt-4 text-center text-xs text-slate-400">
            이미 계정이 있으신가요?
            <RouterLink
              :to="{ name: 'login' }"
              class="text-blue-500 no-underline hover:text-blue-600"
            >
              로그인
            </RouterLink>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { publicApi } from "@/api/client";
import { ref, useTemplateRef } from "vue";
import { useRouter } from "vue-router";
import { toast } from "vue3-toastify";
import { Component as Loading } from "vue-loading-overlay";

const router = useRouter();

const isLoading = ref(false);

const usernameRef = useTemplateRef("username-input");
const passwordRef = useTemplateRef("password-input");
const confirmPasswordRef = useTemplateRef("confirmPassword-input");
const nicknameRef = useTemplateRef("nickname-input");

const form = ref({
  username: "",
  password: "",
  confirmPassword: "",
  nickname: "",
});

const errorField = ref({
  username: "",
  password: "",
  confirmPassword: "",
  nickname: "",
});

const resetErrorField = () => {
  errorField.value.username = "";
  errorField.value.password = "";
  errorField.value.confirmPassword = "";
  errorField.value.nickname = "";
};

const isUsernameEmpty = () => {
  return form.value.username.trim() === "";
};

const isNicknameEmpty = () => {
  return form.value.nickname.trim() === "";
};

const isPasswordEmpty = () => {
  return form.value.password.trim() === "";
};

const isConfirmPasswordEmpty = () => {
  return form.value.confirmPassword.trim() === "";
};

const isPasswordSame = () => {
  return form.value.password === form.value.confirmPassword;
};

const goHome = async () => {
  showSuccessModal.value = false;

  await router.push({
    name: "home",
  });
};

const onSubmit = async () => {
  resetErrorField();
  if (isUsernameEmpty()) {
    errorField.value.username = "아이디를 입력해주세요.";
    usernameRef?.value.focus();
    return;
  } else if (isPasswordEmpty()) {
    errorField.value.password = "비밀번호를 입력해주세요.";
    passwordRef?.value.focus();
    return;
  } else if (isConfirmPasswordEmpty()) {
    errorField.value.confirmPassword = "비밀번호 확인을 입력해주세요.";
    confirmPasswordRef?.value.focus();
    return;
  } else if (!isPasswordSame()) {
    errorField.value.confirmPassword = "비밀번호가 일치하지 않습니다.";
    return;
  } else if (isNicknameEmpty()) {
    errorField.value.nickname = "닉네임을 입력해주세요.";
    nicknameRef?.value.focus();
    return;
  }

  try {
    isLoading.value = true;
    await publicApi.post("/users/signup", form.value);
    toast.success("회원가입이 완료되었습니다.");
    router.push({
      name: "login",
    });
  } catch (error) {
    if (!error.response) {
      return;
    }
    const field = error.response.data?.errorField;
    errorField.value[field] = error.response?.data?.detail ?? "";
  } finally {
    isLoading.value = false;
  }
};
</script>

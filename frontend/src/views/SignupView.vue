<template>
	<div class="min-h-full bg-slate-50 px-4 py-10">
		<div class="mx-auto flex w-full max-w-md flex-col gap-6">
			<div class="text-center">
				<p class="text-sm font-semibold tracking-wide text-blue-600">PASSIT</p>
				<h1 class="mt-2 text-3xl font-bold text-slate-900">회원가입</h1>
				<p class="mt-2 text-sm text-slate-500">
					계정을 만들고 문제 풀이와 커뮤니티를 시작하세요.
				</p>
			</div>

			<form
				class="rounded-2xl border border-slate-200 bg-white p-8 shadow-sm"
				@submit.prevent="handleSubmit"
			>
				<div class="space-y-5">
					<div class="space-y-2">
						<label for="username" class="text-sm font-medium text-slate-700">
							아이디
						</label>
						<input
							id="username"
							v-model="username"
							type="text"
							autocomplete="username"
							placeholder="아이디를 입력하세요"
							class="block w-full rounded-lg border border-slate-300 px-4 py-3 text-slate-900 outline-none transition placeholder:text-slate-400 focus:border-blue-500 focus:ring-2 focus:ring-blue-100"
						/>
					</div>

					<div class="space-y-2">
						<label for="password" class="text-sm font-medium text-slate-700">
							비밀번호
						</label>
						<input
							id="password"
							v-model="password"
							type="password"
							autocomplete="new-password"
							placeholder="비밀번호를 입력하세요"
							class="block w-full rounded-lg border border-slate-300 px-4 py-3 text-slate-900 outline-none transition placeholder:text-slate-400 focus:border-blue-500 focus:ring-2 focus:ring-blue-100"
						/>
					</div>

					<div class="space-y-2">
						<label
							for="passwordConfirm"
							class="text-sm font-medium text-slate-700"
						>
							비밀번호 확인
						</label>
						<input
							id="passwordConfirm"
							v-model="passwordConfirm"
							type="password"
							autocomplete="new-password"
							placeholder="비밀번호를 다시 입력하세요"
							class="block w-full rounded-lg border border-slate-300 px-4 py-3 text-slate-900 outline-none transition placeholder:text-slate-400 focus:border-blue-500 focus:ring-2 focus:ring-blue-100"
						/>
						<p v-if="passwordMismatch" class="text-sm text-red-500">
							비밀번호가 일치하지 않습니다.
						</p>
					</div>

					<div class="space-y-2">
						<label for="nickname" class="text-sm font-medium text-slate-700">
							닉네임
						</label>
						<input
							id="nickname"
							v-model="nickname"
							type="text"
							autocomplete="nickname"
							placeholder="닉네임을 입력하세요"
							class="block w-full rounded-lg border border-slate-300 px-4 py-3 text-slate-900 outline-none transition placeholder:text-slate-400 focus:border-blue-500 focus:ring-2 focus:ring-blue-100"
						/>
					</div>

					<button
						type="submit"
						:disabled="passwordMismatch"
						class="inline-flex w-full items-center justify-center rounded-lg bg-blue-600 px-4 py-3 font-semibold text-white transition hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-200 disabled:cursor-not-allowed disabled:bg-slate-300"
					>
						회원가입
					</button>
				</div>
			</form>

			<div class="text-center text-sm text-slate-500">
				이미 계정이 있으신가요?
				<RouterLink
					:to="{ name: 'login' }"
					class="font-medium text-blue-600 transition hover:text-blue-700"
				>
					로그인
				</RouterLink>
			</div>
		</div>
	</div>
</template>

<script setup>
import { computed, ref } from "vue";

const username = ref("");
const password = ref("");
const passwordConfirm = ref("");
const nickname = ref("");

const passwordMismatch = computed(() => {
	return passwordConfirm.value.length > 0 && password.value !== passwordConfirm.value;
});

const handleSubmit = () => {
	if (passwordMismatch.value) {
		return;
	}

	console.log("signup submit", {
		username: username.value,
		password: password.value,
		nickname: nickname.value,
	});
};
</script>

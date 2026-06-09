<template>
	<div class="min-h-full bg-slate-50 px-4 py-10">
		<div class="mx-auto flex w-full max-w-md flex-col gap-6">
			<div class="text-center">
				<p class="text-sm font-semibold tracking-wide text-blue-600">PASSIT</p>
				<h1 class="mt-2 text-3xl font-bold text-slate-900">로그인</h1>
				<p class="mt-2 text-sm text-slate-500">
					문제 풀이와 커뮤니티 기능을 계속 이용하려면 로그인하세요.
				</p>
			</div>

			<form
				class="rounded-2xl border border-slate-200 bg-white p-8 shadow-sm"
				@submit.prevent="handleSubmit"
			>
				<div class="space-y-5">
					<div class="space-y-2">
						<label for="email" class="text-sm font-medium text-slate-700">
							이메일
						</label>
						<input
							id="email"
							v-model="email"
							type="email"
							autocomplete="email"
							placeholder="name@example.com"
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
							autocomplete="current-password"
							placeholder="비밀번호를 입력하세요"
							class="block w-full rounded-lg border border-slate-300 px-4 py-3 text-slate-900 outline-none transition placeholder:text-slate-400 focus:border-blue-500 focus:ring-2 focus:ring-blue-100"
						/>
					</div>

					<div class="flex items-center justify-between gap-3 text-sm">
						<label class="flex items-center gap-2 text-slate-600">
							<input
								v-model="rememberMe"
								type="checkbox"
								class="h-4 w-4 rounded border-slate-300 text-blue-600 focus:ring-blue-500"
							/>
							로그인 상태 유지
						</label>

						<button
							type="button"
							class="font-medium text-blue-600 transition hover:text-blue-700"
						>
							비밀번호 찾기
						</button>
					</div>

					<button
						type="submit"
						class="inline-flex w-full items-center justify-center rounded-lg bg-blue-600 px-4 py-3 font-semibold text-white transition hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-200"
					>
						로그인
					</button>
				</div>
			</form>

			<div class="text-center text-sm text-slate-500">
				아직 계정이 없으신가요?
				<RouterLink
					:to="{ name: 'signup' }"
					class="font-medium text-blue-600 transition hover:text-blue-700"
				>
					회원가입
				</RouterLink>
			</div>

			<div class="text-center text-sm text-slate-500">
				<RouterLink
					to="/"
					class="font-medium text-blue-600 transition hover:text-blue-700"
				>
					홈으로 돌아가기
				</RouterLink>
			</div>
		</div>
	</div>
</template>

<script setup>
import { ref } from "vue";

const email = ref("");
const password = ref("");
const rememberMe = ref(true);

const handleSubmit = () => {
	console.log("login submit", {
		email: email.value,
		password: password.value,
		rememberMe: rememberMe.value,
	});
};
</script>

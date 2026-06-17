<template>
	<div class="flex min-h-full items-center justify-center bg-gray-50 px-4 py-12">
		<div class="w-full max-w-sm">
			<div class="overflow-hidden rounded-lg border border-slate-200 bg-white shadow-sm">
				<div class="p-6">
					<form class="space-y-4">
						<div>
							<label class="mb-1 block text-xs font-medium text-slate-600">이매일</label>
							<input
								type="email"
								placeholder=""
								class="h-9 w-full rounded-md border border-slate-200 bg-white px-3 text-xs text-slate-800 outline-none transition focus:border-blue-400 focus:ring-1 focus:ring-blue-100"
							/>
						</div>

						<div>
							<label class="mb-1 block text-xs font-medium text-slate-600">비밀번호</label>
							<div class="relative">
								<input
									:type="showPw ? 'text' : 'password'"
									placeholder=""
									class="h-9 w-full rounded-md border border-slate-200 bg-white px-3 pr-8 text-xs text-slate-800 outline-none transition focus:border-blue-400 focus:ring-1 focus:ring-blue-100"
								/>
								<button
									type="button"
									class="absolute right-2.5 top-1/2 -translate-y-1/2 text-slate-400 transition hover:text-slate-600"
									@click="showPw = !showPw"
								>
									<svg class="h-3.5 w-3.5" viewBox="0 0 14 14" fill="none" stroke="currentColor" stroke-width="1.4">
										<path d="M1 7s2.5-4.5 6-4.5S13 7 13 7s-2.5 4.5-6 4.5S1 7 1 7z" />
										<circle cx="7" cy="7" r="1.8" />
									</svg>
								</button>
							</div>
						</div>

						<button
							type="submit"
							class="h-9 w-full rounded-md bg-blue-600 text-xs font-semibold text-white transition hover:bg-blue-700"
						>
							로그인
						</button>
					</form>

					<div class="mt-4 flex items-center justify-center gap-2 text-xs text-slate-400">
						<RouterLink :to="{ name: 'signup' }" class="text-blue-500 no-underline hover:text-blue-600">
							회원가입
						</RouterLink>
						<span>·</span>
						<button class="text-slate-400 hover:text-slate-600">비밀번호 찾기</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script setup>
import { ref } from "vue";
const showPw = ref(false);
</script>

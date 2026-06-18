import { defineStore } from "pinia";
import { computed, ref } from "vue";

export const useAuthStore = defineStore("auth", () => {
	const accessToken = ref(null);

	/**
	 * access token을 가지고 있으면 true, 없으면 false 반환
	 */
	const hasAccessToken = computed(() => {
		return !!accessToken.value;
	});

	/**
	 * access token 설정
	 * @param {string} token 설정할 토큰
	 */
	const setAccessToken = (token) => {
		accessToken.value = token;
	};

	/**
	 * access token 반환
	 * @returns 현재 가지고 있는 토큰
	 */
	const getAccessToken = () => {
		return accessToken.value;
	};

	/**
	 * access token 초기화
	 */
	const clearAccessToken = () => {
		accessToken.value = null;
	};

	return {
		setAccessToken,
		getAccessToken,
		hasAccessToken,
		clearAccessToken,
	};
});

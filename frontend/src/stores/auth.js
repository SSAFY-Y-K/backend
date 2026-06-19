import { publicApi } from "@/api/client";
import { defineStore } from "pinia";
import { computed, ref } from "vue";

const decodeJwtPayload = (token) => {
	try {
		return JSON.parse(atob(token.split(".")[1]));
	} catch {
		return null;
	}
};

export const useAuthStore = defineStore("auth", () => {
	const accessToken = ref(null);

	const hasAccessToken = computed(() => !!accessToken.value);

	const userId = computed(() => {
		const payload = decodeJwtPayload(accessToken.value);
		return payload?.userId ?? null;
	});

	const username = computed(() => {
		const payload = decodeJwtPayload(accessToken.value);
		return payload?.sub ?? null;
	});

	const role = computed(() => {
		const payload = decodeJwtPayload(accessToken.value);
		return payload?.role ?? null;
	});

	const isAdmin = computed(() => role.value === "ADMIN");

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

	/**
	 * 앱 진입/새로고침 시 access token 재발급
	 */
	const initializeAccessToken = async () => {
		try {
			const response = await publicApi.post("/auth/refresh");
			accessToken.value = response.data.accessToken;
		} catch (error) {
			accessToken.value = null;
		}
	};

	return {
		setAccessToken,
		getAccessToken,
		hasAccessToken,
		clearAccessToken,
		initializeAccessToken,
		userId,
		username,
		role,
		isAdmin,
	};
});

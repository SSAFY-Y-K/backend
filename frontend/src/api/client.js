import { useAuthStore } from "@/stores/auth";
import axios from "axios";

/**
 * access token이 필요하지 않은 요청
 */
export const publicApi = axios.create({
	baseURL: "/api",
	withCredentials: true,
});

/**
 * access token이 필요한 요청
 */
export const authApi = axios.create({
	baseURL: "/api",
	withCredentials: true,
});

authApi.interceptors.request.use(
	(config) => {
		const authStore = useAuthStore();

		if (authStore.hasAccessToken) {
			config.headers.Authorization = `Bearer ${authStore.getAccessToken()}`;
		}
		return config;
	},
	(error) => Promise.reject(error),
);

authApi.interceptors.response.use(
	(response) => response,
	async (error) => {
		const authStore = useAuthStore();

		const originalRequest = error.config;

		if (error.response?.status === 401 && !originalRequest._retry) {
			originalRequest._retry = true;

			try {
				const refreshResponse = await publicApi.post("/auth/refresh");

				authStore.setAccessToken(refreshResponse.data.accessToken);

				originalRequest.headers.Authorization = `Bearer ${authStore.getAccessToken()}`;

				return authApi(originalRequest);
				// refresh도 실패하면 access token 삭제
			} catch (error) {
				authStore.clearAccessToken();
				return Promise.reject(error);
			}
		}

		return Promise.reject(error);
	},
);

let refreshPromise = null;

export const initializeAuth = async () => {
	const authStore = useAuthStore();

	if (authStore.initialized) {
		return authStore.hasAccessToken;
	}

	if (!refreshPromise) {
		refreshPromise = publicApi
			.post("/auth/refresh")
			.then((response) => {
				authStore.setAccessToken(response.data.accessToken);
				return true;
			})
			.catch(() => {
				authStore.clearAccessToken();
				return false;
			})
			.finally(() => {
				authStore.setInitialized(true);
				refreshPromise = null;
			});
	}

	return refreshPromise;
};

import { useAuthStore } from "@/stores/auth";
import { normalizeApiText } from "@/utils/text";
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

const normalizeResponseData = (response) => {
	response.data = normalizeApiText(response.data);
	return response;
};

publicApi.interceptors.response.use(
	(response) => normalizeResponseData(response),
	(error) => Promise.reject(error),
);

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
	(response) => normalizeResponseData(response),
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
			} catch (error) {
				authStore.clearAccessToken();
				return Promise.reject(error);
			}
		}

		return Promise.reject(error);
	},
);

import { publicApi } from "@/api/client";
import { defineStore } from "pinia";
import { ref } from "vue";

export const useCertificationStore = defineStore("certification", () => {
	const certifications = ref([]);

	const getCertifications = () => {
		return [...certifications.value];
	};

	const initCertifications = async () => {
		try {
			const response = await publicApi.get("/certification");
			certifications.value = response.data;
		} catch (error) {
			certifications.value = [];
		}
	};

	return {
		getCertifications,
		initCertifications,
	};
});

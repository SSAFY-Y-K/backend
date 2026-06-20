import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import "./style.css";
import { createPinia } from "pinia";
import { useAuthStore } from "./stores/auth.js";
import { useCertificationStore } from "./stores/certification.js";

const app = createApp(App);

const pinia = createPinia();

app.use(pinia);

const authStore = useAuthStore(pinia);
authStore.initializeAccessToken();

const certificationStore = useCertificationStore();
certificationStore.initCertifications();

app.use(router);

app.mount("#app");

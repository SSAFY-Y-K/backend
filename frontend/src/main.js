import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import "./style.css";
import { createPinia } from "pinia";
import { useAuthStore } from "./stores/auth.js";
import { useCertificationStore } from "./stores/certification.js";
import Vue3Toastify from "vue3-toastify";
import "vue3-toastify/dist/index.css";

const app = createApp(App);

const pinia = createPinia();

app.use(pinia);

const authStore = useAuthStore(pinia);
await authStore.initializeAccessToken();

const certificationStore = useCertificationStore(pinia);
certificationStore.initCertifications();

app.use(router);

app.use(Vue3Toastify, {
  autoClose: 3_000,
  position: "top-center",
  closeOnClick: true,
  pauseOnHover: false,
});

app.mount("#app");

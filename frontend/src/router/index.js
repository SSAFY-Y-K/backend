import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import ProblemView from "@/views/ProblemView.vue";
import ProblemDetailView from "@/views/ProblemDetailView.vue";
import CodingProblemDetailView from "@/views/CodingProblemDetailView.vue";
import CreateView from "@/views/CreateView.vue";
import LoginView from "@/views/LoginView.vue";
import SignupView from "@/views/SignupView.vue";
import CommunityView from "@/views/CommunityView.vue";
import CommunityDetailView from "@/views/CommunityDetailView.vue";
import MypageView from "@/views/MypageView.vue";
import AdminReportView from "@/views/AdminReportView.vue";

const router = createRouter({
	history: createWebHistory(import.meta.env.BASE_URL),
	routes: [
		{
			path: "/",
			name: "home",
			component: HomeView,
		},
		{
			path: "/problem",
			name: "problem",
			component: ProblemView,
		},
		{
			path: "/problem/cert/:id",
			name: "problem-detail",
			component: ProblemDetailView,
		},
		{
			path: "/problem/coding/:id",
			name: "coding-problem-detail",
			component: CodingProblemDetailView,
			meta: { requiresAuth: true },
		},
		{
			path: "/create",
			name: "create",
			component: CreateView,
			meta: { requiresAuth: true },
		},
		{
			path: "/community",
			name: "community",
			component: CommunityView,
		},
		{
			path: "/community/:id",
			name: "community-detail",
			component: CommunityDetailView,
		},
		{
			path: "/mypage",
			name: "mypage",
			component: MypageView,
			meta: { hideSidebar: true, requiresAuth: true },
		},
		{
			path: "/admin/reports",
			name: "admin-reports",
			component: AdminReportView,
			meta: { requiresAuth: true, hideSidebar: true },
		},
		{
			path: "/login",
			name: "login",
			component: LoginView,
			meta: { hideSidebar: true },
		},
		{
			path: "/signup",
			name: "signup",
			component: SignupView,
			meta: { hideSidebar: true },
		},
	],
});

import { useAuthStore } from "@/stores/auth";

router.beforeEach((to) => {
	if (to.meta.requiresAuth) {
		const authStore = useAuthStore();
		if (!authStore.hasAccessToken) {
			return { name: "login", query: { redirect: to.fullPath } };
		}
	}
});

export default router;

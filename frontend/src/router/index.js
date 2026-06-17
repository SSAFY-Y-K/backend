import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import ProblemView from "@/views/ProblemView.vue";
import ProblemDetailView from "@/views/ProblemDetailView.vue";
import CreateView from "@/views/CreateView.vue";
import LoginView from "@/views/LoginView.vue";
import SignupView from "@/views/SignupView.vue";
import CommunityView from "@/views/CommunityView.vue";
import MypageView from "@/views/MypageView.vue";

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
			path: "/problem/:id",
			name: "problem-detail",
			component: ProblemDetailView,
		},
		{
			path: "/create",
			name: "create",
			component: CreateView,
		},
		{
			path: "/community",
			name: "community",
			component: CommunityView,
		},
		{
			path: "/mypage",
			name: "mypage",
			component: MypageView,
			meta: { hideSidebar: true },
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

export default router;

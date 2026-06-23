<template>
	<div class="flex h-screen flex-col overflow-hidden">
		<NavBar />
		<div class="flex flex-1 overflow-hidden">
			<SideBar v-if="!route.meta.hideSidebar" />
			<main class="flex-1 overflow-y-auto bg-gray-50">
				<RouterView v-slot="{ Component }">
					<Transition name="page" mode="out-in">
						<component :is="Component" />
					</Transition>
				</RouterView>
			</main>
		</div>
	</div>
</template>

<script setup>
import { useRoute } from "vue-router";
import NavBar from "./components/NavBar.vue";
import SideBar from "./components/SideBar.vue";

const route = useRoute();
</script>

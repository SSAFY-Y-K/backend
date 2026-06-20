<template>
	<div class="min-h-screen bg-slate-50 px-6 py-8">
		<div class="mx-auto max-w-4xl">
			<div class="mb-6 flex items-center justify-between">
				<h1 class="text-lg font-bold text-slate-800">오류 신고 관리</h1>
				<div class="flex items-center gap-3">
					<select
						v-model="statusFilter"
						class="h-8 rounded border border-slate-300 bg-white px-2 text-xs text-slate-600 outline-none"
					>
						<option value="ALL">전체</option>
						<option value="PENDING">대기중</option>
						<option value="RESOLVED">해결됨</option>
					</select>
					<button
						class="h-8 rounded bg-slate-200 px-3 text-xs text-slate-600 transition hover:bg-slate-300"
						@click="load"
					>
						새로고침
					</button>
				</div>
			</div>

			<div v-if="loading" class="py-20 text-center text-sm text-slate-400">불러오는 중...</div>

			<div v-else-if="filtered.length === 0" class="py-20 text-center text-sm text-slate-400">
				신고 없음
			</div>

			<div v-else class="space-y-3">
				<div
					v-for="r in filtered"
					:key="r.reportId"
					class="rounded-xl border bg-white p-4 shadow-sm"
					:class="r.status === 'RESOLVED' ? 'border-slate-200 opacity-60' : 'border-orange-200'"
				>
					<div class="mb-2 flex items-start justify-between gap-4">
						<div>
							<button
								class="text-sm font-semibold text-blue-600 hover:underline"
								@click="goToProblem(r.problemId)"
							>
								{{ r.problemTitle }}
							</button>
							<span class="ml-2 text-xs text-slate-400">by {{ r.nickname }}</span>
							<span class="ml-2 text-[10px] text-slate-400">{{ formatDate(r.createdAt) }}</span>
						</div>
						<div class="flex shrink-0 items-center gap-2">
							<span :class="['text-xs font-bold', r.status === 'RESOLVED' ? 'text-emerald-500' : 'text-orange-500']">
								{{ r.status === 'RESOLVED' ? '해결됨' : '대기중' }}
							</span>
							<button
								v-if="r.status === 'PENDING'"
								class="h-6 rounded bg-emerald-500 px-2 text-[10px] font-semibold text-white transition hover:bg-emerald-600"
								@click="handleResolve(r.reportId)"
							>
								해결 처리
							</button>
							<button
								class="h-6 rounded bg-blue-500 px-2 text-[10px] font-semibold text-white transition hover:bg-blue-600"
								@click="goToProblem(r.problemId)"
							>
								문제로 이동
							</button>
						</div>
					</div>
					<p class="whitespace-pre-wrap rounded-lg bg-slate-50 px-3 py-2 text-sm text-slate-700">{{ r.content }}</p>
				</div>
			</div>
		</div>
	</div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import { useRouter } from "vue-router";
import { getAllReports, resolveReport } from "@/api/index.js";
import { useAuthStore } from "@/stores/auth";

const router = useRouter();
const authStore = useAuthStore();

const reports = ref([]);
const loading = ref(true);
const statusFilter = ref("ALL");

const filtered = computed(() => {
	if (statusFilter.value === "ALL") return reports.value;
	return reports.value.filter(r => r.status === statusFilter.value);
});

const load = async () => {
	loading.value = true;
	try {
		const res = await getAllReports();
		reports.value = res.data.data;
	} finally {
		loading.value = false;
	}
};

const handleResolve = async (reportId) => {
	await resolveReport(reportId);
	reports.value = reports.value.map(r =>
		r.reportId === reportId ? { ...r, status: "RESOLVED" } : r
	);
};

const goToProblem = (problemId) => {
	router.push({ name: "coding-problem-detail", params: { id: problemId } });
};

const formatDate = (dateStr) => {
	if (!dateStr) return "";
	const d = new Date(dateStr);
	return `${d.getFullYear()}-${String(d.getMonth()+1).padStart(2,"0")}-${String(d.getDate()).padStart(2,"0")} ${String(d.getHours()).padStart(2,"0")}:${String(d.getMinutes()).padStart(2,"0")}`;
};

onMounted(() => {
	if (!authStore.isAdmin) {
		router.replace({ name: "home" });
		return;
	}
	load();
});
</script>

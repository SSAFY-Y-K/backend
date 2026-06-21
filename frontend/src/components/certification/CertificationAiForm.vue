<template>
	<div class="mb-4">
		<label class="mb-1 block text-xs font-medium text-slate-500"
			>참고 자료 (선택)</label
		>
		<textarea
			v-model="referenceText"
			rows="4"
			placeholder="문제 출제 범위나 기준 자료를 입력하세요."
			class="w-full resize-none rounded-md border border-slate-200 bg-slate-50 px-3 py-2 text-xs text-slate-700 outline-none transition placeholder:text-slate-300 focus:border-blue-400 focus:bg-white focus:ring-1 focus:ring-blue-100"
		></textarea>
	</div>
</template>

<script setup>
import { computed } from "vue";

const props = defineProps({
	modelValue: {
		type: String,
		default: "",
	},
	errorField: {
		type: Object,
		default: () => ({}),
	},
});

const emit = defineEmits(["update:modelValue"]);

const referenceText = computed({
	get: () => props.modelValue,
	set: (value) => emit("update:modelValue", value),
});
</script>

<template>
	<div class="space-y-4">
		<div>
			<!-- 문제 제목 인풋 -->
			<label class="mb-1 block text-xs font-medium text-slate-500"
				>문제 제목</label
			>
			<input
				v-model="title"
				type="text"
				placeholder="문제 제목을 입력하세요"
				class="h-9 w-full rounded-md border bg-slate-50 px-3 text-xs text-slate-700 outline-none transition placeholder:text-slate-300 focus:bg-white focus:ring-1 focus:ring-blue-100"
				:class="
					errorField.title
						? 'border-red-500'
						: ['border-slate-200', 'focus:border-blue-400']
				"
			/>
		</div>

		<div>
			<label class="mb-1 block text-xs font-medium text-slate-500">문제</label>
			<!-- 문제 내용 인풋 -->
			<textarea
				v-model="question"
				rows="5"
				placeholder="문제를 입력하세요"
				class="w-full resize-none rounded-md border bg-white px-3 py-2 text-xs text-slate-700 outline-none transition placeholder:text-slate-300 focus:ring-1 focus:ring-blue-100"
				:class="
					errorField.question
						? 'border-red-500'
						: ['border-slate-200', 'focus:border-blue-400']
				"
			></textarea>
		</div>

		<!-- 객관식 선택지 인풋 -->
		<template v-if="isMultipleChoice">
			<div class="space-y-2">
				<label class="mb-1 block text-xs font-medium text-slate-500"
					>선택지</label
				>
				<input
					v-model="choice1Content"
					type="text"
					placeholder="1번 선택지"
					class="h-9 w-full rounded-md border bg-slate-50 px-3 text-xs text-slate-700 outline-none transition placeholder:text-slate-300 focus:bg-white focus:ring-1 focus:ring-blue-100"
					:class="
						errorField.choice1Content
							? 'border-red-500'
							: ['border-slate-200', 'focus:border-blue-400']
					"
				/>
				<input
					v-model="choice2Content"
					type="text"
					placeholder="2번 선택지"
					class="h-9 w-full rounded-md border bg-slate-50 px-3 text-xs text-slate-700 outline-none transition placeholder:text-slate-300 focus:bg-white focus:ring-1 focus:ring-blue-100"
					:class="
						errorField.choice2Content
							? 'border-red-500'
							: ['border-slate-200', 'focus:border-blue-400']
					"
				/>
				<input
					v-model="choice3Content"
					type="text"
					placeholder="3번 선택지"
					class="h-9 w-full rounded-md border bg-slate-50 px-3 text-xs text-slate-700 outline-none transition placeholder:text-slate-300 focus:bg-white focus:ring-1 focus:ring-blue-100"
					:class="
						errorField.choice3Content
							? 'border-red-500'
							: ['border-slate-200', 'focus:border-blue-400']
					"
				/>
				<input
					v-model="choice4Content"
					type="text"
					placeholder="4번 선택지"
					class="h-9 w-full rounded-md border bg-slate-50 px-3 text-xs text-slate-700 outline-none transition placeholder:text-slate-300 focus:bg-white focus:ring-1 focus:ring-blue-100"
					:class="
						errorField.choice4Content
							? 'border-red-500'
							: ['border-slate-200', 'focus:border-blue-400']
					"
				/>
			</div>

			<div>
				<!-- 객관식 정답 선택 인풋 -->
				<label class="mb-1 block text-xs font-medium text-slate-500"
					>정답 선택지 번호</label
				>
				<select
					v-model.number="answerNumber"
					class="h-9 w-full rounded-md border bg-slate-50 px-3 text-xs text-slate-700 outline-none transition focus:bg-white"
					:class="
						errorField.answerNumber
							? 'border-red-500'
							: ['border-slate-200', 'focus:border-blue-400']
					"
				>
					<option value="" selected disabled>정답 선택</option>
					<option :value="1">1번</option>
					<option :value="2">2번</option>
					<option :value="3">3번</option>
					<option :value="4">4번</option>
				</select>
			</div>
		</template>

		<!-- 주관식 정답 인풋 -->
		<div v-else>
			<label class="mb-1 block text-xs font-medium text-slate-500">정답</label>
			<input
				v-model="answer"
				type="text"
				placeholder="정답을 입력하세요"
				class="h-9 w-full rounded-md border bg-slate-50 px-3 text-xs text-slate-700 outline-none transition placeholder:text-slate-300 focus:bg-white focus:ring-1 focus:ring-blue-100"
				:class="
					errorField.answer
						? 'border-red-500'
						: ['border-slate-200', 'focus:border-blue-400']
				"
			/>
		</div>
	</div>
</template>

<script setup>
import { computed } from "vue";

const props = defineProps({
	isMultipleChoice: {
		type: Boolean,
		required: true,
	},
	title: {
		type: String,
		default: "",
	},
	question: {
		type: String,
		default: "",
	},
	choice1Content: {
		type: String,
		default: "",
	},
	choice2Content: {
		type: String,
		default: "",
	},
	choice3Content: {
		type: String,
		default: "",
	},
	choice4Content: {
		type: String,
		default: "",
	},
	answerNumber: {
		type: [Number, String],
		default: "",
	},
	answer: {
		type: String,
		default: "",
	},
	errorField: {
		type: Object,
		default: () => ({}),
	},
});

const emit = defineEmits([
	"update:title",
	"update:question",
	"update:choice1Content",
	"update:choice2Content",
	"update:choice3Content",
	"update:choice4Content",
	"update:answerNumber",
	"update:answer",
]);

const bindModel = (propName, eventName) =>
	computed({
		get: () => props[propName],
		set: (value) => emit(eventName, value),
	});

const title = bindModel("title", "update:title");
const question = bindModel("question", "update:question");
const choice1Content = bindModel("choice1Content", "update:choice1Content");
const choice2Content = bindModel("choice2Content", "update:choice2Content");
const choice3Content = bindModel("choice3Content", "update:choice3Content");
const choice4Content = bindModel("choice4Content", "update:choice4Content");
const answerNumber = bindModel("answerNumber", "update:answerNumber");
const answer = bindModel("answer", "update:answer");
</script>

<template>
	<el-form size="small">
		<el-form-item>
			<el-radio v-model="radioValue" :label="1"> 分钟，允许的通配符[, - * /] </el-radio>
		</el-form-item>

		<el-form-item>
			<el-radio v-model="radioValue" :label="2">
				周期从
				<el-input-number v-model="cycle01" :min="0" :max="58" /> -
				<el-input-number v-model="cycle02" :min="cycle01 ? cycle01 + 1 : 1" :max="59" />
				分钟
			</el-radio>
		</el-form-item>

		<el-form-item>
			<el-radio v-model="radioValue" :label="3">
				从
				<el-input-number v-model="average01" :min="0" :max="58" /> 分钟开始，每
				<el-input-number v-model="average02" :min="1" :max="59 - average01 || 0" />
				分钟执行一次
			</el-radio>
		</el-form-item>

		<el-form-item>
			<el-radio v-model="radioValue" :label="4">
				指定
				<el-select
					clearable
					v-model="checkboxList"
					placeholder="可多选"
					multiple
					style="width: 100%"
				>
					<el-option v-for="item in 60" :key="item" :value="item - 1">{{
						item - 1
					}}</el-option>
				</el-select>
			</el-radio>
		</el-form-item>
	</el-form>
</template>

<script setup lang="ts" name="crontab-min">
import { ref, computed, watch } from "vue";
import type { PropType } from "vue";

// 定义props类型
interface CronItem {
	min?: string;
}

const props = defineProps({
	check: {
		type: Function as PropType<
			(value: number, min: number, max: number, ...args: any[]) => number
		>,
		required: true,
	},
	cron: {
		type: Object as PropType<CronItem>,
		required: true,
	},
});

const emit = defineEmits(["update"]);

// 响应式变量
const radioValue = ref(1);
const cycle01 = ref(1);
const cycle02 = ref(2);
const average01 = ref(0);
const average02 = ref(1);
const checkboxList = ref<number[]>([]);

// 计算属性
const cycleTotal = computed(() => {
	const c1 = props.check(cycle01.value, 0, 58);
	const c2 = props.check(cycle02.value, c1 ? c1 + 1 : 1, 59);
	return `${c1}-${c2}`;
});

const averageTotal = computed(() => {
	const a1 = props.check(average01.value, 0, 58);
	const a2 = props.check(average02.value, 1, 59 - a1 || 0);
	return `${a1}/${a2}`;
});

const checkboxString = computed(() => {
	return checkboxList.value.length > 0 ? checkboxList.value.join(",") : "*";
});

// 单选按钮变化处理
const radioChange = () => {
	switch (radioValue.value) {
		case 1:
			emit("update", "min", "*");
			break;
		case 2:
			emit("update", "min", cycleTotal.value);
			break;
		case 3:
			emit("update", "min", averageTotal.value);
			break;
		case 4:
			emit("update", "min", checkboxString.value);
			break;
	}
};

// 监听变量变化
watch(radioValue, radioChange);
watch([cycle01, cycle02], () => {
	if (radioValue.value === 2) {
		emit("update", "min", cycleTotal.value);
	}
});
watch([average01, average02], () => {
	if (radioValue.value === 3) {
		emit("update", "min", averageTotal.value);
	}
});
watch(
	checkboxList,
	() => {
		if (radioValue.value === 4) {
			emit("update", "min", checkboxString.value);
		}
	},
	{ deep: true }
);
</script>

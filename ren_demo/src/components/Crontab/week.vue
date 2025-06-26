<template>
	<el-form size="small">
		<el-form-item>
			<el-radio v-model="radioValue" :label="1"> 周，允许的通配符[, - * ? / L #] </el-radio>
		</el-form-item>

		<el-form-item>
			<el-radio v-model="radioValue" :label="2"> 不指定 </el-radio>
		</el-form-item>

		<el-form-item>
			<el-radio v-model="radioValue" :label="3">
				周期从星期
				<el-select clearable v-model="cycle01">
					<el-option
						v-for="item in weekList"
						:key="item.key"
						:label="item.value"
						:value="item.key"
						:disabled="item.key === 1"
						>{{ item.value }}</el-option
					>
				</el-select>
				-
				<el-select clearable v-model="cycle02">
					<el-option
						v-for="item in weekList"
						:key="item.key"
						:label="item.value"
						:value="item.key"
						:disabled="item.key < cycle01 && item.key !== 1"
						>{{ item.value }}</el-option
					>
				</el-select>
			</el-radio>
		</el-form-item>

		<el-form-item>
			<el-radio v-model="radioValue" :label="4">
				第
				<el-input-number v-model="average01" :min="1" :max="4" /> 周的星期
				<el-select clearable v-model="average02">
					<el-option
						v-for="item in weekList"
						:key="item.key"
						:label="item.value"
						:value="item.key"
						>{{ item.value }}</el-option
					>
				</el-select>
			</el-radio>
		</el-form-item>

		<el-form-item>
			<el-radio v-model="radioValue" :label="5">
				本月最后一个星期
				<el-select clearable v-model="weekday">
					<el-option
						v-for="item in weekList"
						:key="item.key"
						:label="item.value"
						:value="item.key"
						>{{ item.value }}</el-option
					>
				</el-select>
			</el-radio>
		</el-form-item>

		<el-form-item>
			<el-radio v-model="radioValue" :label="6">
				指定
				<el-select
					clearable
					v-model="checkboxList"
					placeholder="可多选"
					multiple
					style="width: 100%"
				>
					<el-option
						v-for="item in weekList"
						:key="item.key"
						:label="item.value"
						:value="String(item.key)"
						>{{ item.value }}</el-option
					>
				</el-select>
			</el-radio>
		</el-form-item>
	</el-form>
</template>

<script setup lang="ts" name="crontab-week">
import { ref, computed, watch } from "vue";
import type { PropType } from "vue";

// 定义星期类型
interface WeekItem {
	key: number;
	value: string;
}

// 定义组件属性类型
interface Props {
	check: (value: number, min: number, max: number) => number;
	cron: {
		day?: string;
	};
}

const props = defineProps<Props>();
const emit = defineEmits(["update"]);

// 响应式变量
const radioValue = ref(2);
const weekday = ref(2);
const cycle01 = ref(2);
const cycle02 = ref(3);
const average01 = ref(1);
const average02 = ref(2);
const checkboxList = ref<string[]>([]);

// 星期列表
const weekList: WeekItem[] = [
	{ key: 2, value: "星期一" },
	{ key: 3, value: "星期二" },
	{ key: 4, value: "星期三" },
	{ key: 5, value: "星期四" },
	{ key: 6, value: "星期五" },
	{ key: 7, value: "星期六" },
	{ key: 1, value: "星期日" },
];

// 计算属性
const cycleTotal = computed(() => {
	const c1 = props.check(cycle01.value, 1, 7);
	const c2 = props.check(cycle02.value, 1, 7);
	return `${c1}-${c2}`;
});

const averageTotal = computed(() => {
	const a1 = props.check(average01.value, 1, 4);
	const a2 = props.check(average02.value, 1, 7);
	return `${a2}#${a1}`;
});

const weekdayCheck = computed(() => {
	return props.check(weekday.value, 1, 7);
});

const checkboxString = computed(() => {
	return checkboxList.value.length > 0 ? checkboxList.value.join(",") : "*";
});

// 更新触发器
const triggerUpdate = () => {
	// 当选择非"不指定"模式时，设置日字段为'?'
	if (radioValue.value !== 2 && props.cron.day !== "?") {
		emit("update", "day", "?");
	}

	switch (radioValue.value) {
		case 1:
			emit("update", "week", "*");
			break;
		case 2:
			emit("update", "week", "?");
			break;
		case 3:
			emit("update", "week", cycleTotal.value);
			break;
		case 4:
			emit("update", "week", averageTotal.value);
			break;
		case 5:
			emit("update", "week", `${weekdayCheck.value}L`);
			break;
		case 6:
			emit("update", "week", checkboxString.value);
			break;
	}
};

// 监听相关数据变化
watch(radioValue, triggerUpdate);
watch(cycleTotal, () => {
	if (radioValue.value === 3) {
		triggerUpdate();
	}
});
watch(averageTotal, () => {
	if (radioValue.value === 4) {
		triggerUpdate();
	}
});
watch(weekdayCheck, () => {
	if (radioValue.value === 5) {
		triggerUpdate();
	}
});
watch(checkboxString, () => {
	if (radioValue.value === 6) {
		triggerUpdate();
	}
});
</script>

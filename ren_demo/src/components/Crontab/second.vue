<template>
	<el-form size="small">
		<el-form-item>
			<el-radio v-model="radioValue" :label="1"> 秒，允许的通配符[, - * /] </el-radio>
		</el-form-item>

		<el-form-item>
			<el-radio v-model="radioValue" :label="2">
				周期从
				<el-input-number v-model="cycle01" :min="0" :max="58" /> -
				<el-input-number v-model="cycle02" :min="cycle01 ? cycle01 + 1 : 1" :max="59" /> 秒
			</el-radio>
		</el-form-item>

		<el-form-item>
			<el-radio v-model="radioValue" :label="3">
				从
				<el-input-number v-model="average01" :min="0" :max="58" /> 秒开始，每
				<el-input-number v-model="average02" :min="1" :max="59 - average01 || 0" />
				秒执行一次
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

<script setup lang="ts" name="crontab-second">
import { ref, computed, watch } from "vue";
import type { PropType } from "vue";

// 定义组件属性类型
interface Props {
	check: (value: number, min: number, max: number) => number;
	radioParent?: number;
}

const props = defineProps<Props>();
const emit = defineEmits(["update"]);

// 响应式变量
const radioValue = ref<number>(props.radioParent ?? 1);
const cycle01 = ref<number>(1);
const cycle02 = ref<number>(2);
const average01 = ref<number>(0);
const average02 = ref<number>(1);
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

// 更新触发器
const triggerUpdate = () => {
	switch (radioValue.value) {
		case 1:
			emit("update", "second", "*");
			break;
		case 2:
			emit("update", "second", cycleTotal.value);
			break;
		case 3:
			emit("update", "second", averageTotal.value);
			break;
		case 4:
			emit("update", "second", checkboxString.value);
			break;
	}
};

// 监听相关数据变化
watch(radioValue, triggerUpdate);
watch(cycleTotal, () => {
	if (radioValue.value === 2) {
		triggerUpdate();
	}
});
watch(averageTotal, () => {
	if (radioValue.value === 3) {
		triggerUpdate();
	}
});
watch(checkboxString, () => {
	if (radioValue.value === 4) {
		triggerUpdate();
	}
});
watch(
	() => props.radioParent,
	(newValue) => {
		if (newValue !== undefined) {
			radioValue.value = newValue;
		}
	}
);
</script>

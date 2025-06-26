<template>
	<el-form size="small">
		<el-form-item>
			<el-radio v-model="radioValue" :label="1"> 不填，允许的通配符[, - * /] </el-radio>
		</el-form-item>

		<el-form-item>
			<el-radio v-model="radioValue" :label="2"> 每年 </el-radio>
		</el-form-item>

		<el-form-item>
			<el-radio v-model="radioValue" :label="3">
				周期从
				<el-input-number v-model="cycle01" :min="fullYear" :max="2098" /> -
				<el-input-number
					v-model="cycle02"
					:min="cycle01 ? cycle01 + 1 : fullYear + 1"
					:max="2099"
				/>
			</el-radio>
		</el-form-item>

		<el-form-item>
			<el-radio v-model="radioValue" :label="4">
				从
				<el-input-number v-model="average01" :min="fullYear" :max="2098" /> 年开始，每
				<el-input-number v-model="average02" :min="1" :max="2099 - average01 || fullYear" />
				年执行一次
			</el-radio>
		</el-form-item>

		<el-form-item>
			<el-radio v-model="radioValue" :label="5">
				指定
				<el-select clearable v-model="checkboxList" placeholder="可多选" multiple>
					<el-option
						v-for="item in 9"
						:key="item"
						:value="item - 1 + fullYear"
						:label="(item - 1 + fullYear).toString()"
					/>
				</el-select>
			</el-radio>
		</el-form-item>
	</el-form>
</template>

<script setup lang="ts" name="crontab-year">
import { ref, computed, watch, onMounted } from "vue";
import type { PropType } from "vue";

// 定义组件属性类型
interface Props {
	check: (value: number, min: number, max: number) => number;
	radioParent?: number;
}

const props = defineProps<Props>();
const emit = defineEmits(["update"]);

// 当前年份
const fullYear = ref(new Date().getFullYear());

// 响应式变量
const radioValue = ref<number>(props.radioParent ?? 1);
const cycle01 = ref<number>(fullYear.value);
const cycle02 = ref<number>(fullYear.value + 1);
const average01 = ref<number>(fullYear.value);
const average02 = ref<number>(1);
const checkboxList = ref<number[]>([]);

// 计算属性
const cycleTotal = computed(() => {
	const c1 = props.check(cycle01.value, fullYear.value, 2098);
	const c2 = props.check(cycle02.value, c1 ? c1 + 1 : fullYear.value + 1, 2099);
	return `${c1}-${c2}`;
});

const averageTotal = computed(() => {
	const a1 = props.check(average01.value, fullYear.value, 2098);
	const a2 = props.check(average02.value, 1, Math.max(2099 - a1, fullYear.value));
	return `${a1}/${a2}`;
});

const checkboxString = computed(() => {
	return checkboxList.value.join(",") || "";
});

// 更新触发器
const triggerUpdate = () => {
	switch (radioValue.value) {
		case 1:
			emit("update", "year", "");
			break;
		case 2:
			emit("update", "year", "*");
			break;
		case 3:
			emit("update", "year", cycleTotal.value);
			break;
		case 4:
			emit("update", "year", averageTotal.value);
			break;
		case 5:
			emit("update", "year", checkboxString.value);
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
watch(checkboxString, () => {
	if (radioValue.value === 5) {
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

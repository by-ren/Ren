<template>
	<div>
		<el-tabs type="border-card">
			<el-tab-pane label="秒" v-if="shouldHide('second')">
				<CrontabSecond
					@update="updateCrontabValue"
					:check="checkNumber"
					:cron="crontabValueObj"
					ref="cronsecond"
				/>
			</el-tab-pane>

			<el-tab-pane label="分钟" v-if="shouldHide('min')">
				<CrontabMin
					@update="updateCrontabValue"
					:check="checkNumber"
					:cron="crontabValueObj"
					ref="cronmin"
				/>
			</el-tab-pane>

			<el-tab-pane label="小时" v-if="shouldHide('hour')">
				<CrontabHour
					@update="updateCrontabValue"
					:check="checkNumber"
					:cron="crontabValueObj"
					ref="cronhour"
				/>
			</el-tab-pane>

			<el-tab-pane label="日" v-if="shouldHide('day')">
				<CrontabDay
					@update="updateCrontabValue"
					:check="checkNumber"
					:cron="crontabValueObj"
					ref="cronday"
				/>
			</el-tab-pane>

			<el-tab-pane label="月" v-if="shouldHide('month')">
				<CrontabMonth
					@update="updateCrontabValue"
					:check="checkNumber"
					:cron="crontabValueObj"
					ref="cronmonth"
				/>
			</el-tab-pane>

			<el-tab-pane label="周" v-if="shouldHide('week')">
				<CrontabWeek
					@update="updateCrontabValue"
					:check="checkNumber"
					:cron="crontabValueObj"
					ref="cronweek"
				/>
			</el-tab-pane>

			<el-tab-pane label="年" v-if="shouldHide('year')">
				<CrontabYear
					@update="updateCrontabValue"
					:check="checkNumber"
					:cron="crontabValueObj"
					ref="cronyear"
				/>
			</el-tab-pane>
		</el-tabs>

		<div class="popup-main">
			<div class="popup-result">
				<p class="title">时间表达式</p>
				<table>
					<thead>
						<th v-for="item in tabTitles" :key="item" width="40">{{ item }}</th>
						<th>Cron 表达式</th>
					</thead>
					<tbody>
						<td>
							<span>{{ crontabValueObj.second }}</span>
						</td>
						<td>
							<span>{{ crontabValueObj.min }}</span>
						</td>
						<td>
							<span>{{ crontabValueObj.hour }}</span>
						</td>
						<td>
							<span>{{ crontabValueObj.day }}</span>
						</td>
						<td>
							<span>{{ crontabValueObj.month }}</span>
						</td>
						<td>
							<span>{{ crontabValueObj.week }}</span>
						</td>
						<td>
							<span>{{ crontabValueObj.year }}</span>
						</td>
						<td>
							<span>{{ crontabValueString }}</span>
						</td>
					</tbody>
				</table>
			</div>
			<CrontabResult :ex="crontabValueString" />

			<div class="pop_btn">
				<el-button size="small" type="primary" @click="submitFill"> 确定 </el-button>
				<el-button size="small" type="warning" @click="clearCron"> 重置 </el-button>
				<el-button size="small" @click="hidePopup">取消</el-button>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts" name="vcrontab">
import { ref, computed, watch, onMounted, type ComponentPublicInstance } from "vue";
import { ElButton, ElTabs, ElTabPane } from "element-plus";
import type { PropType } from "vue";

// 导入子组件
import CrontabSecond from "./second.vue";
import CrontabMin from "./min.vue";
import CrontabHour from "./hour.vue";
import CrontabDay from "./day.vue";
import CrontabMonth from "./month.vue";
import CrontabWeek from "./week.vue";
import CrontabYear from "./year.vue";
import CrontabResult from "./result.vue";

// 定义类型
interface CrontabValue {
	second: string;
	min: string;
	hour: string;
	day: string;
	month: string;
	week: string;
	year: string;
}

interface ChildComponent {
	radioValue: number;
	cycle01: number;
	cycle02: number;
	average01: number;
	average02: number;
	workday: number;
	checkboxList: number[];
}

const props = defineProps({
	expression: {
		type: String,
		default: "",
	},
	hideComponent: {
		type: Array as PropType<string[]>,
		default: () => [],
	},
});

const emit = defineEmits(["update", "fill", "hide"]);

// 响应式数据
const crontabValueObj = ref<CrontabValue>({
	second: "*",
	min: "*",
	hour: "*",
	day: "*",
	month: "*",
	week: "?",
	year: "",
});

// 子组件引用
const cronsecond = ref<ComponentPublicInstance & ChildComponent>();
const cronmin = ref<ComponentPublicInstance & ChildComponent>();
const cronhour = ref<ComponentPublicInstance & ChildComponent>();
const cronday = ref<ComponentPublicInstance & ChildComponent>();
const cronmonth = ref<ComponentPublicInstance & ChildComponent>();
const cronweek = ref<ComponentPublicInstance & ChildComponent>();
const cronyear = ref<ComponentPublicInstance & ChildComponent>();

// 表头
const tabTitles = ref(["秒", "分钟", "小时", "日", "月", "周", "年"]);

// 完整的cron表达式
const crontabValueString = computed(() => {
	const { second, min, hour, day, month, week, year } = crontabValueObj.value;
	return `${second} ${min} ${hour} ${day} ${month} ${week} ${year}`.trim();
});

// 检查组件是否需要隐藏
const shouldHide = computed(() => (key: string) => {
	return !props.hideComponent.includes(key);
});

// 数字边界检查
const checkNumber = (value: number, min: number, max: number, ...args: any[]): number => {
	value = Number.isNaN(value) ? min : Number(value);
	value = Math.min(max, value);
	value = Math.max(min, value);
	return value;
};

// 更新cron值
const updateCrontabValue = (name: keyof CrontabValue, value: string) => {
	crontabValueObj.value[name] = value;
};

// 重置cron表达式
const clearCron = () => {
	crontabValueObj.value = {
		second: "*",
		min: "*",
		hour: "*",
		day: "*",
		month: "*",
		week: "?",
		year: "",
	};

	// 重置子组件状态
	if (cronsecond.value) cronsecond.value.radioValue = 1;
	if (cronmin.value) cronmin.value.radioValue = 1;
	if (cronhour.value) cronhour.value.radioValue = 1;
	if (cronday.value) cronday.value.radioValue = 1;
	if (cronmonth.value) cronmonth.value.radioValue = 1;
	if (cronweek.value) cronweek.value.radioValue = 1;
	if (cronyear.value) cronyear.value.radioValue = 1;
};

// 提交填充
const submitFill = () => {
	emit("fill", crontabValueString.value);
	hidePopup();
};

// 隐藏弹窗
const hidePopup = () => {
	emit("hide");
};

// 解析传入的cron表达式
const resolveExpression = () => {
	if (!props.expression) return;

	const values = props.expression.split(" ");
	if (values.length >= 6) {
		crontabValueObj.value = {
			second: values[0],
			min: values[1],
			hour: values[2],
			day: values[3],
			month: values[4],
			week: values[5],
			year: values.length > 6 ? values[6] : "",
		};
	}
};

// 监听表达式变化
watch(() => props.expression, resolveExpression, { immediate: true });
</script>

<style scoped>
.pop_btn {
	text-align: center;
	margin-top: 20px;
}
.popup-main {
	position: relative;
	margin: 10px auto;
	background: #fff;
	border-radius: 5px;
	font-size: 12px;
	overflow: hidden;
}
.popup-title {
	overflow: hidden;
	line-height: 34px;
	padding-top: 6px;
	background: #f2f2f2;
}
.popup-result {
	box-sizing: border-box;
	line-height: 24px;
	margin: 25px auto;
	padding: 15px 10px 10px;
	border: 1px solid #ccc;
	position: relative;
}
.popup-result .title {
	position: absolute;
	top: -28px;
	left: 50%;
	width: 140px;
	font-size: 14px;
	margin-left: -70px;
	text-align: center;
	line-height: 30px;
	background: #fff;
}
.popup-result table {
	text-align: center;
	width: 100%;
	margin: 0 auto;
}
.popup-result table span {
	display: block;
	width: 100%;
	font-family: arial;
	line-height: 30px;
	height: 30px;
	white-space: nowrap;
	overflow: hidden;
	border: 1px solid #e8e8e8;
}
.popup-result-scroll {
	font-size: 12px;
	line-height: 24px;
	height: 10em;
	overflow-y: auto;
}
</style>

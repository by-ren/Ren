<template>
	<div class="popup-result">
		<p class="title">最近5次运行时间</p>
		<ul class="popup-result-scroll">
			<template v-if="isShow">
				<li v-for="item in resultList" :key="item">{{ item }}</li>
			</template>
			<li v-else>计算结果中...</li>
		</ul>
	</div>
</template>

<script setup lang="ts" name="crontab-result">
import { ref, watch, onMounted } from "vue";

interface Props {
	ex: string;
}

const props = defineProps<Props>();

// 响应式数据
const dayRule = ref("");
const dayRuleSup = ref<number | number[] | string>("");
const dateArr = ref<number[][]>([]);
const resultList = ref<string[]>([]);
const isShow = ref(false);

// 日期数组索引常量
const SECOND_ARR_IDX = 0;
const MINUTE_ARR_IDX = 1;
const HOUR_ARR_IDX = 2;
const DAY_ARR_IDX = 3;
const MONTH_ARR_IDX = 4;
const YEAR_ARR_IDX = 5;

// 表达式值变化时，开始去计算结果
const expressionChange = () => {
	// 计算开始-隐藏结果
	isShow.value = false;
	// 获取规则数组[0秒、1分、2时、3日、4月、5星期、6年]
	const ruleArr = props.ex.split(" ");

	// 用于记录进入循环的次数
	let nums = 0;
	// 用于暂时存符号时间规则结果的数组
	const resultArr: string[] = [];
	// 获取当前时间精确至[年、月、日、时、分、秒]
	const nTime = new Date();
	let nYear = nTime.getFullYear();
	let nMonth = nTime.getMonth() + 1;
	let nDay = nTime.getDate();
	let nHour = nTime.getHours();
	let nMin = nTime.getMinutes();
	let nSecond = nTime.getSeconds();

	// 清空日期数组
	dateArr.value = [];

	// 根据规则获取到近100年可能年数组、月数组等等
	getSecondArr(ruleArr[0]);
	getMinArr(ruleArr[1]);
	getHourArr(ruleArr[2]);
	getDayArr(ruleArr[3]);
	getMonthArr(ruleArr[4]);
	getWeekArr(ruleArr[5]);
	getYearArr(ruleArr[6], nYear);

	// 将获取到的数组赋值-方便使用
	const sDate = dateArr.value[SECOND_ARR_IDX] || [];
	const mDate = dateArr.value[MINUTE_ARR_IDX] || [];
	const hDate = dateArr.value[HOUR_ARR_IDX] || [];
	const DDate = dateArr.value[DAY_ARR_IDX] || [];
	const MDate = dateArr.value[MONTH_ARR_IDX] || [];
	const YDate = dateArr.value[YEAR_ARR_IDX] || [];

	// 获取当前时间在数组中的索引
	let sIdx = getIndex(sDate, nSecond);
	let mIdx = getIndex(mDate, nMin);
	let hIdx = getIndex(hDate, nHour);
	let DIdx = getIndex(DDate, nDay);
	let MIdx = getIndex(MDate, nMonth);
	let YIdx = getIndex(YDate, nYear);

	// 重置月日时分秒的函数
	const resetSecond = () => {
		sIdx = 0;
		nSecond = sDate[sIdx] || 0;
	};

	const resetMin = () => {
		mIdx = 0;
		nMin = mDate[mIdx] || 0;
		resetSecond();
	};

	const resetHour = () => {
		hIdx = 0;
		nHour = hDate[hIdx] || 0;
		resetMin();
	};

	const resetDay = () => {
		DIdx = 0;
		nDay = DDate[DIdx] || 1;
		resetHour();
	};

	const resetMonth = () => {
		MIdx = 0;
		nMonth = MDate[MIdx] || 1;
		resetDay();
	};

	// 如果当前年份不为数组中当前值
	if (nYear !== YDate[YIdx]) {
		resetMonth();
	}

	// 如果当前月份不为数组中当前值
	if (nMonth !== MDate[MIdx]) {
		resetDay();
	}

	// 如果当前"日"不为数组中当前值
	if (nDay !== DDate[DIdx]) {
		resetHour();
	}

	// 如果当前"时"不为数组中当前值
	if (nHour !== hDate[hIdx]) {
		resetMin();
	}

	// 如果当前"分"不为数组中当前值
	if (nMin !== mDate[mIdx]) {
		resetSecond();
	}

	// 循环年份数组
	goYear: for (let Yi = YIdx; Yi < YDate.length; Yi++) {
		const YY = YDate[Yi];

		// 如果到达最大值时
		if (nMonth > MDate[MDate.length - 1]) {
			resetMonth();
			continue;
		}

		// 循环月份数组
		goMonth: for (let Mi = MIdx; Mi < MDate.length; Mi++) {
			// 赋值、方便后面运算
			let MM = MDate[Mi];
			const MMStr = MM < 10 ? `0${MM}` : MM.toString();

			// 如果到达最大值时
			if (nDay > DDate[DDate.length - 1]) {
				resetDay();
				if (Mi === MDate.length - 1) {
					resetMonth();
					continue goYear;
				}
				continue;
			}

			// 循环日期数组
			goDay: for (let Di = DIdx; Di < DDate.length; Di++) {
				// 赋值、方便后面运算
				let DD = DDate[Di];
				let thisDD = DD < 10 ? `0${DD}` : DD.toString();

				// 如果到达最大值时
				if (nHour > hDate[hDate.length - 1]) {
					resetHour();
					if (Di === DDate.length - 1) {
						resetDay();
						if (Mi === MDate.length - 1) {
							resetMonth();
							continue goYear;
						}
						continue goMonth;
					}
					continue;
				}

				// 判断日期的合法性，不合法的话跳出当前循环
				if (
					checkDate(`${YY}-${MMStr}-${thisDD} 00:00:00`) !== true &&
					dayRule.value !== "workDay" &&
					dayRule.value !== "lastWeek" &&
					dayRule.value !== "lastDay"
				) {
					resetDay();
					continue goMonth;
				}

				// 根据日期规则调整日期
				if (dayRule.value === "lastDay") {
					// 如果不是合法日期则需要调整到合法日期（月末最后一天）
					if (checkDate(`${YY}-${MMStr}-${thisDD} 00:00:00`) !== true) {
						while (DD > 0 && checkDate(`${YY}-${MMStr}-${thisDD} 00:00:00`) !== true) {
							DD--;
							thisDD = DD < 10 ? `0${DD}` : DD.toString();
						}
					}
				} else if (dayRule.value === "workDay") {
					// 校验并调整日期（工作日）
					if (checkDate(`${YY}-${MMStr}-${thisDD} 00:00:00`) !== true) {
						while (DD > 0 && checkDate(`${YY}-${MMStr}-${thisDD} 00:00:00`) !== true) {
							DD--;
							thisDD = DD < 10 ? `0${DD}` : DD.toString();
						}
					}

					// 获取达到条件的日期是星期几
					const thisWeek = formatDate(
						new Date(`${YY}-${MMStr}-${thisDD} 00:00:00`),
						"week"
					);

					// 当星期日时
					if (thisWeek === 1) {
						// 先找下一个工作日（周一）
						DD++;
						thisDD = DD < 10 ? `0${DD}` : DD.toString();

						// 判断下一日是否合法
						if (checkDate(`${YY}-${MMStr}-${thisDD} 00:00:00`) !== true) {
							DD -= 3;
						}
					} else if (thisWeek === 7) {
						// 当星期六时只需判断不是1号就可进行操作
						if (dayRuleSup.value !== 1) {
							DD--;
						} else {
							DD += 2;
						}
					}
				} else if (dayRule.value === "weekDay") {
					// 如果指定了是星期几
					// 获取当前日期是星期几
					const thisWeek = formatDate(
						new Date(`${YY}-${MMStr}-${DD} 00:00:00`),
						"week"
					) as number;

					// 校验当前星期是否在星期池中
					if (!Array.isArray(dayRuleSup.value) || !dayRuleSup.value.includes(thisWeek)) {
						// 如果到达最大值时
						if (Di === DDate.length - 1) {
							resetDay();
							if (Mi === MDate.length - 1) {
								resetMonth();
								continue goYear;
							}
							continue goMonth;
						}
						continue;
					}
				} else if (dayRule.value === "assWeek") {
					// 如果指定了是第几周的星期几
					// 获取每月1号是星期几
					if (Array.isArray(dayRuleSup.value)) {
						const [weekNum, weekDay] = dayRuleSup.value;
						const thisWeek = formatDate(
							new Date(`${YY}-${MMStr}-01 00:00:00`),
							"week"
						) as number;

						if (weekDay >= thisWeek) {
							DD = (weekNum - 1) * 7 + weekDay - thisWeek + 1;
						} else {
							DD = weekNum * 7 + weekDay - thisWeek + 1;
						}
					}
				} else if (dayRule.value === "lastWeek") {
					// 如果指定了每月最后一个星期几
					// 校验并调整日期
					if (checkDate(`${YY}-${MMStr}-${thisDD} 00:00:00`) !== true) {
						while (DD > 0 && checkDate(`${YY}-${MMStr}-${thisDD} 00:00:00`) !== true) {
							DD--;
							thisDD = DD < 10 ? `0${DD}` : DD.toString();
						}
					}

					// 获取月末最后一天是星期几
					const thisWeek = formatDate(
						new Date(`${YY}-${MMStr}-${thisDD} 00:00:00`),
						"week"
					) as number;

					// 调整到最近的指定星期几
					if (typeof dayRuleSup.value === "number") {
						const targetWeek = dayRuleSup.value;
						if (targetWeek < thisWeek) {
							DD -= thisWeek - targetWeek;
						} else if (targetWeek > thisWeek) {
							DD -= 7 - (targetWeek - thisWeek);
						}
					}
				}

				// 更新DD值
				thisDD = DD < 10 ? `0${DD}` : DD.toString();

				// 循环"时"数组
				goHour: for (let hi = hIdx; hi < hDate.length; hi++) {
					const hh = hDate[hi] < 10 ? `0${hDate[hi]}` : hDate[hi].toString();

					// 如果到达最大值时
					if (nMin > mDate[mDate.length - 1]) {
						resetMin();
						if (hi === hDate.length - 1) {
							resetHour();
							if (Di === DDate.length - 1) {
								resetDay();
								if (Mi === MDate.length - 1) {
									resetMonth();
									continue goYear;
								}
								continue goMonth;
							}
							continue goDay;
						}
						continue;
					}

					// 循环"分"数组
					goMin: for (let mi = mIdx; mi < mDate.length; mi++) {
						const mm = mDate[mi] < 10 ? `0${mDate[mi]}` : mDate[mi].toString();

						// 如果到达最大值时
						if (nSecond > sDate[sDate.length - 1]) {
							resetSecond();
							if (mi === mDate.length - 1) {
								resetMin();
								if (hi === hDate.length - 1) {
									resetHour();
									if (Di === DDate.length - 1) {
										resetDay();
										if (Mi === MDate.length - 1) {
											resetMonth();
											continue goYear;
										}
										continue goMonth;
									}
									continue goDay;
								}
								continue goHour;
							}
							continue;
						}

						// 循环"秒"数组
						goSecond: for (let si = sIdx; si <= sDate.length - 1; si++) {
							const ss = sDate[si] < 10 ? `0${sDate[si]}` : sDate[si].toString();

							// 添加当前时间（时间合法性在日期循环时已经判断）
							if (MMStr !== "00" && thisDD !== "00") {
								resultArr.push(`${YY}-${MMStr}-${thisDD} ${hh}:${mm}:${ss}`);
								nums++;
							}

							// 如果条数满了就退出循环
							if (nums === 5) break goYear;

							// 如果到达最大值时
							if (si === sDate.length - 1) {
								resetSecond();
								if (mi === mDate.length - 1) {
									resetMin();
									if (hi === hDate.length - 1) {
										resetHour();
										if (Di === DDate.length - 1) {
											resetDay();
											if (Mi === MDate.length - 1) {
												resetMonth();
												continue goYear;
											}
											continue goMonth;
										}
										continue goDay;
									}
									continue goHour;
								}
								continue goMin;
							}
						} // goSecond
					} // goMin
				} // goHour
			} // goDay
		} // goMonth
	} // goYear

	// 判断100年内的结果条数
	if (resultArr.length === 0) {
		resultList.value = ["没有达到条件的结果！"];
	} else {
		resultList.value = resultArr;
		if (resultArr.length !== 5) {
			resultList.value.push(`最近100年内只有上面${resultArr.length}条结果！`);
		}
	}

	// 计算完成-显示结果
	isShow.value = true;
};

// 用于计算某位数字在数组中的索引
const getIndex = (arr: number[], value: number): number => {
	if (arr.length === 0) return 0;

	if (value <= arr[0]) {
		return 0;
	}

	if (value > arr[arr.length - 1]) {
		return 0;
	}

	for (let i = 0; i < arr.length - 1; i++) {
		if (value > arr[i] && value <= arr[i + 1]) {
			return i + 1;
		}
	}

	return 0;
};

// 获取"年"数组
const getYearArr = (rule: string, year: number) => {
	dateArr.value[YEAR_ARR_IDX] = getOrderArr(year, year + 100);

	if (rule !== undefined && rule.trim() !== "") {
		if (rule.includes("-")) {
			dateArr.value[YEAR_ARR_IDX] = getCycleArr(rule, year + 100, false);
		} else if (rule.includes("/")) {
			dateArr.value[YEAR_ARR_IDX] = getAverageArr(rule, year + 100);
		} else if (rule !== "*") {
			dateArr.value[YEAR_ARR_IDX] = getAssignArr(rule);
		}
	}
};

// 获取"月"数组
const getMonthArr = (rule: string) => {
	dateArr.value[MONTH_ARR_IDX] = getOrderArr(1, 12);

	if (rule.includes("-")) {
		dateArr.value[MONTH_ARR_IDX] = getCycleArr(rule, 12, false);
	} else if (rule.includes("/")) {
		dateArr.value[MONTH_ARR_IDX] = getAverageArr(rule, 12);
	} else if (rule !== "*") {
		dateArr.value[MONTH_ARR_IDX] = getAssignArr(rule);
	}
};

// 获取"日"数组-主要为日期规则
const getWeekArr = (rule: string) => {
	// 只有当日期规则的两个值均为""时则表达日期是有选项的
	if (dayRule.value === "" && dayRuleSup.value === "") {
		if (rule.includes("-")) {
			dayRule.value = "weekDay";
			dayRuleSup.value = getCycleArr(rule, 7, false);
		} else if (rule.includes("#")) {
			dayRule.value = "assWeek";
			const matchRule = rule.match(/[0-9]{1}/g);
			if (matchRule) {
				dayRuleSup.value = [Number(matchRule[1]), Number(matchRule[0])];
				dateArr.value[DAY_ARR_IDX] = [1];
				if (dayRuleSup.value[1] === 7) {
					dayRuleSup.value[1] = 0;
				}
			}
		} else if (rule.includes("L")) {
			dayRule.value = "lastWeek";
			const match = rule.match(/[0-9]{1,2}/g);
			if (match) {
				dayRuleSup.value = Number(match[0]);
				dateArr.value[DAY_ARR_IDX] = [31];
				if (dayRuleSup.value === 7) {
					dayRuleSup.value = 0;
				}
			}
		} else if (rule !== "*" && rule !== "?") {
			dayRule.value = "weekDay";
			dayRuleSup.value = getAssignArr(rule);
		}
	}
};

// 获取"日"数组-少量为日期规则
const getDayArr = (rule: string) => {
	dateArr.value[DAY_ARR_IDX] = getOrderArr(1, 31);
	dayRule.value = "";
	dayRuleSup.value = "";

	if (rule.includes("-")) {
		dateArr.value[DAY_ARR_IDX] = getCycleArr(rule, 31, false);
		dayRuleSup.value = "null";
	} else if (rule.includes("/")) {
		dateArr.value[DAY_ARR_IDX] = getAverageArr(rule, 31);
		dayRuleSup.value = "null";
	} else if (rule.includes("W")) {
		dayRule.value = "workDay";
		const match = rule.match(/[0-9]{1,2}/g);
		if (match) {
			dayRuleSup.value = Number(match[0]);
			dateArr.value[DAY_ARR_IDX] = [dayRuleSup.value as number];
		}
	} else if (rule.includes("L")) {
		dayRule.value = "lastDay";
		dayRuleSup.value = "null";
		dateArr.value[DAY_ARR_IDX] = [31];
	} else if (rule !== "*" && rule !== "?") {
		dateArr.value[DAY_ARR_IDX] = getAssignArr(rule);
		dayRuleSup.value = "null";
	} else if (rule === "*") {
		dayRuleSup.value = "null";
	}
};

// 获取"时"数组
const getHourArr = (rule: string) => {
	dateArr.value[HOUR_ARR_IDX] = getOrderArr(0, 23);

	if (rule.includes("-")) {
		dateArr.value[HOUR_ARR_IDX] = getCycleArr(rule, 24, true);
	} else if (rule.includes("/")) {
		dateArr.value[HOUR_ARR_IDX] = getAverageArr(rule, 23);
	} else if (rule !== "*") {
		dateArr.value[HOUR_ARR_IDX] = getAssignArr(rule);
	}
};

// 获取"分"数组
const getMinArr = (rule: string) => {
	dateArr.value[MINUTE_ARR_IDX] = getOrderArr(0, 59);

	if (rule.includes("-")) {
		dateArr.value[MINUTE_ARR_IDX] = getCycleArr(rule, 60, true);
	} else if (rule.includes("/")) {
		dateArr.value[MINUTE_ARR_IDX] = getAverageArr(rule, 59);
	} else if (rule !== "*") {
		dateArr.value[MINUTE_ARR_IDX] = getAssignArr(rule);
	}
};

// 获取"秒"数组
const getSecondArr = (rule: string) => {
	dateArr.value[SECOND_ARR_IDX] = getOrderArr(0, 59);

	if (rule.includes("-")) {
		dateArr.value[SECOND_ARR_IDX] = getCycleArr(rule, 60, true);
	} else if (rule.includes("/")) {
		dateArr.value[SECOND_ARR_IDX] = getAverageArr(rule, 59);
	} else if (rule !== "*") {
		dateArr.value[SECOND_ARR_IDX] = getAssignArr(rule);
	}
};

// 根据传进来的min-max返回一个顺序的数组
const getOrderArr = (min: number, max: number): number[] => {
	const arr: number[] = [];
	for (let i = min; i <= max; i++) {
		arr.push(i);
	}
	return arr;
};

// 根据规则中指定的零散值返回一个数组
const getAssignArr = (rule: string): number[] => {
	const assiginArr = rule.split(",");
	return assiginArr
		.map((item) => Number(item))
		.filter((n) => !isNaN(n))
		.sort((a, b) => a - b);
};

// 根据一定算术规则计算返回一个数组
const getAverageArr = (rule: string, limit: number): number[] => {
	const agArr = rule.split("/");
	if (agArr.length < 2) return [];

	const min = Number(agArr[0]);
	const step = Number(agArr[1]);

	const arr: number[] = [];
	let current = min;

	while (current <= limit) {
		arr.push(current);
		current += step;
	}

	return arr;
};

// 根据规则返回一个具有周期性的数组
const getCycleArr = (rule: string, limit: number, status: boolean): number[] => {
	// status--表示是否从0开始（则从1开始）
	const cycleArr = rule.split("-");
	if (cycleArr.length < 2) return [];

	let min = Number(cycleArr[0]);
	let max = Number(cycleArr[1]);

	if (min > max) {
		max += limit;
	}

	const arr: number[] = [];

	for (let i = min; i <= max; i++) {
		let add = 0;
		if (!status && i % limit === 0) {
			add = limit;
		}
		arr.push(Math.round((i % limit) + add));
	}

	return arr.sort((a, b) => a - b);
};

// 格式化日期格式如：2017-9-19 18:04:33
const formatDate = (value: Date | number, type?: string): string | number => {
	const time = typeof value === "number" ? new Date(value) : value;

	const Y = time.getFullYear();
	const M = time.getMonth() + 1;
	const D = time.getDate();
	const h = time.getHours();
	const m = time.getMinutes();
	const s = time.getSeconds();
	const week = time.getDay();

	if (type === undefined) {
		return `${Y}-${M < 10 ? `0${M}` : M}-${D < 10 ? `0${D}` : D} ${
			h < 10 ? `0${h}` : h
		}:${m < 10 ? `0${m}` : m}:${s < 10 ? `0${s}` : s}`;
	} else if (type === "week") {
		// 修正：返回数值类型而不是字符串
		return week + 1;
	}

	return "";
};

// 检查日期是否存在
const checkDate = (value: string): boolean => {
	const time = new Date(value);
	const format = formatDate(time);
	return value === format;
};

// 监听表达式变化
watch(
	() => props.ex,
	() => {
		expressionChange();
	}
);

// 初始化获取一次结果
onMounted(() => {
	expressionChange();
});
</script>

<style scoped>
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
	list-style: none;
	padding: 10px;
	background-color: #f9f9f9;
	border-radius: 4px;
}

.popup-result-scroll li {
	padding: 5px;
	border-bottom: 1px solid #eee;
}
</style>

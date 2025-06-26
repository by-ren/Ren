<template>
	<div class="redis-monitor-container">
		<!-- 基本信息区 -->
		<el-card class="info-card">
			<div class="card-title">基本信息</div>
			<div class="info-grid">
				<template v-for="(row, rowIndex) in basicInfoRows" :key="rowIndex">
					<div class="grid-row">
						<template v-for="(item, colIndex) in row" :key="colIndex">
							<div class="label">{{ item.label }}</div>
							<div class="value">{{ item.value }}</div>
						</template>
					</div>
				</template>
			</div>
		</el-card>

		<!-- 命令统计和内存信息区 -->
		<div class="charts-container">
			<!-- 命令统计饼图 -->
			<el-card class="chart-card">
				<div class="card-title">命令统计</div>
				<div ref="pieChartRef" class="chart" style="height: 300px"></div>
			</el-card>

			<!-- 内存信息仪表盘 -->
			<el-card class="chart-card">
				<div class="card-title">内存信息</div>
				<div ref="gaugeChartRef" class="chart" style="height: 300px"></div>
			</el-card>
		</div>
	</div>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from "vue";
import * as echarts from "echarts";
import type { ECharts } from "echarts";
import { getCacheInfo } from "@/api/monitor/cache/index";

/*============================数据类型开始============================*/
// 基本数据结构
interface BasicInfoItem {
	label: string;
	value: string;
}

// 命令统计结构
interface CommandStatItem {
	name: string;
	value: number;
}

// 内存统计结构
interface MemoryStatItem {
	min: number;
	max: number;
	value: number;
}
/*============================数据类型开始============================*/

/*============================通用参数开始============================*/
// 基本数据
const basicInfoRows = ref<BasicInfoItem[][]>([]);

// 图表实例引用
const pieChartRef = ref<HTMLElement | null>(null);
const gaugeChartRef = ref<HTMLElement | null>(null);
let pieChart: ECharts | null = null;
let gaugeChart: ECharts | null = null;

// 命令统计数据
const commandStats = ref<CommandStatItem[]>([]);

// 内存统计数据
const memoryStats = ref<MemoryStatItem | null>();
/*============================通用参数结束============================*/

/*============================页面方法开始============================*/
// 初始化图表
const initCharts = () => {
	// 检查DOM元素是否已挂载（防止初始化时元素未加载）
	if (!pieChartRef.value || !gaugeChartRef.value) return;

	// ============ 命令统计饼图 ============
	pieChart = echarts.init(pieChartRef.value); // 初始化饼图实例
	pieChart.setOption({
		// 设置饼图配置选项
		tooltip: {
			// 提示框组件
			trigger: "item", // 触发方式：数据项图形触发
			formatter: "{b}: {c} ({d}%)", // 格式化显示：名称: 数值 (百分比)
		},
		legend: {
			// 图例组件
			bottom: "bottom", // 图例位置：底部居中
			data: commandStats.value?.map((item) => item.name), // 图例数据源（命令名称）
		},
		color: ["#409eff", "#e6a23c", "#67c23a", "#f56c6c", "#909399", "#b37feb"], // 自定义饼图颜色序列
		series: [
			// 系列列表（一个图表可以有多个系列）
			{
				name: "命令统计", // 系列名称
				type: "pie", // 图表类型：饼图
				radius: ["30%", "65%"], // 饼图半径 [内半径, 外半径] => 环状图
				avoidLabelOverlap: false, // 关闭标签防重叠功能（因标签在内部）
				itemStyle: {
					// 图形样式
					borderRadius: 10, // 圆角大小（块状饼图效果）
					borderColor: "#fff", // 边框颜色
					borderWidth: 2, // 边框宽度
				},
				label: {
					// 饼图图形上的文本标签
					show: true, // 显示标签
					position: "inside", // 标签位置：饼图内部
				},
				emphasis: {
					// 高亮状态配置（鼠标悬停）
					label: {
						show: true, // 高亮时显示标签
						fontSize: "14", // 放大字体
						fontWeight: "bold", // 加粗字体
					},
					itemStyle: {
						shadowBlur: 10, // 阴影模糊大小
						shadowColor: "rgba(0, 0, 0, 0.5)", // 阴影颜色
					},
				},
				labelLine: {
					// 标签的视觉引导线配置
					show: true, // 显示引导线
				},
				data: commandStats.value, // 数据数组：命令统计值
			},
		],
	});

	// ============ 内存信息仪表盘 ============
	gaugeChart = echarts.init(gaugeChartRef.value); // 初始化仪表盘实例
	gaugeChart.setOption({
		// 设置仪表盘配置选项
		tooltip: {
			// 提示框组件
			formatter: "{a} <br/>{b} : {c}K", // 格式化显示：系列名 <br/>数据项: 数据值
		},
		series: [
			{
				name: "内存", // 系列名称
				type: "gauge", // 图表类型：仪表盘
				min: memoryStats.value?.min, // 最小值
				max: memoryStats.value?.max, // 最大值
				progress: {
					// 进度条配置
					show: true, // 显示进度条
					width: 18, // 进度条宽度
					roundCap: true, // 圆角端点
				},
				axisLine: {
					// 仪表盘轴线配置
					lineStyle: {
						width: 18, // 轴线宽度（控制进度条粗细）
					},
				},
				axisTick: {
					// 刻度线配置
					show: true, // 显示刻度线
				},
				splitLine: {
					// 分隔线配置（主要刻度）
					distance: -15, // 与轴线距离（负值表示向圆心方向）
					length: 10, // 分隔线长度
					lineStyle: {
						width: 2, // 分隔线宽度
						color: "#999", // 分隔线颜色
					},
				},
				axisLabel: {
					// 刻度标签
					distance: -5, // 与轴线距离
					color: "#999", // 标签颜色
					fontSize: 12, // 标签字体大小
				},
				anchor: {
					// 指针固定点
					show: true, // 显示锚点
					showAbove: true, // 显示在仪表盘上层
					size: 18, // 锚点大小
					itemStyle: {
						borderWidth: 4, // 锚点边框宽度
					},
				},
				title: {
					// 标题（系列名称）
					show: true, // 显示标题
				},
				detail: {
					// 仪表盘详情（数值显示）
					valueAnimation: true, // 数值动画
					formatter: "{value}K", // 格式化显示：数值 + K
					fontSize: 20, // 字体大小
					fontWeight: "bold", // 字体加粗
					color: "#409eff", // 字体颜色
				},
				data: [
					// 数据数组（仪表盘指针值）
					{
						value: memoryStats.value?.value, // 当前值
						name: "内存占用", // 数据项名称
					},
				],
			},
		],
	});
};

// 响应式调整图表大小
const handleResize = () => {
	//响应式调整pieChart的大小
	if (pieChart) pieChart.resize();
	//响应式调整gaugeChart的大小
	if (gaugeChart) gaugeChart.resize();
};

//获取页面数据
const getPageData = async () => {
	const res = await getCacheInfo();
	if (res.code === 200) {
		basicInfoRows.value = res.basicInfoRows;
		commandStats.value = res.commandStats;
		memoryStats.value = res.memoryStats;
		initCharts();
	}
};
/*============================页面方法结束============================*/

/*============================生命周期钩子开始============================*/

// 组件挂载后初始化图表
onMounted(() => {
	getPageData();
	// 添加一个监听器，在窗口调整大小时触发 handleResize 函数
	window.addEventListener("resize", handleResize);
});

// 组件卸载前清理资源
onBeforeUnmount(() => {
	//卸载Echarts的实例
	if (pieChart) pieChart.dispose();
	if (gaugeChart) gaugeChart.dispose();
	// 移除窗口调整大小时触发的监听器
	window.removeEventListener("resize", handleResize);
});

/*============================生命周期钩子结束============================*/
</script>

<style scoped lang="scss">
.redis-monitor-container {
	margin: 20px auto;
	padding: 15px;

	.card-title {
		font-size: 18px;
		font-weight: bold;
		margin-bottom: 15px;
		color: #333;
	}

	.info-card {
		margin-bottom: 20px;
		box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

		.info-grid {
			display: flex;
			flex-direction: column;

			.grid-row {
				display: flex;
				border-bottom: 1px solid #ebeef5;
				padding: 12px 0;

				&:last-child {
					border-bottom: none;
				}

				.label {
					flex: 0 0 150px;
					font-weight: bold;
					color: #606266;
					padding: 0 10px;
				}

				.value {
					flex: 1;
					font-weight: normal;
					padding: 0 10px;
					overflow: hidden;
					text-overflow: ellipsis;
					white-space: nowrap;
				}
			}
		}
	}

	.charts-container {
		display: flex;
		gap: 20px;

		@media (max-width: 768px) {
			flex-direction: column;
		}

		.chart-card {
			flex: 1;
			box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

			.chart {
				width: 100%;
			}
		}
	}
}
</style>

<template>
    <div v-loading="loading">
        <el-row :gutter="20" >
            <el-col :span="12">
                <el-card class="box-card">
                    <template #header>
                        <div class="card-header">
                            <el-icon :size="20" style="margin-right:10px"><i-ep-cpu /></el-icon>
                            <span>CPU</span>
                        </div>
                    </template>
                    <el-descriptions :column="1" :border="true">
                        <el-descriptions-item label="核心数" label-class-name="labelClass"><span class="content-text">{{cpuInfo.cpuNum}}</span></el-descriptions-item>
                        <el-descriptions-item label="用户使用率" label-class-name="labelClass"><span class="content-text">{{cpuInfo.sysUsed}}</span></el-descriptions-item>
                        <el-descriptions-item label="系统使用率" label-class-name="labelClass"><span class="content-text">{{cpuInfo.sysUsed}}</span></el-descriptions-item>
                        <el-descriptions-item label="当前空闲率" label-class-name="labelClass"><span class="content-text">{{cpuInfo.free}}</span></el-descriptions-item>
                    </el-descriptions>
                </el-card>
            </el-col>
            <el-col :span="12">
                <el-card class="box-card">
                    <template #header>
                        <div class="card-header">
                            <el-icon :size="20" style="margin-right:10px"><i-ep-tickets /></el-icon>
                            <span>内存</span>
                        </div>
                    </template>
                    <el-descriptions :column="2" :border="true">
                        <el-descriptions-item label="服务器总内存" label-class-name="labelClass"><span class="content-text">{{memoryInfo.total}}</span></el-descriptions-item>
                        <el-descriptions-item label="服务器已用内存" label-class-name="labelClass"><span class="content-text">{{memoryInfo.used}}</span></el-descriptions-item>
                        <el-descriptions-item label="服务器剩余内存" label-class-name="labelClass"><span class="content-text">{{memoryInfo.available}}</span></el-descriptions-item>
                        <el-descriptions-item label="服务器内存使用率" label-class-name="labelClass"><span class="content-text">{{memoryInfo.occupancyRate}}</span></el-descriptions-item>
                        <el-descriptions-item label="JVM总内存" label-class-name="labelClass"><span class="content-text">{{memoryInfo.jvmTotal}}</span></el-descriptions-item>
                        <el-descriptions-item label="JVM已用内存" label-class-name="labelClass"><span class="content-text">{{memoryInfo.jvmUsed}}</span></el-descriptions-item>
                        <el-descriptions-item label="JVM剩余内存" label-class-name="labelClass"><span class="content-text">{{memoryInfo.jvmAvailable}}</span></el-descriptions-item>
                        <el-descriptions-item label="JVM内存使用率" label-class-name="labelClass"><span class="content-text">{{memoryInfo.jvmOccupancyRate}}</span></el-descriptions-item>
                    </el-descriptions>
                </el-card>
            </el-col>
        </el-row>
        <el-row>
            <el-col :span="24">
                <el-card class="box-card">
                    <template #header>
                        <div class="card-header">
                            <el-icon :size="20" style="margin-right:10px"><i-ep-monitor /></el-icon>
                            <span>服务器信息</span>
                        </div>
                    </template>
                    <el-descriptions :column="2" :border="true">
                        <el-descriptions-item label="服务器名称" label-class-name="labelClass"><span class="content-text">{{computerSystemInfo.computerName}}</span></el-descriptions-item>
                        <el-descriptions-item label="操作系统" label-class-name="labelClass"><span class="content-text">{{computerSystemInfo.family}}</span></el-descriptions-item>
                        <el-descriptions-item label="服务器IP" label-class-name="labelClass"><span class="content-text">{{computerSystemInfo.computerIp}}</span></el-descriptions-item>
                        <el-descriptions-item label="系统架构" label-class-name="labelClass"><span class="content-text">{{computerSystemInfo.osArch}}</span></el-descriptions-item>
                    </el-descriptions>
                </el-card>
            </el-col>
        </el-row>
        <el-row>
            <el-col :span="24">
                <el-card class="box-card">
                    <template #header>
                        <div class="card-header">
                            <el-icon :size="20" style="margin-right:10px"><i-ep-coffeecup /></el-icon>
                            <span>JAVA虚拟机信息</span>
                        </div>
                    </template>
                    <el-descriptions :column="2" :border="true" class="custom-descriptions">
                        <el-descriptions-item label="JAVA名称" label-class-name="labelClass"><span class="content-text">{{jvmInfo.jvmName}}</span></el-descriptions-item>
                        <el-descriptions-item label="JAVA版本" label-class-name="labelClass"><span class="content-text">{{javaInfo.javaVersion}}</span></el-descriptions-item>
                        <el-descriptions-item label="启动时间" label-class-name="labelClass"><span class="content-text">{{projectInfo.startTimeStr}}</span></el-descriptions-item>
                        <el-descriptions-item label="运行时长" label-class-name="labelClass"><span class="content-text">{{projectInfo.runningTimeStr}}</span></el-descriptions-item>
                        <el-descriptions-item label="安装路径" label-class-name="labelClass"><span class="content-text">{{javaInfo.javaHome}}</span></el-descriptions-item>
                        <el-descriptions-item label="项目路径" label-class-name="labelClass"><span class="content-text">{{projectInfo.projectDir}}</span></el-descriptions-item>
                        <el-descriptions-item label="运行参数" label-class-name="labelClass"><span class="long-content">{{jvmInfo.inputArgs}}</span></el-descriptions-item>
                    </el-descriptions>
                </el-card>
            </el-col>
        </el-row>
        <el-row>
            <el-col :span="24">
                <el-card class="box-card">
                    <template #header>
                        <div class="card-header">
                            <el-icon :size="20" style="margin-right:10px"><i-ep-folder /></el-icon>
                            <span>磁盘状态</span>
                        </div>
                    </template>
                    <el-table :data="diskInfoList" style="width: 100%">
                        <el-table-column prop="dirName" label="盘符路径" />
                        <el-table-column prop="sysTypeName" label="文件系统" />
                        <el-table-column prop="typeName" label="盘符类型" />
                        <el-table-column prop="total" label="总大小" />
                        <el-table-column prop="free" label="可用大小" />
                        <el-table-column prop="used" label="已用大小" />
                        <el-table-column prop="usage" label="已用百分比" />
                    </el-table>
                </el-card>
            </el-col>
        </el-row>
    </div>
</template>
  
<script setup lang="ts" name="sysUserOnline">
    import { ref,onMounted } from 'vue'
    import { ElMessage } from 'element-plus'
    import {getSeverInfo} from '@/api/monitor/server/index'
    import type { ComputerSystemInfoBO,CpuInfoBO,DiskInfoBO,JavaInfoBO,JvmInfoBO,MemoryInfoBO,NetWorkInfoBO,ProjectInfoBO } from '@/types/ServerInfo';
    /*============================通用参数开始============================*/
    // Cpu相关信息
    let cpuInfo = ref<CpuInfoBO>({
        cpuNum : 0,
        cpuModel: '',
        used: '',
        waitUsed: '',
        userUsed: '',
        sysUsed: '',
        free: '',
    });
    // 内存信息
    let memoryInfo = ref<MemoryInfoBO>({
        total: '',
        available: '',
        used: '',
        occupancyRate: '',
        jvmTotal: '',
        jvmAvailable: '',
        jvmUsed: '',
        jvmOccupancyRate: '',
        jvmMax: '',
    });
    // 服务器信息
    let computerSystemInfo = ref<ComputerSystemInfoBO>({
        manufacturer: '',
        model: '',
        serialNumber: '',
        family: '',
        osManufacturer: '',
        version: '',
        bitness: 0,
        systemUptime: 0,
        processCount: 0,
        computerName: '',
        computerIp: '',
        osArch: '',
    });
    // Java虚拟机信息
    let jvmInfo = ref<JvmInfoBO>({
        jvmName: '',
        jvmVersion: '',
        jvmVendor: '',
        jvmInfoStr: '',
        jvmSpecName: '',
        jvmSpecVersion: '',
        jvmSpecVendor: '',
        inputArgs: '',
    });
    // Java信息
    let javaInfo = ref<JavaInfoBO>({
        javaVersion: '',
        javaVersionInt: 0,
        javaVendor: '',
        runtimeName: '',
        runtimeVersion: '',
        javaHome: '',
        javaSpecName: '',
        javaSpecVersion: '',
        javaSpecVendor: '',
    });
    // 项目信息
    let projectInfo = ref<ProjectInfoBO>({
        projectDir: '',
        startTimeStr: '',
        runningTimeStr: '',
    })
    // 磁盘信息
    let diskInfoList = ref<DiskInfoBO[]>([]);
    //加载中
    let loading = ref(true);
    /*============================通用参数结束============================*/
    
    
    /*============================页面方法开始============================*/
    //获取服务监控信息
    const getSeverInfoHandler = async () => {
        let result = await getSeverInfo();
        if(result.code == 200){
            cpuInfo.value = result.cpuInfo;
            memoryInfo.value = result.memoryInfo;
            computerSystemInfo.value = result.computerSystemInfo;
            jvmInfo.value = result.jvmInfo;
            javaInfo.value = result.javaInfo;
            projectInfo.value = result.projectInfo;
            diskInfoList.value = result.diskInfoList;
            loading.value = false;
        }else{
            ElMessage.error(result.msg);
        }
    }
    /*============================页面方法结束============================*/

    
    /*============================生命周期钩子开始============================*/
    // 组件加载完成后执行
    // 初始格数据
    onMounted(async () => {
        getSeverInfoHandler();
    })
    /*============================生命周期钩子结束============================*/
</script>
  
<style scoped>
.el-row {
  margin-bottom: 20px;
}
:deep(.el-card__header) {
   padding: 15px 15px 7px;
}
.card-header {
  display: flex;
  justify-content: flex-start;
  align-items: center;
}
/* 确保容器自适应 */
.custom-descriptions {
  width: 100%;
}
:deep(.labelClass) {
    width: 150px;
}
/* 所有内容基础样式 */
.content-text {
  display: inline-block;
  max-width: 100%; /* 预留 label 宽度 */
  word-wrap: break-word; /* 强制换行 */
}
/* 长文本特殊处理 */
.long-content {
  display: inline-block;
  max-width: 100%; 
  word-break: break-all; /* 允许在任意字符间断行 */
  vertical-align: top; /* 顶部对齐防止高度塌陷 */
}
</style>
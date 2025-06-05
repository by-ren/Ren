<template>
    <el-form ref="searchFormRef" :rules="rules" :inline="true" :model="tableParams" class="demo-form-inline">
        <el-form-item label="登陆地址" prop="ipaddr">
            <el-input v-model="tableParams.ipaddr" placeholder="登陆地址" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="用户名称" prop="userName">
            <el-input v-model="tableParams.userName" placeholder="用户名称" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="search">搜索</el-button>
            <el-button @click="resetForm(searchFormRef)">重置</el-button>
        </el-form-item>
    </el-form>
    <el-table :data="paginatedData" stripe>
        <el-table-column prop="deptName" label="部门名称" width="140" show-overflow-tooltip></el-table-column>
        <el-table-column prop="userName" label="用户名称" width="140" show-overflow-tooltip></el-table-column>
        <el-table-column prop="ipaddr" label="登录IP地址" width="180" show-overflow-tooltip></el-table-column>
        <el-table-column prop="loginLocation" label="登录地址" width="200" show-overflow-tooltip></el-table-column>
        <el-table-column prop="browser" label="浏览器类型" width="200" show-overflow-tooltip></el-table-column>
        <el-table-column prop="os" label="操作系统" width="350" show-overflow-tooltip></el-table-column>
        <el-table-column prop="loginTime" label="登录时间" width="180" show-overflow-tooltip></el-table-column>
        <el-table-column fixed="right" label="操作" min-width="120">
            <template #default="scope">
                <el-button link type="primary" size="small" @click="handleCompulsoryWithdrawal(scope.$index, scope.row)">强退</el-button>
            </template>
        </el-table-column>
    </el-table>
    <Pagination
        :total="tableData.length"
        v-model:page="pageNum"
        v-model:limit="pageSize"
        @pagination="getList"
    />
</template>
  
<script setup lang="ts" name="sysUserOnline">
    import { ref,onMounted } from 'vue'
    import type { FormInstance,FormRules } from 'element-plus'
    import { ElMessage,ElTree } from 'element-plus'
    import type Node from 'element-plus/es/components/tree/src/model/node'
    import {getSysUserOnlineList,compulsoryWithdrawalSysUserOnline} from '@/api/monitor/online/index'
    /*============================通用参数开始============================*/
    //表格数据
    let tableData = ref([]);
    interface TableParams {
        ipaddr: string
        userName: string
    }
    //查询参数
    let tableParams = ref<TableParams>({
        //登陆地址
        ipaddr: "",
        //用户名称
        userName: "",
    });
    //查询表单
    const searchFormRef = ref<FormInstance>()
    //表单验证规则（即使用不到，为了重置方法，也需要写）
    const rules = reactive<FormRules<TableParams>>({
    })
    //当前页
    const pageNum = ref(1)
    //每页显示多少条
    const pageSize = ref(10)
    /*============================通用参数结束============================*/
    
    
    /*============================页面方法开始============================*/
    //获取列表
    const getList = async () => {
        let result = await getSysUserOnlineList(tableParams.value);
        if(result.code == 200){
            tableData.value = result.data;
        }else{
            ElMessage.error(result.msg);
        }
    }
    //页面搜索方法
    const search = async () => {
        pageNum.value = 1;
        getList();
    }
    //重置
    const resetForm = (formEl: FormInstance | undefined) => {
        if (!formEl) return
        formEl.resetFields()
    }
    /*********强退在线用户*********/
    const handleCompulsoryWithdrawal = async(index: number, row: any) => {
        //调用强退方法
        try {
            let result = await compulsoryWithdrawalSysUserOnline(row.tokenId);
            if(result.code == 200){
                ElMessage({
                    message: '操作成功',
                    type: 'success',
                })
                //重新加载表单
                search();
            }else{
                ElMessage.error(result.msg);
            }
        } catch (error) {
            ElMessage.error('操作失败');
        }
    }
    /*============================页面方法结束============================*/

    /*============================计算属性开始============================*/
    // 核心计算属性：计算当前页数据
    const paginatedData = computed(() => {
        const start = (pageNum.value - 1) * pageSize.value;
        const end = start + pageSize.value;
        return tableData.value.slice(start, end);
    });
    /*============================计算属性结束============================*/

    /*============================生命周期钩子开始============================*/
    // 组件加载完成后执行
    // 初始化表格数据
    onMounted(async () => {
        search();
    })
    /*============================生命周期钩子结束============================*/
</script>
  
<style scoped>
    .demo-form-inline .el-input {
        --el-input-width: 220px;
    }

    .demo-form-inline .el-select {
        --el-select-width: 220px;
    }

    .btns{
        margin-bottom: 10px;
    }

</style>
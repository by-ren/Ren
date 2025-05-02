<template>
    <el-form ref="searchFormRef" :rules="rules" :inline="true" :model="tableParams" class="demo-form-inline">
        <el-form-item label="用户类型" prop="userType">
            <el-select
                v-model="tableParams.userType"
                placeholder="全部"
                value-on-clear=""
                clearable
            >
                <el-option label="系统用户" value="00" />
            </el-select>
        </el-form-item>
        <el-form-item label="性别" prop="sex">
            <el-select
                v-model="tableParams.sex"
                placeholder="全部"
                :empty-values="[null,undefined,-1]"
                :value-on-clear="-1"
                clearable
            >
                <el-option label="男" :value="0" />
                <el-option label="女" :value="1" />
                <el-option label="未知" :value="2" />
            </el-select>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="search">搜索</el-button>
            <el-button @click="resetForm(searchFormRef)">重置</el-button>
        </el-form-item>
    </el-form>
    <el-table :data="tableData" stripe>
        <el-table-column prop="username" label="登陆账号" width="140" show-overflow-tooltip></el-table-column>
        <el-table-column prop="nickname" label="用户昵称" width="140" show-overflow-tooltip></el-table-column>
        <el-table-column prop="userType" label="用户类型" width="140" :align="'center'" show-overflow-tooltip>
            <template #default="item">
                <el-tag v-if="item.row.userType == '00'">系统用户</el-tag>
            </template>
        </el-table-column>
        <el-table-column prop="email" label="邮箱" width="140" show-overflow-tooltip></el-table-column>
        <el-table-column prop="phonenumber" label="手机号码" width="150" show-overflow-tooltip></el-table-column>
        <el-table-column prop="sex" label="性别" width="120" :align="'center'" show-overflow-tooltip>
            <template #default="item">
                <el-tag v-if="item.row.sex == 0">男</el-tag>
                <el-tag v-if="item.row.sex == 1">女</el-tag>
                <el-tag v-if="item.row.sex == 2">未知</el-tag>
            </template>
        </el-table-column>
        <el-table-column prop="loginDateStr" label="最后登录时间" width="180" show-overflow-tooltip></el-table-column>
        <el-table-column prop="loginIp" label="最后登录IP" width="140" show-overflow-tooltip></el-table-column>
        <el-table-column prop="remark" label="备注" width="200" show-overflow-tooltip></el-table-column>
        <el-table-column fixed="right" label="操作" min-width="120">
            <template #default>
                <el-button link type="primary" size="small">修改</el-button>
                <el-button link type="primary" size="small">删除</el-button>
                <el-button link type="primary" size="small">重置密码</el-button>
            </template>
        </el-table-column>
    </el-table>
</template>
  
<script setup lang="ts" name="home">
    import { ref,onMounted } from 'vue'
    import {getUserList} from '@/api/user/index'
    import type { FormInstance,FormRules } from 'element-plus'
    import { ElMessage } from 'element-plus'
    /*============================通用参数开始============================*/
    //表格数据
    let tableData = ref([]);
    interface TableParams {
        userType: string
        sex: number
    }
    //查询参数
    let tableParams = ref<TableParams>({
        //用户类型
        userType: "",
        //性别(设置-1为默认值，表示全选)
        sex:-1,
    });
    //查询表单
    const searchFormRef = ref<FormInstance>()
    //表单验证规则（即使用不到，为了重置方法，也需要写）
    const rules = reactive<FormRules<TableParams>>({
    })
    /*============================通用参数结束============================*/
    
    
    /*============================页面方法结束============================*/
    //页面搜索方法
    const search = async () => {
        let result = await getUserList(tableParams.value);
        if(result.code == 200){
            tableData.value = result.userList;
        }
    }
    //重置
    const resetForm = (formEl: FormInstance | undefined) => {
        if (!formEl) return
        formEl.resetFields()
    }
    /*============================页面方法结束============================*/


    /*============================生命周期钩子开始============================*/
    // 组件加载完成后执行
    // 初始化表格数据
    onMounted(async () => {
        let result = await getUserList(tableParams.value);
        if(result.code == 200){
            tableData.value = result.userList;
        }
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
</style>
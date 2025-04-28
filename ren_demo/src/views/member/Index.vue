<template>
    <div >
        <el-table :data="tableData" stripe>
            <el-table-column prop="username" label="姓名" width="140" show-overflow-tooltip>
            </el-table-column>
            <el-table-column prop="enabled" label="账号是否启用" width="120" :align="'center'" show-overflow-tooltip>
                <template #default="item">
                    <el-tag v-if="item.row.enabled == false">否</el-tag>
                    <el-tag v-if="item.row.enabled == true">是</el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="accountNonExpired" label="账号是否过期" width="120" :align="'center'" show-overflow-tooltip>
                <template #default="item">
                    <el-tag v-if="item.row.accountNonExpired == true">否</el-tag>
                    <el-tag v-if="item.row.accountNonExpired == false">是</el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="accountNonLocked" label="账号是否锁定" width="120" :align="'center'" show-overflow-tooltip>
                <template #default="item">
                    <el-tag v-if="item.row.accountNonLocked == true">否</el-tag>
                    <el-tag v-if="item.row.accountNonLocked == false">是</el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="credentialsNonExpired" label="证书是否过期" width="120" :align="'center'" show-overflow-tooltip>
                <template #default="item">
                    <el-tag v-if="item.row.credentialsNonExpired == true">否</el-tag>
                    <el-tag v-if="item.row.credentialsNonExpired == false">是</el-tag>
                </template>
            </el-table-column>
            <el-table-column fixed="right" label="Operations" min-width="120">
                <template #default>
                    <el-button link type="primary" size="small">详情</el-button>
                    <el-button link type="primary" size="small">编辑</el-button>
                </template>
            </el-table-column>
        </el-table>
    </div>
  </template>
  
  <script setup lang="ts" name="home">
    import { reactive,computed,onMounted } from 'vue'
    import {getUserList} from '@/api/member/index'
    /*============================获取初始数据开始============================*/
    // 拼接className
    let tableData = reactive([]);
    // 组件加载完成后执行
    onMounted(async () => {
        let result = await getUserList();
        if(result.code == 200){
            Object.assign(tableData,result.userList);
        }
    })
    /*============================获取初始数据结束============================*/
  </script>
  
  <style scoped>
  </style>
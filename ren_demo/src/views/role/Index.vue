<template>
    <el-form ref="searchFormRef" :rules="rules" :inline="true" :model="tableParams" class="demo-form-inline">
        <el-form-item label="查询" prop="searchLike">
            <el-input v-model="tableParams.searchLike" placeholder="角色名称/权限字符" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="search">搜索</el-button>
            <el-button @click="resetForm(searchFormRef)">重置</el-button>
        </el-form-item>
    </el-form>
    <el-row class="btns">
        <el-col :span="24">
            <el-button type="primary" @click="openAddRoleDialog">添加角色</el-button>
        </el-col>
    </el-row>
    <el-table :data="tableData" stripe>
        <el-table-column prop="roleName" label="角色名称" width="140" show-overflow-tooltip></el-table-column>
        <el-table-column prop="roleKey" label="权限字符" width="140" show-overflow-tooltip></el-table-column>
        <el-table-column prop="roleSort" label="显示顺序" width="140" show-overflow-tooltip></el-table-column>
        <el-table-column prop="dataScope" label="数据权限" width="120" :align="'center'" show-overflow-tooltip>
            <template #default="item">
                <el-tag v-if="item.row.dataScope == 1">全部数据</el-tag>
                <el-tag v-if="item.row.dataScope == 2">自定数据</el-tag>
                <el-tag v-if="item.row.dataScope == 3">本部门数据</el-tag>
                <el-tag v-if="item.row.dataScope == 4">本部门及以下数据</el-tag>
                <el-tag v-if="item.row.dataScope == 5">仅本人数据</el-tag>
            </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" width="200" show-overflow-tooltip></el-table-column>
        <el-table-column fixed="right" label="操作" min-width="120">
            <template #default="scope">
                <el-button link type="primary" size="small" @click="openModifyRoleDialog(scope.$index, scope.row)" v-if="scope.row.roleKey != 'admin'">修改</el-button>
                <el-button link type="primary" size="small" @click="handleDeleteRole(scope.$index, scope.row)" v-if="scope.row.roleKey != 'admin'">删除</el-button>
            </template>
        </el-table-column>
    </el-table>
    <el-dialog title="添加角色" v-model="dialogFormAddRole" width="500px">
        <el-form :model="addRoleForm" :rules="addRoleFormRules" ref="addRoleFormRef">
            <el-form-item label="角色名称" :label-width="addRoleFormLabelWidth" prop="roleName">
                <el-input v-model="addRoleForm.roleName" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="是否停用" :label-width="addRoleFormLabelWidth" prop="isStop">
                <el-radio-group v-model="addRoleForm.isStop" size="large" fill="#6cf">
                    <el-radio-button label="是" :value="1" />
                    <el-radio-button label="否" :value="0" />
                </el-radio-group>
            </el-form-item>
            <el-form-item label="权限字符" :label-width="addRoleFormLabelWidth" prop="roleKey">
                <el-input v-model="addRoleForm.roleKey" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="显示顺序" :label-width="addRoleFormLabelWidth" prop="roleSort">
                <el-input v-model="addRoleForm.roleSort" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="数据权限" :label-width="addRoleFormLabelWidth" prop="dataScope">
                <el-select
                    v-model="addRoleForm.dataScope"
                    placeholder="请选择"
                    :empty-values="[null,undefined,-1]"
                    :value-on-clear="-1"
                    clearable
                >
                    <el-option label="全部数据" :value="1" />
                    <el-option label="自定数据" :value="2" />
                    <el-option label="本部门数据" :value="3" />
                    <el-option label="本部门及以下数据" :value="4" />
                    <el-option label="仅本人数据" :value="5" />
                </el-select>
            </el-form-item>
            <el-form-item label="备注" :label-width="addRoleFormLabelWidth" prop="remark">
                <el-input v-model="addRoleForm.remark" autocomplete="off"></el-input>
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="addRoleCancel">取 消</el-button>
                <el-button type="primary" @click="addRoleConfirm(addRoleFormRef)">
                    确 定
                </el-button>
            </span>
        </template>
    </el-dialog>
    <el-dialog title="修改角色" v-model="dialogFormModifyRole" width="500px">
        <el-form :model="modifyRoleForm" :rules="modifyRoleFormRules" ref="modifyRoleFormRef">
            <el-form-item label="角色名称" :label-width="modifyRoleFormLabelWidth" prop="roleName">
                <el-input v-model="modifyRoleForm.roleName" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="是否停用" :label-width="addRoleFormLabelWidth" prop="isStop">
                <el-radio-group v-model="modifyRoleForm.isStop" size="large" fill="#6cf">
                    <el-radio-button label="是" :value="1" />
                    <el-radio-button label="否" :value="0" />
                </el-radio-group>
            </el-form-item>
            <el-form-item label="权限字符" :label-width="modifyRoleFormLabelWidth" prop="roleKey">
                <el-input v-model="modifyRoleForm.roleKey" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="显示顺序" :label-width="modifyRoleFormLabelWidth" prop="roleSort">
                <el-input v-model="modifyRoleForm.roleSort" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="数据权限" :label-width="modifyRoleFormLabelWidth" prop="dataScope">
                <el-select
                    v-model="modifyRoleForm.dataScope"
                    placeholder="请选择"
                    :empty-values="[null,undefined,-1]"
                    :value-on-clear="-1"
                    clearable
                >
                    <el-option label="全部数据" :value="1" />
                    <el-option label="自定数据" :value="2" />
                    <el-option label="本部门数据" :value="3" />
                    <el-option label="本部门及以下数据" :value="4" />
                    <el-option label="仅本人数据" :value="5" />
                </el-select>
            </el-form-item>
            <el-form-item label="备注" :label-width="modifyRoleFormLabelWidth" prop="remark">
                <el-input v-model="modifyRoleForm.remark" autocomplete="off"></el-input>
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="modifyRoleCancel">取 消</el-button>
                <el-button type="primary" @click="modifyRoleConfirm(modifyRoleFormRef)">
                    确 定
                </el-button>
            </span>
        </template>
    </el-dialog>
</template>
  
<script setup lang="ts" name="role">
    import { ref,onMounted } from 'vue'
    import type { FormInstance,FormRules } from 'element-plus'
    import { ElMessage } from 'element-plus'
    import {getRoleList,addRole,modifyRole,deleteRole} from '@/api/role/index'
    /*============================通用参数开始============================*/
    //表格数据
    let tableData = ref([]);
    interface TableParams {
        searchLike: string
    }
    //查询参数
    let tableParams = ref<TableParams>({
        //查询参数
        searchLike: "",
    });
    //查询表单
    const searchFormRef = ref<FormInstance>()
    //表单验证规则（即使用不到，为了重置方法，也需要写）
    const rules = reactive<FormRules<TableParams>>({
    })
    /*============================通用参数结束============================*/
    
    
    /*============================页面方法开始============================*/
    //页面搜索方法
    const search = async () => {
        let result = await getRoleList(tableParams.value);
        if(result.code == 200){
            tableData.value = result.roleList;
        }
    }
    //重置
    const resetForm = (formEl: FormInstance | undefined) => {
        if (!formEl) return
        formEl.resetFields()
    }
    /*********添加角色*********/
    //弹出框是否显示
    let dialogFormAddRole = ref(false);
    //表单元素宽度
    let addRoleFormLabelWidth = ref("80px");
    //添加角色表单初始值
    const initialAddRoleForm = {
        //角色名称
        roleName: "",
        //是否停用
        isStop: 0,
        //权限字符
        roleKey: "",
        //显示顺序
        roleSort: "",
        //数据权限
        dataScope: -1,
        //备注
        remark: "",
    };
    //添加角色表单对象
    const addRoleForm = ref({ ...initialAddRoleForm });
    //添加角色表单对象
    const addRoleFormRef = ref<FormInstance>()
    //添加角色表单验证规则
    const addRoleFormRules = reactive<FormRules>({
        roleName:[
            { required: true, message: '请填写角色名称', trigger: 'blur' }
        ],
        roleKey:[
            { required: true, message: '请填写权限字符', trigger: 'blur' }
        ],
        roleSort:[
            { required: true, message: '请填写显示顺序', trigger: 'blur' },
            { 
                pattern: /^[0-9]\d*$/, 
                message: '只能输入正整数', 
                trigger: ['blur', 'change']
            }
        ],
        dataScope:[
            { pattern: /^(?!-1$|null$).*/, message: '请选择数据权限', trigger: 'blur' },
            { required: true, message: '请选择数据权限', trigger: 'blur' }
        ],
        remark:[
            { required: true, message: '请填写备注', trigger: 'blur' }
        ],
    })
    //打开弹框
    const openAddRoleDialog = () => {
        dialogFormAddRole.value = true;
    }
    //实现添加
    const addRoleConfirm = async (formEl: FormInstance | undefined) => {
        if (!formEl) return
        await formEl.validate(async (valid, fields) => {
            if (valid) {
                //调用添加方法
                let result = await addRole(addRoleForm.value);
                if(result.code == 200){
                    ElMessage({
                        message: '添加成功',
                        type: 'success',
                    })
                    //关闭弹框
                    dialogFormAddRole.value = false;
                    //表单值恢复为初始值
                    addRoleForm.value = { ...initialAddRoleForm };
                    //重新加载表单
                    search();
                }
            }
        })
    };
    //取消弹框
    const addRoleCancel = async () => {
        //关闭弹框
        dialogFormAddRole.value = false;
        //表单值恢复为初始值
        addRoleForm.value = { ...initialAddRoleForm };
    };
    /*********编辑角色*********/
    //弹出框是否显示
    let dialogFormModifyRole = ref(false);
    //表单元素宽度
    let modifyRoleFormLabelWidth = ref("80px");
    //编辑角色表单初始值
    const initialModifyRoleForm = {
        roleId:0,
        //角色名称
        roleName: "",
        //是否停用
        isStop: 0,
        //权限字符
        roleKey: "",
        //显示顺序
        roleSort: "",
        //数据权限
        dataScope: -1,
        //备注
        remark: "",
    };
    //编辑角色表单对象
    const modifyRoleForm = ref({ ...initialModifyRoleForm });
    //编辑角色表单对象
    const modifyRoleFormRef = ref<FormInstance>()
    //编辑角色表单验证规则
    const modifyRoleFormRules = reactive<FormRules>({
        roleName:[
            { required: true, message: '请填写角色名称', trigger: 'blur' }
        ],
        roleKey:[
            { required: true, message: '请填写权限字符', trigger: 'blur' }
        ],
        roleSort:[
            { required: true, message: '请填写显示顺序', trigger: 'blur' },
            { 
                pattern: /^[1-9]\d*$/, 
                message: '只能输入正整数', 
                trigger: ['blur', 'change']
            }
        ],
        dataScope:[
            { pattern: /^(?!-1$|null$).*/, message: '请选择数据权限', trigger: 'blur' },
            { required: true, message: '请选择数据权限', trigger: 'blur' }
        ],
        remark:[
            { required: true, message: '请填写备注', trigger: 'blur' }
        ],
    })
    //打开弹框
    const openModifyRoleDialog = (index: number, row: any) => {
        dialogFormModifyRole.value = true;
        modifyRoleForm.value.roleId = row.roleId;
        modifyRoleForm.value.roleName = row.roleName;
        modifyRoleForm.value.isStop = row.isStop;
        modifyRoleForm.value.roleKey = row.roleKey;
        modifyRoleForm.value.roleSort = row.roleSort;
        modifyRoleForm.value.dataScope = row.dataScope;
        modifyRoleForm.value.remark = row.remark;
    }
    //实现修改
    const modifyRoleConfirm = async (formEl: FormInstance | undefined) => {
        if (!formEl) return
        await formEl.validate(async (valid, fields) => {
            if (valid) {
                //调用修改方法
                let result = await modifyRole(modifyRoleForm.value);
                if(result.code == 200){
                    ElMessage({
                        message: '修改成功',
                        type: 'success',
                    })
                    //关闭弹框
                    dialogFormModifyRole.value = false;
                    //表单值恢复为初始值
                    modifyRoleForm.value = { ...initialModifyRoleForm };
                    //重新加载表单
                    search();
                }
            }
        })
    };
    //取消弹框
    const modifyRoleCancel = async () => {
        //关闭弹框
        dialogFormModifyRole.value = false;
        //表单值恢复为初始值
        modifyRoleForm.value = { ...initialModifyRoleForm };
    };
    /*********删除角色*********/
    const handleDeleteRole = async(index: number, row: any) => {
        //调用删除方法
        try {
            let result = await deleteRole(row.roleId);
            if(result.code == 200){
                ElMessage({
                    message: '删除成功',
                    type: 'success',
                })
                //重新加载表单
                search();
            }
        } catch (error) {
            ElMessage.error('删除失败');
        }
    }
    /*============================页面方法结束============================*/


    /*============================生命周期钩子开始============================*/
    // 组件加载完成后执行
    // 初始化表格数据
    onMounted(async () => {
        let result = await getRoleList(tableParams.value);
        if(result.code == 200){
            tableData.value = result.roleList;
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

    .btns{
        margin-bottom: 10px;
    }
</style>
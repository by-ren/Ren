<template>

<el-container>
    <el-aside width="200px">
        <el-tree 
            :data="deptList" 
            @node-click="handleNodeClick" 
            :default-expand-all="true" 
            :expand-on-click-node="false" 
            :props="{
                label: 'label',
                children: 'children',
            }"
        />
    </el-aside>
    <el-main>
        <el-form ref="searchFormRef" :rules="rules" :inline="true" :model="tableParams" class="demo-form-inline">
            <el-form-item label="查询" prop="searchLike">
                <el-input v-model="tableParams.searchLike" placeholder="登陆账号/昵称/邮箱/手机号码" autocomplete="off"></el-input>
            </el-form-item>
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
        <el-row class="btns">
            <el-col :span="24">
                <el-button type="primary" @click="openAddUserDialog">添加用户</el-button>
            </el-col>
        </el-row>
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
                <template #default="scope">
                    <el-button link type="primary" size="small" @click="openModifyUserDialog(scope.$index, scope.row)">修改</el-button>
                    <el-button link type="primary" size="small" @click="handleDeleteUser(scope.$index, scope.row)">删除</el-button>
                    <el-button link type="primary" size="small" @click="openResetPasswordDialog(scope.$index, scope.row)">重置密码</el-button>
                </template>
            </el-table-column>
        </el-table>
    </el-main>
    <el-dialog title="添加用户" v-model="dialogFormAddUser" width="500px">
        <el-form :model="addUserForm" :rules="addUserFormRules" ref="addUserFormRef">
            <el-form-item label="登陆账号" :label-width="addUserFormLabelWidth" prop="username">
                <el-input v-model="addUserForm.username" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="昵称" :label-width="addUserFormLabelWidth" prop="nickname">
                <el-input v-model="addUserForm.nickname" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="邮箱" :label-width="addUserFormLabelWidth" prop="email">
                <el-input v-model="addUserForm.email" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="手机号" :label-width="addUserFormLabelWidth" prop="phonenumber">
                <el-input v-model="addUserForm.phonenumber" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="性别" :label-width="addUserFormLabelWidth" prop="sex">
                <el-select
                    v-model="addUserForm.sex"
                    placeholder="请选择"
                    :empty-values="[null,undefined,-1]"
                    :value-on-clear="-1"
                    clearable
                >
                    <el-option label="男" :value="0" />
                    <el-option label="女" :value="1" />
                    <el-option label="未知" :value="2" />
                </el-select>
            </el-form-item>
            <el-form-item label="备注" :label-width="addUserFormLabelWidth" prop="remark">
                <el-input v-model="addUserForm.remark" autocomplete="off"></el-input>
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="addUserCancel">取 消</el-button>
                <el-button type="primary" @click="addUserConfirm(addUserFormRef)">
                    确 定
                </el-button>
            </span>
        </template>
    </el-dialog>
    <el-dialog title="编辑用户" v-model="dialogFormModifyUser" width="500px">
        <el-form :model="modifyUserForm" :rules="modifyUserFormRules" ref="modifyUserFormRef">
            <el-form-item label="昵称" :label-width="modifyUserFormLabelWidth" prop="nickname">
                <el-input v-model="modifyUserForm.nickname" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="邮箱" :label-width="modifyUserFormLabelWidth" prop="email">
                <el-input v-model="modifyUserForm.email" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="手机号" :label-width="modifyUserFormLabelWidth" prop="phonenumber">
                <el-input v-model="modifyUserForm.phonenumber" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="性别" :label-width="modifyUserFormLabelWidth" prop="sex">
                <el-select
                    v-model="modifyUserForm.sex"
                    placeholder="请选择"
                    :empty-values="[null,undefined,-1]"
                    :value-on-clear="-1"
                    clearable
                >
                    <el-option label="男" :value="0" />
                    <el-option label="女" :value="1" />
                    <el-option label="未知" :value="2" />
                </el-select>
            </el-form-item>
            <el-form-item label="备注" :label-width="modifyUserFormLabelWidth" prop="remark">
                <el-input v-model="modifyUserForm.remark" autocomplete="off"></el-input>
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="modifyUserCancel">取 消</el-button>
                <el-button type="primary" @click="modifyUserConfirm(modifyUserFormRef)">
                    确 定
                </el-button>
            </span>
        </template>
    </el-dialog>
    <el-dialog title="重置密码" v-model="dialogFormResetPassword" width="500px">
        <el-form :model="resetPasswordForm" :rules="resetPasswordFormRules" ref="resetPasswordFormRef">
            <el-form-item label="密码" :label-width="resetPasswordFormLabelWidth" prop="password">
                <el-input type="password" v-model="resetPasswordForm.password" autocomplete="off"></el-input>
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="resetPasswordCancel">取 消</el-button>
                <el-button type="primary" @click="resetPasswordConfirm(resetPasswordFormRef)">
                    确 定
                </el-button>
            </span>
        </template>
    </el-dialog>
</el-container>

    
</template>
  
<script setup lang="ts" name="home">
    import { ref,onMounted } from 'vue'
    import type { FormInstance,FormRules } from 'element-plus'
    import { ElMessage } from 'element-plus'
    import {getUserList,resetPassword,deleteUser,modifyUser,addUser,getDeptList} from '@/api/user/index'
    /*============================通用参数开始============================*/
    //表格数据
    let tableData = ref([]);
    interface TableParams {
        deptId: number
        searchLike: string
        userType: string
        sex: number
    }
    //查询参数
    let tableParams = ref<TableParams>({
        //部门id
        deptId: -1,
        //查询参数
        searchLike: "",
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
    
    
    /*============================页面方法开始============================*/

    /*********部门树*********/
    interface Tree {
        id: number,
        label: string,
        children?: Tree[]
    }
    const deptList = ref<Tree[]>()
    //点击回调
    const handleNodeClick = (data: Tree) => {
        tableParams.value.deptId = data.id;
        search();
    }

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
    /*********添加用户*********/
    //弹出框是否显示
    let dialogFormAddUser = ref(false);
    //表单元素宽度
    let addUserFormLabelWidth = ref("80px");
    //添加用户表单初始值
    const initialAddUserForm = {
        //登陆账号
        username: "",
        //昵称
        nickname: "",
        //邮箱
        email: "",
        //手机号
        phonenumber: "",
        //性别
        sex: -1,
        //备注
        remark: "",
    };
    //添加用户表单对象
    const addUserForm = ref({ ...initialAddUserForm });
    //添加用户表单对象
    const addUserFormRef = ref<FormInstance>()
    //添加用户表单验证规则
    const addUserFormRules = reactive<FormRules>({
        username:[
            { required: true, message: '请填写登陆账号', trigger: 'blur' }
        ],
        nickname:[
            { required: true, message: '请填写昵称', trigger: 'blur' }
        ],
        email:[
            { required: true, message: '请填写邮箱', trigger: 'blur' },
            { 
                type: 'email', // 内置邮箱格式验证[3,11](@ref)
                message: '请输入有效的邮箱地址（如：user@example.com）', 
                trigger: ['blur', 'change'] 
            },
            { 
                pattern: /^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/, // 更严格的正则[7,8](@ref)
                message: '邮箱格式不合法（需包含@和有效域名）',
                trigger: 'blur'
            }
        ],
        phonenumber:[
            { required: true, message: '请填写手机号', trigger: 'blur' },
            { 
                pattern: /^1[3-9]\d{9}$/, // 中国大陆手机号正则[12,13](@ref)
                message: '手机号格式错误（需以1开头且为11位数字）', 
                trigger: ['blur', 'change'] 
            }
        ],
        sex:[
            { pattern: /^(?!-1$|null$).*/, message: '请选择性别', trigger: 'blur' },
            { required: true, message: '请选择性别', trigger: 'blur' }
        ],
        remark:[
            { required: true, message: '请填写备注', trigger: 'blur' }
        ],
    })
    //打开弹框
    const openAddUserDialog = () => {
        dialogFormAddUser.value = true;
    }
    //实现添加
    const addUserConfirm = async (formEl: FormInstance | undefined) => {
        if (!formEl) return
        await formEl.validate(async (valid, fields) => {
            if (valid) {
                //调用添加方法
                let result = await addUser(addUserForm.value);
                if(result.code == 200){
                    ElMessage({
                        message: '添加成功',
                        type: 'success',
                    })
                    //关闭弹框
                    dialogFormAddUser.value = false;
                    //表单值恢复为初始值
                    addUserForm.value = { ...initialAddUserForm };
                    //重新加载表单
                    search();
                }
            }
        })
    };
    //取消弹框
    const addUserCancel = async () => {
        //关闭弹框
        dialogFormAddUser.value = false;
        //表单值恢复为初始值
        addUserForm.value = { ...initialAddUserForm };
    };
    /*********编辑用户*********/
    //弹出框是否显示
    let dialogFormModifyUser = ref(false);
    //表单元素宽度
    let modifyUserFormLabelWidth = ref("80px");
    //编辑用户表单初始值
    const initialModifyUserForm = {
        //用户标识
        userId: 0,
        //昵称
        nickname: "",
        //邮箱
        email: "",
        //手机号
        phonenumber: "",
        //性别
        sex: -1,
        //备注
        remark: "",
    };
    //编辑用户表单对象
    const modifyUserForm = ref({ ...initialModifyUserForm });
    //编辑用户表单对象
    const modifyUserFormRef = ref<FormInstance>()
    //编辑用户表单验证规则
    const modifyUserFormRules = reactive<FormRules>({
        nickname:[
            { required: true, message: '请填写昵称', trigger: 'blur' }
        ],
        email:[
            { required: true, message: '请填写邮箱', trigger: 'blur' },
            { 
                type: 'email', // 内置邮箱格式验证[3,11](@ref)
                message: '请输入有效的邮箱地址（如：user@example.com）', 
                trigger: ['blur', 'change'] 
            },
            { 
                pattern: /^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/, // 更严格的正则[7,8](@ref)
                message: '邮箱格式不合法（需包含@和有效域名）',
                trigger: 'blur'
            }
        ],
        phonenumber:[
            { required: true, message: '请填写手机号', trigger: 'blur' },
            { 
                pattern: /^1[3-9]\d{9}$/, // 中国大陆手机号正则[12,13](@ref)
                message: '手机号格式错误（需以1开头且为11位数字）', 
                trigger: ['blur', 'change'] 
            }
        ],
        sex:[
            { pattern: /^(?!-1$|null$).*/, message: '请选择性别', trigger: 'blur' },
            { required: true, message: '请选择性别', trigger: 'blur' }
        ],
        remark:[
            { required: true, message: '请填写备注', trigger: 'blur' }
        ],
    })
    //打开弹框
    const openModifyUserDialog = (index: number, row: any) => {
        dialogFormModifyUser.value = true;
        modifyUserForm.value.userId = row.userId;
        modifyUserForm.value.nickname = row.nickname;
        modifyUserForm.value.email = row.email;
        modifyUserForm.value.phonenumber = row.phonenumber;
        modifyUserForm.value.sex = row.sex;
        modifyUserForm.value.remark = row.remark;
    }
    //实现修改
    const modifyUserConfirm = async (formEl: FormInstance | undefined) => {
        if (!formEl) return
        await formEl.validate(async (valid, fields) => {
            if (valid) {
                //调用修改方法
                let result = await modifyUser(modifyUserForm.value);
                if(result.code == 200){
                    ElMessage({
                        message: '修改成功',
                        type: 'success',
                    })
                    //关闭弹框
                    dialogFormModifyUser.value = false;
                    //表单值恢复为初始值
                    modifyUserForm.value = { ...initialModifyUserForm };
                    //重新加载表单
                    search();
                }
            }
        })
    };
    //取消弹框
    const modifyUserCancel = async () => {
        //关闭弹框
        dialogFormModifyUser.value = false;
        //表单值恢复为初始值
        modifyUserForm.value = { ...initialModifyUserForm };
    };
    /*********删除用户*********/
    const handleDeleteUser = async(index: number, row: any) => {
        //调用删除方法
        try {
            let result = await deleteUser(row.userId);
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
    /*********重置密码*********/
    //弹出框是否显示
    let dialogFormResetPassword = ref(false);
    //表单元素宽度
    let resetPasswordFormLabelWidth = ref("80px");
    //重置密码表单初始值
    const initialResetPasswordForm = {
        //用户标识
        userId: 0,
        //新密码
        password: "",
    };
    //重置密码表单对象
    const resetPasswordForm = ref({ ...initialResetPasswordForm });
    //重置密码表单对象
    const resetPasswordFormRef = ref<FormInstance>()
    //重置密码表单验证规则
    const resetPasswordFormRules = reactive<FormRules>({
        password:[
            { required: true, message: '请填写密码', trigger: 'blur' },
            { min: 6, message: '密码长度最小6位，请确认', trigger: 'blur' },
        ]
    })
    //打开弹框
    const openResetPasswordDialog = (index: number, row: any) => {
        dialogFormResetPassword.value = true;
        resetPasswordForm.value.userId = row.userId;
    }
    //实现修改
    const resetPasswordConfirm = async (formEl: FormInstance | undefined) => {
        if (!formEl) return
        await formEl.validate(async (valid, fields) => {
            if (valid) {
                //调用修改方法
                let result = await resetPassword(resetPasswordForm.value);
                if(result.code == 200){
                    ElMessage({
                        message: '修改成功',
                        type: 'success',
                    })
                    //关闭弹框
                    dialogFormResetPassword.value = false;
                    //表单值恢复为初始值
                    resetPasswordForm.value = { ...initialResetPasswordForm };
                    //重新加载表单
                    search();
                }
            }
        })
    };
    //取消弹框
    const resetPasswordCancel = async () => {
        //关闭弹框
        dialogFormResetPassword.value = false;
        //表单值恢复为初始值
        resetPasswordForm.value = { ...initialResetPasswordForm };
    };
    /*============================页面方法结束============================*/


    /*============================生命周期钩子开始============================*/
    // 组件加载完成后执行
    // 初始化表格数据
    onMounted(async () => {
        let resultV2 = await getDeptList();
        if(resultV2.code == 200){
            deptList.value = resultV2.deptList;
        }

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

    .btns{
        margin-bottom: 10px;
    }
</style>
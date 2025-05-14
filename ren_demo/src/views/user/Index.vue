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
        <el-table :data="tableData" stripe @sort-change="tableSortHandle">
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
            <el-table-column prop="loginDateStr" label="最后登录时间" width="180" show-overflow-tooltip sortable="custom" ></el-table-column>
            <el-table-column prop="loginIp" label="最后登录IP" width="140" show-overflow-tooltip></el-table-column>
            <el-table-column prop="remark" label="备注" width="200" show-overflow-tooltip></el-table-column>
            <el-table-column fixed="right" label="操作" min-width="120">
                <template #default="scope">
                    <el-button link type="primary" size="small" @click="openModifyUserDialog(scope.$index, scope.row)" v-if="scope.row.username != 'admin'">修改</el-button>
                    <el-button link type="primary" size="small" @click="handleDeleteUser(scope.$index, scope.row)" v-if="scope.row.username != 'admin'">删除</el-button>
                    <el-button link type="primary" size="small" @click="openResetPasswordDialog(scope.$index, scope.row)" v-if="scope.row.username != 'admin'">重置密码</el-button>
                </template>
            </el-table-column>
        </el-table>
        <Pagination
            :total="total"
            v-model:page="tableParams.pageNum"
            v-model:limit="tableParams.pageSize"
            @pagination="getList"
        />
    </el-main>
    <el-dialog :title="addOrModifyTag == 1 ? '添加用户' : '修改用户'" v-model="dialogFormAddOrModifyUser" width="500px">
        <el-form :model="addOrModifyUserForm" :rules="addOrModifyUserFormRules" ref="addOrModifyUserFormRef">
            <el-form-item label="登陆账号" :label-width="addOrModifyUserFormLabelWidth" prop="username" v-if="addOrModifyTag == 1">
                <el-input v-model="addOrModifyUserForm.username" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="昵称" :label-width="addOrModifyUserFormLabelWidth" prop="nickname">
                <el-input v-model="addOrModifyUserForm.nickname" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="邮箱" :label-width="addOrModifyUserFormLabelWidth" prop="email">
                <el-input v-model="addOrModifyUserForm.email" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="手机号" :label-width="addOrModifyUserFormLabelWidth" prop="phonenumber">
                <el-input v-model="addOrModifyUserForm.phonenumber" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="性别" :label-width="addOrModifyUserFormLabelWidth" prop="sex">
                <el-select
                    v-model="addOrModifyUserForm.sex"
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
            <el-form-item label="所属部门" :label-width="addOrModifyUserFormLabelWidth" prop="deptId">
                <el-tree-select
                    v-model="addOrModifyUserForm.deptId"
                    :data="addOrModifyDeptList"
                    check-strictly
                    :render-after-expand="false"
                    :empty-values="[null,undefined,-1]"
                    :value-on-clear="-1"
                    placeholder="请选择"
                    node-key="id"
                    :props="{
                        value: 'id',
                        label: 'label',
                        children: 'children',
                        disabled: 'disabled'
                    }"
                    show-checkbox
                />
            </el-form-item>
            <el-form-item label="角色" :label-width="addOrModifyUserFormLabelWidth" prop="roleIdArr">
                <el-select
                v-model="addOrModifyUserForm.roleIdArr"
                multiple
                collapse-tags
                collapse-tags-tooltip
                placeholder="请选择"
                >
                    <el-option
                        v-for="item in addOrModifyRoleList"
                        :key="item.roleId"
                        :label="item.roleName"
                        :value="item.roleId"
                    />
                </el-select>
            </el-form-item>
            <el-form-item label="备注" :label-width="addOrModifyUserFormLabelWidth" prop="remark">
                <el-input v-model="addOrModifyUserForm.remark" autocomplete="off"></el-input>
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="addOrModifyUserCancel">取 消</el-button>
                <el-button type="primary" @click="addOrModifyUserConfirm(addOrModifyUserFormRef)">
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
    import {getUserList,resetPassword,deleteUser,modifyUser,addUser,getDeptList,getRoleList,getUserInfo} from '@/api/user/index'
    /*============================通用参数开始============================*/
    //表格数据
    let tableData = ref([]);
    interface TableParams {
        deptId: number
        searchLike: string
        userType: string
        sex: number
        pageNum: number
        pageSize: number
        orderByColumn:string
        orderByWay:string
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
        //当前页
        pageNum:1,
        //每页显示多少条
        pageSize:10,
        //排序字段
        orderByColumn:"",
        //排序方式
        orderByWay:""
    });
    //查询表单
    const searchFormRef = ref<FormInstance>()
    //表单验证规则（即使用不到，为了重置方法，也需要写）
    const rules = reactive<FormRules<TableParams>>({
    })
    //添加或修改(1添加，2修改)
    let addOrModifyTag = ref(1)
    //总数据数量
    const total = ref(0)
    //总页数
    const totalPage = ref(1)
    /*============================通用参数结束============================*/
    
    
    /*============================页面方法开始============================*/

    /*********部门树*********/
    interface Tree {
        id: number,
        label: string,
        children?: Tree[]
    }
    //用于查询的部门列表
    const deptList = ref<Tree[]>()
    //点击回调
    const handleNodeClick = (data: Tree) => {
        tableParams.value.deptId = data.id;
        search();
    }
    //页面获取列表方法
    const getList = async () => {
        console.info(tableParams.value.pageNum)
        console.info(tableParams.value.pageSize)
        let result = await getUserList(tableParams.value);
        if(result.code == 200){
            tableData.value = result.rows;
            tableParams.value.pageNum = result.pageNum;
            tableParams.value.pageSize = result.pageSize;
            total.value = result.total;
            totalPage.value = result.totalPage;
        }else{
            ElMessage.error(result.msg);
        }
    }
    //页面搜索方法
    const search = async () => {
        tableParams.value.pageNum = 1;
        getList();
    }
    //重置
    const resetForm = (formEl: FormInstance | undefined) => {
        if (!formEl) return
        formEl.resetFields()
    }
    /*********添加修改用户*********/
    //弹出框是否显示
    let dialogFormAddOrModifyUser = ref(false);
    //表单元素宽度
    let addOrModifyUserFormLabelWidth = ref("80px");
    //添加用户表单初始值
    const initialAddOrModifyUserForm = {
        //用户标识
        userId: 0,
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
        //部门标识
        deptId: -1,
        //角色标识
        roleIdArr: [],
    };
    //添加用户表单对象
    const addOrModifyUserForm = ref({ ...initialAddOrModifyUserForm });
    //用于添加和修改的部门列表
    const addOrModifyDeptList = ref<Tree[]>()
    //用于添加和修改的角色列表
    const addOrModifyRoleList = ref()
    //添加用户表单对象
    const addOrModifyUserFormRef = ref<FormInstance>()
    //添加用户表单验证规则
    const addOrModifyUserFormRules = reactive<FormRules>({
        deptId:[
            { pattern: /^(?!-1$|null$).*/, message: '请选择上级部门', trigger: 'blur' },
            { required: true, message: '请选择上级部门', trigger: 'blur' }
        ],
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
    //打开添加弹框
    const openAddUserDialog = async () => {
        //表单值恢复为初始值
        addOrModifyUserForm.value = { ...initialAddOrModifyUserForm };
        //清除验证状态
        addOrModifyUserFormRef.value?.clearValidate();
        addOrModifyTag.value = 1;
        dialogFormAddOrModifyUser.value = true;
        try {
            let result = await getDeptList();
            if(result.code == 200){
                addOrModifyDeptList.value = result.deptList;
            }else{
                ElMessage.error(result.msg);
            }
        } catch (error) {
            ElMessage.error('获取部门列表失败');
        }
        try {
            let result = await getRoleList();
            if(result.code == 200){
                addOrModifyRoleList.value = result.roleList;
            }else{
                ElMessage.error(result.msg);
            }
        } catch (error) {
            ElMessage.error('获取角色列表失败');
        }
        addOrModifyUserForm.value.userId = 0;
    }
    //打开修改弹框
    const openModifyUserDialog = async (index: number, row: any) => {
        //表单值恢复为初始值
        addOrModifyUserForm.value = { ...initialAddOrModifyUserForm };
        //清除验证状态
        addOrModifyUserFormRef.value?.clearValidate();
        addOrModifyTag.value = 2;
        dialogFormAddOrModifyUser.value = true;
        try {
            let result = await getDeptList();
            if(result.code == 200){
                addOrModifyDeptList.value = result.deptList;
                // 等待树形组件渲染完成
                await nextTick();
                // 设置默认选中值
                addOrModifyUserForm.value.deptId = row.deptId;
            }else{
                ElMessage.error(result.msg);
            }
        } catch (error) {
            ElMessage.error('获取部门列表失败');
        }
        try {
            let result = await getRoleList();
            if(result.code == 200){
                addOrModifyRoleList.value = result.roleList;
            }else{
                ElMessage.error(result.msg);
            }
        } catch (error) {
            ElMessage.error('获取角色列表失败');
        }
        try {
            let result = await getUserInfo(row.userId);
            if(result.code == 200){
                addOrModifyUserForm.value.roleIdArr = result.roleIdArr;
            }else{
                ElMessage.error(result.msg);
            }
        } catch (error) {
            ElMessage.error('获取角色列表失败');
        }
        addOrModifyUserForm.value.userId = row.userId;
        addOrModifyUserForm.value.username = row.username;
        addOrModifyUserForm.value.nickname = row.nickname;
        addOrModifyUserForm.value.email = row.email;
        addOrModifyUserForm.value.phonenumber = row.phonenumber;
        addOrModifyUserForm.value.sex = row.sex;
        addOrModifyUserForm.value.remark = row.remark;
    }
    //实现添加或修改
    const addOrModifyUserConfirm = async (formEl: FormInstance | undefined) => {
        if (!formEl) return
        await formEl.validate(async (valid, fields) => {
            if (valid) {
                //调用添加或修改方法
                if(addOrModifyTag.value == 1){
                    let result = await addUser(addOrModifyUserForm.value);
                    if(result.code == 200){
                        ElMessage({
                            message: '添加成功',
                            type: 'success',
                        })
                        //关闭弹框
                        dialogFormAddOrModifyUser.value = false;
                        //表单值恢复为初始值
                        addOrModifyUserForm.value = { ...initialAddOrModifyUserForm };
                        //重新加载表单
                        search();
                    }else{
                        ElMessage.error(result.msg);
                    }
                }else {
                    let result = await modifyUser(addOrModifyUserForm.value);
                    if(result.code == 200){
                        ElMessage({
                            message: '修改成功',
                            type: 'success',
                        })
                        //关闭弹框
                        dialogFormAddOrModifyUser.value = false;
                        //表单值恢复为初始值
                        addOrModifyUserForm.value = { ...initialAddOrModifyUserForm };
                        //重新加载表单
                        search();
                    }else{
                        ElMessage.error(result.msg);
                    }
                }
            }
        })
    };
    //取消弹框
    const addOrModifyUserCancel = async () => {
        //关闭弹框
        dialogFormAddOrModifyUser.value = false;
        //表单值恢复为初始值
        addOrModifyUserForm.value = { ...initialAddOrModifyUserForm };
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
            }else{
                ElMessage.error(result.msg);
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
    //重置密码
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
                }else{
                    ElMessage.error(result.msg);
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
    /*********排序相关*********/
    // 定义前端字段名到数据库字段名的映射
    // 注意，这里只需要定义前端页面与数据库字段名不相同的场景，如数据库名为login_date,而前端页面字段名为loginDateStr
    // 但是，如果仅仅是驼峰与下划线命名不同，可以不定义，如数据库为login_date，而前端页面字段名为loginDate
    // 如果不同且未定义，可能会导致查询失败
    const sortFieldMap = {
        loginDateStr: 'login_date',
    };
    const tableSortHandle = (params: {
        column: any
        prop: keyof typeof sortFieldMap // 告诉ts，从prop中取出来的值，一定是sortFieldMap的键
        order: string
    }) => {
        // 参数说明：
        // - column: 当前列的配置对象
        // - prop: 排序的字段名（对应列的 prop）
        // - order: 排序方式（'ascending' 升序 / 'descending' 降序 / null 默认）

        let orderByColumn = sortFieldMap[params.prop] || params.prop;
        let orderByWay = params.order == null ? "" : params.order == "ascending" ? "asc" : "desc";

        if(orderByColumn != undefined && orderByWay != ""){
            tableParams.value.orderByColumn = orderByColumn;
            tableParams.value.orderByWay = orderByWay;
        }else{
            tableParams.value.orderByColumn = "";
            tableParams.value.orderByWay = "";
        }
        search();
    }
    /*============================页面方法结束============================*/


    /*============================生命周期钩子开始============================*/
    // 组件加载完成后执行
    // 初始化表格数据
    onMounted(async () => {
        let resultV2 = await getDeptList();
        if(resultV2.code == 200){
            deptList.value = resultV2.deptList;
        }
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
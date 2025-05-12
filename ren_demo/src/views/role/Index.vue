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
    <el-dialog :title="addOrModifyTag == 1 ? '添加角色' : '修改角色'" v-model="dialogFormAddOrModifyRole" width="500px">
        <el-form :model="addOrModifyRoleForm" :rules="addOrModifyRoleFormRules" ref="addOrModifyRoleFormRef">
            <el-form-item label="角色名称" :label-width="addOrModifyRoleFormLabelWidth" prop="roleName">
                <el-input v-model="addOrModifyRoleForm.roleName" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="是否停用" :label-width="addOrModifyRoleFormLabelWidth" prop="isStop">
                <el-radio-group v-model="addOrModifyRoleForm.isStop" size="large" fill="#6cf">
                    <el-radio-button label="是" :value="1" />
                    <el-radio-button label="否" :value="0" />
                </el-radio-group>
            </el-form-item>
            <el-form-item label="权限字符" :label-width="addOrModifyRoleFormLabelWidth" prop="roleKey">
                <el-input v-model="addOrModifyRoleForm.roleKey" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="显示顺序" :label-width="addOrModifyRoleFormLabelWidth" prop="roleSort">
                <el-input v-model="addOrModifyRoleForm.roleSort" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="菜单权限" :label-width="addOrModifyRoleFormLabelWidth" v-show="menuPermissionsData">
                <div style="width: 100%;">
                    <el-checkbox v-model="isExpand" label="展开/折叠" size="large" />
                    <el-checkbox v-model="isSelectAll" label="全选/全不选" size="large" />
                    <el-checkbox v-model="isLinkage" label="父子联动" size="large" />
                </div>
                <div style="width: 100%;padding: 2px;border: 1px solid #E5E6E7;">
                    <el-tree
                        ref="menuPermissionsRef"
                        :data="menuPermissionsData"
                        show-checkbox
                        node-key="id"
                        :check-strictly="!isLinkage"
                        :props="{
                            children: 'children',
                            label: 'label',
                        }"
                    />
                </div>
            </el-form-item>
            <el-form-item label="数据权限" :label-width="addOrModifyRoleFormLabelWidth" prop="dataScope">
                <el-select
                    v-model="addOrModifyRoleForm.dataScope"
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
                <div style="width: 100%;" v-show="deptPermissionsData && addOrModifyRoleForm.dataScope == 2">
                    <el-checkbox v-model="isDeptExpand" label="展开/折叠" size="large" />
                    <el-checkbox v-model="isDeptSelectAll" label="全选/全不选" size="large" />
                    <el-checkbox v-model="isDeptLinkage" label="父子联动" size="large" />
                </div>
                <div style="width: 100%;padding: 2px;border: 1px solid #E5E6E7;" v-show="deptPermissionsData && addOrModifyRoleForm.dataScope == 2">
                    <el-tree
                        ref="deptPermissionsRef"
                        :data="deptPermissionsData"
                        show-checkbox
                        node-key="id"
                        :check-strictly="!isDeptLinkage"
                        :props="{
                            children: 'children',
                            label: 'label',
                        }"
                    />
                </div>
            </el-form-item>
            <el-form-item label="备注" :label-width="addOrModifyRoleFormLabelWidth" prop="remark">
                <el-input v-model="addOrModifyRoleForm.remark" autocomplete="off"></el-input>
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="addOrModifyRoleCancel">取 消</el-button>
                <el-button type="primary" @click="addOrModifyRoleConfirm(addOrModifyRoleFormRef)">
                    确 定
                </el-button>
            </span>
        </template>
    </el-dialog>
</template>
  
<script setup lang="ts" name="role">
    import { ref,onMounted } from 'vue'
    import type { FormInstance,FormRules } from 'element-plus'
    import { ElMessage,ElTree } from 'element-plus'
    import type Node from 'element-plus/es/components/tree/src/model/node'
    import {getRoleList,addRole,modifyRole,deleteRole,getMenuList,getDeptList} from '@/api/role/index'
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
    //添加或修改(1添加，2修改)
    let addOrModifyTag = ref(1)
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
    let dialogFormAddOrModifyRole = ref(false);
    //表单元素宽度
    let addOrModifyRoleFormLabelWidth = ref("80px");
    //添加角色表单初始值
    const initialAddOrModifyRoleForm = {
        //角色主键
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
        //菜单Id列表
        menuIds: [],
        //部门Id列表
        deptIds: [],
    };
    //添加角色表单对象
    const addOrModifyRoleForm = ref({ ...initialAddOrModifyRoleForm });
    //添加角色表单对象
    const addOrModifyRoleFormRef = ref<FormInstance>()
    //添加角色表单验证规则
    const addOrModifyRoleFormRules = reactive<FormRules>({
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

    //菜单权限相关
    let isExpand = ref(false);
    let isSelectAll = ref(false);
    let isLinkage = ref(false);
    let menuPermissionsRef = ref<InstanceType<typeof ElTree>>();
    let menuPermissionsData = ref();
    // 监听展开/折叠 
    const isExpandWatch = watch(isExpand,(newValue,oldValue)=>{
        // 获取树的所有节点
        const nodes = menuPermissionsRef.value!.store.nodesMap;
        if (newValue) {
            Object.values(nodes).forEach(node => (node as Node).expand());
        } else {
            Object.values(nodes).forEach(node => (node as Node).collapse());
        }
    })
    // 监听全选/全不选 
    const isSelectAllWatch = watch(isSelectAll,(newValue,oldValue)=>{
        // 获取树的所有节点
        const nodes = menuPermissionsRef.value!.store.nodesMap;
        //把所有节点中的id重新收集为一个数字数组
        const ids: number[] = Object.values(nodes).map(node => node.data.id);
        if (newValue) {
            menuPermissionsRef.value!.setCheckedKeys(ids, false)
        } else {
            menuPermissionsRef.value!.setCheckedKeys([]);
        }
    })

    //部门权限相关
    let isDeptExpand = ref(false);
    let isDeptSelectAll = ref(false);
    let isDeptLinkage = ref(false);
    let deptPermissionsRef = ref<InstanceType<typeof ElTree>>();
    let deptPermissionsData = ref();
    // 监听展开/折叠 
    const isDeptExpandWatch = watch(isDeptExpand,(newValue,oldValue)=>{
        // 获取树的所有节点
        const nodes = deptPermissionsRef.value!.store.nodesMap;
        if (newValue) {
            Object.values(nodes).forEach(node => (node as Node).expand());
        } else {
            Object.values(nodes).forEach(node => (node as Node).collapse());
        }
    })
    // 监听全选/全不选 
    const isDeptSelectAllWatch = watch(isDeptSelectAll,(newValue,oldValue)=>{
        // 获取树的所有节点
        const nodes = deptPermissionsRef.value!.store.nodesMap;
        //把所有节点中的id重新收集为一个数字数组
        const ids: number[] = Object.values(nodes).map(node => node.data.id);
        if (newValue) {
            deptPermissionsRef.value!.setCheckedKeys(ids, false)
        } else {
            deptPermissionsRef.value!.setCheckedKeys([]);
        }
    })

    //打开添加弹框
    const openAddRoleDialog = async () => {
        //弹出框设置为添加弹框
        addOrModifyTag.value = 1;
        //表单值恢复为初始值
        addOrModifyRoleForm.value = { ...initialAddOrModifyRoleForm };
        //添加表单的主键设置为0
        addOrModifyRoleForm.value.roleId = 0;
        //显示弹出框
        dialogFormAddOrModifyRole.value = true;
        //树形列表相关设置为默认值
        isExpand.value = false;
        isSelectAll.value = false;
        isLinkage.value = false;
        isDeptExpand.value = false;
        isDeptSelectAll.value = false;
        isDeptLinkage.value = false;
        //获取菜单树列表
        try {
            let result = await getMenuList();
            if(result.code == 200){
                menuPermissionsData.value = result.menuList;
                // 等待树形组件渲染完成
                await nextTick();
                menuPermissionsRef.value!.setCheckedKeys([], false)
            }
        } catch (error) {
            ElMessage.error('获取菜单权限列表失败');
        }
        //获取部门树列表
        try {
            let result = await getDeptList();
            if(result.code == 200){
                deptPermissionsData.value = result.deptList;
                // 等待树形组件渲染完成
                await nextTick();
                deptPermissionsRef.value!.setCheckedKeys([], false)
            }
        } catch (error) {
            ElMessage.error('获取部门权限列表失败');
        }
    }
    //打开修改弹框
    const openModifyRoleDialog = async (index: number, row: any) => {
        //弹出框设置为修改弹框
        addOrModifyTag.value = 2;
        //显示弹出框
        dialogFormAddOrModifyRole.value = true;
        //树形列表相关设置为默认值
        isExpand.value = false;
        isSelectAll.value = false;
        isLinkage.value = false;
        isDeptExpand.value = false;
        isDeptSelectAll.value = false;
        isDeptLinkage.value = false;
        //获取菜单树列表
        try {
            let result = await getMenuList(row.roleId);
            if(result.code == 200){
                menuPermissionsData.value = result.menuList;
                // 等待树形组件渲染完成
                await nextTick();
                if(result.menuIdArr != null){
                    menuPermissionsRef.value!.setCheckedKeys(result.menuIdArr, false)
                }else{
                    menuPermissionsRef.value!.setCheckedKeys([], false)
                }
            }
        } catch (error) {
            ElMessage.error('获取菜单权限列表失败');
        }
        //获取部门树列表
        try {
            let result = await getDeptList(row.roleId);
            if(result.code == 200){
                deptPermissionsData.value = result.deptList;
                // 等待树形组件渲染完成
                await nextTick();
                if(result.deptIdArr != null){
                    deptPermissionsRef.value!.setCheckedKeys(result.deptIdArr, false)
                }else{
                    deptPermissionsRef.value!.setCheckedKeys([], false)
                }
            }
        } catch (error) {
            ElMessage.error('获取部门权限列表失败');
        }
        //设置弹出框中相关值
        addOrModifyRoleForm.value.roleId = row.roleId;
        addOrModifyRoleForm.value.roleName = row.roleName;
        addOrModifyRoleForm.value.isStop = row.isStop;
        addOrModifyRoleForm.value.roleKey = row.roleKey;
        addOrModifyRoleForm.value.roleSort = row.roleSort;
        addOrModifyRoleForm.value.dataScope = row.dataScope;
        addOrModifyRoleForm.value.remark = row.remark;
    }
    //实现添加或修改
    const addOrModifyRoleConfirm = async (formEl: FormInstance | undefined) => {
        if (!formEl) return
        await formEl.validate(async (valid, fields) => {
            if (valid) {
                (addOrModifyRoleForm.value.menuIds as Number[]) = menuPermissionsRef.value!.getCheckedKeys(false).map(key => Number(key));
                (addOrModifyRoleForm.value.deptIds as Number[]) = deptPermissionsRef.value!.getCheckedKeys(false).map(key => Number(key));
                //调用添加或修改方法
                if(addOrModifyTag.value == 1){
                    let result = await addRole(addOrModifyRoleForm.value);
                    if(result.code == 200){
                        ElMessage({
                            message: '添加成功',
                            type: 'success',
                        })
                        //关闭弹框
                        dialogFormAddOrModifyRole.value = false;
                        //表单值恢复为初始值
                        addOrModifyRoleForm.value = { ...initialAddOrModifyRoleForm };
                        //重新加载表单
                        search();
                    }
                }else{
                    let result = await modifyRole(addOrModifyRoleForm.value);
                    if(result.code == 200){
                        ElMessage({
                            message: '修改成功',
                            type: 'success',
                        })
                        //关闭弹框
                        dialogFormAddOrModifyRole.value = false;
                        //表单值恢复为初始值
                        addOrModifyRoleForm.value = { ...initialAddOrModifyRoleForm };
                        //重新加载表单
                        search();
                    }
                }
            }
        })
    };
    //取消弹框
    const addOrModifyRoleCancel = async () => {
        //关闭弹框
        dialogFormAddOrModifyRole.value = false;
        //表单值恢复为初始值
        addOrModifyRoleForm.value = { ...initialAddOrModifyRoleForm };
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
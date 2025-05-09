<template>
    <el-form ref="searchFormRef" :rules="rules" :inline="true" :model="tableParams" class="demo-form-inline">
        <el-form-item label="查询" prop="searchLike">
            <el-input v-model="tableParams.searchLike" placeholder="部门名称" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="search">搜索</el-button>
            <el-button @click="resetForm(searchFormRef)">重置</el-button>
        </el-form-item>
    </el-form>
    <el-button type="primary" @click="openAddDeptDialog">添加部门</el-button>
    <el-table
      :data="tableData"
      style="width: 100%; margin-bottom: 20px"
      row-key="deptId"
      :border="true"
      default-expand-all
      :tree-props="{ children: 'children'}"
    >
      <el-table-column prop="deptName" label="部门名称" sortable />
      <el-table-column prop="orderNum" label="排序" sortable />
      <el-table-column prop="isStop" label="是否停用" sortable >
        <template #default="item">
            <el-tag v-if="item.row.isStop == 0">否</el-tag>
            <el-tag v-if="item.row.isStop == 1">是</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTimeStr" label="创建时间" sortable />
      <el-table-column fixed="right" label="操作" min-width="120">
            <template #default="scope">
                <el-button link type="primary" size="small" @click="openModifyDeptDialog(scope.$index, scope.row)">修改</el-button>
                <el-button link type="primary" size="small" @click="openSubAddDeptDialog(scope.$index, scope.row)">添加子级部门</el-button>
                <el-button link type="primary" size="small" @click="handleDeleteDept(scope.$index, scope.row)">删除</el-button>
            </template>
        </el-table-column>
    </el-table>
    <el-dialog title="添加部门" v-model="dialogFormAddDept" width="500px">
        <el-form :model="addDeptForm" :rules="addDeptFormRules" ref="addDeptFormRef">
            <el-form-item label="上级部门" :label-width="addDeptFormLabelWidth" prop="parentId">
                <el-tree-select
                    v-model="addDeptForm.parentId"
                    :data="parentDeptList"
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
            <el-form-item label="部门名称" :label-width="addDeptFormLabelWidth" prop="deptName">
                <el-input v-model="addDeptForm.deptName" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="显示顺序" :label-width="addDeptFormLabelWidth" prop="orderNum">
                <el-input v-model="addDeptForm.orderNum" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="负责人" :label-width="addDeptFormLabelWidth" prop="leader">
                <el-input v-model="addDeptForm.leader" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="联系电话" :label-width="addDeptFormLabelWidth" prop="phone">
                <el-input v-model="addDeptForm.phone" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="邮箱" :label-width="addDeptFormLabelWidth" prop="email">
                <el-input v-model="addDeptForm.email" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="是否停用" :label-width="addDeptFormLabelWidth" prop="isStop">
                <el-radio-group v-model="addDeptForm.isStop" size="large" fill="#6cf">
                    <el-radio-button label="是" :value="1" />
                    <el-radio-button label="否" :value="0" />
                </el-radio-group>
            </el-form-item>
            <el-form-item label="备注" :label-width="addDeptFormLabelWidth" prop="remark">
                <el-input v-model="addDeptForm.remark" autocomplete="off"></el-input>
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="addDeptCancel">取 消</el-button>
                <el-button type="primary" @click="addDeptConfirm(addDeptFormRef)">
                    确 定
                </el-button>
            </span>
        </template>
    </el-dialog>
    <el-dialog title="编辑部门" v-model="dialogFormModifyDept" width="500px">
        <el-form :model="modifyDeptForm" :rules="modifyDeptFormRules" ref="modifyDeptFormRef">
            <el-form-item label="上级部门" :label-width="modifyDeptFormLabelWidth" prop="parentId">
                <el-tree-select
                    v-model="modifyDeptForm.parentId"
                    :data="parentDeptList"
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
            <el-form-item label="部门名称" :label-width="modifyDeptFormLabelWidth" prop="deptName">
                <el-input v-model="modifyDeptForm.deptName" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="显示顺序" :label-width="modifyDeptFormLabelWidth" prop="orderNum">
                <el-input v-model="modifyDeptForm.orderNum" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="负责人" :label-width="modifyDeptFormLabelWidth" prop="leader">
                <el-input v-model="modifyDeptForm.leader" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="联系电话" :label-width="modifyDeptFormLabelWidth" prop="phone">
                <el-input v-model="modifyDeptForm.phone" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="邮箱" :label-width="modifyDeptFormLabelWidth" prop="email">
                <el-input v-model="modifyDeptForm.email" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="是否停用" :label-width="modifyDeptFormLabelWidth" prop="isStop">
                <el-radio-group v-model="modifyDeptForm.isStop" size="large" fill="#6cf">
                    <el-radio-button label="是" :value="1" />
                    <el-radio-button label="否" :value="0" />
                </el-radio-group>
            </el-form-item>
            <el-form-item label="备注" :label-width="modifyDeptFormLabelWidth" prop="remark">
                <el-input v-model="modifyDeptForm.remark" autocomplete="off"></el-input>
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="modifyDeptCancel">取 消</el-button>
                <el-button type="primary" @click="modifyDeptConfirm(modifyDeptFormRef)">
                    确 定
                </el-button>
            </span>
        </template>
    </el-dialog>
</template>

<script setup lang="ts" name="dept">
    import { ref,onMounted,nextTick } from 'vue'
    import type { FormInstance,FormRules } from 'element-plus'
    import { ElMessage } from 'element-plus'
    import {getDeptList,addDept,modifyDept, deleteDept, getParentDeptList} from '@/api/dept/index'
    /*============================通用参数开始============================*/
    interface Dept {
        deptId: number
        parentId: number
        deptName: string
        orderNum: number
        leader: string
        phone: string
        email: string
        isStop: number
        isDel: number
        createTimeStr: string
        updateTimeStr: string
        remark: string
        children?: Dept[]
    }
    //表格数据
    let tableData = ref<Dept[]>([]);
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
    //添加编辑部门下拉框树形列表
    let parentDeptList = ref()
    /*============================通用参数结束============================*/


    /*============================页面方法开始============================*/
    //页面搜索方法
    const search = async () => {
        let result = await getDeptList(tableParams.value);
        if(result.code == 200){
            tableData.value = result.deptList;
        }
    }
    //重置
    const resetForm = (formEl: FormInstance | undefined) => {
        if (!formEl) return
        formEl.resetFields()
    }
    /*********添加部门*********/
    //弹出框是否显示
    let dialogFormAddDept = ref(false);
    //表单元素宽度
    let addDeptFormLabelWidth = ref("80px");
    //添加部门表单初始值
    const initialAddDeptForm = {
        //上级部门标识
        parentId: -1,
        //部门名称
        deptName: "",
        //显示顺序
        orderNum: "",
        //负责人
        leader: "",
        //联系电话
        phone: "",
        //邮箱
        email: "",
        //是否停用
        isStop: 0,
        //备注
        remark: "",
    };
    //添加部门表单对象
    const addDeptForm = ref({ ...initialAddDeptForm });
    //添加部门表单对象
    const addDeptFormRef = ref<FormInstance>()
    //添加部门表单验证规则
    const addDeptFormRules = reactive<FormRules>({
        parentId:[
            { pattern: /^(?!-1$|null$).*/, message: '请选择上级部门', trigger: 'blur' },
            { required: true, message: '请选择上级部门', trigger: 'blur' }
        ],
        deptName:[
            { required: true, message: '请填写部门名称', trigger: 'blur' }
        ],
        orderNum:[
            { required: true, message: '请填写显示顺序', trigger: 'blur' },
            { 
                pattern: /^[1-9]\d*$/, 
                message: '只能输入正整数', 
                trigger: ['blur', 'change']
            }
        ],
        leader:[
            { required: true, message: '请填写负责人', trigger: 'blur' }
        ],
        phone:[
            { required: true, message: '请填写联系电话', trigger: 'blur' },
            { 
                pattern: /^1[3-9]\d{9}$/, // 中国大陆手机号正则[12,13](@ref)
                message: '手机号格式错误（需以1开头且为11位数字）', 
                trigger: ['blur', 'change'] 
            }
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
        remark:[
            { required: true, message: '请填写备注', trigger: 'blur' }
        ],
    })
    //打开弹框
    const openAddDeptDialog = async() => {
        //获取上级部门列表
        try {
            let result = await getParentDeptList();
            if(result.code == 200){
                parentDeptList.value = result.parentDeptList;
            }
        } catch (error) {
            ElMessage.error('获取上级部门列表失败');
        }
        dialogFormAddDept.value = true;
    }
    //实现添加
    const addDeptConfirm = async (formEl: FormInstance | undefined) => {
        if (!formEl) return
        await formEl.validate(async (valid, fields) => {
            if (valid) {
                //调用添加方法
                let result = await addDept(addDeptForm.value);
                if(result.code == 200){
                    ElMessage({
                        message: '添加成功',
                        type: 'success',
                    })
                    //关闭弹框
                    dialogFormAddDept.value = false;
                    //表单值恢复为初始值
                    addDeptForm.value = { ...initialAddDeptForm };
                    //重新加载表单
                    search();
                }
            }
        })
    };
    //取消弹框
    const addDeptCancel = async () => {
        //关闭弹框
        dialogFormAddDept.value = false;
        //表单值恢复为初始值
        addDeptForm.value = { ...initialAddDeptForm };
    };

    /*********添加子级部门*********/
    const openSubAddDeptDialog = async (index: number, row: any) => {
        //获取上级部门列表
        try {
            let result = await getParentDeptList();
            if(result.code == 200){
                parentDeptList.value = result.parentDeptList;
                // 等待树形组件渲染完成
                await nextTick();
                // 设置默认选中值
                addDeptForm.value.parentId = row.deptId;
            }
        } catch (error) {
            ElMessage.error('获取上级部门列表失败');
        }

        dialogFormAddDept.value = true;
    }

    /*********编辑部门*********/
    //弹出框是否显示
    let dialogFormModifyDept = ref(false);
    //表单元素宽度
    let modifyDeptFormLabelWidth = ref("80px");
    //编辑部门表单初始值
    const initialModifyDeptForm = {
        //上级部门标识
        parentId: -1,
        //部门Id
        deptId: 0,
        //部门名称
        deptName: "",
        //显示顺序
        orderNum: "",
        //负责人
        leader: "",
        //联系电话
        phone: "",
        //邮箱
        email: "",
        //是否停用
        isStop: 0,
        //备注
        remark: "",
    };
    //编辑部门表单对象
    const modifyDeptForm = ref({ ...initialModifyDeptForm });
    //编辑部门表单对象
    const modifyDeptFormRef = ref<FormInstance>()
    //编辑部门表单验证规则
    const modifyDeptFormRules = reactive<FormRules>({
        parentId:[
            { pattern: /^(?!-1$|null$).*/, message: '请选择上级部门', trigger: 'blur' },
            { required: true, message: '请选择上级部门', trigger: 'blur' }
        ],
        deptName:[
            { required: true, message: '请填写部门名称', trigger: 'blur' }
        ],
        orderNum:[
            { required: true, message: '请填写显示顺序', trigger: 'blur' },
            { 
                pattern: /^[0-9]\d*$/, 
                message: '只能输入正整数', 
                trigger: ['blur', 'change']
            }
        ],
        leader:[
            { required: true, message: '请填写负责人', trigger: 'blur' }
        ],
        phone:[
            { required: true, message: '请填写联系电话', trigger: 'blur' },
            { 
                pattern: /^1[3-9]\d{9}$/, // 中国大陆手机号正则[12,13](@ref)
                message: '手机号格式错误（需以1开头且为11位数字）', 
                trigger: ['blur', 'change'] 
            }
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
        remark:[
            { required: true, message: '请填写备注', trigger: 'blur' }
        ],
    })
    //打开弹框
    const openModifyDeptDialog = async (index: number, row: any) => {
        //获取上级部门列表
        try {
            let result = await getParentDeptList(row.deptId);
            if(result.code == 200){
                parentDeptList.value = result.parentDeptList;

                // 等待树形组件渲染完成
                await nextTick();
                
                // 设置默认选中值
                modifyDeptForm.value.parentId = row.parentId;
            }
        } catch (error) {
            ElMessage.error('获取上级部门列表失败');
        }
        dialogFormModifyDept.value = true;
        modifyDeptForm.value.deptId = row.deptId;
        modifyDeptForm.value.deptName = row.deptName;
        modifyDeptForm.value.orderNum = row.orderNum;
        modifyDeptForm.value.leader = row.leader;
        modifyDeptForm.value.phone = row.phone;
        modifyDeptForm.value.email = row.email;
        modifyDeptForm.value.isStop = row.isStop;
        modifyDeptForm.value.remark = row.remark;
    }
    //实现修改
    const modifyDeptConfirm = async (formEl: FormInstance | undefined) => {
        if (!formEl) return
        await formEl.validate(async (valid, fields) => {
            if (valid) {
                //调用修改方法
                let result = await modifyDept(modifyDeptForm.value);
                if(result.code == 200){
                    ElMessage({
                        message: '修改成功',
                        type: 'success',
                    })
                    //关闭弹框
                    dialogFormModifyDept.value = false;
                    //表单值恢复为初始值
                    modifyDeptForm.value = { ...initialModifyDeptForm };
                    //重新加载表单
                    search();
                }
            }
        })
    };
    //取消弹框
    const modifyDeptCancel = async () => {
        //关闭弹框
        dialogFormModifyDept.value = false;
        //表单值恢复为初始值
        modifyDeptForm.value = { ...initialModifyDeptForm };
    };
    /*********删除部门*********/
    const handleDeleteDept = async(index: number, row: any) => {
        //调用删除方法
        try {
            let result = await deleteDept(row.deptId);
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
        let result = await getDeptList(tableParams.value);
        if(result.code == 200){
            tableData.value = result.deptList;
        }
    })
    /*============================生命周期钩子结束============================*/

</script>

<style scoped>
</style>
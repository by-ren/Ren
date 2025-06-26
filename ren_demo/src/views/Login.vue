<template>
	<div class="loginContainer">
		<el-form :model="loginForm" :rules="loginRules" class="loginForm">
			<h3 class="title">{{ title }}</h3>
			<el-form-item prop="username">
				<el-input
					v-model="loginForm.username"
					type="text"
					auto-complete="off"
					placeholder="账号"
				>
					<template #prefix>
						<!-- 也可以写成<svg-icon icon-class="user" class="input-icon" />，支持两种方式 -->
						<SvgIcon icon-class="user" class="input-icon" />
					</template>
				</el-input>
			</el-form-item>
			<el-form-item prop="password">
				<el-input
					v-model="loginForm.password"
					type="password"
					auto-complete="off"
					placeholder="密码"
				>
					<template #prefix>
						<SvgIcon icon-class="password" class="input-icon" />
					</template>
				</el-input>
			</el-form-item>
			<el-form-item style="width: 100%">
				<el-button
					:loading="isLoading"
					size="default"
					type="primary"
					style="width: 100%"
					@click.native.prevent="handleLogin"
				>
					<span v-if="!isLoading">登 录</span>
					<span v-else>登 录 中...</span>
				</el-button>
			</el-form-item>
		</el-form>
	</div>
</template>

<script setup lang="ts" name="login">
import { ref, reactive } from "vue";
import router from "@/router";
import { useAuthStore } from "@/stores/authStore";
import { ElMessage } from "element-plus";

const authStore = useAuthStore();
// 标题
let title = ref("任管理系统");
//表单
let loginForm = reactive({
	//用户名
	username: "",
	//密码
	password: "",
});
//表单验证规则
let loginRules = reactive({
	username: [{ required: true, trigger: "blur", message: "请输入您的账号" }],
	password: [{ required: true, trigger: "blur", message: "请输入您的密码" }],
});
//是否登陆中
const isLoading = ref(false);

//登录
const handleLogin = async () => {
	try {
		isLoading.value = true;
		const result = await authStore.login(loginForm.username, loginForm.password);
		if (result.code === 200) {
			//重定向到首页（不允许回退）
			router.replace("/");
		} else {
			ElMessage.error(result.msg);
		}
	} finally {
		isLoading.value = false;
	}
};
</script>

<style scoped>
.loginContainer {
	display: flex;
	justify-content: center;
	align-items: center;
	height: 100vh;
	background-image: url("../assets/images/login_background.jpg");
	background-size: cover;
	background-position: center;
}
.title {
	margin: 0px auto 30px auto;
	text-align: center;
	color: #707070;
}
.loginForm {
	border-radius: 6px;
	background: #ffffff;
	width: 400px;
	padding: 25px 25px 5px 25px;
	.el-input {
		height: 38px;
		input {
			height: 38px;
		}
	}
	.input-icon {
		height: 39px;
		width: 14px;
		margin-left: 2px;
	}
}
</style>

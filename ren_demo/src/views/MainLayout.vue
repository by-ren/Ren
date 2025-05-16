<template>
  <div class="container">
    <el-container>
      <el-aside class="aside" :width="isCollapse ? '64px' : '200px'">

        <el-menu 
          :router="true"
          active-text-color="#ffd04b"
          background-color="#545c64"
          text-color="#fff"
          :collapse="isCollapse"
          class="custom-menu"
          :unique-opened="true"
          @select="tagAdd"
          ref="menuRef"
        >
          <!-- 遍历菜单项 -->
          <SidebarItem v-for="item in menuItems" :key="item.index" :item="item"></SidebarItem>
        </el-menu>
      </el-aside>
      
      <el-container>
        <el-header class="container-header">
          <el-row class="el-row1">
            <el-col :span="20" class="left">
              <el-button link @click="collapseFun">
                <el-icon :size="30" v-if="!isCollapse"><i-ep-fold /></el-icon>
                <el-icon :size="30" v-if="isCollapse"><i-ep-expand /></el-icon>
              </el-button>
            </el-col>
            <el-col :span="4" class="right">
              <el-dropdown @command="userInfoCommand">
                <!-- 触发元素 -->
                <el-icon :size="20" style="margin-right: 15px">
                  <i-ep-setting />
                </el-icon>
                <!-- 下拉菜单（Vue 3 插槽语法） -->
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="loginOut">退出登录</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </el-col>
          </el-row>
          <el-row class="el-row2">
            <el-tag :closable="item.closable" :type="item.type" v-for="(item,index) in tagArr" :key="item.tagId" @click="tagClick(index)" @close="tagClose(index)">{{item.tagName}}</el-tag>
          </el-row>
        </el-header>
        
        <el-main class="container-main">
          <RouterView></RouterView>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup lang="ts" name="home">
  import { reactive,ref,onMounted,watch } from 'vue'
  import { useAuthStore } from '@/stores/authStore'
  import { useRoute } from 'vue-router';
  import router from '@/router';
  import type { Menu } from '@/types/Menu';
  import { storeToRefs } from 'pinia'
  // 该组件是递归组件，所以无法自动导入，需要手动导入
  import SidebarItem from '@/components/SidebarItem/Index.vue'
  /*============================通用参数开始============================*/
  // auth相关pinia
  const authStore = useAuthStore()
  // 已选中的路由
  const route = useRoute();
  // 菜单栏
  const menuRef = ref();
  /*============================通用参数结束============================*/


  /*============================菜单栏相关开始============================*/
  const isCollapse = ref(false)
  //修改菜单栏是否折叠
  const collapseFun = () => {
    isCollapse.value = !(isCollapse.value)
  }
  const menuItems = ref<Menu[]>([]);

  /*============================菜单栏相关结束============================*/


  /*============================个人中心相关开始============================*/
  const userInfoCommand = (command:string) => {
    switch(command) {
      case 'loginOut':
      authStore.logout()
        break
    }
  }
  /*============================个人中心相关结束============================*/


  /*============================标签页开始============================*/
  // 定义合法类型
  type TagType = 'primary' | 'success' | 'warning' | 'danger' | 'info';
  interface TagItem {
    tagId: string
    tagName: string
    type: TagType
    closable: boolean
  }
  let tagArr = reactive<TagItem[]>([
    {tagId:"Index",tagName:"首页",type:'primary',closable:false},
  ]);
  // 统一状态更新方法
  const updateTagStates = (activeTagId: string) => {
    tagArr.forEach(tag => {
      tag.type = tag.tagId === activeTagId ? 'primary' : 'info';
      tag.closable = tag.tagId !== "Index"; // 首页始终不可关闭
    });
  };
  //标签页点击方法
  let tagClick = async (index:number) => {
    await router.push({ name: tagArr[index].tagId });
    // 动态选中菜单
    menuRef.value.updateActiveIndex(tagArr[index].tagId); 
    updateTagStates(tagArr[index].tagId);
  };
  //标签页关闭方法
  const tagClose = async (index: number) => {
    //需要关闭的标签
    const closedTag = tagArr[index];
    //删除标签
    tagArr.splice(index, 1);
    // 关闭后自动激活相邻标签
    if (closedTag.type === 'primary' && tagArr.length > 0) {
      const newIndex = Math.min(index, tagArr.length - 1);
      const targetTagId = tagArr[newIndex].tagId;

      // 路由跳转到激活标签对应页面
      await router.push({ name: targetTagId });
      // 动态选中菜单
      menuRef.value.updateActiveIndex(targetTagId);
      updateTagStates(targetTagId);
    }

  };
  //标签页添加方法
  const tagAdd = (index: string, indexPath: string[]) => {
    // 获取当前菜单项配置
    const findMenuItem = (items: any[]): any => {
      for (const item of items) {
        if (item.index === index) return item
        if (item.children) {
          const found = findMenuItem(item.children)
          if (found) return found
        }
      }
    };
    //当前选中的菜单项
    const currentItem = findMenuItem(menuItems.value) || { index, name: '未知菜单' };
    // 判断是否已存在相同标签
    const existingTag = tagArr.find(t => t.tagId === currentItem.index);
    if (!existingTag) {
      tagArr.push({
        tagId: currentItem.index,
        tagName: currentItem.name,
        type: 'primary',
        closable: true
      });
    }
    // 统一更新所有标签状态
    updateTagStates(currentItem.index);
  };
  /*============================标签页结束============================*/


  /*============================监视器相关开始============================*/
  // 监听 tagArr 变化并持久化
  watch(
    () => tagArr,
    (newVal) => {
      localStorage.setItem('tagArr', JSON.stringify(newVal));
    },
    { deep: true } // 深度监听数组内部对象变化
  );
  /*============================监视器相关结束============================*/


  /*============================生命周期钩子开始============================*/
  onMounted(async () => {
    // 先等待路由加载完成
    await router.isReady();
    // router.getRoutes()获取当前路由表信息
    console.info(router.getRoutes());
    const { menus } = storeToRefs(authStore);
    menuItems.value = [{ index: 'Index', name: '首页', icon: 'i-ep-house' }];
    menuItems.value.push(...menus.value);
    // 从 localStorage 加载标签页数据
    const savedTags = localStorage.getItem('tagArr');
    if (savedTags) {
      Object.assign(tagArr, JSON.parse(savedTags));
    }
    //动态选中菜单
    menuRef.value.updateActiveIndex(route.name?.toString() || '');
  })
  /*============================生命周期钩子结束============================*/
</script>

<style scoped>
  .icon{
    margin-right: 15px;
  }
  .container {
    min-height: 100vh;
  }
  .aside{
    min-height: 100vh;
    background-color: #545c64;
    transition: all 0.3s ease-in-out;
    /* 如果菜单需要滚动，仅在内容溢出时显示滚动条 */
    .el-menu {
      height: 100%;
      overflow-y: auto; /* 将滚动条转移到菜单内部 */
      border: none;
    }
  }
  .container-header{
    padding: 0;
    font-size: 12px;
    height:auto;
    .el-row1{
      height: 50px;
      border-bottom:1px solid #F6F6F6;
      .left{
        display: flex;
        align-content: center;
        align-items: center;
      }
      .right{
        display: flex;
        justify-content: end;
        align-items: center;
        align-content: center;
      }
    }
    .el-row2{
      .el-tag{
        cursor: pointer;
        margin: 5px 5px;
      }
    }
  }
</style>
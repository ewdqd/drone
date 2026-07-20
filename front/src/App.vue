<template>
  <div class="app-wrapper">
    <!-- 导航栏（所有页面共享） -->
    <GuestNavbar />

    <!-- 路由视图（显示当前页面内容） -->
    <router-view class="page-content" />

    <!-- 页脚（除登录页外共享，用v-if控制） -->
    <GuestFooter v-if="$route.name !== 'Login'" />
  </div>
</template>

<script>
import GuestNavbar from './components/GuestNavbar.vue'
import GuestFooter from './components/GuestFooter.vue'

export default {
  name: 'App',
  components: {
    GuestNavbar,
    GuestFooter
  }
}
</script>

<style>
/* 全局样式重置 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Inter', 'PingFang SC', 'Microsoft YaHei', 'sans-serif';
  background-color: #fff;
  color: #333;
}

::-webkit-scrollbar {
  display: none;
}
</style>

<style scoped>
/* 根容器：Flex 列布局，占满视口高度 */
.app-wrapper {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}
.scrollable::-webkit-scrollbar {
  display: none;
}
/* 主体内容区：自动填充剩余高度，推动页脚到底部 */
.page-content {
  flex: 1;
  /* 可选：内容与页脚的间距 */
}
</style>

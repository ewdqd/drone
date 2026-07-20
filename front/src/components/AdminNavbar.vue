<template>
  <el-container class="admin-navbar-container">
    <el-aside width="220px" class="admin-sidebar">
      <div class="logo">低空研究院管理端</div>
      <el-menu
        :default-active="$route.path"  
        class="admin-menu"
        router 
        background-color="#304156"    
        text-color="#bfcbd9"          
        active-text-color="#409EFF"   
        active-background-color="#263445" 
      >
        <el-menu-item index="/admin/home">管理首页</el-menu-item>
        <el-menu-item index="/admin/home/news-manage">新闻管理</el-menu-item>
        <el-menu-item index="/admin/home/product-manage">产品管理</el-menu-item>
        <el-menu-item index="/admin/home/user-manage">用户管理</el-menu-item>
        <el-menu-item index="/admin/home/setting">系统设置</el-menu-item>
      </el-menu>
    </el-aside>
  </el-container>
</template>

<script>
export default {
  name: 'AdminNavbar'
}
</script>

<style scoped>
.admin-navbar-container {
  height: 100vh;
}
.admin-sidebar {
  background-color: #304156;
  color: #fff;
}
.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  font-size: 18px;
  border-bottom: 1px solid #475669;
}
.admin-menu {
  border-right: none;
}
</style>
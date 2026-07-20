<template>
  <header class="sticky top-0 z-50 bg-[#014a95] text-white shadow-none border-none relative">
    <!-- 顶部Logo区域 -->
    <div class="container mx-auto px-0 py-0 h-[183px]">
      <img src="@/assets/images/xuptlogo.png" alt="西安邮电大学Logo"
        class="absolute left-[119px] top-[44px] w-[398px] h-[94px] bg-[rgba(229,229,229,1)] object-contain">
      <div class="topWord">低空经济研究院</div>
      <img src="@/assets/images/xiaoxun.png" alt="校训"
        class="absolute left-[997px] top-[10px] w-[512px] h-[163px] object-contain">
    </div>

    <!-- 底部导航栏 -->
    <div class="bg-white border-t border-gray-200 h-[42px] overflow-x-hidden w-full z-20">
      <div class="container mx-auto px-0 w-[1920px]">
        <nav class="flex items-center h-full w-full">
          <router-link to="/" exact active-class="nav-active"
            class="flex-1 py-2 flex items-center justify-center text-black hover:text-blue-600 transition-colors font-medium text-base pointer-events-auto">
            <i class="fa fa-home mr-2"></i>
            <span>首页</span>
          </router-link>
          <router-link to="/institute-intro" active-class="nav-active"
            class="flex-1 py-2 flex items-center justify-center text-black hover:text-blue-600 transition-colors font-medium text-base pointer-events-auto">
            <i class="fa fa-info-circle mr-2"></i>
            <span>研究院概况</span>
          </router-link>
          <router-link to="/news-list" active-class="nav-active"
            class="flex-1 py-2 flex items-center justify-center text-black hover:text-blue-600 transition-colors font-medium text-base pointer-events-auto">
            <i class="fa fa-newspaper mr-2"></i>
            <span>科研新闻</span>
          </router-link>
          <router-link to="/product-list" active-class="nav-active"
            class="flex-1 py-2 flex items-center justify-center text-black hover:text-blue-600 transition-colors font-medium text-base pointer-events-auto">
            <i class="fa fa-box-open mr-2"></i>
            <span>成果展示</span>
          </router-link>
          <router-link to="/team" active-class="nav-active"
            class="flex-1 py-2 flex items-center justify-center text-black hover:text-blue-600 transition-colors font-medium text-base pointer-events-auto">
            <i class="fa fa-users mr-2"></i>
            <span>研究队伍</span>
          </router-link>
          <router-link to="/cooperation" active-class="nav-active"
            class="flex-1 py-2 flex items-center justify-center text-black hover:text-blue-600 transition-colors font-medium text-base pointer-events-auto">
            <i class="fa fa-handshake mr-2"></i>
            <span>联系我们</span>
          </router-link>
          <router-link to="/admin/login" active-class="nav-active"
            class="flex-1 py-2 flex items-center justify-center text-black hover:text-blue-600 transition-colors font-medium text-base pointer-events-auto">
            <i class="fa fa-sign-in mr-2"></i>
            <span>飞控平台</span>
          </router-link>
        </nav>
      </div>
    </div>
  </header>
</template>

<script>
export default {
  name: 'AppNavbar',
  data() {
    return {
      isMobileMenuOpen: false
    }
  },
  methods: {
    toggleMobileMenu() {
      this.isMobileMenuOpen = !this.isMobileMenuOpen;
    }
  }
}
</script>

<style scoped>
@font-face {
  font-family: 'YouSheBiaoTiHei';
  /* 自定义字体名称 */
  src: url('@/assets/fonts/YouSheBiaoTiHei-2.ttf') format('truetype')
}

.topWord {
  position: absolute;
  left: 593px;
  top: 56px;
  font-family: 'YouSheBiaoTiHei';
  color: rgba(255, 255, 255, 1);
  font-size: 48px;
}

.nav-active {
  background-color: rgba(59, 130, 246, 0.1);
  color: #2563eb !important;
  border-bottom: 3px solid #2563eb;
  padding: 8px 0 !important;
  border-radius: 4px 4px 0 0;
}

.z-20 {
  z-index: 20;
}

::-webkit-scrollbar {
  display: none;
}

img {
  width: auto !important;
  height: auto !important;
  object-fit: contain;
}

/* 移动端适配 */
@media (max-width: 768px) {
  .container {
    width: 100% !important;
  }

  .flex {
    flex-direction: column;
  }

  nav {
    flex-wrap: wrap;
  }

  router-link {
    width: 100%;
    text-align: center;
  }
}
</style>
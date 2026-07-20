<template>
  <div class="guest-home-page">
    <!-- 首页Banner -->
    <div class="relative">
      <img src="@/assets/images/homeBanner.jpg" alt="无人机Banner" class="w-full h-[380px] object-cover">
      <div class="bannerWord">
        <div
          style="font-family: 'YouSheBiaoTiHei';color: aliceblue;font-size: 120px;margin-top: 30px;margin-left: 45%;">
          扎根低空</div>
        <div
          style="font-family: 'YouSheBiaoTiHei';color: aliceblue;font-size: 120px;margin-top: 140px;margin-left: 0px;">
          成就高远</div>
      </div>
    </div>

    <!-- 内容板块：2列网格布局 -->
    <div class="container mx-auto px-6 py-8">
      <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
        <!-- 左侧轮播图 -->
        <div class="relative flex flex-col h-full">
          <!-- 轮播图片容器 -->
          <div class="carousel-container relative w-full overflow-hidden flex-grow mb-4">
            <img v-for="(img, index) in carouselImages" :key="index" :src="img.src" :alt="img.alt"
              class="carousel-img absolute top-0 left-0 w-full h-full object-cover transition-opacity duration-500"
              :class="{ 'opacity-100': currentIndex === index, 'opacity-0': currentIndex !== index }">
          </div>

          <!-- 指示器 -->
          <div class="carousel-indicators flex justify-center gap-3 z-10">
            <button v-for="(img, index) in carouselImages" :key="index" @click="currentIndex = index"
              class="w-6 h-1 transition-all duration-300"
              :class="{ 'bg-primary': currentIndex === index, 'bg-gray-300': currentIndex !== index }">
            </button>
          </div>
        </div>

        <!-- 科研新闻 -->
        <div class="bg-white rounded-lg shadow p-6">
          <div class="flex items-center mb-3 border-b border-solid border-gray-400 pb-2">
            <i class="fa fa-newspaper text-primary mr-2"></i>
            <h2 class="text-xl font-bold">科研新闻</h2>
            <span class="ml-auto text-sm text-primary hover:underline cursor-pointer"
              @click="goToResearchNewsList">更多</span>
          </div>

          <ul class="space-y-0">
            <!-- 有数据时：区分“有效新闻”和“暂无新闻”默认项 -->
            <template v-if="researchNewsList && researchNewsList.length > 0">
              <li v-for="(news, index) in researchNewsList.slice(0, 3)" :key="index"
                class="py-2 border-b border-dashed border-gray-300 last:border-b-0">
                <div class="flex items-start justify-between">
                  <!-- 若为“暂无新闻”：显示纯文本（无链接） -->
                  <template v-if="news.title === '暂无新闻'">
                    <p class="text-gray-500" style="cursor: default;">{{ news.title }}</p>
                  </template>
                  <!-- 有效新闻：显示可点击链接 -->
                  <template v-else>
                    <a @click="goToNewsDetail(news.id)" class="hover:text-primary transition-colors cursor-pointer">
                      {{ news.title }}
                    </a>
                  </template>

                </div>
              </li>
            </template>
            <!-- 极端情况：列表为空时的兜底提示 -->
            <template v-else>
              <li class="py-12 text-center" style="cursor: default;">
                <i class="fa fa-newspaper-o text-4xl mb-3 text-gray-400"></i>
                <p class="text-gray-500 text-base">暂无科研新闻</p>
              </li>
            </template>
          </ul>
        </div>

        <!-- 通知公告 -->
        <div class="bg-white rounded-lg shadow p-6">
          <div class="flex items-center justify-between mb-3 border-b border-solid border-gray-400 pb-2">
            <div class="flex items-center">
              <i class="fa fa-bell text-primary mr-2"></i>
              <h2 class="text-xl font-bold">通知公告</h2>
            </div>
            <router-link to="/notice-list" class="text-sm text-blue-600 hover:underline transition-colors">
              更多
            </router-link>
          </div>
          <ul class="space-y-0">
            <li class="py-2 border-b border-dashed border-gray-300">
              <div class="flex items-start justify-between">
                <router-link :to="{ name: 'NoticeDetail', query: { id: '1' } }"
                  class="hover:text-primary transition-colors cursor-pointer">
                  “宏志助航计划”西安邮电大学2026届毕业生秋季就业洽谈会（综合类第五场）参会企业名录
                </router-link>
              </div>
            </li>
            <li class="py-2 border-b border-dashed border-gray-300">
              <div class="flex items-start justify-between">
                <router-link :to="{ name: 'NoticeDetail', query: { id: '2' } }"
                  class="hover:text-primary transition-colors cursor-pointer">
                  关于做好我校2026年“硕博连读”研究生推荐选拔工作的通知
                </router-link>
              </div>
            </li>
            <li class="py-2">
              <div class="flex items-start justify-between">
                <router-link :to="{ name: 'NoticeDetail', query: { id: '3' } }"
                  class="hover:text-primary transition-colors cursor-pointer">
                  西安邮电大学2026年博士研究生招生简章
                </router-link>
              </div>
            </li>
          </ul>
        </div>

        <!-- 学术信息 -->
        <div class="bg-white rounded-lg shadow p-6">
          <div class="flex items-center justify-between mb-3 border-b border-solid border-gray-400 pb-2">
            <div class="flex items-center">
              <i class="fa fa-graduation-cap text-primary mr-2"></i>
              <h2 class="text-xl font-bold">学术信息</h2>
            </div>
            <router-link to="/academic-list" class="text-sm text-blue-600 hover:underline transition-colors ml-auto">
              更多
            </router-link>
          </div>
          <ul class="space-y-0">
            <li class="py-2 border-b border-dashed border-gray-300">
              <router-link :to="{ name: 'AcademicDetail', query: { id: '1' } }"
                class="hover:text-blue-600 transition-colors cursor-pointer block">
                西邮学报——《IRS辅助多用户BC安全卸载的感知MEC网络资源分配方案》
              </router-link>
            </li>
            <li class="py-2 border-b border-dashed border-gray-300">
              <router-link :to="{ name: 'AcademicDetail', query: { id: '2' } }"
                class="hover:text-blue-600 transition-colors cursor-pointer block">
                西邮学报——《智能反射面辅助 CR-SR的安全边缘计算网络优化方案》
              </router-link>
            </li>
            <li class="py-2">
              <router-link :to="{ name: 'AcademicDetail', query: { id: '3' } }"
                class="hover:text-blue-600 transition-colors cursor-pointer block">
                西邮学报——《一种移动源偏差消除的时频域联合定位算法》
              </router-link>
            </li>
          </ul>
        </div>
      </div>
    </div>

    <!-- 友情链接与底部信息 -->
    <div class="bg-[#014a95] text-white py-4">
      <div class="container mx-auto px-6">
        <div class="flex flex-col md:flex-row items-start gap-4 mb-2">
          <div class="flex-grow">
            <div class="mb-4">
              <h3
                style="font-family:PingFangSC-bold; font-size:28px; line-height:39px; color:#fff; text-align:left; margin-bottom:0px;">
                友情链接
              </h3>
              <div class="w-36 h-1 bg-white"></div>
            </div>
            <div class="grid grid-cols-3 gap-x-8 gap-y-4">
              <a href="https://www.xupt.edu.cn" target="_blank"
                style="font-family:PingFangSC-regular; font-size:20px; line-height:28px; color:#fff; text-align:left;">学校官网</a>
              <a href="https://tg.xupt.edu.cn/" target="_blank"
                style="font-family:PingFangSC-regular; font-size:20px; line-height:28px; color:#fff; text-align:left;">通信与信息工程学院</a>
              <a href="https://dianxin.xupt.edu.cn/" target="_blank"
                style="font-family:PingFangSC-regular; font-size:20px; line-height:28px; color:#fff; text-align:left;">电子工程学院</a>
              <a href="https://automation.xupt.edu.cn/" target="_blank"
                style="font-family:PingFangSC-regular; font-size:20px; line-height:28px; color:#fff; text-align:left;">人工智能、自动化学院</a>
              <a href="http://lib.xiyou.edu.cn" target="_blank"
                style="font-family:PingFangSC-regular; font-size:20px; line-height:28px; color:#fff; text-align:left;">图书馆</a>
              <a href="http://cs.xupt.edu.cn" target="_blank"
                style="font-family:PingFangSC-regular; font-size:20px; line-height:28px; color:#fff; text-align:left;">计算机学院</a>
              <a href="http://jiuye.xupt.edu.cn" target="_blank"
                style="font-family:PingFangSC-regular; font-size:20px; line-height:28px; color:#fff; text-align:left;">就业创业信息网</a>
              <a href="https://gr.xupt.edu.cn/" target="_blank"
                style="font-family:PingFangSC-regular; font-size:20px; line-height:28px; color:#fff; text-align:left;">研究生院</a>
              <a href="https://jgy.xiyou.edu.cn/jgsz.htm" target="_blank"
                style="font-family:PingFangSC-regular; font-size:20px; line-height:28px; color:#fff; text-align:left;">经济与管理学院</a>
            </div>
          </div>
          <div class="w-full md:w-auto h-64 md:h-198 bg-gray-200 flex-shrink-0" style="background-color: #e5e5e5;">
            <img src="@/assets/images/xiaoxun.png" alt="右侧图片" class="w-full h-full object-cover">
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { getNewsList } from '@/api/manage.js';
export default {
  name: 'GuestHome',
  data() {
    return {
      newsList: [],
      jobNewsList: [],
      researchNewsList: [],
      NewsQueryDto: {
        newsTypeId: null,
        current: 1,
        size: 10,
      },
      // 轮播图数据
      carouselImages: [

        { src: require("@/assets/images/syt1.jpg"), alt: "轮播图1" },
        { src: require("@/assets/images/syt2.jpg"), alt: "轮播图2" },
        { src: require("@/assets/images/syt3.jpg"), alt: "轮播图3" },
        { src: require("@/assets/images/syt4.jpg"), alt: "轮播图4" },



      ],
      currentIndex: 0, // 当前显示的轮播索引
      carouselTimer: null // 自动播放定时器
    };
  },
  created() {
    this.fetchNews();
  },
  mounted() {
    // this.fetchLatestNews();
    // 启动自动播放
    this.startAutoPlay();
    // 鼠标悬停时停止播放，离开时继续
    const carousel = this.$refs.carousel;
    if (carousel) {
      carousel.addEventListener("mouseenter", this.stopAutoPlay);
      carousel.addEventListener("mouseleave", this.startAutoPlay);
    }
  },
  beforeDestroy() {
    // 清除定时器，避免内存泄漏
    this.stopAutoPlay();
  },
  methods: {
    // 上一张
    prevSlide() {
      this.currentIndex = (this.currentIndex - 1 + this.carouselImages.length) % this.carouselImages.length;
    },
    // 下一张
    nextSlide() {
      this.currentIndex = (this.currentIndex + 1) % this.carouselImages.length;
    },
    // 自动播放
    startAutoPlay() {
      this.carouselTimer = setInterval(() => {
        this.nextSlide();
      }, 5000); // 5秒切换一次
    },
    // 停止自动播放
    stopAutoPlay() {
      if (this.carouselTimer) {
        clearInterval(this.carouselTimer);
      }
    },

    async fetchNews() {
      try {
        // 获取科研新闻列表
        const researchNewsQuery = {
          ...this.NewsQueryDto,
          newsTypeId: 1, // 科研新闻类型ID
          size: 10 // 获取更多数据以便排序
        };
        const { data } = await getNewsList(researchNewsQuery);

        // 按创建时间降序排序
        this.researchNewsList = data.news.sort((a, b) => {
          return new Date(b.createTime) - new Date(a.createTime);
        });

        // 保存所有新闻
        this.newsList = data.news;
        this.total = data.count;
      } catch (error) {
        this.$message.info(error.message);
      }
    },

    // 跳转到科研新闻列表页面
    goToResearchNewsList() {
      this.$router.push({
        path: '/news-list',
        query: {
          newsTypeId: 1,
          size: 3
        }
      });
    },
    // 跳转到新闻详情页
    goToNewsDetail(id) {
      if (id == null) {
        this.$message?.warning('该新闻暂无详情');
        return;
      }
      this.$router.push({
        path: '/news-detail',
        query: { id: id }
      });
    },

  }
}
</script>

<style scoped>
/* 轮播容器样式 */
.carousel-container {
  position: relative;
  width: 100%;
  height: 100%;
  overflow: hidden;
  padding: 0;
  margin: 0;
}

/* 轮播图片基础样式 */
.carousel-img {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  object-fit: cover;
  opacity: 0;
  transition: opacity 0.5s ease;
  width: 100%;
  height: 100%;
  border-radius: 8px;
}

/* 当前显示的图片 */
.carousel-img.opacity-100 {
  opacity: 1;
}

/* 指示器容器 */
.carousel-indicators {
  z-index: 2;
}

@font-face {
  font-family: 'YouSheBiaoTiHei';
  src: url('@/assets/fonts/YouSheBiaoTiHei-2.ttf') format('truetype');
}

.guest-home-page {
  display: block;
  position: relative;
  z-index: 1;
}

.bannerWord {
  position: absolute;
  inset: 0;
  display: flex;
  z-index: 1;
}

/* 友情链接hover样式 */
::v-deep .grid.grid-cols-3.gap-x-8.gap-y-4 a:hover {
  color: #add8e6 !important;
  transition: color 0.3s;
}

/* 就业、招生信息链接hover样式 */
li a {
  color: inherit;
  text-decoration: none;
}

li a:hover {
  color: #014a95;
  transition: color 0.3s;
}
</style>

<template>
  <div class="research-institute-page">
    <div class="container">
      <div class="layout-row">
        <!-- 左侧图片区域 -->
        <div class="carousel image-section">
          <div class="carousel-container">
            <div class="carousel-images">
              <img 
                v-for="(image, index) in images" 
                :key="index"
                :src="image.src" 
                :alt="image.alt"
                class="research-image"
                :class="{ active: index === activeIndex }"
              >
            </div>
            <!-- 轮播指示器 -->
            <div class="carousel-indicators">
              <span 
                v-for="(image, index) in images" 
                :key="index"
                class="indicator"
                :class="{ active: index === activeIndex }"
                @click="setActiveIndex(index)"
              ></span>
            </div>
          </div>
        </div>
        
        <!-- 右侧文字描述区域 -->
        <div class="text-section">
          <h2 class="section-title">{{ sectionTitle }}</h2>
          <p class="section-content">
            西安邮电大学低空经济研究院立足和服务于国家、行业低空经济发展战略，是学校信息与通信工程、集成电路、数据科学、人工智能、智能制造、网络空间安全和数字经济等特色学科创新交叉融合的高能级研究枢纽与全链路产业集群。
          </p>
          <p class="section-content">
            研究院聚焦"飞行设施网、低空智联网、低空服务网"的创新方向，通过设立"多模态算法、低空态势感知、高可靠通信及安全技术、低空物流技术、低空网络规划优化、低空经济发展"等多个研究中心，攻关低空基础设施、低空巡检、低空物流、全域感知等相关低空智联网领域关键技术，形成了"低空经济+行教融合""低空经济+研用融合""低空经济+数智融合"的发展特色；聚力"位置+感知+计算"的核心能力，推进低慢小监控、应急救援、无人机巡检、空间数字治理、低空出行、低空物流等场景应用，推动低空空域从"可通达、可计算"到"可应用、可运营"的转变，助力空中邮路建设、低空数字治理、行业赋智增能和国土安全守护，为低空经济提供稳定、可靠的基础设施建设服务保障。研究院与中国铁塔股份有限公司签约共建"低空经济联合创新中心"，与陕西省公安厅共建"无人机应用与反制联合实验室"。
          </p>
          <p class="section-content">
            研究院将坚持"扎根低空，成就高远"的理念，围绕低空领域基础设施关键技术、低空行业应用，推进科技成果转化为现实生产力，将研究院打造成为重要的创新平台。
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import img1 from "@/assets/images/fly1 (1).jpg"
import img2 from "@/assets/images/fly1 (2).jpg"
import img3 from "@/assets/images/fly1 (3).jpg"
import img4 from "@/assets/images/fly1 (4).jpg"
import img5 from "@/assets/images/fly1 (5).jpg"
import img6 from "@/assets/images/fly1 (6).jpg"
import img7 from "@/assets/images/fly1 (7).jpg"

export default {
  name: 'GuestInstituteIntro',
  data() {
    return {
      images: [
        { src: img1, alt: "低空经济研究图像1" },
        { src: img2, alt: "低空经济研究图像2" },
        { src: img3, alt: "低空经济研究图像3" },
        { src: img4, alt: "低空经济研究图像4" },
        { src: img5, alt: "低空经济研究图像5" },
        { src: img6, alt: "低空经济研究图像6" },
        { src: img7, alt: "低空经济研究图像7" },
      ],
      sectionTitle: "西安邮电大学低空经济研究院",
      activeIndex: 0,
      interval: 3000, // 自动播放间隔时间（毫秒）
      timer: null,
    }
  },
  mounted() {
    this.startAutoPlay();
  },
  beforeDestroy() {
    this.stopAutoPlay();
  },
  methods: {
    startAutoPlay() {
      this.timer = setInterval(() => {
        this.nextSlide();
      }, this.interval);
    },
    stopAutoPlay() {
      if (this.timer) {
        clearInterval(this.timer);
        this.timer = null;
      }
    },
    nextSlide() {
      this.activeIndex = (this.activeIndex + 1) % this.images.length;
    },
    setActiveIndex(index) {
      this.activeIndex = index;
      // 重置自动播放
      this.stopAutoPlay();
      this.startAutoPlay();
    }
  }
}
</script>

<style scoped>
.research-institute-page {
  padding: 40px 0;
  background-color: #fff;
}

.container {
  max-width: 1920px;
  margin: 0 auto;
  padding: 0 20px;
}

.layout-row {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 40px;
}

.image-section {
  flex: 0 0 45%;
  min-width: 300px;
  background-color: #f5f5f5;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
  box-sizing: border-box;
}

.carousel-container {
  position: relative;
  width: 100%;
  overflow: hidden;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.carousel-images {
  position: relative;
  width: 100%;
  height: 600px; /* 固定高度，可根据需要调整 */
}

.research-image {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  opacity: 0;
  transition: opacity 0.8s ease-in-out;
}

.research-image.active {
  opacity: 1;
}

.carousel-indicators {
  position: absolute;
  bottom: 15px;
  left: 0;
  right: 0;
  display: flex;
  justify-content: center;
  gap: 8px;
  z-index: 10;
}

.indicator {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background-color: rgba(255, 255, 255, 0.5);
  cursor: pointer;
  transition: all 0.3s ease;
}

.indicator.active {
  background-color: #fff;
  transform: scale(1.2);
}

.indicator:hover {
  background-color: rgba(255, 255, 255, 0.8);
}

.text-section {
  flex: 0 0 45%;
  min-width: 300px;
  padding: 20px;
  box-sizing: border-box;
}

.section-title {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 24px;
  text-align: center;
  color: #333;
  position: relative;
  padding-bottom: 12px;
}

.section-title::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 60px;
  height: 3px;
  background-color: #1890ff;
}

.section-content {
  font-size: 16px;
  line-height: 1.8;
  color: #666;
  font-family: 'Source Han Sans SC', 'Microsoft YaHei', sans-serif;
  margin-bottom: 20px;
  text-align: justify;
  text-indent: 2em;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .layout-row {
    flex-direction: column;
    gap: 20px;
  }
  
  .image-section,
  .text-section {
    flex: 0 0 100%;
  }
  
  .carousel-images {
    height: 300px;
  }
  
  .section-title {
    font-size: 24px;
  }
  
  .section-content {
    font-size: 14px;
    line-height: 1.6;
  }
}

@media (max-width: 480px) {
  .research-institute-page {
    padding: 20px 0;
  }
  
  .carousel-images {
    height: 250px;
  }
  
  .section-title {
    font-size: 20px;
  }
}
</style>
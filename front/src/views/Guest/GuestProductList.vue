<template>
  <div class="product-showcase">
    <!-- 产品展示网格 -->
    <div class="products-grid">
      <!-- 循环渲染两个卡片：P600无人机 + 研发中提示卡片 -->
      <div v-for="(product, index) in productList" :key="product.id" class="product-card"
        :style="{ animationDelay: `${Math.min(index * 100, 500)}ms` }">
        <div class="product-img-container" v-if="product.id !== 2">
          <!-- 仅第一个卡片显示图片 -->
          <img :src="product.image" :alt="product.name" class="product-img" loading="lazy"
            @error="e.target.src = require('@/assets/images/default.gif')">
          <!-- 仅第一个卡片显示遮罩层 -->
          <div class="product-overlay">
            <router-link :to="`/product-detail/${product.id}`" class="view-details">
              查看详情
            </router-link>
          </div>
        </div>
        <div class="product-info" :class="{ 'develop-tip': product.id === 2 }">
          <h3 class="product-name" v-if="product.id !== 2">{{ product.name }}</h3>
          <p class="product-desc">{{ product.desc }}</p>
        </div>
      </div>
    </div>

    <!-- 动图已注释，保持原有注释状态 -->
    <!-- <div class="decor-gif decor-gif-left"></div> -->
    <!-- <div class="decor-gif decor-gif-right"></div> -->
  </div>
</template>

<script>
export default {
  name: 'ModernProductShowcase',
  data() {
    return {
      // 新增第二个卡片数据（研发中提示）
      productList: [
        {
          id: 1,
          name: 'Prometheus600科研无人机',
          image: require('@/assets/images/P600.jpg'),
          desc: 'P600系列第四代产品，深度融合Prometheus和SpireCV，具备超远距离目标检测与跟踪能力。'
        },
        {
          id: 2,
          name: '', // 空名称，仅展示描述
          image: '', // 无图片
          desc: '更多项目正在研发中...'
        }
      ]
    }
  },
  methods: {
    /**
     * 处理图片加载错误
     * @param {Event} e - 错误事件对象
     */
    handleImageError(e) {
      e.target.src = require('@/assets/images/default.gif');
    }
  },
  mounted() {
    // 使用IntersectionObserver API实现滚动动画
    const observer = new IntersectionObserver((entries) => {
      entries.forEach(entry => {
        if (entry.isIntersecting) {
          entry.target.classList.add('visible');
        }
      });
    }, {
      threshold: 0.1,// 当元素10%可见时触发
      rootMargin: '0px 0px -50px 0px' // 添加底部负边距，提前触发动画
    });

    // 观察所有产品卡片
    document.querySelectorAll('.product-card').forEach(card => {
      observer.observe(card);
    });

    // 保存观察者实例，用于销毁
    this.observer = observer;
  },
  beforeUnmount() {
    // 组件销毁前清理观察者，防止内存泄漏
    if (this.observer) {
      this.observer.disconnect();
    }
  }
}
</script>

<style scoped>
/* 核心修复：移除强制100vh高度，改为自适应 + 垂直居中 */
.product-showcase {
  /* 移除min-height: 100vh，改为自适应高度 */
  min-height: auto;
  /* 若需要内容垂直居中且占满屏幕（无空白），用这个： */
  /* min-height: 100vh; */
  /* 优化padding，减少上下留白 */
  padding: 3rem 2rem;
  background-color: #fafbff;
  position: relative;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  align-items: center;
  /* 新增：垂直居中内容，彻底解决空白 */
  justify-content: center;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 2.5rem;
  max-width: 1200px;
  margin: 0 auto;
  position: relative;
  z-index: 2;
  /* 新增：确保网格容器自适应内容，无多余空白 */
  width: 100%;
}

/* 更多项目提示文字样式（已迁移到卡片内） */
.more-projects-tip {
  display: none;
  /* 隐藏原有文字提示 */
}

.product-card {
  background-color: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.05);
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  opacity: 0;
  transform: translateY(30px);
  /* 优化：卡片高度自适应，移除固定min-height */
  height: 100%;
}

/* 第二个卡片（研发中提示）专属样式 */
.product-card:nth-child(2) {
  background-color: #f8f9fa;
  border: 1px dashed #e0e6ff;
  display: flex;
  align-items: center;
  justify-content: center;
  /* 优化：移除固定min-height，改为自适应 */
  min-height: auto;
}

/* 研发中提示卡片的信息区样式 */
.product-info.develop-tip {
  padding: 3rem 1.5rem;
  text-align: center;
}

.product-info.develop-tip .product-desc {
  font-size: 1rem;
  color: #666;
  -webkit-line-clamp: unset;
  line-clamp: unset;
  margin: 0;
  letter-spacing: 0.8px;
}

.product-card.visible {
  opacity: 1;
  transform: translateY(0);
}

.product-img-container {
  position: relative;
  height: 220px;
  overflow: hidden;
}

.product-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.6s ease;
}

.product-card:hover .product-img {
  transform: scale(1.05);
}

.product-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.7) 0%, transparent 100%);
  opacity: 0;
  transition: opacity 0.4s ease;
  display: flex;
  align-items: flex-end;
  justify-content: center;
  padding: 1.5rem;
}

.product-card:hover .product-overlay {
  opacity: 1;
}

.view-details {
  background-color: white;
  color: #1a2456;
  padding: 0.6rem 1.5rem;
  border-radius: 30px;
  font-size: 0.9rem;
  font-weight: 600;
  text-decoration: none;
  transition: all 0.3s ease;
  transform: translateY(10px);
  opacity: 0;
}

.product-card:hover .view-details {
  transform: translateY(0);
  opacity: 1;
}

.view-details:hover {
  background-color: #1a2456;
  color: white;
}

.product-info {
  padding: 1.5rem;
}

.product-name {
  font-size: 1.25rem;
  font-weight: 700;
  color: #1a2456;
  margin-bottom: 0.75rem;
  transition: color 0.3s ease;
}

.product-card:hover .product-name {
  color: #3b82f6;
}

.product-desc {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .product-showcase {
    padding: 2rem 1rem;
    /* 进一步减少移动端padding */
  }

  .products-grid {
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
    gap: 1.5rem;
  }

  .product-info.develop-tip {
    padding: 2rem 1rem;
  }

  .product-info.develop-tip .product-desc {
    font-size: 0.9rem;
  }
}

/* 平板横屏 */
@media (max-width: 1024px) {
  .products-grid {
    gap: 2rem;
  }
}

/* 小屏手机 */
@media (max-width: 480px) {
  .product-showcase {
    padding: 1.5rem 1rem;
    /* 最小化移动端padding */
  }

  .products-grid {
    grid-template-columns: 1fr;
    /* 单列布局，两个卡片上下排列 */
  }

  .product-info.develop-tip .product-desc {
    font-size: 0.85rem;
  }
}
</style>

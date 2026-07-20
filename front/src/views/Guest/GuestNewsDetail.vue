<template>
  <div class="news-container">
    <div class="detail">
      <div class="back-button">
        <button @click="goBack" class="back-btn">
          <i class="el-icon-arrow-left"></i> 返回新闻列表
        </button>
      </div>
      <div class="title">{{ news.title }}</div>
      <div class="point">
        <div class="flow-index">
          <span>
            发布时间：{{ news.createTime }}
          </span>
          <span>
            浏览量：{{ news.viewCount }}
          </span>
        </div>
      </div>

      <!-- 根据新闻ID显示对应图片 -->
      <div class="news-image" v-if="newsImage">
        <img :src="newsImage" :alt="news.title" class="news-img">
      </div>

      <div class="content" v-html="news.content"></div>
    </div>
  </div>
</template>

<script>
import { fetchNewsDetail } from '@/api/manage.js';
import { getNewsList } from '@/api/manage.js';
export default {
  data() {
    return {
      id: null,
      news: {
        title: '',
        content: '',
        createTime: '',
        viewCount: 0,
        summary: '',
        cover: '', // 封面图片
      },
      newsImage: '', // 根据新闻ID加载的图片
      startTime: null, // 开始阅读时间
      prevNewsId: null, // 上一篇新闻ID
      nextNewsId: null, // 下一篇新闻ID
      allNewsIds: [], // 所有新闻ID列表
    }
  },
  async created() {
    // 获取路由参数
    const id = this.$route?.query?.id;
    if (!id) {
      this.error = '未找到新闻ID';
      this.$message.error(this.error);
      return;
    }
    await this.read(id);
    // 初始化导航功能
    await this.fetchAllNewsIds();
    this.updateNavigation();
  },

  beforeDestroy() {
    // 组件销毁时移除事件监听
    window.removeEventListener('beforeunload', this.endTimeClose);
  },
  methods: {
    // async read(id) {
    //         console.log(this.id)
    //         this.id = id;
    //         this.fetchDetail(id); // 通过ID查询新闻详情
    //         this.saveViewOperation(id); // 记录用户的阅读行为
    //         this.initStartTime();
    //         window.addEventListener('beforeunload', this.endTimeClose);
    //     },

    async read(id) {
      if (!id || this.id === id) return; // 防止重复加载

      console.log('加载新闻:', id);
      this.id = id;

      try {
        // 并行执行，但确保数据加载完成
        await Promise.all([
          this.fetchDetail(id),
          this.saveViewOperation(id)
        ]);

        this.initStartTime();
      } catch (error) {
        this.$message.error('加载新闻失败');
        console.error('读取新闻异常:', error);
      }
    },
    // 记录用户的停留行为
    async endTimeClose() {
      try {
        const endTime = new Date().getTime();
        const taskTime = endTime - this.startTime;
        const dto = {
          type: 4, //声明是停留行为
          newsId: this.id,
          times: taskTime,
        }
      } catch (error) {
        this.$message.warning(error.message);
        console.error('记录停留时长情况异常:', error);
      }
    },
    initStartTime() {
      this.startTime = new Date().getTime(); // 返回当前时间的时间戳
    },
    goBack() {
      this.$router.push('/news-list');
    },
    // 记录阅读行为
    async saveViewOperation(id) {
      try {
        const dto = {
          type: 2, //声明是阅读行为
          newsId: id
        }
      } catch (error) {
        this.$message.warning(error.message);
        console.error('记录行为操作情况异常:', error);
      }
    },
    async fetchDetail(id) {
      try {
        const { data } = await fetchNewsDetail(id);
        this.news = { ...data, };
        // 根据新闻ID设置对应的图片路径
        this.newsImage = require(`@/assets/cover/${id}.jpg`);
      } catch (error) {
        this.$message.warning(error.message);
        console.error('查询新闻详情异常:', error);
        // 如果图片加载失败，设置为空字符串，避免显示错误
        this.newsImage = '';
      }
    },

    // 获取所有新闻ID
    // 获取所有新闻ID（通过新闻列表API）
    async fetchAllNewsIds() {
      try {
        // 使用较大的size参数来获取所有新闻
        const { data } = await getNewsList({
          current: 1,
          size: 9999, // 根据实际情况调整
          newsTypeId: this.selectedNewsType
        });
        return data.news.map(item => item.id);
      } catch (error) {
        console.error('获取新闻ID列表失败:', error);
        return [];
      }
    },


    // 更新导航按钮状态
    updateNavigation() {
      if (!this.allNewsIds.length) return;

      const currentIndex = this.allNewsIds.findIndex(id => id === this.id);

      // 设置上一条新闻ID
      if (currentIndex > 0) {
        this.prevNewsId = this.allNewsIds[currentIndex - 1];
      } else {
        this.prevNewsId = null;
      }

      // 设置下一条新闻ID
      if (currentIndex < this.allNewsIds.length - 1) {
        this.nextNewsId = this.allNewsIds[currentIndex + 1];
      } else {
        this.nextNewsId = null;
      }
    },

    // 导航到上一篇新闻
    goToPrevNews() {
      if (this.prevNewsId) {
        // 更新路由
        this.$router.push({ query: { id: this.prevNewsId } });
        // 直接更新当前新闻ID，而不是重新加载整个页面
        this.id = this.prevNewsId;
        // 加载新闻详情
        this.fetchDetail(this.prevNewsId);
        // 记录浏览行为
        this.saveViewOperation(this.prevNewsId);
        // 更新导航状态
        this.updateNavigation();
        // 滚动到页面顶部
        window.scrollTo(0, 0);
      }
    },

    // 导航到下一篇新闻
    goToNextNews() {
      if (this.nextNewsId) {
        // 更新路由
        this.$router.push({ query: { id: this.nextNewsId } });
        // 直接更新当前新闻ID，而不是重新加载整个页面
        this.id = this.nextNewsId;
        // 加载新闻详情
        this.fetchDetail(this.nextNewsId);
        // 记录浏览行为
        this.saveViewOperation(this.nextNewsId);
        // 更新导航状态
        this.updateNavigation();
        // 滚动到页面顶部
        window.scrollTo(0, 0);
      }
    },
  }
}
</script>

<style scoped lang="scss">
.news-container {
  margin: 0 auto;
  width: 100%;
  max-width: 800px;
  padding: 30px 20px;
  box-sizing: border-box;
  display: block;

  .operation {
    display: none !important;
  }

  .detail {
    width: 100%;

    .back-button {
      margin-bottom: 20px;

      .back-btn {
        display: flex;
        align-items: center;
        padding: 8px 16px;
        background-color: #f5f7fa;
        border: 1px solid #dcdfe6;
        border-radius: 4px;
        color: #606266;
        cursor: pointer;
        font-size: 14px;
        transition: all 0.3s;

        i {
          margin-right: 5px;
        }

        &:hover {
          background-color: #ecf5ff;
          color: #409eff;
          border-color: #c6e2ff;
        }
      }
    }
  }

  .title {
    font-size: 30px;
    font-weight: 600;
    text-align: center;
    padding-block: 15px;
    margin-bottom: 20px;
    position: sticky;
    top: 0;
    z-index: 1;
    background-color: #fff;
  }

  .point {
    margin-bottom: 30px;

    .flow-index {
      text-align: center;
      color: #888;
      font-size: 14px;
      background-color: #f5f5f5;
      padding: 8px 0;
      border-radius: 4px;

      span {
        margin: 0 10px;
      }
    }
  }

  .news-image {
    margin: 20px auto;
    display: flex;
    justify-content: center;
    align-items: center;

    .news-img {
      max-width: 100%;
      height: auto;
      border-radius: 8px;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
      transition: all 0.3s ease;

      &:hover {
        transform: scale(1.02);
        box-shadow: 0 6px 16px rgba(0, 0, 0, 0.15);
      }
    }
  }

  .content {
    padding-top: 10px;
    line-height: 1.8;
    font-size: 15px;
    color: #333;

    p {
      margin-bottom: 16px;
    }
  }

  .nav-footer {
    display: flex;
    justify-content: space-between;
    margin-top: 40px;
    padding-top: 20px;
    border-top: 1px solid #eee;

    .nav-btn {
      padding: 10px 20px;
      border: 1px solid #ddd;
      border-radius: 4px;
      background: #fff;
      cursor: pointer;
      transition: all 0.3s;

      &:hover:not(:disabled) {
        background: #f5f5f5;
        border-color: #409eff;
      }

      &:disabled {
        cursor: not-allowed;
        opacity: 0.6;
        color: #999;
      }
    }
  }

  @media (max-width: 768px) {
    padding: 20px 15px;

    .back-button {
      margin-bottom: 15px;
    }

    .title {
      font-size: 24px;
      margin-bottom: 15px;
    }

    .point {
      margin-bottom: 15px;
      font-size: 13px;
    }

    .news-image {
      margin: 15px 0;
    }

    .content {
      font-size: 14px;
      line-height: 1.7;
    }

    .nav-footer {
      flex-direction: column;
      gap: 10px;

      .nav-btn {
        width: 100%;
      }
    }
  }
}
</style>
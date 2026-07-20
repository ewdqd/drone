<template>
  <!-- 只保留页面主体内容，导航栏由全局布局控制（避免重复） -->
  <div class="news-page">

    <!-- 新闻列表主体 -->
    <div class="news-container">

      <!-- 新闻条目列表 -->
      <div class="news-list">
        <div v-if="!newsList.length">
          <el-empty description="暂无新闻信息"></el-empty>
        </div>

        <div @click="newsClick(news)" class="news-item" v-for="(news,index) in newsList" 
        :key="index">
          <div class="news-img">
             <img :src="getNewsCover(news.id)" alt="新闻封面" srcset="">
          </div>

          <div class="news-content">
            <h3 class="title">{{ news.title }}</h3>
            <div class="meta">
              <span class="date">发布于：{{ news.createTime }}</span>
              <span class="views">阅读量: {{ news.viewCount }}</span>
            </div>
            <p class="brief">{{ news.summary }}</p>
          </div>
        </div>

        <div class="pager">
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
              :current-page="NewsQueryDto.current" :page-sizes="[10, 20]"
              :page-size="NewsQueryDto.size" layout="total, sizes, prev, pager, next, jumper"
              :total="total"></el-pagination>
        </div>

      </div>
    </div>
  </div>
</template>


<script>
import { getNewsList } from '@/api/manage.js'
export default {
    data() {
        return {
            selectedType: {},
            newsTypeList: [],
            newsList: [],
            NewsTopList: [],
            total: 0,
            NewsQueryDto: {
                newsTypeId: null,
                current: 1,
                size: 10,
            },
        }
    },
    created() {
        this.fetchNews();
        // this.fetchRecommendHealthNews(this.defaultRecommendCount);
        // this.fetchNewsTop(1, 3); 
    },
    methods: {
        // 输入框组件输入回传
        listener(text) {
            this.NewsQueryDto.title = text; 
            this.fetchNews(); 
        },
        newsClick(news) {
          this.$router.push(`/news-detail?id=${news.id}`);
        },
        handleSizeChange(size) {
            this.NewsQueryDto.size = size;
            this.NewsQueryDto.current = 1;
            this.fetchNews();
        },
        handleCurrentChange(current) {
            this.NewsQueryDto.current = current;
            this.fetchNews();
        },
        typeChange(type) {
            this.selectedType = type;
            this.NewsQueryDto.newsTypeId = type.value;
            this.fetchNews();
        },

        async fetchNews() {
            try {
                const { data,total } = await getNewsList(this.NewsQueryDto);
                // console.log("!!!!!!!!!",data);
                this.newsList = data.news;
                this.total = data.count;

                // 按发布时间排序，最新发布的新闻显示在最上面
                this.newsList.sort((a, b) => {
                    // 将createTime转换为Date对象进行比较
                    const dateA = new Date(a.createTime);
                    const dateB = new Date(b.createTime);
                    // 降序排列，日期大的排在前面
                    return dateB - dateA;
                });
            } catch (error) {
                this.$message.info(error.message);
            }
        },
        // 根据新闻ID获取对应的封面图片
        getNewsCover(id) {
            // 根据新闻ID返回对应的图片路径
            // 这里假设图片按照ID命名存储在assets/images/news目录下
            try {
                // 尝试动态导入图片
                return require(`@/assets/cover/${id}.jpg`);
            } catch (e) {
                // 如果找不到对应ID的图片，返回默认图片
                return require('@/assets/cover/test.jpg');
            }
        },
    }
}
</script>


<style scoped>

/* 页面整体样式 */
.news-page {
  width: 100%;
  min-width: 1200px;
}

/* 二级导航 */
.second-nav {
  background-color: #f5f5f5;
  border-bottom: 1px solid #e0e0e0;
}

.nav-container {
  width: 1200px;
  margin: 0 auto;
  display: flex;
  padding: 10px 0;
}

.nav-item {
  margin-right: 30px;
  color: #333;
  text-decoration: none;
  font-size: 14px;
}

.nav-item.active {
  color: #0066cc;
  font-weight: bold;
}

/* 新闻列表容器 */
.news-container {
  width: 1200px;
  margin: 30px auto;
  padding: 0 20px;
}

/* 新闻列表 */
.news-list {
  display: flex;
  flex-direction: column;
  gap: 25px;
}

/* 单条新闻 */
.news-item {
  display: flex;
  background: white;
  border: 1px solid #eee;
  border-radius: 4px;
  overflow: hidden;
  cursor: pointer;
  transition: box-shadow 0.3s;
}

.news-item:hover {
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
}

/* 新闻图片 */
.news-img {
  width: 220px;
  height: 160px;
  flex-shrink: 0;
  overflow: hidden;
}

.img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  /* 保持图片比例，避免拉伸 */
}

/* 新闻内容 */
.news-content {
  flex: 1;
  padding: 20px;
  display: flex;
  flex-direction: column;
}

/* 新闻标题 */
.title {
  font-size: 18px;
  color: #333;
  margin: 0 0 15px 0;
  transition: color 0.3s;
}

.news-item:hover .title {
  color: #0066cc;
}

/* 元数据（日期、来源等） */
.meta {
  display: flex;
  gap: 20px;
  color: #999;
  font-size: 12px;
  margin-bottom: 10px;
}

/* 新闻摘要 */
.brief {
  color: #666;
  font-size: 14px;
  line-height: 1.6;
  margin: 0;
  flex: 1;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.pager {
    display: flex;
    justify-content: center;
}

</style>
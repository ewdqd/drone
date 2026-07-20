<template>
    <div class="news-container">
        <div class="left">
            <div>
                <el-page-header @back="goBack" content="详情页面">
                </el-page-header>
            </div>
            <div class="detail">
                <div class="title">{{ news.title }}</div>
                <div class="point">
                    <div class="flow-index">
                        <span>
                            发布于{{ news.createTime }}
                        </span>
                        <span>
                            {{ news.viewCount }}人阅读
                        </span>
                        <span>
                            {{ news.collectionCount }}人收藏
                        </span>
                        <span>
                            {{ news.upvoteCount }}人点赞
                        </span>
                    </div>
                    <div><el-tag size="mini" type="success">{{ news.newsTypeName }}</el-tag></div>
                </div>
                <div class="summary">{{ news.summary }}</div>
                <div class="content" v-html="news.content"></div>
            </div>
            <div>
                <Evaluations contentType="NEWS" :contentId="Number(id)" :userId="Number(userId)"
                    :avatar="String(avatar)" />
            </div>
        </div>
        <!-- 行为操作区 -->
        <div class="operation">
            <div v-if="!collectionOperationList.length">
                <div @click="collectionOperation">
                    <i class="el-icon-star-off"></i>
                    收藏
                </div>

            </div>
            <div v-else>
                <div @click="collectionOperation">
                    <i class="el-icon-star-on"></i>
                    取消收藏
                </div>

            </div>
            <div v-if="!upvoteList.length">
                <div @click="upvoteOperation">
                    <img src="/update-active.png" alt="">
                    点赞
                </div>

            </div>
            <div v-else>
                <div @click="upvoteOperation">
                    <img src="/update-default.png" alt="">
                    取消点赞
                </div>

            </div>
        </div>
        <div class="right">
            <div style="font-size: 24px;margin-bottom: 10px;font-weight: 600;">新闻推荐</div>
            <template v-if="recommendNews.length">
                <div style="margin-bottom: 10px;" v-for="item in recommendNews" :key="item.id">
                    <img style="width: 100%;height: 140px;border-radius: 5px;object-fit: cover;" :src="item.cover || ''"
                        alt="新闻封面">
                    <div @click="read(item.id)" class="news-title">
                        {{ item.title }}
                    </div>
                </div>
            </template>
            <div v-else class="no-news">
                <i class="el-icon-news"></i>
                <span>暂无最新新闻</span>
            </div>
        </div>
    </div>
</template>

<script>
import { saveActionEntity } from '@/api/manage.js';
export default {
    data() {
        return {
            id: null,
            news: {},
            userId: 0,
            avatar: '',
            collectionOperationList: [],
            upvoteList: [],
            startTime: null, // 开始阅读时间
            recommendNews: [],
        }
    },
    async created() {
        await this.fetchUserInfo();
        this.id = this.$router.currentRoute.query.id;
        this.read(this.id);
        this.fetchRecommendHealthNews(3); // 默认推荐3条
    },
    methods: {
        read(id) {
            this.id = id;
            this.fetchDetail(id); // 通过ID查询新闻详情
            this.saveViewOperation(id); // 记录用户的阅读行为
            this.initStartTime();
            window.addEventListener('beforeunload', this.endTimeClose);
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
                await saveActionEntity(dto);
            } catch (error) {
                this.$message.warning(error.message);
                console.error('记录停留时长情况异常:', error);
            }
        },
        initStartTime() {
            this.startTime = new Date().getTime(); // 返回当前时间的时间戳
        },
        // 记录阅读行为
        async saveViewOperation(id) {
            try {
                const dto = {
                    type: 2, //声明是阅读行为
                    newsId: id
                }
                await saveActionEntity(`/action-operation/saveEntity`, dto);
            } catch (error) {
                this.$message.warning(error.message);
                console.error('记录行为操作情况异常:', error);
            }
        },
        goBack() {
            this.$router.push('/user');
        },
        async collectionOperation() {
            try {
                const dto = {
                    type: 3, //声明是收藏行为
                    newsId: this.id
                }
                await this.$axios.post(`/action-operation/operation`, dto);
                this.fetchCollectionInfo(this.id);
                this.fetchDetail(this.id); // 通过ID查询新闻详情
                this.$notify({
                    title: '收藏操作',
                    message: this.collectionOperationList.length ? '取消收藏成功' : '收藏成功',
                    type: 'success',
                    duration: 1000,
                    position: 'bottom-right'
                });
            } catch (error) {
                this.$message.warning(error.message);
                console.error('收藏行为操作异常:', error);
            }
        },
        async fetchUpvoteInfo(id) {
            try {
                const dto = {
                    type: 1, //声明是点赞行为
                    newsId: id
                }
                const { data } = await this.$axios.post(`/action-operation/list`, dto);
                this.upvoteList = data;
            } catch (error) {
                this.$message.warning(error.message);
                console.error('查询点赞行为操作情况异常:', error);
            }
        },
        async upvoteOperation() {
            try {
                const dto = {
                    type: 1, //声明是收藏行为
                    newsId: this.id
                }
                await this.$axios.post(`/action-operation/operation`, dto);
                this.fetchUpvoteInfo(this.id);
                this.fetchDetail(this.id); // 通过ID查询新闻详情
                this.$notify({
                    title: '点赞操作',
                    message: this.collectionOperationList.length ? '已取消点赞' : '点赞成功',
                    type: 'success',
                    duration: 1000,
                    position: 'bottom-right'
                });
            } catch (error) {
                this.$message.warning(error.message);
                console.error('点赞行为操作异常:', error);
            }
        },
        async fetchCollectionInfo(id) {
            try {
                const dto = {
                    type: 3, //声明是收藏行为
                    newsId: id
                }
                const { data } = await this.$axios.post(`/action-operation/list`, dto);
                this.collectionOperationList = data;
            } catch (error) {
                this.$message.warning(error.message);
                console.error('查询收藏行为操作情况异常:', error);
            }
        },
        async fetchUserInfo() {
            try {
                const { data } = await this.$axios.get(`/user/auth`);
                this.userId = data.id; // 用户ID
                this.avatar = data.avatar; // 用户头像
            } catch (error) {
                this.$message.warning(error.message);
                console.error('查询用户信息异常:', error);
            }
        },
        async fetchDetail(id) {
            try {
                const { data } = await this.$axios.get(`/news/${id}`);
                this.news = { ...data };
            } catch (error) {
                this.$message.warning(error.message);
                console.error('查询新闻详情异常:', error);
            }
        },
    }
}
</script>

<style scoped lang="scss">
.news-container {
    margin: 0 auto;
    width: 1000px;
    padding: 30px 60px;
    box-sizing: border-box;
    display: flex;
    justify-content: left;
    gap: 30px;

    .operation {
        position: fixed;
        padding: 20px 10px;
        box-shadow: 0 4px 6px rgb(240, 240, 240);
        border-radius: 20px;
        top: 300px;
        left: 80px;

        div {
            padding: 6px 10px;
            cursor: pointer;
            font-weight: 500;
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 6px;

            img {
                width: 20px;
                height: 20px;
            }

            i {
                font-size: 20px;
            }

            &:hover {
                background-color: rgb(246, 246, 246);
                border-radius: 4px;
            }
        }
    }

    .left {
        width: 70%;

        .detail {
            padding-block: 20px;

            .point {
                margin-block: 20px;
                display: flex;
                justify-content: space-between;

                .flow-index {
                    display: flex;
                    gap: 12px;
                    font-weight: 500;
                    color: rgb(106, 86, 86);
                }
            }

            .title {
                font-size: 30px;
                padding-block: 10px;
                font-weight: 600;
                position: sticky;
                top: 0;
                z-index: 100;
                background-color: rgb(255, 255, 255);
            }

            .summary {
                padding: 10px;
                box-sizing: border-box;
                background-color: rgb(248, 248, 248);
                margin-block: 10px;
                font-size: 14px;
            }
        }
    }

    .right {
        width: 30%;
        box-shadow: 0 6px 12px rgb(240, 240, 240);
        padding: 10px 30px;
        box-sizing: border-box;
        border-radius: 5px;

        .no-news {
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            height: 200px;
            color: #999;

            i {
                font-size: 40px;
                margin-bottom: 10px;
            }
        }

        .news-title {
            cursor: pointer;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
            margin-top: 8px;

            &:hover {
                color: #409EFF;
            }
        }
    }

}
</style>
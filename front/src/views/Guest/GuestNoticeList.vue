<template>
    <div class="container mx-auto px-6 py-8">
        <!-- 面包屑导航 -->
        <div class="breadcrumbs mb-6 flex items-center text-sm">
            <router-link to="/" class="text-primary hover:underline">首页</router-link>
            <span class="mx-2">/</span>
            <span class="text-gray-800">通知公告</span>
        </div>

        <!-- 页面标题 -->
        <h1 class="text-2xl font-bold mb-6 text-center text-gray-800">
            通知公告列表
        </h1>

        <!-- 通知列表主体 -->
        <div class="max-w-4xl mx-auto bg-white rounded-lg shadow-sm p-6 border border-gray-200">
            <table class="min-w-full text-gray-700">
                <thead>
                    <!-- 表头：仅保留“通知标题”“发布时间”，并设置居中 -->
                    <tr class="bg-gray-100">
                        <th class="py-3 px-4 border-b font-semibold text-left">通知标题</th>
                        <th class="py-3 px-4 border-b font-semibold text-left">发布时间</th>
                    </tr>
                </thead>
                <tbody>
                    <!-- 仅保留标题+时间列，内容左对齐，点击标题跳转详情 -->
                    <tr v-for="notice in noticeList" :key="notice.id" class="hover:bg-gray-50 transition-colors">
                        <td class="py-3 px-4 border-b text-left">
                            <router-link :to="{ name: 'NoticeDetail', query: { id: notice.id } }"
                                class="text-gray-800 hover:text-primary transition-colors">
                                {{ notice.title }}
                            </router-link>
                        </td>
                        <td class="py-3 px-4 border-b text-gray-600 text-left">{{ notice.date }}</td>
                    </tr>
                </tbody>
            </table>

            <!-- 无数据兜底 -->
            <p v-if="noticeList.length === 0" class="text-center py-6 text-gray-500">暂无通知公告</p>
        </div>
    </div>
</template>

<script>
export default {
    name: 'GuestNoticeList',
    data() {
        return {
            noticeList: [
                {
                    id: '1',
                    title: '“宏志助航计划”西安邮电大学2026届毕业生 秋季就业洽谈会（综合类第五场）参会企业名录',
                    date: '2025-11-25'
                },
                {
                    id: '2',
                    title: '关于做好我校2026年“硕博连读”研究生推荐选拔工作的通知',
                    date: '2025-11-24'
                },
                {
                    id: '3',
                    title: '西安邮电大学2026年博士研究生招生简章',
                    date: '2025-11-24'
                },
                {
                    id: '4',
                    title: '汇丰软件开发（广东）有限公司校内宣讲会',
                    date: '2025-11-20'
                }, {
                    id: '5',
                    title: '中国核工业第五建设有限公司校内宣讲会',
                    date: '2025-11-19'
                }
            ]
        };
    },
    mounted() {
        window.scrollTo(0, 0);
    }
};
</script>

<style scoped>
/* 表格响应式适配 */
@media (max-width: 640px) {
    table {
        font-size: 0.875rem;
    }

    th,
    td {
        padding: 2px 4px;
    }
}
</style>
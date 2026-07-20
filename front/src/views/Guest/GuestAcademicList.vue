<template>
    <div class="container mx-auto px-6 py-8">
        <!-- 面包屑导航 -->
        <div class="breadcrumbs mb-6 flex items-center text-sm">
            <router-link to="/" class="text-primary hover:underline">首页</router-link>
            <span class="mx-2">/</span>
            <span class="text-gray-800">学术信息</span>
        </div>

        <!-- 页面标题 -->
        <h1 class="text-2xl font-bold mb-6 text-center text-gray-800">
            学术信息列表
        </h1>

        <!-- 学术信息列表主体 -->
        <div class="max-w-4xl mx-auto bg-white rounded-lg shadow-sm p-6 border border-gray-200">
            <table class="min-w-full text-gray-700">
                <thead>
                    <tr class="bg-gray-100">
                        <th class="py-3 px-4 border-b text-left font-semibold">学术信息标题</th>
                        <th class="py-3 px-4 border-b text-left font-semibold">发布时间/期刊号</th>
                    </tr>
                </thead>
                <tbody>
                    <!-- 循环渲染学术信息（模拟数据，可扩展） -->
                    <tr v-for="item in academicList" :key="item.id" class="hover:bg-gray-50 transition-colors">
                        <td class="py-3 px-4 border-b">
                            <router-link :to="{ name: 'AcademicDetail', query: { id: item.id } }"
                                class="text-gray-800 hover:text-primary transition-colors">
                                {{ item.title }}
                            </router-link>
                        </td>
                        <td class="py-3 px-4 border-b text-gray-600">{{ item.date }}</td>
                    </tr>
                </tbody>
            </table>

            <!-- 无数据兜底 -->
            <p v-if="academicList.length === 0" class="text-center py-6 text-gray-500">暂无学术信息</p>
        </div>
    </div>
</template>

<script>
export default {
    name: 'GuestAcademicList',
    data() {
        return {
            // 模拟学术信息数据（可替换为后端接口，预留扩展）
            academicList: [
                {
                    id: '1',
                    title: '西邮学报——《IRS辅助多用户BC安全卸载的感知MEC网络资源分配方案》',
                    date: '2025, 06, v.30 1-10'
                },
                {
                    id: '2',
                    title: '西邮学报——《智能反射面辅助 CR-SR的安全边缘计算网络优化方案》',
                    date: '2025, 05, v.30 12-21'
                },
                {
                    id: '3',
                    title: '西邮学报——《一种移动源偏差消除的时频域联合定位算法》',
                    date: '2025, 05, v.30 31-40'
                },
                {
                    id: '4',
                    title: '西邮学报——《IRS辅助无人机反向散射边缘计算网络能效最大化方案》',
                    date: '2025, 04, v.30 9-20'
                },
                {
                    id: '5',
                    title: '西邮学报——《有源可重构智能表面辅助抖动无人机空地安全通信方案》',
                    date: '2025, 04, v.30 29-38'
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
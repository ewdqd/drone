<template>
    <div class="container mx-auto px-6 py-8">
        <!-- 面包屑导航 -->
        <div class="breadcrumbs mb-6 flex items-center text-sm">
            <router-link to="/" class="text-primary hover:underline">首页</router-link>
            <span class="mx-2">/</span>
            <router-link to="/academic-list" class="text-primary hover:underline">学术信息</router-link>
            <span class="mx-2">/</span>
            <span class="text-gray-800">详情</span>
        </div>

        <!-- 学术信息详情主体 -->
        <div class="max-w-4xl mx-auto bg-white rounded-lg shadow-sm p-6 border border-gray-200">
            <!-- 动态标题/时间/部门 -->
            <div class="border-b border-gray-200 pb-4 mb-6">
                <h1 class="text-xl font-bold text-gray-800 mb-2">
                    {{ currentAcademic.title || '暂无通知标题' }}
                </h1>
                <div class="flex text-sm text-gray-500 gap-4 flex-wrap">
                    <span>发布时间/期刊号：{{ currentAcademic.date || '暂无发布时间' }}</span>
                </div>
            </div>

            <!-- 正文区域 -->
            <div class="prose max-w-none text-gray-700 leading-relaxed">
                <p v-if="currentAcademic.id === '1'">
                    <span style="font-weight: 700; font-size: 20px; display: block;">一、论文摘要：</span>
                    &nbsp;&nbsp;&nbsp;&nbsp;针对物联网（Internet of Things,IoT）移动边缘计算（Mobile Edge
                    Computing,MEC）网络中的频谱资源紧张、计算资源不足及信息传输易被窃听与篡改的问题，提出一种智能反射面（Intelligent Reflecting
                    Surface,IRS）辅助多用户反向散射通信（Backscatter Communication,BC）技术安全卸载的感知MEC网络资源分配方案。通过融合认知无线电（Cognitive
                    Radio,CR）、IRS与BC技术，构建以安全MEC网络吞吐量最大化为目标的优化问题，并采用基于块坐标下降（Block Coordinate
                    Descent,BCD）、拉格朗日对偶及二次变换的方法进行联合优化求解。仿真结果表明，所提方案与随机IRS相位和随机波束成形两个方案相比，次用户可完成的任务量分别提升了约260%和178%，验证了其在复杂约束条件下的有效性与可行性。<br><br>
                    <span style="font-weight: 700; font-size: 20px; display: block;">二、发表期刊：</span>
                    《西安邮电大学学报》<br><br>
                    <span style="font-weight: 700; font-size: 20px; display: block;">三、作者信息：</span>
                    刘伯阳&nbsp;孙连锐&nbsp;万宇航&nbsp;李泽&nbsp;贺嘉成<br><br>
                    <span style="font-weight: 700; font-size: 20px; display: block;">四、基金项目：</span>
                    国家自然科学基金项目(61901366); 陕西省自然科学基础研究计划项目(2020JQ-851);
                    陕西省普通高校青年杰出人才支持计划项目<br><br>
                    <span style="font-weight: 700; font-size: 20px; display: block;">五、论文链接：</span>
                    <a href="https://xayd.cbpt.cnki.net/portal/journal/portal/client/paper/599a8d7b44da607182d3fe1b232d5297"
                        target="_blank" class="text-primary hover:underline">点击查看全文</a>
                </p>

                <p v-if="currentAcademic.id === '2'">
                    <span style="font-weight: 700; font-size: 20px; display: block;">一、论文摘要：</span>
                    &nbsp;&nbsp;&nbsp;&nbsp;针对物联网设备算力不足和频谱资源短缺的问题，提出一种智能反射面辅助的认知无线电(Cognitive Radio, CR)和共生无线电(Symbiotic
                    Radio, SR)的安全移动边缘计算(Mobile Edge Computing,
                    MEC)网络优化方案。该方案先根据构建的MEC网络模型建立次用户总平均计算任务量最大化问题，然后采用变量替换和分块迭代算法将所建立的非凸问题转换为凸问题，并利用拉格朗日对偶算法进行求解。通过优化次用户发射波束成形矢量、次用户设备的计算资源以及智能反射面的相移矩阵，在保证次用户信息传输安全的前提下最大限度提高次用户总平均计算任务量。仿真结果表明，与随机智能反射面相位方案和随机发射波束成形矢量方案相比，所提方案分别将计算任务量提高了38.5%和32.2%,能够为算力不足和频谱资源受限的用户设备提供更好的服务。<br><br>
                    <span style="font-weight: 700; font-size: 20px; display: block;">二、发表期刊：</span>
                    《西安邮电大学学报》<br><br>
                    <span style="font-weight: 700; font-size: 20px; display: block;">三、作者信息：</span>
                    刘超文&nbsp;蔡雨卓&nbsp;曾安玲&nbsp;李玉泽&nbsp;刘伯阳<br><br>
                    <span style="font-weight: 700; font-size: 20px; display: block;">四、基金项目：</span>
                    陕西省重点研发计划项目(2023-YBGY-272)<br><br>
                    <span style="font-weight: 700; font-size: 20px; display: block;">五、论文链接：</span>
                    <a href="https://xayd.cbpt.cnki.net/portal/journal/portal/client/paper/b5bf51a1f7f1f20760e4e9427095b717"
                        target="_blank" class="text-primary hover:underline">点击查看全文</a>
                </p>

                <p v-if="currentAcademic.id === '3'">
                    <span style="font-weight: 700; font-size: 20px; display: block;">一、论文摘要：</span>
                    &nbsp;&nbsp;&nbsp;&nbsp;针对基于到达时间差(Time Difference of Arrival, TDOA)和到达频率差(Frequency Difference of
                    Arrival, FDOA)无源定位中存在的偏差问题，提出一种移动源偏差消除的时频域联合定位算法，即偏差减小的约束加权最小二乘(Bias Reduced-Constrained Weighted
                    Least Squares, BR-CWLS)算法。根据TDOA和FDOA联合测量模型，构建约束加权最小二乘(Constrained Weighted Least Squares,
                    CWLS)问题，再引入二次约束条件，将其转化为具有偏差减小能力的BR-CWLS问题并完成求解。该二次约束条件综合考虑CWLS问题中涉及矩阵的二阶噪声和误差，以有效减小由于测量噪声及加权矩阵的近似估计产生的偏差。仿真结果表明，在噪声功率高达-30
                    dB时，所提算法的估计精度可以达到克拉美罗下界(Cramér-Rao Lower Bound, CRLB),且位置定位偏差相较于半正定松弛(Semidefinite Relaxation,
                    SDR)算法、CWLS算法及两步加权最小二乘(Two-Step Weighted Least Squares, TSWLS)算法，分别降低了5.3 dB、5.4 dB及14.7 dB。<br><br>
                    <span style="font-weight: 700; font-size: 20px; display: block;">二、发表期刊：</span>
                    《西安邮电大学学报》<br><br>
                    <span style="font-weight: 700; font-size: 20px; display: block;">三、作者信息：</span>
                    万鹏武&nbsp;段晨晨&nbsp;张泽&nbsp;李浩&nbsp;单洁<br><br>
                    <span style="font-weight: 700; font-size: 20px; display: block;">四、基金项目：</span>
                    国家自然科学基金项目(62101441); 陕西省教育厅重点科学研究计划项目(24JR151)<br><br>
                    <span style="font-weight: 700; font-size: 20px; display: block;">五、论文链接：</span>
                    <a href="https://xayd.cbpt.cnki.net/portal/journal/portal/client/paper/6e0b799e75a0375bb26e825bb44672fd"
                        target="_blank" class="text-primary hover:underline">点击查看全文</a>
                </p>

                <p v-if="currentAcademic.id === '4'">
                    <span style="font-weight: 700; font-size: 20px; display: block;">一、论文摘要：</span>
                    &nbsp;&nbsp;&nbsp;&nbsp;针对物联网(Internet of Things, IoT)边缘计算(Mobile Edge Computing,
                    MEC)网络中建筑物遮挡、信道质量差导致的能耗增大的问题，提出一种智能反射表面(Intelligent Reflecting Surface, IRS)辅助无人机(Unmanned Aerial
                    Vehicle,
                    UAV)反向散射边缘计算网络能效最大化方案。通过优化反向散射过程中用户的反射系数、UAV的传输功率、用户设备的计算资源、IRS的相移矩阵和UAV的轨迹实现系统能效最大化。针对强耦合非凸优化问题，采用基于Dinkelbach设计的交替迭代优化算法，利用拉格朗日对偶、连续凸近似(Successive
                    Convex Approximation,
                    SCA)及最大最小化算法迭代求解。最后，将所提方案与固定UAV轨迹和IRS相移方案及单独固定UAV轨迹方案进行对比，仿真结果表明，所提方案在不同参数下可有效降低系统能耗，降幅最高可达51.2%,能够更有效地优化资源分配并实现系统网络能效最大化。<br><br>
                    <span style="font-weight: 700; font-size: 20px; display: block;">二、发表期刊：</span>
                    《西安邮电大学学报》<br><br>
                    <span style="font-weight: 700; font-size: 20px; display: block;">三、作者信息：</span>
                    刘伯阳&nbsp;赵云&nbsp;党儒鸽&nbsp;孙连锐&nbsp;吕涛<br><br>
                    <span style="font-weight: 700; font-size: 20px; display: block;">四、基金项目：</span>
                    国家自然科学基金项目(61901366); 陕西省自然科学基础研究计划项目(2020JQ-851); 陕西省普通高校青年杰出人才支持计划项目<br><br>
                    <span style="font-weight: 700; font-size: 20px; display: block;">五、论文链接：</span>
                    <a href="https://xayd.cbpt.cnki.net/portal/journal/portal/client/paper/e9f7a280d3200d71adcf98272088cb75"
                        target="_blank" class="text-primary hover:underline">点击查看全文</a>
                </p>

                <p v-if="currentAcademic.id === '5'">
                    <span style="font-weight: 700; font-size: 20px; display: block;">一、论文摘要：</span>
                    &nbsp;&nbsp;&nbsp;&nbsp;针对随机气流和无人机(Unmanned Aerial Vehicles,
                    UAV)机体振动引起的抖动影响UAV安全通信性能的问题，提出一种基于有源可重构智能表面(Reconfigurable Intelligence Surface,
                    RIS)的辅助抖动UAV空地安全通信方案。该方案采用泰勒展开的线性近似方法表征仰角和方位角变化所产生的信道误差，在用户保密速率约束下，优化UAV机载基站的预编码矩阵和有源RIS的反射系数，以最小化机载基站的发射功率。将对应的优化问题分解为发射端预编码矩阵和有源RIS反射系数优化两个子问题，并通过交替优化方法求解。仿真结果表明，所提方案可以使UAV机载基站的发射功率在UAV抖动量为0.02的情况下降低10倍，能够在UAV抖动情况下保障用户安全通信速率。<br><br>
                    <span style="font-weight: 700; font-size: 20px; display: block;">二、发表期刊：</span>
                    《西安邮电大学学报》<br><br>
                    <span style="font-weight: 700; font-size: 20px; display: block;">三、作者信息：</span>
                    戈艺萌&nbsp;刘浩宇&nbsp;万鹏武&nbsp;刘伯阳&nbsp;刘超文&nbsp;任远<br><br>
                    <span style="font-weight: 700; font-size: 20px; display: block;">四、基金项目：</span>
                    陕西省教育厅科学研究计划项目(24JK0668)<br><br>
                    <span style="font-weight: 700; font-size: 20px; display: block;">五、论文链接：</span>
                    <a href="https://xayd.cbpt.cnki.net/portal/journal/portal/client/paper/30a53711dd830127fd8cb56dcf6f98de"
                        target="_blank" class="text-primary hover:underline">点击查看全文</a>
                </p>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'GuestAcademicDetail',
    data() {
        return {
            currentAcademic: {},
            allAcademic: [
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
    created() {
        // 从路由参数获取ID，匹配对应数据
        const academicId = this.$route.query.id;
        this.currentAcademic = this.allAcademic.find(item => item.id === academicId) || {};
    },
    mounted() {
        window.scrollTo(0, 0);
    }
};
</script>

<style scoped>
.prose {
    font-size: 1rem;
    line-height: 1.8;
}

.prose p {
    margin-bottom: 1rem;
}
</style>
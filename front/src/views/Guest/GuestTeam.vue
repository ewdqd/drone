<template>
  <div class="team-page section-padding bg-gray-50">
    <div class="container mx-auto px-4">
      <!-- 教师队伍区域 -->
      <div class="mb-16">
        <div class="flex items-center mb-6">
          <h3 class="text-3xl font-bold text-blue-800 flex items-center gap-2">
            <svg viewBox="0 0 24 24" width="28" height="28" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2"/>
            <circle cx="9" cy="7" r="4"/>
            <path d="M22 21v-2a4 4 0 0 0-3-3.87"/>
            <path d="M16 3.13a4 4 0 0 1 0 7.75"/>
            </svg>
            师资队伍
          </h3>
          <div class="flex-grow h-0.5 bg-blue-400 ml-3"></div>
        </div>
        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
          <router-link 
            v-for="teacher in teachers" 
            :key="teacher.id"
            :to="`/team-detail/${teacher.id}`"
            class="block"
          >
            <div class="team-card bg-white rounded-xl shadow hover:shadow-xl p-5 border border-gray-100 hover:border-blue-200">
              <div class="aspect-square mb-4 overflow-hidden rounded-lg">
                <img 
                  :src="teacher.avatar" 
                  alt="教师头像" 
                  class="w-full h-full object-cover transition-transform hover:scale-105 duration-500"
                >
              </div>
              <h4 class="font-bold text-xl mb-2 text-gray-800">{{ teacher.name }}</h4>
              <p class="text-gray-600 mb-3">{{ teacher.title }}</p>
              <p class="text-gray-700 text-sm line-clamp-3">{{ teacher.desc }}</p>
            </div>
          </router-link>
        </div>
      </div>

      <!-- 学生风采区域 -->
      <div class="mb-16">
        <div class="flex items-center mb-6">
          <h3 class="text-3xl font-bold text-blue-800 flex items-center gap-2">
            <svg viewBox="0 0 24 24" width="28" height="28" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2"/>
            <circle cx="9" cy="7" r="4"/>
            <path d="M22 21v-2a4 4 0 0 0-3-3.87"/>
            <path d="M16 3.13a4 4 0 0 1 0 7.75"/>
            </svg>
            学生风采
          </h3>
          <div class="flex-grow h-0.5 bg-blue-400 ml-3"></div>
        </div>

        <!-- 分组按钮 -->
        <div class="mb-6 flex flex-wrap gap-3">
          <!-- 软件组 -->
          <button 
            @click="goToGroup('软件组')"
            :class="activeGroup === '软件组' ? 'active' : ''"
            class="px-4 py-2 bg-white border border-gray-200 rounded-full flex items-center gap-2 text-sm font-medium text-gray-700 hover:border-blue-400 hover:text-blue-600 transition-all shadow-sm"
          >
            <svg viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <rect x="2" y="3" width="20" height="14" rx="2" ry="2"/>
              <line x1="8" y1="21" x2="16" y2="21"/>
              <line x1="12" y1="17" x2="12" y2="21"/>
            </svg>
            软件组 
            <span class="bg-gray-200 text-gray-700 rounded-full w-5 h-5 flex items-center justify-center text-xs">5</span>
          </button>

          <!-- 硬件组 -->
          <button 
            @click="goToGroup('硬件组')"
            :class="activeGroup === '硬件组' ? 'active' : ''"
            class="px-4 py-2 bg-white border border-gray-200 rounded-full flex items-center gap-2 text-sm font-medium text-gray-700 hover:border-blue-400 hover:text-blue-600 transition-all shadow-sm"
          >
            <svg viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <polyline points="16 18 22 12 16 6"/>
              <polyline points="8 6 2 12 8 18"/>
            </svg>
            硬件组 
            <span class="bg-gray-200 text-gray-700 rounded-full w-5 h-5 flex items-center justify-center text-xs">8</span>
          </button>

          <!-- 理论组 -->
          <button 
            @click="goToGroup('理论组')"
            :class="activeGroup === '理论组' ? 'active' : ''"
            class="px-4 py-2 bg-white border border-gray-200 rounded-full flex items-center gap-2 text-sm font-medium text-gray-700 hover:border-blue-400 hover:text-blue-600 transition-all shadow-sm"
          >
            <svg viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M2 3h6a4 4 0 0 1 4 4v14a3 3 0 0 0-3-3H2z"/>
              <path d="M22 3h-6a4 4 0 0 0-4 4v14a3 3 0 0 1 3-3h7z"/>
            </svg>
            理论组 
            <span class="bg-gray-200 text-gray-700 rounded-full w-5 h-5 flex items-center justify-center text-xs">2</span>
          </button>

          <!-- 其他组 -->
          <button 
            @click="goToGroup('其他组')"
            :class="activeGroup === '其他组' ? 'active' : ''"
            class="px-4 py-2 bg-white border border-gray-200 rounded-full flex items-center gap-2 text-sm font-medium text-gray-700 hover:border-blue-400 hover:text-blue-600 transition-all shadow-sm"
          >
            <svg viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <circle cx="12" cy="12" r="10"/>
              <line x1="12" y1="8" x2="12" y2="12"/>
              <line x1="12" y1="16" x2="12.01" y2="16"/>
            </svg>
            其他组 
            <span class="bg-gray-200 text-gray-700 rounded-full w-5 h-5 flex items-center justify-center text-xs">21</span>
          </button>
        </div>

        <!-- 学生信息 - 调整网格布局为更紧凑的5列 -->
        <div class="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 gap-4">
          <div 
            v-for="student in filteredStudents" 
            :key="student.id"
            class="student-card bg-white rounded-xl shadow hover:shadow-lg transition-all p-4 flex flex-col items-center text-center"
          >
            <div class="w-20 h-20 mb-3 relative">
              <div class="absolute inset-0 rounded-full bg-blue-100 opacity-50"></div>
              <img 
                :src="student.avatar" 
                alt="学生头像" 
                class="w-full h-full object-cover rounded-full border-3 border-white shadow-sm"
              >
            </div>
            <h4 class="font-bold text-base mb-1 text-gray-800">{{ student.name }}</h4>
            <p class="text-gray-600 text-xs">{{ student.grade }}</p>
          </div>
        </div>
      </div>

      <!-- 优秀毕业生区域-->
      <div>
        <div class="flex items-center mb-6">
          <h3 class="text-3xl font-bold text-blue-800 flex items-center gap-2">
            <svg viewBox="0 0 24 24" width="28" height="28" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M22 10v6M2 10l10-5 10 5-10 5z"/>
              <path d="M6 12v5c3 3 9 3 12 0v-5"/>
            </svg>
            毕业生去向
          </h3>
          <div class="flex-grow h-0.5 bg-blue-400 ml-3"></div>
        </div>
        
        <!-- 网格布局 - 移动端1列，桌面端2列 -->
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div 
            v-for="grad in graduatedStudents" 
            :key="grad.id"
            class="bg-white rounded-xl shadow p-4 hover:shadow-md transition-all flex flex-col md:flex-row md:items-center justify-between"
          >
            <div>
              <h4 class="font-bold text-lg text-gray-800">{{ grad.name }}</h4>
              <p class="text-gray-600 text-sm">{{ grad.graduationYear }}届 {{ grad.degree }}</p>
            </div>
            <div class="mt-2 md:mt-0 text-right">
              <p class="text-gray-700 font-medium">{{ grad.company }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'GuestTeam',
  data() {
    return {
      activeGroup: '软件组', 
      teachers: [
        { id: 1, name: '刘伯阳', title: '副教授', desc: '研究方向为软件无线电、边缘计算、智能通信...', avatar: require('../../assets/images/LiuBY.jpg') },
        { id: 2, name: '万鹏武', title: '副教授', desc: '研究方向为信号处理、传感网目标定位...', avatar: require('../../assets/images/WanPW.jpg') },
        { id: 3, name: '刘超文', title: '副教授', desc: '研究方向为B5G&6G通信中的索引调制技术...', avatar: require('../../assets/images/LiuCW.jpg') },
        { id: 4, name: '杜炜民', title: '讲师', desc: '研究方向为信号处理...', avatar: require('../../assets/images/DuWM.jpg') },
        { id: 5, name: '吕少卿', title: '副教授', desc: '研究方向为数据挖掘、大数据、图神经网络...', avatar: require('../../assets/images/LvSQ.png') },
        { id: 6, name: '范文', title: '高级工程师', desc: '研究方向为雷达信号设计、阵列信号处理...', avatar: require('../../assets/images/FanW.jpg') },
        { id: 7, name: '郭玉蓉', title: '讲师', desc: '研究方向为6G物联网、随机接入、低功耗广域网...', avatar: require('../../assets/images/GuoYR.png') },
      ],
      students: [
        { id: 1, name: '刘心如', grade: '2024级 硕士', group: '软件组', avatar: require('../../assets/images/Girl.jpg') },
        { id: 2, name: '贾申岳', grade: '2024级 硕士', group: '软件组',avatar: require('../../assets/images/Boy.jpg') },
        { id: 3, name: '郭向涛', grade: '2024级 硕士', group: '软件组', avatar: require('../../assets/images/Boy.jpg') },
        { id: 4, name: '李雅睿', grade: '2025级 硕士', group: '软件组', avatar: require('../../assets/images/Girl.jpg') },
        { id: 5, name: '李璐', grade: '2025级 硕士', group: '软件组',  avatar: require('../../assets/images/Girl.jpg') },
        { id: 6, name: '李玉泽', grade: '2025级 博士', group: '理论组', avatar: require('../../assets/images/Girl.jpg') },
        { id: 7, name: '滕嘉丞', grade: '2025级 硕士', group: '理论组', avatar: require('../../assets/images/Boy.jpg') },
        { id: 8, name: '胡拓拓', grade: '2024级 硕士', group: '硬件组', avatar: require('../../assets/images/Boy.jpg') },
        { id: 9, name: '张文琦', grade: '2024级 硕士', group: '硬件组', avatar: require('../../assets/images/Boy.jpg') },
        { id: 10, name: '宋夫文', grade: '2024级 硕士', group: '硬件组', avatar: require('../../assets/images/Boy.jpg') },
        { id: 11, name: '王文熙', grade: '2025级 硕士', group: '硬件组', avatar: require('../../assets/images/Boy.jpg') },
        { id: 12, name: '陈凡', grade: '2025级 硕士', group: '硬件组', avatar: require('../../assets/images/Boy.jpg') },
        { id: 13, name: '李坤', grade: '2025级 硕士', group: '硬件组', avatar: require('../../assets/images/Boy.jpg') },
        { id: 14, name: '江磊', grade: '2025级 硕士', group: '硬件组', avatar: require('../../assets/images/Boy.jpg') },
        { id: 15, name: '李昱龙', grade: '2025级 硕士', group: '硬件组', avatar: require('../../assets/images/Boy.jpg') },
        { id: 16, name: '柯贤渭', grade: '2024级 硕士', group: '其他组', avatar: require('../../assets/images/Boy.jpg') },
        { id: 17, name: '刘佳昕', grade: '2024级 硕士', group: '其他组', avatar: require('../../assets/images/Boy.jpg') },
        { id: 18, name: '李诺', grade: '2024级 硕士', group: '其他组', avatar: require('../../assets/images/Boy.jpg') },
        { id: 19, name: '骆鑫', grade: '2024级 硕士', group: '其他组', avatar: require('../../assets/images/Boy.jpg') },
        { id: 20, name: '戚建坤', grade: '2024级 硕士', group: '其他组', avatar: require('../../assets/images/Boy.jpg') },
        { id: 21, name: '黄涛', grade: '2025级 硕士', group: '其他组', avatar: require('../../assets/images/Boy.jpg') },
        { id: 22, name: '牛俊祎', grade: '2025级 硕士', group: '其他组', avatar: require('../../assets/images/Boy.jpg') },
        { id: 23, name: '陶兴建', grade: '2025级 硕士', group: '其他组', avatar: require('../../assets/images/Boy.jpg') },
        { id: 24, name: '王一飞', grade: '2025级 硕士', group: '其他组', avatar: require('../../assets/images/Girl.jpg') },
        { id: 25, name: '王成洋', grade: '2025级 硕士', group: '其他组', avatar: require('../../assets/images/Boy.jpg') },
        { id: 26, name: '周升伟', grade: '2025级 硕士', group: '其他组', avatar: require('../../assets/images/Boy.jpg') },
        { id: 27, name: '胡超洋', grade: '2025级 硕士', group: '其他组', avatar: require('../../assets/images/Boy.jpg') },
        { id: 28, name: '周晨昊', grade: '2024级 硕士', group: '其他组', avatar: require('../../assets/images/Boy.jpg') },
        { id: 29, name: '党一帆', grade: '2024级 硕士', group: '其他组', avatar: require('../../assets/images/Boy.jpg') },
        { id: 30, name: '闫睿琛', grade: '2024级 硕士', group: '其他组', avatar: require('../../assets/images/Boy.jpg') },
        { id: 31, name: '吴宇', grade: '2024级 硕士', group: '其他组', avatar: require('../../assets/images/Boy.jpg') },
        { id: 32, name: '贾茜', grade: '2024级 硕士', group: '其他组', avatar: require('../../assets/images/Girl.jpg') },
        { id: 33, name: '孙可心', grade: '2025级 硕士', group: '其他组', avatar: require('../../assets/images/Girl.jpg') },
        { id: 34, name: '张泽西', grade: '2025级 硕士', group: '其他组', avatar: require('../../assets/images/Boy.jpg') },
        { id: 35, name: '王信婷', grade: '2025级 硕士', group: '其他组', avatar: require('../../assets/images/Girl.jpg') },
        { id: 36, name: '谢俊豪', grade: '2025级 硕士', group: '其他组', avatar: require('../../assets/images/Boy.jpg') },

      ],
      graduatedStudents: [
        { id: 1, name: '马玉娇', graduationYear: '2020', degree: '硕士', company: '中国移动通信集团陕西有限公司' },
        { id: 2, name: '李伟', graduationYear: '2021', degree: '硕士', company: '西安荣耀终端有限公司'},
        { id: 3, name: '张浩然', graduationYear: '2021', degree: '硕士', company: '中国兵器工业集团爱生无人机试验测试中心'},
        { id: 4, name: '宋佳佳', graduationYear: '2022', degree: '硕士', company: '中国邮政储蓄银行软件研发中心'},
        { id: 5, name: '万奕尧', graduationYear: '2022', degree: '硕士', company: '南京航空航天大学在读博士' },
        { id: 6, name: '魏晨松', graduationYear: '2023', degree: '硕士', company: '上海思朗科技股份有限公司' },
        { id: 7, name: '边嘉慧', graduationYear: '2023', degree: '硕士', company: '北京长焜科技有限公司'},
        { id: 8, name: '马季', graduationYear: '2023', degree: '硕士', company: '国家知识产权局专利局专利审查协作江苏中心' },
        { id: 9, name: '任彦磊', graduationYear: '2024', degree: '硕士', company: '银河航天科技有限公司' },
        { id: 10, name: '刘超', graduationYear: '2024', degree: '硕士', company: '西安爱生技术集团有限公司'},
        { id: 11, name: '万宇航', graduationYear: '2024', degree: '硕士', company: '陕西千山航空电子有限责任公司'},
        { id: 12, name: '王怡心', graduationYear: '2024', degree: '硕士', company: '陕西有色榆林新材料集团有限责任公司'},
        { id: 13, name: '何雯静', graduationYear: '2024', degree: '硕士', company: '西安建筑科技大学华清学院'},
        { id: 14, name: '党儒鸽', graduationYear: '2024', degree: '硕士', company: '联通西部创新研究院有限公司'},
        { id: 15, name: '王丽平', graduationYear: '2024', degree: '硕士', company: '中华联合财产保险股份有限公司'},
        { id: 16, name: '余飞', graduationYear: '2024', degree: '硕士', company: '长江存储科技有限责任公司'},
        { id: 17, name: '李政', graduationYear: '2024', degree: '硕士', company: '陕西烽火电子股份有限公司'},
        { id: 18, name: '孙连锐', graduationYear: '2025', degree: '硕士', company: '小米科技有限责任公司'},
        { id: 19, name: '赵云', graduationYear: '2025', degree: '硕士', company: '小米科技有限责任公司'},
        { id: 20, name: '李泽', graduationYear: '2025', degree: '硕士', company: '爱科微半导体科技（北京）有限公司'},
        { id: 21, name: '蔡雨卓', graduationYear: '2025', degree: '硕士', company: '陕西黄河集团有限公司'},
        { id: 22, name: '史正民', graduationYear: '2025', degree: '硕士', company: '中国通信建设集团设计院有限公司第四分公司'},
        { id: 23, name: '庞晨曦', graduationYear: '2025', degree: '硕士', company: '西安小米通讯技术有限公司'},
        { id: 24, name: '李玉泽', graduationYear: '2025', degree: '硕士', company: '西安邮电大学在读博士'},
        { id: 25, name: '曾安玲', graduationYear: '2025', degree: '硕士', company: 'OPPO 广东移动通信有限公司'},
      ]
    }
  },
  computed: {
    // 根据activeGroup筛选学生
    filteredStudents() {
      return this.students.filter(student => student.group === this.activeGroup)
    }
  },
  methods: {
    // 点击按钮切换分组
    goToGroup(groupName) {
      this.activeGroup = groupName
    }
  }
}
</script>

<style scoped>
.section-padding {
  padding: 4rem 0;
}
.team-card:hover, .student-card:hover {
  transform: translateY(-5px);
  transition: all 0.3s ease;
}
img {
  transition: all 0.3s ease;
}
/* 按钮激活状态样式 */
button.active {
  border-color: #3b82f6;
  background-color: #eff6ff;
  color: #3b82f6;
}
button.active span {
  background-color: #3b82f6;
  color: white;
}
/* 调整学生卡片整体尺寸，使其更紧凑合理 */
.student-card {
  min-height: 160px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
/* 响应式调整，确保在小屏幕上显示正常 */
@media (max-width: 640px) {
  .student-card {
    min-height: 140px;
  }
  .w-20 {
    width: 16rem;
  }
  .h-20 {
    height: 16rem;
  }
}
@media (max-width: 480px) {
  .grid-cols-2 {
    grid-template-columns: 1fr 1fr;
  }
  .student-card {
    padding: 3px;
    min-height: 120px;
  }
  .w-20 {
    width: 14rem;
  }
  .h-20 {
    height: 14rem;
  }
  h4.text-base {
    font-size: 0.875rem;
  }
  p.text-xs {
    font-size: 0.75rem;
  }
}
</style>
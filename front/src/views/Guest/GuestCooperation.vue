<template>
  <div class="cooperation-page section-padding">
    <div class="container mx-auto px-4">
      <!-- 合作咨询 -->
      <div class="bg-white rounded-lg shadow p-6 md:p-8 mb-8">
        <h3 class="text-xl font-bold mb-6 text-blue-800">合作咨询</h3>
        <!-- 横向排布三个咨询信息 -->
        <div class="max-w-5xl mx-auto grid grid-cols-1 md:grid-cols-3 gap-6">
          <!-- 合作热线 -->
          <div class="flex items-start">
            <div class="w-12 h-12 bg-neutral rounded-full flex items-center justify-center mr-4 flex-shrink-0">
              <i class="fa fa-phone text-primary text-xl"></i>
            </div>
            <div>
              <h4 class="text-lg font-medium text-gray-800 mb-1">合作热线</h4>
              <p class="text-gray-600">15809230396</p>
              <p class="text-gray-500 text-sm mt-1">工作日 9:00-18:00</p>
            </div>
          </div>
          <!-- 电子邮箱 -->
          <div class="flex items-start">
            <div class="w-12 h-12 bg-neutral rounded-full flex items-center justify-center mr-4 flex-shrink-0">
              <i class="fa fa-envelope text-primary text-xl"></i>
            </div>
            <div>
              <h4 class="text-lg font-medium text-gray-800 mb-1">电子邮箱</h4>
              <p class="text-gray-600">liuboyang@xupt.edu.cn</p>
              <p class="text-gray-500 text-sm mt-1">我们将尽快回复您的邮件</p>
            </div>
          </div>
          <!-- 长安校区信息 -->
          <div class="flex items-start">
            <div class="w-12 h-12 bg-neutral rounded-full flex items-center justify-center mr-4 flex-shrink-0">
              <i class="fa fa-map-marker text-primary text-xl"></i>
            </div>
            <div>
              <h4 class="text-lg font-medium text-gray-800 mb-2">长安校区</h4>
              <p class="text-gray-600 mb-1">邮政编码：710121</p>
              <p class="text-gray-600">地址：西安市长安区西长安街618号</p>
            </div>
          </div>
        </div>
      </div>

      <!-- 招生信息模块 -->
      <div class="bg-white rounded-lg shadow p-6 md:p-8 mb-8">
        <h3 class="text-xl font-bold mb-6 text-blue-800">招生信息</h3>

        <!-- 基本要求 -->
        <div class="mb-10 max-w-5xl mx-auto">
          <h4 class="text-lg font-semibold mb-4 text-gray-800">实验室基本要求</h4>
          <ul class="list-disc pl-6 text-gray-600 space-y-2">
            <li>具有相关专业如通信工程、电子信息工程、计算机科学与技术等本科学历，具备扎实的专业基础知识</li>
            <li>较好的数学与英语基础、较好的通信原理与数字信号处理基础</li>
            <li>吃苦耐劳、尊重师长、团结同学、思想健康</li>
            <li>具备良好的团队合作精神和沟通能力</li>
            <li>具有较强的自主学习能力和研究潜力</li>
            <li>发表过相关领域学术论文或有项目经验者优先</li>
          </ul>
        </div>

        <!-- 每位老师的具体要求-->
        <div class="max-w-5xl mx-auto">
          <h4 class="text-lg font-semibold mb-6 text-gray-800">导师招生方向及具体要求</h4>

          <!-- 两列网格布局：移动端1列，桌面端2列 -->
          <div class="grid grid-cols-1 gap-8">
            <!-- 老师1 - 折叠面板 -->
            <div class="border border-gray-200 rounded-lg overflow-hidden">
              <div class="flex items-center justify-between p-4 cursor-pointer hover:bg-gray-50 transition-colors"
                @click="toggleTeacher(0)">
                <div class="flex items-center w-full">
                  <div class="flex-1">
                    <h5 class="text-lg font-medium">刘伯阳</h5>
                    <p class="text-gray-600 text-sm line-clamp-1">研究方向：软件无线电、边缘计算、智能通信研究</p>
                  </div>
                  <i class="fa ml-4"
                    :class="expandedTeachers[0] ? 'fa-chevron-up text-primary' : 'fa-chevron-down text-gray-500'"></i>
                </div>
              </div>
              <!-- 折叠内容  -->
              <div class="p-4 bg-gray-50" v-show="expandedTeachers[0]">
                <div class="space-y-3">
                  <div>
                    <p class="font-medium text-gray-700 mb-1">招生要求：</p>
                    <ul class="list-disc pl-6 text-gray-600 space-y-1">
                      <li>目前主要致力于软件无线电的工程化实现，实验室氛围非常好，学生可参与较多实际工程项目，就业无忧</li>
                      <li>想在理论研究中快速成长，想在未来就业中游刃有余，想在研究生阶段奋斗一下子的同学欢迎报考</li>
                      <li>有FPGA、USRP基础优先</li>
                      <li>对前后端开发或ROS+PX4飞控感兴趣优先</li>
                    </ul>
                  </div>
                  <div class="pt-2 border-t border-gray-200">
                    <p class="font-medium text-gray-700 mb-1">联系方式：</p>
                    <p class="text-gray-600">邮箱：liuboyang@xupt.edu.cn | 办公地址：逸夫楼801</p>
                  </div>
                </div>
              </div>
            </div>

            <!-- 老师2 - 折叠面板 -->
            <div class="border border-gray-200 rounded-lg overflow-hidden">
              <div class="flex items-center justify-between p-4 cursor-pointer hover:bg-gray-50 transition-colors"
                @click="toggleTeacher(1)">
                <div class="flex items-center w-full">
                  <div class="flex-1">
                    <h5 class="text-lg font-medium">万鹏武</h5>
                    <p class="text-gray-600 text-sm line-clamp-1">研究方向：信号处理、传感网目标定位、隐蔽通信研究</p>
                  </div>
                  <i class="fa ml-4"
                    :class="expandedTeachers[1] ? 'fa-chevron-up text-primary' : 'fa-chevron-down text-gray-500'"></i>
                </div>
              </div>
              <!-- 折叠内容 -->
              <div class="p-4 bg-gray-50" v-show="expandedTeachers[1]">
                <div class="space-y-3">
                  <div>
                    <p class="font-medium text-gray-700 mb-1">招生要求：</p>
                    <ul class="list-disc pl-6 text-gray-600 space-y-1">
                      <li>欢迎刻苦勤奋，自律意识较强，数学和英语基础良好的同学报考</li>
                      <li>欢迎来信交流</li>
                    </ul>
                  </div>
                  <div class="pt-2 border-t border-gray-200">
                    <p class="font-medium text-gray-700 mb-1">联系方式：</p>
                    <p class="text-gray-600">邮箱：wanpengwu@xupt.edu.cn | 办公地址：逸夫楼801</p>
                  </div>
                </div>
              </div>
            </div>

            <!-- 老师3 - 折叠面板 -->
            <div class="border border-gray-200 rounded-lg overflow-hidden">
              <div class="flex items-center justify-between p-4 cursor-pointer hover:bg-gray-50 transition-colors"
                @click="toggleTeacher(2)">
                <div class="flex items-center w-full">
                  <div class="flex-1">
                    <h5 class="text-lg font-medium">杜炜民</h5>
                    <p class="text-gray-600 text-sm line-clamp-1">研究方向：信号处理</p>
                  </div>
                  <i class="fa ml-4"
                    :class="expandedTeachers[2] ? 'fa-chevron-up text-primary' : 'fa-chevron-down text-gray-500'"></i>
                </div>
              </div>
              <!-- 折叠内容 -->
              <div class="p-4 bg-gray-50" v-show="expandedTeachers[2]">
                <div class="space-y-3">
                  <div>
                    <p class="font-medium text-gray-700 mb-1">招生要求：</p>
                    <ul class="list-disc pl-6 text-gray-600 space-y-1">
                    </ul>
                  </div>
                  <div class="pt-2 border-t border-gray-200">
                    <p class="font-medium text-gray-700 mb-1">联系方式：</p>
                    <p class="text-gray-600">邮箱：duweimin@xupt.edu.cn | 办公地址：逸夫楼801</p>
                  </div>
                </div>
              </div>
            </div>

            <!-- 老师4 - 折叠面板 -->
            <div class="border border-gray-200 rounded-lg overflow-hidden">
              <div class="flex items-center justify-between p-4 cursor-pointer hover:bg-gray-50 transition-colors"
                @click="toggleTeacher(3)">
                <div class="flex items-center w-full">
                  <div class="flex-1">
                    <h5 class="text-lg font-medium">刘超文</h5>
                    <p class="text-gray-600 text-sm line-clamp-1">
                      研究方向：B5G&6G通信中的索引调制技术、物理层信息安全、智能通信、智能超表面使能技术、超大规模MIMO、流体天线</p>
                  </div>
                  <i class="fa ml-4"
                    :class="expandedTeachers[3] ? 'fa-chevron-up text-primary' : 'fa-chevron-down text-gray-500'"></i>
                </div>
              </div>
              <!-- 折叠内容 -->
              <div class="p-4 bg-gray-50" v-show="expandedTeachers[3]">
                <div class="space-y-3">
                  <div>
                    <p class="font-medium text-gray-700 mb-1">招生要求：</p>
                    <ul class="list-disc pl-6 text-gray-600 space-y-1">
                    </ul>
                  </div>
                  <div class="pt-2 border-t border-gray-200">
                    <p class="font-medium text-gray-700 mb-1">联系方式：</p>
                    <p class="text-gray-600">邮箱：cwliuhb@163.com | 办公地址：逸夫楼801</p>
                  </div>
                </div>
              </div>
            </div>

            <!-- 老师5 - 折叠面板 -->
            <div class="border border-gray-200 rounded-lg overflow-hidden">
              <div class="flex items-center justify-between p-4 cursor-pointer hover:bg-gray-50 transition-colors"
                @click="toggleTeacher(4)">
                <div class="flex items-center w-full">
                  <div class="flex-1">
                    <h5 class="text-lg font-medium">吕少卿</h5>
                    <p class="text-gray-600 text-sm line-clamp-1">研究方向：数据挖掘、大数据、图神经网络、工业互联网异常检测</p>
                  </div>
                  <i class="fa ml-4"
                    :class="expandedTeachers[4] ? 'fa-chevron-up text-primary' : 'fa-chevron-down text-gray-500'"></i>
                </div>
              </div>
              <!-- 折叠内容 -->
              <div class="p-4 bg-gray-50" v-show="expandedTeachers[4]">
                <div class="space-y-3">
                  <div>
                    <p class="font-medium text-gray-700 mb-1">招生要求：</p>
                    <ul class="list-disc pl-6 text-gray-600 space-y-1">
                      <li>与其他老师联合招生，对机器学习、数据挖掘有一定基础的同学。</li>
                    </ul>
                  </div>
                  <div class="pt-2 border-t border-gray-200">
                    <p class="font-medium text-gray-700 mb-1">联系方式：</p>
                    <p class="text-gray-600">邮箱：lvsq3601@xupt.edu.cn | 办公地址：逸夫楼801</p>
                  </div>
                </div>
              </div>
            </div>

            <!-- 老师6 - 折叠面板 -->
            <div class="border border-gray-200 rounded-lg overflow-hidden">
              <div class="flex items-center justify-between p-4 cursor-pointer hover:bg-gray-50 transition-colors"
                @click="toggleTeacher(5)">
                <div class="flex items-center w-full">
                  <div class="flex-1">
                    <h5 class="text-lg font-medium">郭玉蓉</h5>
                    <p class="text-gray-600 text-sm line-clamp-1">研究方向：6G物联网、随机接入、低功耗广域网</p>
                  </div>
                  <i class="fa ml-4"
                    :class="expandedTeachers[5] ? 'fa-chevron-up text-primary' : 'fa-chevron-down text-gray-500'"></i>
                </div>
              </div>
              <!-- 折叠内容 -->
              <div class="p-4 bg-gray-50" v-show="expandedTeachers[5]">
                <div class="space-y-3">
                  <div>
                    <p class="font-medium text-gray-700 mb-1">招生要求：</p>
                    <ul class="list-disc pl-6 text-gray-600 space-y-1">
                      <li>认真踏实，具有较好的数学与通信理论基础。</li>
                    </ul>
                  </div>
                  <div class="pt-2 border-t border-gray-200">
                    <p class="font-medium text-gray-700 mb-1">联系方式：</p>
                    <p class="text-gray-600">邮箱：yrguo_1@163.com | 办公地址：逸夫楼801</p>
                  </div>
                </div>
              </div>
            </div>

            <!-- 老师7 - 折叠面板（占满一行） -->
            <div class="border border-gray-200 rounded-lg overflow-hidden">
              <div class="flex items-center justify-between p-4 cursor-pointer hover:bg-gray-50 transition-colors"
                @click="toggleTeacher(6)">
                <div class="flex items-center w-full">
                  <div class="flex-1">
                    <h5 class="text-lg font-medium">范文</h5>
                    <p class="text-gray-600 text-sm line-clamp-1">研究方向：雷达信号设计、阵列信号处理、雷达通信一体化、优化方法</p>
                  </div>
                  <i class="fa ml-4"
                    :class="expandedTeachers[6] ? 'fa-chevron-up text-primary' : 'fa-chevron-down text-gray-500'"></i>
                </div>
              </div>
              <!-- 折叠内容 -->
              <div class="p-4 bg-gray-50" v-show="expandedTeachers[6]">
                <div class="space-y-3">
                  <div>
                    <p class="font-medium text-gray-700 mb-1">招生要求：</p>
                    <ul class="list-disc pl-6 text-gray-600 space-y-1">
                      <li>拥护中国共产党的领导，思想健康，尊重师长，团结协作，具备较强的责任心和吃苦耐劳精神</li>
                      <li>电子信息、通信工程、自动化、微电子、数学等相关专业背景，具备扎实的数学与英语基础</li>
                      <li>掌握雷达原理、信号处理、数字图像处理、通信原理等核心知识，有相关科研项目或工程实践经验者优先</li>
                      <li>具备良好的自主学习能力、逻辑思维能力和创新意识，愿意投身雷达与通信领域的技术攻关工作</li>
                    </ul>
                  </div>
                  <div class="pt-2 border-t border-gray-200">
                    <p class="font-medium text-gray-700 mb-1">联系方式：</p>
                    <p class="text-gray-600">邮箱：fanwen@xupt.edu.cn | 办公地址：逸夫楼801</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 引导模块 -->
      <div class="relative rounded-lg shadow p-8 md:p-12 mb-8 text-center overflow-hidden">
        <!-- 背景层：蓝色渐变 + 无人机图片 -->
        <div class="absolute inset-0 z-0">
          <!-- 蓝色渐变（从深蓝到浅蓝） -->
          <div class="absolute inset-0 bg-gradient-to-br from-blue-800 to-blue-500 opacity-90"></div>
          <!-- 无人机背景图 -->
          <img src="../../assets/images/homeBanner.jpg" alt="无人机背景" class="w-full h-full object-cover opacity-20">
        </div>
        <!-- 内容层（叠加在背景上） -->
        <div class="relative z-10">
          <h3 class="text-2xl md:text-3xl font-bold mb-4 text-white">加入我们，共创低空经济未来</h3>
          <p class="text-blue-50 max-w-2xl mx-auto mb-8 text-lg">
            我们欢迎对低空经济、无人机技术和航空领域感兴趣的研究人员、学生和企业合作伙伴加入我们的团队。
          </p>
          <!-- 联系我们按钮 - 点击触发弹窗 -->
          <button @click="showContactForm = true"
            class="bg-white text-blue-800 px-8 py-3 rounded-lg hover:bg-blue-50 transition-colors font-medium text-lg shadow-lg">
            联系我们
          </button>
        </div>
      </div>

      <!-- 联系我们表单弹窗 -->
      <teleport to="body">
        <div v-if="showContactForm" class="fixed inset-0 bg-black/50 flex items-center justify-center p-4 z-50">
          <div class="bg-white rounded-lg shadow-xl w-full max-w-2xl max-h-[90vh] overflow-y-auto">
            <!-- 弹窗头部 -->
            <div class="p-6 border-b flex justify-between items-center">
              <h3 class="text-xl font-bold text-blue-800">联系我们</h3>
              <button @click="showContactForm = false" class="text-gray-500 hover:text-gray-700">
                <i class="fa fa-times text-xl"></i>
              </button>
            </div>
            <!-- 弹窗表单内容 -->
            <div class="p-6 md:p-8">
              <form @submit.prevent="handleSubmit" class="space-y-6">
                <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                  <div>
                    <label for="name" class="block text-gray-700 mb-2">昵称</label>
                    <input type="text" id="name" v-model="formData.name"
                      class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent"
                      required>
                  </div>
                  <div>
                    <label for="contact" class="block text-gray-700 mb-2">联系方式</label>
                    <input type="text" id="contact" v-model="formData.contact"
                      class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent"
                      required>
                  </div>
                </div>
                <div>
                  <label for="subject" class="block text-gray-700 mb-2">主题</label>
                  <input type="text" id="subject" v-model="formData.subject"
                    class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent"
                    required>
                </div>
                <div>
                  <label for="message" class="block text-gray-700 mb-2">留言内容</label>
                  <textarea id="message" v-model="formData.message" rows="5"
                    class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent"
                    required></textarea>
                </div>
                <div class="flex justify-end gap-4">
                  <button type="button" @click="showContactForm = false"
                    class="px-6 py-3 border border-gray-300 rounded-lg hover:bg-gray-50 transition-colors font-medium">
                    取消
                  </button>
                  <button type="submit"
                    class="bg-primary text-white px-6 py-3 rounded-lg hover:bg-primary/90 transition-colors font-medium">
                    提交留言
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </teleport>
    </div>
  </div>
</template>

<script>
export default {
  name: 'GuestCooperation',
  data() {
    return {
      formData: {
        name: '',
        contact: '',
        subject: '',
        message: ''
      },
      showContactForm: false,
      // 控制老师的展开状态，默认全部折叠
      expandedTeachers: [false, false, false, false, false, false, false]
    }
  },
  methods: {
    handleSubmit() {
      // 表单提交逻辑
      console.log('表单提交数据:', this.formData)
      alert('感谢您的留言，我们会尽快回复您！')
      // 重置表单并关闭弹窗
      this.formData = {
        name: '',
        contact: '',
        subject: '',
        message: ''
      }
      this.showContactForm = false
    },
    // 修复：使用$set确保Vue2数组响应性
    toggleTeacher(index) {
      this.$set(this.expandedTeachers, index, !this.expandedTeachers[index])
    }
  }
}
</script>

<style scoped>
.section-padding {
  @apply py-12 md:py-16;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .grid-cols-3 {
    grid-template-columns: 1fr;
  }

  .md\:ml-\[25\%\] {
    margin-left: 0 !important;
  }
}

/* 自定义中性色（如果未在全局定义） */
.bg-neutral {
  background-color: #f3f4f6;
}

/* 自定义主色 */
.text-primary {
  color: #014a95;
}

.bg-primary {
  background-color: #014a95;
}

.hover\:border-primary:hover {
  border-color: #014a95;
}

.focus\:ring-primary:focus {
  --tw-ring-color: #014a95;
}

/* 背景图相关样式 */
.object-cover {
  object-fit: cover;
}

.opacity-20 {
  opacity: 0.2;
}

.opacity-90 {
  opacity: 0.9;
}

.bg-gradient-to-br {
  background-image: linear-gradient(to bottom right, var(--tw-gradient-stops));
}

.from-blue-800 {
  --tw-gradient-from: #1e3a8a;
  --tw-gradient-to: rgba(30, 58, 138, 0);
  --tw-gradient-stops: var(--tw-gradient-from), var(--tw-gradient-to);
}

.to-blue-500 {
  --tw-gradient-to: #3b82f6;
}

/* 折叠面板样式优化 */
.cursor-pointer {
  cursor: pointer;
}

.transition-colors {
  transition: background-color 0.2s ease;
}

/* 文本溢出处理 */
.line-clamp-1 {
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

/* 两列布局间距优化 */
.gap-8 {
  gap: 2rem;
}
</style>
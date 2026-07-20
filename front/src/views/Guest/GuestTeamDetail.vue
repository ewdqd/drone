<template>
  <div class="team-detail section-padding bg-gray-50 overflow-hidden">
    <div class="container mx-auto px-4">
      <!-- 返回按钮 -->
      <button 
        @click="$router.back()" 
        class="mb-2 inline-flex items-center text-blue-600 hover:text-blue-800 transition-colors"
      >
        <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18"></path>
        </svg>
        返回研究队伍
      </button>
      
      <!-- 详情卡片 -->
      <div class="bg-white rounded-xl shadow-md overflow-hidden">
        <div class="flex flex-col md:flex-row">
          <!-- 左侧头像区域 -->
          <div class="md:w-2/5 bg-gray-100 p-6 flex items-center justify-center flex-shrink-0 h-[50vh] md:h-[70vh]">
            <img 
              :src="teacher.avatar" 
              alt="教师头像" 
              class="object-contain rounded-lg shadow-lg border-4 border-white max-w-full max-h-full object-scale-down"
            >
          </div>
          
          <!-- 右侧详情区域 -->
          <div class="md:w-3/5 p-6 md:p-8 overflow-y-auto h-[45vh] md:h-[70vh]">
            <h2 class="text-3xl font-bold text-gray-800 mb-2">{{ teacher.name }}</h2>
            <p class="text-xl text-gray-600 mb-6">{{ teacher.title }}</p>
            
            <div class="mb-8">
              <h3 class="text-xl font-semibold text-gray-700 mb-3 flex items-center">
                <svg class="w-5 h-5 mr-2 text-blue-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path>
                </svg>
                个人简介
              </h3>
              <div class="text-gray-600 leading-relaxed">
                <p class="text-indent-2em mb-2" v-for="(para, idx) in teacher.introduction.split('\n')" :key="idx">
                  {{ para }}
                </p>
              </div>
            </div>
            
            <div class="mb-8">
              <h3 class="text-xl font-semibold text-gray-700 mb-3 flex items-center">
                <svg class="w-5 h-5 mr-2 text-blue-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2"></path>
                </svg>
                研究方向
              </h3>
              <p class="text-gray-600 leading-relaxed">{{ teacher.desc }}</p>
            </div>
            
            <div class="mb-8">
              <h3 class="text-xl font-semibold text-gray-700 mb-3 flex items-center">
                <svg class="w-5 h-5 mr-2 text-blue-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253"></path>
                </svg>
                教学方向
              </h3>
              <ul class="list-disc pl-5 text-gray-600 space-y-1">
                <li v-for="(direction, index) in teacher.teaching" :key="index">{{ direction }}</li>
              </ul>
            </div>

            <div class="mb-8">
              <h3 class="text-xl font-semibold text-gray-700 mb-3 flex items-center">
                <svg class="w-5 h-5 mr-2 text-blue-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 3v4M3 5h4M6 17v4m-2-2h4m5-16l2.286 6.857L21 12l-5.714 2.143L13 21l-2.286-6.857L5 12l5.714-2.143L13 3z"></path>
                </svg>
                科研奖励
              </h3>
              <ul class="list-disc pl-5 text-gray-600 space-y-1">
                <li v-for="(award, index) in teacher.researchAwards" :key="index">{{ award }}</li>
              </ul>
            </div>
            
            <div class="mb-8">
              <h3 class="text-xl font-semibold text-gray-700 mb-3 flex items-center">
                <svg class="w-5 h-5 mr-2 text-blue-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z"></path>
                </svg>
                科研项目
              </h3>
              <ul class="list-disc pl-5 text-gray-600 space-y-1">
                <li v-for="(project, index) in teacher.researchProjects" :key="index">{{ project }}</li>
              </ul>
            </div>
            
            <div class="mb-8">
              <h3 class="text-xl font-semibold text-gray-700 mb-3 flex items-center">
                <svg class="w-5 h-5 mr-2 text-blue-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
                </svg>
                发表论文
              </h3>
              <ul class="list-disc pl-5 text-gray-600 space-y-1">
                <li v-for="(paper, index) in teacher.publishedPapers" :key="index">{{ paper }}</li>
              </ul>
            </div>
            
            <div class="mb-8">
              <h3 class="text-xl font-semibold text-gray-700 mb-3 flex items-center">
                <svg class="w-5 h-5 mr-2 text-blue-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 14v3m4-3v3m4-3v3M3 21h18M3 10h18M3 7l9-4 9 4M4 10h16v11H4V10z"></path>
                </svg>
                会议与工作论文
              </h3>
              <ul class="list-disc pl-5 text-gray-600 space-y-1">
                <li v-for="(paper, index) in teacher.conferencePapers" :key="index">{{ paper }}</li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'GuestTeamDetail',
  data() {
    return {
      teachers: [
         {
          id: 1,
          name: '刘伯阳',
          title: '副教授',
          introduction: '西安邮电大学硕士生导师，2016年毕业于西安电子科技大学军事通信学专业，获工学博士学位，2017年-今在西安邮电大学通信与信息工程（人工智能）学院工作，担任多个顶级期刊审稿人及顶级会议TPC委员。主要从事边缘计算、软件无线电、智能通信等领域研究，获陕西省高校第三批青年杰出人才支持计划。在发表SCI 与EI等论文三十余篇，其中JCR二区及以上论文十余篇，国家发明专利三项，主持并参与国家自然科学基金、陕西省自然科学基础研究计划、陕西省教育厅项目各一项以及企业横向项目多项。',
          desc: '主要从事软件无线电、边缘计算、智能通信研究。',
          avatar: require('../../assets/images/LiuBY.jpg'),
          teaching: [
            '机器学习',
            '物联网专业英语',
          ],
          researchAwards: [
            '担任多个顶级期刊审稿人及顶级会议TPC委员',
            '2019年10月获批 陕西省高校第三批青年杰出人才支持计划'
          ],
          researchProjects: [
            '国家自然科学基金青年项目，2018. 5G认知小蜂窝网络中深度认知与资源分配技术研究，2018.01',
            '陕西省自然科学基础研究计划项目, 2020. 基于认知的边缘计算技术研究. 2020.01',
            '陕西省教育厅专项科研计划项目, 2019. 高能效认知边缘计算技术研究, 2019.01',
            '企业横向项目，2021. 基于软件无线电的视频传输系统搭建，2021.01',
            '企业横向项目，2021. 边缘计算可行性研究项目，2021.01'
          ],
          publishedPapers: [
            'B. Liu, F. Zhou, G. Lu and R. Q. Hu, "Energy Efficient and Robust Beamforming for MISO Cognitive Small Cell Networks," in IEEE Internet of Things Journal, vol. 5, no. 6, pp. 5002-5014, Dec. 2018.',
            'Boyang Liu, Haoran Zhang, Yiyao Wan, Fuhui Zhou, Qihui Wu and Derrick Wing Kwan Ng, “ Robust Adversarial Attacks on Deep Learning Based RF Fingerprint Identification,” IEEE Wireless Communications Letters, 2023（国际通信权威期刊，中科院二区）',
            'Boyang Liu, Yiyao Wan, Fuhui Zhou, Qihui Wu and Rose Qingyang Hu, “Resource Allocation and Trajectory Design for MISO UAV-Assisted MEC Networks,” vol. 71, no. 5, pp. 4933-4948. IEEE Transactions on Vehicular Technology , 2022（国际通信权威期刊，中科院二区）',
            'Boyang Liu, Yiyao Wan, Fuhui Zhou, Qihui Wu and Rose Qingyang Hu, “Robust Trajectory and Beamforming Design for Cognitive MISO UAV Networks,” vol. 10, no. 2, pp. 396-400. IEEE Wireless Communications Letters, 2021（国际通信权威期刊，中科院二区）',
            'B. Liu*, J. Bai, Y. Ma, J. Wang and G. Lu, "Energy-Efficient Resource Allocation for Wireless Powered Cognitive Mobile Edge Computing," 2019 IEEE International Conference on Communications Workshops (ICC Workshops), Shanghai, China, 2019, pp. 1-6. （通信 A 类会议）'
          ],
          conferencePapers: [
            'B. Liu*, J. Bai, Y. Ma, J. Wang and G. Lu, "Energy-Efficient Resource Allocation for Wireless Powered Cognitive Mobile Edge Computing," 2019 IEEE International Conference on Communications Workshops (ICC Workshops), Shanghai, China, 2019, pp. 1-6. （通信 A 类会议）" in Proc. IEEE ICC, 2023.',
            'Boyang Liu, Zan Li, Guangyue Lu, and Fuhui Zhou, “Spectrum sensing interval optimization and power control for energy efficient cognitive radio networks”, 2017 9th International Conference on Wireless Communications and Signal Processing (WCSP), Nanjing, China, 2017, pp. 1-6 (EI 会议)',
            ' Yuxi Qiang, Guangyue Lu, Boyang Liu and Yuxin Li, "Optimal energy harvesting-aided spectrum sensing sensors scheduling in cognitive radio networks," 2017 9th International Conference on Wireless Communications and Signal Processing (WCSP), Nanjing, China, 2017, pp. 1-5'
          ]
        },
        {
          id: 2,
          name: '万鹏武',
          title: '副教授',
          introduction: '西安邮电大学硕士生导师，中国通信学会会员，中国电子学会会员。2018年毕业于西安电子科技大学通信工程学院，获军事通信学博士学位，2018年4月至今在西安邮电大学通信与信息工程学院（人工智能学院）工作，担任物联网系主任、专业负责人、党支部书记。2020年获校级师德考核优秀个人，2021年被评为校级优秀共产党员。\n主要从事传感器网络定位、隐蔽通信、频谱监测、物联网技术等领域的科学研究，在国内外主流期刊和会议累计发表论文20余篇，申请国家发明公开专利11项，已授权6项。曾受邀担任2019陕西青年科学家大会分论坛技术委员会主席。主持/参与多项科研项目，包括国家自然科学基金项目、国家科技重大专项项目、“十三五”和“十四五”预研课题等。担任无线通信领域多个SCI/EI检索期刊审稿人，以及多个国际会议的程序委员会委员。主讲课程《数字信号处理》、《物联网导论》、《新生研讨课》等课程。',
          desc: '主要从事信号处理、传感网目标定位、隐蔽通信研究。',
          avatar: require('../../assets/images/WanPW.jpg'),
          teaching: [
            '数字信号处理',
          ],
          researchAwards: [
            '万鹏武，2022年，陕西省第四届本科高校课堂创新大赛一等奖（4/4）',
            '万鹏武，2022年，教育部在线教育研究中心“拓金示范课程”（5/5）',
            '万鹏武，2023年，第六届全国高等学校电子信息类专业青年教师授课竞赛决赛全国三等奖'
          ],
          researchProjects: [
            '万鹏武，2022.6-2024.6，面向抗干扰卫星通信的复合干扰分析与识别技术，国防科技重点实验室基金一般项目',
            '万鹏武，2022.01-2024.12，基于多域联合的共形传感器阵列刚体定位方法研究，国家自然科学基金青年项目',
            '万鹏武，2020.01-2021.12，复杂电磁环境下被动定位技术研究，陕西省教育厅科学研究计划一般项目',
          ],
          publishedPapers: [
            '万鹏武 魏健 姚媛媛 彭康 李文杰. 非视距环境下基于时域测量的刚体位姿估计[J]. 北京邮电大学学报, 2023, 46(1): 115-120.',
            'Pengwu Wan, Jian Wei, Jin Wang, Qiongdan Huang. Wireless Sensor Network-Based Rigid Body Localization for NLOS Parameter Estimation[J]. Sensors, 2022, 22(18): 6810.',
            'Pengwu Wan, Qianli Yan, Guangyue Lu, Jin Wang and Qiongdan Huang, “Robust Localization of Signal Source Based on Information Fusion”, IEEE Syst. J., vol. 15, no. 2, pp. 1764–1775, June 2021.',
            '万鹏武, 姚媛媛, 闫千里,等. 能时频域信息融合的信源被动定位[J]. 西安电子科技大学学报, 2021, 48(6):10.',
            'Pengwu Wan, Qiongdan Huang, Guangyue Lu,  Jin Wang, Qianli Yan, Yufei Chen:“Passive Localization of Signal Source Based on UAVs in Complex Environment”China Communications, 2020, 17(2): 107-116.'
          ],
          conferencePapers: [
            'Pengwu Wan, Kang Peng, Danyang Wang, Jin Wang, and Xinyu Zhao, Multi-antenna Covert Communication Based on Spectrum Mask, International Conference on Ubiquitous Communication (Ucom), in China Xi’an, July 7-9, 2023, 1-5.',
            'Pengwu Wan, Yuanyuan Yao, Guangyue Lu, Jian Wei, “Rigid Body Localization Based on Conformal Sensor Array in Non-Line-of-Sight Environment,” IEEE/CIC International Conference on Communications in China (ICCC), in China Xiamen, June, 2021, 1-6.',
            'Pengwu Wan, Jian Wei, Chaowen Liu, Yuanyuan Yao and Guangyue Lu, "Robust Localization Based on RSS and TDOA Combination in Complex Environment," 2021 13th International Conference on Wireless Communications and Signal Processing (WCSP), Changsha, China, 2021, pp. 1-6.'
          ]
        },        
        {
          id: 3,
          name: '刘超文',
          title: '副教授',
          introduction: '西安邮电大学硕士生导师。2018年6月于西安交通大学智能网络与网络安全教育部重点实验室获信息与通信工程专业工学博士学位，后于西安邮电大学通信与信息工程学院任教至今。博士期间受国家留学基金委资助，赴英国南安普顿大学公派联合培养两年。主要研究方向为B5G&6G通信中的索引调制技术、物理层信息安全、智能通信等。在IEEE系列汇刊、快报，及其他知名国内外期刊和国际学术会议上发表论文30余篇，SCI检索国际期刊10余篇，申请发明专利20余项，获授权专利7项。博士至今，担任领域内多个SCI/EI检索国际期刊审稿人、旗舰和知名国际会议TPC Member，多次在国内外学术会议上作成果报告。先后主持国家自然科学青年基金项目、陕西省重点研发计划项目、陕西省教育厅自然科学专项研究项目各一项。作为骨干成员先后参研国家863项目子课题、国家自然科学基金面上项目、教育部博士点项目、陕西省自然科学基金等各类国家级/省部级项目。目前已承担《数字信号处理》、《概率论与数理统计》、《通信概论》、《Matlab与工程应用技术》等课程的教学工作。',
          desc: '主要从事B5G&6G通信中的索引调制技术、物理层信息安全、智能通信、智能超表面使能技术、超大规模MIMO、流体天线研究。',
          avatar: require('../../assets/images/LiuCW.jpg'),
          teaching: [
            '专业通识课：《通信概论》',
            '专业基础课：《数字信号处理》',
            '工程数学课：《概率论与数理统计》',
            '软件实践课：《Matlab与工程应用技术》',
          ],
          researchAwards: [
            '2023年，Ucom最佳论文奖，A Multi-Layer Transmission Paradigm of Augmented Pattern Index Modulation with RIS，中国通信学会。',
          ],
          researchProjects: [
            '全双工基站协助的上行安全空间调制技术研究，国家自然科学基金青年基金，61901366，主持，2020年1月至2022年12月.',
            '面向B5G通信中空间调制辅助的高效安全传输技术。陕西省重点研发计划项目，2023-YBGY-272，主持, 2023年1月至2024年12月.',
            '5G通信中全双工节点辅助的空间调制安全传输策略研究，陕西省教育厅专项科研计划项目，20JK0919，主持，2020年1月至2021年12月.',
            '基于软件无线电的无线通信网络实验平台搭建，横向项目，主持，2022年4月至2022年12月.',
            '调制编码技术的软件无线电实现，横向项目，主持，2020年12月至2021年12月.',
          ],
          publishedPapers: [
            'Chaowen Liu, Lie-Liang Yang, Wenjie Wang, Transmitter-Precoding-Aided Spatial Modulation Achieving Both Transmit and Receive Diversity, IEEE Transactions on Vehicular Technology, 2018年3月.',
            'Chaowen Liu, Lie-Liang Yang, Wenjie Wang, Secure Spatial Modulation With a Full-Duplex Receiver, IEEE Wireless Communications Letters, 2017年12月.',
            'Chaowen Liu, Ji Ma, Pengyu Zhai, Boyang Liu, Tongxing Zheng, Fasong Wang, Enabling joint transmitter-receiver spatial modulation with RF mirrors: A low-cost and highly-flexible perspective, Digital Signal Processing, 2023年3月.',
            'Fasong Wang, Chaowen Liu, Qi Wang, Jiankang Zhang, Rong Zhang, Lieliang Zhang, Lajos Hanzo, Optical Jamming Enhances the Secrecy Performance of the Generalized Space-Shift-Keying-Aided Visible-Light Downlink, IEEE Transactions on Communications, 2018年9月.',
            'Menghan Lin, Wenjie Wang, Chaowen Liu, Preventing Hostile TOA/TDOA Localization With AF Relay,  IEEE Communications Letters, 2023年4月.',
          ],
          conferencePapers: [
            'Fei Yu, Chaowen Liu, Menghan Lin, Tongxing Zheng, Yi He, Boyang Liu, Guangyue Lu, Reconfigurable Intelligent Surface Assisted Transmitter Pre-coding Spatial Modulation, IEEE Global Communications Conference(GLOBECOM), 2023年9月.',
            'Fei Yu, Zhengmin Shi, Chaowen Liu, Menghan Lin, Tongxing Zheng, Yi He, Boyang Liu, Guangyue Lu，Secure Uplink Spatial Modulation Enabled by IRS, IEEE Vehicular Technology Conference, 2023年9月.',
            'Fei Yu, Chaowen Liu, Zhengmin Shi , Boyang Liu, Menghan Lin, Tongxing Zheng, IRS-Enabled Physical Layer Secure Transmission, International Conference on Wireless Communications and Signal Processing (WCSP), 2023年9月.',
            'Ji Ma, Chaowen Liu, Tongxing Zheng, Chao Liu, Guangyue Lu, Augmented Pattern Index Modulation Empowered by RIS, ICC 2022 - IEEE International Conference on Communications, Seoul, Korea, 2022年5月.',
            'Chaowen Liu, Yihua Dong, Boyang Liu, Guangyue Lu, Pengyu Zhai, Enabling Joint Tx-Rx Spatial Modulation with RF Mirrors, 2020 IEEE/CIC International Conference on Communications in China (ICCC), Chongqing, China, 2020年8月.',
          ]
        },
        {
          id: 4,
          name: '杜炜民',
          title: '讲师',
          introduction: '2022年毕业于西安电子科技大学军事通信学专业，获工学博士学位。曾于2016年至2017年任职于青岛鼎信通信股份有限公司西安分公司，参与电力线通信芯片的研发工作。主要从事信号处理及相关领域的科研与教学工作。近年来，以第一作者或核心作者身份发表SCI收录学术论文数篇；主持及核心参与多项国家自然科学基金项目，同时主持相关领域企业横向合作项目多项。',
          desc: '主要从事信号处理研究。',
          avatar: require('../../assets/images/DuWM.jpg'),
          teaching: [
            '通信工程项目管理',
          ],
          researchAwards: [
          ],
          researchProjects: [
            '国家自然科学基金青年项目，索引AFDM赋能的大带宽低功耗传输机制研究',
          ],
          publishedPapers: [
            'Weimin Du, Dongmin Li, Fan Li, Fei Han "Sorting-Based Soft-Demapping Algorithm for LoRa Systems," IEEE Communication Letters, 2024, 5(66): 4453-4457.',
            'Weimin Du and Zujun Liu. Beam Shaping for Massive MIMO Systems With One-Bit Spatial Σ-Δ ADCs. IEEE Transactions on Vehicular Technology, 72(2):1831–1846, 2023.',
            'Weimin Du, Zujun Liu, "Dominant Path Zero-forcing Based on Low-rank Matrix Approximation for Full-dimensional Massive MIMO Systems," IEEE Transactions on Vehicular Technology, 2021, 70(3): 2888-2893',
            'Weimin Du, Zujun Liu, "Throughput-based Energy and Spectral Efficiency Tradeoff for Massive MIMO Systems," IET Communications, 2021, 15(17): 2224-2236.',
            'Zujun Liu, Weimin Du, Dechun Sun. "Energy and Spectral Efficiency Tradeoff for Massive MIMO Systems with Transmit Antenna Selection," IEEE Transactions on Vehicular Technology, 2017,5(66): 4453-4457.',
          ],
          conferencePapers: [
          ]
        },
        {
          id: 5,
          name: '吕少卿',
          title: '副教授',
          introduction: '西安邮电大学硕士生导师。2016年毕业于西安电子科技大学信息安全专业，获工学博士学位，2016年至今在西安邮电大学通信与信息工程学院工作，2019年9月-2020年8月中南大学计算机学院访问学者（中组部“西部之光”）。主要从事数据挖掘、图神经网络、工业互联网异常检测等研究。在国内外主流会议和期刊发表论文20余篇。主持(完成)省级项目1项，厅局级自然科学基金2项，其他横向若干。\n华为、阿里云、腾讯云认证讲师，通过华为HCIA-BigData、阿里云ACP大数据分析、腾讯大数据工程师等认证。给陕西移动、北京电信、上海证券、广州地铁、中国银行、上海商飞、宁德核电等全国三十余企事业单位开展技术培训。',
          desc: '主要从事数据挖掘、大数据、图神经网络、工业互联网异常检测。',
          avatar: require('../../assets/images/LvSQ.png'),
          teaching: [
          ],
          researchAwards: [
            '2022、2021连续两年获得华为“智能基座”奖教金',
            '2021年，华为ICT大赛“优秀指导教师”（省级二等奖）',
          ],
          researchProjects: [
            '陕西省科技厅,青年项目, 2022JQ-675, 基于网络嵌入的属性二分网络异常检测研究, 2022-01 至 2023-12, 5万元, 在研, 主持',
            '陕西省能源大数据智能处理省市共建重点实验室, 开放基金, IPBED10, 基于动态网络表示学习的能源大数据异常检测研究, 2020-07 至 2021-12, 2万元, 结题, 主持',
            '咸阳市科技局, 科技计划项目, 201810, 面向时空大数据的用户行为分析及预测, 2018-01 至 2020-06, 10万元, 结题, 主持',
            '陕西省教育厅, 自然科学研究项目, 17JK0703, 大规模社交网络中社区发现算法及应用研究, 2017-01至 2018-12, 2万元, 结题, 主持',
            '横向项目，企业培训（大数据、数据挖掘方面），累计60余万元',
          ],
          publishedPapers: [
            'Shaoqing Lv*, Ju Xiang, Yiyang Li, Xincheng Ren, Guangyue Lu. MERP: motifs enhanced network embedding based on edge reweighting preprocessing. Frontiers in Physics, 2022: 1040.',
            'Shaoqing Lv*, Ju Xiang, Jingyu Feng, Hongguang Wang, Guangyue Lu, Min Li. Community enhancement network embedding based on edge reweighting preprocessing, Journal of Statistical Mechanics: Theory and Experiment, 2020, 2020(10): 0-18.'
          ],
          conferencePapers: [
          ]
        },
        {
          id: 6,
          name: '范文',
          title: '高级工程师',
          introduction: '范文，男，汉族，中共党员，工学博士，高级工程师。2021年3月毕业于西北工业大学电子科学与技术专业，获工学博士学位；2020年1月-2020年8月赴意大利那不勒斯费德里克二世大学开展博士联合培养。2021年6月入职中国电科54所，先后在创新中心、总体部担任工程师、高级工程师，长期深耕雷达与通信领域的科研创新工作。师从西北工业大学博导卢广山研究员（中航工业原总工程师、国家产业基础委员会专家委员）、梁军利教授，合作导师包括IEEE Fellow Hing Cheung So（香港城市大学）、Jian Li（美国佛罗里达州大学）、Marco Lops（意大利费德里克二世大学）等国际知名专家。研究方向覆盖雷达信号设计、阵列信号处理、雷达通信一体化等多个前沿领域，以第一作者发表SCI/EI论文10余篇；授权/申请国家发明专利10余项；主持或核心参与国家级重大科技专项、研究所发展基金、博士创新基金等项目10余项。担任IEEE TSP、IEEE TAP、IEEE TAES、《雷达学报》等多个国际国内权威期刊审稿人，具备扎实的理论基础、丰富的工程实践经验和广泛的学术交流资源。',
          desc: '主要从事雷达信号设计、阵列信号处理、雷达通信一体化、优化方法研究。',
          avatar: require('../../assets/images/FanW.jpg'),
          teaching: [
            '暂无明确课堂教学任务，侧重科研项目实践指导，聚焦工程研发能力与学术创新思维培养，助力团队成员提升技术攻关与成果转化能力。欢迎对雷达与通信一体化、信号处理等方向有兴趣的同学交流合作。',
          ],
          researchAwards: [
            '2024年 河北省科学技术进步二等奖',
            '2023年 西北工业大学优秀博士学位论文',
            '2022年 中国电科54所记功荣誉称号',
            '2021年 陕西省优秀毕业生',
            '2021年 西北工业大学优秀毕业生',
            '2020年 西北工业大学优秀研究生标兵',
            '2019年 首届华为—西北工业大学博士学术资助计划资助',
          ],
          researchProjects: [
            'xxx创新特区项目，xxx信息调制与高校利用',
          ],
          publishedPapers: [
            'W. Fan, X. Fan, J. Liang and H. C. So, "Joint Waveform Design and Antenna Selection for MIMO Radar Beam Scanning," in IEEE Trans. Signal Process., vol. 72, pp. 4479-4492, 2024.（中科院SCI2区，Top期刊）',
            'W. Fan, J. Liang, G. Lu, X. Fan. H. C. So, “Spectrally-Agile Waveform Design for Wideband MIMO Radar Transmit Beampattern Synthesis via Majorization-ADMM,” in IEEE Trans. Signal Process., Jan. 19, 2021.（中科院SCI2区，Top期刊）',
            'W. Fan, J. Liang, H. C. So and G. Lu, "Min-Max Metric for Spectrally Compatible Waveform Design Via Log-Exponential Smoothing," in IEEE Trans. Signal Process., vol. 68, pp. 1075-1090, 2020.（中科院SCI2区，Top期刊）',
            'W. Fan, J. Liang, G. Yu, H. C. So and G. Lu, "MIMO Radar Waveform Design for Quasi-Equiripple Transmit Beampattern Synthesis via Weighted lp-Minimization," in IEEE Trans. Signal Process., vol. 67, no. 13, pp. 3397-3411, 2019.（中科院SCI2区，Top期刊）',
            'W. Fan, J. Liang and J. Li, "Constant Modulus MIMO Radar Waveform Design With Minimum Peak Sidelobe Transmit Beampattern," in IEEE Trans. Signal Process., vol. 66, no. 16, pp. 4207-4222, 2018.（中科院SCI2区，Top期刊）',
            'W. Fan, J. Liang, G. Yu, H. C. So, G. Lu, “Minimum local peak sidelobe level waveform design with correlation and/or spectral constraints,” in Signal Process., Vol. 171, 2020, 107450.（中科院SCI2区）',
            'W. Fan, J. Liang, X. Fan, H. C. So, “A Unified Sparse Array Design Framework for Beampattern Synthesis,” in Signal Process, Vol. 182, 2021, 107930.（中科院SCI2区）',
            'W. Fan, J. Liang, X. Fan, H. C. So, “Spectrally Compatible Aperiodic Sequence Set Design with Low Cross- and Auto-Correlation PSL,” in Signal Process., Vol. 183, 2021, 107960.（中科院SCI2区）',
            '范文, 蔚保国, 陈镜,等. 基于波形优化和天线位置选择的MIMO雷达波束扫描算法研究[J]. 雷达学报, 2022, 11(4):530–542.doi:10.12000/JR22135',
            '范文,李淳泽,等.复杂电磁环境下雷达抗干扰及多功能一体化波形设计方法研究[J].无线电通信技术,2023,49(5):960-970.',
            'J. Liang, X. Fan, W. Fan, D. Zhou and J. Li, "Phase-Only Pattern Synthesis for Linear Antenna Arrays," in IEEE Antenn. Wireless Propag. Lett., vol. 16, pp. 3232-3235, 2017.',
            'P Li, J Liang, M Zhang, W Fan, G Yu, "Joint image denoising with gradient direction and edge-preserving regularization," in Pattern Recognition 125, 108506.',
            'G Yu, J Liang, W Fan, HC So, D Zhou, "Autofocus algorithms with phase error correction for synthetic aperture radar imagery," in Digital Signal Processing 130, 103692.',
            '姚誉、李泽清、范文、杜晓林、吴乐南，"基于ABSUM的MIMO雷达频谱兼容波形设计," 雷达学报 11 (4), 543-556.',
            'J Yang, X Yu, M Sha, W Fan, G Cui, "Signal design for MIMO dual-function systems with permutation learning," in Science China Information Sciences, 2023•Springer.',
          ],
          conferencePapers: [
            'W. Fan, J. Liang, G. Yu, H. C. So and J. Li, "Robust Capon Beamforming via ADMM," ICASSP 2019 - 2019 IEEE International Conference on Acoustics, Speech and Signal Processing (ICASSP), Brighton, United Kingdom, 2019, pp. 4345-4349.（顶会）',
            'W. Fan, J. Liang and P. Li, "Coupled convolution method for image processing," 2017 IEEE International Conference on Unmanned Systems (ICUS), Beijing, 2017, pp. 512-516.',
            'W. Fan, J. Liang, X. Ye and M. Li, "Sequential error concealment via canonical correlation analysis," 2015 IEEE ICSPCC, Ningbo, 2015, pp. 1-4.',
          ]
        },
        {
          id: 7,
          name: '郭玉蓉',
          title: '讲师',
          introduction: '西安邮电大学讲师。2024年毕业于西安电子科技大学军事通信学专业，获工学博士学位。2024年6月-至今，在西安邮电大学通信与信息工程学院工作。',
          desc: '主要从事6G物联网、随机接入、低功耗广域网研究。',
          avatar: require('../../assets/images/GuoYR.png'),
          teaching: [
            '数字信号处理',
            '随机信号分析',
          ],
          researchAwards: [
          ],
          researchProjects: [
            '陕西省教育厅专项科研计划项目，面向低空经济异步mMTC场景的随机接入关键技术研究，2025.',
            '横向项目，数字经济及智慧城市管理相关研究，2025.4',
          ],
          publishedPapers: [
            'Yurong Guo, Zujun Liu, “Time-Delay-Estimation-Liked Detection Algorithm for LoRa Signal Over Multipath Channels,” IEEE Wireless Communications Letters, 2020, vol. 9, no.7, pp. 1093-1096. (中科院二区）',
            'Yurong Guo, Zujun Liu, “Low-Complexity Joint Activity Detection and Channel Estimation with Partially Orthogonal Pilot for Asynchronous Massive Access,” IEEE Internet of Things Journal, 2024, vol. 11, no.1, pp. 1773-1783. (中科院一区）',
            'Yurong Guo, Zujun Liu, "Chirp Index Modulated Pilots-Aided Joint Activity Detection and Channel Estimation for Asynchronous Massive Access," IEEE Transactions on Vehicular Technology, vol. 73, no. 12, pp. 19263-19274, Dec. 2024. (中科院二区) ',
            'Y. Xu and Y. Guo, "Low-Complexity Grant-Free Random Access Based on Chirp Signals in Asynchronous Scenarios," IEICE Communications Express, vol. 14, no. 9, pp. 349-352, Sep. 2025. (中科院四区)',
          ],
          conferencePapers: [
          ],
        }
        // 其他教师数据...
      ],
      teacher: null
    }
  },
  created() {
    const teacherId = parseInt(this.$route.params.id)
    this.teacher = this.teachers.find(t => t.id === teacherId)
    if (!this.teacher) {
      alert('未找到该教师信息')
      this.$router.back()
    }
  }
}
</script>

<style scoped>
.section-padding {
  padding: 0.5rem 0;
}

button, .team-card {
  transition: all 0.3s ease;
}

.text-indent-2em {
  text-indent: 2em; 
}

.overflow-hidden {
  overflow: hidden;
}

.h-\[50vh\] {
  height: 50vh;
}

.md\:h-\[70vh\] {
  height: 70vh;
}

.overflow-y-auto {
  overflow-y: auto;
}

.overflow-y-auto::-webkit-scrollbar {
  width: 6px;
}

.overflow-y-auto::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 10px;
}

.overflow-y-auto::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 10px;
}

.overflow-y-auto::-webkit-scrollbar-thumb:hover {
  background: #a1a1a1;
}

.object-scale-down {
  object-fit: scale-down;
}

.flex-shrink-0 {
  flex-shrink: 0;
}

.max-h-full {
  max-height: 100%;
}

.max-w-full {
  max-width: 100%;
}
</style>
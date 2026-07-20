<template>
    <div class="container mx-auto px-6 py-8">
        <!-- 面包屑导航 -->
        <div class="breadcrumbs mb-6 flex items-center text-sm">
            <router-link to="/" class="text-primary hover:underline">首页</router-link>
            <span class="mx-2">/</span>
            <router-link to="/notice-list" class="text-primary hover:underline">通知公告</router-link>
            <span class="mx-2">/</span>
            <span class="text-gray-800">通知详情</span>
        </div>

        <!-- 通知详情主体 -->
        <div class="max-w-4xl mx-auto bg-white rounded-lg shadow-sm p-6 border border-gray-200">
            <!-- 动态标题/时间/部门（匹配当前通知） -->
            <div class="border-b border-gray-200 pb-4 mb-6">
                <h1 class="text-xl font-bold text-gray-800 mb-2">
                    {{ currentNotice.title || '暂无通知标题' }}
                </h1>
                <div class="flex text-sm text-gray-500 gap-4 flex-wrap">
                    <span>发布时间：{{ currentNotice.date || '暂无发布时间' }}</span>
                    <span>发布部门：{{ currentNotice.publishDept || '暂无发布部门' }}</span>
                </div>
            </div>

            <!-- 正文：匹配当前通知ID -->
            <div class="prose max-w-none text-gray-700 leading-relaxed">
                <!-- 【ID=1：就业洽谈会企业名录（对应第一个通知）】 -->
                <div class="notice-content" v-if="currentNotice.id === '1'">
                    <!-- 正文直接显示企业名录（与标题对应） -->
                    <table class="enterprise-table w-full border-collapse mb-4">
                        <thead>
                            <tr class="bg-gray-100">
                                <th class="border border-gray-300 p-3 text-center">单位名称</th>
                                <th class="border border-gray-300 p-3 text-center">展位号</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td class="border border-gray-300 p-2">中国能源工程（西安）环保科技有限公司</td>
                                <td class="border border-gray-300 p-2 text-center">2—08</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">西安卫光科技有限公司</td>
                                <td class="border border-gray-300 p-2 text-center">2—09</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">青岛海信电器营销股份有限公司西安分公司</td>
                                <td class="border border-gray-300 p-2 text-center">2—10</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">西安云遮月网络科技有限公司</td>
                                <td class="border border-gray-300 p-2 text-center">3—07</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">深圳市中兴新云服务有限公司</td>
                                <td class="border border-gray-300 p-2 text-center">3—08</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">西安运控信息科技有限公司</td>
                                <td class="border border-gray-300 p-2 text-center">3—09</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">快上云（上海）网络科技有限公司</td>
                                <td class="border border-gray-300 p-2 text-center">1—01</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">西安俊睿电子科技有限公司</td>
                                <td class="border border-gray-300 p-2 text-center">1—02</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">西安交大捷普网络科技有限公司</td>
                                <td class="border border-gray-300 p-2 text-center">1—03</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">西安阳易信息技术有限公司</td>
                                <td class="border border-gray-300 p-2 text-center">1—04</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">鸿图前程（湖北）智能科技有限公司</td>
                                <td class="border border-gray-300 p-2 text-center">1—05</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">美钛技术服务（上海）有限公司西安分公司</td>
                                <td class="border border-gray-300 p-2 text-center">1—06</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">陕西启源电科技有限公司</td>
                                <td class="border border-gray-300 p-2 text-center">2—05</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">陕西森渝电气有限公司</td>
                                <td class="border border-gray-300 p-2 text-center">2—06</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">西安申首半导体科技有限公司</td>
                                <td class="border border-gray-300 p-2 text-center">2—07</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">安吉热威电热科技有限公司</td>
                                <td class="border border-gray-300 p-2 text-center">3—04</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">北京万悦信息科技有限公司</td>
                                <td class="border border-gray-300 p-2 text-center">3—05</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">长信智联（西安）通信科技有限公司</td>
                                <td class="border border-gray-300 p-2 text-center">3—06</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">陕西量维生物工程有限公司</td>
                                <td class="border border-gray-300 p-2 text-center">1—07</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">陕西卫峰核电子有限公司</td>
                                <td class="border border-gray-300 p-2 text-center">1—08</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">上海弼兴律师事务所</td>
                                <td class="border border-gray-300 p-2 text-center">1—09</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">嘉兴和新精冲科技有限公司</td>
                                <td class="border border-gray-300 p-2 text-center">1—10</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">上海蓝海人力资源科技股份有限公司</td>
                                <td class="border border-gray-300 p-2 text-center">1—11</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">西安魔度艺创商贸有限公司</td>
                                <td class="border border-gray-300 p-2 text-center">1—12</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">西安天健监测仪表有限公司</td>
                                <td class="border border-gray-300 p-2 text-center">1—13</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">云月千里（西安）科技有限公司</td>
                                <td class="border border-gray-300 p-2 text-center">1—14</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">陕西苏宁易购销售有限公司</td>
                                <td class="border border-gray-300 p-2 text-center">1—15</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">西安文網网络信息科技有限责任公司</td>
                                <td class="border border-gray-300 p-2 text-center">1—16</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">西安觉醒元宇宙信息科技有限公司</td>
                                <td class="border border-gray-300 p-2 text-center">1—17</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">华政教育科技（西宁）有限公司西安分公司</td>
                                <td class="border border-gray-300 p-2 text-center">1—18</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">宝鸡圭彬光电设备有限公司</td>
                                <td class="border border-gray-300 p-2 text-center">2—01</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">陕西鸿信会计师事务所有限责任公司</td>
                                <td class="border border-gray-300 p-2 text-center">2—02</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">西安桃李食品有限公司</td>
                                <td class="border border-gray-300 p-2 text-center">2—03</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">陕西氮学教育科技有限公司</td>
                                <td class="border border-gray-300 p-2 text-center">2—04</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">美林数据技术股份有限公司</td>
                                <td class="border border-gray-300 p-2 text-center">2—11</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">陕西四菱电子科技股份有限公司</td>
                                <td class="border border-gray-300 p-2 text-center">2—12</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">福州译国译民集团有限公司</td>
                                <td class="border border-gray-300 p-2 text-center">2—13</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">陕西威思曼高压电源股份有限公司</td>
                                <td class="border border-gray-300 p-2 text-center">2—14</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">杭州世播科技有限公司</td>
                                <td class="border border-gray-300 p-2 text-center">2—15</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">西安星辉电力科技有限公司</td>
                                <td class="border border-gray-300 p-2 text-center">2—16</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">陕西吉新人力资源有限公司</td>
                                <td class="border border-gray-300 p-2 text-center">2—17</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">西安高途教育科技有限公司</td>
                                <td class="border border-gray-300 p-2 text-center">3—01</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">陕西学恒教育科技有限公司</td>
                                <td class="border border-gray-300 p-2 text-center">3—02</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">西安赛远网络科技有限公司</td>
                                <td class="border border-gray-300 p-2 text-center">3—03</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">博奥赛斯（重庆）生物科技有限公司</td>
                                <td class="border border-gray-300 p-2 text-center">3—10</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">西安华讯科技有限责任公司</td>
                                <td class="border border-gray-300 p-2 text-center">3—11</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">上海长宁区乐读线上教育培训学校</td>
                                <td class="border border-gray-300 p-2 text-center">3—12</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">雨润控股集团有限公司</td>
                                <td class="border border-gray-300 p-2 text-center">3—13</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">深圳学而思教育科技有限公司西安分公司</td>
                                <td class="border border-gray-300 p-2 text-center">3—14</td>
                            </tr>
                            <tr>
                                <td class="border border-gray-300 p-2">陕西启智教育科技有限公司</td>
                                <td class="border border-gray-300 p-2 text-center">3—15</td>
                            </tr>
                        </tbody>
                    </table>
                    详情请见原文链接：<a href="http://jiuye.xupt.edu.cn/detail/news?id=1691960&menu_id=&type_id=18080"
                        target="_blank"
                        class="text-primary hover:underline">http://jiuye.xupt.edu.cn/detail/news?id=1691960&menu_id=&type_id=18080</a>
                </div>

                <div class="notice-content" v-if="currentNotice.id === '2'">
                    各研究生培养单位、相关研究生：<br><br>
                    &nbsp;&nbsp;为做好我校2026年“硕博连读”研究生推荐选拔工作，根据《西安邮电大学招收攻读博士学位研究生管理办法》等相关规定，现将有关事项通知如下：<br><br>
                    <span style="font-weight: 700; font-size: 20px; display: block;">一、选拔条件</span>
                    &nbsp;&nbsp;1. 拥护中国共产党的领导，具有正确的政治方向，热爱祖国，愿意为社会主义现代化建设服务，遵纪守法，品德良好，学风端正，无任何学术不端行为及违法违纪记录。<br>
                    &nbsp;&nbsp;2. 身体和心理健康状况符合国家规定的体检要求。<br>
                    &nbsp;&nbsp;3. 有至少两名所报考学科专业领域内的教授（或相当专业技术职称的专家）的书面推荐意见。<br>
                    &nbsp;&nbsp;4. 已获得学士学位，已完成规定课程学习，无不及格成绩且硕士学位课加权平均成绩优良的一、二年级全日制非定向在学硕士研究生。<br>
                    &nbsp;&nbsp;5. 有至少两名所报考学科专业领域内的教授（或相当专业技术职称的专家）的书面推荐意见。<br>
                    &nbsp;&nbsp;6. 符合招生学院提出的其他要求。<br><br>
                    <span style="font-weight: 700; font-size: 20px; display: block;">二、选拔程序及时间安排</span>
                    &nbsp;&nbsp;1.2025年11月25日—2025年12月25日，符合申请条件的我校硕士研究生根据《西安邮电大学2026年博士研究生招生目录》以及各学院硕博连读研究生选拔实施细则，填写《西安邮电大学硕博连读研究生申请表》，并登录中国研究生招生信息网（http://yz.chsi.com.cn）进行报名。报名须按照要求如实填写报考信息，报考方式选择“硕博连读”，同时缴纳报名费用。同时按要求提供硕士成绩单、外语水平证明、科研成果等相关支撑材料，经导师、学科推荐，报相关招生学院进行资格审核（具体要求等参见各学院通知）。<br>
                    &nbsp;&nbsp;2. 研究生院对考生报考材料进行审核确认，并将通过名单反馈至各招生学院。<br>
                    &nbsp;&nbsp;3. 招生学院根据学校及本学院硕博连读研究生选拔实施细则，按照“德智体美劳全面衡量，择优录取，保证质量，宁缺勿滥”的原则，对申请者进行全面考核。<br>
                    &nbsp;&nbsp;4. 各学院将考核及公示后的“硕博连读”研究生名单以及其他相关申请材料报研究生院审查备案。<br><br>
                    <span style="font-weight: 700; font-size: 20px; display: block;">三、材料提交要求</span>
                    &nbsp;&nbsp;网上报名后、信息确认时，考生应在规定时间内向研究生院提交以下报考材料：<br>
                    &nbsp;&nbsp;1．西安邮电大学硕博连读申请表（贴照片）。<br>
                    &nbsp;&nbsp;2．网上报名成功后打印的报名信息简表。<br>
                    &nbsp;&nbsp;3．第二代有效居民身份证复印件。<br>
                    &nbsp;&nbsp;4．大学（本科或高职高专）毕业证、学士学位证（复印件）；学历学位认证报告（取得国境外学历学位的考生，需提供教育部留学服务中心出具的学历学位认证报告）。<br>
                    &nbsp;&nbsp;5．专家推荐书（两名所报考学科专业领域内的教授（或相当专业技术职称的专家）的书面推荐意见）。<br>
                    &nbsp;&nbsp;6．硕士阶段成绩单（须加盖学院或培养科管理部门公章）。<br>
                    &nbsp;&nbsp;7．外语水平证书、外语成绩单、公开发表的英文学术论文或英语语种国家（地区）学习经历证明等。<br>
                    &nbsp;&nbsp;8．综合能力证明材料，如近期发表的与报考领域相关的学术论文、研究项目、发明专利或专著、咨询报告、获奖证书等。<br>
                    &nbsp;&nbsp;9．需提交的其他资料。<br><br>
                    详情请见原文链接：<a href="https://gr.xupt.edu.cn/info/1176/9201.htm" target="_blank"
                        class="text-primary hover:underline">https://gr.xupt.edu.cn/info/1176/9201.htm</a>
                </div>

                <p v-if="currentNotice.id === '3'">
                    <span style="font-weight: 700; font-size: 20px; display: block;">一、培养目标</span>
                    &nbsp;&nbsp;培养德智体美劳全面发展，在本门学科上掌握坚实宽广的基础理论和系统深入的专门知识，具有独立从事科学研究工作的能力，在科学或专门技术上做出创造性成果，为社会主义建设服务的高级专门人才。<br><br>
                    <span style="font-weight: 700; font-size: 20px; display: block;">二、招生计划</span>
                    &nbsp;&nbsp;2026年博士研究生具体招生人数以教育部最终下达计划为准。<br><br>
                    <span style="font-weight: 700; font-size: 20px; display: block;">三、学制及学习方式</span>
                    &nbsp;&nbsp;博士生的学制为4年，最长学习年限为7年。我校招收的攻读博士学位研究生学习方式均为全日制。<br><br>
                    <span style="font-weight: 700; font-size: 20px; display: block;">四、招生方式及报名时间</span>
                    &nbsp;&nbsp;我校2026年博士生招生采取普通招考、“申请-考核”和硕博连读三种方式。<br>
                    &nbsp;&nbsp;硕博连读报名时间计划在2025年11月至12月，“申请-考核”和普通招考报名时间计划2026年3月至4月，具体安排以报名通知为准，请各位考生随时关注我校研究生院官网相关信息。<br><br>
                    <span style="font-weight: 700; font-size: 20px; display: block;">五、报考条件</span>
                    （一）普通招考方式报考的基本条件：<br>
                    &nbsp;&nbsp;1．拥护中国共产党的领导，具有正确的政治方向，热爱祖国，愿意为社会主义现代化建设服务，遵纪守法，品行端正，无学术或诚信不良记录。<br>
                    &nbsp;&nbsp;2．考生必须符合下列条件之一：<br>
                    &nbsp;&nbsp;&nbsp;&nbsp;（1）硕士研究生毕业或已获硕士学位的人员；<br>
                    &nbsp;&nbsp;&nbsp;&nbsp;（2）应届硕士毕业生（最迟须在入学前毕业或取得硕士学位）。<br>
                    &nbsp;&nbsp;3．身体和心理健康状况符合规定的体检要求。<br>
                    &nbsp;&nbsp;4．两名所报考学科专业领域内的教授（或相当专业技术职称的专家）的书面推荐意见。<br>
                    &nbsp;&nbsp;5．现役军人报考博士研究生，按中国人民解放军相关部门规定办理。<br>
                    &nbsp;&nbsp;6．满足所报考学院的其他学术条件要求。<br>
                    （二）“申请-考核”制申请条件：<br>
                    &nbsp;&nbsp;除满足上述“普通招考方式报考的基本条件”中第1、2、3、4条规定外，近5年内科研成果须满足以下条件之一：<br>
                    &nbsp;&nbsp;1．以第一作者或通讯作者在EI、SCI、CSCD、“中国科技期刊卓越行动计划”领军期刊、重点期刊上公开发表并收录与本学科相关论文1篇；或在IEEE系列会议上发表论文1篇。<br>
                    &nbsp;&nbsp;2．参加中国国际大学生创新大赛国家级竞赛获奖排名前5，或省级竞赛金奖排名前3、银奖排名前2、铜奖排名第1；或获得“中国研究生创新实践系列大赛”赛事国家级一档排名前3，二档排名前2；<br>
                    &nbsp;&nbsp;3．作为重要成员参与国家级科研项目（国家自然科学基金、国防科技计划等）学生排名前2；或作为重要成员参与省级科技重大、重点支持类计划项目学生排名前1；或作为主持人，完成校级研究生创新基金重大、重点项目。<br>
                    &nbsp;&nbsp;4．获批发明专利1项，个人排名前2；或参与出版本学科专著、教材、译著或编著1部，且本人承担5万字以上；或获得省部级及以上科学技术奖、中国通信学会、中国电子学会科学技术奖二等奖及以上，学生排名第1。<br>
                    &nbsp;&nbsp;5．所报考学院的其他学术条件要求。<br>
                    （三）硕博连读方式报考的条件：<br>
                    &nbsp;&nbsp;除满足上述“普通招考方式报考的基本条件”中第1、3、4条规定外，应已完成规定的课程学习和考核，成绩优秀，对学术研究有浓厚兴趣，具有较强创新精神和科研能力的本校全日制在学硕士生。具体要求及安排另行通知。<br><br>
                    <span style="font-weight: 700; font-size: 20px; display: block;">六、选拔过程</span>
                    （一）报名申请<br>
                    &nbsp;&nbsp;所有报名人员须在规定时间内，在教育部研招网进行网上报名。同时按要求在报名时间内将以下材料电子版及纸质版提交至学校研招办，进行报考信息确认，并按要求进行报名缴费，逾期将不再受理。所需材料清单：<br>
                    &nbsp;&nbsp;1．西安邮电大学攻读博士学位研究生报名申请表（附件1）；<br>
                    &nbsp;&nbsp;2．两名所报考学科领域内教授（或相当专业技术职称的专家）的书面推荐书（附件2）；<br>
                    &nbsp;&nbsp;3．硕士学位课程学习成绩单原件及复印件各一份（培养单位校级主管部门盖章或成绩档案管理部门盖章有效）；<br>
                    &nbsp;&nbsp;4．学历学位证明材料原件及复印件：<br>
                    &nbsp;&nbsp;往届生提供身份证，硕士学位证书及学位认证报告，学历证书及学历电子注册备案表（应届毕业生须在入学前补交）；<br>
                    &nbsp;&nbsp;持国外学位证书报考者，需提交“教育部留学生服务中心”出具的学历、学位证明；<br>
                    &nbsp;&nbsp;应届硕士毕业生和本校全日制在学硕士生提供学生证、身份证及学籍在线验证报告。<br>
                    &nbsp;&nbsp;5．外语水平成绩证明复印件；<br>
                    &nbsp;&nbsp;6．已有科研成果、获奖证书等相关材料（包括发表学术论文、申请专利、撰写专著、获得科技奖励、竞赛奖励、参与科研项目等情况）。<br>
                    &nbsp;&nbsp;以上各项内容为资格初审必备材料，还需提供报考学院要求的其他材料。所有材料原件备查。<br>
                    &nbsp;&nbsp;所有考生均应当对本人网上报名信息进行认真核对并确认，报名信息经考生确认后一律不作修改，考生应按要求准确填写个人网上报名信息并提供真实材料。凡因网报信息填写错误或填报虚假信息而造成不能考试、复试或录取的，后果由考生本人承担。<br>
                    （二）资格审查<br>
                    &nbsp;&nbsp;各招生学院派专人对考生按照报考条件进行资格审查，核对考生提交的资料是否真实、完备、有效。<br>
                    （三）材料评议<br>
                    &nbsp;&nbsp;各招生学院组织专家小组对考生提交的各类成果材料进行审核评议，通过考生的硕士课程成绩、硕士学位论文、考生参与科研、发表论文、出版专著、获奖等情况及专家推荐意见、考生自我评价等材料对其做出评价结论，得出材料评议结果。该结论应作为录取环节的重要参考依据之一。<br>
                    （四）初试<br>
                    &nbsp;&nbsp;普通招考方式报考考生须参加初试，初试科目包括英语及两门专业课程，考试方式均为笔试。笔试成绩加权记入入学考试总成绩。<br>
                    （五）考核<br>
                    &nbsp;&nbsp;各招生学院根据本学院《博士研究生招生录取工作实施细则》及考核工作流程，对报名人员进行遴选考核。考核须包含外国语水平、专业基础知识、科研创新能力、心理素质综合能力测试等方面内容，同时强化对学生思想政治素质、学术道德、专业伦理、诚实守信等方面的考核，不合格者不予录取。<br><br>
                    <span style="font-weight: 700; font-size: 20px; display: block;">七、录取</span>
                    &nbsp;&nbsp;学校将按照“按需招生、德智体美劳全面衡量、择优录取和宁缺毋滥”的原则，根据考生考核综合成绩、思想政治素质和品德表现，身心健康状况等全面衡量，导师、考生双向选择，择优录取。考生体检工作在考生拟录取后组织进行，具体要求届时详见我校公告。<br>
                    &nbsp;&nbsp;录取类别为非定向的博士研究生，入学前须将人事档案等关系转入我校，毕业后自主择业。录取类别为定向的博士研究生，拟录取后须签订定向培养协议书，考生户口和人事档案等关系不转入我校，毕业后回原单位工作。<br><br>
                    <span style="font-weight: 700; font-size: 20px; display: block;">八、学费及奖助体系</span>
                    （一）学费<br>
                    &nbsp;&nbsp;按照陕西省物价局有关规定执行，博士研究生学费为每生10000元/年，分学年收取。<br>
                    （二）奖助学金（具体发放标准以教育部和学校相关文件为准）<br>
                    &nbsp;&nbsp;符合相关要求的全日制非定向博士研究生可享受我校奖助学金：<br>
                    &nbsp;&nbsp;1．国家奖学金：符合条件者均可申请，名额以国家当年下达的指标为准，每生30000元。<br>
                    &nbsp;&nbsp;2．助学金：学制内每生每年25000元（国家助学金13000元+学校助学金12000元）。<br>
                    &nbsp;&nbsp;3．学业奖学金：具体参见我校相关文件。<br>
                    &nbsp;&nbsp;4．科研补助：经费来源于导师科研经费，每生每年不低于12000元。<br>
                    &nbsp;&nbsp;5．其他资助：具体参见我校相关文件。<br>
                    &nbsp;&nbsp;6．“三助”岗位助学金：学校为博士研究生提供三助（助教、助研、助管）岗位。<br><br>
                    详情请见原文链接：<a href="https://gr.xupt.edu.cn/info/1176/9200.htm" target="_blank"
                        class="text-primary hover:underline">https://gr.xupt.edu.cn/info/1176/9200.htm</a>
                </p>

                <p v-if="currentNotice.id === '4'">
                    &nbsp;&nbsp;宣讲单位：中国核工业第五建设有限公司<br><br>
                    &nbsp;&nbsp;宣讲时间：2025年11月26日 14:30<br><br>
                    &nbsp;&nbsp;所在学校：西安邮电大学<br><br>
                    &nbsp;&nbsp;宣讲地点：第一宣讲室<br><br>
                    单位简介<br>
                    &nbsp;&nbsp;中国核工业第五建设有限公司组建于1964年，隶属于中国核工业建设股份有限公司，是一家以国防工程，核工程、核电工程和工业与民用建筑安装工程业务为主的具有建筑、安装总承包资质的大型综合性建筑安装企业。
                    公司具有电力（核电）工程、化工石油工程、机电安装工程、房屋建筑工程等四个施工总承包一级资质；核工程、钢结构工程、起重设备安装工程、化工石油设备管道安装工程等四个专业承包一级资质；取得了中华人民共和国民用核安全设备制造、安装许可证，压力管道安装、压力容器制造、锅炉安装和起重机械安装维修资质许可证；取得了中国合格评定国家认可委员会实验室认可证书及国家核安全局认定
                    公司曾先后荣获中国建筑工程鲁班奖、国家优质工程金奖、银奖、省部级优质工程奖、上海市白玉兰杯、申安杯优质工程奖等多个奖项，拥有发明/实用新型专利、工法43项。公司先后24次被评为上海市优秀公司，是上海市建筑企业综合实力前50强企业。<br><br>
                    详情见原文链接：<a href="http://jiuye.xupt.edu.cn/detail/career?id=676530" target="_blank"
                        class="text-primary hover:underline">http://jiuye.xupt.edu.cn/detail/career?id=676530</a>
                </p>

                <p v-if="currentNotice.id === '5'">
                    &nbsp;&nbsp;宣讲单位：汇丰软件开发（广东）有限公司<br><br>
                    &nbsp;&nbsp;宣讲时间：2025年11月27日 15:00<br><br>
                    &nbsp;&nbsp;所在学校：西安邮电大学<br><br>
                    &nbsp;&nbsp;宣讲地点：长安校区东区逸夫楼二楼FZ215<br><br>
                    单位简介<br>
                    &nbsp;&nbsp;汇丰科技中国自2000年成立以来，已经发展成为汇丰集团全球科技中心的杰出团队。我们拥有超过上万名人员，遍布广州、西安、上海和深圳，这些团队是我们在开发世界一流银行解决方案和产品方面的坚强后盾。我们致力于支持汇丰银行的数字化转型，并助力其在大湾区、亚洲乃至全球实现宏伟目标。<br><br>
                    详情见原文链接：<a href="http://jiuye.xupt.edu.cn/detail/career?id=678113" target="_blank"
                        class="text-primary hover:underline">http://jiuye.xupt.edu.cn/detail/career?id=678113</a>
                </p>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'GuestNoticeDetail',
    data() {
        return {
            currentNotice: {},
            // 【核心调整：把“就业洽谈会”设为第一个通知（id=1）】
            allNotices: [
                {
                    id: '1',
                    title: '“宏志助航计划”西安邮电大学2026届毕业生秋季就业洽谈会（综合类第五场）参会企业名录',
                    date: '2025-11-25',
                    publishDept: '西安邮电大学就业创业信息网'
                },
                {
                    id: '2',
                    title: '关于做好我校2026年“硕博连读”研究生推荐选拔工作的通知',
                    date: '2025-11-24',
                    publishDept: '西安邮电大学研究生院'
                },
                {
                    id: '3',
                    title: '西安邮电大学2026年博士研究生招生简章',
                    date: '2025-11-24',
                    publishDept: '西安邮电大学研究生院'
                },
                {
                    id: '4',
                    title: '汇丰软件开发（广东）有限公司校内宣讲会',
                    date: '2025-11-20',
                    publishDept: '西安邮电大学就业创业信息网'
                },
                {
                    id: '5',
                    title: '中国核工业第五建设有限公司校内宣讲会',
                    date: '2025-11-19',
                    publishDept: '西安邮电大学就业创业信息网'
                }
            ]
        };
    },
    created() {
        // 匹配路由参数对应的通知
        const noticeId = this.$route.query.id;
        this.currentNotice = this.allNotices.find(item => item.id === noticeId) || {};
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

.prose a {
    color: #1a2456;
    text-decoration: underline;
    transition: color 0.3s ease;
}

.prose a:hover {
    color: #3b82f6;
}

.enterprise-table {
    font-size: 0.9rem;
}

.enterprise-table th {
    font-weight: 600;
}

.enterprise-table tr:nth-child(even) {
    background-color: #f9fafb;
}

.enterprise-table td {
    word-break: break-word;
}
</style>
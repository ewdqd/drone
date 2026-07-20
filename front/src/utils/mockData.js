// 产品数据（图片路径添加提示，避免找不到文件）
export const productData = [
  {
    id: 1,
    name: 'Prometheus600科研无人机开发平台（P600）',
    subtitle: 'P600系列第四代产品',
    desc: '深度融合两大开源项目Prometheus和SpireCV...',
    params: [
      '核心技术：Prometheus+SpireCV深度融合',
      '光学配置：40倍变焦光电吊舱'
    ],
    // 提示：需在src/assets/images/下添加p600.jpg，或替换为现有图片路径
    img: require('@/assets/images/P600.jpg')
  }
]

// 新闻数据（同上，提示图片路径）
export const newsData = [
  {
    id: 1,
    title: '通信与信息工程学院举办职业生涯规划讲座',
    date: '2025年10月31日',
    content: '为引导低年级学生科学规划职业生涯...',
    // 提示：需添加lecture.jpg
    img: require('@/assets/images/lecture.jpg')
  }
]

// 团队数据
export const teamData = [
  {
    id: 1,
    name: '陈丽',
    position: '室内设计主管',
    // 提示：需添加chenli.jpg
    img: require('@/assets/images/chenli.jpg')
  }
]

// 学生数据（修复student3.jpg路径提示）
export const studentData = [
  { id: 1, name: '李四', img: require('@/assets/images/student1.jpg') }, // 需添加student1.jpg
  { id: 2, name: '王五', img: require('@/assets/images/student2.jpg') }, // 需添加student2.jpg
  { id: 3, name: '赵六', img: require('@/assets/images/student3.jpg') }  // 需添加student3.jpg
]

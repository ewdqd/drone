import Vue from 'vue'
import VueRouter from 'vue-router'

// 游客端页面组件（请确保路径与实际文件一致）
import GuestHome from '@/views/Guest/GuestHome.vue'
import GuestInstituteIntro from '@/views/Guest/GuestInstituteIntro.vue'
import GuestNewsList from '@/views/Guest/GuestNewsList.vue'
import GuestProductList from '@/views/Guest/GuestProductList.vue'
import GuestProductDetail from '@/views/Guest/GuestProductDetail.vue'
import GuestTeam from '@/views/Guest/GuestTeam.vue'
import GuestCooperation from '@/views/Guest/GuestCooperation.vue'
import GuestNewsDetail from '@/views/Guest/GuestNewsDetail.vue'
import GuestTeamDetail from '@/views/Guest/GuestTeamDetail.vue'
import GuestNoticeDetail from '@/views/Guest/GuestNoticeDetail.vue'
import GuestNoticeList from '@/views/Guest/GuestNoticeList.vue' 
import GuestAcademicList from '@/views/Guest/GuestAcademicList.vue'
import GuestAcademicDetail from '@/views/Guest/GuestAcademicDetail.vue'

// 管理端页面组件（请确保路径与实际文件一致）
import AdminLogin from '@/views/Admin/AdminLogin'
import AdminHome from '@/views/Admin/AdminHome'
import AdminNewsManage from '@/views/Admin/AdminNewsManage'
import AdminNewsDetail from '@/views/Admin/AdminNewsDetail.vue'
import AdminUserManage from '@/views/Admin/AdminUserManage'

// 注册VueRouter插件
Vue.use(VueRouter)

// 定义路由规则
const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [
    // 游客端路由
    {
      path: '/',
      name: 'GuestHome',
      component: GuestHome,
      meta: { title: '低空研究院平台-首页' }
    },
    {
      path: '/notice-list', 
      name: 'GuestNoticeList',
      component: GuestNoticeList
    },
    {
      path: '/notice-detail',
      name: 'NoticeDetail',
      component: GuestNoticeDetail
    },
    {
      path: '/academic-list',
      name: 'AcademicList',
      component: GuestAcademicList
    },
    {
      path: '/academic-detail',
      name: 'AcademicDetail',
      component: GuestAcademicDetail
    },
    {
      path: '/institute-intro',
      name: 'GuestInstituteIntro',
      component: GuestInstituteIntro,
      meta: { title: '研究院概况' }
    },
    {
      path: '/product-list',
      name: 'GuestProductList',
      component: GuestProductList,
      meta: { title: '产品展示' }
    },
    {
      path: '/product-detail/:id',
      name: 'GuestProductDetail',
      component: GuestProductDetail,
      meta: { title: '产品详情' },
      props: true
    },
    {
      path: '/team',
      name: 'GuestTeam',
      component: GuestTeam,
      meta: { title: '团队风采' }
    },
    {
      path: '/news-list',
      name: 'GuestNewsList',
      component: GuestNewsList,
      meta: { title: '科研新闻' }
    },
    {
      path: '/news-detail',
      name: 'GuestNewsDetail',
      component: GuestNewsDetail,
      meta: { title: '新闻详情' },
      props: true
    },
    {
      path: '/cooperation',
      name: 'GuestCooperation',
      component: GuestCooperation,
      meta: { title: '合作交流' }
    },
    {
      path: '/team-detail/:id',
      name: 'GuestTeamDetail',
      component: GuestTeamDetail,
      meta: { title: '教师详情' },
      props: true
    },

    // 管理端路由
    {
      path: '/admin/login',
      name: 'AdminLogin',
      component: AdminLogin,
      meta: { title: '管理端登录', isPublic: true }
    },
    {
      path: '/admin/home',
      name: 'AdminHome',
      component: AdminHome,
      meta: { title: '管理端首页', requireAuth: true },
      children: [
        {
          path: 'news-manage',
          name: 'AdminNewsManage',
          component: AdminNewsManage,
          meta: { title: '新闻管理' }
        },
        {
          path: 'admin-news-detail',
          name: 'AdminNewsManage',
          component: AdminNewsDetail,
          meta: { title: '新闻管理' }
        },
        {
          path: 'user-manage',
          name: 'AdminUserManage',
          component: AdminUserManage,
          meta: { title: '用户管理' }
        },

      ]
    },

    // 404重定向（可选）
    { path: '*', redirect: '/' }
  ]
})

// 路由守卫：处理页面标题和权限控制
router.beforeEach((to, from, next) => {
  document.title = to.meta.title || '低空研究院平台'
  // 公开页面（如登录页）直接放行
  if (to.meta.isPublic) {
    next()
  } // 需要权限的页面，校验 token
  else if (to.meta.requireAuth && !localStorage.getItem('x-auth-token')) {
    next({ path: '/admin/login' })
  }
  else {
    next()
  }
})

export default router

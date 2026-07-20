// 1. 引入 Vue 2 核心（注意：Vue 2 是整体引入，不是解构 import { createApp }）
import Vue from 'vue'
// 2. 引入根组件 App
import App from './App.vue'
// 3. 引入 Vue Router 3（之前已安装的 Router 3 版本，适配 Vue 2）
import router from './router'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import './assets/font-awesome/css/all.min.css'
// main.js
import 'leaflet/dist/leaflet.css';

// 4. 关闭 Vue 生产环境提示（可选，优化控制台输出）
Vue.config.productionTip = false
Vue.use(ElementUI) // 全局注册 Element UI 组件
// 5. Vue 2 初始化方式：new Vue() 挂载根组件

import store from './store/index'
new Vue({
  router,
  render: h => h(App),
  store
}).$mount('#app')
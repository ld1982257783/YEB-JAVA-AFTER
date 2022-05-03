import Vue from 'vue'
import App from './App.vue'
//引入element-ui
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';

//导入 font-awesome 
import 'font-awesome/css/font-awesome.css'

//引入路由
//引入vue-router
import VueRouter from 'vue-router'
//引入自己创建的路由器 
import router from './router/index'

//引入vuex 
import store from './store/index'




import { postRequest } from "./utils/api";
import { getRequest } from "./utils/api";
import { deleteRequest } from "./utils/api";
import { putRequest } from "./utils/api";
import { initMenu } from './utils/menus';
import { downloadRequest } from './utils/download';


//插件形式使用请求 
Vue.prototype.postRequest = postRequest;
Vue.prototype.getRequest = getRequest;
Vue.prototype.deletetRequest = deleteRequest;
Vue.prototype.putRequest = putRequest;
Vue.prototype.downloadRequest = downloadRequest










//定义全局路由守卫 路由器前守卫   获取 用户菜单列表 
router.beforeEach((to, from, next) => {
  //代表已登录
  if (window.sessionStorage.getItem('tokenStr')) {
    // store.dispatch('initCurrentAdmin');
    initMenu(router, store);
    //判断是否能获取到用户信息 
    if (!window.sessionStorage.getItem('user')) {
      //给后端发送请求获取 user信息 
      return getRequest('/admin/info').then(resp => {
        if (resp) {
          window.sessionStorage.setItem('user', JSON.stringify(resp))
          store.commit('INIT_ADMIN',resp);
          next();
        }
      })
    }
    next();
  } else {
    //是否跳转登录页面
    if (to.path == '/') {
      next();
    } else {
      next('/?redirect=' + to.path);
    }
  }
})










//使用element-ui 
Vue.use(ElementUI, { size: 'small' });

//使用vue-router插件
Vue.use(VueRouter)





Vue.config.productionTip = false


new Vue({
  render: h => h(App),
  router,
  store
}).$mount('#app')

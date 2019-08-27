// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
//import ElementUI from 'element-ui'
//import 'element-ui/lib/theme-chalk/index.css'
import Vant, { Toast } from 'vant'
import {Dialog} from 'vant'
import 'vant/lib/index.css';
import store from './store'
import {getRequest} from './utils/api'
import {postRequest} from './utils/api'
import {jsonPostRequest} from './utils/api'
import {deleteRequest} from './utils/api'
import {putRequest} from './utils/api'
import './utils/filter_utils'
import 'font-awesome/css/font-awesome.min.css'
//import { AddressList } from 'vant';
import {Lazyload} from 'vant'

Vue.config.productionTip = false
//Vue.use(ElementUI)
Vue.use(Vant)

Vue.use(Toast);
Vue.use(Dialog);
Vue.use(Lazyload);
//Vue.use(AddressList);
import {
  GoodsAction,
  GoodsActionIcon,
  GoodsActionButton
} from 'vant';

Vue
  .use(GoodsAction)
  .use(GoodsActionIcon)
  .use(GoodsActionButton);

Vue.prototype.getRequest = getRequest;
Vue.prototype.postRequest = postRequest;
Vue.prototype.jsonPostRequest = jsonPostRequest;
Vue.prototype.deleteRequest = deleteRequest;
Vue.prototype.putRequest = putRequest;

import VueClipboard from 'vue-clipboard2'
VueClipboard.config.autoSetContainer = true
Vue.use(VueClipboard)

router.beforeEach((to, from, next)=> {
    console.log(to);
    if (from.path == '/goodslist'){
      if (to.path == '/goodsinfo'){
        from.meta.keepAlive=true;
      }else{
        from.meta.keepAlive=false;
      }
    }
    if (to.name == 'Login') {
      next();
      return;
    }
    if (to.name == '主页'){
      next("/gallery");
      return;
    }
    if (to.name == null){
      Toast('此功能还未完成');
      next(false);
      return;
    }
    var name = store.state.user.username;
    if (name == '未登录') {
      if (to.meta.requireAuth || to.name == null) {
        next({path: '/login', query: {redirect: to.path}})
      } else {
        next();
      }
    } else {
      next();
    }
  }
)

new Vue({
  el: '#app',
  router,
  store,
  template: '<App/>',
  components: {App}
})

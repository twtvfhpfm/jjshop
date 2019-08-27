import Vue from 'vue'
import Vuex from 'vuex'
import '../lib/sockjs'
import '../lib/stomp'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    user: {
      username: window.localStorage.getItem('user' || '[]') == null ? '未登录' : JSON.parse(window.localStorage.getItem('user' || '[]')).username,
      role: window.localStorage.getItem('user' || '[]') == null ? '' : JSON.parse(window.localStorage.getItem('user' || '[]')).role,
      id: window.localStorage.getItem('user' || '[]') == null ? -1 : JSON.parse(window.localStorage.getItem('user' || '[]')).id
    },
    routes: [],
    msgList: [],
    isDotMap: new Map(),
    currentFriend: {},
    stomp: null,
    nfDot: false,
    address: window.localStorage.getItem('address') == null ? {} : JSON.parse(window.localStorage.getItem("address")),
    submitOrder:{
      totalPrice: window.localStorage.getItem('submitOrder') == null ? 0 : JSON.parse(window.localStorage.getItem("submitOrder")).totalPrice,
      goods: window.localStorage.getItem('submitOrder') == null ? [] : JSON.parse(window.localStorage.getItem("submitOrder")).goods
    },
    orderId: window.localStorage.getItem('orderId') == null ? "" : window.localStorage.getItem("orderId"),
    manager: {
      categoryId: window.localStorage.getItem('manager') == null ? 0 : JSON.parse(window.localStorage.getItem('manager')).categoryId,
      goodsId: window.localStorage.getItem('manager') == null ? 0: JSON.parse(window.localStorage.getItem('manager')).goodsId
    },
    goodsList: window.localStorage.getItem('goodsList') == null ? {text: null, categoryId: 0} : JSON.parse(window.localStorage.getItem('goodsList')),
    goods: window.localStorage.getItem('goods') == null ? {} : JSON.parse(window.localStorage.getItem('goods')),
    goodsOrder:{
      orderId: window.localStorage.getItem("goodsOrder") == null ? "" : JSON.parse(window.localStorage.getItem("goodsOrder")).orderId
    },
    pay: window.localStorage.getItem("pay") == null ? {} : JSON.parse(window.localStorage.getItem("pay"))
  },
  mutations: {
    initMenu(state, menus){
      state.routes = menus;
    },
    setManagerCategoryId(state, id){
      state.manager.categoryId = id;
      window.localStorage.setItem('manager', JSON.stringify(state.manager));
    },
    setManagerGoodsId(state, id){
      state.manager.goodsId = id;
      window.localStorage.setItem('manager', JSON.stringify(state.manager));
    },
    setGoodsList(state, item){
      state.goodsList = item;
      window.localStorage.setItem('goodsList', JSON.stringify(state.goodsList));
    },
    setGoodsOrderId(state, orderId){
      state.goodsOrder.orderId = orderId;
      window.localStorage.setItem('goodsOrder', JSON.stringify(state.goodsOrder));
    },
    setGoods(state, item){
      state.goods = item;
      window.localStorage.setItem('goods', JSON.stringify(state.goods));
    },
    setOrderId(state, orderId){
      state.orderId = orderId;
      window.localStorage.setItem('orderId', orderId);
    },
    setSubmitOrder(state, submitOrder){
      state.submitOrder = submitOrder;
      window.localStorage.setItem('submitOrder', JSON.stringify(submitOrder));
    },
    setPay(state, pay){
      state.pay = pay;
      window.localStorage.setItem("pay", JSON.stringify(pay));
    },
    login(state, user){
      state.user = user;
      window.localStorage.setItem('user', JSON.stringify(user));
    },
    logout(state){
      window.localStorage.removeItem('user');
      window.localStorage.removeItem('address');
      state.address={};
      state.routes = [];
      state.user.username='未登录';
      state.user.id=-1;
    },
    toggleNFDot(state, newValue){
      state.nfDot = newValue;
    },
    updateMsgList(state, newMsgList){
      state.msgList = newMsgList;
    },
    updateCurrentFriend(state, newFriend){
      state.currentFriend = newFriend;
    },
    addValue2DotMap(state, key){
      state.isDotMap.set(key, "您有未读消息")
    },
    removeValueDotMap(state, key){
      state.isDotMap.delete(key);
    },
    setAddress(state, item){
      state.address=item;
      window.localStorage.setItem('address', JSON.stringify(item));
    }
  },
  actions: {
  }
});

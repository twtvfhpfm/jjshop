<template>
  <div>
    <router-view></router-view>

    <van-tabbar route :z-index="1"  style="background-color: rgb(240, 240,240);">
      <van-tabbar-item replace to="/gallery" icon="home-o">首页</van-tabbar-item>
      <van-tabbar-item to="/cart" icon="shopping-cart-o">购物车</van-tabbar-item>
      <van-tabbar-item replace to="/my" icon="user-o">我的</van-tabbar-item>
    </van-tabbar>
  </div>
</template>
<script>
export default {
  data() {
    return {};
  }
};
</script>

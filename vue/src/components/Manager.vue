<template>
<div>
  <van-sticky>
    <van-nav-bar title="管理" left-text="" left-arrow @click-left="onClickLeft" style="background-color: rgb(240, 240,240);"/>
    </van-sticky>
      <van-cell title="商品分类管理" is-link to="/categorymanager" />
      <van-cell title="订单管理" is-link to="/goodsordermanager" />
      <van-cell title="会员管理" is-link to="/usermanager" />
      <van-cell title="收钱码管理" is-link to="/chargecodemanager" />
      <van-cell title="账单管理" is-link to="/billmanager" />
</div>
</template>
<script>
export default {
  data() {
    return {
    };
  },
  methods: {
    onClickLeft() {
      this.$router.back(-1);
    }
  }
};
</script>
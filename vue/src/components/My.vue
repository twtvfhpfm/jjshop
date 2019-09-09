<template>
  <div>
    <div style="position: fixed; top: 10px; left: 10px; z-index: 5;" v-if="loggedin">Hello, {{username}}</div>
    <img
      class="user-poster"
      src="https://img.yzcdn.cn/public_files/2017/10/23/8690bb321356070e0b8c4404d087f8fd.png"
    />
    <!--van-row class="user-links">
      <van-col span="6">
        <van-icon name="pending-payment" />待付款
      </van-col>
      <van-col span="6">
        <van-icon name="records" />待接单
      </van-col>
      <van-col span="6">
        <van-icon name="tosend" />待发货
      </van-col>
      <van-col span="6">
        <van-icon name="logistics" />已发货
      </van-col>
    </van-row-->
    <van-cell-group class="user-group">
      <van-cell icon="records" title="全部订单" is-link to="/goodsorderlist"/>
    </van-cell-group>

    <van-cell-group class="user-group">
      <van-cell icon="gold-coin-o" title="优惠券" is-link to="/coupon" />
      <van-cell icon="location-o" title="收货地址" is-link to="locations" />
    </van-cell-group>
    <van-cell-group class="user-group" v-if="$store.state.user.role==1">
    <van-cell icon="setting-o" title="管理" is-link to="/manager" />
    </van-cell-group>
    <div style="margin: 10px 10px;">
    <van-button type="danger" v-if="loggedin" @click="onClickExit" size="large">退出账号</van-button>
    <van-button type="info" v-else to="/login" size="large">登录</van-button>
    </div>
  </div>
</template>

<script>
import { Row, Col, Icon, Cell, CellGroup } from "vant";
export default {
  data() {
    return {
      username: this.$store.state.user.username,
      loggedin: window.localStorage.getItem("user") != null
    };
  },
  components: {
    [Row.name]: Row,
    [Col.name]: Col,
    [Icon.name]: Icon,
    [Cell.name]: Cell,
    [CellGroup.name]: CellGroup
  },
  methods: {
    onClickExit() {
      this.$toast.loading({duration:0, forbidClick:true, message:'加载中...'});
      this.postRequest("/user/logout")
        .then(resp => {
          this.$toast.clear();
          if(resp.data.status!=200) {this.$toast(resp.data.msg);}
          else{
              console.log(resp);
            this.$toast(resp.data.msg);
            this.$store.commit("removeLoginHistory");
            this.$store.commit("logout");
            console.log(this.loggedin);
            this.loggedin = false;
          }
        })
        .catch(err => {
          this.$toast.clear();
          console.log(err);
          this.$toast("服务器异常");
        });
    }
  }
};
</script>

<style lang="less">
.user {
  &-poster {
    width: 100%;
    height: 53vw;
    display: block;
  }
  &-group {
    margin-bottom: 15px;
    text-align: left;
  }
  &-links {
    padding: 15px 0;
    font-size: 12px;
    text-align: center;
    background-color: #fff;
    .van-icon {
      display: block;
      font-size: 24px;
    }
  }
}
</style>
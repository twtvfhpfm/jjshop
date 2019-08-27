<template>
  <div>
    <van-sticky>
    <van-nav-bar title="收银台" left-text="" left-arrow @click-left="onClickLeft" style="background-color: rgb(240, 240,240);"/>
    </van-sticky>
    <h1 class="redText">￥{{formatPrice(totalPrice)}}</h1>
    <div style="padding: 10px 10px;text-align: left;">请复制订单号：{{orderId}}</div>
    <div style="margin: 10px 10px;">
    <van-button
      type="info"
      size="large"
      :text="btnText"
      v-clipboard:copy="orderId"
      v-clipboard:success="onCopy"
      v-clipboard:error="onError"
    ></van-button>
    </div>
    <div v-if="showstep2" style="padding: 10px 10px;text-align: left;">选择付款方式：</div>
    <van-radio-group v-model="radio" v-if="showstep2">
      <van-cell-group>
        <van-cell title="微信" clickable @click="onChooseWechat">
          <van-radio slot="right-icon" name="1" />
        </van-cell>
        <van-cell title="支付宝" clickable @click="onChooseAlipay">
          <van-radio slot="right-icon" name="2" />
        </van-cell>
      </van-cell-group>
    </van-radio-group>
    <div v-if="showWechat">长按以下二维码进行付款</div>
    <div v-if="showWechat" class="redText">付款时请备注订单号</div>
    <van-image  v-if="showWechat" width="60%" :src="chargeCodeUrl" />
    <div v-if="showAlipay" class="redText">付款时请备注订单号</div>
    <div style="margin: 10px 10px;">
    <van-button type="default" size="large" v-if="showAlipay" @click="alipay">点击去支付宝付款</van-button>
    </div>
    <div style="padding: 30px 0px;"/>
  </div>
</template>
<script>
export default {
  data() {
    return {
        radio: '0',
        showWechat: false,
        showAlipay: false,
        btnText: "点击复制订单号",
        showstep2: false,
      chargeCodeUrl: "/api/chargecode/getwechat?amount=0",
    };
  },
  mounted() {},
  computed: {
    orderId: function() {
      return this.$store.state.pay.orderId;
    },
    totalPrice: function() {
      return this.$store.state.pay.totalPrice;
    }
  },
  methods: {
    alipay(){
      //window.open('/shop/static/alipay.html');
      window.location.href="/shop/static/alipay.html";
    },
    onChooseWechat(){
      this.radio="1";
      this.showWechat=true;
      this.showAlipay=false;
    },
    onChooseAlipay(){
      this.radio="2";
      this.showWechat=false;
      this.showAlipay=true;
    },
    onClickLeft() {
      this.$router.back(-1);
    },
    formatPrice(price) {
      return (price / 100).toFixed(2);
    },
    onCopy(e) {
      this.btnText="已复制到剪贴板";
      this.showstep2=true;
    },
    onError(e) {
      this.$toast("复制失败");
    }
  }
};
</script>
<style>
.redText {
  color: red;
  padding: 10px 0px;
}
</style>


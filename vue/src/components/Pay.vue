<template>
  <div>
    <van-sticky>
    <van-nav-bar title="收银台" left-text="" left-arrow @click-left="onClickLeft" style="background-color: rgb(240, 240,240);"/>
    </van-sticky>
    
    <h1 class="redText">￥{{formatPrice(totalPrice)}}</h1>
    <div v-if="showWechat">
      <van-divider :style="{ color: '#1989fa', borderColor: '#1989fa', padding: '0 16px' }">
        微信支付
      </van-divider>
      <div>长按以下二维码进行付款</div>
      <van-image  v-if="showWechat" width="60%" :src="chargeCodeUrl" />
    </div>
    <div v-if="showAlipay">
      <van-divider :style="{ color: '#1989fa', borderColor: '#1989fa', padding: '0 16px' }">
        支付宝支付
      </van-divider>
      <div style="margin: 10px 10px;">
      <van-button type="default" size="large" v-if="showAlipay" @click="alipay">点击去支付宝付款</van-button>
      </div>
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
        timer: 0,
        count: 0,
      chargeCodeUrl: "",
    };
  },
  mounted() {
    this.$toast.loading({duration:0, forbidClick:true, message:'正在生成二维码...'});
    this.timer = setInterval(this.checkWeChatQRCode, 2000);
  },
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
    checkWeChatQRCode(){
      this.postRequest("/chargecode/checkwechatqrcode",{orderId: this.orderId}).then(resp=>{
        if (resp.data.status == 200){
          this.$toast.clear();
          clearInterval(this.timer);
          this.count = 0;
          this.chargeCodeUrl="/api/chargecode/getwechatqrcode?orderId="+this.orderId;
          this.showWechat=true;
          this.showAlipay=true;
        }else{
          if (this.count++ == 2){
          this.$toast.clear();
          clearInterval(this.timer);
          this.count = 0;
          this.chargeCodeUrl="/api/chargecode/getwechat?amount=0";
          this.showWechat=true;
          this.showAlipay=true;
          }
        }
      }).catch(err=>{
          this.$toast.clear();
          clearInterval(this.timer);
          this.count = 0;
          this.chargeCodeUrl="/api/chargecode/getwechat?amount=0";
          this.showWechat=true;
          this.showAlipay=true;
        console.log(err);
        this.$toast("服务器异常");
      })
    },
    onClickLeft() {
      this.$router.back(-1);
    },
    formatPrice(price) {
      return (price / 100).toFixed(2);
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


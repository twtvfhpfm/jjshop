<template>
  <div>
    <van-sticky>
      <van-nav-bar
        title="提交订单"
        left-text
        left-arrow
        @click-left="onClickLeft"
        style="background-color: rgb(240, 240,240);"
      />
    </van-sticky>
    <van-popup v-model="showLoading" :overlay="false" :close-on-click-overlay="false">
      <van-button
        loading
        type="primary"
        loading-type="spinner"
        style="background-color: grey; border-color: grey;"
      />
    </van-popup>
    <van-cell>
      <div style="color: gray;">收货地址</div>
    </van-cell>
    <van-cell icon="location-o" is-link to="/locations">
      <div v-if="addrItem.name!=undefined">{{addrItem.name}}, {{addrItem.tel}}</div>
      <div v-else>选择收货地址</div>
      <div v-if="addrItem.address!=undefined">{{addrItem.address}}</div>
    </van-cell>
    <van-card
      v-for="item in goods"
      :key="item.id"
      :title="item.title"
      :desc="item.desc"
      :num="item.num"
      :price="formatPrice(item.price)"
      :thumb="item.thumb"
    >
      <div slot="tags">
        <van-tag plain type="danger" v-if="item.s1name!=''">{{item.s1name}}</van-tag>
        <van-tag plain type="danger" v-if="item.s2name!=''">{{item.s2name}}</van-tag>
        <van-tag plain type="danger" v-if="item.s3name!=''">{{item.s3name}}</van-tag>
      </div>
    </van-card>

    <van-submit-bar
      style="bottom: 50px;"
      :price="totalPrice"
      button-text="提交订单"
      @submit="onSubmit"
    />
    <div style="padding: 80px 0px;" />
  </div>
</template>
<script>
export default {
  data() {
    return {
      showLoading: false
    };
  },
  computed: {
    goods: function() {
      return this.$store.state.submitOrder.goods;
    },
    totalPrice: function() {
      return this.$store.state.submitOrder.totalPrice;
    },
    addrItem: function() {
      console.log("address");
      console.log(this.$store.state.address);
      return this.$store.state.address;
    }
  },
  methods: {
    onClickLeft() {
      this.$router.back(-1);
    },
    formatPrice(price) {
      return (price / 100).toFixed(2);
    },
    onSubmit() {
      if (this.addrItem.name==undefined){
        this.$toast("请选择收货地址");
        return;
      }
      var cl = "";
      for (var item of this.goods) {
        cl += item.id + ",";
      }
      if (cl.endsWith(",")) {
        cl = cl.substr(0, cl.length - 1);
      } else {
        this.$toast("未勾选任何商品");
        return;
      }
      this.showLoading = true;
      this.postRequest("/goodsorder/add", {
        uid: this.$store.state.user.id,
        cidList: cl,
        addressId: this.$store.state.address.id
      })
        .then(resp => {
          this.showLoading = false;
          if (resp.data.status != 200) {
            this.$toast(resp.data.msg);
          } else {
            this.$toast("提交成功");
            this.$store.commit("setPay", {
              orderId: resp.data.obj,
              totalPrice: this.totalPrice
            });
            this.$router.replace({ path: "/pay" });
          }
        })
        .catch(err => {
          this.showLoading = false;
          console.log(err);
          this.$toast("服务器异常");
        });
    }
  }
};
</script>
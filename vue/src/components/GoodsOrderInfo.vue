<template>
  <div>
    <van-sticky>
      <van-nav-bar
        title="订单详情"
        left-text
        left-arrow
        @click-left="$router.back(-1);"
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
    <van-action-sheet v-model="logisticShow" :title="'运单号：'+logisticCode" close-on-click-overlay>
        <van-steps direction="vertical" :active="0">
        <van-step v-for="i2 in logisticList" :key="i2.AcceptTime">
            <div style="text-align: left;font-weight:bold;">{{i2.AcceptStation}}</div>
            <div style="text-align: left;">{{i2.AcceptTime}}</div>
        </van-step>
        </van-steps>
    </van-action-sheet>
    <van-cell icon="location-o">
      <div>{{goodsOrder.address.name}}, {{goodsOrder.address.mobile}}</div>
      <div>{{goodsOrder.address.province+goodsOrder.address.city+goodsOrder.address.county+goodsOrder.address.addressDetail}}</div>
    </van-cell>
    <van-card
      v-for="item in goodsOrder.goodsList"
      :key="item.goodsId"
      :title="item.title"
      :desc="item.description"
      :num="item.num"
      :price="item.price.toFixed(2)"
      :thumb="item.thumb"
    >
      <div slot="tags">
        <van-tag v-for="tag in item.tagNames" :key="tag" plain type="danger">{{tag}}</van-tag>
      </div>
    </van-card>
    <div style="padding: 80px 0px;"></div>
    <van-submit-bar
      style="bottom: 50px;"
      :price="goodsOrder.totalPrice*100"
      :button-text="btnText"
      :disabled="btnDisable"
      @submit="onSubmit"
    >
        <van-button type="info" style="position: fixed;bottom:130px;right:10px;" :round="true" icon="logistics" @click="queryLogistic" v-if="goodsOrder.status==2"></van-button>
    </van-submit-bar>
  </div>
</template>

<script>
export default {
  data() {
    return {
      logisticList: [],
      logisticShow: false,
      logisticCode: "",
      goodsOrder: {},
      showLoading: false,
      btnText: "",
      btnDisable: false
    };
  },
  mounted() {
    this.getGoodsOrder();
  },
  methods: {
      queryLogistic(){
          this.showLoading=true;
          this.postRequest("/logistics/getlogistic", {orderId: this.goodsOrder.orderId})
          .then(resp=>{
              this.showLoading=false;
              if(resp.data.status!=200){this.$toast(resp.data.msg);}
              else{
                  this.logisticCode=resp.data.obj.logisticCode;
                  var response = JSON.parse(resp.data.obj.response);
                  if(!response.Success){
                      this.$toast("物流查询失败");
                      return;
                  }
                  this.logisticList = response.Traces.reverse();
                  if(this.logisticList.length==0){
                      this.$toast(response.Reason);
                  }
                  this.logisticShow = true;
              }
          }).catch(err=>{
              this.showLoading=false;
              console.log(err);
              this.$toast("服务器异常");
          })
      },
    confirmReceipt() {
      this.$dialog
        .confirm({
          title: "确认收货",
          message: "确认收货?"
        })
        .then(() => {
          this.showLoading = true;
          this.postRequest("/goodsorder/confirmreceipt", {
            orderId: this.goodsOrder.orderId
          })
            .then(resp => {
              this.showLoading = false;
              if (resp.data.status != 200) {
                this.$toast(resp.data.msg);
              } else {
                this.btnText = "交易完成";
                this.btnDisable = true;
              }
            })
            .catch(err => {
              this.showLoading = false;
              console.log(err);
              this.$toast("服务器异常");
            });
        });
    },
    onSubmit() {
      switch (this.goodsOrder.status) {
        case 0:
          this.$store.commit("setPay", {
            orderId: this.goodsOrder.orderId,
            totalPrice: this.goodsOrder.totalPrice * 100
          });
          this.$router.push({ path: "/pay" });
          break;
        case 2:
          this.confirmReceipt();
          break;
      }
    },
    getGoodsOrder() {
      this.showLoading = true;
      this.postRequest("/goodsorder/get", {
        orderId: this.$store.state.goodsOrder.orderId
      })
        .then(resp => {
          this.showLoading = false;
          if (resp.data.status != 200) {
            this.$toast(resp.data.msg);
          } else {
            this.goodsOrder = resp.data.obj;
            for(var goods of this.goodsOrder.goodsList){
                goods.thumb = goods.imgId!=0 ? '/api/picture/get?id='+goods.imgId : "https://s2.ax1x.com/2019/08/22/mwKZlj.jpg";
            }
            switch (this.goodsOrder.status) {
              case 0:
                this.btnText = "去付款";
                break;
              case 1:
                this.btnText = "待发货";
                this.btnDisable = true;
                break;
              case 2:
                this.btnText = "确认收货";
                break;
              case 3:
                this.btnText = "去评价";
                break;
              case 4:
                this.btnText = "交易完成";
                this.btnDisable = true;
                break;
              case 5:
                this.btnText = "已取消";
                this.btnDisable = true;
                break;
            }
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
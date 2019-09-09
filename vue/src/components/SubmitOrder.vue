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
    <div v-if="couponList.length>0">
      <van-cell-group>
      <van-cell icon="gold-coin-o" title="可用优惠券" style="text-align:left;padding-left:10px;margin-top:10px;"/>
      </van-cell-group>
      <van-radio-group v-model="couponRadio">
        <van-radio v-for="item in couponList" :key="item.id" :name="item.id" class="coupon">
          <div style="height:70px;overflow:auto;width: 95%; margin: 10px 10px;background-image: url(https://s2.ax1x.com/2019/09/05/nmg8PI.png);background-size:100% 100%;">
              <div style="width: 68%;float:left;">
                  <div style="text-align: left;padding: 5px 5px;font-size:small;color:white;">有效期至: {{new Date(item.endTime).toLocaleDateString()}}</div>
                  <div style="text-align: center;width: 100%;font-size: 20px;color:white;">{{(item.amount/1).toFixed(2)}}元</div>
              </div>
              <div style="width: 32%;float:left;height:100%;">
                  <span style="line-height: 70px;font-size:small;">{{goodsTitle(item)}}</span>
              </div>
          </div>
        </van-radio>
      </van-radio-group>
    </div>

    <van-submit-bar
      style="bottom: 50px;"
      :price="totalPrice+transportFee*100-reducedPrice*100"
      button-text="提交订单"
      @submit="onSubmit"
    >
      <div style="font-size:small;color:grey;padding-left:5px;text-align:left;">运费:￥{{(transportFee/1).toFixed(2)}}<br/>
      优惠:￥{{(reducedPrice/1).toFixed(2)}}</div>
    </van-submit-bar>
    <div style="padding: 80px 0px;" />
  </div>
</template>
<script>
export default {
  data() {
    return {
      couponList:[],
      couponRadio:'',
      priceReduceList: [],
      goodsMap: new Map(),/*{id,totalPrice,num,transportFee}*/
      totalNum: 0,
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
    },
    transportFee: function(){
      var fee = 0;
      var ignore = this.priceReduceList;
      for(var g of this.goodsMap){
        fee += g[1].transportFee;
      }
      return fee;
    },
    reducedPrice: function(){
      var price = 0;
      for(var g of this.priceReduceList){
        price += g.amount;
      }
      for(var g of this.couponList){
        if (g.id == this.couponRadio){
          price += g.amount;
          break;
        }
      }
      return price;
    }
  },
  mounted(){
    this.buildGoodsList();
    this.getCouponList();
  },
  methods: {
        buildGoodsList(){
          for(var item of this.goods){
            this.totalNum += item.num;
            if (this.goodsMap.has(item.goodsId)){
              var g = this.goodsMap.get(item.goodsId);
              g.totalPrice+= item.price*item.num/100;
              g.num += item.num;
            }else{
              this.goodsMap.set(item.goodsId, 
              {id: item.goodsId, totalPrice: item.price*item.num/100, num: item.num, transportFee: item.transportFee});
            }
          }
          console.log("goodsMap");
          console.log(this.goodsMap);
        },
        filterCouponList(fullList){
          this.couponList = [];
          for(var item of fullList){
            if (item.isUsed == 0 && item.isDeleted == 0){
              if (item.goodsId == 0){
                if (this.totalPrice/100 >= item.priceCond && this.totalNum >= item.numCond){
                  this.couponList.push(item);
                }
              }else{
                if (this.goodsMap.has(item.goodsId)){
                  var g = this.goodsMap.get(item.goodsId);
                  if (g.totalPrice >= item.priceCond && g.num >= item.numCond){
                    this.couponList.push(item);
                  }
                }
              }
            }
          }
        },
        filterPriceReduceList(fullList){
          this.priceReduceList = [];
          for(var item of fullList){
            var g = this.goodsMap.get(item.goodsId);
            if (g.num >= item.numCond){
              g.transportFee = item.transportFee;
              this.priceReduceList.push(item);
            }
          }
        },
        goodsTitle(item){
            if (item.goodsTitle.length>5){
                return item.goodsTitle.substr(0,5)+'...';
            }
            return item.goodsTitle;
        },
        getCouponList(){
            this.$toast.loading({duration:0, forbidClick:true, message:'加载中...'});
            this.postRequest('/coupon/get', {id: 0, goodsId: 0, orderId: ''})
            .then(resp=>{
                this.$toast.clear();
                if (resp.data.status!=200){this.$toast.fail(resp.data.msg);}
                else {
                    this.filterCouponList(resp.data.obj);
                    this.getPriceReduceList();
                }
            }).catch(err=>{
                this.$toast.clear();
                console.log(err);
                this.$toast.fail("服务器异常");
            })
        },
    getPriceReduceList() {
      var goodsId = '';
      for(var id of this.goodsMap.keys()){
        goodsId+= id+',';
      }
      if (goodsId.endsWith(','))
        goodsId = goodsId.substr(0, goodsId.length - 1);
      this.$toast.loading({duration:0, forbidClick:true, message:'加载中...'});
      this.postRequest("/pricereduce/getbygoodsid", { goodsId: goodsId })
        .then(resp => {
          this.$toast.clear();
          if (resp.data.status != 200) {
            this.$toast(resp.data.msg);
          } else {
            this.filterPriceReduceList(resp.data.obj);
          }
        })
        .catch(err => {
          this.$toast.clear();
          console.log(err);
          this.$toast("服务器异常");
        });
    },
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
      this.$toast.loading({duration:0, forbidClick:true, message:'加载中...'});
      this.postRequest("/goodsorder/add", {
        uid: this.$store.state.user.id,
        cidList: cl,
        addressId: this.$store.state.address.id,
        couponId: this.couponRadio
      })
        .then(resp => {
          this.$toast.clear();
          if (resp.data.status != 200) {
            this.$toast(resp.data.msg);
          } else {
            this.$toast("提交成功");
            this.$store.commit("setPay", {
              orderId: resp.data.obj,
              totalPrice: this.totalPrice+this.transportFee*100-this.reducedPrice*100
            });
            this.$router.replace({ path: "/pay" });
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
.coupon{
  padding-left: 10px;
  .van-radio__label{
    width: 100%;
  }
}
</style>
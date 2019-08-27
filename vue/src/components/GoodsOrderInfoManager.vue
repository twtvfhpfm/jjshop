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
    
        <van-button
          type="info"
          icon="description"
          :round="true"
          v-clipboard:copy="orderInfo"
          v-clipboard:success="onCopy"
          v-clipboard:error="onError"
          style="position: fixed; bottom: 180px; right: 10px;"
        ></van-button>
        <van-button type="info" icon="logistics" :round="true" @click="setLogistic"  style="position: fixed; bottom: 130px; right: 10px;"></van-button>
    <van-row class="bottomFixed">
      <van-col
        span="8"
        offset="0"
        style="text-align: left;font-size: small;color: red;padding: 5px 5px;"
      >
        <div>收入：￥{{(goodsOrder.totalPrice/1).toFixed(2)}}</div>
        <div>支出：￥{{(goodsOrder.totalSuperiorPrice/1).toFixed(2)}}</div>
        <div>赚：￥{{((goodsOrder.totalPrice-goodsOrder.totalSuperiorPrice)/1).toFixed(2)}}</div>
      </van-col>
      <van-col span="8" offset="8" style="padding: 5px 0px;" v-if="goodsOrder.status==0">
        <van-button type="danger" @click="confirmCharge" style="witdh: 100%">确认收款</van-button>
      </van-col>
      <van-col span="8" offset="8" style="padding: 5px 0px;" v-else>
        <van-button type="danger" @click="showPicker=true" :text="btnText" style="witdh: 100%"></van-button>
      </van-col>
    </van-row>
    <van-popup v-model="showPicker" position="bottom">
      <van-picker
        show-toolbar
        :columns="statusList"
        @cancel="showPicker = false"
        @confirm="onPickerConfirm"
      />
    </van-popup>
    
    <van-action-sheet v-model="logisticShow" title="设置物流" close-on-click-overlay>
        <van-row style="padding: 10px 0px;">
            <van-col span="15" offset="1"><van-field v-model="logisticCode" placeholder="请输入运单号"/></van-col>
            <van-col span="6" offset="1"><van-button type="default" @click="getShipper">查询</van-button></van-col>
        </van-row>
        <van-row style="padding:10px 0px;">
            <van-radio-group v-model="shipperRadio" style="padding-left: 10px;">
                <van-radio v-for="item in shipperList" style="padding:20px 5px;" :key="item.ShipperCode" :name="item.ShipperCode">{{item.ShipperName}}</van-radio>
            </van-radio-group>
        </van-row>
        <van-row>
            <van-button type="danger" size="large" @click="saveLogistic">保存</van-button>
        </van-row>
    </van-action-sheet>
  </div>
</template>

<script>
export default {
  data() {
    return {
        shipperRadio: "",
        logisticCode: "",
        shipperCode: "",
        shipperList: [],
      logisticShow: false,
      orderInfo: "",
      statusList: [
        "待付款",
        "待发货",
        "待收货",
        "待评价",
        "交易完成",
        "已取消"
      ],
      showPicker: false,
      goodsOrder: {},
      showLoading: false,
      btnText: "",
      btnDisable: false,
      fullShipperList:[
          {ShipperName: "顺丰", ShipperCode: "SF"},
          {ShipperName: "百世", ShipperCode: "HTKY"},
          {ShipperName: "中通", ShipperCode: "ZTO"},
          {ShipperName: "申通", ShipperCode: "STO"},
          {ShipperName: "圆通", ShipperCode: "YTO"},
          {ShipperName: "韵达", ShipperCode: "YD"},
          {ShipperName: "邮政", ShipperCode: "YZPY"},
          {ShipperName: "EMS", ShipperCode: "EMS"},
          {ShipperName: "天天", ShipperCode: "HHTT"},
          {ShipperName: "京东", ShipperCode: "JD"},
          {ShipperName: "优速", ShipperCode: "UC"},
          {ShipperName: "德邦", ShipperCode: "DBL"},
          {ShipperName: "宅急送", ShipperCode: "ZJS"},
          {ShipperName: "TNT", ShipperCode: "TNT"},
          {ShipperName: "UPS", ShipperCode: "UPS"},
          {ShipperName: "联邦", ShipperCode: "FEDEX"},
          {ShipperName: "安能", ShipperCode: "ANE"},
          {ShipperName: "如风达", ShipperCode: "RFD"}]
    };
  },
  mounted() {
    this.getGoodsOrder();
  },
  methods: {
      saveLogistic(){
          if(this.shipperRadio.trim()==""){
              this.$toast("请选择快递公司");
              return;
          }
          this.showLoading=true;
          var customerName = "";
          if(this.shipperRadio=="SF"){
              customerName=this.goodsOrder.address.mobile.substr(-4);
          }
          this.postRequest("/logistics/update",{
              orderId: this.goodsOrder.orderId,
              customerName: customerName,
              shipperCode: this.shipperRadio,
              logisticCode: this.logisticCode
              })
          .then(resp=>{
              this.showLoading=false;
              if(resp.data.status!=200){this.$toast(resp.data.msg);}
              else{
                  this.$toast("保存成功");
                  this.logisticShow = false;
              }
          }).catch(err=>{
              this.showLoading=false;
              console.log(err);
              this.$toast("服务器异常");
          })
      },
      setLogistic(){
          this.showLoading=true;
          this.postRequest("/logistics/get",{orderId: this.goodsOrder.orderId})
          .then(resp=>{
              this.showLoading=false;
              if(resp.data.status!=200){this.$toast(resp.data.msg);}
             else{
                  this.logisticCode=resp.data.obj.logisticCode;
                  this.shipperCode=resp.data.obj.shipperCode;
             }
                  this.logisticShow = true;
              
          }).catch(err=>{
              this.showLoading=false;
              console.log(err);
              this.$toast("服务器异常");
          })
      },
      getShipper(){
          if(this.logisticCode.trim()==""){
              this.$toast("请输入运单号");
              return;
          }
          this.showLoading=true;
          this.postRequest("/logistics/getshippercode",{logisticCode: this.logisticCode})
          .then(resp=>{
              this.showLoading=false;
              if(resp.data.status!=200){this.$toast(resp.data.msg);}
              else{
                  var response = JSON.parse(resp.data.obj);
                  if(!response.Success){
                      this.$toast("物流查询失败");
                      return;
                  }
                  this.shipperList = response.Shippers;
                  this.shipperRadio="";
                  if(this.shipperList.length==0){
                    this.shipperList = this.fullShipperList;
                      this.$toast("订单号识别失败，请手动选择快递公司");
                  }
              }
          }).catch(err=>{
              this.showLoading=false;
              console.log(err);
              this.$toast("服务器异常");
          })
      },
    confirmCharge() {
      this.$dialog
        .confirm({
          title: "确认收款",
          message: "确认收款" + this.goodsOrder.totalPrice + "元?"
        })
        .then(() => {
          this.onPickerConfirm("待发货");
        });
    },
    onPickerConfirm(value) {
      for (var i = 0; i < this.statusList.length; i++) {
        if (this.statusList[i] == value) {
          this.showLoading = true;
          this.postRequest("/goodsorder/updatestatus", {
            orderId: this.goodsOrder.orderId,
            status: i
          })
            .then(resp => {
              this.showLoading = false;
              if (resp.data.status == 500) {
                this.$toast(resp.data.msg);
              } else {
                this.showPicker = false;
                this.btnText = this.statusList[i];
                this.goodsOrder.status = i;
                this.$toast("状态设置成功");
              }
            })
            .catch(err => {
              this.showLoading = false;
              console.log(err);
              this.$toast("服务器异常");
            });
          break;
        }
      }
      if (i == this.statusList.length) {
        this.$toast("所选状态无效");
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
            this.btnText = this.statusList[this.goodsOrder.status];
            this.orderInfo = "";
            for (var goods of this.goodsOrder.goodsList) {
                goods.thumb = goods.imgId!=0 ? '/api/picture/get?id='+goods.imgId : "https://s2.ax1x.com/2019/08/22/mwKZlj.jpg";
              this.orderInfo += goods.title + ": ";
              for (var tag of goods.tagNames) {
                this.orderInfo += tag + " , ";
              }
              this.orderInfo += goods.num + "件\n";
            }
            this.orderInfo += "---------------------\n";
            this.orderInfo +=
              this.goodsOrder.address.name +
              ", " +
              this.goodsOrder.address.mobile +
              ", " +
              this.goodsOrder.address.province +
              this.goodsOrder.address.city +
              this.goodsOrder.address.county +
              this.goodsOrder.address.addressDetail;
          }
        })
        .catch(err => {
          this.showLoading = false;
          console.log(err);
          this.$toast("服务器异常");
        });
    },
    onCopy(e) {
      this.$toast("已复制到剪贴板");
    },
    onError(e) {
      this.$toast("复制失败");
    }
  }
};
</script>
<style>
.bottomFixed {
  position: fixed;
  bottom: 50px;
  left: 0;
  z-index: 5;
  background-color: rgb(240, 240, 240);
  width: 100%;
}
</style>
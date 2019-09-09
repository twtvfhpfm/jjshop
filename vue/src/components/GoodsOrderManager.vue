<template>
  <div>
    <van-sticky>
      <van-nav-bar
        title="订单列表"
        left-text
        left-arrow
        @click-left="$router.back(-1);"
        style="background-color: rgb(240, 240,240);"
      />
      <form action="/">
        <van-search
          v-model="value"
          placeholder="请输入部分订单号"
          show-action
          background="#f0f0f0"
          type="number"
          @search="onSearch"
          @cancel="onCancel"
        >
          <div slot="action" @click="onSearch">搜索</div>
        </van-search>
      </form>
    </van-sticky>

        <van-list
      v-model="loading"
      :finished="finished"
      :error.sync="error"
      error-text="请求失败，点击重新加载"
      finished-text="没有更多了"
      style="background-color: rgb(240,240,240);"
      @load="onLoad"
    >
        <div v-for="item in list" :key="item.id" @click="onClick(item)" style="margin: 0px 10px;margin-bottom: 10px;background-color:white;border-radius: 10px;">
            <van-row style="padding-top: 10px;">
                <van-col span="17" offset="1" style="text-align: left;">订单号：<span>{{item.orderId}}</span></van-col>
                <van-col span="4" offset="1"><div :style="statusStyle(item.status)">{{item.statusText}}</div></van-col>
            </van-row>
            <van-divider/>
            <van-row style="padding-bottom: 10px;">
                <van-col span="13" offset="1"><div v-for="(title,index) in item.title" :key="index" style="color: grey; text-align: left;font-size:small;">&#8226;&nbsp;{{title}}</div></van-col>
                <van-col span="8" offset="1"><div style="text-align: right;padding: 10px 0px;">￥{{(item.totalPrice/1).toFixed(2)}}</div></van-col>
            </van-row>
        </div> 
    </van-list>
    <div style="padding: 30px 0px;background-color:rgb(240,240,240);"></div>
  </div>
</template>
<script>
export default {
  data() {
    return {
        value: "",
      list: [
          //{orderId: "", status: 0, statusText: "", totalPrice: 0, title:[]}
      ],
      loading: false,
      finished: false,
      error: false,
      lastMinId: 0
    };
  },
  computed:{
  },
  methods: {
    statusStyle(status) {
      if (status == 0){
        return {color: 'red', fontSize: 'small'};
      }else if (status == 1){
        return {color: 'green', fontSize: 'small'};
      }else if (status == 2){
        return {color: 'blue', fontSize: 'small'};
      }else if (status == 3){
        return {color: 'yellow', fontSize: 'small'};
      }else if (status == 4){
        return {color: 'grey', fontSize: 'small'};
      }else if (status == 5){
        return {color: 'grey', fontSize: 'small'};
      }
    },
      onClick(item){
          this.$store.commit("setGoodsOrderId", item.orderId);
          this.$router.push({path:"/goodsorderinfomanager"});
      },
    onSearch() {
      this.lastMinId=0;
      this.list=[];
      this.getOrderList(this.value);
    },
    onCancel() {
      this.$toast("cancel: " + this.value);
    },
    onLoad() {
       this.getOrderList("");
    },
    getOrderList(text){
        this.$toast.loading({duration:0, forbidClick:true, message:'加载中...'});
        this.postRequest("/goodsorder/list",{
            lastMinId: this.lastMinId,
            text: text
        }).then(resp=>{
            this.$toast.clear();
            if(resp.data.status!=200) {this.$toast(resp.data.msg);}
            else{
            if (resp.data.msg=="end") this.finished=true;
            if (resp.data.obj.length > 0) this.lastMinId = resp.data.obj[resp.data.obj.length-1].id;
            for(var item of resp.data.obj){
                var titleList = [];
                for(var goods of item.goodsList){
                    titleList.push(goods.title+" x "+goods.num);
                }
                var statusText = '';
                if (item.status == 0) statusText='待付款';
                else if (item.status == 1) statusText='待发货';
                else if (item.status == 2) statusText='待收货';
                else if (item.status == 3) statusText='待评价';
                else if (item.status == 4) statusText='交易完成';
                else if (item.status == 5) statusText='已取消';
                this.list.push({
                    orderId: item.orderId,
                    status: item.status,
                    totalPrice: item.totalPrice,
                    title: titleList,
                    statusText: statusText
                });
            }
            this.loading=false;
            }
        }).catch(err=>{
            this.loading=false;
            this.$toast.clear();
            this.error=true;
            console.log(err);
            this.$toast("服务器异常");
        })
    },
  }
};
</script>
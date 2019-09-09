<template>
  <div>
      <van-sticky>
    <van-nav-bar title="账单管理" left-text="" left-arrow @click-left="$router.back(-1);" style="background-color: rgb(240, 240,240);"/>
    </van-sticky>

    <van-tabs v-model="active" animated swipeable lazy-render color="#1989fa">
        <van-tab title="日报">
            <van-circle
            v-model="currentRate[0]"
            :rate="currentRate[0]"
            :speed="100"
            :text="text[0]"
            layer-color="#f0f0f0"
            style="padding-top: 30px;"
            />
            <van-list
                v-model="loading[0]"
                :finished="finished[0]"
                :error.sync="error[0]"
                error-text="请求失败，点击重新加载"
                finished-text="没有更多了"
                @load="onLoad"
            >
                <van-cell v-for="item in list[0]" :key="item.date" :title="item.date" :value="'￥'+(item.inCome/1).toFixed(2)"/>
            </van-list>
        </van-tab>
        <van-tab title="周报">
            <van-circle
            v-model="currentRate[1]"
            :rate="currentRate[1]"
            :speed="100"
            :text="text[1]"
            layer-color="#f0f0f0"
            style="padding-top: 30px;"
            />
            <van-list
                v-model="loading[1]"
                :finished="finished[1]"
                :error.sync="error[1]"
                error-text="请求失败，点击重新加载"
                finished-text="没有更多了"
                @load="onLoad"
            >
                <van-cell v-for="item in list[1]" :key="item.week" :title="item.week.substr(0,4)+'年第'+item.week.substr(4,2)+'周'" :value="'￥'+(item.inCome/1).toFixed(2)"/>
            </van-list>
        </van-tab>
        <van-tab title="月报">
            <van-circle
            v-model="currentRate[2]"
            :rate="currentRate[2]"
            :speed="100"
            :text="text[2]"
            layer-color="#f0f0f0"
            style="padding-top: 30px;"
            />
            <van-list
                v-model="loading[2]"
                :finished="finished[2]"
                :error.sync="error[2]"
                error-text="请求失败，点击重新加载"
                finished-text="没有更多了"
                @load="onLoad"
            >
                <van-cell v-for="item in list[2]" :key="item.month" :title="item.month" :value="'￥'+(item.inCome/1).toFixed(2)"/>
            </van-list>
        </van-tab>
    </van-tabs>
    <div style="padding: 30px 0px;"></div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      currentRate:[0,0,0],
      active: 0,
      list: [[],[],[]],
      loading: [false,false,false],
      finished: [false,false,false],
      error: [false,false,false],
      cursor: ["","",""],
      text: ["","",""]
    };
  },
  mounted() {
      var d = new Date();
      this.currentRate[0] = d.getHours()/24*100;
      this.currentRate[1] = d.getDay()/7*100;
      this.currentRate[2] = d.getDate()/30*100;
  },
  computed:{
  },
  methods: {
    getBill(type){
      this.$toast.loading({duration:0, forbidClick:true, message:'加载中...'});
      this.postRequest("/bill/get", {
        cursor: this.cursor[type],
        type: type
      }).then(resp =>{
        this.$toast.clear();
        if(resp.data.status!=200) {this.$toast(resp.data.msg);}
        else{
          var data = resp.data;
          if (data.msg=="end") this.finished[type]=true;
          if (data.obj.length > 0){
           this.list[type] = this.list[type].concat(data.obj);
           if(type==0) this.cursor[type] = data.obj[data.obj.length - 1].date;
           if(type==1) this.cursor[type] = data.obj[data.obj.length - 1].week;
           if(type==2) this.cursor[type] = data.obj[data.obj.length - 1].month;
           this.loading[type]=false;
           this.text[type]=''+this.list[type][0].inCome;
          }else{
            this.loading[type]=false;
          }
        }
      }).catch(err=>{
        this.$toast.clear();
        console.log(err);
        this.$toast("服务器异常");
      })
    },
    onLoad() {
      this.getBill(this.active);
    },
  }
};
</script>
<style>
</style>



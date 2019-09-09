<template>
  <div>
    <van-sticky>
      <van-nav-bar
        title="会员列表"
        left-text
        left-arrow
        @click-left="$router.back(-1);"
        style="background-color: rgb(240, 240,240);"
      />
      <form action="/">
        <van-search
          v-model="value"
          placeholder="请输入搜索关键词"
          show-action
          background="#f0f0f0"
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
    <van-swipe-cell :on-close="onClose" v-for="item in list" :key="item.id" :name="item.id">
        <div style="margin: 0px 10px;margin-bottom: 10px;background-color:white;border-radius: 10px;">
            <van-row style="padding-top: 10px;padding-bottom: 20px;">
                <van-col span="17" offset="1" style="text-align: left;"><span>{{item.username}}</span></van-col>
                <van-col span="4" offset="1">
                    <div style="color: red;font-size:small;" v-if="item.role==0">用户</div>
                    <div style="color: red;font-size:small;" v-if="item.role==1">管理员</div>
                </van-col>
            </van-row>
        </div> 
      <template slot="right">
        <van-button square type="danger" text="设为管理员" v-if="item.role==0" @click="setRole(item, 1)"/>
        <van-button square type="danger" text="设为用户" v-if="item.role==1" @click="setRole(item, 0)"/>
      </template>
    </van-swipe-cell>
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
      ],
      loading: false,
      finished: false,
      error: false,
      lastMinId: 0
    };
  },
  methods: {
    onClose(clickPosition, instance, detail) {
      console.log(detail);
      switch (clickPosition) {
        case "left":
        case "cell":
        case "outside":
          instance.close();
          break;
        case "right":
          break;
      }
    },
    setRole(item, role){
        this.$toast.loading({duration:0, forbidClick:true, message:'加载中...'});
        this.postRequest('/user/update',{
            id: item.id,
            role: role
        }).then(resp=>{
            this.$toast.clear();
            if(resp.data.status!=200) {this.$toast(resp.data.msg);}
            else{
                item.role=role;
            }
        }).catch(err=>{
            this.$toast.clear();
            console.log(err);
            this.$toast("服务器异常");
        })
    },
      onClick(item){
          //this.$store.commit("setGoodsOrderId", item.orderId);
          //this.$router.push({path:"/goodsorderinfo"});
      },
    onSearch() {
      //this.$store.commit("setGoodsList", { text: this.value, categoryId: 0 });
      //this.$router.push({ path: "/goodslist" });
      this.lastMinId=0;
      this.list=[];
      this.getList(this.value);
    },
    onCancel() {
      this.$toast("cancel: " + this.value);
    },
    onLoad() {
      //var categoryId = this.$store.state.manager.categoryId;
       // this.getByCategory(categoryId);
       this.getList("");
    },
    getList(text){
        this.$toast.loading({duration:0, forbidClick:true, message:'加载中...'});
        this.postRequest("/user/list",{
            lastMinId: this.lastMinId,
            text: text
        }).then(resp=>{
        this.$toast.clear();
        if(resp.data.status!=200) {this.$toast(resp.data.msg);}
        else{
            if (resp.data.msg=="end") this.finished=true;
            if (resp.data.obj.length > 0) this.lastMinId = resp.data.obj[resp.data.obj.length-1].id;
            for(var item of resp.data.obj){
                this.list.push(item);
            }
            this.loading=false;
        }
        }).catch(err=>{
            this.loading=false;
        this.$toast.clear();
            console.log(err);
            this.$toast("服务器异常");
        })
    },
  }
};
</script>
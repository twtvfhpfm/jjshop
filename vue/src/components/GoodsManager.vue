<template>
  <div>
      <van-sticky>
    <van-nav-bar title="商品管理" left-text="" left-arrow @click-left="onClickLeft" style="background-color: rgb(240, 240,240);"/>
    </van-sticky>
    <van-list
      v-model="loading"
      :finished="finished"
      :error.sync="error"
      error-text="请求失败，点击重新加载"
      finished-text="没有更多了"
      @load="onLoad"
    >



    <van-swipe-cell :on-close="onClose" v-for="item in list" :key="item.id" :name="item.id">
      <van-cell :title="item.title" @click="OnGoodsClick(item.id)"/>

      <template slot="right">
        <van-button square type="danger" text="删除" />
      </template>
    </van-swipe-cell>


    </van-list>
    <div style="padding: 80px 0px;"></div>
    <van-button type="danger" size="large" text="新增商品" style="position: fixed;bottom: 50px;left: 0;z-index: 5;" @click="OnNewGoods" />
  </div>
</template>
<script>
export default {
  data() {
    return {
      list: [],
      loading: false,
      finished: false,
      error: false,
      lastMinId: 0
    };
  },
  mounted() {
  },
  methods: {
    // clickPosition 表示关闭时点击的位置
    onClose(clickPosition, instance, detail) {
      console.log(detail);
      switch (clickPosition) {
        case "left":
        case "cell":
        case "outside":
          instance.close();
          break;
        case "right":
          this.$dialog
            .confirm({
              message: "确定删除吗？"
            })
            .then(() => {
              instance.close();
              this.$toast.loading({duration:0, forbidClick:true, message:'加载中...'});
              this.postRequest("/goods/delete", {
                id: detail.name
              })
                .then(resp => {
                  this.$toast.clear();
                  if(resp.data.status!=200) {this.$toast(resp.data.msg);}
                  else{
                    this.lastMinId=0;
                    this.list = [];
                    this.onLoad();
                  }
                })
                .catch(err => {
                  this.$toast.clear();
                  console.log(err);
                  this.$toast("服务器异常");
                });
            });
          break;
      }
    },
    onClickLeft() {
      this.$router.back(-1);
    },
    OnNewGoods(e) {
        this.$store.commit('setManagerGoodsId', 0);
        this.$router.push({path: '/goodsinfomanager'});
    },
    getByCategory(catId){
      var _this = this;
      this.$toast.loading({duration:0, forbidClick:true, message:'加载中...'});
      this.postRequest("/goods/getbycategory", {
        categoryId: catId,
        lastMinId: this.lastMinId
      }).then(resp =>{
        this.$toast.clear();
        if(resp.data.status!=200) {this.$toast(resp.data.msg);}
        else{
          var data = resp.data;
          if (data.msg=="end") _this.finished=true;
          if (data.obj.length > 0){
          _this.list = _this.list.concat(data.obj);
          _this.lastMinId = data.obj[data.obj.length - 1].id;
          _this.loading=false;
          }else{
            _this.loading=false;
          }
        }
      }).catch(err=>{
        this.$toast.clear();
        console.log(err);
        this.$toast("服务器异常");
      })
    },
    OnGoodsClick(id){
        this.$store.commit('setManagerGoodsId', id);
        this.$router.push({path: "/goodsinfomanager"});
    },
    onLoad() {
      var categoryId = this.$store.state.manager.categoryId;
    this.getByCategory(categoryId);
    },
  }
};
</script>
<style>
</style>



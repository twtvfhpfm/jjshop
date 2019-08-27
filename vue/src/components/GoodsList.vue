<template>
  <div>
    <van-sticky>
    <van-nav-bar :title="pageTitle" left-text="" left-arrow @click-left="onClickLeft" style="background-color: rgb(240, 240,240);"/>
    </van-sticky>
    <van-popup v-model="showLoading" :overlay="false" :close-on-click-overlay="false">
        <van-button loading type="primary" loading-type="spinner" style="background-color: grey; border-color: grey;"/>
    </van-popup>
    <van-list
      v-model="loading"
      :finished="finished"
      :error.sync="error"
      error-text="请求失败，点击重新加载"
      finished-text="没有更多了"
      @load="onLoad"
    >
      <van-card
        class="goodsCard"
        v-for="item in list"
        :key="item.id"
        :price="item.price"
        :title="item.title"
        :thumb="item.thumb"
        @click="onGoodsClick(item)"
      />
    </van-list>
    <div style="padding: 30px 0px;"></div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      pageTitle: "",
      showLoading: false,
      list: [],
      loading: false,
      finished: false,
      error: false,
      lastMinId: 0
    };
  },

  methods: {
    onClickLeft() {
      this.$router.back(-1);
    },
    getByCategory(catId){
      var _this = this;
      this.showLoading=true;
      this.postRequest("/goods/getbycategory", {
        categoryId: catId,
        lastMinId: this.lastMinId
      }).then(resp =>{
        this.showLoading=false;
        if(resp.data.status!=200) {this.$toast(resp.data.msg);}
        else{
          var data = resp.data;
          if (data.msg=="end") _this.finished=true;
          if (data.obj.length > 0){
            for(var goods of data.obj){
              goods.thumbs = goods.thumb;
              var end = goods.thumb.split(",")[0];
              if (end!=""){
                goods.thumb="/api/picture/get?id=" + end;
              }else{
                goods.thumb="https://s2.ax1x.com/2019/08/22/mwKZlj.jpg";
              }
            }

          _this.list = _this.list.concat(data.obj);
          _this.lastMinId = data.obj[data.obj.length - 1].id;
          _this.loading=false;
          }else{
            _this.loading=false;
          }
        }
      }).catch(err=>{
        this.showLoading=false;
        console.log(err);
        this.$toast("服务器异常");
      })
    },
    getByText(t){
      var _this = this;
      this.showLoading=true;
      this.postRequest("/goods/getbytext", {
        text: t,
        lastMinId: this.lastMinId
      }).then(resp =>{
        this.showLoading=false;
        if(resp.data.status!=200) {this.$toast(resp.data.msg);}
        else{
          var data = resp.data;
          if (data.msg=="end") _this.finished=true;
          if (data.obj.length > 0){

            for(var goods of data.obj){
              goods.thumbs = goods.thumb;
              var end = goods.thumb.split(",")[0];
              if (end!=""){
                goods.thumb="/api/picture/get?id=" + end;
              }else{
                goods.thumb="https://s2.ax1x.com/2019/08/22/mwKZlj.jpg";
              }
            }
          _this.list = _this.list.concat(data.obj);
          _this.lastMinId = data.obj[data.obj.length - 1].id;
          _this.loading=false;
          }else{
            _this.loading=false;
          }
        }
      }).catch(err=>{
        this.showLoading=false;
        console.log(err);
        this.$toast("服务器异常");
      })
    },
    onLoad() {
      var params = this.$store.state.goodsList;
      console.log("params");
      console.log(params);
      if (params.text == null){
        this.pageTitle=params.categoryName;
        console.log("get catId="+params.categoryId);
        this.getByCategory(params.categoryId);
      }else{
        this.pageTitle=params.text;
        this.getByText(params.text);
      }
    },
    onGoodsClick(item) {
      this.$store.commit("setGoods", item);
      this.$router.push({path: "/goodsinfo"});
    }
  }
};
</script>
<style lang="less" scoped>
.goodsCard{
  .van-card__title{
    font-size: large;
    padding: 10px 0px;
  }
  .van-card__price{
    font-size: large;
  }
}
</style>
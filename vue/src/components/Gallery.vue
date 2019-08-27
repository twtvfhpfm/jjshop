<template>
  <div>
    <van-sticky>
    <form action="/">
      <van-search
        v-model="value"
        placeholder="请输入搜索关键词"
        show-action
        background="#f0f0f0"
        @search="onSearch"
        @cancel="onCancel"
      >
        <div slot="action" @click="onSearch" >搜索</div>
      </van-search>
    </form>
    </van-sticky>
    <van-popup v-model="showLoading" :overlay="false" :close-on-click-overlay="false">
        <van-button loading type="primary" loading-type="spinner" style="background-color: grey; border-color: grey;"/>
    </van-popup>
    <van-swipe :autoplay="3000" :width="250">
      <van-swipe-item v-for="(goods, index) in goodsList" :key="index">
        <img v-lazy="goods.thumb" class="img-poster" @click="onImgClick(goods)"/>
      </van-swipe-item>
    </van-swipe>

    <van-grid :gutter="10" :column-num="3" clickable class="classGrid">
      <van-grid-item v-for="cat in categories" :key="cat.id" @click="onCategoryClick(cat)">
        <van-image :src="cat.thumb" :lazy-load="true" />
        <van-cell :value="cat.name" />
      </van-grid-item>
    </van-grid>
    <div :style="paddingStyle">没有更多了</div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      showLoading: false,
      goodsList: [],
      value: "",
      categories: [],
      wait: true,
    };
  },
  computed:{
    paddingStyle() {
      var bottom = window.innerHeight - document.body.offsetHeight;
      if(bottom<0) bottom=30;
      return {paddingBottom: '30px', background: '#f0f0f0', color: 'grey', fontSize: 'small'};
    }
  },
  mounted() {
    this.getSwipeImage();
    //this.getCategory();
  },
  methods: {
    onSearch() {
      if(this.value.trim()==""){
        return;
      }
      this.$store.commit("setGoodsList", {text: this.value.trim(), categoryId: 0, categoryName: ""});
      this.$router.push({path: "/goodslist"});
    },
    onCancel() {
      this.$toast("cancel: " + this.value);
    },
    onCategoryClick(cat) {
      console.log("setGoodsList: catId=" + cat.id);
      this.$store.commit("setGoodsList", {text: null, categoryId: cat.id, categoryName: cat.name});
      this.$router.push({path: "/goodslist"});
    },
    getSwipeImage() {
      this.showLoading=true;
      this.postRequest("/goods/getbyrandom", {})
        .then(resp => {
          this.showLoading=false;
          if(resp.data.status!=200){
            this.$toast(resp.data.msg);
          }else{
            var data = resp.data;
            console.log(data.obj);
            for (var goods of data.obj) {
              goods.thumbs = goods.thumb;
              var end = goods.thumb.split(",")[0];
              if (end!=""){
                goods.thumb="/api/picture/get?id=" + end;
              }else{
                goods.thumb="https://s2.ax1x.com/2019/08/22/mwKZlj.jpg";
              }
              this.goodsList.push(goods);
            }
          }
        }).then(()=>{
          this.getCategory();
        })
        .catch(err => {
          this.showLoading=false;
          console.log(err);
          this.$toast("服务器异常");
        });
    },
    getCategory() {
      this.showLoading=true;
      this.postRequest("/category/list", {})
        .then(resp => {
          this.showLoading=false;
          if(resp.data.status!=200){
            this.$toast(resp.data.msg);
          }else{
            var data = resp.data;
            this.categories = data.obj;
            for(var cat of this.categories){
              var end = cat.thumb.split(",")[0]
              if (end==""){
                  cat.thumb = "https://s2.ax1x.com/2019/08/22/mwKZlj.jpg";
              }else{
                  cat.thumb = "/api/picture/get?id=" + end;
              }
            }
          }
        })
        .catch(err => {
          this.showLoading=false;
          console.log(err);
          this.$toast("服务器异常");
        });
    },
    onImgClick(goods){
      this.$store.commit("setGoods", goods);
      this.$router.push({path: "/goodsinfo"});
    }
  }
};
</script>
<style>
.img-poster {
  width: 100%;
  height: 53vw;
  display: block;
}
.classGrid {
  padding: 10px 0px;
  background-color: #f0f0f0;
}
</style>
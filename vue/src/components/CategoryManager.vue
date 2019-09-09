<template>
  <div>
    <van-sticky>
    <van-nav-bar title="分类管理" left-text="" left-arrow @click-left="onClickLeft" style="background-color: rgb(240, 240,240);"/>
    </van-sticky>
    <van-swipe-cell :on-close="onClose" v-for="item in list" :key="item.id" :name="item.id">
      <van-cell :title="item.name" @click="OnCategoryClick(item.id)" :value="item.count"/>

      <template slot="right">
        <van-button square type="danger" text="删除" />
      </template>
    </van-swipe-cell>

    <div style="padding: 80px 0px;"></div>
    <van-button type="danger" size="large" text="新增分类" style="position: fixed;bottom: 50px;left: 0;z-index: 5;" @click="OnNewCategory" />
    <van-dialog v-model="showNewDialog" title="新增分类" show-cancel-button @confirm="OnNewConfirm">
      <van-field v-model="categoryName" placeholder="请输入分类名称" />
    </van-dialog>
  </div>
</template>
<script>
export default {
  data() {
    return {
      list: [],
      showNewDialog: false,
      categoryName: ""
    };
  },
  mounted() {
    this.getCategoryList();
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
              this.postRequest("/category/delete", {
                id: detail.name
              })
                .then(resp => {
                  this.$toast.clear();
                  if(resp.data.status!=200){
                    this.$toast(resp.data.msg);
                  }else{
                    this.getCategoryList();
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
    OnNewCategory(e) {
      console.log("new");
      this.showNewDialog = true;
    },
    OnNewConfirm() {
      if (this.categoryName.trim() == "") {
        this.$toast("名称不能为空");
        return;
      }
      this.$toast.loading({duration:0, forbidClick:true, message:'加载中...'});
      this.postRequest("/category/add", {
        name: this.categoryName
      })
        .then(resp => {
          this.$toast.clear();
          if(resp.data.status!=200){
            this.$toast(resp.data.msg);
          }else{
            this.categoryName = "";
            this.getCategoryList();
          }
        })
        .catch(err => {
          this.$toast.clear();
          console.log(err);
          this.$toast("服务器异常");
        });
    },
    getCategoryList() {
      this.$toast.loading({duration:0, forbidClick:true, message:'加载中...'});
      this.postRequest("/category/list", {})
        .then(resp => {
          this.$toast.clear();
          if(resp.data.status!=200){
            this.$toast(resp.data.msg);
          }else{
            this.list = resp.data.obj;
          }
        })
        .catch(err => {
          this.$toast.clear();
          console.log(err);
          this.$toast("服务器异常");
        });
    },
    OnCategoryClick(id){
        this.$store.commit('setManagerCategoryId', id);
        this.$router.push({path: "/goodsmanager"});
    }
  }
};
</script>
<style>
</style>



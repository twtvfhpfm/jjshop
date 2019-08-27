<template>
  <div>
    <van-sticky>
      <van-nav-bar
        title="商品管理"
        left-text
        right-text="保存"
        left-arrow
        @click-left="$router.back(-1);"
        @click-right="onClickRight"
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
    <div class="divLabel">填写商品信息</div>
    <van-cell-group>
      <van-field v-model="goods.title" label="标题" placeholder="请输入商品标题" required />
      <van-field
        v-model="goods.description"
        label="描述"
        placeholder="请输入商品描述"
        type="textarea"
        autosize
      />
      <van-field v-model="goods.price" type="number" label="价格" placeholder="请输入商品价格" required />
      <van-field v-model="goods.superiorPrice" type="number" label="上级价格" placeholder="请输入商品上级代理价格" required />
      <van-field v-model="goods.remain" type="number" label="库存" placeholder="请输入商品库存" required />
      <van-field v-model="goods.sales" type="number" label="销量" placeholder="请输入商品销量" required />
      <van-field
        readonly
        clickable
        label="分类"
        :value="goods.categoryName"
        placeholder="选择分类"
        required
        @click="showPicker = true"
      />
    </van-cell-group>
    <van-popup v-model="showPicker" position="bottom">
      <van-picker
        show-toolbar
        :columns="categoryNameList"
        @cancel="showPicker = false"
        @confirm="onPickerConfirm"
      />
    </van-popup>
    <div class="divLabel">上传商品图片</div>
    <van-uploader
      v-model="fileListGoods"
      multiple
      :after-read="afterReadGoods"
      @delete="onDeleteGoods"
    />
    <div class="divLabel">添加商品规格</div>
    <div style="margin: 10px 10px;">
    <van-button type="default" :disabled="disableSkuBtn" @click="onSkuClick" size="large">编辑商品规格</van-button>
    </div>
    <div style="padding: 30px 0px;"></div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      showLoading: false,
      disableSkuBtn: false,
      showPicker: false,
      categories: [],
      categoryNameList: [],
      goods: {
        id: 0,
        categoryId: 0,
        categoryName: "",
        price: "",
        superiorPrice: "",
        remain: "",
        title: "",
        description: "",
        thumb: "",
        sales: 0
      },
      fileListGoods: []
    };
  },
  mounted() {
    this.getCategoryList();
    this.getGoodsInfo();
  },
  methods: {
    onSkuClick() {
      this.onClickRight();
      this.$router.push({ path: "/skumanager" });
    },
    getCategoryList() {
      this.showLoading = true;
      this.postRequest("/category/list", {})
        .then(resp => {
          this.showLoading = false;
          if (resp.data.status != 200) {
            this.$toast(resp.data.msg);
          } else {
            this.categories = resp.data.obj;
            for (var cat of this.categories) {
              this.categoryNameList.push(cat.name);
            }
          }
        })
        .catch(err => {
          this.showLoading = false;
          console.log(err);
          this.$toast("服务器异常");
        });
    },
    getGoodsInfo() {
      var goodsId = this.$store.state.manager.goodsId;
      if (goodsId == 0) {
        this.disableSkuBtn = true;
        return;
      }
      this.showLoading = true;
      this.postRequest("/goods/get", {
        id: goodsId
      })
        .then(resp => {
          this.showLoading = false;
          if (resp.data.status != 200) {
            this.$toast(resp.data.msg);
          } else {
            this.goods = resp.data.obj;
            for (var thumb of this.goods.thumb.split(",")) {
              if (thumb == "") continue;
              this.fileListGoods.push({
                url: "/api/picture/get?id=" + thumb + "&name.jpeg"
              });
            }
          }
        })
        .catch(err => {
          this.showLoading = false;
          console.log(err);
          this.$toast("服务器异常");
        });
    },
    onPickerConfirm(value) {
      this.goods.categoryName = value;
      this.showPicker = false;
      for (var cat of this.categories) {
        if (cat.name == value) {
          this.goods.categoryId = cat.id;
        }
      }
    },
    onClickLeft() {
      this.$router.back(-1);
    },
    onClickRight() {
      var action = this.goods.id == 0 ? "add" : "update";
      this.showLoading = true;
      this.postRequest("/goods/" + action, {
        id: this.goods.id,
        categoryId: this.goods.categoryId,
        price: this.goods.price - 0,
        superiorPrice: this.goods.superiorPrice - 0,
        remain: this.goods.remain - 0,
        title: this.goods.title,
        description: this.goods.description,
        thumb: this.goods.thumb,
        sales: this.goods.sales
      })
        .then(resp => {
          this.showLoading=false;
          if(resp.data.status!=200) {this.$toast(resp.data.msg);}
          else{
            if (action == "add") {
              this.goods.id = resp.data.obj;
              this.$store.commit("setManagerGoodsId", this.goods.id);
            }
            this.disableSkuBtn = false;
            this.$toast("保存成功");
          }
        })
        .catch(err => {
          this.showLoading=false;
          console.log(err);
          this.$toast("服务器异常");
        });
    },
    afterReadGoods(files) {
      this.afterRead(files, this.fileListGoods, this.goods);
    },
    onDeleteGoods(file) {
      this.onDelete(file, this.fileListGoods, this.goods);
    },
    afterRead(files, fileList, obj) {
      // 此时可以自行将文件上传至服务器
      console.log(files);
      if (files.length == undefined) files = [files];
      for (var i = 0; i < files.length; i++) fileList.pop();
      for (var file of files) {
        var needle = "base64,";
        var start = file.content.indexOf(needle) + needle.length;
        this.showLoading=true;
        this.postRequest("/picture/add", {
          base64Data: file.content.substr(start)
        })
          .then(resp => {
            this.showLoading=false;
            if(resp.data.status!=200) {this.$toast(resp.data.msg);}
            else{
              obj.thumb += "," + resp.data.obj;
              if (obj.thumb.startsWith(",")) obj.thumb = obj.thumb.substr(1);
              console.log(this.goods.thumb);
              fileList.push({
                url: "/api/picture/get?id=" + resp.data.obj + "&name.jpeg"
              });
            }
          })
          .catch(err => {
            this.showLoading=false;
            console.log(err);
            this.$toast("服务器异常");
          });
      }
    },
    onDelete(file, fileList, obj) {
      var id = file.url.split("=")[1].split("&")[0] - "0";
      if (obj.thumb.startsWith(id + ","))
        obj.thumb = obj.thumb.replace(id + ",", "");
      else if (obj.thumb == id) obj.thumb = "";
      else obj.thumb = obj.thumb.replace("," + id, "");
      console.log(this.goods.thumb);
    }
  }
};
</script>
<style>
.divLabel {
  color: white;
  text-align: left;
  padding: 10px 10px;
  background-color: rgb(130, 180, 255);
}
</style>

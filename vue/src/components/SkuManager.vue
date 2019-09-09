<template>
  <div>
    <van-sticky>
      <van-nav-bar
        title="商品规格管理"
        left-text
        right-text="保存"
        left-arrow
        @click-left="$router.back(-1);"
        @click-right="OnRightClick"
        style="background-color: rgb(240, 240,240);"
      />
    </van-sticky>
    <van-dialog
      v-model="showDialog"
      title="编辑规格"
      show-cancel-button
      @confirm="onDialogConfirm"
      @cancel="onDialogCancel"
      :before-close="beforeDialogClose"
      cancel-button-text="删除"
      cancel-button-color="#f00"
      close-on-click-overlay
    >
      <van-field v-model="editValueName" label placeholder="请输入规格值" required />
      <van-uploader
        v-model="fileList"
        :after-read="afterRead"
        :max-count="1"
        :disabled="disableUpload"
        @delete="onDelete"
      />
    </van-dialog>
    <van-field
      v-model="keyName1"
      label="规格一"
      :border="true"
      placeholder="请输入规格名称，如颜色、尺寸等"
      style="color: white;background-color: rgb(130, 180, 255);"
    />

    <van-grid :column-num="2" :border="false" clickable>
      <van-grid-item v-for="cat in s1List" :key="cat.name" @click="onSClick(cat)">
        <van-button type="default" size="normal" :icon="cat.thumb" :text="cat.name.substr(0,7)"></van-button>
      </van-grid-item>
    </van-grid>
    <van-field
      v-model="keyName2"
      label="规格二"
      :border="true"
      placeholder="请输入规格名称，如颜色、尺寸等"
      style="color: white;background-color: rgb(130, 180, 255);"
    />
    <van-grid :column-num="2" :border="false" clickable>
      <van-grid-item v-for="cat in s2List" :key="cat.name" @click="onSClick(cat)">
        <van-button type="default" size="normal" :icon="cat.thumb" :text="cat.name.substr(0,9)"></van-button>
      </van-grid-item>
    </van-grid>
    <van-field
      v-model="keyName3"
      label="规格三"
      :border="true"
      placeholder="请输入规格名称，如颜色、尺寸等"
      style="color: white;background-color: rgb(130, 180, 255);"
    />
    <van-grid :column-num="2" :border="false" clickable>
      <van-grid-item v-for="cat in s3List" :key="cat.name" @click="onSClick(cat)">
        <van-button type="default" size="normal" :icon="cat.thumb" :text="cat.name.substr(0,9)"></van-button>
      </van-grid-item>
    </van-grid>
    <div class="divLabel">价格/库存</div>
    <!--div style="margin: 10px 10px;">
    <van-button
      type="default"
      :disabled="disableSkuBtn"
      @click="onSkuListClick"
      size="large"
    >编辑库存/价格</van-button>
    </div-->
    <div style="margin: 10px 10px;">
    <van-button
      type="default"
      :disabled="disableSkuBtn"
      @click="onBatchSkuListClick"
      size="large"
    >编辑库存/价格</van-button>
    </div>
    <div style="padding: 30px 0px;"></div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      disableSkuBtn: false,
      disableUpload: false,
      editItem: {},
      editValueName: "",
      fileList: [],
      showDialog: false,
      keyName1: "",
      keyName2: "",
      keyName3: "",
      s1List: [{ id: 0, thumb: "plus", imgId: 0, name: "", order: 1 }],
      s2List: [{ id: 0, thumb: "plus", imgId: 0, name: "", order: 2 }],
      s3List: [{ id: 0, thumb: "plus", imgId: 0, name: "", order: 3 }]
    };
  },
  mounted() {
    this.getSku();
  },
  methods: {
    onSkuListClick() {
      this.OnRightClick();
      this.$router.push({ path: "/skulistmanager" });
    },
    onBatchSkuListClick(){
      this.OnRightClick();
      this.$router.push({path: "/batchskulistmanager"});
    },
    onSClick(item) {
      if (item.order == 1) this.disableUpload = false;
      else this.disableUpload = true;
      this.editItem = item;
      this.showDialog = true;
      this.editValueName = item.name;
      this.fileList = item.thumb == "plus" ? [] : [{ url: item.thumb }];
    },
    getSku() {
      var goodsId = this.$store.state.manager.goodsId;
      if (goodsId == 0) {
        this.disableSkuBtn = true;
        return;
      }

      this.s1List = [{ id: 0, thumb: "plus", imgId: 0, name: "", order: 1 }];
      this.s2List = [{ id: 0, thumb: "plus", imgId: 0, name: "", order: 2 }];
      this.s3List = [{ id: 0, thumb: "plus", imgId: 0, name: "", order: 3 }];
      this.$toast.loading({duration:0, forbidClick:true, message:'加载中...'});
      this.postRequest("/sku/getbygoodsid", {
        goodsId: goodsId
      })
        .then(resp => {
          this.$toast.clear();
          if (resp.data.status != 200) {
            this.$toast(resp.data.msg);
          } else {
            var sku = resp.data.obj;
            for (var item of sku) {
              if (item.deleted) continue;
              if (item.skuOrder == 1) {
                this.s1List.push({
                  id: item.id,
                  thumb: "/api/picture/get?id=" + item.imgId + "&name.jpeg",
                  imgId: item.imgId,
                  name: item.valueName,
                  order: 1
                });
                this.keyName1 = item.keyName;
              } else if (item.skuOrder == 2) {
                this.s2List.push({
                  id: item.id,
                  thumb: "",
                  imgId: item.imgId,
                  name: item.valueName,
                  order: 2
                });
                this.keyName2 = item.keyName;
              } else if (item.skuOrder == 3) {
                this.s3List.push({
                  id: item.id,
                  thumb: "",
                  imgId: item.imgId,
                  name: item.valueName,
                  order: 3
                });
                this.keyName3 = item.keyName;
              }
            }
          }
        })
        .catch(err => {
          this.$toast.clear();
          console.log(err);
          this.$toast("服务器异常");
        });
    },
    buildSaveList() {
      var list = [];
      var goodsId = this.$store.state.manager.goodsId;
      for (var item of this.s1List) {
        if (item.thumb == "plus") continue;
        list.push({
          id: item.id,
          goodsId: goodsId,
          imgId: item.imgId,
          keyName: this.keyName1,
          valueName: item.name,
          skuOrder: 1
        });
      }
      for (var item of this.s2List) {
        if (item.thumb == "plus") continue;
        list.push({
          id: item.id,
          goodsId: goodsId,
          imgId: item.imgId,
          keyName: this.keyName2,
          valueName: item.name,
          skuOrder: 2
        });
      }
      for (var item of this.s3List) {
        if (item.thumb == "plus") continue;
        list.push({
          id: item.id,
          goodsId: goodsId,
          imgId: item.imgId,
          keyName: this.keyName3,
          valueName: item.name,
          skuOrder: 3
        });
      }
      console.log("list");
      console.log(list);
      return list;
    },
    OnRightClick() {
      var list = this.buildSaveList();
      this.$toast.loading({duration:0, forbidClick:true, message:'加载中...'});
      this.jsonPostRequest("/sku/addorupdate", list)
        .then(resp => {
          this.$toast.clear();
          if (resp.data.status != 200) {
            this.$toast(resp.data.msg);
          } else {
            this.getSku();
          }
        })
        .catch(err => {
          this.$toast.clear();
          console.log(err);
          this.$toast("服务器异常");
        });
    },
    afterRead(files) {
      // 此时可以自行将文件上传至服务器
      console.log(files);
      if (files.length == undefined) files = [files];
      for (var i = 0; i < files.length; i++) this.fileList.pop();
      for (var file of files) {
        var needle = "base64,";
        var start = file.content.indexOf(needle) + needle.length;
        this.$toast.loading({duration:0, forbidClick:true, message:'加载中...'});
        this.postRequest("/picture/add", {
          base64Data: file.content.substr(start)
        })
          .then(resp => {
            this.$toast.clear();
            if (resp.data.status != 200) {
              this.$toast(resp.data.msg);
            } else {
              this.fileList.push({
                url: "/api/picture/get?id=" + resp.data.obj + "&name.jpeg"
              });
            }
          })
          .catch(err => {
            this.$toast.clear();
            console.log(err);
            this.$toast("服务器异常");
          });
      }
    },
    onDelete(file) {
      var id = file.url.split("=")[1].split("&")[0] - "0";
    },
    onDialogConfirm() {
      if (this.editValueName.trim() == "") return;
      var thumb = this.fileList.length == 0 ? "" : this.fileList[0].url;
      var imgId = thumb == "" ? 0 : thumb.split("=")[1].split("&")[0] - "0";
      var newItem = {
        id: 0,
        name: this.editValueName,
        thumb: thumb,
        imgId: imgId,
        order: this.editItem.order
      };
      if (this.editItem.thumb == "plus") {
        if (this.editItem.order == 1) this.s1List.push(newItem);
        if (this.editItem.order == 2) this.s2List.push(newItem);
        if (this.editItem.order == 3) this.s3List.push(newItem);
      } else {
        this.editItem.thumb = thumb;
        this.editItem.name = this.editValueName;
        this.editItem.imgId = imgId;
      }
    },
    onDialogCancel() {
      this.$dialog
        .confirm({
          title: "删除",
          message: "确认删除?"
        })
        .then(() => {
          // on confirm
          if (this.editItem.thumb == "plus") {
            //do nothing
          } else {
            if (this.editItem.id != 0) {
              this.$toast.loading({duration:0, forbidClick:true, message:'加载中...'});
              this.postRequest("/sku/delete", {
                id: this.editItem.id
              })
                .then(resp => {
                  this.$toast.clear();
                  if (resp.data.status != 200) {
                    this.$toast(resp.data.msg);
                  }
                })
                .catch(err => {
                  this.$toast.clear();
                  console.log(err);
                  this.$toast("服务器异常");
                });
            }
            var list;
            if (this.editItem.order == 1) list = this.s1List;
            if (this.editItem.order == 2) list = this.s2List;
            if (this.editItem.order == 3) list = this.s3List;
            var idx = list.indexOf(this.editItem);
            if (idx != -1) {
              list.splice(idx, 1);
            }
          }
        })
        .catch(() => {
          // on cancel
        });
    },
    beforeDialogClose(action, done) {
      if (action == "confirm" && this.editValueName.trim() == "") {
        this.$toast("规格值不能为空");
        done(false);
      } else {
        done();
      }
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
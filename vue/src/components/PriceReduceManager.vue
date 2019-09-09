<template>
  <div>
    <van-sticky>
      <van-nav-bar
        title="满减/运费管理"
        left-text
        left-arrow
        @click-left="$router.back(-1);"
        style="background-color: rgb(240, 240,240);"
      />
    </van-sticky>
    <van-dialog
      v-model="showDialog"
      show-cancel-button
      :before-close="beforeDialogClose"
      close-on-click-overlay
    >
      <van-field
        v-model="priceReduce.numCond"
        type="number"
        label="件数要求"
        placeholder="请输入件数要求"
        required
      />
      <van-field
        v-model="priceReduce.amount"
        type="number"
        label="优惠金额"
        placeholder="请输入优惠金额"
        required
      />
      <van-field
        v-model="priceReduce.superiorAmount"
        type="number"
        label="上级优惠金额"
        placeholder="请输入上级优惠金额"
        required
      />
      <van-field
        v-model="priceReduce.transportFee"
        type="number"
        label="运费"
        placeholder="请输入运费"
        required
      />
    </van-dialog>
    <van-row style="padding: 10px 0px;background-color: #888888;color: white;">
      <van-col span="6">件数</van-col>
      <van-col span="6">优惠</van-col>
      <van-col span="6">上级优惠</van-col>
      <van-col span="6">运费</van-col>
    </van-row>
    <van-swipe-cell v-for="item in priceReduceList" :key="item.id">
      <van-row style="padding: 10px 0px;">
        <van-col span="6">{{item.numCond}}</van-col>
        <van-col span="6">{{item.amount}}</van-col>
        <van-col span="6">{{item.superiorAmount}}</van-col>
        <van-col span="6">{{item.transportFee}}</van-col>
      </van-row>
      <template slot="right">
        <van-button square type="info" text="编辑" @click="onEdit(item)" />
        <van-button square type="danger" text="删除" @click="onDelete(item)" />
      </template>
    </van-swipe-cell>

    <div style="padding: 80px 0px;"></div>
    <van-button
      type="danger"
      size="large"
      text="新增满减"
      style="position: fixed;bottom: 50px;left: 0;z-index: 5;"
      @click="onNewReduce"
    />
  </div>
</template>
<script>
export default {
  data() {
    return {
      showDialog: false,
      priceReduce: {
        id: 0,
        goodsId: 0,
        amount: 0,
        superiorAmount: 0,
        numCond: 0,
        transportFee: 0
      },
      priceReduceList: []
    };
  },
  mounted() {
      this.getPriceReduceList();
  },
  methods: {
    getPriceReduceList() {
      var goodsId = this.$store.state.manager.goodsId;
      if (goodsId == 0) {
        return;
      }
      this.$toast.loading({duration:0, forbidClick:true, message:'加载中...'});
      this.postRequest("/pricereduce/getbygoodsid", { goodsId: goodsId })
        .then(resp => {
          this.$toast.clear();
          if (resp.data.status != 200) {
            this.$toast(resp.data.msg);
          } else {
            this.priceReduceList = resp.data.obj;
          }
        })
        .catch(err => {
          this.$toast.clear();
          console.log(err);
          this.$toast("服务器异常");
        });
    },
    onNewReduce() {
      this.priceReduce = {
        id: 0,
        goodsId: this.$store.state.manager.goodsId,
        amount: "",
        superiorAmount: "",
        numCond: "",
        transportFee: ""
      };
      this.showDialog = true;
    },
    onEdit(item) {
      this.priceReduce = item;
      this.showDialog = true;
    },
    onDelete(item) {
      this.$dialog
        .confirm({
          title: "删除",
          message: "确认删除?"
        })
        .then(() => {
          // on confirm
          this.$toast.loading({duration:0, forbidClick:true, message:'加载中...'});
          this.postRequest("/pricereduce/delete", {
            id: item.id
          })
            .then(resp => {
              this.$toast.clear();
              this.$toast(resp.data.msg);
              if (resp.data.status == 200) {
                var idx = this.priceReduceList.indexOf(item);
                this.priceReduceList.splice(idx, 1);
              }
            })
            .catch(err => {
              this.$toast.clear();
              console.log(err);
              this.$toast("服务器异常");
            });
        })
        .catch(() => {
          // on cancel
        });
    },
    beforeDialogClose(action, done) {
      if (action == "confirm") {
        if (
          (''+this.priceReduce.numCond).trim() == "" ||
          (''+this.priceReduce.amount).trim() == "" ||
          (''+this.priceReduce.superiorAmount).trim() == "" ||
          (''+this.priceReduce.transportFee).trim() == ""
        ) {
          this.$toast("值不能为空");
          done(false);
        }else{
            done();
            this.onDialogConfirm();
        }
      } else {
        done();
      }
    },
    onDialogConfirm() {
      var action = this.priceReduce.id == 0 ? "add" : "update";
      this.$toast.loading({duration:0, forbidClick:true, message:'加载中...'});
      this.postRequest("/pricereduce/" + action, this.priceReduce)
        .then(resp => {
          this.$toast.clear();
          this.$toast(resp.data.msg);
          this.getPriceReduceList();
        })
        .catch(err => {
          this.$toast.clear();
          console.log(err);
          this.$toast("服务器异常");
        });
    }
  }
};
</script>
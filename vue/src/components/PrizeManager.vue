<template>
  <div>
    <van-sticky>
      <van-nav-bar
        title="奖品管理"
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
        v-model="prize.amount"
        type="number"
        label="金额"
        placeholder="请输入金额"
        required
      />
      <van-field
        v-model="prize.num"
        type="number"
        label="数量"
        placeholder="请输入数量"
        required
      />
      <van-field
        v-model="prize.expireDays"
        type="number"
        label="有效期"
        placeholder="请输入有效天数"
        required
      />
    </van-dialog>
    <van-row style="padding: 10px 0px;background-color: #aaaaaa;color: white;">
      <van-col span="6">金额</van-col>
      <van-col span="6">数量</van-col>
      <van-col span="6">有效天数</van-col>
      <van-col span="6">概率</van-col>
    </van-row>
    <van-swipe-cell v-for="item in prizeList" :key="item.id">
      <van-row style="padding: 10px 0px;background-color: #eeeeee;">
        <van-col span="6">￥{{(item.amount/1).toFixed(2)}}</van-col>
        <van-col span="6">{{item.num}}</van-col>
        <van-col span="6">{{item.expireDays}}</van-col>
        <van-col span="6">{{item.probability}}</van-col>
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
      text="新增奖品"
      style="position: fixed;bottom: 50px;left: 0;z-index: 5;"
      @click="onNewPrize"
    />
  </div>
</template>
<script>
export default {
  data() {
    return {
      showDialog: false,
      prize: {
        id: 0,
        num: 0,
        amount: 0,
        expireDays: 0,
      },
      prizeList: []
    };
  },
  mounted() {
      this.getPrizeList();
  },
  methods: {
    calcProbability(){
        var totalNum = 0;
        for(var item of this.prizeList){
            totalNum += item.num;
        }
        for(var item of this.prizeList){
            item.probability = (item.num / totalNum).toFixed(2);
        }
    },
    getPrizeList() {
      this.$toast.loading({duration:0, forbidClick:true, message:'加载中...'});
      this.postRequest("/prize/list")
        .then(resp => {
          this.$toast.clear();
          if (resp.data.status != 200) {
            this.$toast(resp.data.msg);
          } else {
            this.prizeList = resp.data.obj;
            this.calcProbability();
          }
        })
        .catch(err => {
          this.$toast.clear();
          console.log(err);
          this.$toast("服务器异常");
        });
    },
    onNewPrize() {
      this.prize = {
        id: 0,
        num: "",
        amount: "",
        expireDays: "",
      };
      this.showDialog = true;
    },
    onEdit(item) {
      this.prize = item;
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
          this.postRequest("/prize/delete", {
            id: item.id
          })
            .then(resp => {
              this.$toast.clear();
              this.$toast(resp.data.msg);
              if (resp.data.status == 200) {
                var idx = this.prizeList.indexOf(item);
                this.prizeList.splice(idx, 1);
                this.calcProbability();
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
          (''+this.prize.num).trim() == "" ||
          (''+this.prize.amount).trim() == "" ||
          (''+this.prize.expireDays).trim() == ""
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
      var action = this.prize.id == 0 ? "add" : "update";
      this.$toast.loading({duration:0, forbidClick:true, message:'加载中...'});
      this.jsonPostRequest("/prize/" + action, this.prize)
        .then(resp => {
          this.$toast.clear();
          this.$toast(resp.data.msg);
          this.getPrizeList();
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
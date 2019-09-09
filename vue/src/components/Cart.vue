<template>
  <div>
    <van-sticky>
      <van-nav-bar
        title="购物车"
        left-text
        left-arrow
        @click-left="onClickLeft"
        style="background-color: rgb(240, 240,240);"
      />
    </van-sticky>
    <van-checkbox-group class="card-goods" v-model="checkedGoods">

      <van-swipe-cell :on-close="onClose" v-for="item in goods" :key="item.id" :name="item.id">


      <van-checkbox class="card-goods__item" :name="item.id" :disabled="item.notAvail">
        <div v-if="item.notAvail" style="background: #f0f0f09f;z-index: 5;position: absolute; top: 0px; left: 0px; width: 100%; height: 100%;"/>
        <div v-if="item.notAvail" style="background: red;color: white;z-index: 999;position: absolute;top: 15px; left: 40px;width: 30px;">无货</div>
        <van-card
          :title="item.title"
          :desc="item.desc"
          :num="item.num"
          :price="formatPrice(item.price)"
          :thumb="item.thumb"
        >
          <div slot="tags">
            <van-tag plain type="danger" v-if="item.s1name!=''">{{item.s1name}}</van-tag>
            <van-tag plain type="danger" v-if="item.s2name!=''">{{item.s2name}}</van-tag>
            <van-tag plain type="danger" v-if="item.s3name!=''">{{item.s3name}}</van-tag>
          </div>
        </van-card>
      </van-checkbox>

      <template slot="right" style="width: 210px;">
        <van-button square type="info" style="height: 100%;width: 70px;font-size:large;" text="-" @click="onMinus(item)" :disabled="item.num==1"/>
        <van-button square type="info" style="height: 100%;width: 70px;font-size:large;" text="+" @click="onPlus(item)"/>
        <van-button square type="danger" style="height: 100%;width: 70px;" text="删除" @click="onDelete(item)"/>
      </template>
      <div style="height: 2px;background:white;"/>

      </van-swipe-cell>


    </van-checkbox-group>
    <div v-if="cartEmpty" style="color: grey;">购物车空空如也</div>
    <van-submit-bar
      style="bottom: 50px;"
      :price="totalPrice"
      :disabled="!checkedGoods.length"
      :button-text="submitBarText"
      @submit="onSubmit"
    />
    <div style="padding: 80px 0px;" />
  </div>
</template>

<script>
import { Checkbox, CheckboxGroup, Card, SubmitBar, Toast } from "vant";
export default {
  components: {
    [Card.name]: Card,
    [Checkbox.name]: Checkbox,
    [SubmitBar.name]: SubmitBar,
    [CheckboxGroup.name]: CheckboxGroup
  },
  mounted() {
    this.getCart();
  },
  data() {
    return {
      checkedGoods: [],
      goods: [],
      cartEmpty: false
    };
  },
  computed: {
    submitBarText() {
      const count = this.checkedGoods.length;
      return "结算" + (count ? `(${count})` : "");
    },
    totalPrice() {
      console.log(this.checkedGoods);
      return this.goods.reduce(
        (total, item) =>
          total +
          (this.checkedGoods.indexOf(item.id) !== -1
            ? item.price * item.num
            : 0),
        0
      );
    }
  },
  methods: {
    onMinus(item){
      console.log("minus: "+item.id);
      if(item.num>1){
        this.changeNum(item, -1);
      }
    },
    onPlus(item){
      console.log("plus: "+item.id);
      this.changeNum(item, 1);
    },
    changeNum(item, delta){
      this.$toast.loading({duration:0, forbidClick:true, message:'加载中...'});
      item.num+=delta;
      this.postRequest("/cart/changenum",{delta: delta, id: item.id})
      .then(resp=>{
        this.$toast.clear();
        if(resp.data.status!=200) {this.$toast(resp.data.msg);}
      }).catch(err=>{
        this.$toast.clear();
        console.log(err);
        this.$toast("服务器异常");
      })
    },
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
    getCart() {
      this.$toast.loading({duration:0, forbidClick:true, message:'加载中...'});
      this.postRequest("/cart/getbyuid", {
        uid: this.$store.state.user.id
      })
        .then(resp => {
          this.$toast.clear();
          if (resp.data.status != 200) {
            this.$toast(resp.data.msg);
          } else {
            if (resp.data.obj.length == 0) this.cartEmpty = true;
            for (var cartModel of resp.data.obj) {
              var end = cartModel.goods.thumb.split(",")[0];
              if (end != "") {
                var thumb =
                  "/api/picture/get?id=" + end;
              } else {
                thumb = "https://s2.ax1x.com/2019/08/22/mwKZlj.jpg";
              }
              var goods = {
                id: "" + cartModel.id,
                goodsId: cartModel.goods.id,
                title: cartModel.goods.title,
                desc: cartModel.goods.description,
                price: cartModel.goods.price * 100,
                transportFee: cartModel.goods.transportFee,
                num: cartModel.num,
                thumb: thumb,
                s1name: "",
                s2name: "",
                s3name: "",
                notAvail: (cartModel.goods.deleted || cartModel.goods.remain==0)
              };
              if (cartModel.skuListId != 0) {
                for (var skulist of cartModel.goods.skuList) {
                  if (skulist.id == cartModel.skuListId) break;
                }
                if (skulist.stockNum==0) goods.notAvail=true;
                goods.price = skulist.price * 100;
                for (var sku of cartModel.goods.sku) {
                  if (skulist.s1Id == sku.id) {
                    goods.s1name = sku.valueName;
                    if(sku.deleted) goods.notAvail=true;
                  }
                  if (skulist.s2Id == sku.id) {
                    goods.s2name = sku.valueName;
                    if(sku.deleted) goods.notAvail=true;
                  }
                  if (skulist.s3Id == sku.id) {
                    goods.s3name = sku.valueName;
                    if(sku.deleted) goods.notAvail=true;
                  }
                }
              }
              this.goods.push(goods);
            }
          }
        })
        .catch(err => {
          this.$toast.clear();
          console.log(err);
          this.$toast("服务器异常");
        });
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
          this.postRequest("/cart/delete", {
            id: item.id - "0"
          })
            .then(resp => {
              this.$toast.clear();
              this.$toast(resp.data.msg);
              if (resp.data.status == 200) {
                var idx = this.goods.indexOf(item);
                this.goods.splice(idx, 1);
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
    onClickLeft() {
      this.$router.back(-1);
    },
    formatPrice(price) {
      return (price / 100).toFixed(2);
    },
    onSubmit() {
      console.log(this.totalPrice);
      var submitOrder = {
        totalPrice: this.totalPrice,
        goods: []
      };
      for (var item of this.goods) {
        if (this.checkedGoods.indexOf(item.id) != -1) {
          submitOrder.goods.push(item);
        }
      }
      this.$store.commit("setSubmitOrder", submitOrder);
      this.$router.push({ path: "/submitorder" });
    }
  }
};
</script>

<style lang="less">
.card-goods {
  padding: 10px 0;
  background-color: #fff;
  &__item {
    position: relative;
    background-color: #fafafa;
    .van-checkbox__label {
      width: 100%;
      height: auto; // temp
      padding: 0 10px 0 15px;
      box-sizing: border-box;
    }
    .van-checkbox__icon {
      top: 50%;
      left: 10px;
      z-index: 1;
      position: absolute;
      margin-top: -10px;
    }
    .van-card__price {
      color: #f44;
    }
  }
}
</style>
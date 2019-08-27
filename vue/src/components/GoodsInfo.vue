<template>
  <div class="goods">
    <van-sticky>
    <van-nav-bar title left-text="" left-arrow @click-left="onClickLeft" style="background-color: rgb(240, 240,240);"/>
    </van-sticky>
    <van-swipe class="goods-swipe" :autoplay="3000">
      <van-swipe-item v-for="thumb in goods.thumbs" :key="thumb">
        <img :src="thumb" />
      </van-swipe-item>
    </van-swipe>
    <van-popup v-model="showLoading" :overlay="false" :close-on-click-overlay="false">
        <van-button loading type="primary" loading-type="spinner" style="background-color: grey; border-color: grey;"/>
    </van-popup>
    <van-cell-group>
      <van-cell>
        <div class="goods-title">{{ goods.title }}</div>
        <div class="goods-price">{{ formatPrice(goods.price*100) }}</div>
      </van-cell>
      <van-cell class="goods-express">
        <van-col span="6">运费：0</van-col>
        <van-col span="9">剩余：{{ goods.remain }}</van-col>
        <van-col span="9">销量：{{ goods.sales }}</van-col>
      </van-cell>
    </van-cell-group>

    <!--van-cell-group class="goods-cell-group">
      <van-cell title="查看商品详情" is-link @click="sorry" />
    </van-cell-group-->
    <div style="text-align:left;padding: 10px 10px;">{{goods.description}}</div>
    <div style="padding: 30px 0px;"/>
    <van-submit-bar style="bottom: 50px;" button-text="加入购物车" @submit="onClickShowSku" />
    <van-sku
      v-model="show"
      :sku="sku"
      :goods="goodssku"
      :goods-id="goods.id"
      :quota="quota"
      :quota-used="quotaUsed"
      :hide-stock="sku.hide_stock"
      :close-on-click-overlay="true"
      :show-add-cart-btn="false"
      buy-text="加入购物车"
      @buy-clicked="onAddCartClicked"
      @add-cart="onAddCartClicked"
    />
  </div>
</template>

<script>
import {
  Tag,
  Col,
  Icon,
  Cell,
  CellGroup,
  Swipe,
  Toast,
  SwipeItem,
  GoodsAction,
  GoodsActionIcon,
  GoodsActionButton
} from "vant";
export default {
  components: {
    [Tag.name]: Tag,
    [Col.name]: Col,
    [Icon.name]: Icon,
    [Cell.name]: Cell,
    [CellGroup.name]: CellGroup,
    [Swipe.name]: Swipe,
    [SwipeItem.name]: SwipeItem,
    [GoodsAction.name]: GoodsAction,
    [GoodsActionIcon.name]: GoodsActionIcon,
    [GoodsActionButton.name]: GoodsActionButton
  },
  mounted() {
    this.getGoods();
  },
  data() {
    return {
      goods: {
        price: 0,
        remain: 0,
        title: "",
        id: 0,
        categoryId: 0,
        description: "",
        thumbs: [],
        sales: 0,
      },
      showLoading:false,
      quota: 0,
      quotaUsed: 0,
      show: false,
      sku: {
        // 所有sku规格类目与其值的从属关系，比如商品有颜色和尺码两大类规格，颜色下面又有红色和蓝色两个规格值。
        // 可以理解为一个商品可以有多个规格类目，一个规格类目下可以有多个规格值。
        tree: [],
        // 所有 sku 的组合列表，比如红色、M 码为一个 sku 组合，红色、S 码为另一个组合
        list: [],
        price: "1.00", // 默认价格（单位元）
        stock_num: 227, // 商品总库存
        collection_id: 2261, // 无规格商品 skuId 取 collection_id，否则取所选 sku 组合对应的 id
        none_sku: false, // 是否无规格商品
        hide_stock: false // 是否隐藏剩余库存
      },
      goodssku: {
        // 商品标题
        title: "测试商品",
        // 默认商品 sku 缩略图
        picture: ""
      }
    };
  },
  methods: {
    getGoods(){
      this.showLoading=true;
      this.postRequest("/goods/get",{id: this.$store.state.goods.id})
      .then(resp=>{
        this.showLoading=false;
        if(resp.data.status!=200){
          this.$toast(resp.data.msg);
        }else{
          this.goods=resp.data.obj;
          this.goods.thumbs=[];
          var thumbs = this.goods.thumb;
          for(var id of thumbs.split(',')){
            if (id.length>0){
              this.goods.thumbs.push("/api/picture/get?id=" + id);
            }
          }
          if (this.goods.thumbs.length==0){
            this.goods.thumbs.push("https://s2.ax1x.com/2019/08/22/mwKZlj.jpg");
          }
          
          this.sku.price=this.goods.price;
          this.sku.stock_num=this.goods.remain;
          this.sku.collection_id=this.goods.id;
          this.goodssku.title=this.goods.title;
          this.goodssku.picture=this.goods.thumbs[0];

          if (this.goods.sku.length==0){
            this.sku.none_sku=true;
          }else{
            for(var item of this.goods.sku){
              if (item.deleted) continue;
              this.sku.none_sku=false;
              if (this.sku.tree[item.skuOrder-1] == undefined){
                this.sku.tree.push({});
                var s = this.sku.tree[item.skuOrder-1];
                s.k=item.keyName;
                s.v=[];
                s.k_s="s"+item.skuOrder;
              }
              var url = item.imgId!=0 ? "/api/picture/get?id="+item.imgId : "https://s2.ax1x.com/2019/08/22/mwKZlj.jpg";
              s.v.push({
                id: ""+item.id,
                name: item.valueName,
                imgUrl: url
              });
            }
          }

          for(var item of this.goods.skuList){
            if(item.deleted) continue;
            this.sku.list.push({
              id: item.id,
              price: item.price*100,
              s1: ""+item.s1Id,
              s2: ""+item.s2Id,
              s3: ""+item.s3Id,
              stock_num: item.stockNum
            });
          }
        }
      }).catch(err=>{
        this.showLoading=false;
        console.log(err);
        this.$toast("服务器异常");
      })
    },
    formatPrice() {
      return "¥" + (this.goods.price).toFixed(2);
    },
    sorry() {
      Toast("暂无后续逻辑~");
    },
    onClickLeft() {
      this.$router.back(-1);
    },
    onClickShowSku() {
      this.show = true;
    },
    onAddCartClicked(data) {
      if (this.$store.state.user.id == -1){
        this.$router.push({path:"/login"});
        return;
      }
      var skuId = this.sku.none_sku ? 0 : data.selectedSkuComb.id;
      console.log(data);
      this.show = false;
      this.showLoading=true;
      this.postRequest("/cart/add",{
        uid: this.$store.state.user.id,
        goodsId: data.goodsId,
        skuListId: skuId,
        num: data.selectedNum,
        ordered: 0
      }).then(resp=>{
        this.showLoading=false;
        this.$toast(resp.data.msg);
      }).catch(err=>{
        this.showLoading=false;
        console.log(err);
        this.$toast("添加失败");
      })
    }
  }
};
</script>

<style lang="less">
.goods {
  padding-bottom: 50px;
  &-swipe {
    img {
      width: 100%;
      display: block;
    }
  }
  &-title {
    font-size: 16px;
  }
  &-price {
    color: #f44;
  }
  &-express {
    color: #999;
    font-size: 12px;
    padding: 5px 15px;
  }
  &-cell-group {
    margin: 15px 0;
    .van-cell__value {
      color: #999;
    }
  }
  &-tag {
    margin-left: 5px;
  }
}
</style>
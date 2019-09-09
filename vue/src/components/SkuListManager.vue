<template>
    <div>

    <van-sticky>
      <van-nav-bar
        title="商品库存管理"
        left-text
        right-text="保存"
        left-arrow
        @click-left="$router.back(-1);"
        @click-right="OnRightClick"
        style="background-color: rgb(240, 240,240);"
      />
    </van-sticky>
    <div v-for="item in list" :key="item.id">
    <van-row><div class="divLabel">{{item.name}}</div></van-row>
    <van-row>
        <van-col span="7" offset="1"><van-field v-model="item.price" type="number" placeholder="请输入价格" style="background-color: rgb(240, 240,240);"/></van-col>
        <van-col span="7" offset="1"><van-field v-model="item.stockNum" type="number" placeholder="请输入库存" style="background-color: rgb(240, 240,240);"/></van-col>
        <van-col span="7" offset="1"><van-field v-model="item.superiorPrice" type="number" placeholder="上级价格" style="background-color: rgb(240, 240,240);"/></van-col>
        </van-row>
    </div>
        <div style="padding: 30px 0px;"></div>
    </div>
</template>
<script>
export default {
    data(){
        return {
            list: [{id: 0,s1Id:0,s2Id:0,s3Id:0,name:"",price:"",superiorPrice:"",stockNum:""}],
            goods:{}
        };
    },
    mounted(){
        this.getGoods();
    },
    methods:{
        OnRightClick(){
            var goodsId = this.$store.state.manager.goodsId;
            if(goodsId==0)return;
            for(var item of this.list){
                item.goodsId=goodsId;
            }
            this.$toast.loading({duration:0, forbidClick:true, message:'加载中...'});
            this.jsonPostRequest("/skulist/addorupdate", this.list).then(resp=>{
                this.$toast.clear();
                if(resp.data.status!=200) {this.$toast(resp.data.msg);}
                else{
                    this.getGoods();
                    this.$toast("保存成功");
                }
            }).catch(err=>{
                this.$toast.clear();
                console.log(err);
                this.$toast("服务器异常");
            })
        },
        getGoods(){
            var goodsId = this.$store.state.manager.goodsId;
            if (goodsId==0) return;
            this.$toast.loading({duration:0, forbidClick:true, message:'加载中...'});
            this.postRequest("/goods/get",{
                id: goodsId
            }).then(resp=>{
                this.$toast.clear();
                if(resp.data.status!=200) {this.$toast(resp.data.msg);}
                else{
                    this.goods = resp.data.obj;

                    var s1List=[];
                    var s2List=[];
                    var s3List=[];
                    for(var sku of this.goods.sku){
                        if (sku.deleted) continue;
                        if (sku.skuOrder==1) s1List.push(sku);
                        if (sku.skuOrder==2) s2List.push(sku);
                        if (sku.skuOrder==3) s3List.push(sku);
                    }
                    this.list=[];
                    for(var s1 of s1List){
                        if (s2List.length==0){
                            this.list.push({s1Id:s1.id,s2Id:0,s3Id:0,name:s1.valueName,price:this.goods.price,superiorPrice:this.goods.superiorPrice,stockNum:this.goods.remain})
                        }else{
                            for(var s2 of s2List){
                                if (s3List.length==0){
                                    this.list.push({s1Id:s1.id,s2Id:s2.id,s3Id:0,name:s1.valueName+" - "+s2.valueName,price:this.goods.price,superiorPrice:this.goods.superiorPrice,stockNum:this.goods.remain})
                                }else{
                                    for(var s3 of s3List){
                                        this.list.push({s1Id:s1.id,s2Id:s2.id,s3Id:s3.id,name:s1.valueName+" - "+s2.valueName+" - "+s3.valueName,price:this.goods.price,superiorPrice:this.goods.superiorPrice,stockNum:this.goods.remain})
                                    }
                                }
                            }
                        }
                    }


                    for(var item of this.goods.skuList){
                        if(item.deleted) continue;
                        for(var s of this.list){
                            if (s.s1Id == item.s1Id && s.s2Id==item.s2Id && s.s3Id==item.s3Id){
                                s.price=item.price;
                                s.superiorPrice=item.superiorPrice;
                                s.stockNum=item.stockNum;
                                s.id=item.id;
                            }
                        }
                    }

                }
            }).catch(err=>{
                this.$toast.clear();
                console.log(err);
                this.$toast("服务器异常");
            })
        }
    }
}
</script>
<style>


.divLabel {
  color: white;
  text-align: left;
  padding: 10px 10px;
  margin-bottom: 10px;
  margin-top: 10px;
  background-color: rgb(130, 180, 255);
}
</style>
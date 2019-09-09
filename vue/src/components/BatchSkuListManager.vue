<template>
    <div>

    <van-sticky>
      <van-nav-bar
        title="商品库存批量管理"
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
      title="编辑库存/价格"
      show-cancel-button
      @confirm="onDialogConfirm"
    >
      <van-field v-model="dialogPrice" type="number" label="价格" placeholder="请输入价格" />
      <van-field v-model="dialogStockNum" type="number" label="库存" placeholder="请输入库存" />
      <van-field v-model="dialogSuperiorPrice" type="number" label="上级价格" placeholder="请输入上级价格" />
    </van-dialog>
    <div class="divLabel">规格一</div>
    <van-row>
    <van-checkbox-group v-model="s1Result" :max="1">
        <van-col span="7" offset="1" v-for="item in s1List" :key="item.id">
            <van-checkbox
                :name="item.id"
            >
                {{ item.valueName }}
            </van-checkbox>
        </van-col>
    </van-checkbox-group>
    </van-row>
    <div class="divLabel">规格二</div>
    <van-row>
    <van-checkbox-group v-model="s2Result" :max="1">
        <van-col span="7" offset="1" v-for="item in s2List" :key="item.id">
            <van-checkbox
                :name="item.id"
            >
                {{ item.valueName }}
            </van-checkbox>
        </van-col>
    </van-checkbox-group>
    </van-row>
    <div class="divLabel">规格三</div>
    <van-row>
    <van-checkbox-group v-model="s3Result" :max="1">
        <van-col span="7" offset="1" v-for="item in s3List" :key="item.id">
            <van-checkbox
                :name="item.id"
            >
                {{ item.valueName }}
            </van-checkbox>
        </van-col>
    </van-checkbox-group>
    </van-row>
    <div style="margin: 10px 10px;">
    <van-button
      type="info"
      @click="onBatchSetClick"
      size="large"
    >批量编辑库存/价格</van-button>
    </div>
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
            dialogPrice: "",
            dialogSuperiorPrice: "",
            dialogStockNum: "",
            showDialog: false,
            s1Result:[],
            s2Result:[],
            s3Result:[],
            s1List:[],
            s2List:[],
            s3List:[],
            list: [{id: 0,s1Id:0,s2Id:0,s3Id:0,name:"",price:"",superiorPrice:"",stockNum:""}],
            goods:{}
        };
    },
    mounted(){
        this.getGoods();
    },
    methods:{
        onDialogConfirm(){
            for (var item of this.list){
                console.log(item);
                if ((this.s1Result.length == 0 || item.s1Id == this.s1Result[0])
                    && (this.s2Result.length == 0 || item.s2Id == this.s2Result[0])
                    && (this.s3Result.length == 0 || item.s3Id == this.s3Result[0])){
                        if (this.dialogPrice.trim()!="") item.price=this.dialogPrice.trim();
                        if (this.dialogSuperiorPrice.trim()!="") item.superiorPrice=this.dialogSuperiorPrice.trim();
                        if (this.dialogStockNum.trim()!="") item.stockNum=this.dialogStockNum.trim();
                    }
            }
        },
        onBatchSetClick(){
            this.dialogPrice="";
            this.dialogSuperiorPrice="";
            this.dialogStockNum="";
            this.showDialog=true;
        },
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

                    this.s1List=[];
                    this.s2List=[];
                    this.s3List=[];
                    for(var sku of this.goods.sku){
                        if (sku.deleted) continue;
                        if (sku.skuOrder==1) this.s1List.push(sku);
                        if (sku.skuOrder==2) this.s2List.push(sku);
                        if (sku.skuOrder==3) this.s3List.push(sku);
                    }
                    this.list=[];
                    for(var s1 of this.s1List){
                        if (this.s2List.length==0){
                            this.list.push({s1Id:s1.id,s2Id:0,s3Id:0,name:s1.valueName,price:this.goods.price,superiorPrice:this.goods.superiorPrice,stockNum:this.goods.remain})
                        }else{
                            for(var s2 of this.s2List){
                                if (this.s3List.length==0){
                                    this.list.push({s1Id:s1.id,s2Id:s2.id,s3Id:0,name:s1.valueName+" - "+s2.valueName,price:this.goods.price,superiorPrice:this.goods.superiorPrice,stockNum:this.goods.remain})
                                }else{
                                    for(var s3 of this.s3List){
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
<template>
    <div>
    <van-sticky>
      <van-nav-bar
        title="优惠券"
        left-text
        left-arrow
        @click-left="$router.back(-1);"
        style="background-color: rgb(240, 240,240);"
      />
    </van-sticky>
    <van-pull-refresh v-model="isLoading" @refresh="onRefresh">
    <div v-if="couponList.length==0">
        <div style="padding: 10px 0px; color: grey;">没有券</div>
    </div>
    <div v-for="item in couponList" :key="item.id" style="height:100px;overflow:auto;width: 95%; margin: 10px 10px;background-image: url(https://s2.ax1x.com/2019/09/05/nmg8PI.png);background-size:100% 100%;">
        <div style="width: 68%;float:left;">
            <div style="text-align: left;padding: 5px 5px;font-size:small;color:white;">有效期至: {{new Date(item.endTime.replace("+0000","Z")).toLocaleString()}}</div>
            <div style="text-align: left;width: 100%;text-align:center;font-size: 30px;color:white;">{{(item.amount/1).toFixed(2)}}元</div>
            <div style="text-align: left;padding: 5px 5px;font-size:small;color:white;">使用条件: {{useCondition(item)}}</div>
        </div>
        <div style="width: 32%;float:left;height:100%;">
            <van-notice-bar color="#000000" background="#ffffff" scrollable style="font-size:small; margin: 5px 5px;height:90%;">{{item.goodsTitle}}</van-notice-bar>
            <!--span style="line-height: 100px;font-size:small;">{{goodsTitle(item)}}</span-->
        </div>
    </div>
    <div style="padding: 80px 0px;"></div>
    </van-pull-refresh>
    <van-button type="danger" size="large" :disabled="drawCount<=0" :text="'您当前有'+drawCount+'次抽奖机会'" style="position: fixed;bottom: 50px;left: 0;z-index: 5;" @click="draw" />
    </div>
</template>
<script>
export default {
    data(){
        return{
            couponList:[],
            drawCount: 0,
            isLoading: false,
        }
    },
    mounted(){
        this.getCouponList();
    },
    computed:{
    },
    methods:{
        onRefresh(){
            this.getCouponList();
        },
        draw(){
            this.$toast.loading({duration:0, forbidClick:true, message:'正在抽奖...'});
            setTimeout(() => {
                this.postRequest('/prizedraw/draw')
                .then(resp=>{
                    this.$toast.clear();
                    if (resp.data.status!=200){this.$toast.fail(resp.data.msg);}
                    else {
                        this.drawCount-=1;
                        this.$toast(resp.data.msg);
                    }
                }).catch(err=>{
                    this.$toast.clear();
                    console.log(err);
                    this.$toast.fail("服务器异常");
                })
            }, 2000);
        },
        getDrawCount(){
            this.$toast.loading({duration:0, forbidClick:true, message:'加载中...'});
            this.postRequest('/prizedraw/count')
            .then(resp=>{
                this.$toast.clear();
                if (resp.data.status!=200){this.$toast.fail(resp.data.msg);}
                else {
                    this.drawCount = resp.data.obj.num;
                }
            }).catch(err=>{
                this.$toast.clear();
                console.log(err);
                this.$toast.fail("服务器异常");
            })
        },
        useCondition(item){
            if (item.priceCond==0 && item.numCond<=1) return '无';
            var str = '';
            if (item.priceCond>0){
                str = +item.priceCond+'元以上'
            }
            if (item.numCond>1){
                str += ','+item.numCond+'件以上'
            }
            str+='可用';
            return str;
        },
        goodsTitle(item){
            if (item.goodsTitle.length>6){
                return item.goodsTitle.substr(0,6)+'...';
            }
            return item.goodsTitle;
        },
        getCouponList(){
            this.isLoading=false;
            this.$toast.loading({duration:0, forbidClick:true, message:'加载中...'});
            this.postRequest('/coupon/get', {id: 0, goodsId: 0, orderId: ''})
            .then(resp=>{
                this.$toast.clear();
                if (resp.data.status!=200){this.$toast.fail(resp.data.msg);}
                else {
                    this.couponList = resp.data.obj.filter(i => i.isUsed==0 && i.isDeleted==0);
                }
                this.getDrawCount();
            }).catch(err=>{
                this.$toast.clear();
                console.log(err);
                this.$toast.fail("服务器异常");
            })
        }
    }
}
</script>
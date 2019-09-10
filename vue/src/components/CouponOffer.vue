<template>
    <div>
    <van-sticky>
      <van-nav-bar
        title="发放优惠券"
        left-text
        right-text="发放"
        left-arrow
        @click-left="$router.back(-1);"
        @click-right="onRightClick"
        style="background-color: rgb(240, 240,240);"
      />
    </van-sticky>
    <van-action-sheet v-model="listShow" :actions="list" close-on-click-overlay @select="onSelect" class="actionList">
    </van-action-sheet>
    <van-action-sheet v-model="dateShow" close-on-click-overlay>
        <van-datetime-picker
            v-model="currentDate"
            type="datetime"
            :min-date="minDate"
            :max-date="maxDate"
            @confirm="dateConfirm"
            :formatter="dateFormatter"
        />
    </van-action-sheet>
    <van-cell-group>
        <van-field v-model="username" readonly label="发放对象" placeholder="请选择发放对象">
            <van-button slot="button" size="small" type="primary" @click="getUserList">选择</van-button>
        </van-field>
        <van-field v-model="goodsTitle" readonly label="适用商品" placeholder="请选择适用商品">
            <van-button slot="button" size="small" type="primary" @click="getGoodsList">选择</van-button>
        </van-field>
        <van-field v-model="coupon.priceCond" type="number" label="价格条件" placeholder="请输入使用优惠券的价格条件"/>
        <van-field v-model="coupon.numCond" type="number" label="数量条件" placeholder="请输入使用优惠券的数量条件"/>
        <van-field v-model="coupon.amount" type="number" label="金额" placeholder="请输入优惠券金额"/>
        <van-field v-model="coupon.beginTime" readonly label="开始时间" placeholder="请选择开始时间">
            <van-button slot="button" size="small" type="primary" @click="dateType=1;dateShow=true">选择</van-button>
        </van-field>
        <van-field v-model="coupon.endTime" readonly label="结束时间" placeholder="请选择结束时间">
            <van-button slot="button" size="small" type="primary" @click="dateType=2;dateShow=true">选择</van-button>
        </van-field>
    </van-cell-group>
    <div style="padding: 30px 0px;"></div>
    </div>
</template>
<script>
export default {
    data(){
        return{
            listShow: false,
            dateShow: false,
            dateType: 1,
            list:[],
            coupon: {type:1,uid:'',goodsId:'',priceCond:'',numCond:'',amount:'',beginTime:'',endTime:''},
            username: '',
            goodsTitle: '',
            userList: [],
            goodsList: [],
            radio: '',
            minDate: new Date(),
            maxDate: new Date(2021, 1, 1),
            currentDate: new Date(),
        }
    },
    methods:{
        onRightClick(){
            if ((this.coupon.uid+'').trim()=='' || (this.coupon.goodsId+'').trim()==''
            || (this.coupon.priceCond+'').trim()=='' || (this.coupon.numCond+'').trim()==''
            || (this.coupon.amount+'').trim()=='' || (this.coupon.beginTime+'').trim()==''
            || (this.coupon.endTime+'').trim()==''){
                this.$toast.fail('未填写完整');
                return;
            }
            this.$toast.loading({duration:0, forbidClick:true, message:'加载中...'});
            this.postRequest('/coupon/offer', this.coupon)
            .then(resp=>{
                this.$toast.clear();
                if (resp.data.status!=200){this.$toast.fail(resp.data.msg);}
                else {
                    this.$toast.success('发放成功');
                }
            }).catch(err=>{
                this.$toast.clear();
                console.log(err);
                this.$toast.fail("服务器异常");
            })
        },
        dateFormatter(type, value){
            switch(type){
                case 'year': return value+'年';
                case 'month': return value+'月';
                case 'day': return value+'日';
                case 'hour': return value+'时';
                case 'minute': return value+'分';
                default: return value;
            }
        },
        dateConfirm(value){
            this.dateShow = false;
            var dateStr = value.getFullYear()+'-'+(value.getMonth()+1+'').padStart(2,'0')+'-'+(value.getDate()+'').padStart(2,'0')+' '
                            +(value.getHours()+'').padStart(2,'0')+':'+(value.getMinutes()+'').padStart(2,'0')+':00';
            if (this.dateType==1){
                this.coupon.beginTime = dateStr;
            }else{
                this.coupon.endTime = dateStr;
            }
        },
        onSelect(item){
            this.listShow=false;
            if (this.list == this.userList){
                this.username = item.name;
                this.coupon.uid = item.id;
            }else{
                this.goodsTitle = item.name;
                this.coupon.goodsId = item.id;
            }
        },
        getUserList(){
            this.$toast.loading({duration:0, forbidClick:true, message:'加载中...'});
            this.postRequest('/user/listall')
            .then(resp=>{
                this.$toast.clear();
                if (resp.data.status!=200){this.$toast.fail(resp.data.msg);}
                else {
                    this.userList = [{id: 0, name: "所有人"}];
                    for(var item of resp.data.obj){
                        this.userList.push({id: item.id, name: item.username});
                    }
                    this.list = this.userList;
                    this.listShow = true;
                }
            }).catch(err=>{
                this.$toast.clear();
                console.log(err);
                this.$toast.fail("服务器异常");
            })
        },
        getGoodsList(){
            this.$toast.loading({duration:0, forbidClick:true, message:'加载中...'});
            this.postRequest('/goods/listall')
            .then(resp=>{
                this.$toast.clear();
                if (resp.data.status!=200){this.$toast.fail(resp.data.msg);}
                else {
                    this.goodsList = [{id: 0, name: "所有商品"}];
                    for(var item of resp.data.obj){
                        this.goodsList.push({id: item.id, name: item.title});
                    }
                    this.list = this.goodsList;
                    this.listShow = true;
                }
            }).catch(err=>{
                this.$toast.clear();
                console.log(err);
                this.$toast.fail("服务器异常");
            })
        }
    }
}
</script>
<style lang="less">
.actionList{
    .van-action-sheet__item{
        text-align: left;
        padding-left: 10px;
    }
}
</style>
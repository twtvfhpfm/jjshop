<template>
    <div>
    <van-popup v-model="showLoading" :overlay="false" :close-on-click-overlay="false">
      <van-button
        loading
        type="primary"
        loading-type="spinner"
        style="background-color: grey; border-color: grey;"
      />
    </van-popup>
    <div style="font-size: large;padding: 20px 0px;color: grey;">{{text}}</div>
    </div>
</template>
<script>
export default {
    data(){
        return{
            showLoading: false,
            text: "",
        }
    },
    mounted(){
        if (navigator.userAgent.indexOf("MicroMessenger") == -1){
            this.getUrl();
        }else{
            this.text="请点击右上角，选择在浏览器打开";
        }
    },
    methods:{
        getUrl(){
            this.showLoading=true;
            this.postRequest("/chargecode/getalipay",{amount: 0})
            .then(resp=>{
                this.showLoading=false;
                if (resp.data.status!=200){this.$toast(resp.data.msg);}
                else{
                    window.location.href=resp.data.obj.url;
                }
            }).catch(err=>{
                this.showLoading=false;
                console.log(err);
                this.$toast("服务器异常");
            })
        }
    }
}
</script>
<template>
    <div>
    <div style="font-size: large;padding: 20px 0px;color: grey;">{{text}}</div>
    </div>
</template>
<script>
export default {
    data(){
        return{
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
            this.$toast.loading({duration:0, forbidClick:true, message:'加载中...'});
            this.postRequest("/chargecode/getalipay",{amount: 0})
            .then(resp=>{
                this.$toast.clear();
                if (resp.data.status!=200){this.$toast(resp.data.msg);}
                else{
                    window.location.href=resp.data.obj.url;
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
<template>
    <div>
      <van-sticky>
      <van-nav-bar
        title="收钱码管理"
        left-text
        right-text="保存"
        left-arrow
        @click-left="$router.back(-1);"
        @click-right="onClickRight"
        style="background-color: rgb(240, 240,240);"
      />
      </van-sticky>
    <van-popup v-model="showLoading" :overlay="false" :close-on-click-overlay="false">
        <van-button loading type="primary" loading-type="spinner" style="background-color: grey; border-color: grey;"/>
    </van-popup>
      <div style="padding: 10px 10px;text-align:left;">微信收款码</div>
      <van-uploader
        v-model="fileList"
        :after-read="afterRead"
        :max-count="1"
        @delete="onDelete"
        />
    <div style="padding: 10px 10px;text-align:left;">支付宝收款地址</div>
    <div style="margin: 10px 10px;">
      <van-field
        v-model="value"
        placeholder="请输入支付宝收款地址"
        type="textarea"
        style="background: rgb(230,230,230);"
        autosize
      />
    </div>
    </div>
</template>
<script>
export default {
    data(){
        return{
            value: "",
            showLoading: false,
            fileList:[{url: "/api/chargecode/getwechat?amount=0&name.jpeg"}],
        }
    },
    mounted(){
        this.showLoading=true;
        this.postRequest("/chargecode/getalipay",{amount: 0})
        .then(resp=>{
            this.showLoading=false;
            if(resp.data.status!=200){this.$toast(resp.data.msg);}
            else{
                this.value=resp.data.obj.url;
            }
        }).catch(err=>{
            this.showLoading=false;
            console.log(err);
            this.$toast("服务器异常");
        })
    },
    methods:{
        onClickRight(){
            this.showLoading=true;
            this.postRequest("/chargecode/addalipay",{amount: 0, url: this.value})
            .then(resp=>{
                this.showLoading=false;
                if(resp.data.status!=200){this.$toast(resp.data.msg);}
                else{
                    this.$toast("保存成功");
                }
            }).catch(err=>{
                this.showLoading=false;
                console.log(err);
                this.$toast("服务器异常");
            })   
        },
        
    afterRead(files) {
      // 此时可以自行将文件上传至服务器
      console.log(files);
      if (files.length == undefined) files = [files];
      for (var i = 0; i < files.length; i++) this.fileList.pop();
      for (var file of files) {
        var needle = "base64,";
        var start = file.content.indexOf(needle) + needle.length;
            this.showLoading=true;
        this.postRequest("/chargecode/addwechat", {
            amount:0,
          base64Data: file.content.substr(start)
        })
          .then(resp => {
            this.showLoading=false;
            if(resp.data.status!=200){
                this.$toast(resp.data.msg);
            }else{
            this.fileList.push({
              url: "/api/chargecode/getwechat?amount=0&name.jpeg"
            });
            }
          })
          .catch(err => {
            this.showLoading=false;
            console.log(err);
            this.$toast("服务器异常");
          });
      }
    },
    onDelete(file) {
      var id = file.url.split("=")[1].split("&")[0] - "0";
        this.showLoading=true;
        this.postRequest("/chargecode/delete", {amount: 0, type: 0})
        .then(resp=>{
            this.showLoading=false;
            this.$toast(resp.data.msg);
        }).catch(err=>{
            console.log(err);
            this.showLoading=false;
            this.$toast('服务器异常');
        })
    },
    }
}
</script>
<template>
  <div>
    <van-sticky>
    <van-nav-bar title="地址编辑" left-text="" left-arrow @click-left="onClickLeft" style="background-color: rgb(240, 240,240);"/>
    </van-sticky>
    <van-address-edit
      :area-list="areaList"
      :address-info="addressInfo"
      show-delete
      show-set-default
      @save="onSave"
      @delete="onDelete"
    />
  </div>
</template>
<script>
import { address } from "../../static/area.js";
export default {
  data() {
    return {
        mode: "add",
      areaList: address,
      searchResult: [],
      addressInfo: {
        id: -1,
        name: "",
        tel: "",
        //province: "浙",
        //city: "绍兴",
        //county: "诸暨",
        addressDetail: "",
        areaCode: "",
        isDefault: false
      }
    };
  },
  mounted() {
    console.log("params");
    console.log(this.$route.params);
    if (this.$route.params != undefined &&this.$route.params.addrInfo){
        this.mode = "update";
        this.addressInfo = this.$route.params.addrInfo;
    }
  },

  methods: {
    onClickLeft() {
      this.$router.back(-1);
    },
    onSave(content) {
      content["uid"] = this.$store.state.user.id;
      content["mode"] = this.mode;
      console.log(content);
      this.$toast.loading({duration:0, forbidClick:true, message:'加载中...'});
      this.postRequest("/address/add", content).then(resp => {
        this.$toast.clear();
        var data = resp.data;
        this.$toast(data.msg);
        this.$router.back(-1);
      });
    },
    onDelete(content) {
      this.$toast.loading({duration:0, forbidClick:true, message:'加载中...'});
      this.postRequest("/address/delete", content).then(resp => {
        this.$toast.clear();
        if (this.$store.state.address.id == content.id){
          this.$store.commit('setAddress', {});
        }
        var data = resp.data;
        this.$toast(data.msg);
        this.$router.back(-1);
      });
    }
  }
};
</script>
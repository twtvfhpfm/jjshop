<template>
  <div>
    <van-sticky>
    <van-nav-bar title="地址管理" left-text="" left-arrow @click-left="onClickLeft" style="background-color: rgb(240, 240,240);"/>
    </van-sticky>
    <van-address-list
      v-model="chosenAddressId"
      :list="list"
      @add="onAdd"
      @edit="onEdit"
      @select="onSelect"
    />
  </div>
</template>
<script>
export default {
  data() {
    return {
      chosenAddressId: 0,
      list: [],
      disabledList: []
    };
  },
  mounted(){
      var uid = this.$store.state.user.id;
      this.$toast.loading({duration:0, forbidClick:true, message:'加载中...'});
      this.postRequest('/address/list', {
          uid
      }).then(resp => {
        this.$toast.clear();
          var data = resp.data;
          if (data.status == 200){
              for(var addr of data.obj){
                  this.list.push({
                      id: addr.id, 
                  name: addr.name, 
                  tel: addr.mobile, 
                  areaCode: addr.areaCode,
                  addressDetail: addr.addressDetail,
                  address: addr.province+addr.city+addr.county+addr.addressDetail,
                  isDefault: addr.default
                  });
              }
          }else{
              this.$toast(data.msg);
          }
      });
  },
  methods: {
    onClickLeft() {
      this.$router.back(-1);
    },
    onAdd() {
      this.$router.push({ path: "location_edit" });
    },

    onEdit(item, index) {
      this.$router.push({
          name: "编辑收货地址",
          params: {addrInfo: this.list[index]}
          });
    },
    onSelect(item, index) {
      console.log(item);
      this.$store.commit("setAddress", item);
      this.$router.back(-1);
    }
  }
};
</script>
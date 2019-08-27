<template>
  <div>
    <van-sticky>
    <van-nav-bar title="注册" left-text="" left-arrow @click-left="onClickLeft" style="background-color: rgb(240, 240,240);"/>
    </van-sticky>
    <van-popup v-model="showLoading" :overlay="false" :close-on-click-overlay="false">
        <van-button loading type="primary" loading-type="spinner" style="background-color: grey; border-color: grey;"/>
    </van-popup>
    <van-cell-group>
      <van-field
        v-model="username"
        clearable
        left-icon="contact"
        label="用户名"
        placeholder="请输入用户名"
      />

      <van-field
        v-model="password"
        type="password"
        left-icon="closed-eye"
        label="密码"
        placeholder="请输入密码"
      />
      <van-field
        v-model="password2"
        type="password"
        left-icon="closed-eye"
        label="再次输入密码"
        placeholder="请再次输入密码"
      />
      <div style="margin: 10px 10px;">
      <van-button type="info" size="large" @click="submitClick">注册</van-button>
      </div>
    </van-cell-group>
  </div>
</template>
<script>
export default {
  data() {
    return {
      showLoading: false,
      username: "",
      password: "",
      password2: ""
    };
  },
  methods: {
    onClickLeft() {
      this.$router.back(-1);
    },
    submitClick: function() {
      var _this = this;
      if (this.username == "") {
        this.$toast("用户名不能为空");
        return false;
      }
      if (this.password == "") {
        this.$toast("密码不能为空");
        return false;
      }
      if (this.password != this.password2) {
        this.$toast("密码不一致");
        return false;
      }
      this.showLoading=true;
      this.postRequest("/user/register", {
        username: this.username,
        password: this.password
      }).then(resp => {
        this.showLoading=false;
        console.log(resp);
        if(resp.data.status!=200) {this.$toast(resp.data.msg);}
        else{
          var data = resp.data;
          this.$toast(data.msg);
          if (data.status == 200) this.$router.back(-1);
        }
      }).catch(err=>{
        this.showLoading=false;
        console.log(err);
        this.$toast("服务器异常");
      })
    }
  }
};
</script>
<style>
.login-container {
  border-radius: 15px;
  background-clip: padding-box;
  margin: 180px auto;
  width: 350px;
  padding: 35px 35px 15px 35px;
  background: #fff;
  border: 1px solid #eaeaea;
  box-shadow: 0 0 25px #cac6c6;
}
.login_title {
  margin: 0px auto 40px auto;
  text-align: center;
  color: #505458;
}
.login_remember {
  margin: 0px 0px 35px 0px;
  text-align: left;
}
</style>

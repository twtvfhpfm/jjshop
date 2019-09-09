<template>
  <div>
    <van-sticky>
    <van-nav-bar
      title="登录"
      left-text=""
      right-text="注册"
      left-arrow
      @click-left="onClickLeft"
      @click-right="onClickRight"
      style="background-color: rgb(240, 240,240);"
    />
    </van-sticky>
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
      <div style="margin: 10px 10px;">
      <van-button type="info" size="large" @click="submitClick">登录</van-button>
      </div>
    </van-cell-group>
  </div>
</template>
<script>
export default {
  data() {
    return {
      username: "",
      password: "",
    };
  },
  mounted(){
      this.username=this.$store.state.loginHistory.username;
      this.password=this.$store.state.loginHistory.password;
  },
  methods: {
    onClickLeft() {
      this.$router.back(-1);
    },
    onClickRight() {
      this.$router.push({ path: "/register" });
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
      this.$toast.loading({duration:0, forbidClick:true, message:'加载中...'});
      this.postRequest("/user/login", {
        username: this.username,
        password: this.password
      }).then(resp => {
        this.$toast.clear();
        if(resp.data.status!=200) {this.$toast(resp.data.msg);}
        else {
          var data = resp.data;
          _this.$store.commit("login", data.obj);
          _this.$store.commit("setLoginHistory", {username: this.username, password: this.password});
          _this.$router.back(-1);
        }
      }).catch(err => {
        this.$toast.clear();
        console.log(err);
        this.$toast("服务器异常");
      });
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

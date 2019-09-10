<template>
<div>
  <van-sticky>
    <van-nav-bar title="优惠券管理" left-text="" left-arrow @click-left="$router.back(-1);" style="background-color: rgb(240, 240,240);"/>
    </van-sticky>
      <van-cell title="奖品设置" is-link to="/prizemanager" />
      <van-cell title="发放优惠券" is-link to="/couponoffer" />
</div>
</template>
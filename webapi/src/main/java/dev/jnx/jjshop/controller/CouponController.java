package dev.jnx.jjshop.controller;

import dev.jnx.jjshop.bean.RespBean;
import dev.jnx.jjshop.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.session.RedisSessionProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/coupon")
public class CouponController {
    @Autowired private CouponService couponService;

    @RequestMapping("/offer")
    public RespBean offer(@RequestParam Map<String, Object> params){
        return couponService.offer(params);
    }

    @RequestMapping("/use")
    public RespBean use(int id, String orderId, int currentUid){
        return couponService.use(id, orderId, currentUid);
    }
    @RequestMapping("/unuse")
    public RespBean unUse(int id, String orderId, int currentUid){
        return couponService.unUse(id, orderId, currentUid);
    }

    @RequestMapping("/get")
    public RespBean get(int id, int currentUid, int goodsId, String orderId){
        return couponService.get(id, currentUid, goodsId, orderId);
    }
}

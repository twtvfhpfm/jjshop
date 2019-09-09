package dev.jnx.jjshop.service;

import dev.jnx.jjshop.bean.RespBean;
import dev.jnx.jjshop.dao.CouponDao;
import dev.jnx.jjshop.dao.GoodsDao;
import dev.jnx.jjshop.model.Coupon;
import dev.jnx.jjshop.model.GoodsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CouponService {
    @Autowired private CouponDao couponDao;
    @Autowired private GoodsDao goodsDao;

    public RespBean offer(Map<String, Object> params){
        int ret = couponDao.offer(params);
        if (ret <= 0){
            return RespBean.error("优惠券发放失败");
        }else{
            return RespBean.ok("发放成功");
        }
    }

    public RespBean use(int id, String orderId, int uid){
        int ret = couponDao.use(id, orderId, uid);
        if (ret != 1){
            return RespBean.error("优惠券使用失败");
        }else{
            return RespBean.ok("使用成功");
        }
    }
    public RespBean unUse(int id, String orderId, int uid){
        int ret = couponDao.unUse(id, orderId, uid);
        if (ret != 1){
            return RespBean.error("优惠券找回失败");
        }else{
            return RespBean.ok("找回成功");
        }
    }
    public RespBean get(int id, int uid, int goodsId, String orderId){
        List<Map<String, Object>> list = couponDao.get(id, uid, goodsId, orderId);
        list.forEach(i -> {
            Object a = i.get("goodsId");
            Object t = i.get("startTime");
            if (i.get("goodsId").equals(0)){
                i.put("goodsTitle", "全品类");
            }else{
                GoodsModel model = goodsDao.get((int)i.get("goodsId"));
                i.put("goodsTitle", model.getTitle());
            }
        });
        return RespBean.ok("获取成功", list);
    }
}

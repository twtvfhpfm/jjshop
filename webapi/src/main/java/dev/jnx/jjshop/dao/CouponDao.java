package dev.jnx.jjshop.dao;

import dev.jnx.jjshop.model.Coupon;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CouponDao {
    int offer(Map<String, Object> params);
    int use(@Param("id") int id, @Param("orderId") String orderId, @Param("uid") int uid);
    int unUse(@Param("id") int id, @Param("orderId") String orderId, @Param("uid") int uid);
    List<Map<String, Object>> get(@Param("id") int id,
                     @Param("uid") int uid,
                     @Param("goodsId") int goodsId,
                     @Param("orderId") String orderId);
    Coupon getBean(@Param("id") int id,
                   @Param("uid") int uid);
}

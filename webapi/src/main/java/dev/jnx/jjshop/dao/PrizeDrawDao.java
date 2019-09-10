package dev.jnx.jjshop.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PrizeDrawDao {
    int add(@Param("uid") int uid, @Param("orderId") String orderId);
    int use(@Param("couponId") int couponId, @Param("uid") int uid);
    int count(int uid);
}

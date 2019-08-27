package dev.jnx.jjshop.dao;

import dev.jnx.jjshop.model.GoodsOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface GoodsOrderDao {
    int add(GoodsOrder g);
    int delete(@Param("orderId") String orderId, @Param("uid") int uid);
    int update(@Param("orderId") String orderId, @Param("status") int status);
    int confirmReceipt(@Param("orderId") String orderId, @Param("uid") int uid);
    GoodsOrder get(String orderId);
    List<GoodsOrder> getByUID(@Param("uid") int uid, @Param("lastMinId")int lastMinId);
    List<GoodsOrder> list(@Param("lastMinId") int lastMinId, @Param("text") String text);
}

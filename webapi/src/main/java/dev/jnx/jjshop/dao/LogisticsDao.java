package dev.jnx.jjshop.dao;

import dev.jnx.jjshop.model.Logistics;
import dev.jnx.jjshop.model.LogisticsModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LogisticsDao {
    int add(@Param("orderId") String orderId,
            @Param("customerName") String customerName,
            @Param("shipperCode") String shipperCode,
            @Param("logisticCode") String logisticCode);
    int update(@Param("orderId") String orderId,
            @Param("customerName") String customerName,
            @Param("shipperCode") String shipperCode,
            @Param("logisticCode") String logisticCode);
    LogisticsModel get(String orderId);
}

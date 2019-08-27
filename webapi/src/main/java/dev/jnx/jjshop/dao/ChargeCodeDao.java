package dev.jnx.jjshop.dao;

import dev.jnx.jjshop.model.ChargeCode;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ChargeCodeDao {
    int add(ChargeCode c);
    ChargeCode get(@Param("amount") BigDecimal amount, @Param("type") int type);
    int delete(@Param("amount") BigDecimal amount, @Param("type") int type);
    List<ChargeCode> list();
}

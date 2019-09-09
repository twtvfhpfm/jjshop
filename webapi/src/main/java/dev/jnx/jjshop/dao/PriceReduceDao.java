package dev.jnx.jjshop.dao;

import dev.jnx.jjshop.model.PriceReduce;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface PriceReduceDao {
    int add(Map<String, Object> params);
    int update(Map<String, Object> params);
    List<PriceReduce> getByGoodsId(int goodsId);
    int delete(int id);
}

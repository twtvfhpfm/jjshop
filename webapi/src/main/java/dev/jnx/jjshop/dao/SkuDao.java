package dev.jnx.jjshop.dao;

import dev.jnx.jjshop.model.Sku;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkuDao {
    Sku get(int id);
    List<Sku> getByGoodsId(int goodsId);
    void delete(int id);
    int update(Sku sku);
    int add(Sku sku);
}

package dev.jnx.jjshop.dao;

import dev.jnx.jjshop.model.SkuList;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkuListDao {
    int add(SkuList s);
    int delete(int id);
    int update(SkuList s);
    SkuList get(int id);
    List<SkuList> getByGoodsId(int goodsId);
}

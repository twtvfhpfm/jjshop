package dev.jnx.jjshop.dao;

import dev.jnx.jjshop.model.Goods;
import dev.jnx.jjshop.model.GoodsModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface GoodsDao {
    GoodsModel get(int id);
    List<Goods> getByCategory(@Param("categoryId") int categoryId, @Param("lastMinId") int lastMinId);
    List<Goods> getByLike(@Param("text") String text, @Param("lastMinId") int lastMinId);
    int add(Goods goods);
    int incrSales(@Param("id") int id, @Param("num") int num);
    void delete(int id);
    int update(Goods goods);
    int getCountInCategory(int categoryId);
    List<Map<String, Object>> listAll();
}

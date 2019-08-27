package dev.jnx.jjshop.dao;

import dev.jnx.jjshop.model.Cart;
import dev.jnx.jjshop.model.CartModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartDao {
    Cart get(int id);
    int delete(@Param("id") int id, @Param("uid") int uid);
    int update(Cart c);
    int add(Cart c);
    int setOrdered(int id);
    List<CartModel> getByUid(int uid);
    int changeNum(@Param("delta") int delta, @Param("id") int id, @Param("uid") int uid);
}

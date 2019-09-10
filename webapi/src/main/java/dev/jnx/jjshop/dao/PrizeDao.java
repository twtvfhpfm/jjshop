package dev.jnx.jjshop.dao;

import dev.jnx.jjshop.model.Prize;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrizeDao {
    List<Prize> list();
    int update(Prize prize);
    int add(Prize prize);
    int delete(int id);
}

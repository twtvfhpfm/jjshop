package dev.jnx.jjshop.dao;

import dev.jnx.jjshop.model.Address;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressDao {
    Address getDefault(int uid);
    int setDefault(@Param("id") int id, @Param("uid") int uid);
    List<Address> list(int uid);
    int add(Address addr);
    int delete(@Param("id") int id, @Param("uid") int uid);
    int update(Address addr);
    Address get(int id);
}

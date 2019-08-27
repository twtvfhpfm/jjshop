package dev.jnx.jjshop.dao;

import dev.jnx.jjshop.model.User;
import dev.jnx.jjshop.model.UserModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {
    UserModel getByUsername(String username);

    int add(@Param("username") String username, @Param("password") String password);
    int update(User user);
    List<User> list(@Param("lastMinId") int lastMinId, @Param("text") String text);
}

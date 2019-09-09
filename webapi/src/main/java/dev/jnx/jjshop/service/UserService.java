package dev.jnx.jjshop.service;

import dev.jnx.jjshop.bean.RespBean;
import dev.jnx.jjshop.dao.UserDao;
import dev.jnx.jjshop.model.User;
import dev.jnx.jjshop.model.UserModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;
    @Autowired private RedisTemplate redisTemplate;
    Logger logger = Logger.getLogger(this.getClass());

    public RespBean login(String username, String password){
        String pwdEnc = DigestUtils.md5DigestAsHex(password.getBytes());
        UserModel user = userDao.getByUsername(username);
        if (user == null || !user.getPassword().equals(pwdEnc)){
            return RespBean.error("账户名或者密码输入错误!");
        }
        String token = user.getUsername() + user.getId() + "@" + System.currentTimeMillis();
        try {
            String tokenMd5 = DigestUtils.md5DigestAsHex(token.getBytes("utf-8"));
            redisTemplate.opsForValue().set(tokenMd5, user, 7, TimeUnit.DAYS);
            user.setToken(tokenMd5);
        }catch(UnsupportedEncodingException ex){
            return RespBean.error("token生成失败");
        }
        return RespBean.ok("登录成功", user);
    }

    public RespBean logout(String token){
        if (token != null && !token.isEmpty()){
            redisTemplate.expireAt(token, new Date());
        }
        return RespBean.ok("登出成功");
    }

    public RespBean register(String username, String password){
        if (userDao.getByUsername(username) != null){
            return RespBean.error("该昵称已被注册");
        }
        String pwdEnc = DigestUtils.md5DigestAsHex(password.getBytes());
        int ret = userDao.add(username, pwdEnc);
        return (ret == 1 ? RespBean.ok("注册成功") : RespBean.error("注册失败"));
    }

    public RespBean update(int id, int role){
        User user = new User();
        user.setId(id);
        user.setRole(role);
        int ret = userDao.update(user);
        return (ret == 1 ? RespBean.ok("修改成功") : RespBean.error("修改失败"));
    }

    public RespBean list(int lastMinId, String text){
        List<User> ul = userDao.list(lastMinId, text);
        String msg;
        if (ul.size()< 20){
            msg = "end";
        }else msg="continue";

        return RespBean.ok(msg,ul);
    }

    public RespBean listAll(){
        List<Map<String, Object>> ul = userDao.listAll();
        return RespBean.ok("获取成功",ul);
    }
}

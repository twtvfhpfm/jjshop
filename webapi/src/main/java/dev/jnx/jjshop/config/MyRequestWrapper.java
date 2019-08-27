package dev.jnx.jjshop.config;

import dev.jnx.jjshop.model.UserModel;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Enumeration;
import java.util.Map;

public class MyRequestWrapper extends HttpServletRequestWrapper {
    private UserModel user;
    public MyRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    public MyRequestWrapper(HttpServletRequest request, UserModel user){
        super(request);
        this.user = user;
    }

    @Override
    public String getParameter(String name){
        return super.getParameter(name);
    }

    @Override
    public Map<String, String[]> getParameterMap(){
        return super.getParameterMap();
    }

    @Override
    public Enumeration<String> getParameterNames(){
        return super.getParameterNames();
    }

    @Override
    public String[] getParameterValues(String name){
        if (name.equals("uid")){
            int uid = -1;
            if(user!=null){
                uid = user.getId();
            }
            return new String[]{String.valueOf(uid)};
        }
        return super.getParameterValues(name);
    }
}

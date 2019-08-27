package dev.jnx.jjshop.config;

import com.alibaba.fastjson.JSON;
import dev.jnx.jjshop.bean.RespBean;
import dev.jnx.jjshop.model.User;
import dev.jnx.jjshop.model.UserModel;
import dev.jnx.jjshop.util.NetUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.regex.Pattern;

@Component
@WebFilter(urlPatterns = "/**")
public class AuthFilter implements Filter {
    Logger logger = Logger.getLogger(this.getClass());
    //管理员才能访问的接口
    private static final String[] authAdmin= {"/category/add", "/category/delete",
            "/chargecode/addwechat", "/chargecode/addalipay", "/chargecode/list", "/chargecode/delete",
            "/goods/update", "/goods/delete",
            "/goodsorder/updatestatus", "/goodsorder/list",
            "/picture/add", "/picture/delete",
            "/sku/addorupdate", "/sku/add", "/sku/update", "/sku/delete",
            "/skulist/addorupdate", "/skulist/add", "/skulist/update", "/skulist/delete",
            "/user/update", "/user/list",
            "/logistics/add", "/logistics/update", "/logistics/getshippercode", "/logistics/get",
            "/bill/.*"};
    //普通用户登录才能访问的接口
    private static final String[] authUser = {"/address/.*", "/cart/.*", "/goodsorder/.*",
            "/logistics/getlogistic"};
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)servletRequest;
        HttpServletResponse resp = (HttpServletResponse)servletResponse;
        //record log
        Map<String, String[]> map = req.getParameterMap();

        StringBuilder sb = new StringBuilder();
        sb.append(NetUtil.getRemoteIp(req)).append(" - ");
        sb.append(req.getRequestURI()).append(": ");
        if(!req.getRequestURI().equals("/picture/add"))
        for(Map.Entry entry: map.entrySet()){
            sb.append(entry.getKey()).append("=");
            for(String s: (String[])entry.getValue()){
                sb.append(s).append(",");
            }
        }
        logger.info(sb.toString());

        UserModel user = null;
        String token = req.getHeader("token");
        if(token != null && !token.isEmpty()){
            user = (UserModel) redisTemplate.opsForValue().get(token);
        }
        String url = req.getRequestURI();
        for(String regex: authAdmin){
            if (Pattern.matches(regex, url)){
                if (token == null || token.isEmpty()){
                    response(resp, RespBean.build().setStatus(401).setMsg("请先登录"));
                    return;
                }
                if (user == null){
                    response(resp, RespBean.build().setStatus(401).setMsg("登录过期，请重新登录"));
                    return;
                }
                if (user.getRole() == 0){
                    response(resp, RespBean.build().setStatus(403).setMsg("没有访问权限"));
                    return;
                }
                break;
            }
        }
        for(String regex: authUser){
            if (Pattern.matches(regex, url)){
                if (token == null || token.isEmpty()){
                    response(resp, RespBean.build().setStatus(401).setMsg("请先登录"));
                    return;
                }
                if (user == null){
                    response(resp, RespBean.build().setStatus(401).setMsg("登录过期，请重新登录"));
                    return;
                }
                break;
            }
        }
        filterChain.doFilter(new MyRequestWrapper(req, user), resp);
    }

    private void response(HttpServletResponse resp, RespBean bean){
        resp.setContentType("application/json;charset=utf-8");
        resp.setStatus(200);
        try(PrintWriter out = resp.getWriter()){
            out.write(JSON.toJSONString(bean));
        }catch(IOException ex){
            logger.error("can't get writer: " + ex.getMessage());
        }
    }
}

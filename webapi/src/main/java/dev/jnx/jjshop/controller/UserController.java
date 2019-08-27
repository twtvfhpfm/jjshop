package dev.jnx.jjshop.controller;

import dev.jnx.jjshop.bean.RespBean;
import dev.jnx.jjshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired private UserService userService;
    @PostMapping("/login")
    public RespBean login(String username, String password){
        return userService.login(username, password);
    }
    @RequestMapping("/logout")
    public RespBean logout(HttpServletRequest request){
        return userService.logout(request.getHeader("token"));
    }
    @RequestMapping("/register")
    public RespBean register(String username, String password){
        return userService.register(username, password);
    }
    @RequestMapping("/update")
    public RespBean update(int id, int role){
        return userService.update(id, role);
    }
    @RequestMapping("/list")
    public RespBean list(int lastMinId, String text){
        if (lastMinId == 0) lastMinId = Integer.MAX_VALUE;
        return userService.list(lastMinId, text);
    }

}

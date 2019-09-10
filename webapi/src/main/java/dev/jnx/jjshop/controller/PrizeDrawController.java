package dev.jnx.jjshop.controller;

import dev.jnx.jjshop.bean.RespBean;
import dev.jnx.jjshop.service.PrizeDrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/prizedraw")
public class PrizeDrawController {
    @Autowired private PrizeDrawService prizeDrawService;

    @RequestMapping("/draw")
    public RespBean draw(int currentUid){
        return prizeDrawService.draw(currentUid);
    }

    @RequestMapping("/count")
    public RespBean count(int currentUid){
        return prizeDrawService.count(currentUid);
    }
}

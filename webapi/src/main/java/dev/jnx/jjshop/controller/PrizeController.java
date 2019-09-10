package dev.jnx.jjshop.controller;

import dev.jnx.jjshop.bean.RespBean;
import dev.jnx.jjshop.model.Prize;
import dev.jnx.jjshop.service.PrizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/prize")
public class PrizeController {
    @Autowired private PrizeService prizeService;

    @RequestMapping("/list")
    public RespBean list(){
        return prizeService.list();
    }

    @RequestMapping("/addorupdate")
    public RespBean addOrUpdate(@RequestBody List<Prize> prizeList){
        for(Prize prize: prizeList){
            if (prize.getId() == 0){
                prizeService.add(prize);
            }else{
                prizeService.update(prize);
            }
        }
        return RespBean.ok("修改成功");
    }

    @RequestMapping("/add")
    public RespBean add(@RequestBody Prize prize){
        return prizeService.add(prize);
    }

    @RequestMapping("/update")
    public RespBean update(@RequestBody Prize prize){
        return prizeService.update(prize);
    }

    @RequestMapping("/delete")
    public RespBean delete(int id){
        return prizeService.delete(id);
    }
}

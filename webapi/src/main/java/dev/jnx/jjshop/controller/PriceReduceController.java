package dev.jnx.jjshop.controller;

import dev.jnx.jjshop.bean.RespBean;
import dev.jnx.jjshop.service.PriceReduceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/pricereduce")
@RestController
public class PriceReduceController {
    @Autowired private PriceReduceService priceReduceService;

    @RequestMapping("/add")
    public RespBean add(@RequestParam Map<String, Object> params){
        return priceReduceService.add(params);
    }

    @RequestMapping("/update")
    public RespBean update(@RequestParam Map<String, Object> params){
        return priceReduceService.update(params);
    }

    @RequestMapping("/getbygoodsid")
    public RespBean getByGoodsId(int[] goodsId)
    {
        return priceReduceService.getByGoodsId(goodsId);
    }

    @RequestMapping("/delete")
    public RespBean delete(int id)
    {
        return priceReduceService.delete(id);
    }
}

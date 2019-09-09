package dev.jnx.jjshop.controller;

import dev.jnx.jjshop.bean.RespBean;
import dev.jnx.jjshop.model.Goods;
import dev.jnx.jjshop.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.session.RedisSessionProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Autowired private GoodsService goodsService;

    @RequestMapping("/get")
    public RespBean get(int id){
        return goodsService.get(id);
    }

    @RequestMapping("/getbytext")
    public RespBean getByLike(String text, int lastMinId){
        if (lastMinId == 0) lastMinId = Integer.MAX_VALUE;
        return goodsService.getByLike(text, lastMinId);
    }

    @RequestMapping("/getbycategory")
    public RespBean getByCategory(int categoryId, int lastMinId){
        if (lastMinId == 0) lastMinId = Integer.MAX_VALUE;
        return goodsService.getByCategory(categoryId, lastMinId);
    }

    @RequestMapping("/getbyrandom")
    public RespBean getByRandom() {
        return goodsService.getByRandom();
    }

    @RequestMapping("/add")
    public RespBean add(int categoryId, BigDecimal price, BigDecimal superiorPrice,
                        int remain, String title, String description, String thumb,
                        int sales, BigDecimal transportFee){
        Goods g = Goods.of(categoryId, price, superiorPrice, remain, title,
                description, thumb, 0, sales, transportFee);
        return goodsService.add(g);
    }

    @RequestMapping("/incrsales")
    public RespBean incrSales(int id, int num){
        return goodsService.incrSales(id, num);
    }

    @RequestMapping("/update")
    public RespBean update(int categoryId, BigDecimal price, BigDecimal superiorPrice,
                        int remain, String title, String description, String thumb, int id,
                           int sales, BigDecimal transportFee){
        Goods g = Goods.of(categoryId, price, superiorPrice, remain, title,
                description, thumb, id, sales, transportFee);
        return goodsService.update(g);
    }

    @RequestMapping("/delete")
    public RespBean delete(int id){
        return goodsService.delete(id);
    }

    @RequestMapping("/getcountincategory")
    public RespBean getCountInCategory(int categoryId){
        return goodsService.getCountInCategory(categoryId);
    }

    @RequestMapping("/listall")
    public RespBean listAll(){
        return goodsService.listAll();
    }
}

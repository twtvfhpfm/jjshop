package dev.jnx.jjshop.controller;

import dev.jnx.jjshop.bean.RespBean;
import dev.jnx.jjshop.model.Sku;
import dev.jnx.jjshop.model.SkuList;
import dev.jnx.jjshop.service.SkuListService;
import dev.jnx.jjshop.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/skulist")
public class SkuListController {
    @Autowired
    private SkuListService skuService;

    @RequestMapping("/addorupdate")
    public RespBean addOrUpdate(@RequestBody List<SkuList> skuList){
        for(SkuList item: skuList){
            if(item.getId()==0)
                skuService.add(item);
            else skuService.update(item);
        }
        return RespBean.ok("修改成功");
    }

    @RequestMapping("/add")
    public RespBean add(int goodsId, BigDecimal price, BigDecimal superiorPrice, int s1Id, int s2Id, int s3Id, int stockNum){
        SkuList sku = SkuList.of(0, goodsId, price, superiorPrice, s1Id, s2Id, s3Id, stockNum);
        return skuService.add(sku);
    }

    @RequestMapping("/delete")
    public RespBean delete(int id){
        return skuService.delete(id);
    }

    @RequestMapping("/update")
    public RespBean update(int goodsId, BigDecimal price, BigDecimal superiorPrice, int s1Id, int s2Id, int s3Id, int stockNum, int id){
        SkuList sku = SkuList.of(id, goodsId, price, superiorPrice, s1Id, s2Id, s3Id, stockNum);
        return skuService.update(sku);
    }

    @RequestMapping("/get")
    public RespBean get(int id){
        return skuService.get(id);
    }

    @RequestMapping("/getbygoodsid")
    public RespBean getByGoodsId(int goodsId){
        return skuService.getByGoodsId(goodsId);
    }
}

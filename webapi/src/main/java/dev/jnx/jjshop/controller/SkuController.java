package dev.jnx.jjshop.controller;

import dev.jnx.jjshop.bean.RespBean;
import dev.jnx.jjshop.model.Sku;
import dev.jnx.jjshop.service.SkuService;
import javafx.beans.property.ReadOnlySetProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sku")
public class SkuController {
    @Autowired private SkuService skuService;

    @RequestMapping("/addorupdate")
    public RespBean addOrUpdate(@RequestBody List<Sku> skuList){
        for(Sku sku: skuList){
            if (sku.getId()==0){
                skuService.add(sku);
            }else{
                skuService.update(sku);
            }
        }
        return RespBean.ok("修改成功");
    }

    @RequestMapping("/add")
    public RespBean add(int goodsId, int imgId, String keyName, String valueName, int skuOrder){
        Sku sku = Sku.of(goodsId, imgId, keyName, valueName, skuOrder, 0);
        return skuService.add(sku);
    }

    @RequestMapping("/delete")
    public RespBean delete(int id){
        return skuService.delete(id);
    }

    @RequestMapping("/update")
    public RespBean update(int goodsId, int imgId, String keyName, String valueName, int skuOrder, int id){
        Sku sku = Sku.of(goodsId, imgId, keyName, valueName, skuOrder, id);
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

package dev.jnx.jjshop.service;

import dev.jnx.jjshop.bean.RespBean;
import dev.jnx.jjshop.dao.SkuDao;
import dev.jnx.jjshop.model.Sku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.java2d.pipe.RegionSpanIterator;

import java.util.List;

@Service
public class SkuService {
    @Autowired private SkuDao skuDao;

    public RespBean get(int id){
        Sku sku = skuDao.get(id);
        if (sku == null){
            return RespBean.error("sku不存在");
        }
        else{
            return RespBean.ok("获取成功", sku);
        }
    }

    public RespBean add(Sku sku){
        int ret = skuDao.add(sku);
        if (ret == 1){
            return RespBean.ok("添加成功", sku.getId());
        }
        else{
            return RespBean.error("添加sku失败");
        }
    }

    public RespBean delete(int id){
        skuDao.delete(id);
        return RespBean.ok("删除成功");
    }

    public RespBean update(Sku sku){
        int ret = skuDao.update(sku);
        if (ret == 1){
            return RespBean.ok("修改成功");
        }
        else{
            return RespBean.error("修改sku失败");
        }
    }

    public RespBean getByGoodsId(int goodsId){
        List<Sku> sl = skuDao.getByGoodsId(goodsId);
        return RespBean.ok("获取成功", sl);
    }
}

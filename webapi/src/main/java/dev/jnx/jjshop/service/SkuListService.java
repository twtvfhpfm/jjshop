package dev.jnx.jjshop.service;

import dev.jnx.jjshop.bean.RespBean;
import dev.jnx.jjshop.dao.SkuDao;
import dev.jnx.jjshop.dao.SkuListDao;
import dev.jnx.jjshop.model.Sku;
import dev.jnx.jjshop.model.SkuList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkuListService {
    @Autowired
    private SkuListDao skuDao;

    public RespBean get(int id){
        SkuList sku = skuDao.get(id);
        if (sku == null){
            return RespBean.error("sku不存在");
        }
        else{
            return RespBean.ok("获取成功", sku);
        }
    }

    public RespBean add(SkuList sku){
        int ret = skuDao.add(sku);
        if (ret == 1){
            return RespBean.ok("添加成功", sku.getId());
        }
        else{
            return RespBean.error("添加sku失败");
        }
    }

    public RespBean delete(int id){
        int ret = skuDao.delete(id);
        if (ret == 1){
            return RespBean.ok("删除成功");
        }
        else{
            return RespBean.error("删除sku失败");
        }
    }

    public RespBean update(SkuList sku){
        int ret = skuDao.update(sku);
        if (ret == 1){
            return RespBean.ok("修改成功");
        }
        else{
            return RespBean.error("修改sku失败");
        }
    }

    public RespBean getByGoodsId(int goodsId){
        List<SkuList> sl = skuDao.getByGoodsId(goodsId);
        return RespBean.ok("获取成功", sl);
    }
}

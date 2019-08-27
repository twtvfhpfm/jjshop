package dev.jnx.jjshop.service;

import dev.jnx.jjshop.bean.RespBean;
import dev.jnx.jjshop.dao.GoodsDao;
import dev.jnx.jjshop.dao.SkuDao;
import dev.jnx.jjshop.dao.SkuListDao;
import dev.jnx.jjshop.model.Goods;
import dev.jnx.jjshop.model.GoodsModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class GoodsService {
    @Autowired private GoodsDao goodsDao;
    @Autowired private SkuDao skuDao;
    @Autowired private SkuListDao skuListDao;
    Logger logger = Logger.getLogger(this.getClass());

    private void fillSku(GoodsModel model){
        model.setSku(skuDao.getByGoodsId(model.getId()));
        model.setSkuList(skuListDao.getByGoodsId(model.getId()));
    }

    public RespBean getByRandom(){
        List<Goods> gl = goodsDao.getByLike("", Integer.MAX_VALUE);
        while(gl.size() > 5){
            gl.remove(new Random().nextInt(gl.size()));
        }
        return RespBean.ok("获取成功", gl);
    }

    public RespBean getByLike(String text, int lastMinId){
        List<Goods> gl = goodsDao.getByLike(text, lastMinId);
        String msg;
        if (gl.size() < 20){
            msg = "end";
        }else{
            msg = "continue";
        }
        return RespBean.ok(msg, gl);
    }

    public RespBean getByCategory(int cagetoryId, int lastMinId){
        List<Goods> gl = goodsDao.getByCategory(cagetoryId, lastMinId);
        String msg;
        if (gl.size() < 20){
            msg = "end";
        }else{
            msg = "continue";
        }
        return RespBean.ok(msg, gl);
    }

    public RespBean get(int id){
        GoodsModel g = goodsDao.get(id);
        if (g == null){
            return RespBean.error("商品不存在");
        }else {
            fillSku(g);
            return RespBean.ok("获取成功", g);
        }
    }

    public RespBean add(Goods g){
        int ret = goodsDao.add(g);
        if (ret == 1){
            return RespBean.ok("添加成功", g.getId());
        }else{
            return RespBean.error("商品添加失败");
        }
    }

    public RespBean incrSales(int id, int num){
        int ret = goodsDao.incrSales(id, num);
        if (ret == 1){
            return RespBean.ok("更新成功");
        }else{
            return RespBean.error("销量更新失败");
        }
    }

    public RespBean delete(int id){
        goodsDao.delete(id);
        return RespBean.ok("商品删除成功");
    }

    public RespBean update(Goods g){
        int ret = goodsDao.update(g);

        if (ret == 1){
            return RespBean.ok("修改成功");
        }else{
            return RespBean.error("商品修改失败");
        }
    }

    public RespBean getCountInCategory(int categoryId){
        int count = goodsDao.getCountInCategory(categoryId);
        return RespBean.ok("获取成功", count);
    }
}

package dev.jnx.jjshop.service;

import dev.jnx.jjshop.bean.RespBean;
import dev.jnx.jjshop.dao.PriceReduceDao;
import dev.jnx.jjshop.model.PriceReduce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PriceReduceService{
    @Autowired private PriceReduceDao priceReduceDao;

    public RespBean add(Map<String, Object> params) {
        int ret = priceReduceDao.add(params);
        if (ret != 1){
            return RespBean.error("减免添加失败");
        }
        return RespBean.ok("添加成功");
    }

    public RespBean update(Map<String, Object> params) {
        int ret = priceReduceDao.update(params);
        if (ret != 1){
            return RespBean.error("减免更新失败");
        }
        return RespBean.ok("更新成功");
    }

    public RespBean getByGoodsId(int[] goodsId) {
        List<PriceReduce> result = new ArrayList<>();
        for(int id: goodsId) {
            List<PriceReduce> list = priceReduceDao.getByGoodsId(id);
            result.addAll(list);
        }
        return RespBean.ok("获取成功", result);
    }

    public RespBean delete(int id){
        int ret = priceReduceDao.delete(id);
        if (ret != 1){
            return RespBean.error("减免删除失败");
        }
        return RespBean.ok("删除成功");
    }
}

package dev.jnx.jjshop.service;

import dev.jnx.jjshop.bean.RespBean;
import dev.jnx.jjshop.dao.PrizeDao;
import dev.jnx.jjshop.model.Prize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PrizeService {
    @Autowired private PrizeDao prizeDao;

    public RespBean add(Prize prize){
        int ret = prizeDao.add(prize);
        if (ret != 1){
            return RespBean.error("添加失败");
        }else{
            Map<String, Object> retMap = new HashMap<>();
            retMap.put("id", prize.getId());
            return RespBean.ok("添加成功", retMap);
        }
    }

    public RespBean update(Prize prize){
        int ret = prizeDao.update(prize);
        if (ret != 1){
            return RespBean.error("更新失败");
        }else{
            Map<String, Object> retMap = new HashMap<>();
            retMap.put("id", prize.getId());
            return RespBean.ok("更新成功", retMap);
        }
    }

    public RespBean list(){
        List<Prize> prizeList = prizeDao.list();
        return RespBean.ok("获取成功", prizeList);
    }

    public RespBean delete(int id){
        int ret = prizeDao.delete(id);
        if (ret != 1){
            return RespBean.error("删除失败");
        }else{
            return RespBean.ok("删除成功");
        }
    }

}

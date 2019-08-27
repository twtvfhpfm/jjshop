package dev.jnx.jjshop.service;

import dev.jnx.jjshop.bean.RespBean;
import dev.jnx.jjshop.dao.LogisticsDao;
import dev.jnx.jjshop.kdniao.KdNiaoUtil;
import dev.jnx.jjshop.model.LogisticsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

@Service
public class LogisticsService {
    @Autowired private LogisticsDao logisticsDao;
    @Autowired private RedisTemplate redisTemplate;
    @Autowired private KdNiaoUtil kdNiaoUtil;

    public RespBean add(String orderId,String customerName,String shipperCode,String logisticCode){
        int ret = logisticsDao.add(orderId, customerName, shipperCode, logisticCode);
        if(ret !=1){
            return RespBean.error("添加物流单号失败");
        }else{
            return RespBean.ok("添加成功");
        }
    }

    public RespBean update(String orderId,String customerName,String shipperCode,String logisticCode){
        LogisticsModel model = logisticsDao.get(orderId);
        int ret = 0;
        if (model == null){
            ret = logisticsDao.add(orderId, customerName, shipperCode, logisticCode);
        }
        else {
            ret = logisticsDao.update(orderId, customerName, shipperCode, logisticCode);
        }
        if(ret !=1){
            return RespBean.error("修改物流单号失败");
        }else{
            return RespBean.ok("修改成功");
        }
    }

    public RespBean get(String orderId){
        LogisticsModel model = logisticsDao.get(orderId);
        if (model == null){
            return RespBean.error("未查到物流信息");
        }
        return RespBean.ok("查询成功", model);
    }

    public RespBean getLogistic(String orderId){
        LogisticsModel model = logisticsDao.get(orderId);
        if (model == null || model.getLogisticCode().isEmpty()
                || model.getShipperCode().isEmpty()){
            return RespBean.error("未查到物流信息");
        }
        String response = (String)redisTemplate.opsForValue().get("logistics_" + orderId);
        if (response == null || response.isEmpty()){
            try {
                response = kdNiaoUtil.queryLogistic(model.getCustomerName(), model.getShipperCode(),
                        model.getLogisticCode());
                redisTemplate.opsForValue().set("logistics_" + orderId, response, 5, TimeUnit.HOURS);
            }
            catch(UnsupportedEncodingException ex){
                return RespBean.error("编码失败");
            }
        }
        model.setResponse(response);
        return RespBean.ok("查询成功", model);
    }

    public RespBean getShipperCode(String logisticCode){
        try {
            String resp = kdNiaoUtil.queryShipper(logisticCode);
            return RespBean.ok("查询成功", resp);
        }
        catch(UnsupportedEncodingException ex){
            return RespBean.error("编码失败");
        }
    }
}

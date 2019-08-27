package dev.jnx.jjshop.controller;

import dev.jnx.jjshop.bean.RespBean;
import dev.jnx.jjshop.service.LogisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.java2d.pipe.RegionSpanIterator;

@RestController
@RequestMapping("/logistics")
public class LogisticsController {
    @Autowired private LogisticsService logisticsService;

    @RequestMapping("/add")
    public RespBean add(String orderId,String customerName,String shipperCode,String logisticCode){
        return logisticsService.add(orderId, customerName, shipperCode, logisticCode);
    }

    @RequestMapping("/update")
    public RespBean update(String orderId,String customerName,String shipperCode,String logisticCode){
        return logisticsService.update(orderId, customerName, shipperCode, logisticCode);
    }

    @RequestMapping("/getlogistic")
    public RespBean getLogistic(String orderId){
        return logisticsService.getLogistic(orderId);
    }

    @RequestMapping("/getshippercode")
    public RespBean getShipperCode(String logisticCode){
        return logisticsService.getShipperCode(logisticCode);
    }

    @RequestMapping("/get")
    public RespBean get(String orderId){
        return logisticsService.get(orderId);
    }

    //@RequestMapping("/getlogistic")
    //public RespBean getLogistic(String logisticCode, String shipperCode, String customerName){
    //    return logisticsService.getLogistic(logisticCode, shipperCode, customerName);
    //}
}

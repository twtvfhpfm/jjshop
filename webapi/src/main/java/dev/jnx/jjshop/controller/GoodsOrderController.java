package dev.jnx.jjshop.controller;

import dev.jnx.jjshop.bean.RespBean;
import dev.jnx.jjshop.model.GoodsOrder;
import dev.jnx.jjshop.service.GoodsOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/goodsorder")
public class GoodsOrderController {
    @Autowired
    private GoodsOrderService goodsOrderService;

    @RequestMapping("/add")
    public RespBean add(int currentUid, String cidList, int addressId, Integer couponId){
        DateFormat df = new SimpleDateFormat("SSSyyMMddHHmmss");
        String orderId = df.format(new Date());
        orderId = String.format("A%03d", (int)(Math.random()*999)) + orderId;
        GoodsOrder goodsOrder = GoodsOrder.of(0, orderId, currentUid, cidList, 0, addressId,
                BigDecimal.ZERO, BigDecimal.ZERO);
        return goodsOrderService.add(goodsOrder, couponId);
    }

    @RequestMapping("/delete")
    public RespBean delete(String orderId, int currentUid){
        return goodsOrderService.delete(orderId, currentUid);
    }

    @RequestMapping("/updatestatus")
    public RespBean update(String orderId, int status){
        return goodsOrderService.update(orderId, status);
    }

    @RequestMapping("/confirmreceipt")
    public RespBean confirmReceipt(String orderId, int currentUid){
        return goodsOrderService.confirmReceipt(orderId, currentUid);
    }

    @RequestMapping("/get")
    public RespBean get(String orderId){
        return goodsOrderService.get(orderId);
    }

    @RequestMapping("/getbyuid")
    public RespBean getByUID(int currentUid, int lastMinId, String text){
        if (lastMinId == 0) lastMinId = Integer.MAX_VALUE;
        return goodsOrderService.getByUID(currentUid, lastMinId, text);
    }

    @RequestMapping("/list")
    public RespBean list(int lastMinId, String text, int status, String beginTime, String endTime){
        if (lastMinId == 0) lastMinId = Integer.MAX_VALUE;
        return goodsOrderService.list(lastMinId, text, status, beginTime, endTime);
    }
}

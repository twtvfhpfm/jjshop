package dev.jnx.jjshop.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

public class GoodsOrder {
    @Getter @Setter int id;
    @Getter @Setter String orderId;
    @Getter @Setter int uid;
    @Getter @Setter String cidList; //cart id
    @Getter @Setter int status; //0: toPay 1: toDelivery 2: toReceipt 3: toComment 4: finish
    @Getter @Setter int addressId;
    @Getter @Setter BigDecimal totalPrice;
    @Getter @Setter BigDecimal totalSuperiorPrice;
    @Getter @Setter Date createTime;

    public static GoodsOrder of(int id, String orderId, int uid, String cidList, int status, int addressId,
                                BigDecimal totalPrice, BigDecimal totalSuperiorPrice){
        GoodsOrder g = new GoodsOrder();
        g.setId(id);
        g.setOrderId(orderId);
        g.setUid(uid);
        g.setCidList(cidList);
        g.setStatus(status);
        g.setAddressId(addressId);
        g.setTotalPrice(totalPrice);
        g.setTotalSuperiorPrice(totalSuperiorPrice);
        g.setCreateTime(new Date());
        return g;
    }
}

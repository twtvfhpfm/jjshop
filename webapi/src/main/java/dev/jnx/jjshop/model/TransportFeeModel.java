package dev.jnx.jjshop.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

public class TransportFeeModel {
    @Getter @Setter int goodsId;
    @Getter @Setter int num;
    @Getter @Setter BigDecimal transportFee;
    @Getter @Setter BigDecimal totalPrice;

    public void addNum(int o){
        num+=o;
    }
    public void addPrice(BigDecimal o){
        totalPrice = totalPrice.add(o);
    }
}

package dev.jnx.jjshop.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

public class PriceReduce {
    @Getter @Setter private int id;
    @Getter @Setter private int goodsId;
    @Getter @Setter private BigDecimal amount;
    @Getter @Setter private BigDecimal superiorAmount;
    @Getter @Setter private BigDecimal transportFee;
    @Getter @Setter private int numCond;
}

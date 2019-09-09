package dev.jnx.jjshop.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

public class Coupon {
    @Getter @Setter private int id;
    @Getter @Setter private int uid;
    @Getter @Setter private int goodsId;
    @Getter @Setter private String orderId;
    @Getter @Setter private BigDecimal priceCond;
    @Getter @Setter private int type;
    @Getter @Setter private int numCond;
    @Getter @Setter private BigDecimal amount;
    @Getter @Setter private boolean isDeleted;
    @Getter @Setter private boolean isUsed;
    @Getter @Setter private Date beginTime;
    @Getter @Setter private Date endTime;
    @Getter @Setter private Date createTime;
}

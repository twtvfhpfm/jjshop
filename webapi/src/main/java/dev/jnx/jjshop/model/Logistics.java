package dev.jnx.jjshop.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class Logistics {
    @Getter @Setter private int id;
    @Getter @Setter private String orderId;
    @Getter @Setter private String customerName;
    @Getter @Setter private String shipperCode;
    @Getter @Setter private String logisticCode;
    @Getter @Setter private Date createTime;
}

package dev.jnx.jjshop.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
public class Prize {
    private int num;
    private BigDecimal amount;
    private int id;
    private int expireDays;
}

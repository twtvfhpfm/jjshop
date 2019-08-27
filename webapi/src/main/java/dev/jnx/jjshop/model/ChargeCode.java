package dev.jnx.jjshop.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

public class ChargeCode {
    @Getter @Setter int id;
    @Getter @Setter int type;
    @Getter @Setter String url;
    @Getter @Setter BigDecimal amount;
    @Getter @Setter byte[] data;
}

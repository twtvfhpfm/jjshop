package dev.jnx.jjshop.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

public class Goods {
    @Getter @Setter int id;
    @Getter @Setter int categoryId;
    @Getter @Setter BigDecimal price;
    @Getter @Setter BigDecimal superiorPrice;
    @Getter @Setter BigDecimal transportFee;
    @Getter @Setter int remain;
    @Getter @Setter int sales;
    @Getter @Setter boolean isDeleted;
    @Getter @Setter String title;
    @Getter @Setter String description;
    @Getter @Setter String thumb;
    @Getter @Setter Date createTime;

    public static Goods of(int categoryId, BigDecimal price, BigDecimal superiorPrice,
                           int remain, String title, String description, String thumb, int id,
                           int sales, BigDecimal transportFee){
        Goods g = new Goods();
        g.setCategoryId(categoryId);
        g.setDescription(description);
        g.setPrice(price);
        g.setRemain(remain);
        g.setSuperiorPrice(superiorPrice);
        g.setTitle(title);
        g.setThumb(thumb);
        g.setId(id);
        g.setSales(sales);
        g.setTransportFee(transportFee);
        return g;
    }
}

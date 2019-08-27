package dev.jnx.jjshop.model;

import lombok.Getter;
import lombok.Setter;
import sun.awt.image.PixelConverter;

import java.math.BigDecimal;
import java.util.Date;

public class Cart {
    @Getter @Setter int id;
    @Getter @Setter int uid;
    @Getter @Setter int goodsId;
    @Getter @Setter int skuListId;
    @Getter @Setter BigDecimal price;
    @Getter @Setter BigDecimal superiorPrice;
    @Getter @Setter int num;
    @Getter @Setter int ordered;
    @Getter @Setter Date createTime;

    public static Cart of(int id, int uid, BigDecimal price, BigDecimal superiorPrice, int goodsId, int skuListId, int num, int ordered){
        Cart c = new Cart();
        c.setId(id);
        c.setUid(uid);
        c.setPrice(price);
        c.setSuperiorPrice(superiorPrice);
        c.setGoodsId(goodsId);
        c.setSkuListId(skuListId);
        c.setNum(num);
        c.setOrdered(ordered);
        c.setCreateTime(new Date());
        return c;
    }
}

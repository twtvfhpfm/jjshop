package dev.jnx.jjshop.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

public class SkuList {
    @Getter @Setter int id;
    @Getter @Setter int goodsId;
    @Getter @Setter BigDecimal price;
    @Getter @Setter BigDecimal superiorPrice;
    @Getter @Setter int s1Id;
    @Getter @Setter int s2Id;
    @Getter @Setter int s3Id;
    @Getter @Setter int stockNum;
    @Getter @Setter boolean isDeleted;
    @Getter @Setter Date createTime;

    public static SkuList of(int id, int goodsId, BigDecimal price, BigDecimal superiorPrice, int s1Id, int s2Id, int s3Id, int stockNum){
        SkuList s = new SkuList();
        s.setId(id);
        s.setGoodsId(goodsId);
        s.setPrice(price);
        s.setSuperiorPrice(superiorPrice);
        s.setS1Id(s1Id);
        s.setS2Id(s2Id);
        s.setS3Id(s3Id);
        s.setStockNum(stockNum);
        return s;
    }
}

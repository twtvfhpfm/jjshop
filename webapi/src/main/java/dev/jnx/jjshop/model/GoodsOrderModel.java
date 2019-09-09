package dev.jnx.jjshop.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

public class GoodsOrderModel extends GoodsOrder {
    @Getter @Setter private Address address;
    @Getter @Setter private List<GoodsInfo> goodsList;

    public void fill(GoodsOrder o){
        this.setAddressId(o.getAddressId());
        this.setCidList(o.getCidList());
        this.setCreateTime(o.getCreateTime());
        this.setId(o.getId());
        this.setOrderId(o.getOrderId());
        this.setTotalSuperiorPrice(o.getTotalSuperiorPrice());
        this.setStatus(o.getStatus());
        this.setTotalPrice(o.getTotalPrice());
        this.setUid(o.getUid());
    }

    public static class GoodsInfo{
        @Setter @Getter private int goodsId;
        @Setter @Getter private int num;
        @Setter @Getter private String title;
        @Setter @Getter private String description;
        @Setter @Getter private int imgId;
        @Setter @Getter private List<String> tagNames;
        @Setter @Getter private BigDecimal price;
        @Setter @Getter private BigDecimal transportFee;

        public static GoodsInfo of(int goodsId, int num, String title, String description,
                                   int imgId, List<String> tagNames, BigDecimal price, BigDecimal transportFee){
            GoodsInfo g = new GoodsInfo();
            g.setGoodsId(goodsId);
            g.setNum(num);
            g.setTitle(title);
            g.setDescription(description);
            g.setImgId(imgId);
            g.setTagNames(tagNames);
            g.setPrice(price);
            g.setTransportFee(transportFee);
            return g;
        }
    }

    public void addGoods(int goodsId, int num, String title, String description,
                         int imgId, List<String> tagNames, BigDecimal price,
                         BigDecimal transportFee){
        goodsList.add(GoodsInfo.of(goodsId, num, title, description, imgId, tagNames, price, transportFee));
    }


}

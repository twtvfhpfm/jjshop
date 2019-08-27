package dev.jnx.jjshop.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class Sku {
    @Getter @Setter int id;
    @Getter @Setter int goodsId;
    @Getter @Setter int imgId;
    @Getter @Setter String keyName;
    @Getter @Setter String valueName;
    @Getter @Setter int skuOrder;
    @Getter @Setter boolean isDeleted;
    @Getter @Setter Date createTime;

    public static Sku of(int goodsId, int imgId, String keyName, String valueName, int skuOrder, int id){
        Sku sku = new Sku();
        sku.setGoodsId(goodsId);
        sku.setImgId(imgId);
        sku.setKeyName(keyName);
        sku.setValueName(valueName);
        sku.setSkuOrder(skuOrder);
        sku.setId(id);
        sku.setCreateTime(new Date());
        return sku;
    }
}

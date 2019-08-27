package dev.jnx.jjshop.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class GoodsModel extends Goods {
    @Getter @Setter String categoryName;
    @Getter @Setter List<Sku> sku;
    @Getter @Setter List<SkuList> skuList;
}

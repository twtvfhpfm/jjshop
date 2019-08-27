package dev.jnx.jjshop.model;

import lombok.Getter;
import lombok.Setter;

public class CartModel extends Cart {
    @Getter @Setter GoodsModel goods;
}

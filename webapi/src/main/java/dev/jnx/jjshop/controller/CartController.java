package dev.jnx.jjshop.controller;

import dev.jnx.jjshop.bean.RespBean;
import dev.jnx.jjshop.model.Cart;
import dev.jnx.jjshop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @RequestMapping("/add")
    public RespBean add(int currentUid, int goodsId, int skuListId, int num, int ordered){
        Cart cart = Cart.of(0, currentUid, BigDecimal.ZERO, BigDecimal.ZERO, goodsId, skuListId, num, ordered);
        return cartService.add(cart);
    }

    @RequestMapping("/delete")
    public RespBean delete(int id, int currentUid){
        return cartService.delete(id, currentUid);
    }

    //@RequestMapping("/update")
    //public RespBean update(int currentUid, int goodsId, int skuListId, int num, int ordered, int id){
    //    Cart cart = Cart.of(id, currentUid, goodsId, skuListId, num, ordered);
    //    return cartService.update(cart);
    //}

    @RequestMapping("/get")
    public RespBean get(int id){
        return cartService.get(id);
    }

    @RequestMapping("/getbyuid")
    public RespBean getByUid(int currentUid){
        return cartService.getByUid(currentUid);
    }

    @RequestMapping("/changenum")
    public RespBean changeNum(int delta, int id, int currentUid){
        return cartService.changeNum(delta, id, currentUid);
    }
}

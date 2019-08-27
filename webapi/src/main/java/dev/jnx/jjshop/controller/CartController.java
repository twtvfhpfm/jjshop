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
    public RespBean add(int uid, int goodsId, int skuListId, int num, int ordered){
        Cart cart = Cart.of(0, uid, BigDecimal.ZERO, BigDecimal.ZERO, goodsId, skuListId, num, ordered);
        return cartService.add(cart);
    }

    @RequestMapping("/delete")
    public RespBean delete(int id, int uid){
        return cartService.delete(id, uid);
    }

    //@RequestMapping("/update")
    //public RespBean update(int uid, int goodsId, int skuListId, int num, int ordered, int id){
    //    Cart cart = Cart.of(id, uid, goodsId, skuListId, num, ordered);
    //    return cartService.update(cart);
    //}

    @RequestMapping("/get")
    public RespBean get(int id){
        return cartService.get(id);
    }

    @RequestMapping("/getbyuid")
    public RespBean getByUid(int uid){
        return cartService.getByUid(uid);
    }

    @RequestMapping("/changenum")
    public RespBean changeNum(int delta, int id, int uid){
        return cartService.changeNum(delta, id, uid);
    }
}

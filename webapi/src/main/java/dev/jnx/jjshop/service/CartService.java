package dev.jnx.jjshop.service;

import dev.jnx.jjshop.bean.RespBean;
import dev.jnx.jjshop.dao.CartDao;
import dev.jnx.jjshop.dao.GoodsDao;
import dev.jnx.jjshop.dao.SkuDao;
import dev.jnx.jjshop.dao.SkuListDao;
import dev.jnx.jjshop.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartService {
    @Autowired
    private CartDao cartDao;
    @Autowired private SkuListDao skuListDao;
    @Autowired private GoodsDao goodsDao;
    @Autowired private SkuDao skuDao;

    public RespBean get(int id){
        Cart cart = cartDao.get(id);
        if (cart == null){
            return RespBean.error("购物车id不存在");
        }
        else{
            return RespBean.ok("获取成功", cart);
        }
    }

    public RespBean add(Cart cart){
        Goods g = goodsDao.get(cart.getGoodsId());
        BigDecimal price = g.getPrice();
        BigDecimal superiorPrice = g.getSuperiorPrice();
        if (cart.getSkuListId()!=0) {
            SkuList skuList = skuListDao.get(cart.getSkuListId());
            price = skuList.getPrice();
            superiorPrice = skuList.getSuperiorPrice();
        }
        cart.setPrice(price);
        cart.setSuperiorPrice(superiorPrice);
        int ret = cartDao.add(cart);
        if (ret == 1){
            return RespBean.ok("添加成功", cart.getId());
        }
        else{
            return RespBean.error("购物车添加失败");
        }
    }

    public RespBean delete(int id, int uid){
        int ret = cartDao.delete(id, uid);
        if (ret == 1){
            return RespBean.ok("删除成功");
        }
        else{
            return RespBean.error("购物车删除失败");
        }
    }

    public RespBean update(Cart cart){
        int ret = cartDao.update(cart);
        if (ret == 1){
            return RespBean.ok("修改成功");
        }
        else{
            return RespBean.error("购物车修改失败");
        }
    }

    public RespBean getByUid(int uid){
        List<CartModel> sl = cartDao.getByUid(uid);
        for(CartModel model : sl ){
            GoodsModel goods = goodsDao.get(model.getGoodsId());
            if(goods==null){
                return RespBean.error("商品id: "+model.getGoodsId()+"不存在");
            }
            goods.setSku(skuDao.getByGoodsId(goods.getId()));
            goods.setSkuList(skuListDao.getByGoodsId(goods.getId()));
            model.setGoods(goods);
        }
        return RespBean.ok("获取成功", sl);
    }

    public RespBean changeNum(int delta, int id, int uid){
        int ret = cartDao.changeNum(delta, id, uid);
        if (ret == 1){
            return RespBean.ok("修改成功");
        }
        else{
            return RespBean.error("购物车修改失败");
        }
    }
}

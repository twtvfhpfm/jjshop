package dev.jnx.jjshop.service;

import dev.jnx.jjshop.bean.RespBean;
import dev.jnx.jjshop.dao.*;
import dev.jnx.jjshop.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class GoodsOrderService {
    @Autowired
    private GoodsOrderDao goodsOrderDao;
    @Autowired private CartDao cartDao;
    @Autowired private AddressDao addressDao;
    @Autowired private GoodsDao goodsDao;
    @Autowired private SkuListDao skuListDao;
    @Autowired private SkuDao skuDao;
    @Autowired private RedisTemplate redisTemplate;

    public GoodsOrderModel getModel(GoodsOrder goodsOrder){
        GoodsOrderModel model = new GoodsOrderModel();
        model.fill(goodsOrder);
        model.setAddress(addressDao.get(goodsOrder.getAddressId()));
        model.setGoodsList(new ArrayList<>());

        for(String cid: goodsOrder.getCidList().split(",")){
            Cart cart = cartDao.get(Integer.parseInt(cid));
            Goods goods = goodsDao.get(cart.getGoodsId());
            int imgId = 0;
            if(!goods.getThumb().isEmpty()){
                imgId = Integer.parseInt(goods.getThumb().split(",")[0]);
            }
            List<String> tagList = new ArrayList<>();
            if (cart.getSkuListId()!=0){
                SkuList skuList = skuListDao.get(cart.getSkuListId());
                if (skuList.getS1Id()!=0){
                    tagList.add(skuDao.get(skuList.getS1Id()).getValueName());
                }
                if (skuList.getS2Id()!=0){
                    tagList.add(skuDao.get(skuList.getS2Id()).getValueName());
                }
                if (skuList.getS3Id()!=0){
                    tagList.add(skuDao.get(skuList.getS3Id()).getValueName());
                }
            }
            model.addGoods(cart.getGoodsId(), cart.getNum(), goods.getTitle(), goods.getDescription(),
                    imgId, tagList, cart.getPrice());
        }
        return model;
    }

    public RespBean get(String orderId){
        GoodsOrder goodsOrder = goodsOrderDao.get(orderId);
        if (goodsOrder == null){
            return RespBean.error("订单不存在");
        }
        else{
            return RespBean.ok("获取成功", getModel(goodsOrder));
        }
    }

    public RespBean add(GoodsOrder goodsOrder){
        String[] cidList = goodsOrder.getCidList().split(",");
        List<Cart> cartList = new ArrayList<>(cidList.length);
        BigDecimal totalPrice = BigDecimal.ZERO;
        BigDecimal totalSuperiorPrice = BigDecimal.ZERO;
        for(String cid: cidList){
            Cart c = cartDao.get(Integer.parseInt(cid));
            cartList.add(c);
            totalPrice = totalPrice.add(c.getPrice().multiply(BigDecimal.valueOf(c.getNum())));
            totalSuperiorPrice = totalSuperiorPrice.add(c.getSuperiorPrice().multiply(BigDecimal.valueOf(c.getNum())));
        }
        goodsOrder.setTotalPrice(totalPrice);
        goodsOrder.setTotalSuperiorPrice(totalSuperiorPrice);
        int ret = goodsOrderDao.add(goodsOrder);
        if (ret == 1){
            cartList.forEach(o->{
                cartDao.setOrdered(o.getId());
            });
            redisTemplate.opsForList().leftPush("listWaitForQRCode", goodsOrder);
            return RespBean.ok("添加成功", goodsOrder.getOrderId());
        }
        else{
            return RespBean.error("添加订单失败");
        }
    }

    public RespBean delete(String orderId, int uid){
        int ret = goodsOrderDao.delete(orderId, uid);
        if (ret == 1){
            return RespBean.ok("删除成功");
        }
        else{
            return RespBean.error("删除订单失败");
        }
    }

    public RespBean update(String orderId, int status){
        if (status == 1){
            //累加销量
            GoodsOrder goodsOrder = goodsOrderDao.get(orderId);
            GoodsOrderModel model = getModel(goodsOrder);
            for(GoodsOrderModel.GoodsInfo info : model.getGoodsList()){
                int ret = goodsDao.incrSales(info.getGoodsId(), info.getNum());
                if(ret != 1){
                    return RespBean.error("修改销量失败");
                }
            }
        }
        int ret = goodsOrderDao.update(orderId, status);
        if (ret == 1){
            return RespBean.ok("修改成功");
        }
        else{
            return RespBean.error("修改订单失败");
        }
    }

    public RespBean confirmReceipt(String orderId, int uid){
        int ret = goodsOrderDao.confirmReceipt(orderId, uid);
        if (ret == 1){
            return RespBean.ok("修改成功");
        }
        else{
            return RespBean.error("确认收货失败");
        }
    }

    public RespBean getByUID(int uid, int lastMinId, String text){
        List<GoodsOrderModel> modelList = new ArrayList<>();
        String msg = "continue";
        while(modelList.size()<20){
            List<GoodsOrder> ret = goodsOrderDao.getByUID(uid, lastMinId);
            for(GoodsOrder o : ret){
                GoodsOrderModel model = getModel(o);
                for(GoodsOrderModel.GoodsInfo info: model.getGoodsList()){
                    if (text == null || text.isEmpty() || info.getTitle().contains(text)) {
                        modelList.add(model);
                        break;
                    }
                }
                lastMinId = o.getId();
            }
            if(ret.size()<20){
                msg="end";
                break;
            }
        }
        return RespBean.ok(msg, modelList);
    }

    public RespBean list(int lastMinId, String text){
        List<GoodsOrder> ret = goodsOrderDao.list(lastMinId, text);
        List<GoodsOrderModel> modelList = new ArrayList<>();
        for(GoodsOrder o: ret){
            modelList.add(getModel(o));
        }
        String msg;
        if (modelList.size() < 20) msg="end";
        else msg = "continue";
        return RespBean.ok(msg, modelList);
    }
}

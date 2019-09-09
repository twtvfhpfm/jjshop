package dev.jnx.jjshop.service;

import dev.jnx.jjshop.bean.RespBean;
import dev.jnx.jjshop.dao.*;
import dev.jnx.jjshop.model.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

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
    @Autowired private CouponDao couponDao;
    @Autowired private PriceReduceDao priceReduceDao;
    private Logger logger = Logger.getLogger(this.getClass());

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
                    imgId, tagList, cart.getPrice(), goods.getTransportFee());
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

    @Transactional
    public RespBean add(GoodsOrder goodsOrder, Integer couponId){
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
        //////
        BigDecimal[] priceDiscountTotal = new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO};
        BigDecimal transportFeeTotal = BigDecimal.ZERO;
        GoodsOrderModel orderModel = getModel(goodsOrder);
        Map<Integer, TransportFeeModel> map = new HashMap<>();
        for(GoodsOrderModel.GoodsInfo info: orderModel.getGoodsList()){
            if (map.containsKey(info.getGoodsId())){
                TransportFeeModel model = map.get(info.getGoodsId());
                model.addNum(info.getNum());
                model.addPrice(info.getPrice().multiply(BigDecimal.valueOf(info.getNum())));
            }else{
                TransportFeeModel model = new TransportFeeModel();
                model.setGoodsId(info.getGoodsId());
                model.setNum(info.getNum());
                model.setTransportFee(info.getTransportFee());
                model.setTotalPrice(info.getPrice().multiply(BigDecimal.valueOf(info.getNum())));
                map.put(info.getGoodsId(), model);
            }
        }
        if (couponId != null && couponId != 0){
            Coupon coupon = couponDao.getBean(couponId, goodsOrder.getUid());
            if (coupon != null){
                if (coupon.isDeleted() || coupon.isUsed()){
                    return RespBean.error("优惠券无效");
                }
                int totalNumForCoupon = orderModel.getGoodsList().stream()
                        .filter(o->coupon.getGoodsId()==0 || coupon.getGoodsId()==o.getGoodsId())
                        .mapToInt(GoodsOrderModel.GoodsInfo::getNum).sum();
                BigDecimal totalPriceForCoupon = orderModel.getGoodsList().stream()
                        .filter(o->coupon.getGoodsId()==0 || coupon.getGoodsId()==o.getGoodsId())
                        .map(o->o.getPrice().multiply(BigDecimal.valueOf(o.getNum())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                if (totalNumForCoupon < coupon.getNumCond()
                        || totalPriceForCoupon.compareTo(coupon.getPriceCond()) < 0){
                    return RespBean.error("优惠券无法使用");
                }
                totalPrice = totalPrice.subtract(coupon.getAmount());
                priceDiscountTotal[0] = priceDiscountTotal[0].add(coupon.getAmount());
            }
        }
        for(Map.Entry<Integer, TransportFeeModel> entry: map.entrySet()){
            List<PriceReduce> reduceList = priceReduceDao.getByGoodsId(entry.getKey());
            TransportFeeModel feeModel = entry.getValue();
            for(PriceReduce reduce: reduceList){
                if (feeModel.getNum() >= reduce.getNumCond()){
                    feeModel.setTransportFee(reduce.getTransportFee());
                    totalPrice = totalPrice.subtract(reduce.getAmount());
                    totalSuperiorPrice = totalSuperiorPrice.subtract(reduce.getSuperiorAmount());
                    priceDiscountTotal[0] = priceDiscountTotal[0].add(reduce.getAmount());
                    priceDiscountTotal[1] = priceDiscountTotal[1].add(reduce.getSuperiorAmount());
                    break;
                }
            }
            totalPrice = totalPrice.add(feeModel.getTransportFee());
            totalSuperiorPrice = totalSuperiorPrice.add(feeModel.getTransportFee());
            transportFeeTotal= transportFeeTotal.add(feeModel.getTransportFee());
        }
        if (totalPrice.compareTo(BigDecimal.ZERO)<0) totalPrice = BigDecimal.ZERO;
        if (totalSuperiorPrice.compareTo(BigDecimal.ZERO)<0) totalSuperiorPrice = BigDecimal.ZERO;
        logger.info("totalDiscount: " + priceDiscountTotal[0]+ " totalSupDiscount:"
                     + priceDiscountTotal[1] + "transportFee: " + transportFeeTotal);
        //////
        goodsOrder.setTotalPrice(totalPrice);
        goodsOrder.setTotalSuperiorPrice(totalSuperiorPrice);
        if (couponId != null && couponId != 0) {
            int ret = couponDao.use(couponId, goodsOrder.getOrderId(), goodsOrder.getUid());
            if (ret != 1){
                return RespBean.error("优惠券使用失败");
            }
        }
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

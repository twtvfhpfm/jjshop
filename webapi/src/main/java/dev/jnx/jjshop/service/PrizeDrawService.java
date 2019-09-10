package dev.jnx.jjshop.service;

import dev.jnx.jjshop.bean.RespBean;
import dev.jnx.jjshop.dao.CouponDao;
import dev.jnx.jjshop.dao.PrizeDao;
import dev.jnx.jjshop.dao.PrizeDrawDao;
import dev.jnx.jjshop.model.Prize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class PrizeDrawService{
    @Autowired private PrizeDrawDao prizeDrawDao;
    @Autowired private PrizeDao prizeDao;
    @Autowired private CouponDao couponDao;

    @Transactional
    public RespBean draw(int uid) {
        if (prizeDrawDao.count(uid) <= 0){
            return RespBean.error("您的抽奖机会已用完");
        }
        List<Prize> prizeList = prizeDao.list();
        List<Integer> idList = new ArrayList<>();
        for(Prize p: prizeList){
            for(int i = 0; i < p.getNum(); i++){
                idList.add(p.getId());
            }
        }

        int idx = idList.isEmpty() ? 0 : new Random().nextInt(65535) % idList.size();
        Optional<Prize> drawResult = prizeList.stream()
                .filter(o -> o.getId() == idList.get(idx)).findFirst();

        int couponId = 0;
        Map<String, Object> couponMap = new HashMap<>();
        drawResult.ifPresent(result ->{
            if (result.getAmount().compareTo(BigDecimal.ZERO) > 0){
                couponMap.put("uid", uid);
                couponMap.put("goodsId", 0);
                couponMap.put("orderId", "");
                couponMap.put("priceCond", "0");
                couponMap.put("numCond", 0);
                couponMap.put("type", 2);
                couponMap.put("amount", result.getAmount());
                couponMap.put("isDeleted", 0);
                couponMap.put("isUsed", 0);
                couponMap.put("beginTime", new Date());
                Calendar endTime = Calendar.getInstance();
                endTime.add(Calendar.DATE, result.getExpireDays());
                couponMap.put("endTime", endTime.getTime());
            }
        });

        if (!couponMap.isEmpty()){
            if (1 != couponDao.offer(couponMap)){
                return RespBean.error("抽奖失败");
            }
        }

        int ret = prizeDrawDao.use(couponId, uid);
        if (ret != 1){
            return RespBean.error("抽奖失败");
        }
        if (couponMap.isEmpty()){
            return RespBean.ok("与优惠券擦肩而过");
        }else{
            return RespBean.ok("恭喜抽中一张" + couponMap.get("amount") + "元优惠券");
        }
    }

    public RespBean count(int uid) {
        int ret = prizeDrawDao.count(uid);
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("num", ret);
        return RespBean.ok("查询成功", retMap);
    }
}

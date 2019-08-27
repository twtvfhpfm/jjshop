package dev.jnx.jjshop.service;

import dev.jnx.jjshop.bean.RespBean;
import dev.jnx.jjshop.dao.ChargeCodeDao;
import dev.jnx.jjshop.model.ChargeCode;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;

@Service
public class ChargeCodeService {
    @Autowired
    private ChargeCodeDao chargeCodeDao;
    Logger logger = Logger.getLogger(this.getClass());

    public RespBean get(BigDecimal amount, int type){
        ChargeCode pic = chargeCodeDao.get(amount, type);
        if (pic == null){
            return RespBean.error("收钱码不存在");
        }else{
            if (type ==1 ){
                pic.setUrl(new String(pic.getData()));
            }
            return RespBean.ok("获取成功", pic);
        }
    }

    public RespBean add(BigDecimal amount, int type, byte[] data){
        ChargeCode pic = new ChargeCode();
        pic.setAmount(amount);
        pic.setData(data);
        pic.setType(type);
        chargeCodeDao.delete(amount, type);
        int ret = chargeCodeDao.add(pic);
        if (ret == 1){
            return RespBean.ok("添加成功", pic.getId());
        }else{
            return RespBean.error("添加收钱码失败");
        }
    }
    
    public RespBean list(){
        List<ChargeCode> cl = chargeCodeDao.list();
        return RespBean.ok("获取成功", cl);
    }

    public RespBean delete(BigDecimal amount, int type){
        int ret = chargeCodeDao.delete(amount, type);

        if (ret == 1){
            return RespBean.ok("删除成功");
        }else{
            return RespBean.error("删除收钱码失败");
        }
    }
}

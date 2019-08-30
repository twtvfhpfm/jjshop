package dev.jnx.jjshop.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import dev.jnx.jjshop.bean.RespBean;
import dev.jnx.jjshop.dao.ChargeCodeDao;
import dev.jnx.jjshop.model.ChargeCode;
import dev.jnx.jjshop.model.GoodsOrder;
import dev.jnx.jjshop.model.QRCodeModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class ChargeCodeService {
    @Autowired
    private ChargeCodeDao chargeCodeDao;
    @Autowired private RedisTemplate redisTemplate;
    @Value("${md5key}") private String md5Key;
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

    public RespBean getWork(QRCodeModel model){
        StringBuilder sb = new StringBuilder();
        sb.append(model.getAmount().setScale(2).toString()).append(model.getDesc())
                .append(model.getUrl()).append(md5Key);
        String sign = DigestUtils.md5DigestAsHex(sb.toString().getBytes());
        if (!sign.equals(model.getSign())){
            return RespBean.error("签名错误");
        }

        GoodsOrder goodsOrder = (GoodsOrder)redisTemplate.opsForList().rightPop("listWaitForQRCode");
        if (goodsOrder == null){
            return RespBean.error("没有待生成二维码的订单");
        }
        return RespBean.ok("获取成功", QRCodeModel.of(goodsOrder.getTotalPrice(),
                goodsOrder.getOrderId(), "", md5Key));
    }

    public RespBean putQRCode(QRCodeModel model){
        if (model == null || model.getAmount() == null || model.getDesc() == null
                || model.getDesc().isEmpty() || model.getUrl() == null ||
                model.getUrl().isEmpty()){
            return RespBean.error("参数错误");
        }
        StringBuilder sb = new StringBuilder();
        sb.append(model.getAmount().setScale(2).toString()).append(model.getDesc())
                .append(model.getUrl()).append(md5Key);
        String sign = DigestUtils.md5DigestAsHex(sb.toString().getBytes());
        if (!sign.equals(model.getSign())){
            return RespBean.error("签名错误");
        }
        redisTemplate.opsForValue().set("wechatqrcode_"+model.getDesc(), model,
                30, TimeUnit.DAYS);
        return RespBean.ok("保存成功");
    }

    public RespBean getWeChatQRCode(String orderId){
        QRCodeModel model = (QRCodeModel)redisTemplate.opsForValue().get("wechatqrcode_" + orderId);
        if (model == null){
            return RespBean.build().setStatus(404).setMsg("未获取到指定订单的二维码");
        }
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        Map hints = new HashMap();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try{
            bitMatrix = multiFormatWriter.encode(model.getUrl(), BarcodeFormat.QR_CODE,
                    240,240, hints);
            BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
            ImageIO.write(image, "jpeg", outputStream);
        }catch(WriterException ex){
            logger.error(ex);
            return RespBean.error("生成二维码失败");
        }catch(IOException ex){
            logger.error(ex);
            return RespBean.error("二维码图片写入失败");
        }
        return RespBean.ok("生成二维码成功", outputStream.toByteArray());
    }

    public RespBean checkWeChatQRCode(String orderId) {
        QRCodeModel model = (QRCodeModel)redisTemplate.opsForValue().get("wechatqrcode_" + orderId);
        if (model == null){
            return RespBean.error("未获取到指定订单的二维码");
        }else{
            return RespBean.ok("获取成功");
        }
    }
}

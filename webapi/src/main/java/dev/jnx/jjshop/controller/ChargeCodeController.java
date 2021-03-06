package dev.jnx.jjshop.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import dev.jnx.jjshop.bean.RespBean;
import dev.jnx.jjshop.model.ChargeCode;
import dev.jnx.jjshop.model.QRCodeModel;
import dev.jnx.jjshop.service.ChargeCodeService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Base64;

@RestController
@RequestMapping("/chargecode")
public class ChargeCodeController {
    @Autowired
    private ChargeCodeService chargeCodeService;
    Logger logger = Logger.getLogger(this.getClass());

    @RequestMapping("/getwechat")
    public void getWechat(BigDecimal amount, HttpServletResponse resp){
        RespBean b = chargeCodeService.get(amount, 0);
        if (b.getStatus() == 500){
            resp.setStatus(404);
            return;
        }
        resp.setContentType("image/jpeg");
        ChargeCode pic = (ChargeCode)b.getObj();
        resp.setContentLength(pic.getData().length);
        try (ServletOutputStream out = resp.getOutputStream()){
            out.write(pic.getData());
        }
        catch(IOException ex){
            logger.error(ex.getMessage());
        }
    }

    @RequestMapping("/getalipay")
    public RespBean getAlipay(BigDecimal amount){
        return chargeCodeService.get(amount, 1);
    }

    @RequestMapping("/addwechat")
    public RespBean addWechat(BigDecimal amount, String base64Data){
        byte[] data = Base64.getDecoder().decode(base64Data);
        return chargeCodeService.add(amount, 0, data);
    }

    @RequestMapping("/addalipay")
    public RespBean addAlipay(BigDecimal amount, String url){
        return chargeCodeService.add(amount, 1, url.getBytes());
    }

    @RequestMapping("/list")
    public RespBean list(){
        return chargeCodeService.list();
    }

    @RequestMapping("/delete")
    public RespBean delete(BigDecimal amount, int type){
        return chargeCodeService.delete(amount, type);
    }

    @RequestMapping("/getwork")
    public RespBean getWork(@RequestBody QRCodeModel model){
        return chargeCodeService.getWork(model);
    }

    @RequestMapping("/putqrcode")
    public RespBean putQRCode(@RequestBody QRCodeModel model){
        return chargeCodeService.putQRCode(model);
    }

    @RequestMapping("/getwechatqrcode")
    public void getWeChatQRCode(String orderId, HttpServletResponse  resp)
            throws UnsupportedEncodingException {
        RespBean b = chargeCodeService.getWeChatQRCode(orderId);
        byte[] data;
        if (b.getStatus() != 200){
            resp.setContentType("application/json;charset=utf-8");
            data = JSON.toJSONString(b).getBytes("utf-8");
        }else{
            resp.setContentType("image/jpeg");
            data = (byte[])b.getObj();
        }
        resp.setContentLength(data.length);
        try (ServletOutputStream out = resp.getOutputStream()){
            out.write(data);
        }
        catch(IOException ex){
            logger.error(ex.getMessage());
        }
    }

    @RequestMapping("/checkwechatqrcode")
    public RespBean checkWeChatQRCode(String orderId){
        return chargeCodeService.checkWeChatQRCode(orderId);
    }
}

package dev.jnx.jjshop.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.DigestUtils;

import java.math.BigDecimal;

public class QRCodeModel {
    @Getter @Setter
    BigDecimal amount;
    @Getter @Setter String desc;
    @Getter @Setter String url;
    @Getter @Setter String sign;

    public static QRCodeModel of(BigDecimal amount, String desc, String url, String key){
        QRCodeModel model = new QRCodeModel();
        model.setAmount(amount);
        model.setDesc(desc);
        model.setUrl(url);
        StringBuilder sb = new StringBuilder();
        sb.append(amount.setScale(2).toString()).append(desc).append(url).append(key);
        model.setSign(DigestUtils.md5DigestAsHex(sb.toString().getBytes()));
        return model;
    }
}

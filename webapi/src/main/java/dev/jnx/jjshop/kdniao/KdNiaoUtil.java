package dev.jnx.jjshop.kdniao;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;

@Component
public class KdNiaoUtil {
    @Value("${kdniao.businessID}")
    private String businessID;
    @Value("${kdniao.apiKey}")
    private String apiKey;
    @Value("${kdniao.url}")
    private String url;

    Logger logger = Logger.getLogger(this.getClass());

    private String query(String requestType, String requestData) throws UnsupportedEncodingException {
        logger.info("kdniao query: " + requestType + requestData);
        String sign = DigestUtils.md5DigestAsHex((requestData+apiKey).getBytes("utf8"));
        sign = Base64.getEncoder().encodeToString(sign.getBytes("utf8"));
        sign = URLEncoder.encode(sign, "utf8");
        StringBuilder sb = new StringBuilder();
        sb.append("RequestType=").append(requestType)
                .append("&EBusinessID=").append(businessID)
                .append("&RequestData=").append(requestData)
                .append("&DataSign=").append(sign)
                .append("&DataType=2");

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = new StringEntity(sb.toString(),
                ContentType.create("application/x-www-form-urlencoded", "UTF-8"));
        httpPost.setEntity(entity);

        String result="";

        try(CloseableHttpResponse response = httpClient.execute(httpPost)){
            HttpEntity entity1 = response.getEntity();
            result = EntityUtils.toString(entity1);
            logger.info("快递鸟返回: " + result);
        }
        catch(Exception ex){
            logger.error("快递鸟query失败: " + ex.getMessage());
        }
        return result;
    }

    public String queryShipper(String logisticCode) throws UnsupportedEncodingException{
        JSONObject jo = new JSONObject();
        jo.put("logisticCode", logisticCode);
        return query("2002", jo.toJSONString());
    }

    public String queryLogistic(String customerName, String shipperCode, String logisticCode)
            throws UnsupportedEncodingException{
        JSONObject jo = new JSONObject();
        jo.put("OrderCode", "");
        jo.put("ShipperCode", shipperCode);
        jo.put("CustomerName", customerName);
        jo.put("LogisticCode", logisticCode);
        return query("1002", jo.toJSONString());
    }
}

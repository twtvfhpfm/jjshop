package dev.jnx.jjshop.config;

import dev.jnx.jjshop.bean.RespBean;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    Logger logger = Logger.getLogger(this.getClass());
    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public RespBean handleException(HttpServletRequest request, Exception ex)throws IOException {
        Map<String, String[]> map = request.getParameterMap();
        StringBuilder sb = new StringBuilder();
        sb.append(request.getRequestURI()).append(": ");
        for(Map.Entry entry: map.entrySet()){
            sb.append(entry.getKey()).append("=");
            for(String s: (String[])entry.getValue()){
                sb.append(s).append(",");
            }
        }
        logger.fatal(sb.toString(), ex);
        return RespBean.error("服务器一不小心崩溃啦");
    }
}

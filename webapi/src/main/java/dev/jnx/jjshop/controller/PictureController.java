package dev.jnx.jjshop.controller;

import dev.jnx.jjshop.bean.RespBean;
import dev.jnx.jjshop.model.Picture;
import dev.jnx.jjshop.service.PictureService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping("/picture")
public class PictureController {
    @Autowired private PictureService pictureService;
    Logger logger = Logger.getLogger(this.getClass());

    @RequestMapping("/get")
    public void get(int id, HttpServletResponse resp){
        RespBean b = pictureService.get(id);
        if (b.getStatus() == 500){
            resp.setStatus(404);
            return;
        }
        resp.setContentType("image/jpeg");
        Picture pic = (Picture)b.getObj();
        resp.setContentLength(pic.getData().length);
        try (ServletOutputStream out = resp.getOutputStream()){
            out.write(pic.getData());
        }
        catch(IOException ex){
            logger.error(ex.getMessage());
        }
    }

    @RequestMapping("/add")
    public RespBean add(String base64Data){
        byte[] data = Base64.getDecoder().decode(base64Data);
        return pictureService.add(data);
    }

    @RequestMapping("/delete")
    public RespBean delete(int id){
        return pictureService.delete(id);
    }
}

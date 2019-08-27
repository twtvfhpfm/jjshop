package dev.jnx.jjshop.service;

import dev.jnx.jjshop.bean.RespBean;
import dev.jnx.jjshop.dao.PictureDao;
import dev.jnx.jjshop.model.Picture;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Blob;
import java.sql.SQLException;

@Service
public class PictureService {
    @Autowired private PictureDao pictureDao;
    Logger logger = Logger.getLogger(this.getClass());

    public RespBean get(int id){
        Picture pic = pictureDao.get(id);
        if (pic == null){
            return RespBean.error("图片不存在");
        }else{
            return RespBean.ok("获取成功", pic);
        }
    }

    public RespBean add(byte[] data){
        Picture pic = new Picture();
        pic.setData(data);
        int ret = pictureDao.add(pic);
        if (ret == 1){
            return RespBean.ok("添加成功", pic.getId());
        }else{
            return RespBean.error("添加图片失败");
        }
    }

    public RespBean delete(int id){
        int ret = pictureDao.delete(id);
        if (ret == 1){
            return RespBean.ok("删除成功");
        }else{
            return RespBean.error("删除图片失败");
        }
    }
}

package dev.jnx.jjshop.service;

import dev.jnx.jjshop.bean.RespBean;
import dev.jnx.jjshop.dao.CategoryDao;
import dev.jnx.jjshop.dao.GoodsDao;
import dev.jnx.jjshop.model.Category;
import dev.jnx.jjshop.model.CategoryModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired private CategoryDao categoryDao;
    @Autowired private GoodsDao goodsDao;
    Logger logger = Logger.getLogger(this.getClass());

    public RespBean list(){
        List<CategoryModel> cl = categoryDao.list();
        return RespBean.ok("获取成功", cl);
    }

    public RespBean add(Category c){
        int ret = categoryDao.add(c);
        return (ret == 1 ? RespBean.ok("添加成功")
        : RespBean.error("添加分类失败"));
    }

    public RespBean delete(int id){
        int count = goodsDao.getCountInCategory(id);
        if(count != 0){
            return RespBean.error("该分类下还有商品，不能删除");
        }
        int ret = categoryDao.delete(id);
        return (ret == 1 ? RespBean.ok("删除成功")
                : RespBean.error("删除分类失败"));
    }
}

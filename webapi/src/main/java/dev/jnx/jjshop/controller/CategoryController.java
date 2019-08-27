package dev.jnx.jjshop.controller;

import dev.jnx.jjshop.bean.RespBean;
import dev.jnx.jjshop.model.Category;
import dev.jnx.jjshop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired private CategoryService categoryService;

    @RequestMapping("/list")
    public RespBean list(){
        return categoryService.list();
    }

    @RequestMapping("/add")
    public RespBean add(String name){
        Category c = new Category();
        c.setName(name);
        return categoryService.add(c);
    }

    @RequestMapping("/delete")
    public RespBean delete(int id){
        return categoryService.delete(id);
    }
}

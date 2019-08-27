package dev.jnx.jjshop.dao;

import dev.jnx.jjshop.model.Category;
import dev.jnx.jjshop.model.CategoryModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryDao {
    List<CategoryModel> list();
    int add(Category c);
    int delete(int id);
}

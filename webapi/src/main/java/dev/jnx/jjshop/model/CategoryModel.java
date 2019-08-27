package dev.jnx.jjshop.model;

import lombok.Getter;
import lombok.Setter;

public class CategoryModel extends Category {
    @Getter @Setter int count;
    @Getter @Setter String thumb;

    public static CategoryModel of(int id, String name, int count, String thumb){
        CategoryModel c = new CategoryModel();
        c.setId(id);
        c.setName(name);
        c.setCount(count);
        c.setThumb(thumb);
        return c;
    }
}

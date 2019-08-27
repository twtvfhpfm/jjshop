package dev.jnx.jjshop.dao;

import dev.jnx.jjshop.model.Picture;
import org.springframework.stereotype.Repository;

import java.sql.Blob;

@Repository
public interface PictureDao {
    int add(Picture pic);
    Picture get(int id);
    int delete(int id);
}

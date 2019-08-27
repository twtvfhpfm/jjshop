package dev.jnx.jjshop.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class User{
    @Getter @Setter private int id;
    @Getter @Setter private String username;
    @Getter @Setter private String password;
    @Getter @Setter private Date createTime;
    @Getter @Setter private int role;
}
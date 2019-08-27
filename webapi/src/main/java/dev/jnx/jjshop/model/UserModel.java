package dev.jnx.jjshop.model;

import lombok.Getter;
import lombok.Setter;

public class UserModel extends  User {
    @Getter @Setter private String token;
}

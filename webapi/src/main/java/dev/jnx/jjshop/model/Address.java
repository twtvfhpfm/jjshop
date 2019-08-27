package dev.jnx.jjshop.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class Address {
    @Getter @Setter int id;
    @Getter @Setter int uid;
    @Getter @Setter String name;
    @Getter @Setter String mobile;
    @Getter @Setter String province;
    @Getter @Setter String city;
    @Getter @Setter String county;
    @Getter @Setter String addressDetail;
    @Getter @Setter String areaCode;
    @Getter @Setter boolean isDefault;
    @Getter @Setter boolean isDeleted;
    @Getter @Setter Date createTime;
}
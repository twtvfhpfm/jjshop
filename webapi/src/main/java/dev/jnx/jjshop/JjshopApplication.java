package dev.jnx.jjshop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("dev.jnx.jjshop.dao")
public class JjshopApplication {

    public static void main(String[] args) {
        SpringApplication.run(JjshopApplication.class, args);
    }

}

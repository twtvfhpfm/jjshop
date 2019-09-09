package dev.jnx.jjshop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("dev.jnx.jjshop.dao")
@EnableTransactionManagement
public class JjshopApplication {

    public static void main(String[] args) {
        SpringApplication.run(JjshopApplication.class, args);
    }

}

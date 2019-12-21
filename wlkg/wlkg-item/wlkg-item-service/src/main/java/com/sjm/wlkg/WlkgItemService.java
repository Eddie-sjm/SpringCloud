package com.sjm.wlkg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = "com.sjm.wlkg.mapper")
@ComponentScan(basePackages = {"com.sjm.common","com.sjm.wlkg"})
public class WlkgItemService {
    public static void main(String[] args) {
        SpringApplication.run(WlkgItemService.class,args);
    }
}

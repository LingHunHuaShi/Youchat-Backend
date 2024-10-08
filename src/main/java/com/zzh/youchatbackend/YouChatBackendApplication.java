package com.zzh.youchatbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAsync
@SpringBootApplication(scanBasePackages = {"com.zzh.youchatbackend"})
@MapperScan(basePackages = {"com.zzh.youchatbackend.common.mapper", "com.zzh.youchatbackend.module.chat.mapper"})
@EnableTransactionManagement
@EnableScheduling
public class YouChatBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(YouChatBackendApplication.class, args);
    }

}

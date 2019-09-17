package com.simba.dongfeng.executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class DongfengExecutorApplication {

    /*@PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+08"));
    }*/

    public static void main(String[] args) {
        SpringApplication.run(DongfengExecutorApplication.class, args);
    }

}

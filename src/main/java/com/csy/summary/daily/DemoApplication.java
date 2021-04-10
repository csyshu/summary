package com.csy.summary.daily;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@ServletComponentScan
@RestController
@MapperScan(value = "com.csy.druid.demo.mapper")
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(DemoApplication.class);
        //关闭banner.mode
        springApplication.setBannerMode(Banner.Mode.CONSOLE);
        springApplication.run(args);
    }

}

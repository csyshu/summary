package com.csy.summary.daily;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @author shuyun.cheng
 */
@SpringBootApplication
@ServletComponentScan
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(DemoApplication.class);
        //关闭banner.mode
        springApplication.setBannerMode(Banner.Mode.CONSOLE);
        springApplication.run(args);
    }

}

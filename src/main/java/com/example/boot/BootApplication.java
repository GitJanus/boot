package com.example.boot;

import com.example.boot.filter.BaseFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BootApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootApplication.class, args);
    }

//    //添加一个基本过滤器，用于设置编码，调试观察请求数据
//    @Bean
//    public FilterRegistrationBean registerBaseFilter() {
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        registration.setFilter(new BaseFilter());
//        registration.addUrlPatterns("/*");
//        registration.setOrder(1);
//        return registration;
//    }
}

package com.changgou.web.center;

import com.changgou.interceptor.FeignInterceptor;
import com.changgou.util.SMSUtilsLqz;
import com.changgou.interceptor.FeignInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = { "com.changgou.pay.feign","com.changgou.order.feign","com.changgou.user.feign","com.changgou.goods.feign"})
public class WebCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebCenterApplication.class,args);
    }

    @Bean
    public FeignInterceptor feignInterceptor() {
        return new FeignInterceptor();
    }


    @Bean
    public SMSUtilsLqz smsUtilsLqz(){
        return new SMSUtilsLqz();
    }
}

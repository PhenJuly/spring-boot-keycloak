package com.phenjuly.security.webapp.teacher.student;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.session.HttpSessionEventPublisher;

/**
 * @author PhenJuly
 */
@EnableFeignClients
@SpringBootApplication
public class StudentSecurityWebAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentSecurityWebAppApplication.class, args);
    }

    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
    }
}

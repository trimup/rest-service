package com.lihe;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@SpringBootApplication
@EnableDiscoveryClient
public class UserAccountApplication /*extends SpringBootServletInitializer */ {


    public static void main(String[] args) throws Exception {
        SpringApplication.run(UserAccountApplication.class, args);
    }


    @Value("${allowed-origins}")
    private String[] allowedOrigins = new String[] {"*"};

    @Bean
    public WebMvcConfigurerAdapter configurerAdapter() {
        return new WebMvcConfigurerAdapter() {

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedMethods("OPTIONS", "POST", "GET", "DELETE")
                        .allowedOrigins(allowedOrigins == null || allowedOrigins.length == 0 ?
                                new String[] {"*"} :
                                allowedOrigins)
                        .allowCredentials(false).maxAge(3600);
            }
        };
    }

    @LoadBalanced
    @Bean
    RestTemplate loadBalanced() {
        return new RestTemplate();
    }



}

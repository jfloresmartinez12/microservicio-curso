package com.thoughtmechanix.licenses;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.sleuth.Sampler;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.thoughtmechanix.licenses.config.ServiceConfig;

@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
public class Application {

    @Autowired
    private ServiceConfig serviceConfig;

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    @Bean
    public Sampler defaultSampler() {
        return new AlwaysSampler();
    }
    
    // @LoadBalanced
    @Bean
    public RestTemplate getRestTemplate() {
        RestTemplate template = new RestTemplate();

        return template;
    }

    

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

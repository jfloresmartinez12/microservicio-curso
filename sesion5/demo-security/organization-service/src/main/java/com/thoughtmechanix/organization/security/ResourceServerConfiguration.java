package com.thoughtmechanix.organization.security;


import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {


    @Override
    public void configure(HttpSecurity http) throws Exception{
    	//1
        http
          .authorizeRequests()
          //1.1
          .anyRequest()
          //1.2
          .authenticated();
    }
}

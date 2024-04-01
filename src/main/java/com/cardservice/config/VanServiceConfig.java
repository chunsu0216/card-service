package com.cardservice.config;

import com.cardservice.service.van.VanService;
import com.cardservice.service.van.ksnet.KsnetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class VanServiceConfig {

    @Bean(name = "KSNET")
    public VanService ksnetService(){
        return new KsnetService(new RestTemplate(), new ObjectMapper());
    }
}

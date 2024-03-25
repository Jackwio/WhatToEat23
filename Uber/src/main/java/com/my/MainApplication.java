package com.my;

import jakarta.jms.ConnectionFactory;
import jakarta.jms.Destination;
import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import javax.crypto.interfaces.PBEKey;

@SpringBootApplication
public class MainApplication {
    @Bean
    public Destination orderQueue(){
        return new ActiveMQQueue("jms.queue.order.queue");
    }
    @Bean
    public Destination informQueue(){
        return new ActiveMQQueue("jms.queue.inform.queue");
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder){
        return restTemplateBuilder.build();
    }
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class,args);
    }
}

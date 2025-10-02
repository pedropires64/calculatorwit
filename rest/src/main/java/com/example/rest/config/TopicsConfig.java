package com.example.rest.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TopicsConfig {
  @Bean public NewTopic calcRequests(){ return TopicBuilder.name("calc.requests").partitions(1).replicas(1).build(); }
  @Bean public NewTopic calcReplies(){ return TopicBuilder.name("calc.replies").partitions(1).replicas(1).build(); }
}

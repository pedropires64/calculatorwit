package com.example.rest.config;

import com.example.rest.model.CalcRequest;
import com.example.rest.model.CalcResult;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

  @Value("${spring.kafka.bootstrap-servers}") private String bootstrap;
  @Value("${app.kafka.reply-topic:calc.replies}") private String replyTopic;

  @Bean
  public ProducerFactory<String, CalcRequest> producerFactory() {
    Map<String, Object> props = new HashMap<>();
    props.put(org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
    props.put(org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
    return new DefaultKafkaProducerFactory<>(props);
  }

  @Bean
  public ConsumerFactory<String, CalcResult> repliesConsumerFactory() {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
    JsonDeserializer<CalcResult> jd = new JsonDeserializer<>(CalcResult.class);
    jd.addTrustedPackages("*");
    jd.ignoreTypeHeaders();
    return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), jd);
  }

  @Bean
  public ConcurrentMessageListenerContainer<String, CalcResult> repliesContainer(
      ConsumerFactory<String, CalcResult> cf) {
    var factory = new ConcurrentKafkaListenerContainerFactory<String, CalcResult>();
    factory.setConsumerFactory(cf);
    var container = factory.createContainer(replyTopic);
    container.getContainerProperties().setGroupId("rest-replies-group");
    return container;
  }

  @Bean
  public ReplyingKafkaTemplate<String, CalcRequest, CalcResult> replyingKafkaTemplate(
      ProducerFactory<String, CalcRequest> pf,
      ConcurrentMessageListenerContainer<String, CalcResult> repliesContainer) {
    return new ReplyingKafkaTemplate<>(pf, repliesContainer);
  }
}

package com.example.calculator.config;

import com.example.calculator.model.CalcRequest;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ListenerFactoryConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrap;

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, CalcRequest> kafkaListenerContainerFactory(
      KafkaTemplate<Object, Object> kafkaTemplate
  ) {
    Map<String, Object> props = new HashMap<>();
    // 🔑 BOOTSTRAP SERVERS OBRIGATÓRIO
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

    JsonDeserializer<CalcRequest> valueDeserializer = new JsonDeserializer<>(CalcRequest.class);
    valueDeserializer.addTrustedPackages("*");
    valueDeserializer.ignoreTypeHeaders();

    var consumerFactory = new DefaultKafkaConsumerFactory<>(
        props, new StringDeserializer(), valueDeserializer
    );

    var factory = new ConcurrentKafkaListenerContainerFactory<String, CalcRequest>();
    factory.setConsumerFactory(consumerFactory);
    // reply template para suportar @SendTo
    factory.setReplyTemplate(kafkaTemplate);
    return factory;
  }
}

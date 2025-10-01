package com.example.calculator.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrap;

  private Map<String, Object> baseProps() {
    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    // evita headers de tipo no JsonSerializer (compatibilidade)
    props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
    return props;
  }

  /** ProducerFactory genérico (Object,Object) */
  @Bean
  public ProducerFactory<Object, Object> genericProducerFactory() {
    return new DefaultKafkaProducerFactory<>(baseProps());
  }

  /** KafkaTemplate genérico (primário) — o @SendTo vai encontrar este bean. */
  @Bean
  @Primary
  public KafkaTemplate<Object, Object> kafkaTemplate(ProducerFactory<Object, Object> pf) {
    return new KafkaTemplate<>(pf);
  }
}

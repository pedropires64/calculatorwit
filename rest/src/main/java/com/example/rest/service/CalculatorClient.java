package com.example.rest.service;

import com.example.rest.model.CalcRequest;
import com.example.rest.model.CalcResult;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Service
public class CalculatorClient {

  private final ReplyingKafkaTemplate<String, CalcRequest, CalcResult> template;
  private final String requestTopic;
  private final String replyTopic;
  private final long timeoutMs;

  public CalculatorClient(ReplyingKafkaTemplate<String, CalcRequest, CalcResult> template,
                          @Value("${app.kafka.request-topic:calc.requests}") String requestTopic,
                          @Value("${app.kafka.reply-topic:calc.replies}") String replyTopic,
                          @Value("${app.kafka.reply-timeout-ms:5000}") long timeoutMs) {
    this.template = template; this.requestTopic = requestTopic; this.replyTopic = replyTopic; this.timeoutMs = timeoutMs;
  }

  public CalcResult calculate(CalcRequest req) throws Exception {
    ProducerRecord<String, CalcRequest> record = new ProducerRecord<>(requestTopic, req);
    record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, replyTopic.getBytes(StandardCharsets.UTF_8)));
    record.headers().add(new RecordHeader(KafkaHeaders.CORRELATION_ID, req.getRequestId().getBytes(StandardCharsets.UTF_8)));
    RequestReplyFuture<String, CalcRequest, CalcResult> future = template.sendAndReceive(record);
    return future.get(timeoutMs, TimeUnit.MILLISECONDS).value();
  }
}

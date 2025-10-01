package com.example.calculator.kafka;

import com.example.calculator.model.*;
import com.example.calculator.service.CalculationService;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CalcRequestListener {

  private final CalculationService calc;
  @Value("${app.mdc.key:requestId}") private String mdcKey;

  public CalcRequestListener(CalculationService calc) { this.calc = calc; }

  @KafkaListener(
      topics = "${app.kafka.request-topic:calc.requests}",
      groupId = "calculator-group",
      containerFactory = "kafkaListenerContainerFactory"
  )
  @SendTo("${app.kafka.reply-topic:calc.replies}")
  public CalcResult onMessage(CalcRequest req) {
    try {
      if (req.getRequestId()!=null) MDC.put(mdcKey, req.getRequestId());
      BigDecimal res;
      switch (req.getOperation()) {
        case SUM -> res = calc.sum(req.getA(), req.getB());
        case SUB -> res = calc.sub(req.getA(), req.getB());
        case MUL -> res = calc.mul(req.getA(), req.getB());
        case DIV -> res = calc.div(req.getA(), req.getB());
        default -> throw new IllegalArgumentException("Unknown operation");
      }
      return new CalcResult(req.getRequestId(), res, null);
    } catch (ArithmeticException ae) {
      return new CalcResult(req.getRequestId(), null, ae.getMessage());
    } catch (Exception e) {
      return new CalcResult(req.getRequestId(), null, "Internal error");
    } finally { MDC.clear(); }
  }
}

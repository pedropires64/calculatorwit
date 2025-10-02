package com.example.calculator.service;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CalculationServiceTest {

  private CalculationService service;

  @BeforeEach
  void setUp() {
    // escala default 18, como em application.properties
    service = new CalculationService(18);
  }

  @Test
  void sum_shouldAddWithScale() {
    BigDecimal a = new BigDecimal("1.234567890123456789");
    BigDecimal b = new BigDecimal("2");
    BigDecimal res = service.sum(a, b);
    assertEquals("3.234567890123456789", res.toPlainString());
  }

  @Test
  void div_shouldDivideWithScale() {
    BigDecimal a = new BigDecimal("10");
    BigDecimal b = new BigDecimal("3");
    BigDecimal res = service.div(a, b);
    assertEquals("3.333333333333333333", res.toPlainString());
  }

  @Test
  void div_shouldThrowOnDivisionByZero() {
    BigDecimal a = new BigDecimal("10");
    BigDecimal b = new BigDecimal("0");
    assertThrows(ArithmeticException.class, () -> service.div(a, b));
  }
}

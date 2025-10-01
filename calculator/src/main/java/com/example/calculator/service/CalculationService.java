package com.example.calculator.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CalculationService {
  private final int scale;
  public CalculationService(@Value("${app.decimal.scale:18}") int scale) { this.scale = scale; }
  public BigDecimal sum(BigDecimal a, BigDecimal b){ return a.add(b).setScale(scale, RoundingMode.HALF_UP); }
  public BigDecimal sub(BigDecimal a, BigDecimal b){ return a.subtract(b).setScale(scale, RoundingMode.HALF_UP); }
  public BigDecimal mul(BigDecimal a, BigDecimal b){ return a.multiply(b).setScale(scale, RoundingMode.HALF_UP); }
  public BigDecimal div(BigDecimal a, BigDecimal b){
    if (b.compareTo(BigDecimal.ZERO)==0) throw new ArithmeticException("Division by zero");
    return a.divide(b, scale, RoundingMode.HALF_UP);
  }
}

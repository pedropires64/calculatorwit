package com.example.rest.model;

import java.math.BigDecimal;

public class CalcRequest {
  private String requestId;
  private Operation operation;
  private BigDecimal a;
  private BigDecimal b;

  public CalcRequest() {}
  public CalcRequest(String requestId, Operation operation, BigDecimal a, BigDecimal b) {
    this.requestId = requestId; this.operation = operation; this.a = a; this.b = b;
  }

  public String getRequestId(){return requestId;} public void setRequestId(String v){this.requestId=v;}
  public Operation getOperation(){return operation;} public void setOperation(Operation v){this.operation=v;}
  public BigDecimal getA(){return a;} public void setA(BigDecimal v){this.a=v;}
  public BigDecimal getB(){return b;} public void setB(BigDecimal v){this.b=v;}
}

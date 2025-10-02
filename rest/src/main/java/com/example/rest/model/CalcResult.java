package com.example.rest.model;

import java.math.BigDecimal;

public class CalcResult {
  private String requestId;
  private BigDecimal result;
  private String error;

  public CalcResult() {}
  public CalcResult(String requestId, BigDecimal result, String error){
    this.requestId=requestId; this.result=result; this.error=error;
  }

  public String getRequestId(){return requestId;} public void setRequestId(String v){this.requestId=v;}
  public BigDecimal getResult(){return result;} public void setResult(BigDecimal v){this.result=v;}
  public String getError(){return error;} public void setError(String v){this.error=v;}
}

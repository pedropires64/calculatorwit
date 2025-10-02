package com.example.rest.model;

public class ApiResponse {
  private final String operation;
  private final String a;
  private final String b;
  private final String result;
  private final String requestId;

  public ApiResponse(String operation, String a, String b, String result, String requestId){
    this.operation=operation; this.a=a; this.b=b; this.result=result; this.requestId=requestId;
  }

  public String getOperation(){return operation;}
  public String getA(){return a;}
  public String getB(){return b;}
  public String getResult(){return result;}
  public String getRequestId(){return requestId;}
}

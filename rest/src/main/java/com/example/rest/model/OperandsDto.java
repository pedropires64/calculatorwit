package com.example.rest.model;

import jakarta.validation.constraints.NotBlank;

public class OperandsDto {
  @NotBlank
  private String a;
  @NotBlank
  private String b;

  public String getA(){return a;} public void setA(String v){this.a=v;}
  public String getB(){return b;} public void setB(String v){this.b=v;}
}

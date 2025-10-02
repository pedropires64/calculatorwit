package com.example.rest.web;

import com.example.rest.model.CalcRequest;
import com.example.rest.model.CalcResult;
import com.example.rest.model.Operation;
import com.example.rest.service.CalculatorClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CalcController.class)
class CalcControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private CalculatorClient calculatorClient;

  @Test
  void sum_shouldReturnOkResponse() throws Exception {
    // mock do reply vindo do Kafka
    Mockito.when(calculatorClient.calculate(any(CalcRequest.class)))
        .thenReturn(new CalcResult("rid-1", new BigDecimal("3.234567890123456789"), null));

    String json = """
      {"a":"1.234567890123456789","b":"2"}
      """;

    mockMvc.perform(post("/api/v1/calc/sum")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.operation").value("sum"))
        .andExpect(jsonPath("$.result").value("3.234567890123456789"));
  }

  @Test
  void sum_shouldValidateNotBlank() throws Exception {
    String json = """
      {"a":"","b":"2"}
      """;

    mockMvc.perform(post("/api/v1/calc/sum")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").exists());
  }

  @Test
  void sum_shouldReturnBadRequestOnInvalidNumber() throws Exception {
    String json = """
      {"a":"abc","b":"2"}
      """;

    mockMvc.perform(post("/api/v1/calc/sum")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Invalid number format"));
  }

  @Test
  void div_shouldMapDivisionByZeroAs400() throws Exception {
    // mock erro vindo do calculator
    Mockito.when(calculatorClient.calculate(any(CalcRequest.class)))
        .thenReturn(new CalcResult("rid-2", null, "Division by zero"));

    String json = """
      {"a":"10","b":"0"}
      """;

    mockMvc.perform(post("/api/v1/calc/div")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Division by zero"));
  }
}

package com.example.rest.web;

import com.example.rest.config.RequestIdFilter;
import com.example.rest.model.*;
import com.example.rest.service.CalculatorClient;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/calc")
public class CalcController {

  private final CalculatorClient client;
  public CalcController(CalculatorClient client){ this.client = client; }

  @PostMapping("/sum")
  public ResponseEntity<?> sum(@Valid @RequestBody OperandsDto body, HttpServletRequest http){
    return handle(Operation.SUM, body, http);
  }

  @PostMapping("/sub")
  public ResponseEntity<?> sub(@Valid @RequestBody OperandsDto body, HttpServletRequest http){
    return handle(Operation.SUB, body, http);
  }

  @PostMapping("/mul")
  public ResponseEntity<?> mul(@Valid @RequestBody OperandsDto body, HttpServletRequest http){
    return handle(Operation.MUL, body, http);
  }

  @PostMapping("/div")
  public ResponseEntity<?> div(@Valid @RequestBody OperandsDto body, HttpServletRequest http){
    return handle(Operation.DIV, body, http);
  }

  private ResponseEntity<?> handle(Operation op, OperandsDto body, HttpServletRequest http){
    try {
      String requestId = (String) http.getAttribute(RequestIdFilter.MDC_KEY);
      BigDecimal a = new BigDecimal(body.getA());
      BigDecimal b = new BigDecimal(body.getB());
      CalcRequest req = new CalcRequest(requestId, op, a, b);
      CalcResult res = client.calculate(req);

      if (res.getError() != null) {
        return ResponseEntity.badRequest().body(new ApiError(res.getError()));
      }
      return ResponseEntity.ok(new ApiResponse(op.name().toLowerCase(), body.getA(), body.getB(),
          res.getResult() == null ? null : res.getResult().toPlainString(), requestId));
    } catch (NumberFormatException nfe) {
      return ResponseEntity.badRequest().body(new ApiError("Invalid number format"));
    } catch (Exception e) {
      return ResponseEntity.status(504).body(new ApiError("Upstream timeout"));
    }
  }
}

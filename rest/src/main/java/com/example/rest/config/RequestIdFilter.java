package com.example.rest.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class RequestIdFilter implements Filter {
  public static final String MDC_KEY = "requestId";

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    String requestId = UUID.randomUUID().toString();
    MDC.put(MDC_KEY, requestId);
    if (response instanceof HttpServletResponse resp) {
      resp.setHeader("X-Request-ID", requestId);
    }
    request.setAttribute(MDC_KEY, requestId);
    try { chain.doFilter(request, response); }
    finally { MDC.clear(); }
  }
}

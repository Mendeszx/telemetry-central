package com.lib.telemetry_central.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoggingContextFilter implements Filter {

    @Value("${request.requestId}")
    private String requestIdHeader;

    @Value("${request.clientId}")
    private String clientIdHeader;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        try {

            String requestId = request.getHeader(requestIdHeader);
            if (requestId == null) {
                requestId = "UNKNOWN";
            }

            String clientId = request.getHeader(clientIdHeader);
            if (clientId == null) {
                clientId = "UNKNOWN";
            }

            MDC.put("requestId", requestId);
            MDC.put("clientId", clientId);

            filterChain.doFilter(servletRequest, servletResponse);

        } finally {

            MDC.clear();
        }
    }
}

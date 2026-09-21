package com.platform.deck.card.service.util;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UserIdMdcFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String userId = ((HttpServletRequest) request).getHeader("X-User-Id");
        if (userId != null) {
            MDC.put("userId", userId);
        }
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove("userId"); // always clean up
        }
    }
}


package com.ssportal.be.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Set;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(RequestFilter.class);

    @Value("${ALLOW_ORIGINS:*}")
    String allowOrigins;

    Set<String> allowOriginsSet;

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;

        if (allowOriginsSet == null) {
            allowOriginsSet = new HashSet<>();
            String[] domains = allowOrigins.split(",");
            for (String domain : domains) {
                allowOriginsSet.add(domain);
            }
        }

        String originHeader = request.getHeader("Origin");
        if (allowOriginsSet.contains("*")) {
            response.setHeader("Access-Control-Allow-Origin", "*");
        } else if (originHeader != null && allowOriginsSet.contains(originHeader)) {
            response.setHeader("Access-Control-Allow-Origin", originHeader);
        }
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with, x-auth-token");
        response.setHeader("Access-Control-Max-Age", "3600");
        if (!allowOriginsSet.contains("*")) {
            response.setHeader("Access-Control-Allow-Credentials", "true");
        }

        if (!(request.getMethod().equalsIgnoreCase("OPTIONS"))) {
            try {
                chain.doFilter(req, res);
            } catch (Exception e) {
                LOG.warn(e.getMessage());
            }
        } else {
//            System.out.println("Pre-flight");
            response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "authorization, content-type, x-auth-token, " +
                    "access-control-request-headers,access-control-request-method,accept,origin,authorization,x-requested-with");
            response.setStatus(HttpServletResponse.SC_OK);
        }

    }

    public void init(FilterConfig filterConfig) {
    }

    public void destroy() {
    }

}

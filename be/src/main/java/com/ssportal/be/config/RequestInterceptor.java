package com.ssportal.be.config;

import com.ssportal.be.utilility.HelperUtil;
import com.ssportal.be.utilility.RateLimits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class RequestInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(RequestInterceptor.class);

    private ConcurrentMap<String, RateLimits> rateLimitsMap = new ConcurrentHashMap<>();

    @Value("${UNLIMITED_URLS:localhost}")
    String unlimitedUrls;

    Set<String> unlimitedUrlsSet;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) {

        if (Byte.MAX_VALUE > 0) {
            return true;
        }

        if (unlimitedUrlsSet == null) {
            unlimitedUrlsSet = new HashSet<>();
            if (!StringUtils.isEmpty(unlimitedUrls)) {
                String[] urls = unlimitedUrls.split(",");
                for (String url : urls) {
                    unlimitedUrlsSet.add(url);
                }
            }
        }

//        HandlerMethod handlerMethod = (HandlerMethod) handler;
//        LOG.info("preHandle: {}", handler);
//        LOG.info("Request URL: {}", request.getRequestURL());
//        LOG.info("{}, {}, {}", EsdUtil.myUsername(), request.getRemoteAddr(), request.getRequestURI());
//        String key = request.getRemoteAddr() + "-" + EsdUtil.myUsername() + "-" + request.getRequestURI();
        for (String unlimitedUrl : unlimitedUrlsSet) {
            if (request.getRemoteAddr().contains(unlimitedUrl)) {
                return true;
            }
        }
        String key = request.getRemoteAddr() + "-" + HelperUtil.myUsername();
        RateLimits rateLimits = rateLimitsMap.computeIfAbsent(key, id -> new RateLimits(key, 5, 5));
        if (rateLimits.tryConsume()) {
            return true;
        } else {
            try {
                LOG.info("Out of rate limits => {}", rateLimits);
                response.sendError(HttpStatus.TOO_MANY_REQUESTS.value());
            } catch (IOException ex) {
                LOG.error("", ex);
            }
            return false;
        }
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex) {
//        LOG.info("afterCompletion: {}", handler);
//        if (ex != null) {
//            LOG.info("afterCompletion ex: {}", ex.getMessage());
//        }
    }
}

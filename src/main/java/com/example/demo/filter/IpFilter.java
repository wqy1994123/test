package com.example.demo.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
/*
    解决恶意访问
 */
@WebFilter(urlPatterns = "/bucket")
public class IpFilter implements Filter {

    public final static int MAX = 30;

    @Resource
    RedisTemplate<String, Integer> redisTemplate;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String ip = request.getRemoteAddr();

        Integer count = redisTemplate.opsForValue().get(ip);

        if (count == null) {
            redisTemplate.opsForValue().set(ip, 1, 5, TimeUnit.SECONDS);

        } else if (count < MAX) {
            redisTemplate.opsForValue().set(ip, count+1, 5, TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().set(ip, count, 5, TimeUnit.SECONDS);
            response.getWriter().write("您的请求太频繁了，请稍后重试");
        }

        filterChain.doFilter(request, response);
    }
}

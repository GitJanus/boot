package com.example.boot.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;

public class BaseFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
       System.out.println("filter start...");
       HttpServletRequest req = (HttpServletRequest) servletRequest;
       X509Certificate[] certs = (X509Certificate[]) req.getAttribute("javax.servlet.request.X509Certificate");
       System.out.println(Arrays.toString(certs));
    }

    @Override
    public void init(FilterConfig config) {
    }

    @Override
    public void destroy() {
    }
}

package com.rrs.log.filter;

import com.rrs.log.properties.TraceProperties;
import com.rrs.log.util.MDCTraceUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 过滤器 接收上游传递过来的traceid
 */
@ConditionalOnClass(value = {HttpServletRequest.class, OncePerRequestFilter.class})
@Order(value = MDCTraceUtils.FILTER_ORDER)
public class WebTraceFilter extends OncePerRequestFilter {

    @Resource
    private TraceProperties traceProperties;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !traceProperties.getEnable();
    }

    //接收traceid 设置到mdc中
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
            String traceId=httpServletRequest.getHeader(MDCTraceUtils.TRACE_ID_HEADER);
            if(StringUtils.isEmpty(traceId)){
                MDCTraceUtils.addTraceId();
            }else {
                MDCTraceUtils.putTraceId(traceId);
            }
            filterChain.doFilter(httpServletRequest,httpServletResponse);
        }finally {
            //这里为什么要remove呢?
            MDCTraceUtils.removeTraceId();
        }
    }
}

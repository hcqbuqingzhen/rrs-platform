package com.rrs.common.ribbon.filter;

import cn.hutool.core.util.StrUtil;
import com.rrs.common.core.constants.CommonConstant;
import com.rrs.common.core.context.LbIsolationContextHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 负载均衡隔离规则过滤器 
 */
public class LbIsolationFilter extends OncePerRequestFilter {
    @Value("${xsyw.ribbon.isolation.enabled:false}")
    private boolean enableIsolation;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !enableIsolation;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        try {
            String version = request.getHeader(CommonConstant.Z_L_T_VERSION);
            if(StrUtil.isNotEmpty(version)){
                LbIsolationContextHolder.setVersion(version);
            }

            filterChain.doFilter(request, response);
        } finally {
            LbIsolationContextHolder.clear();
        }
    }
}

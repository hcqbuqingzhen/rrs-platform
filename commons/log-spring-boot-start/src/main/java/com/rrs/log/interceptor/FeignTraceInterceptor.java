package com.rrs.log.interceptor;

import com.rrs.log.properties.TraceProperties;
import com.rrs.log.util.MDCTraceUtils;
import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * 服务之间调用 拦截增加traceid
 */
@ConditionalOnClass(value = {RequestInterceptor.class})
public class FeignTraceInterceptor {
    @Resource
    private TraceProperties traceProperties;

    @Bean
    public RequestInterceptor feignTraceInterceptor() {
        return template -> {
            if (traceProperties.getEnable()) {
                //传递日志traceId
                String traceId = MDCTraceUtils.getTraceId();
                if (!StringUtils.isEmpty(traceId)) {
                    template.header(MDCTraceUtils.TRACE_ID_HEADER, traceId);
                }
            }
        };
    }
}

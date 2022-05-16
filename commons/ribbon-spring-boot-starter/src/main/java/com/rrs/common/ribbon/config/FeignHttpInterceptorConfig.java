package com.rrs.common.ribbon.config;

import com.rrs.common.core.constants.CommonConstant;
import com.rrs.common.core.constants.SecurityConstants;
import com.rrs.common.core.context.UserInfoContextHolder;
import com.rrs.common.core.model.CommonUser;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 过滤器服务之间调用时将用户信息放入请求头
 */
public class FeignHttpInterceptorConfig {
    protected List<String> requestHeaders = new ArrayList<>();

    @PostConstruct
    public void initialize() {
        requestHeaders.add(SecurityConstants.USER_ID_HEADER);
        requestHeaders.add(SecurityConstants.USER_HEADER);
        requestHeaders.add(SecurityConstants.ROLE_HEADER);
        requestHeaders.add(SecurityConstants.ACCOUNT_TYPE_HEADER);
        requestHeaders.add(CommonConstant.Z_L_T_VERSION);
    }

    /**
     * 使用feign client访问别的微服务时，将上游传过来的access_token、username、roles等信息放入header传递给下一个服务
     */
    @Bean
    public RequestInterceptor httpFeignInterceptor() {
        return template -> {
            //先从线程上下文中获取,如果没有,再从头中获取.是因为服务在网关解析后存放到请求头,当一次调用后,到达另一个服务后,信息就只能用一次.
            //到达另一个服务后 可以存到ThreadLocal,然后再走此拦截器发送到下一个.
            CommonUser commonUser=null;
            if((commonUser=UserInfoContextHolder.getUserInfo())!=null){
                Long userid = commonUser.getId();
                String username = commonUser.getUsername();
                template.header("user_id", String.valueOf(userid));
                template.header("username",username);
                return;
            }
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                Enumeration<String> headerNames = request.getHeaderNames();
                if (headerNames != null) {
                    String headerName;
                    String headerValue;
                    while(headerNames.hasMoreElements()) {
                        headerName = headerNames.nextElement();
                        if (requestHeaders.contains(headerName)) {
                            headerValue = request.getHeader(headerName);
                            template.header(headerName, headerValue);
                        }
                    }
                }
                //传递access_token，无网络隔离时需要传递
                /*
                String token = extractHeaderToken(request);
                if (StrUtil.isEmpty(token)) {
                    token = request.getParameter(CommonConstant.ACCESS_TOKEN);
                }
                if (StrUtil.isNotEmpty(token)) {
                    template.header(CommonConstant.TOKEN_HEADER, CommonConstant.BEARER_TYPE + " " + token);
                }
                */
            }
        };
    }

    /**
     * 解析head中的token
     * @param request
     */
    private String extractHeaderToken(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaders(CommonConstant.TOKEN_HEADER);
        while (headers.hasMoreElements()) {
            String value = headers.nextElement();
            if ((value.toLowerCase().startsWith(CommonConstant.BEARER_TYPE.toLowerCase()))) {
                String authHeaderValue = value.substring(CommonConstant.BEARER_TYPE.length()).trim();
                int commaIndex = authHeaderValue.indexOf(',');
                if (commaIndex > 0) {
                    authHeaderValue = authHeaderValue.substring(0, commaIndex);
                }
                return authHeaderValue;
            }
        }
        return null;
    }
}

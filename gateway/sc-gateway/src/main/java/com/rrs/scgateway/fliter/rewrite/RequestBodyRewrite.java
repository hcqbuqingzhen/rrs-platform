package com.rrs.scgateway.fliter.rewrite;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rrs.scgateway.exception.CustomizeInfoException;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.util.Map;


/**
 * 1.RewriteFunction的实现，用于传入ModifyRequestBody过滤器配置
 *   用于处理修改请求体的逻辑在这个类的方法中
 * 2.此类apply返回的ResponseStatusException异常会被传递到spring的异常处理类 DefaultErrorWebExceptionHandler 中
 * 3.异常信息会在DefaultErrorWebExceptionHandler解析返回。
 *
 * 21-21-18新增自定义异常信息类，返回定制CustomizeInfoException异常信息。
 * CustomizeInfoException的信息，会被自定义的CustomizeErrorWebFluxAutoConfiguration 处理后返回。
 *
 */
@Slf4j
public class RequestBodyRewrite implements RewriteFunction<String, String> {

    private ObjectMapper objectMapper;

    public RequestBodyRewrite(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 根据用户ID获取用户名称的方法，可以按实际情况来内部实现，例如查库或缓存，或者远程调用
     * @param userId
     * @return
     */
    private  String mockUserName(int userId) {
        return "user-" + userId;
    }

    @Override
    public Publisher<String> apply(ServerWebExchange exchange, String body) {
        try {
            Map<String, Object> map = objectMapper.readValue(body, Map.class);
            //添加一个判断 返回给前端异常信息
            if(!map.containsKey("user-id")){

                CustomizeInfoException customizeInfoException = new CustomizeInfoException();
                // 这里返回406，您可以按照业务需要自行调整
                customizeInfoException.setHttpStatus(HttpStatus.NOT_ACCEPTABLE);
                // 这里按照业务需要自行设置code
                customizeInfoException.setCode("010020003");

                // 这里按照业务需要自行设置返回的message
                customizeInfoException.setMessage("请确保请求参数中的user-id字段是有效的");

                return Mono.error(customizeInfoException);

                //return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,"参数中无user-id字段"));

            }
            // 取得id
            int userId = (Integer)map.get("user-id");

            // 得到nanme后写入map
            map.put("user-name", mockUserName(userId));

            // 添加一个key/value
            map.put("gateway-request-tag", userId + "-" + System.currentTimeMillis());

            return Mono.just(objectMapper.writeValueAsString(map));
        } catch (Exception ex) {
            log.error("1. json process fail", ex);
            // json操作出现异常时的处理
            return Mono.error(new Exception("1. json process fail", ex));
        }
    }
}

package com.rrs.uaa.sa.api.fallback;

import com.rrs.uaa.sa.api.UserFeignClient;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author hcq
 */
@Component
@Slf4j
public class UserFeignFallbackFactory implements FallbackFactory<UserFeignClient> {
    @Override
    public UserFeignClient create(Throwable throwable) {
        return new UserFeignClient() {


        };
    }
}

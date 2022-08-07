package com.rrs.uaa.sa.api;

import com.rrs.common.core.constants.ServiceNameConstants;
import com.rrs.uaa.sa.api.fallback.UserFeignFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author hcq
 */
@FeignClient(value = ServiceNameConstants.USER_SERVICE,fallbackFactory = UserFeignFallbackFactory.class)
public interface UserFeignClient {

}

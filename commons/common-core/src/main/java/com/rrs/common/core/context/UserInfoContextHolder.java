package com.rrs.common.core.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.rrs.common.core.model.CommonUser;

/**
 * 传递用户信息
 */
public class UserInfoContextHolder {
    /**
     * 支持父子线程之间的数据传递
     */
    private static final ThreadLocal<CommonUser> USER_INFO_CONTEXT = new TransmittableThreadLocal<>();

    public static void setUserInfo(CommonUser userInfo) {
        USER_INFO_CONTEXT.set(userInfo);
    }

    public static CommonUser getUserInfo() {
        return USER_INFO_CONTEXT.get();
    }

    public static void clear() {
        USER_INFO_CONTEXT.remove();
    }
}

/*
 * Copyright (c) 2019-2029. 恩梯梯数据（中国）信息技术有限公司. All Rights Reserved.
 */

package com.rrs.uaa.sa.feign;

import org.springframework.web.bind.annotation.RestController;
import com.rrs.uaa.sa.api.feign.ITestFeign;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
/**
 *  的Feign接口提供者trim
 *
 * @author NCIT
 * @date 2022/08/07 23:35:17
 */
@Slf4j
@RestController
@Api(tags = " ")
public class TestProvider implements ITestFeign {


    /**
     * TODO 请书写注释
     *
     * @return
     */
    @ApiOperation(value = "")
    @Override
    public String testLogin(String username,String password) {
        // 此处仅作模拟示例，真实项目需要从数据库中查询数据进行比对
        if("zhang".equals(username) && "123456".equals(password)) {
            StpUtil.login(10001);
            return "登录成功";
        }
        return "登录失败";
    }

}

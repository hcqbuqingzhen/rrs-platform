package com.rrs.uaa.sa.param.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hcq
 * 登陆参数
 */
@Data
@ApiModel("用户登陆请求信息")
public class LoginParam {
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("用户密码")
    private  String password;
    @ApiModelProperty("用户手机号")
    private String phone;
    @ApiModelProperty("用户类型")
    private String userType;
    @ApiModelProperty("用户登陆方式")
    private String loginType;
}

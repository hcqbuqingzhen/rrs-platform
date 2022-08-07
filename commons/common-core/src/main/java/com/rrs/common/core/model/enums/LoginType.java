package com.rrs.common.core.model.enums;

/**
 * 用户登陆方式 验证码,密码,二维码. 等验证码二维码等.
 * @author hcq
 */
public enum LoginType {
    /**
     * 客户端用户
     */
    PASSWORD("password","密码登陆"),
    /**
     * 后台用户
     */
    SMS("sms","验证码登陆"),
    ;

    String type;
    String des;

    LoginType(String type,String des){
        this.type=type;
        this.des=des;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}

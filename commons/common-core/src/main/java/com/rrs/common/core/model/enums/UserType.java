package com.rrs.common.core.model.enums;

/**
 * @author hcq
 * 用户类型
 */
public enum UserType {
    /**
     * 客户端用户
     */
    APP("app","客户端类型用户"),
    /**
     * 后台用户
     */
    BACK("back","后台管理用户"),
    ;

    String type;
    String des;

    UserType(String type,String des){
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

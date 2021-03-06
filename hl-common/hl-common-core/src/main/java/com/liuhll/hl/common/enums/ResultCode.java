package com.liuhll.hl.common.enums;

import lombok.Getter;

@Getter
public enum ResultCode {
    Ok(0,"成功"),
    UnAuthentication(1,"未认证"),
    UnAuthorized(2,"未授权"),
    BusinessError(3,"业务异常"),
    PlatformException(4,"框架异常"),
    FriendlyTips(5,""),
    ClentForbidden(6,"无权访问该应用"),
    NonExistentClent(7,"不存在该应用"),
    UnValid(8,"验证未通过"),
    UserInvalid(9,"账号未被激活"),
    UserLocked(10,"账号被锁定")
    ;

    private int code;
    private String memo;

    ResultCode(int code, String memo){
        this.code = code;
        this.memo = memo;

    }

    @Override
    public String toString() {
        return memo;
    }
}


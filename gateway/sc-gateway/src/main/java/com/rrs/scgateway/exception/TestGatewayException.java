package com.rrs.scgateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.BAD_REQUEST,reason = "user-id不能为空")
public class TestGatewayException extends Exception{

}

package com.rrs.hello.controller;

import com.rrs.common.core.constants.Constants;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RestController
public class UserInfoController {
    private String dateStr(){
        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
    }

    @GetMapping("/userinfo")
    public String userInfo(@RequestParam("username") String username) {
        return Constants.HELLO_PREFIX + " " + username + ", " + dateStr();
    }
    @PostMapping("/change")
    public Map<String, Object> change(@RequestBody Map<String, Object> map) {
        map.put("response-tag", dateStr());
        return map;
    }
}

package com.rrs.common.core.utils;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class JsonUtils {
    private final static ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 获取list ,从string.
     * @param cls
     * @param string json字符串
     * @return
     */
    public static <T> List<T> getList(String string,Class<T> cls){
        if(StringUtils.isBlank(string)){
            return null;
        }
        try{
            JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, cls);
            return MAPPER.readValue(string, javaType);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}

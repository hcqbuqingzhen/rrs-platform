package com.rrs.log.config;

import com.rrs.log.properties.AuditLogProperties;
import com.rrs.log.properties.LogDbProperties;
import com.rrs.log.properties.TraceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({TraceProperties.class, AuditLogProperties.class, LogDbProperties.class})
public class LogAutoConfigure {
    //可以在这配置
}

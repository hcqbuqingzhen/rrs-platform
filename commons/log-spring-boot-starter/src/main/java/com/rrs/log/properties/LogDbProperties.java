package com.rrs.log.properties;

import com.zaxxer.hikari.HikariConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 日志数据源配置
 * logType=db时生效(非必须)，如果不配置则使用当前数据源
 * 我认为一般要配置,毕竟不希望每个fuwu dou jia yige xin de biao .
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "rrs.audit-log.datasource")
public class LogDbProperties extends HikariConfig {
    /**
     * jdbc url
     * 审计日志需要的配置
     */
    private volatile String jdbcUrl ;
    /**
     * name
     */
    private volatile String username;
    /**
     * password
     */
    private volatile String password;
    /**
     * qudong
     */
    private String driverClassName;
}

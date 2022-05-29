package com.rrs.log.service.impl;

import com.rrs.log.model.Audit;
import com.rrs.log.service.IAuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 设计日志入es库实现类
 */
@Slf4j
@ConditionalOnProperty(name = "rrs.audit-log.log-type", havingValue = "es")
@ConditionalOnClass(JdbcTemplate.class)
public class EsAudioServiceImpl implements IAuditService {
    @Override
    public void save(Audit audit) {

    }
}

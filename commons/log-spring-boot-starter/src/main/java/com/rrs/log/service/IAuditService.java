package com.rrs.log.service;

import com.rrs.log.model.Audit;

/**
 * 审计日志接口
 *
 */
public interface IAuditService {
    void save(Audit audit);
}

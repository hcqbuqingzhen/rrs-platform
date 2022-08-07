package com.rrs.log.aspect;

import com.rrs.log.annotation.AuditLog;
import com.rrs.log.model.Audit;
import com.rrs.log.properties.AuditLogProperties;
import com.rrs.log.service.IAuditService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * 审计日志切面
 */
@Slf4j
@Aspect
@ConditionalOnClass({HttpServletRequest.class, RequestContextHolder.class})
public class AuditLogAspect {
    @Value("${spring.application.name}")
    private String applicationName;

    private AuditLogProperties auditLogProperties;
    private IAuditService auditService;
    public AuditLogAspect(AuditLogProperties auditLogProperties, IAuditService auditService) {
        this.auditLogProperties = auditLogProperties;
        this.auditService = auditService;
    }

    /**
     * 用于SpEL表达式解析.
     */
    private SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
    /**
     * 用于获取方法参数定义名字.
     */
    private DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    //这里使用@within是可以通过aop拦截类上的注解。而@annotation是通过aop拦截方法上的注解。
    @Before("@within(auditLog) || @annotation(auditLog)")
    public void beforeMethod(JoinPoint joinPoint, AuditLog auditLog) {
        //判断功能是否开启
        if (auditLogProperties.getEnabled()) {
            if (auditService == null) {
                log.warn("AuditLogAspect - auditService is null");
                return;
            }
            if (auditLog == null) {
                // 获取类上的注解
                auditLog = joinPoint.getTarget().getClass().getDeclaredAnnotation(AuditLog.class);
            }
            Audit audit = getAudit(auditLog, joinPoint);
            auditService.save(audit);
        }
    }


    /**
     * 解析spEL表达式
     */
    private String getValBySpEL(String spEL, MethodSignature methodSignature, Object[] args) {
        //获取方法形参名数组
        String[] paramNames = nameDiscoverer.getParameterNames(methodSignature.getMethod());
        if (paramNames != null && paramNames.length > 0) {
            Expression expression = spelExpressionParser.parseExpression(spEL);
            // spring的表达式上下文对象
            EvaluationContext context = new StandardEvaluationContext();
            // 给上下文赋值
            for(int i = 0; i < args.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }
            return expression.getValue(context).toString();
        }
        return null;
    }


    /**
     * 构建审计对象
     */
    private Audit getAudit(AuditLog auditLog, JoinPoint joinPoint) {
        Audit audit = new Audit();
        audit.setTimestamp(LocalDateTime.now());
        audit.setApplicationName(applicationName);
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        audit.setClassName(methodSignature.getDeclaringTypeName());
        audit.setMethodName(methodSignature.getName());

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String userId = request.getHeader("x-userid-header");
        String userName = request.getHeader("x-user-header");
        String clientId = request.getHeader("x-tenant-header");
        audit.setUserId(userId);
        audit.setUserName(userName);
        audit.setClientId(clientId);

        String operation = auditLog.operation();
        //一般我们没有加# 里面就是自定义的操作描述.如果加了# 说明审计日志中需要包含方法参数.
        if (operation.contains("#")) {
            //获取方法的参数
            Object[] args = joinPoint.getArgs();
            //使用spel表达式构建字符串
            operation = getValBySpEL(operation, methodSignature, args);
        }
        audit.setOperation(operation);

        return audit;
    }
}

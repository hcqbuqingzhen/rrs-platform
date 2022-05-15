package org.txlcn.demo.servicea;

import com.codingapi.txlcn.common.util.Transactions;
import com.codingapi.txlcn.tc.annotation.DTXPropagation;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.codingapi.txlcn.tc.annotation.TccTransaction;
import com.codingapi.txlcn.tracing.TracingContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.txlcn.demo.common.db.domain.Demo;
import org.txlcn.demo.common.feign.ServiceBClient;
import org.txlcn.demo.common.feign.ServiceCClient;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

/**
 * Description:
 * Date: 2018/12/25
 *
 * @author ujued
 */
@Service
@Slf4j
public class DemoServiceImpl implements org.txlcn.demo.servicea.DemoService {
    @Resource
    private DemoMapper demoMapper;
    @Resource
    private ServiceBClient serviceBClient;
    @Resource
    private ServiceCClient serviceCClient;

    @LcnTransaction
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String execute(String value, String exFlag, String flag) {
        String dResp = serviceBClient.rpc(value);
        // step2. call remote ServiceB
        String eResp = serviceCClient.rpc(value);
        // step3. execute local transaction
        Demo demo = new Demo();
        demo.setGroupId(TracingContext.tracing().groupId());
        demo.setDemoField(value);
        demo.setCreateTime(new Date());
        demo.setAppName(Transactions.getApplicationId());
        demoMapper.save(demo);

        // 置异常标志，DTX 回滚
        if (Objects.nonNull(exFlag)) {
            throw new IllegalStateException("by exFlag");
        }

        return dResp + " > " + eResp + " > " + "ok-service-a";
    }

    @TccTransaction(confirmMethod = "confirmExecuteTcc",cancelMethod ="confirmExecuteTcc" )
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String executeTcc(String value, String exFlag, String flag) {
        //tcc模式下增加一个用于判断的字段，当try阶段是这个字段的含义是 增加中。
        String dResp = serviceBClient.rpcTcc(value);
        // step2. call remote ServiceB
        String eResp = serviceCClient.rpcTcc(value);
        // step3. execute local transaction
        Demo demo = new Demo();
        demo.setGroupId(TracingContext.tracing().groupId());
        demo.setDemoField(value);
        demo.setCreateTime(new Date());
        demo.setAppName(Transactions.getApplicationId());
        demo.setStatus(0);
        demoMapper.save(demo);

        // 置异常标志，DTX 回滚
        if (Objects.nonNull(exFlag)) {
            throw new IllegalStateException("by exFlag");
        }

        return dResp + " > " + eResp + " > " + "ok-service-a";
    }

    public void confirmExecuteTcc(String value, String exFlag, String flag){
        //确认方法 需要自己手动修改业务逻辑
        //我们确认字段增加成功
        String applicationId = Transactions.getApplicationId();
        demoMapper.updateById(value,applicationId);

    }

    public void cancelExecuteTcc(String value, String exFlag, String flag){
        //确认方法 需要自己手动修改业务逻辑
        //我们修改字段为增加失败

        String applicationId = Transactions.getApplicationId();
        demoMapper.deleteById(value,applicationId);
    }
}

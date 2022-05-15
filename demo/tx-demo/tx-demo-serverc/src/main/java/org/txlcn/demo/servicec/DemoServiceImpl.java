package org.txlcn.demo.servicec;

import com.codingapi.txlcn.common.util.Transactions;
import com.codingapi.txlcn.tc.annotation.DTXPropagation;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.codingapi.txlcn.tc.annotation.TccTransaction;
import com.codingapi.txlcn.tracing.TracingContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.txlcn.demo.common.db.domain.Demo;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Description:
 * Date: 2018/12/25
 *
 * @author ujued
 */
@Service
@Slf4j
public class DemoServiceImpl implements DemoService {
    @Resource
    private DemoMapper demoMapper;

    @Override
    @LcnTransaction
    @Transactional(rollbackFor = Exception.class)
    public String rpc(String value) {
        Demo demo = new Demo();
        demo.setDemoField(value);
        demo.setCreateTime(new Date());
        demo.setAppName(Transactions.getApplicationId());
        demo.setGroupId(TracingContext.tracing().groupId());
        demoMapper.save(demo);
        if ("456".equals(value)) {
            throw new IllegalStateException("by test");
        }
        return "ok-service-c";
    }

    @Override
    @TccTransaction(confirmMethod = "confirmRpcTcc",cancelMethod ="cancelRpcTcc" )
    @Transactional(rollbackFor = Exception.class)
    public String rpcTcc(String value) {
        Demo demo = new Demo();
        demo.setDemoField(value);
        demo.setCreateTime(new Date());
        demo.setAppName(Transactions.getApplicationId());
        demo.setGroupId(TracingContext.tracing().groupId());
        demo.setStatus(0);
        demoMapper.save(demo);
        if ("456".equals(value)) {
            throw new IllegalStateException("by test");
        }
        return "ok-service-c";
    }

    public void confirmRpcTcc(String value){
        //确认方法 需要自己手动修改业务逻辑
        //我们确认字段增加成功
        System.out.println("confirm 执行了");
        String applicationId = Transactions.getApplicationId();
        demoMapper.updateById(value,applicationId);
    }

    public void cancelRpcTcc(String value){
        //确认方法 需要自己手动修改业务逻辑
        //我们修改字段为增加失败
        System.out.println("cancel 执行了");
        String applicationId = Transactions.getApplicationId();
        demoMapper.deleteById(value,applicationId);
    }
}

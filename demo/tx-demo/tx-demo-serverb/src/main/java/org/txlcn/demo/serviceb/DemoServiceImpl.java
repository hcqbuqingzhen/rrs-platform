package org.txlcn.demo.serviceb;

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
import java.util.Objects;

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
        demo.setGroupId(TracingContext.tracing().groupId());
        demo.setDemoField(value);
        demo.setAppName(Transactions.getApplicationId());
        demo.setCreateTime(new Date());
        demoMapper.save(demo);
        return "ok-service-b";
    }

    @TccTransaction(confirmMethod = "confirmRpcTcc",cancelMethod ="cancelRpcTcc" )
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String rpcTcc(String value) {
        Demo demo = new Demo();
        demo.setGroupId(TracingContext.tracing().groupId());
        demo.setDemoField(value);
        demo.setAppName(Transactions.getApplicationId());
        demo.setCreateTime(new Date());
        demo.setStatus(0);
        demoMapper.save(demo);
        return "ok-service-b";
    }


    public void confirmRpcTcc(String value){
        //确认方法 需要自己手动修改业务逻辑
        //我们确认字段增加成功
        System.out.println("confirm 运行了");
        String applicationId = Transactions.getApplicationId();
        demoMapper.updateById(value,applicationId);
    }

    public void cancelRpcTcc(String value){
        //确认方法 需要自己手动修改业务逻辑
        //我们修改字段为增加失败
        System.out.println("cancl 运行了");
        String applicationId = Transactions.getApplicationId();
        demoMapper.deleteById(value,applicationId);
    }
}

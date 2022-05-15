package org.txlcn.demo.common.db.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.txlcn.demo.common.db.domain.Demo;

@Mapper
public interface BaseDemoMapper {
    @Insert("insert into t_demo(kid, demo_field, group_id, create_time,app_name,status) values(#{kid}, #{demoField}, #{groupId}, #{createTime},#{appName},#{status})")
    void save(Demo demo);

    @Delete("delete from t_demo where demo_field=#{kid} and app_name=#{appName}")
    void deleteById(@Param(value = "kid") String kid, @Param(value = "appName") String appName );

    @Delete("update t_demo set status=1 where demo_field=#{kid} and app_name=#{appName}")
    void updateById(@Param(value = "kid") String kid, @Param(value = "appName") String appName);
}

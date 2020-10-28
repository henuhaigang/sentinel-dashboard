package com.alibaba.csp.sentinel.dashboard.rule.redis;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.ApiDefinitionEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.GatewayFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleProvider;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * description: GatewayApiRuleRedisProvider <br>
 *
 * @author xie hui <br>
 * @version 1.0 <br>
 * @date 2020/10/27 10:01 <br>
 */
@Component("gatewayApiRuleRedisProvider")
public class GatewayApiRuleRedisProvider implements DynamicRuleProvider<List<ApiDefinitionEntity>> {

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Autowired
    private RuleConstants ruleConstants;

    @Override
    public List<ApiDefinitionEntity> getRules(String appName) throws Exception {
        System.out.println("Sentinel 从 Redis 拉取 gateway api 规则 begin >>>>>>>>>>>>>>>>>>>>");
        String value = redisTemplate.opsForValue().get(ruleConstants.gatewayApi + appName);
        if (StringUtils.isEmpty(value)){
            return new ArrayList<>();
        }
        System.out.println("Sentinel 从 Redis 拉取 gateway api 规则 end >>>>>>>>>>>>>>>>>>>>");
        return JSONObject.parseArray(value, ApiDefinitionEntity.class);
    }
}


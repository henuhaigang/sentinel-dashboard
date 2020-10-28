package com.alibaba.csp.sentinel.dashboard.rule.redis;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.ApiDefinitionEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.GatewayFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * description: GatewayApiRuleRedisPublisher <br>
 *
 * @author xie hui <br>
 * @version 1.0 <br>
 * @date 2020/10/27 10:01 <br>
 */
@Component("gatewayApiRuleRedisPublisher")
public class GatewayApiRuleRedisPublisher implements DynamicRulePublisher<List<ApiDefinitionEntity>> {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RuleConstants ruleConstants;

    @Override
    public void publish(String app, List<ApiDefinitionEntity> rules) throws Exception {
        System.out.println("Sentinel 向 Redis 推送 gateway api 规则 begin >>>>>>>>>>>>>>>>>>>>");
        if (rules == null){
            return;
        }
        redisTemplate.multi();
        redisTemplate.opsForValue().set(ruleConstants.gatewayApi+app, rules);
        redisTemplate.convertAndSend(ruleConstants.gatewayApiChannel+app,rules);
        redisTemplate.exec();
        System.out.println("Sentinel 向 Redis 推送 gateway api 规则 end >>>>>>>>>>>>>>>>>>>>");
    }
}

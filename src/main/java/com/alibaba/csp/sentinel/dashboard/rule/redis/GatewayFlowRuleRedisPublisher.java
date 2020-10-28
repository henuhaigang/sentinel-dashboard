package com.alibaba.csp.sentinel.dashboard.rule.redis;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.GatewayFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.AssertUtil;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
/**
 * description: GatewayFlowRuleRedisPublisher <br>
 *
 * @author xie hui <br>
 * @version 1.0 <br>
 * @date 2020/10/26 13:39 <br>
 */
@Component("gatewayFlowRuleRedisPublisher")
public class GatewayFlowRuleRedisPublisher implements DynamicRulePublisher<List<GatewayFlowRuleEntity>> {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RuleConstants ruleConstants;

    @Override
    public void publish(String app, List<GatewayFlowRuleEntity> rules) throws Exception {
        System.out.println("Sentinel 向 Redis 推送 gateway 规则 begin >>>>>>>>>>>>>>>>>>>>");
        if (rules == null){
            return;
        }
        redisTemplate.multi();
        redisTemplate.opsForValue().set(ruleConstants.gatewayFlow+app,rules);
        redisTemplate.convertAndSend(ruleConstants.gatewayFlowChannel+app,rules);
        redisTemplate.exec();
        System.out.println("Sentinel 向 Redis 推送 gateway 规则 end >>>>>>>>>>>>>>>>>>>>");
    }
}
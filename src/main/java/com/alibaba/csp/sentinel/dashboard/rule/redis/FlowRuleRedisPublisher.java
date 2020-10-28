package com.alibaba.csp.sentinel.dashboard.rule.redis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;

/**
 * 自定义实现限流配置推送规则
 * @author lv
 * @version 1.0.0
 */
@Component("flowRuleRedisPublisher")
public class FlowRuleRedisPublisher implements DynamicRulePublisher<List<FlowRuleEntity>> {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RuleConstants ruleConstants;

    @Override
    public void publish(String app, List<FlowRuleEntity> rules) throws Exception {
        System.out.println("Sentinel 向 Redis 推送 FlowRule 规则 begin >>>>>>>>>>>>>>>>>>>>");
        if (rules == null){
            return;
        }
        redisTemplate.multi();
        redisTemplate.opsForValue().set(ruleConstants.ruleFlow+app, rules);
        redisTemplate.convertAndSend(ruleConstants.ruleFlowChannel+app,rules);
        redisTemplate.exec();
        System.out.println("Sentinel 向 Redis 推送 FlowRule 规则 end >>>>>>>>>>>>>>>>>>>>");
    }
}
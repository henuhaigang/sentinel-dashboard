package com.alibaba.csp.sentinel.dashboard.rule.redis;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleProvider;
import com.alibaba.fastjson.JSONObject;
/**
 * 自定义实现基于redis的拉取规则
 * @author lv
 * @version 1.0.0
 */
@Component("flowRuleRedisProvider")
public class FlowRuleRedisProvider implements DynamicRuleProvider<List<FlowRuleEntity>> {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RuleConstants ruleConstants;

    @Override
    public List<FlowRuleEntity> getRules(String appName) throws Exception {
        System.out.println("Sentinel 从 Redis 拉取 FlowRule 规则 begin >>>>>>>>>>>>>>>>>>>>");
        String value = redisTemplate.opsForValue().get(ruleConstants.ruleFlow + appName);
        if (StringUtils.isEmpty(value)){
            return new ArrayList<>();
        }
        System.out.println("Sentinel 从 Redis 拉取 FlowRule 规则 end >>>>>>>>>>>>>>>>>>>>");
        return JSONObject.parseArray(value,FlowRuleEntity.class);
    }
}

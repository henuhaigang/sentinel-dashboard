package com.alibaba.csp.sentinel.dashboard.rule.redis;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("degradeRuleRedisPublisher")
public class DegradeRuleRedisPublisher implements DynamicRulePublisher<List<DegradeRuleEntity>> {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RuleConstants ruleConstants;

    @Override
    public void publish(String app, List<DegradeRuleEntity> rules) throws Exception {
        if (rules == null){
            return;
        }
        redisTemplate.multi();
        redisTemplate.opsForValue().set(ruleConstants.ruleDegrade + app, rules);
        redisTemplate.convertAndSend(ruleConstants.ruleDegradeChannel+app,rules);
        redisTemplate.exec();

    }

}
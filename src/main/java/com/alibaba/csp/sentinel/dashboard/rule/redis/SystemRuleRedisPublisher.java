package com.alibaba.csp.sentinel.dashboard.rule.redis;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.SystemRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * description: SystemRuleRedisPublisher <br>
 *
 * @author xie hui <br>
 * @version 1.0 <br>
 * @date 2020/10/26 17:40 <br>
 */
@Component("systemRuleRedisPublisher")
public class SystemRuleRedisPublisher implements DynamicRulePublisher<List<SystemRuleEntity>> {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RuleConstants ruleConstants;

    @Override
    public void publish(String app, List<SystemRuleEntity> rules) throws Exception {
        if (rules == null){
            return;
        }
        redisTemplate.multi();
        redisTemplate.opsForValue().set(ruleConstants.ruleSystem + app,rules);
        redisTemplate.convertAndSend(ruleConstants.ruleSystemChannel+app,rules);
        redisTemplate.exec();

    }

}
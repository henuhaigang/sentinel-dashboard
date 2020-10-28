package com.alibaba.csp.sentinel.dashboard.rule.redis;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleProvider;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component("degradeRuleRedisProvider")
public class DegradeRuleRedisProvider implements DynamicRuleProvider<List<DegradeRuleEntity>> {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RuleConstants ruleConstants;

    @Override
    public List<DegradeRuleEntity> getRules(String appName) throws Exception {
        String rules = redisTemplate.opsForValue().get(ruleConstants.ruleDegrade + appName);
        if (StringUtil.isEmpty(rules)) {
            return new ArrayList<>();
        }
        return JSONObject.parseArray(rules,DegradeRuleEntity.class);
    }

}
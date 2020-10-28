package com.alibaba.csp.sentinel.dashboard.rule.redis;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.SystemRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleProvider;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * description: SystemRuleRedisProvider <br>
 *
 * @author xie hui <br>
 * @version 1.0 <br>
 * @date 2020/10/26 17:40 <br>
 */
@Component("systemRuleRedisProvider")
public class SystemRuleRedisProvider implements DynamicRuleProvider<List<SystemRuleEntity>> {
    @Autowired
    private StringRedisTemplate redisTemplate;


    @Autowired
    private RuleConstants ruleConstants;

    @Override
    public List<SystemRuleEntity> getRules(String appName) throws Exception {
        String rules = redisTemplate.opsForValue().get(ruleConstants.ruleSystem + appName);
        if (StringUtil.isEmpty(rules)) {
            return new ArrayList<>();
        }
        return JSONObject.parseArray(rules,SystemRuleEntity.class);
    }

}

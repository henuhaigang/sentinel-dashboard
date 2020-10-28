package com.alibaba.csp.sentinel.dashboard.rule.redis;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.GatewayFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleProvider;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * description: GatewayFlowRuleRedisProvider <br>
 *
 * @author xie hui <br>
 * @version 1.0 <br>
 * @date 2020/10/26 13:38 <br>
 */
@Component("gatewayFlowRuleRedisProvider")
public class GatewayFlowRuleRedisProvider implements DynamicRuleProvider<List<GatewayFlowRuleEntity>> {

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Autowired
    private RuleConstants ruleConstants;

    @Override
    public List<GatewayFlowRuleEntity> getRules(String appName) throws Exception {
        System.out.println("Sentinel 从 Redis 拉取 gateway 规则 begin >>>>>>>>>>>>>>>>>>>>");
        String value =  redisTemplate.opsForValue().get(ruleConstants.gatewayFlow + appName);
        if (StringUtils.isEmpty(value)){
            return new ArrayList<>();
        }
        System.out.println("Sentinel 从 Redis 拉取 gateway 规则 end >>>>>>>>>>>>>>>>>>>>");
        return JSONObject.parseArray(value, GatewayFlowRuleEntity.class);
    }
}

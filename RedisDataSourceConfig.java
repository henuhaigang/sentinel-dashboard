package com.example.demo_gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.config.SentinelConfig;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.redis.RedisDataSource;
import com.alibaba.csp.sentinel.datasource.redis.config.RedisConnectionConfig;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @project: xxx
 * @description: 客户端在启动时初始化Redis中规则
 * @version 1.0.0
 * @errorcode
 *            错误码: 错误描述
 * @author
 *         <li>2020-07-17 guopengfei@bobfintech.com.cn Create 1.0
 * @copyright ©2019-2020 xxxx，版权所有。
 */
@Component
public class RedisDataSourceConfig implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(RedisDataSourceConfig.class);

    @Value("${spring.redis.host}")
    public String redisHost;

    @Value("${spring.redis.port}")
    public int redisPort;

    @Value("${spring.redis.password}")
    public String redisPass;

    @Value("${spring.redis.database}")
    public Integer database;


    public final String RULE_GATEWAYFLOW = "sentinel_rule_gatewayflow_";
    public final String RULE_GATEWAYFLOW_CHANNEL = "sentinel_rule_gatewayflow_channel_";

    //降级规则key前缀
    public final String RULE_GATEWAYDEGRADE = "sentinel_rule_degrade_";
    public final String RULE_GATEWAYDEGRADE_CHANNEL = "sentinel_rule_degrade_channel_";

    //系统规则key前缀
    public final String RULE_GATEWAYSYSTEM = "sentinel_rule_system_";
    public final String RULE_GATEWAYSYSTEM_CHANNEL = "sentinel_rule_system_channel_";

    //系统规则key前缀
    public final String RULE_GATEWAYAPI = "sentinel_rule_gatewayapi_";
    public final String RULE_GATEWAYAPI_CHANNEL = "sentinel_rule_gatewayapi_channel_";

    /**
     * ApplicationRunner
     * 该接口的方法会在服务启动之后被立即执行
     * 主要用来做一些初始化的工作
     * 但是该方法的运行是在SpringApplication.run(…) 执行完毕之前执行
     */
    @Override
    public void run(ApplicationArguments args) {
        log.info("执行sentinel规则初始化 start >>>>>>>>>>>>>");
        RedisConnectionConfig config = RedisConnectionConfig.builder().withHost(redisHost).withPort(redisPort)
                .withPassword(redisPass).withDatabase(database).build();
        Converter<String, Set<GatewayFlowRule>> parser = source -> JSON.parseObject(source,
                new TypeReference<Set<GatewayFlowRule>>()
                {});

        ReadableDataSource<String, Set<GatewayFlowRule>> redisDataSource = new RedisDataSource<>(config, RULE_GATEWAYFLOW+ SentinelConfig.getAppName(),
                RULE_GATEWAYFLOW_CHANNEL+ SentinelConfig.getAppName(), parser);
        GatewayRuleManager.register2Property(redisDataSource.getProperty());

        Converter<String, List<DegradeRule>> parserDegrade = source -> JSON.parseObject(source,
                new TypeReference<List<DegradeRule>>() {
                });
        ReadableDataSource<String, List<DegradeRule>> redisDataSourceDegrade = new RedisDataSource<>(config, RULE_GATEWAYDEGRADE+ SentinelConfig.getAppName(),
                RULE_GATEWAYDEGRADE_CHANNEL+ SentinelConfig.getAppName(), parserDegrade);
        DegradeRuleManager.register2Property(redisDataSourceDegrade.getProperty());

        Converter<String, List<SystemRule>> parserSystem = source -> JSON.parseObject(source,
                new TypeReference<List<SystemRule>>() {
                });
        ReadableDataSource<String, List<SystemRule>> redisDataSourceSystem =
                new RedisDataSource<>(config, RULE_GATEWAYSYSTEM + SentinelConfig.getAppName(), RULE_GATEWAYSYSTEM_CHANNEL+ SentinelConfig.getAppName(), parserSystem);
        SystemRuleManager.register2Property(redisDataSourceSystem.getProperty());

        Converter<String, Set<ApiDefinition>> parserApi = source ->parseApiDefineJson(source);

        ReadableDataSource<String, Set<ApiDefinition>> redisDataSourceAPI =
                new RedisDataSource<>(config, RULE_GATEWAYAPI + SentinelConfig.getAppName(), RULE_GATEWAYAPI_CHANNEL+ SentinelConfig.getAppName(), parserApi);
        GatewayApiDefinitionManager.register2Property(redisDataSourceAPI.getProperty());

        log.info(">>>>>>>>>执行sentinel规则初始化 end。。。");
    }

    private Set<ApiDefinition> parseApiDefineJson(String data) {
        Set<ApiDefinition> apiDefinitions = new HashSet();
        JSONArray array = JSON.parseArray(data);
        Iterator var4 = array.iterator();

        while(var4.hasNext()) {
            Object obj = var4.next();
            JSONObject o = (JSONObject)obj;
            ApiDefinition apiDefinition = new ApiDefinition(o.getString("apiName"));
            Set<ApiPredicateItem> predicateItems = new HashSet();
            JSONArray itemArray = o.getJSONArray("predicateItems");
            if (itemArray != null) {
                predicateItems.addAll(itemArray.toJavaList(ApiPathPredicateItem.class));
            }

            apiDefinition.setPredicateItems(predicateItems);
            apiDefinitions.add(apiDefinition);
        }

        return apiDefinitions;
    }
}
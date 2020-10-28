package com.alibaba.csp.sentinel.dashboard.rule.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RuleConstants {

    /**
     * 流控规则key前缀
     */
    @Value("${rule.flow}")
    public String ruleFlow;

    /**
     * 流控规则key前缀
     */
    @Value("${rule.degrade}")
    public String ruleDegrade;

    /**
     * 流控规则key前缀
     */
    @Value("${rule.system}")
    public String ruleSystem;

    /**
     * 流控规则key前缀
     */
    @Value("${rule.gatewayflow}")
    public String gatewayFlow;
    /**
     * 流控规则key前缀
     */
    @Value("${rule.gatewaydegrade}")
    public String gatewayDegrade;
    /**
     * 流控规则key前缀
     */
    @Value("${rule.gatewaysystem}")
    public String gatewaySystem;

    /**
     * 流控规则key前缀
     */
    @Value("${rule.gatewayapi}")
    public String gatewayApi;

    @Value("${rule.flowChannel}")
    public String ruleFlowChannel;

    @Value("${rule.degradeChannel}")
    public String ruleDegradeChannel;

    @Value("${rule.systemChannel}")
    public String ruleSystemChannel;

    @Value("${rule.gatewayflowChannel}")
    public String gatewayFlowChannel;

    @Value("${rule.gatewaydegradeChannel}")
    public String gatewayDegradeChannel;

    @Value("${rule.gatewaysystemChannel}")
    public String gatewaySystemChannel;

    @Value("${rule.gatewayapiChannel}")
    public String gatewayApiChannel;
}
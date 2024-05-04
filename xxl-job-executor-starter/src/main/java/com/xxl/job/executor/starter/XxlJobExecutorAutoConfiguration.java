package com.xxl.job.executor.starter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class XxlJobExecutorAutoConfiguration {
    @Value("${xxl.job.admin.addresses}")
    private String adminAddresses;

    @Value("${xxl.job.accessToken}")
    private String accessToken;

    @Value("${xxl.job.executor.app-name}")
    private String appName;

    @Value("${xxl.job.executor.app-desc}")
    private String appDesc;

    @Value("${xxl.job.executor.address}")
    private String address;

    @Value("${xxl.job.executor.ip}")
    private String ip;

    @Value("${xxl.job.executor.port}")
    private int port;

    @Value("${xxl.job.executor.log-path}")
    private String logPath;

    @Value("${xxl.job.executor.log-retention-days}")
    private int logRetentionDays;

    @Value("${xxl.job.executor.auto-init:false}")
    private boolean autoInit;

    @Bean
    public XxlJobAutoExecutor xxlJobExecutor() {
        XxlJobAutoExecutor xxlJobExecutor = new XxlJobAutoExecutor();
        xxlJobExecutor.setAdminAddresses(adminAddresses);
        xxlJobExecutor.setAppname(appName);
        xxlJobExecutor.setAddress(address);
        xxlJobExecutor.setIp(ip);
        xxlJobExecutor.setPort(port);
        xxlJobExecutor.setAccessToken(accessToken);
        xxlJobExecutor.setLogPath(logPath);
        xxlJobExecutor.setLogRetentionDays(logRetentionDays);
        xxlJobExecutor.setAutoInit(autoInit);
        xxlJobExecutor.setAppDesc(appDesc);
        return xxlJobExecutor;
    }
}

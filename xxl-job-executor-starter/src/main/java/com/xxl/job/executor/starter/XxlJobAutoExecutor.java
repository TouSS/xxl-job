package com.xxl.job.executor.starter;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import com.xxl.job.core.handler.annotation.XxlJob;

import java.lang.reflect.Method;

public class XxlJobAutoExecutor extends XxlJobSpringExecutor {
    private String appDesc;
    private boolean autoInit;

    public void setAppDesc(String appDesc) {
        this.appDesc = appDesc;
    }

    public void setAutoInit(boolean autoInit) {
        this.autoInit = autoInit;
    }

    @Override
    public void afterSingletonsInstantiated() {
        // Persist group.
        super.afterSingletonsInstantiated();
    }

    @Override
    protected void registJobHandler(XxlJob xxlJob, Object bean, Method executeMethod) {
        super.registJobHandler(xxlJob, bean, executeMethod);
        // Persist job.
    }
}

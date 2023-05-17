package com.xxl.job.executor.starter;

import com.xxl.job.core.biz.model.GroupParam;
import com.xxl.job.core.biz.model.JobParam;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import com.xxl.job.core.glue.GlueTypeEnum;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.handler.annotation.XxlJobRegister;
import com.xxl.job.core.handler.impl.MethodJobHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;

public class XxlJobAutoExecutor extends XxlJobSpringExecutor {
    private static final Logger logger = LoggerFactory.getLogger(XxlJobAutoExecutor.class);
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
        super.afterSingletonsInstantiated();


        if (autoInit && !CollectionUtils.isEmpty(getAdminBizList())) {
            // Persist group and job.
            persistGroup();
            persistJob();
        }
    }

    @Override
    protected void registJobHandler(XxlJob xxlJob, Object bean, Method executeMethod) {
        // Persist job.
        if (autoInit && !CollectionUtils.isEmpty(getAdminBizList())) {
            XxlJobRegister register = executeMethod.getAnnotation(XxlJobRegister.class);
            if (register != null) {
                JobParam jobParam = new JobParam();
                jobParam.setGlueType(GlueTypeEnum.BEAN.name());
                jobParam.setExecutorParam(register.param());
                jobParam.setExecutorHandler(xxlJob.value());
                jobParam.setAlarmEmail(register.alarmEmail());
                jobParam.setAuthor(register.author());
                jobParam.setDescription(register.desc());
                jobParam.setAppName(appname);
                jobParam.setExecutorBlockStrategy(register.blockStrategy());
                jobParam.setExecutorFailRetryCount(register.retryCount());
                jobParam.setExecutorRouteStrategy(register.routeStrategy());
                jobParam.setExecutorTimeout(register.timeout());
                jobParam.setMisfireStrategy(register.misfireStrategy());
                jobParam.setScheduleConf(register.scheduleConf());
                jobParam.setScheduleType(register.scheduleType());
                getAdminBizList().forEach(adminBiz -> {
                    ReturnT<String> ret = adminBiz.jobPersist(jobParam);
                    if (ReturnT.SUCCESS_CODE != ret.getCode()) {
                        logger.warn("Persist executor job failed, {}", ret.getMsg());
                    }
                });
            }
        }
        super.registJobHandler(xxlJob, bean, executeMethod);
    }

    private void persistGroup() {
        getAdminBizList().forEach(adminBiz -> {
            ReturnT<String> ret = adminBiz.groupPersist(new GroupParam(appname, appDesc));
            if (ReturnT.SUCCESS_CODE != ret.getCode()) {
                logger.warn(">>>>>>>>>>> xxl-job persist executor failed, {}", ret.getMsg());
            } else {
                logger.info(">>>>>>>>>>> xxl-job persist executor success, name: {}", appname);
            }
        });
    }

    private void persistJob() {
        jobHandlerRepository.forEach((name, handler) -> {
            XxlJobRegister register = ((MethodJobHandler) handler).getMethod().getAnnotation(XxlJobRegister.class);
            if (register != null) {
                JobParam jobParam = new JobParam();
                jobParam.setGlueType(GlueTypeEnum.BEAN.name());
                jobParam.setExecutorParam(register.param());
                jobParam.setExecutorHandler(name);
                jobParam.setAlarmEmail(register.alarmEmail());
                jobParam.setAuthor(register.author());
                jobParam.setDescription(register.desc());
                jobParam.setAppName(appname);
                jobParam.setExecutorBlockStrategy(register.blockStrategy());
                jobParam.setExecutorFailRetryCount(register.retryCount());
                jobParam.setExecutorRouteStrategy(register.routeStrategy());
                jobParam.setExecutorTimeout(register.timeout());
                jobParam.setMisfireStrategy(register.misfireStrategy());
                jobParam.setScheduleConf(register.scheduleConf());
                jobParam.setScheduleType(register.scheduleType());
                getAdminBizList().forEach(adminBiz -> {
                    ReturnT<String> ret = adminBiz.jobPersist(jobParam);
                    if (ReturnT.SUCCESS_CODE != ret.getCode()) {
                        logger.warn(">>>>>>>>>>> xxl-job persist jobhandler failed, {}", ret.getMsg());
                    } else {
                        logger.info(">>>>>>>>>>> xxl-job persist jobhandler success, name: {}", name);
                    }
                });
            }
        });
    }
}

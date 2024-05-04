package com.xxl.job.admin.service.impl;

import com.xxl.job.admin.core.model.XxlJobGroup;
import com.xxl.job.admin.core.model.XxlJobInfo;
import com.xxl.job.admin.core.thread.JobCompleteHelper;
import com.xxl.job.admin.core.thread.JobRegistryHelper;
import com.xxl.job.admin.dao.XxlJobGroupDao;
import com.xxl.job.admin.dao.XxlJobInfoDao;
import com.xxl.job.admin.service.XxlJobService;
import com.xxl.job.core.biz.AdminBiz;
import com.xxl.job.core.biz.model.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author xuxueli 2017-07-27 21:54:20
 */
@Service
public class AdminBizImpl implements AdminBiz {
    @Resource
    private XxlJobGroupDao xxlJobGroupDao;
    @Resource
    private XxlJobInfoDao xxlJobInfoDao;
    @Resource
    private XxlJobService xxlJobService;

    @Override
    public ReturnT<String> callback(List<HandleCallbackParam> callbackParamList) {
        return JobCompleteHelper.getInstance().callback(callbackParamList);
    }

    @Override
    public ReturnT<String> registry(RegistryParam registryParam) {
        return JobRegistryHelper.getInstance().registry(registryParam);
    }

    @Override
    public ReturnT<String> registryRemove(RegistryParam registryParam) {
        return JobRegistryHelper.getInstance().registryRemove(registryParam);
    }

    @Override
    public ReturnT<String> groupPersist(GroupParam groupParam) {
        if (!StringUtils.hasText(groupParam.getAppName())
                || !StringUtils.hasText(groupParam.getAppDesc())) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "Illegal Argument.");
        }
        if (xxlJobGroupDao.findByAppName(groupParam.getAppName()) != null) {
            // 已经存在, 不做处理
            return ReturnT.SUCCESS;
        }
        XxlJobGroup group = new XxlJobGroup();
        group.setAppname(groupParam.getAppName());
        group.setTitle(groupParam.getAppDesc());
        // 自动注册
        group.setAddressType(0);
        group.setUpdateTime(new Date());
        return (xxlJobGroupDao.save(group)>0)?ReturnT.SUCCESS:ReturnT.FAIL;
    }

    @Override
    public ReturnT<String> jobPersist(JobParam jobParam) {
        XxlJobGroup group = xxlJobGroupDao.findByAppName(jobParam.getAppName());
        if (group == null) {
            // 执行器未初始化, 不做处理
            return ReturnT.SUCCESS;
        }
        if (xxlJobInfoDao.findByGroupAndHandler(jobParam.getAppName(), jobParam.getExecutorHandler()) != null) {
            // 任务已存在, 不做处理
            return ReturnT.SUCCESS;
        }
        XxlJobInfo job = new XxlJobInfo();
        job.setJobGroup(group.getId());
        job.setAuthor(jobParam.getAuthor());
        job.setAlarmEmail(jobParam.getAlarmEmail());
        job.setJobDesc(jobParam.getDescription());
        job.setExecutorBlockStrategy(jobParam.getExecutorBlockStrategy());
        job.setExecutorFailRetryCount(jobParam.getExecutorFailRetryCount());
        job.setExecutorHandler(jobParam.getExecutorHandler());
        job.setExecutorRouteStrategy(jobParam.getExecutorRouteStrategy());
        job.setExecutorParam(jobParam.getExecutorParam());
        job.setGlueRemark(jobParam.getGlueRemark());
        job.setGlueSource(jobParam.getGlueSource());
        job.setGlueType(jobParam.getGlueType());
        job.setMisfireStrategy(jobParam.getMisfireStrategy());
        job.setScheduleConf(jobParam.getScheduleConf());
        job.setScheduleType(jobParam.getScheduleType());
        job.setAddTime(new Date());
        job.setUpdateTime(job.getAddTime());
        job.setGlueUpdatetime(job.getAddTime());
        return xxlJobService.add(job);
    }

}

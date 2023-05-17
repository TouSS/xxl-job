package com.xxl.job.core.handler.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface XxlJobRegister {
    String desc() default "";
    String scheduleType() default "CRON";
    String scheduleConf();
    String misfireStrategy() default "DO_NOTHING";
    String author() default "anonymous";
    String alarmEmail() default "";
    String routeStrategy() default "FIRST";
    String param() default "";
    String blockStrategy() default "SERIAL_EXECUTION";
    int timeout() default 0;
    int retryCount() default 0;
}

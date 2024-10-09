//package top.twip.market.job;
//
//import org.quartz.*;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class TestJobConfig {
//    @Bean
//    JobDetail TestJobDetail() {
//        return JobBuilder.newJob(TestJob.class)
//                .withIdentity("TestJob")
//                .storeDurably()
//                .build();
//    }
//
//    /**
//     * 未领用护照写入数据，每天 0 点 35 分执行
//     */
//    @Bean
//    Trigger TestJobTrigger(JobDetail TestJobDetail) {
//        return TriggerBuilder.newTrigger().forJob(TestJobDetail)
//                .withIdentity("TestJobTrigger")
//                .withSchedule(CronScheduleBuilder.cronSchedule("0 */1 * * * ?"))
//                .build();
//    }
//}

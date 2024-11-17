package co.kr.common.config;

import lombok.Setter;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Setter
public class SchedulerConfigurer implements SchedulingConfigurer {

  private String threadNamePrefix = "Scheduler";
  private int poolSize = 10;

  @Override
  public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
    ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
    scheduler.setThreadNamePrefix(this.threadNamePrefix);
    scheduler.setPoolSize(this.poolSize);
    scheduler.initialize();

    taskRegistrar.setTaskScheduler(scheduler);
  }
}

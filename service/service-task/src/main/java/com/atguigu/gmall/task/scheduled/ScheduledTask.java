package com.atguigu.gmall.task.scheduled;

import com.atguigu.gmall.common.constant.MqConst;
import com.atguigu.gmall.common.service.RabbitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
public class ScheduledTask {

    @Autowired
    private RabbitService rabbitService;

    /**
     * 每日凌晨一点执行
     * 0 0 1 * * ?
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void taskOne() {
        log.info("schedule: {}, started...", "taskOne");
        rabbitService.sendMessage(
                MqConst.EXCHANGE_DIRECT_TASK,
                MqConst.ROUTING_TASK_1,
                ""
        );
        log.info("schedule: {}, end...", "taskOne");
    }
}

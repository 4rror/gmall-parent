package com.atguigu.gmall.activity.receiver;

import com.atguigu.gmall.activity.cache.CacheHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class MessageReceiver {
    @SuppressWarnings("unused")
    public void receive(String message) {
        log.info("received redis message: {}", message);
        if (!StringUtils.isEmpty(message)) {
            message = message.replaceAll("\"", "");
            String[] split = message.split(":");
            if (split.length == 2) {
                CacheHelper.put(split[0], split[1]);
            }
        }
    }
}

package com.supinfood.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 咕泡学院，只为更好的你
 * 咕泡学院-Mic: 2082233439
 * http://www.gupaoedu.com
 **/
public class InitPrizeToRedisEvent extends ApplicationEvent {

    @Getter
    private final Integer lotteryId;

    public InitPrizeToRedisEvent(Object source, Integer lotteryId) {
        super(source);
        this.lotteryId = lotteryId;
    }
}


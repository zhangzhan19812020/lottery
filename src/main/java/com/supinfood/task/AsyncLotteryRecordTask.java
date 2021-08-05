package com.supinfood.task;

import com.supinfood.mapper.LotteryRecordMapper;
import com.supinfood.model.LotteryItem;
import com.supinfood.model.LotteryRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * 咕泡学院，只为更好的你
 * 咕泡学院-Mic: 2082233439
 * http://www.gupaoedu.com
 **/
@Slf4j
@Component
public class AsyncLotteryRecordTask {

    @Resource
    LotteryRecordMapper lotteryRecordMapper;

    @Async("lotteryServiceExecutor")
    public void saveLotteryRecord(String accountIp, String accountName, LotteryItem lotteryItem, String prizeName){
        log.info(Thread.currentThread().getName()+"---saveLotteryRecord");
        //存储中奖信息
        LotteryRecord record = new LotteryRecord();
        record.setAccountIp(accountIp);
        record.setAccountName(accountName);
        record.setItemId(lotteryItem.getId());
        record.setPrizeName(prizeName);
        record.setCreateTime(LocalDateTime.now());
        lotteryRecordMapper.insert(record);
    }
}

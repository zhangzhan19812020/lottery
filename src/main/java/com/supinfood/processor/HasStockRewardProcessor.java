package com.supinfood.processor;

import com.supinfood.constant.LotteryConstants;
import com.supinfood.model.RewardContext;
import com.supinfood.task.AsyncLotteryRecordTask;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;


/**
 * 咕泡学院，只为更好的你
 * 咕泡学院-Mic: 2082233439
 * http://www.gupaoedu.com
 **/
@Service
public class HasStockRewardProcessor extends AbstractRewardProcessor {

    @Resource
    AsyncLotteryRecordTask asyncLotteryRecordTask;

    @Override
    protected void afterProcessor(RewardContext context) {
        asyncLotteryRecordTask.saveLotteryRecord(context.getAccountIp(), context.getAccountName(), context.getLotteryItem(),context.getPrizeName());
    }

    @Override
    protected int getAwardType() {
        return LotteryConstants.PrizeTypeEnum.NORMAL.getValue();
    }
}

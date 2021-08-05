package com.supinfood.processor;

import com.supinfood.constant.LotteryConstants;
import com.supinfood.constant.ReturnCodeEnum;
import com.supinfood.exception.UnRewardException;
import com.supinfood.mapper.LotteryPrizeMapper;
import com.supinfood.model.RewardContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 咕泡学院，只为更好的你
 * 咕泡学院-Mic: 2082233439
 * http://www.gupaoedu.com
 **/
@Slf4j
public abstract class AbstractRewardProcessor implements RewardProcessor<RewardContext>, ApplicationContextAware {

    public static Map<Integer, RewardProcessor<RewardContext>> rewardProcessorMap = new ConcurrentHashMap<>();

    @Resource
    protected RedisTemplate<Object, Object> redisTemplate;

    @Resource
    LotteryPrizeMapper lotteryPrizeMapper;

    private void beforeProcessor(RewardContext context) {
    }

    @Override
    public void doReward(RewardContext context) {
        beforeProcessor(context);
        processor(context);
        afterProcessor(context);
    }

    protected abstract void afterProcessor(RewardContext context);


    /**
     * 发放对应的奖品
     *
     * @param context
     */
    protected void processor(RewardContext context) {
        //扣减库存（redis的更新）
        Long result = redisTemplate.opsForHash().increment(context.getKey(),"validStock",-1);
        //当前奖品库存不足，提示未中奖，或者返回一个兜底的奖品
        if(result.intValue()<0){
            throw new UnRewardException(ReturnCodeEnum.LOTTER_REPO_NOT_ENOUGHT.getCode(),ReturnCodeEnum.LOTTER_REPO_NOT_ENOUGHT.getMsg());
        }
        List<Object> properties= Arrays.asList("id","prizeName");
        List<Object> prizes=redisTemplate.opsForHash().multiGet(context.getKey(),properties);
        context.setPrizeId(Integer.parseInt(prizes.get(0).toString()));
        context.setPrizeName(prizes.get(1).toString());
        //更新库存（数据库的更新）
        lotteryPrizeMapper.updateValidStock(context.getPrizeId());
    }

    /**
     * 返回当前奖品类型
     *
     * @return
     */
    protected abstract int getAwardType();

    @SuppressWarnings("all")
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        rewardProcessorMap.put(LotteryConstants.PrizeTypeEnum.THANK.getValue(), (RewardProcessor) applicationContext.getBean(NoneStockRewardProcessor.class));
        rewardProcessorMap.put(LotteryConstants.PrizeTypeEnum.NORMAL.getValue(), (RewardProcessor) applicationContext.getBean(HasStockRewardProcessor.class));
    }
}

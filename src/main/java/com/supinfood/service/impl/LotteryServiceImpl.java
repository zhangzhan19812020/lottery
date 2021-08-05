package com.supinfood.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supinfood.constant.LotteryConstants;
import com.supinfood.constant.RedisKeyManager;
import com.supinfood.constant.ReturnCodeEnum;
import com.supinfood.event.InitPrizeToRedisEvent;
import com.supinfood.exception.BizException;
import com.supinfood.exception.UnRewardException;
import com.supinfood.mapper.LotteryItemMapper;
import com.supinfood.mapper.LotteryMapper;
import com.supinfood.model.Lottery;
import com.supinfood.model.LotteryItem;
import com.supinfood.model.LotteryItemVo;
import com.supinfood.model.RewardContext;
import com.supinfood.processor.AbstractRewardProcessor;
import com.supinfood.service.ILotteryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mic
 * @since 2021-08-03
 */
@Slf4j
@Service
public class LotteryServiceImpl extends ServiceImpl<LotteryMapper, Lottery> implements ILotteryService {

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private LotteryMapper lotteryMapper;

    @Resource
    private LotteryItemMapper lotteryItemMapper;

    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    private static final int MULTIPLE = 10000;

    /**
     * 开始抽奖
     * @param lotteryItemVo
     * @throws Exception
     */
    @Override
    public RewardContext doDraw(LotteryItemVo lotteryItemVo) throws Exception {
        RewardContext context = new RewardContext();
        LotteryItem lotteryItem = null;
        try {
            Lottery lottery = checkLottery(lotteryItemVo);
            log.info("InitPrizeToRedisEvent时间开始发布");
            applicationContext.publishEvent(new InitPrizeToRedisEvent(this, lottery.getId()));
            log.info("InitPrizeToRedisEvent时间结束发布");
            lotteryItem = doPlay(lottery);
            log.info("lotteryItem:{}", lotteryItem);
            String key = RedisKeyManager.getLotteryPrizeRedisKey(lottery.getId(), lotteryItem.getPrizeId());
            int prizeType = !Objects.isNull(redisTemplate.opsForHash().get(key, "prizeType")) ? Integer.parseInt(Objects.requireNonNull(redisTemplate.opsForHash().get(key, "prizeType")).toString()) : 0;
            String prizeName = Objects.requireNonNull(redisTemplate.opsForHash().get(key, "prizeName")).toString();

            log.info("key:{},prizeType:{}", key, prizeType);
            context.setLottery(lottery);
            context.setLotteryItem(lotteryItem);
            context.setAccountIp(lotteryItemVo.getAccountIp());
            context.setAccountName(lotteryItemVo.getAccountName());
            context.setPrizeName(prizeName);
            context.setPrizeId(lotteryItem.getPrizeId());
            context.setKey(key);
            log.info("content:{}", context);

            AbstractRewardProcessor.rewardProcessorMap.get(prizeType).doReward(context);
            return context;
        }
        catch (UnRewardException ex){
            assert lotteryItem != null;
            context.setKey(RedisKeyManager.getDefaultLotteryPrizeRedisKey(lotteryItem.getLotteryId()));
            lotteryItem = (LotteryItem) redisTemplate.opsForValue().get(RedisKeyManager.getDefaultLotteryItemRedisKey(lotteryItem.getLotteryId()));
            context.setLotteryItem(lotteryItem);
            AbstractRewardProcessor.rewardProcessorMap.get(LotteryConstants.PrizeTypeEnum.THANK.getValue()).doReward(context);
            return context;
        }
    }

    /**
     * 检验活动的合法性
     * @param lotteryItemVo
     * @return
     */
    private Lottery checkLottery(LotteryItemVo lotteryItemVo) {
        Lottery lottery;
        Object lotteryJsonStr = redisTemplate.opsForValue().get(RedisKeyManager.getLotteryRedisKey(lotteryItemVo.getLotteryId()));
        if (!Objects.isNull(lotteryJsonStr)) {
            lottery = JSON.parseObject(lotteryJsonStr.toString(), Lottery.class);
            log.info("从redis中获取了lottery:{}", lottery);
        } else {
            lottery = lotteryMapper.selectById(lotteryItemVo.getLotteryId());
            log.info("从数据库中获取了lottery:{}", lottery);
        }
        if (Objects.isNull(lottery)) {
            throw new BizException(ReturnCodeEnum.LOTTER_NOT_EXIST.getCode(), ReturnCodeEnum.LOTTER_NOT_EXIST.getMsg());
        }
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(lottery.getStartTime())) {
            throw new BizException(ReturnCodeEnum.LOTTER_NOT_START.getCode(), ReturnCodeEnum.LOTTER_NOT_START.getMsg());
        }
        if (now.isAfter(lottery.getEndTime())) {
            throw new BizException(ReturnCodeEnum.LOTTER_FINISH.getCode(), ReturnCodeEnum.LOTTER_FINISH.getMsg());
        }
        return lottery;
    }

    //执行抽奖
    @SuppressWarnings("all")
    private LotteryItem doPlay(Lottery lottery) {
        LotteryItem lotteryItem = null;

        Object lotteryItemsObj = redisTemplate.opsForValue().get(RedisKeyManager.getLotteryItemRedisKey(lottery.getId()));
        List<LotteryItem> lotteryItems;
        //说明还未加载到缓存中，同步从数据库加载，并且异步将数据缓存
        if(Objects.isNull(lotteryItemsObj)) {
            QueryWrapper<LotteryItem> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("lottery_id", lottery.getId());
            lotteryItems = lotteryItemMapper.selectList(queryWrapper);
            log.info("从数据库中获取奖项列表:{}", lotteryItems);
        } else {

            lotteryItems = (List<LotteryItem>)lotteryItemsObj;
            log.info("从redis中获取奖项列表:{}", lotteryItems);
        }

        //奖项数据未配置
        if (lotteryItems.isEmpty()) {
            throw new BizException(ReturnCodeEnum.LOTTER_ITEM_NOT_INITIAL.getCode(), ReturnCodeEnum.LOTTER_ITEM_NOT_INITIAL.getMsg());
        }

        int lastScope = 0;
        Collections.shuffle(lotteryItems); //将lotteryItems元素顺序随机打乱
        Map<Integer, int[]> awardItemScope = new HashMap<>();

        for (LotteryItem item : lotteryItems) {
            int currentScope = lastScope + new BigDecimal(item.getPercent().floatValue()).multiply(new BigDecimal(MULTIPLE)).intValue();
            awardItemScope.put(item.getId(), new int[]{lastScope + 1, currentScope});
            lastScope = currentScope;
        }

        int luckyNumber = new Random().nextInt(lastScope);
        int luckyItemId = 0;
        if (!awardItemScope.isEmpty()) {
            Set<Map.Entry<Integer, int[]>> set = awardItemScope.entrySet();
            for (Map.Entry<Integer, int[]> entry : set) {
                if (luckyNumber >= entry.getValue()[0] && luckyNumber <= entry.getValue()[1]) {
                    luckyItemId = entry.getKey();
                    break;
                }
            }
        }

        for (LotteryItem item : lotteryItems) {
            if (item.getId().intValue() == luckyItemId) {
                lotteryItem = item;
                break;
            }
        }
        return lotteryItem;
    }


}

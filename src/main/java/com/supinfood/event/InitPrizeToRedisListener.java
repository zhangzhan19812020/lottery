package com.supinfood.event;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.supinfood.constant.RedisKeyManager;
import com.supinfood.mapper.LotteryItemMapper;
import com.supinfood.mapper.LotteryPrizeMapper;
import com.supinfood.model.LotteryItem;
import com.supinfood.model.LotteryPrize;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 咕泡学院，只为更好的你
 * 咕泡学院-Mic: 2082233439
 * http://www.gupaoedu.com
 **/
@Slf4j
@Component
public class InitPrizeToRedisListener implements ApplicationListener<InitPrizeToRedisEvent> {

    @Resource
    RedisTemplate<Object, Object> redisTemplate;
    @Resource
    LotteryPrizeMapper lotteryPrizeMapper;
    @Resource
    LotteryItemMapper lotteryItemMapper;

    @Override
    public void onApplicationEvent(InitPrizeToRedisEvent initPrizeToRedisEvent) {
        log.info("begin InitPrizeToRedisListener:{}", initPrizeToRedisEvent);
        Boolean result = redisTemplate.opsForValue().setIfAbsent(RedisKeyManager.getLotteryPrizeRedisKey(initPrizeToRedisEvent.getLotteryId()),"1");

        if(!result){
            log.info("already initial");
            return;
        }

        QueryWrapper<LotteryItem> lotteryItemQueryWrapper = new QueryWrapper<>();
        lotteryItemQueryWrapper.eq("lottery_id", initPrizeToRedisEvent.getLotteryId());
        List<LotteryItem> lotteryItems = lotteryItemMapper.selectList(lotteryItemQueryWrapper);

        lotteryItems.forEach(System.out::println);

        //如果指定的奖品没有了，会生成一个默认的奖项
        LotteryItem defaultLotteryItem = lotteryItems.parallelStream().filter(o -> o.getDefaultItem() == 1).findFirst().orElse(null);

        Map<String,Object> lotteryItemMap=new HashMap<>(16);
        lotteryItemMap.put(RedisKeyManager.getLotteryItemRedisKey(initPrizeToRedisEvent.getLotteryId()),lotteryItems);
        lotteryItemMap.put(RedisKeyManager.getDefaultLotteryItemRedisKey(initPrizeToRedisEvent.getLotteryId()), defaultLotteryItem);
        redisTemplate.opsForValue().multiSet(lotteryItemMap);

        QueryWrapper<LotteryPrize> lotteryPrizeQueryWrapper=new QueryWrapper<>();
        lotteryPrizeQueryWrapper.eq("lottery_id", initPrizeToRedisEvent.getLotteryId());
        List<LotteryPrize> lotteryPrizes = lotteryPrizeMapper.selectList(lotteryPrizeQueryWrapper);

        //保存一个默认奖项
        AtomicReference<LotteryPrize> defaultPrize = new AtomicReference<>();
        lotteryPrizes.forEach(lotteryPrize -> {
            assert defaultLotteryItem != null;
            if(lotteryPrize.getId().equals(defaultLotteryItem.getPrizeId())){
                defaultPrize.set(lotteryPrize);
            }

            String key = RedisKeyManager.getLotteryPrizeRedisKey(initPrizeToRedisEvent.getLotteryId(),lotteryPrize.getId());
            setLotteryPrizeToRedis(key, lotteryPrize);
        });

        String key = RedisKeyManager.getDefaultLotteryPrizeRedisKey(initPrizeToRedisEvent.getLotteryId());
        setLotteryPrizeToRedis(key,defaultPrize.get());

        log.info("finish InitPrizeToRedisListener:{}", initPrizeToRedisEvent);
    }

    private void setLotteryPrizeToRedis(String key, LotteryPrize lotteryPrize){
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        redisTemplate.opsForHash().put(key,"id",lotteryPrize.getId());
        redisTemplate.opsForHash().put(key,"lotteryId",lotteryPrize.getLotteryId());
        redisTemplate.opsForHash().put(key,"prizeName",lotteryPrize.getPrizeName());
        redisTemplate.opsForHash().put(key,"prizeType",lotteryPrize.getPrizeType());
        redisTemplate.opsForHash().put(key,"totalStock",lotteryPrize.getTotalStock());
        redisTemplate.opsForHash().put(key,"validStock",lotteryPrize.getValidStock());
    }
}
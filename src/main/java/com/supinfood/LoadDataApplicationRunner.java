package com.supinfood;

import com.alibaba.fastjson.JSON;
import com.supinfood.constant.RedisKeyManager;
import com.supinfood.mapper.LotteryMapper;
import com.supinfood.model.Lottery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 咕泡学院，只为更好的你
 * 咕泡学院-Mic: 2082233439
 * http://www.gupaoedu.com
 **/
@Slf4j
@Component
public class LoadDataApplicationRunner implements ApplicationRunner {



    @Resource
    private LotteryMapper lotteryMapper;

    @Resource
    RedisTemplate<Object, Object> redisTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("=========begin load lottery data to Redis===========");
        //加载当前抽奖活动信息
        Lottery lottery = lotteryMapper.selectById(1);

        if(Objects.isNull(lottery)) {
            log.info("未找到活动信息");
            return;
        }

        redisTemplate.opsForValue().set(RedisKeyManager.getLotteryRedisKey(lottery.getId()), JSON.toJSONString(lottery));

        log.info(lottery.toString());


        log.info("=========finish load lottery data to Redis===========");
    }
}



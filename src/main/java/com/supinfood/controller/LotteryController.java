package com.supinfood.controller;

import com.supinfood.constant.RedisKeyManager;
import com.supinfood.constant.ReturnCodeEnum;
import com.supinfood.exception.RewardException;
import com.supinfood.model.LotteryItemVo;
import com.supinfood.model.RewardContext;
import com.supinfood.model.SysUser;
import com.supinfood.result.ResultResp;
import com.supinfood.service.ILotteryService;
import com.supinfood.util.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mic
 * @since 2021-08-03
 */

@Slf4j
@RestController
@RequestMapping("/lottery")
public class LotteryController {

    @Resource
    private ILotteryService lotteryService;

    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    @SuppressWarnings("all")
    @GetMapping("/{id}")
    public ResultResp<LotteryItemVo> doDraw(@PathVariable("id")Integer id, Authentication authentication) {

        SysUser currentUser = (SysUser)authentication.getPrincipal();
        String accountIp = currentUser.getUsername();
        String accountName = currentUser.getTrueName();

        log.info("抽奖开始咯!!! 用户名={}, 姓名={}, lotteryId={}:",accountIp, accountName, id);

        ResultResp<LotteryItemVo> resultResp=new ResultResp<>();
        try {
            checkDrawParams(id, accountIp);

            LotteryItemVo lotteryItemVo = new LotteryItemVo(id, accountIp, accountName, "", 0, 0);
            RewardContext context = lotteryService.doDraw(lotteryItemVo);

            resultResp.setCode(ReturnCodeEnum.SUCCESS.getCode());
            resultResp.setMsg(ReturnCodeEnum.SUCCESS.getMsg());
            lotteryItemVo.setPrizeName(context.getPrizeName());
            lotteryItemVo.setPrizeId(context.getPrizeId());
            lotteryItemVo.setLevel(context.getLotteryItem().getLevel());
            log.info("lotteryItemVo:{}", lotteryItemVo);
            resultResp.setResult(lotteryItemVo);


        } catch (Exception e){
            //e.printStackTrace();
            return ExceptionUtil.handlerException4biz(resultResp,e);
        } finally {
            //清除占位标记
            redisTemplate.delete(RedisKeyManager.getDrawingRedisKey(accountIp));
        }
        return resultResp;

    }


    /**
     * 检验抽奖的合法性
     * @param id  活动id
     * @param accountIp 抽奖人
     */
    private void checkDrawParams(Integer id, String accountIp) {
        if(Objects.isNull(id)) {
            throw new RewardException(ReturnCodeEnum.REQUEST_PARAM_NOT_VALID.getCode(),ReturnCodeEnum.REQUEST_PARAM_NOT_VALID.getMsg());
        }
        //采用setNx命令，判断当前用户上一次抽奖是否结束
        Boolean result = redisTemplate.opsForValue().setIfAbsent(RedisKeyManager.getDrawingRedisKey(accountIp),"1", 60, TimeUnit.SECONDS);

        //如果为false，说明上一次抽奖还未结束
        if(Objects.isNull(result) || !result){
            throw new RewardException(ReturnCodeEnum.LOTTER_DRAWING.getCode(),ReturnCodeEnum.LOTTER_DRAWING.getMsg());
        }
    }
}
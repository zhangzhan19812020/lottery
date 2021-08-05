package com.supinfood.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.supinfood.model.Lottery;
import com.supinfood.model.LotteryItemVo;
import com.supinfood.model.RewardContext;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mic
 * @since 2021-08-03
 */
public interface ILotteryService extends IService<Lottery> {
    RewardContext doDraw(LotteryItemVo lotteryItemVo) throws Exception;

}

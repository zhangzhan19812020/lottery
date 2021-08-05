package com.supinfood.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 咕泡学院，只为更好的你
 * 咕泡学院-Mic: 2082233439
 * http://www.gupaoedu.com
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LotteryItemVo {
    /**
     * 活动号
     */
    private Integer lotteryId;

    /**
     * 中奖用户ip
     */
    private String accountIp;

    /**
     * 中奖者姓名
     */
    private String accountName;

    /**
     * 奖品名称
     */
    private String prizeName;

    /**
     * 中奖登记
     */
    private Integer level;

    /**
     * 奖品id
     */
    private Integer prizeId;
}

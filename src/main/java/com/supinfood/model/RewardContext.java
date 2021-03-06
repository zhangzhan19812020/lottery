package com.supinfood.model;

import lombok.Data;

/**
 * 咕泡学院，只为更好的你
 * 咕泡学院-Mic: 2082233439
 * http://www.gupaoedu.com
 **/
@Data
public class RewardContext {

    private Lottery lottery;

    private LotteryItem lotteryItem;

    private String key;

    private String accountIp;

    private String accountName;

    private String prizeName;

    private Integer level;

    private Integer prizeId;
}

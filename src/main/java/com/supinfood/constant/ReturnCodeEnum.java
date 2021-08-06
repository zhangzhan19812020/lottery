package com.supinfood.constant;

import lombok.Getter;

/**
 * 咕泡学院，只为更好的你
 * 咕泡学院-Mic: 2082233439
 * http://www.gupaoedu.com
 **/
public enum  ReturnCodeEnum {
    SUCCESS("0000", "成功"),
    LOTTERY_NOT_EXIST("9000","指定抽奖活动不存在"),
    LOTTERY_NOT_START("9001","活动未开始"),
    LOTTERY_FINISH("9002","活动已结束"),
    LOTTERY_REPO_NOT_ENOUGHT("9003","当前奖品库存不足"),
    LOTTERY_ITEM_NOT_INITIAL("9004","奖项数据未初始化"),
    LOTTERY_DRAWING("9005","上一次抽奖还未结束"),
    PLAY_TIMES_ZERO("9906", "抽奖次数已用完"),
    REQUEST_PARAM_NOT_VALID("9998","请求参数不正确"),

    SYSTEM_ERROR("9999", "系统繁忙,请稍后重试");


    @Getter
    private String code;

    @Getter
    private String msg;

    private ReturnCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}

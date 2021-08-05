package com.supinfood.controller;


import com.supinfood.constant.ReturnCodeEnum;
import com.supinfood.model.LotteryRecord;
import com.supinfood.result.ResultResp;
import com.supinfood.service.ILotteryRecordService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mic
 * @since 2021-08-03
 */
@RestController
@RequestMapping("/lottery-record")
public class LotteryRecordController {
    @Resource
    ILotteryRecordService lotteryRecordService;

    @GetMapping
    public ResultResp<List<LotteryRecord>> records(){
        List<LotteryRecord> records=lotteryRecordService.list();
        ResultResp resultResp=new ResultResp();
        resultResp.setMsg(ReturnCodeEnum.SUCCESS.getMsg());
        resultResp.setCode(ReturnCodeEnum.SUCCESS.getCode());
        resultResp.setResult(records);
        return resultResp;
    }
}

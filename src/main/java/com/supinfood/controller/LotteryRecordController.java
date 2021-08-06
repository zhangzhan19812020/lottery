package com.supinfood.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.supinfood.constant.ReturnCodeEnum;
import com.supinfood.model.LotteryRecord;
import com.supinfood.model.SysUser;
import com.supinfood.result.ResultResp;
import com.supinfood.service.ILotteryRecordService;
import org.springframework.security.core.Authentication;
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
    public ResultResp<List<LotteryRecord>> records(Authentication authentication){
        SysUser currentUser = (SysUser)authentication.getPrincipal();
        String accountIp = currentUser.getUsername();

        QueryWrapper<LotteryRecord> lotteryRecordQueryWrapper = new QueryWrapper<>();
        lotteryRecordQueryWrapper.eq("account_ip", accountIp);
        List<LotteryRecord> records= lotteryRecordService.list(lotteryRecordQueryWrapper);
        ResultResp resultResp=new ResultResp();
        resultResp.setMsg(ReturnCodeEnum.SUCCESS.getMsg());
        resultResp.setCode(ReturnCodeEnum.SUCCESS.getCode());
        resultResp.setResult(records);
        return resultResp;
    }
}

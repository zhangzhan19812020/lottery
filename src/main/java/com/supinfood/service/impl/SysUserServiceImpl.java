package com.supinfood.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supinfood.mapper.LotteryRecordMapper;
import com.supinfood.mapper.SysUserMapper;
import com.supinfood.model.LotteryItem;
import com.supinfood.model.SysUser;
import com.supinfood.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private LotteryRecordMapper lotteryRecordMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        QueryWrapper<SysUser> sysUserQueryWrapper = new QueryWrapper<>();
        sysUserQueryWrapper.eq("username", s);
        List<SysUser> sysUsers = sysUserMapper.selectList(sysUserQueryWrapper);

        if(sysUsers != null && sysUsers.size() > 0) {
            int playedTimes = lotteryRecordMapper.SelectCountByAccountIp(s);
            SysUser user = sysUsers.get(0);
            int remainDrawTimes = user.getDrawTimes()- playedTimes <0 ? 0 : user.getDrawTimes()- playedTimes;
            user.setDrawTimes(remainDrawTimes);
            return user;
        }


        log.info(String.format("用户%s不存在", s));
        throw new UsernameNotFoundException(String.format("用户%s不存在", s));
    }
}

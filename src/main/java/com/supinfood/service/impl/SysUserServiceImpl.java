package com.supinfood.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.supinfood.mapper.SysUserMapper;
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
    private SysUserMapper mapper;



    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("username", s);
        List<SysUser> sysUsers = mapper.selectByMap(queryMap);
        if(sysUsers != null && sysUsers.size() > 0) {
            return sysUsers.get(0);
        }
        log.info(String.format("用户%s不存在", s));
        throw new UsernameNotFoundException(String.format("用户%s不存在", s));
    }
}

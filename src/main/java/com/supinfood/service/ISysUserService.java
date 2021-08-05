package com.supinfood.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.supinfood.model.SysUser;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mic
 * @since 2021-08-03
 */
public interface ISysUserService extends IService<SysUser>, UserDetailsService {

}

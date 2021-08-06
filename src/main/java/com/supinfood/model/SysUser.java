package com.supinfood.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author mic
 * @since 2021-08-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUser extends Model implements UserDetails {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 工号
     */
    private String username;

    /**
     * 姓名
     */
    private String trueName;

    /**
     * 抽奖次数
     */
    private int drawTimes;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("role1"));
        return roles;
    }



    @Override
    public String getPassword() {

        return "$2a$10$6M8cjeqVC4.nG0naTv7dVuslAl7jQfdydNgJNb7Z5r4ZY5F8JRmHW";
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

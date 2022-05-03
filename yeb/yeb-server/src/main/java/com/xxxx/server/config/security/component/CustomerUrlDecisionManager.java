package com.xxxx.server.config.security.component;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;


/**
 * 根据登录用户判断拥有哪些 角色 然后进行 相应的 匹配
 */
@Component
public class CustomerUrlDecisionManager implements AccessDecisionManager {

    @Override
    public void decide(Authentication authentication, Object object,
                       Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        for (ConfigAttribute configAttribute : configAttributes) {
            //当前url所需要的角色   此处获取的就是 CustomerFilter判断后的角色
            String needRole = configAttribute.getAttribute();
            //判断角色是否登录即可访问的角色 此角色在CustomerFilter中设置
            if("ROLE_LOGIN".equalsIgnoreCase(needRole)){
                //判断是否登录
                if(authentication instanceof AnonymousAuthenticationToken){
                    throw new AccessDeniedException("尚未登录,请登录");
                }else{
                    return;
                }
            }

            //判断用户角色是否为url所需角色  因为一个用户可能有多个角色 所以这里循环判断
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                //判断 url过滤后的角色 与 所需要的角色是否一致
                if(authority.getAuthority().equals(needRole)){
                    //如果 一致 放行
                    return;
                }
                
            }
        }
        throw new AccessDeniedException("权限不足,请联系系统管理员");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return false;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }
}

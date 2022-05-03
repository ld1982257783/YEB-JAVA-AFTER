package com.xxxx.server.config.security.component;

import com.xxxx.server.pojo.Menu;
import com.xxxx.server.pojo.Role;
import com.xxxx.server.servive.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;


/**
 * 根据URL判断需要哪个  角色
 */
@Component
public class CustomerFilter implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private IMenuService menuService;

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        //获取请求 url
        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        //查询所有的菜单
        List<Menu> menus = menuService.getMenusByRole();
        for (Menu menu : menus) {
            //循环判断请求url与菜单角色所需的url是否匹配
            if(antPathMatcher.match(menu.getUrl(),requestUrl)){
                //将此url请求所需的角色 封装起来
                String[] str = menu.getRoles().stream().map(Role::getName).toArray(String[]::new);
                //返回
                return SecurityConfig.createList(str);

            }
        }
        //没匹配的URL默认登录 即可访问
        return SecurityConfig.createList("ROLE_login");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }
}

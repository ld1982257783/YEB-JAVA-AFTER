package com.xxxx.server.servive.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.server.config.security.component.JwtTokenUtil;
import com.xxxx.server.mapper.AdminMapper;
import com.xxxx.server.mapper.AdminRoleMapper;
import com.xxxx.server.pojo.Admin;
import com.xxxx.server.pojo.AdminRole;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.pojo.Role;
import com.xxxx.server.servive.IAdminService;
import com.xxxx.server.utils.AdminUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lida
 * @since 2022-04-05
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private AdminRoleMapper adminRoleMapper;

    @Autowired
    private AdminMapper adminMapper;
    /**
     * 登录之后返回token
     * @param username
     * @param password
     * @param request
     * @return
     */
    @Override
    public RespBean login(String username, String password, String code,HttpServletRequest request) {
        //从session获取验证码
        String captcha = (String) request.getSession().getAttribute("captcha");
        if(captcha.isEmpty() || !captcha.equalsIgnoreCase(code)){
            return RespBean.error("验证码输入错误 请重新输入");
        }


        //获取登录用户信息  校验登录
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        //获取用户信息
        System.out.println("查询到的用户信息为"+userDetails.getPassword());
        System.out.println("接收到的密码为"+password);
        System.out.println("密码校验结果"+passwordEncoder.matches(password,userDetails.getPassword()));
        if(userDetails==null || !passwordEncoder.matches(password,userDetails.getPassword())){
            return RespBean.error("用户名或密码不正确");
        }
        if(!userDetails.isEnabled()){
            return RespBean.error("账号被禁用,请联系管理员");
        }

        //更新security登录用户对象
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);


        //登录成功 根据登录信息获取 令牌
        String token = jwtTokenUtil.generatorToken(userDetails);
        System.out.println("生成的token为"+token);
        Map<String,String> tokenMap = new HashMap<>();
        tokenMap.put("token",token);
        tokenMap.put("tokenHead",tokenHead);
        return RespBean.success("登录成功",tokenMap);

    }


    /**
     * 根据用户名获取用户
     * @param username
     * @return
     */
    @Override
    public Admin getAdminByUserName(String username) {
        AdminMapper adminMapper = this.baseMapper;
        return adminMapper.selectOne(new QueryWrapper<Admin>().eq("username",username).eq("enabled",true));
    }



    /**
     * 根据用户id查询角色列表
     * @param adminId
     * @return
     */
    @Override
    public List<Role> getRoles(Integer adminId) {
        return this.baseMapper.getRoles(adminId);
    }

    /**
     * 获取所有操作员
     * @param keywords
     * @return
     */
    @Override
    public List<Admin> getAllAdmins(String keywords) {
        return this.baseMapper
                .getAllAdmins(AdminUtils.getCurrentAdmin().getId(),keywords);
    }


    /**
     * 更新操作员角色
     * @param adminId
     * @param rids
     * @return
     */
    @Override
    @Transactional
    public RespBean updateAdminRole(Integer adminId, Integer[] rids) {
        //更新之前先进行删除
        adminRoleMapper.delete(new QueryWrapper<AdminRole>().eq("adminId",adminId));
        Integer result = adminRoleMapper.addAdminRole(adminId, rids);
        if(rids.length == result){
            return RespBean.success("更新成功!");
        }
        return RespBean.error("更新失败!");
    }


    /**
     * 更新用户密码
     * @param oldPass
     * @param pass
     * @param adminId
     * @return
     */
    @Override
    public RespBean updatePassword(String oldPass, String pass, Integer adminId) {
        //根据用户id 传当前用户 信息
        Admin admin = adminMapper.selectById(adminId);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        //将传递来的就密码 与 用户密码进行校验
        System.out.println("校验结果为"+encoder.matches(oldPass,admin.getPassword()));
        if(encoder.matches(oldPass,admin.getPassword())){
            //如果校验成功  设置新密码
            admin.setPassword(encoder.encode(pass));
            int i = adminMapper.updateById(admin);
            if(i == 1){
                return RespBean.success("更细成功!");
            }
            return RespBean.error("更新失败!");
        }
        return null;
    }


}

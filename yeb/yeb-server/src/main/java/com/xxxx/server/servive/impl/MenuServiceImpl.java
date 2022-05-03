package com.xxxx.server.servive.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.server.mapper.MenuMapper;
import com.xxxx.server.mapper.MenuRoleMapper;
import com.xxxx.server.pojo.Menu;
import com.xxxx.server.pojo.MenuRole;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.servive.IMenuService;
import com.xxxx.server.utils.AdminUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lida
 * @since 2022-04-05
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MenuRoleMapper menuRoleMapper;

    /**
     * 通过用户id查询菜单列表
     * @return
     */
    @Override
    public List<Menu> getMenusByAdminId() {
        //获取上下文的 登录用户的 AdminID
        Integer adminId = AdminUtils.getCurrentAdmin().getId();
        //先去redis取值
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        List menus = (List<Menu>)valueOperations.get("menu_" + adminId);
        //如果redis查询结果为空
        if(CollectionUtils.isEmpty(menus)){
            //去mysql进行查询
            System.out.println("redis中没有值，开始查询mysql");
            List<Menu> menusByAdminId = this.baseMapper.getMenusByAdminId(adminId);
            //查询完毕后 查询到menus菜单放到redis
            valueOperations.set("menu_"+adminId,menusByAdminId);
            return menusByAdminId;
        }
        return menus;
    }

    /**
     * 根据角色获取菜单列表
     * @return
     */
    @Override
    public List<Menu> getMenusByRole() {

        return this.baseMapper.getMenusByRole();
    }


    /**
     * 查询所有菜单
     * @return
     */
    @Override
    public List<Menu> getAllMenus() {
        return this.baseMapper.getAllMenus();
    }


    /**
     * 权限菜单更新功能实现
     * @param rid
     * @param mids
     * @return
     */
    @Override
    @Transactional
    public RespBean updateMenusRole(Integer rid, Integer[] mids) {
        //删除所有菜单  先将角色名下的所有菜单进行删除
        menuRoleMapper.delete(new QueryWrapper<MenuRole>().eq("rid",rid));
        if(null == mids || 0 ==mids.length){
            return RespBean.success("更新成功");
        }
        //否则 就需要插入 更新角色菜单
        Integer result = this.baseMapper.insertRecored(rid, mids);
        if(result==mids.length){
            return RespBean.success("更新成功");
        }
        return RespBean.error("更新失败");
    }


}

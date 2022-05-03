package com.xxxx.server.servive;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.server.pojo.Menu;
import com.xxxx.server.pojo.RespBean;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lida
 * @since 2022-04-05
 */
public interface IMenuService extends IService<Menu> {

    /**
     * 通过用户id查询菜单
     * @return
     */
    List<Menu> getMenusByAdminId();

    /**
     * 根据角色获取菜单列表
     * @return
     */
    List<Menu> getMenusByRole();

    /**
     * 查询所有菜单
     * @return
     */
    List<Menu> getAllMenus();

    /**
     * 权限菜单更新功能实现
     * @param rid
     * @param mids
     * @return
     */
    RespBean updateMenusRole(Integer rid, Integer[] mids);
}

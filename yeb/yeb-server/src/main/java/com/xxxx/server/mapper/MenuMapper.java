package com.xxxx.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.server.pojo.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lida
 * @since 2022-04-05
 */
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据用户id查询菜单列表
     * @param id
     * @return
     */
    List<Menu> getMenusByAdminId(Integer id);

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
     * 更新角色菜单
     * @param rid
     * @param mids
     */
    Integer insertRecored(@Param("rid") Integer rid, @Param("mids") Integer[] mids);
}

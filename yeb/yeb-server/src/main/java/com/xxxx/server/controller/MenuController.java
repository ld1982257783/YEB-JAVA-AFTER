package com.xxxx.server.controller;


import com.xxxx.server.pojo.Menu;
import com.xxxx.server.servive.IMenuService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lida
 * @since 2022-04-05
 */
@RestController
@RequestMapping("/system/config")
public class MenuController {

    @Autowired
    private IMenuService menuService;

    @ApiOperation(value = "通过用户id查询菜单列表")
    @GetMapping("/menu")
    public List<Menu> getMenusByAdminId(){
        return menuService.getMenusByAdminId();
    }


//    @ApiModelProperty(value = "通过用户id")
//    @GetMapping("/menu")
//    public List<Menu> getMenusByRoleId(){
//        return menuService.getMenusByRoleId();
//    }

}

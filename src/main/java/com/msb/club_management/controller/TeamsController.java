package com.msb.club_management.controller;


import com.msb.club_management.handle.CacheHandle;
import com.msb.club_management.msg.PageData;
import com.msb.club_management.msg.R;
import com.msb.club_management.service.TeamsService;
import com.msb.club_management.service.UsersService;
import com.msb.club_management.vo.Teams;
import com.msb.club_management.vo.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;



import java.util.List;

/**
 * 系统请求响应控制器
 * 社团信息
 */
@Controller
@RequestMapping("/teams")
public class TeamsController extends BaseController {

    protected static final Logger Log = LoggerFactory.getLogger(TeamsController.class);

    @Autowired
    private CacheHandle cacheHandle;

    @Autowired
    private UsersService usersService;

    @Autowired
    private TeamsService teamsService;

    @RequestMapping("")
    public String index() {

        return "pages/Teams";
    }


    @GetMapping("/all")
    @ResponseBody
    public R getAll(){

        Log.info("获取全部的社团");

        List<Teams> list = teamsService.getAllTeams();

        return R.successData(list);
    }

    @GetMapping("/man")
    @ResponseBody
    public R getListByManId(String manId){

        Log.info("获取指定社团管理员相关的社团列表");

        List<Teams> list=teamsService.getListByManId(manId);

        return R.successData(list);
    }

    @GetMapping("/page")
    @ResponseBody
    public R getPageInfos(Long pageIndex, Long pageSize,
                          String token, Teams teams) {

        Users user = usersService.selectUser(cacheHandle.getUserInfoCache(token));

        if(ObjectUtils.isEmpty(user)) {
            return R.error("登录信息不存在，请重新登录");
        }
        if(user.getType() == 1){

            teams.setManager(user.getId());
        }

        Log.info("分页查找社团信息，当前页码：{}，"
                        + "每页数据量：{}, 模糊查询，附加参数：{}", pageIndex,
                pageSize, teams);

        PageData page = teamsService.getPageInfo(pageIndex, pageSize, teams);

        return R.successData(page);
    }

}
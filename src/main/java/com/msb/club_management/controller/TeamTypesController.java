package com.msb.club_management.controller;

import com.msb.club_management.msg.PageData;
import com.msb.club_management.msg.R;
import com.msb.club_management.service.TeamTypesService;
import com.msb.club_management.vo.TeamTypes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/teamTypes")
public class TeamTypesController extends BaseController{

    protected static final Logger Log = LoggerFactory.getLogger(TeamTypesController.class);

    @Autowired
    private TeamTypesService teamTypesService;

    @RequestMapping("")
    public String index() {

        return "pages/TeamTypes";
    }

    @GetMapping("/all")
    @ResponseBody
    public R getAll(){

        Log.info("查看全部的社团类型信息");

        List<TeamTypes> list = teamTypesService.getAllTeamTypes();

        return R.successData(list);
    }

    @GetMapping("/page")
    @ResponseBody
    public R getPageInfos(Long pageIndex, Long pageSize,
                          TeamTypes teamTypes) {

        Log.info("分页查找社团类型，当前页码：{}，"
                        + "每页数据量：{}, 模糊查询，附加参数：{}", pageIndex,
                pageSize, teamTypes);

        PageData page = teamTypesService.getPageInfo(pageIndex, pageSize, teamTypes);

        return R.successData(page);
    }
}

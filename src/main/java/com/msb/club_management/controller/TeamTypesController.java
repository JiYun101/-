package com.msb.club_management.controller;

import com.msb.club_management.handle.CacheHandle;
import com.msb.club_management.msg.PageData;
import com.msb.club_management.msg.R;
import com.msb.club_management.service.TeamTypesService;
import com.msb.club_management.service.TeamsService;
import com.msb.club_management.service.UsersService;
import com.msb.club_management.utils.DateUtils;
import com.msb.club_management.utils.IDUtils;
import com.msb.club_management.vo.TeamTypes;

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

@Controller
@RequestMapping("/teamTypes")
public class TeamTypesController extends BaseController{

    protected static final Logger Log = LoggerFactory.getLogger(TeamTypesController.class);

    // 注入用户服务
    @Autowired
    private UsersService usersService;

    // 注入缓存处理服务
    @Autowired
    private CacheHandle cacheHandle;


    @Autowired
    private TeamTypesService teamTypesService;

    @Autowired
    private TeamsService teamsService;



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
    public R getPageInfos(Long pageIndex, Long pageSize,String token,
                          TeamTypes teamTypes) {

        // 根据令牌获取用户信息
        Users user = usersService.selectUser(cacheHandle.getUserInfoCache(token));
        // 验证用户信息是否存在
        if(ObjectUtils.isEmpty(user)) {
            return R.error("用户未登录！");
        }

        Log.info("分页查找社团类型，当前页码：{}，"
                        + "每页数据量：{}, 模糊查询，附加参数：{}", pageIndex,
                pageSize, teamTypes);

        PageData page = teamTypesService.getPageInfo(pageIndex, pageSize, teamTypes);

        return R.successData(page);
    }

    /**
     * 更新团队类型信息
     *
     * @param teamTypes 团队类型对象，包含需要更新的团队类型信息
     * @return 返回操作结果，成功则返回一个成功标志
     */
    @PostMapping("/upd")
    @ResponseBody
    public R updInfo(TeamTypes teamTypes) {
        // 记录日志，打印更新团队类型的操作信息
        Log.info("修改社团类型，传入参数：{}", teamTypes);

        // 设置更新时间为当前时间
        teamTypes.setUpdateTime(DateUtils.getNowDate());
        // 调用服务层方法，执行更新操作
        teamTypesService.update(teamTypes);

        // 返回操作成功的标志
        return R.success();
    }

    @PostMapping("/add")
    @ResponseBody
    public R addInfo(TeamTypes teamTypes) {
        Log.info("添加社团类型，传入参数：{}", teamTypes);
        String name = teamTypes.getName();
        TeamTypes teamTypes1 = teamTypesService.selectTeamTypesByName(name);
        if (teamTypes1!=null){
            return R.error("添加失败！已有该类型");
        }
        teamTypes.setCreateTime(DateUtils.getNowDate());
        teamTypes.setUpdateTime(DateUtils.getNowDate());
        teamTypes.setState("1");
        teamTypes.setId(IDUtils.makeIDByCurrent());
        teamTypesService.add(teamTypes);
        return R.success();
    }

    @PostMapping("/del")
    @ResponseBody
    public R delInfo(String id) {
        if(teamTypesService.isRemove(id)){

            Log.info("删除社团类型, ID:{}", id);

            TeamTypes teamTypes = teamTypesService.getOne(id);

            teamTypes.setState("0");
            teamTypesService.update(teamTypes);

            return R.success();
        }else{

            return R.warn("存在关联社团，无法移除");
        }
    }
}

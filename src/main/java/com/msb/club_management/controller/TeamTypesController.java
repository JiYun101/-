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

    /**
     * 查看全部的社团类型信息
     * 本段代码首先记录一条信息，然后调用服务获取所有社团类型的列表，并将这个列表作为成功数据返回。
     */
    @GetMapping("/all")
    @ResponseBody
    public R getAll(){

        // 记录查看全部社团类型信息的操作
        Log.info("查看全部的社团类型信息");
        // 调用服务，获取所有社团类型的列表
        List<TeamTypes> list = teamTypesService.getAllTeamTypes();
        // 返回操作成功的结果，并包含社团类型列表数据
        return R.successData(list);

    }

    /**
     * 获取社团类型信息的分页数据。
     * @param pageIndex 当前页码
     * @param pageSize 每页的数据量
     * @param token 用户的令牌，用于验证用户身份
     * @param teamTypes 社团类型的查询条件
     * @return 返回分页数据的成功响应，包含查询到的社团类型信息
     */
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

        // 记录查询日志
        Log.info("分页查找社团类型，当前页码：{}，"
                        + "每页数据量：{}, 模糊查询，附加参数：{}", pageIndex,
                pageSize, teamTypes);

        // 执行分页查询操作
        PageData page = teamTypesService.getPageInfo(pageIndex, pageSize, teamTypes);

        // 返回查询结果
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
        // 记录添加社团类型的日志
        Log.info("添加社团类型，传入参数：{}", teamTypes);
        // 获取社团类型的名称
        String name = teamTypes.getName();
        // 根据名称查询是否已存在该社团类型
        TeamTypes teamTypes1 = teamTypesService.selectTeamTypesByName(name);
        if (teamTypes1!=null){
            // 如果已存在，则返回添加失败的信息
            return R.error("添加失败！已有该类型");
        }
        // 设置创建时间和更新时间为当前时间
        teamTypes.setCreateTime(DateUtils.getNowDate());
        teamTypes.setUpdateTime(DateUtils.getNowDate());
        // 设置状态为启用（1）
        teamTypes.setState("1");
        // 生成并设置唯一标识ID
        teamTypes.setId(IDUtils.makeIDByCurrent());
        // 添加社团类型到数据库
        teamTypesService.add(teamTypes);
        // 返回添加成功的信息
        return R.success();
    }


    /**
     * 删除社团类型信息。
     *
     * @param id 社团类型的唯一标识符。
     * @return 返回一个操作结果对象，成功则返回成功信息，失败则返回警告信息和具体原因。
     */
    @PostMapping("/del")
    @ResponseBody
    public R delInfo(String id) {
        // 检查该社团类型是否可以被删除
        if(teamTypesService.isRemove(id)){

            // 记录删除操作的日志
            Log.info("删除社团类型, ID:{}", id);

            // 获取对应的社团类型对象
            TeamTypes teamTypes = teamTypesService.getOne(id);

            // 设置该社团类型为已删除状态
            teamTypes.setState("0");
            // 更新社团类型的状态
            teamTypesService.update(teamTypes);

            // 返回操作成功的响应
            return R.success();
        }else{

            // 返回操作失败的响应，说明存在关联社团，无法删除
            return R.warn("存在关联社团，无法移除");
        }
    }

}

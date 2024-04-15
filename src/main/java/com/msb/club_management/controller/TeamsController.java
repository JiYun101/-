package com.msb.club_management.controller;

import com.msb.club_management.handle.CacheHandle;
import com.msb.club_management.msg.PageData;
import com.msb.club_management.msg.R;
import com.msb.club_management.service.TeamsService;
import com.msb.club_management.service.UsersService;
import com.msb.club_management.utils.DateUtils;
import com.msb.club_management.utils.IDUtils;
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

    @GetMapping("/info")
    @ResponseBody
    public R getInfo(String id){
        Log.info("获取社团信息,ID：{}", id);
        Teams teams= teamsService.getOne(id);
        return R.successData(teams);
    }

    @GetMapping("/all")
    @ResponseBody
    public R getAll(){

        Log.info("获取全部的社团");

        List<Teams> list = teamsService.getAllTeams();

        return R.successData(list);
    }



    @GetMapping("/page")
    @ResponseBody
    public R getPageInfos(Long pageIndex, Long pageSize,
                          String token, Teams teams) {

        Users user = usersService.selectUser(cacheHandle.getUserInfoCache(token));

        // 验证用户信息是否存在
        if(ObjectUtils.isEmpty(user)) {
            return R.error("用户未登录！");
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

    /**
     * 根据团长ID获取社团信息
     */
    @GetMapping("/man")
    @ResponseBody
    public R getListByManId(String manId) {

        Log.info("根据团长ID获取社团信息，团长ID：{}", manId);

        List<Teams> list = teamsService.getListByManId(manId);

        return R.successData(list);
    }

    /**
     * 添加社团
     */
    @PostMapping("/add")
    @ResponseBody
    public R addTeams(Teams teams) {

        teams.setId(IDUtils.makeIDByCurrent());
        teams.setCreateTime(DateUtils.getNowDate("yyyy-MM-dd"));
        teams.setState("1");
        teams.setUpdateTime(DateUtils.getNowDate("yyyy-MM-dd"));
        Log.info("添加社团信息，参数：{}", teams);

        int count=teamsService.addTeams(teams);
        if (count==0){
            return R.error("添加失败！！");
        }
        if (count==3){
            return R.error("该用户不存在！！");
        }
        return R.success();
    }

    /**
     * 修改社团信息
     */
    @PostMapping("/upd")
    @ResponseBody
    public R updateTeams(Teams teams){
        teams.setUpdateTime(DateUtils.getNowDate("yyyy-MM-dd"));

        Log.info("修改社团信息，参数：{}", teams);

        int count=teamsService.updateTeams(teams);
        if (count==0){
            return R.error("更新社团信息失败！");
        }
        if (count==3){
            return R.error("该用户不存在！！");
        }
        return R.success();
    }

    @PostMapping("/del")
    @ResponseBody
    public R deleteTeams(String id){

        Log.info("删除社团信息，ID：{}", id);

        //根据id查询要删除的社团数据
        Teams teams=teamsService.getOne(id);
        teams.setState("0");
        //查询当前社团的团长是否有两个或两个以上的社团
        //如果只有1个，则判断团长是否还有其他社团
        if (teamsService.getListByManId(teams.getManager()).size()>1){
                //如果还有其他社团，则直接删除
                teamsService.update(teams);
            }else {
                //如果只有1个社团，则把团长的类型改为2
                Users user=usersService.getOne(teams.getManager());
                user.setType(2);
                usersService.update(user);
                teamsService.update(teams);
           }
        //执行删除操作
        return R.success();
    }
}
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
        // 记录日志：获取指定ID的社团信息
        Log.info("获取社团信息,ID：{}", id);
        // 从服务层获取指定ID的社团信息
        Teams teams= teamsService.getOne(id);
        // 构建并返回操作成功的响应结果，包含获取到的社团信息
        return R.successData(teams);

    }

    @GetMapping("/all")
    @ResponseBody
    public R getAll(){

        /**
         * 获取全部的社团信息
         *
         * 参数: 无
         * 返回值: 包含所有社团信息的列表
         */
        Log.info("获取全部的社团");

        // 从服务层获取所有社团的信息列表
        List<Teams> list = teamsService.getAllTeams();

        // 返回操作成功的结果，并附带社团信息列表
        return R.successData(list);

    }



    @GetMapping("/page")
    @ResponseBody
    public R getPageInfos(Long pageIndex, Long pageSize,
                          String token, Teams teams) {

        /**
         * 根据用户token查询用户信息，并分页查询社团信息。
         *
         * @param token 用户的令牌，用于获取用户信息。
         * @param pageIndex 当前页码，用于分页查询。
         * @param pageSize 每页的数据量，用于分页查询。
         * @return 返回查询结果，如果用户未登录返回错误信息，否则返回分页的社团信息。
         */
        Users user = usersService.selectUser(cacheHandle.getUserInfoCache(token));

        // 验证用户信息是否存在
        if(ObjectUtils.isEmpty(user)) {
            return R.error("用户未登录！");
        }
        // 如果用户类型为1，设置其为团队管理员
        if(user.getType() == 1){

            teams.setManager(user.getId());
        }

        // 记录查询日志
        Log.info("分页查找社团信息，当前页码：{}，"
                        + "每页数据量：{}, 模糊查询，附加参数：{}", pageIndex,
                pageSize, teams);

        // 执行分页查询操作
        PageData page = teamsService.getPageInfo(pageIndex, pageSize, teams);

        // 返回查询结果
        return R.successData(page);
    }


    /**
     * 根据团长ID获取社团信息
     */
    @GetMapping("/man")
    @ResponseBody
    public R getListByManId(String manId) {

        // 记录日志：根据团长ID获取社团信息，团长ID为{manId}
        Log.info("根据团长ID获取社团信息，团长ID：{}", manId);

        // 根据团长ID获取社团列表
        List<Teams> list = teamsService.getListByManId(manId);

        // 返回获取到的社团列表信息
        return R.successData(list);

    }

    /**
     * 添加社团
     */
    @PostMapping("/add")
    @ResponseBody
    public R addTeams(Teams teams) {

        // 设置社团基本属性
        teams.setId(IDUtils.makeIDByCurrent()); // 为社团设置唯一标识ID
        teams.setCreateTime(DateUtils.getNowDate("yyyy-MM-dd")); // 设置创建时间为当前日期
        teams.setState("1"); // 设置社团状态为1（通常表示正常或激活状态）
        teams.setUpdateTime(DateUtils.getNowDate("yyyy-MM-dd")); // 设置更新时间为当前日期
        Log.info("添加社团信息，参数：{}", teams); // 记录添加社团信息的日志

        // 尝试添加社团到数据库
        int count=teamsService.addTeams(teams); // 添加社团到数据库
        // 处理添加结果
        if (count==0){
            return R.error("添加失败！！"); // 添加失败，返回错误信息
        }
        if (count==3){
            return R.error("该用户不存在！！"); // 用户不存在，返回错误信息
        }
        return R.success(); // 添加成功，返回成功信息

    }

    /**
     * 修改社团信息
     */
    @PostMapping("/upd")
    @ResponseBody
    public R updateTeams(Teams teams){
        // 更新社团信息的更新时间
        teams.setUpdateTime(DateUtils.getNowDate("yyyy-MM-dd"));

        // 记录日志，包括当前更新的社团信息
        Log.info("修改社团信息，参数：{}", teams);

        // 调用服务层方法，更新社团信息
        int count=teamsService.updateTeams(teams);
        // 更新失败处理
        if (count==0){
            return R.error("更新社团信息失败！");
        }
        // 不存在该用户处理
        if (count==3){
            return R.error("该用户不存在！！");
        }
        // 更新成功，返回成功信息
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
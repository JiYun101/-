package com.msb.club_management.controller;

import com.msb.club_management.handle.CacheHandle;
import com.msb.club_management.msg.PageData;
import com.msb.club_management.msg.R;
import com.msb.club_management.service.ApplyLogsService;
import com.msb.club_management.service.MembersService;
import com.msb.club_management.service.UsersService;
import com.msb.club_management.utils.DateUtils;
import com.msb.club_management.utils.IDUtils;
import com.msb.club_management.vo.ApplyLogs;
import com.msb.club_management.vo.Members;
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
 * 申请记录
 */
@Controller
@RequestMapping("/applyLogs")
public class ApplyLogsController extends BaseController{

    protected static final Logger Log= LoggerFactory.getLogger(ApplyLogsController.class);

    @Autowired
    private CacheHandle cacheHandle;

    @Autowired
    private UsersService usersService;

    @Autowired
    private ApplyLogsService applyLogsService;

    @Autowired
    private MembersService membersService;

    @RequestMapping("")
    public String index(){
        return "pages/ApplyLogs";
    }

    /**
     * 获取指定申请记录的信息。
     *
     * @param id 申请记录的唯一标识符。
     * @return 返回一个包含申请记录详情的响应对象。
     */
    @GetMapping("/info")
    @ResponseBody
    public R getInfo(String id){

        // 记录查询操作日志
        Log.info("查找指定申请记录，ID:{}",id);
        // 根据ID查询申请记录
        ApplyLogs applyLogs=applyLogsService.getOne(id);
        // 返回查询结果
        return R.successData(applyLogs);
    }

    /**
     * 根据用户权限和查询条件获取申请记录信息。
     *
     * @param pageIndex 当前页码
     * @param pageSize 每页数据量
     * @param token 用户的token，用于验证用户身份
     * @param teamName 查询条件中的团队名称，可模糊查询
     * @param userName 查询条件中的用户姓名，可模糊查询
     * @return 返回申请记录的信息页面，封装在R对象中。如果用户不存在，返回错误信息。
     */
    @ResponseBody
    @GetMapping("/page")
    public R getInfos(Long pageIndex,Long pageSize,String token,String teamName,String userName){
        // 根据token获取用户信息
        Users user=usersService.getOne(cacheHandle.getUserInfoCache(token));
        // 验证用户信息是否存在
        if(ObjectUtils.isEmpty(user)) {
            return R.error("用户未登录！");
        }
        // 用户类型为0时，查询全部申请记录
        if (user.getType()==0){
            Log.info("分页查看全部申请记录，当前页码：{},"+"每页数据量：{},模糊查询，团队名称：{}，用户姓名：{}",pageIndex,pageSize,teamName,userName);
            PageData page=applyLogsService.getPageInfo(pageIndex,pageSize,null,teamName,userName);
            return R.successData(page);
        }
        // 用户类型为1时，团队管理员查看申请记录
        else if (user.getType()==1){
            Log.info("团队管理员查看申请记录，当前页码：{}，"+"每页数据量：{}，模糊查询，团队名称：{}，用户姓名：{}",pageIndex,pageSize,teamName,userName);
            PageData page=applyLogsService.getManPageInfo(pageIndex,pageSize,user.getId(),teamName,userName);
            return R.successData(page);
        }else {
            // 其他用户类型，查询用户相关的申请记录
            Log.info("分页用户相关申请记录，当前页码：{}，"+"每页数据量：{},模糊查询，团队名称：{}。用户姓名：{}",pageIndex,pageSize,teamName,userName);
            PageData page=applyLogsService.getPageInfo(pageIndex,pageSize,user.getId(),teamName,null);
            return R.successData(page);
        }
    }

    /**
     * 添加申请信息
     * @param token 用户的令牌，用于识别用户身份
     * @param applyLogs 申请日志对象，包含申请加入团队的相关信息
     * @return 返回一个结果对象，可能是成功提示、警告信息或错误信息
     */
    @PostMapping("/add")
    @ResponseBody
    public R addInfo(String token,ApplyLogs applyLogs){
        // 根据token获取用户信息，并提取用户ID
        Users user=usersService.getOne(cacheHandle.getUserInfoCache(token));
        String id = user.getId();
        // 检查该用户是否已经是申请团队的成员
        Integer i = membersService.selectMembers(id,applyLogs.getTeamId());

        // 如果用户已经是团队成员，则返回警告信息
        if (i>0){
            return R.warn("你已经加入该团队，请勿重复申请");
        }
        // 检查用户是否已经提交过加入申请
        if (applyLogsService.isApply(user.getId(),applyLogs.getTeamId())){
            // 设置申请日志的ID、用户ID、创建时间和更新时间
            applyLogs.setId(IDUtils.makeIDByCurrent());
            applyLogs.setUserId(user.getId());
            applyLogs.setCreateTime(DateUtils.getNowDate());
            applyLogs.setUpdateTime(DateUtils.getNowDate());
            // 记录申请日志信息
            Log.info("添加申请记录，传入参数：{}",applyLogs);

            // 添加申请记录到数据库
            applyLogsService.add(applyLogs);

            // 返回成功提示信息
            return R.success();
        }else {
            // 如果用户已有申请正在审核中，则返回警告信息
            return R.warn("申请审核中，请耐心等候");
        }
    }


    /**
     * 修改申请记录
     * @param applyLogs
     * @return
     */
    @PostMapping("/upd")
    @ResponseBody
    public R updInfo(ApplyLogs applyLogs){

        Log.info("添加申请记录，传入参数：{}",applyLogs);

        applyLogs.setUpdateTime(DateUtils.getNowDate());

        applyLogsService.updateApplyStatus(applyLogs);

        return R.success();
    }

    /**
     * 删除申请记录
     * @param id
     * @return
     */
    @PostMapping("/del")
    @ResponseBody
    public R delInfo(String id){

        ApplyLogs applyLogs=applyLogsService.getOne(id);

        applyLogsService.delete(applyLogs);

        return R.success();
    }
}

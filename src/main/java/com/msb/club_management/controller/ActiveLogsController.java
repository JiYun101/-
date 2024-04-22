package com.msb.club_management.controller;

import com.msb.club_management.handle.CacheHandle;
import com.msb.club_management.msg.R;
import com.msb.club_management.service.ActiveLogsService;
import com.msb.club_management.service.UsersService;
import com.msb.club_management.utils.DateUtils;
import com.msb.club_management.utils.IDUtils;
import com.msb.club_management.vo.ActiveLogs;
import com.msb.club_management.vo.Activities;
import com.msb.club_management.vo.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/activeLogs")
public class ActiveLogsController extends BaseController{
    protected static final Logger Log = LoggerFactory.getLogger(ActiveLogsController.class);
    @Autowired
    private CacheHandle cacheHandle;

    @Autowired
    private UsersService usersService;

    @Autowired
    private ActiveLogsService activeLogsService;



    @RequestMapping("")
    public String index() {

        return "pages/ActiveLogs";
    }


    @GetMapping("/info")
    @ResponseBody
    public R getInfo(String id) {

        // 记录查询特定报名记录的日志信息
        Log.info("查找指定报名记录，ID：{}", id);

        // 根据ID从服务中获取特定的活动日志记录
        ActiveLogs activeLogs = activeLogsService.getOne(id);

        // 返回查询结果的成功数据响应
        return R.successData(activeLogs);

    }
    @GetMapping("/list")
    @ResponseBody
    public R getList(String activeId) {

        // 记录获取指定活动报名记录的日志信息，活动ID通过format占位符输出
        Log.info("获取指定活动的报名记录，活动ID：{}", activeId);

        // 根据活动ID从活动日志服务中获取报名记录列表
        List<Map<String, Object>> list = activeLogsService.getListByActiveId(activeId);

        // 返回报名记录列表的成功响应
        return R.successData(list);

    }

    /**
     * 添加信息到活动日志
     * @param token 用户的令牌，用于识别用户身份
     * @param activeLogs 活动日志对象，包含活动的相关信息
     * @return 返回操作结果，成功则返回成功信息，失败则返回警告信息
     */
    @PostMapping("/add")
    @ResponseBody
    public R addInfo(String token,ActiveLogs activeLogs){
        // 根据token获取用户信息
        Users user=usersService.getOne(cacheHandle.getUserInfoCache(token));
        // 检查用户是否已经参与了该活动
        if (activeLogsService.isActive(activeLogs.getActiveId(),user.getId())){
            // 为活动日志设置ID、用户ID和创建时间
            activeLogs.setId(IDUtils.makeIDByCurrent());
            activeLogs.setUserId(user.getId());
            activeLogs.setCreateTime(DateUtils.getNowDate());

            // 记录添加报名记录的日志
            Log.info("添加报名记录，传入参数：{}",activeLogs);
            // 添加活动日志到数据库
            activeLogsService.add(activeLogs);
            // 返回成功结果
            return R.success();
        }else {
            // 返回警告结果，提示用户已参与该活动
            return R.warn("该活动您以参与，请勿重复报名");
        }
    }

    @PostMapping("/upd")
    @ResponseBody
    public R  updInfo(ActiveLogs activeLogs){
        Log.info("修改报名记录，传入参数：{}",activeLogs);
        activeLogsService.update(activeLogs);
        return R.success();
    }

    @PostMapping("/del")
    @ResponseBody
    public R delInfo(String id){
        Log.info("删除报名记录，ID：{}",id);
        ActiveLogs activeLogs=activeLogsService.getOne(id);
        activeLogsService.delete(activeLogs);
        return R.success();
    }
}

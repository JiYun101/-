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

        Log.info("查找指定报名记录，ID：{}", id);

        ActiveLogs activeLogs = activeLogsService.getOne(id);

        return R.successData(activeLogs);
    }
    @GetMapping("/list")
    @ResponseBody
    public R getList(String activeId) {

        Log.info("获取指定活动的报名记录，活动ID：{}", activeId);

        List<Map<String, Object>> list = activeLogsService.getListByActiveId(activeId);

        return R.successData(list);
    }

    /**
     * 添加报名记录
     * @param token
     * @param activeLogs
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public R addInfo(String token,ActiveLogs activeLogs){
        Users user=usersService.getOne(cacheHandle.getUserInfoCache(token));
        if (activeLogsService.isActive(activeLogs.getActiveId(),user.getId())){
            activeLogs.setId(IDUtils.makeIDByCurrent());
            activeLogs.setUserId(user.getId());
            activeLogs.setCreateTime(DateUtils.getNowDate());

            Log.info("添加报名记录，传入参数：{}",activeLogs);

            activeLogsService.add(activeLogs);

            return R.success();
        }else {
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

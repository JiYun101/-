package com.msb.club_management.controller;

import com.msb.club_management.handle.CacheHandle;
import com.msb.club_management.msg.PageData;
import com.msb.club_management.msg.R;
import com.msb.club_management.service.ApplyLogsService;
import com.msb.club_management.service.UsersService;
import com.msb.club_management.vo.ApplyLogs;
import com.msb.club_management.vo.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @RequestMapping("")
    public String index(){
        return "pages/ApplyLogs";
    }

    @GetMapping("/info")
    @ResponseBody
    public R getInfo(String id){
        Log.info("查找指定申请记录，ID:{}",id);
        ApplyLogs applyLogs=applyLogsService.getOne(id);
        return R.successData(applyLogs);
    }

    public R getInfos(Long pageIndex,Long pageSize,String token,String teamName,String userName){
        Users user=usersService.getOne(cacheHandle.getUserInfoCache(token));
        if (ObjectUtils.isEmpty(user)){
            return R.error("登录信息不存在，请重新输入");
        }
        if (user.getType()==0){
            Log.info("分页查看全部申请记录，当前页码：{},"+"每页数据量：{},模糊查询，团队名称：{}，用户姓名：{}",pageIndex,pageSize,teamName,userName);
            PageData page=applyLogsService.getPageInfo(pageIndex,pageSize,null,teamName,userName);
            return R.successData(page);
        }
        else if (user.getType()==1){
            Log.info("团队管理员查看申请记录，当前页码：{}，"+"每页数据量：{}，模糊查询，团队名称：{}，用户姓名：{}",pageIndex,pageSize,teamName,userName);
            PageData page=applyLogsService.getManPageInfo(pageIndex,pageSize,user.getId(),teamName,userName);
            return R.successData(page);
        }else {
            Log.info("分页用户相关申请记录，当前页码：{}，"+"每页数据量：{},模糊查询，团队名称：{}。用户姓名：{}",pageIndex,pageSize,teamName,userName);
            PageData page=applyLogsService.getPageInfo(pageIndex,pageSize,user.getId(),teamName,null);
            return R.successData(page);
        }
    }
}

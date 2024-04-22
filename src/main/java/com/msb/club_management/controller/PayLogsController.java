package com.msb.club_management.controller;



import com.msb.club_management.handle.CacheHandle;
import com.msb.club_management.msg.PageData;
import com.msb.club_management.msg.R;
import com.msb.club_management.service.PayLogsService;
import com.msb.club_management.service.UsersService;
import com.msb.club_management.utils.DateUtils;
import com.msb.club_management.utils.IDUtils;
import com.msb.club_management.vo.PayLogs;
import com.msb.club_management.vo.Users;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 系统请求响应控制器
 * 缴费记录
 */
@Controller
@RequestMapping("/payLogs")
public class PayLogsController extends BaseController{

    protected static final Logger Log= LoggerFactory.getLogger(PayLogsController.class);

    @Autowired
    private CacheHandle cacheHandle;

    @Autowired
    private UsersService usersService;

    @Autowired
    private PayLogsService payLogsService;

    @RequestMapping("")
    public String index(){
        return "pages/PayLogs";
    }


    @GetMapping("/info")
    @ResponseBody
    public R getInfo(String id){

        Log.info("查找指定缴费记录，ID：{}",id);

        PayLogs payLogs=payLogsService.getOne(id);

        return R.successData(payLogs);
    }

    @PostMapping("/add")
    @ResponseBody
    public R addInfo(PayLogs payLogs){
        //生成唯一的ID并设置给payLogs对象
        payLogs.setId(IDUtils.makeIDByCurrent());

        payLogs.setCreateTime(DateUtils.getNowDate());

        Log.info("添加缴费记录，{}",payLogs);

        payLogsService.add(payLogs);
        return R.success();
    }

    @GetMapping("/page")
    @ResponseBody
    public R getPageInfos(Long pageIndex, Long pageSize, String token,String teamName,String userName){
        /**
         * 根据用户类型分页查询缴费记录。
         * @param token 用户的令牌，用于获取用户信息。
         * @param pageIndex 当前页码。
         * @param pageSize 每页的数据量。
         * @param teamName 查询条件中的团队名称，可模糊查询。
         * @param userName 查询条件中的用户姓名，可模糊查询。
         * @return 返回缴费记录的分页信息。
         */
        Users user=usersService.getOne(cacheHandle.getUserInfoCache(token));
        if (ObjectUtils.isEmpty(user)){
            // 如果用户信息不存在，则返回登录信息不存在的错误提示
            return R.error("登录信息不存在，请重新登陆");
        }
        if (user.getType()==0){
            // 如果用户类型为0，查询全部缴费记录
            Log.info("分页查看全部缴费记录，当前页码：{}"+"每页数据量：{}，模糊查询，团队名称：{}，用户姓名：{}",pageIndex,pageSize,teamName,userName);
            PageData page=payLogsService.getPageInfo(pageIndex,pageSize,null,teamName,userName);
            return R.successData(page);
        }else if (user.getType()==1){
            // 如果用户类型为1，查询团队管理员的缴费记录
            Log.info("团队管理员查看缴费记录，当前页码：{}，"+ "每页数据量：{}, 模糊查询，团队名称：{}，用户姓名：{}", pageIndex, pageSize, teamName, userName);
            PageData page=payLogsService.getManPageInfo(pageIndex,pageSize,user.getId(),teamName,userName);
            return R.successData(page);
        }else {
            // 如果用户类型为其他，查询用户本人的缴费记录
            Log.info("分页用户相关缴费记录，当前页码：{}，"+ "每页数据量：{}, 模糊查询，团队名称：{}，用户姓名：{}", pageIndex, pageSize, teamName, userName);
            PageData page=payLogsService.getPageInfo(pageIndex,pageSize,user.getId(),teamName,null);
            return R.successData(page);
        }

    }

    @PostMapping("/del")
    @ResponseBody
    public R delInfo(String id) {

        // 记录删除缴费记录的操作日志
        Log.info("删除缴费记录, ID:{}", id);

        // 根据ID获取对应的缴费记录
        PayLogs payLogs = payLogsService.getOne(id);

        // 执行删除操作
        payLogsService.delete(payLogs);

        // 返回操作成功的响应
        return R.success();

    }
}

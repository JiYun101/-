package com.msb.club_management.controller;

import com.msb.club_management.handle.CacheHandle;
import com.msb.club_management.msg.PageData;
import com.msb.club_management.msg.R;
import com.msb.club_management.service.NoticesService;
import com.msb.club_management.service.UsersService;
import com.msb.club_management.utils.DateUtils;
import com.msb.club_management.utils.IDUtils;
import com.msb.club_management.utils.StringUtils;
import com.msb.club_management.vo.Notices;
import com.msb.club_management.vo.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;


import static com.msb.club_management.controller.UsersController.Log;

@Controller
@RequestMapping("/notices")
public class NoticesController extends BaseController{
    @Autowired
    private NoticesService noticesService;

    // 注入用户服务
    @Autowired
    private UsersService usersService;

    // 注入缓存处理服务
    @Autowired
    private CacheHandle cacheHandle;


    /**
     * 分页查找通知记录
     */
    @GetMapping("/page")
    @ResponseBody
    public R getPageInfos(Long pageIndex, Long pageSize,String token, String title, String teamName) {

        Users user = usersService.getOne(cacheHandle.getUserInfoCache(token));

        // 验证用户信息是否存在
        if(ObjectUtils.isEmpty(user)) {
            return R.error("用户未登录！");
        }
        if (user.getType()==0){
            Log.info("分页查找活动通知，当前页码：{}，每页数据量：{}, 模糊查询，标题：{}，社团名：{}", pageIndex, pageSize, title, teamName);
            PageData page=noticesService.getPageAll(pageIndex, pageSize, title, teamName);
            return R.successData(page);
        }else {

            Log.info("分页查找指定用户相关通知记录，当前页码：{}，每页数据量：{}, 模糊查询，标题：{}，社团名：{}", pageIndex, pageSize, title, teamName);

            // 调用服务层方法，获取分页信息
            PageData page = noticesService.getPageAll(pageIndex, pageSize, title, teamName);

            // 返回成功响应，携带分页数据
            return R.successData(page);
        }
    }

    /**
     * 添加通知信息
     *
     * @param notices 通知对象，包含通知的详细信息
     * @return 返回操作结果，成功则返回一个包含成功标识的R对象
     */
    @PostMapping("/add")
    @ResponseBody
    public R addNotice(Notices notices) {
        // 生成通知的唯一标识
        notices.setId(IDUtils.makeIDByCurrent());
        // 设置通知的创建时间为当前时间
        notices.setCreateTime(DateUtils.getNowDate("yyyy-MM-dd"));
        // 设置通知的状态为1（代表某种状态，具体含义根据业务逻辑定）
        notices.setState("1");
        // 检查notices对象中的teamId是否为空或空字符串
        // 如果是，则将其设置为null
        if(StringUtils.isNullOrEmpty(notices.getTeamId())){

            notices.setTeamId(null);
        }

        // 调用服务层方法，将通知信息添加到数据库
        noticesService.add(notices);

        return R.success(); // 返回操作成功的标识
    }

    /**
     * 删除通知信息
     */
    @PostMapping("/del")
    @ResponseBody
    public R delNotice(String id) {
        Notices notices = noticesService.getOne(id);
        notices.setState("0");
        noticesService.update(notices);

        return R.success(); // 返回操作成功的标识
    }
}

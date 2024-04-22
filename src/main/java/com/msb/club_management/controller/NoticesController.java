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
    /**
     * 根据用户信息和查询条件获取通知信息的分页数据。
     *
     * @param pageIndex 当前页码
     * @param pageSize 每页的数据量
     * @param token 用户的令牌，用于验证用户身份
     * @param title 查询条件中的通知标题（模糊查询）
     * @param teamName 查询条件中的社团名称（模糊查询）
     * @return 返回一个结果对象，其中包含分页数据或错误信息
     */
    public R getPageInfos(Long pageIndex, Long pageSize,String token, String title, String teamName) {

        Users user = usersService.getOne(cacheHandle.getUserInfoCache(token));

        // 验证用户信息是否存在
        if(ObjectUtils.isEmpty(user)) {
            return R.error("用户未登录！");
        }
        if (user.getType()==0){
            // 当用户类型为0时，查询活动通知
            Log.info("分页查找活动通知，当前页码：{}，每页数据量：{}, 模糊查询，标题：{}，社团名：{}", pageIndex, pageSize, title, teamName);
            PageData page=noticesService.getPageAll(pageIndex, pageSize, title, teamName);
            return R.successData(page);
        }else {
            // 当用户类型不为0时，查询指定用户相关通知
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
    /**
     * 添加一条通知信息。
     * @param notices 通知对象，包含通知的详细信息。
     * @return 返回操作结果，如果操作成功，则返回成功的标识。
     */
    public R addNotice(Notices notices) {
        // 生成并设置通知的唯一标识
        notices.setId(IDUtils.makeIDByCurrent());
        // 设置通知的创建时间为当前时间
        notices.setCreateTime(DateUtils.getNowDate("yyyy-MM-dd"));
        // 设置通知的状态为1（代表某种状态，具体含义根据业务逻辑定义）
        notices.setState("1");
        // 检查通知对象中的teamId是否为空或空字符串，如果是，则将其设置为null
        if(StringUtils.isNullOrEmpty(notices.getTeamId())){

            notices.setTeamId(null);
        }

        // 调用服务层方法，将通知信息添加到数据库
        noticesService.add(notices);

        return R.success(); // 返回操作成功的标识
    }


    /**
     * 删除通知信息
     * @param id 通知的唯一标识符
     * @return 返回操作结果，成功则返回一个成功的标识
     */
    @PostMapping("/del")
    @ResponseBody
    public R delNotice(String id) {
        // 根据id获取通知对象
        Notices notices = noticesService.getOne(id);
        // 设置通知状态为已删除（0）
        notices.setState("0");
        // 更新通知状态
        noticesService.update(notices);

        return R.success(); // 返回操作成功的标识
    }
}

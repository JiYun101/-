package com.msb.club_management.controller;


import com.msb.club_management.handle.CacheHandle;
import com.msb.club_management.msg.PageData;
import com.msb.club_management.msg.R;
import com.msb.club_management.service.ActivitiesService;
import com.msb.club_management.service.UsersService;
import com.msb.club_management.utils.IDUtils;
import com.msb.club_management.vo.Activities;
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

@Controller
@RequestMapping("/activities")
public class ActivitiesController extends BaseController{
    protected static final Logger Log = LoggerFactory.getLogger(ActivitiesController.class);
    @Autowired
    private ActivitiesService activitiesService;

    @Autowired
    private CacheHandle cacheHandle;

    @Autowired
    private UsersService usersService;



    @RequestMapping("")
    public String index() {

        return "pages/Activities";
    }

    @GetMapping("/info")
    @ResponseBody
    public R getInfo(String id) {

        Log.info("查找指定活动信息，ID：{}", id);

        Activities activities = activitiesService.getOne(id);

        return R.successData(activities);
    }



    /**
     * 获取活动信息分页数据。
     *
     * @param pageIndex 当前页码
     * @param pageSize 每页的数据量
     * @param token 用户的登录令牌，用于验证用户身份
     * @param teamName 社团名称，用于活动信息的模糊查询
     * @param activeName 活动名称，用于活动信息的模糊查询
     * @return 返回一个包含分页数据的结果对象，如果登录信息不存在，则返回错误信息
     */
    @GetMapping("/page")
    @ResponseBody
    public R getPageInfos(Long pageIndex, Long pageSize,
                          String token, String teamName, String activeName) {

        // 根据token获取用户信息，验证用户登录状态
        Users user = usersService.getOne(cacheHandle.getUserInfoCache(token));
        if (ObjectUtils.isEmpty(user)) {
            return R.error("登录信息不存在，请重新登录");
        }
        if (user.getType() == 0) {
            // 管理员查询所有活动信息
            Log.info("分页查找活动信息，当前页码：{}，"
                        + "每页数据量：{}, 模糊查询，社团名称：{}，活动名称：{}", pageIndex,
                    pageSize, teamName, activeName);

            PageData page = activitiesService.getPageAllActivities(pageIndex, pageSize, teamName, activeName);

            return R.successData(page);
        } else {
            // 普通用户查询自己参与的活动信息
            Log.info("分页查找活动信息，当前页码：{}，"
                        + "每页数据量：{}, 模糊查询，社团名称：{}，活动名称：{}", pageIndex,
                    pageSize, teamName, activeName);

            PageData page = activitiesService.getPageByUserId(pageIndex, pageSize, user.getId(), teamName, activeName);

            return R.successData(page);
        }
    }
    @PostMapping("/add")
    @ResponseBody
    public R addInfo(Activities activities) {

        activities.setId(IDUtils.makeIDByCurrent());

        Log.info("添加活动信息，传入参数：{}", activities);

        activitiesService.add(activities);

        return R.success();
    }

    @PostMapping("/del")
    @ResponseBody
    public R delInfo(String id){
        Log.info("删除活动信息，传入参数：{}", id);

        Activities activities = activitiesService.getOne(id);
        activities.setState("0");
        activitiesService.update(activities);

        return R.success();
    }


    @GetMapping("/upd")
    @ResponseBody
    public R add(Activities activities) {

        Log.info("添加活动信息，ID：{}", activities.getId());

        activitiesService.update(activities);

        return R.success();
    }
}

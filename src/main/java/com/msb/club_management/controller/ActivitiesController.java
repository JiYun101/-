package com.msb.club_management.controller;


import com.msb.club_management.handle.CacheHandle;
import com.msb.club_management.msg.PageData;
import com.msb.club_management.msg.R;
import com.msb.club_management.service.ActivitiesService;
import com.msb.club_management.service.UsersService;
import com.msb.club_management.vo.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/activities")
public class ActivitiesController extends BaseController{
    protected static final Logger Log = LoggerFactory.getLogger(ActivitiesController.class);

    @Autowired
    private ActivitiesService activitiesService;


    @Autowired
    private UsersService usersService;
    @Autowired
    private CacheHandle cacheHandle;
    @RequestMapping("")
    public String index() {

        return "pages/Activities";
    }

    @GetMapping("/page")
    @ResponseBody
    public R getPageInfos(Long pageIndex, Long pageSize,
                          String token, String teamName, String activeName) {

        Users user = usersService.getOne(cacheHandle.getUserInfoCache(token));
        if(ObjectUtils.isEmpty(user)) {
            return R.error("登录信息不存在，请重新登录");
        }
        if (user.getType() == 0) {

            Log.info("分页查找活动信息，当前页码：{}，"
                            + "每页数据量：{}, 模糊查询，社团名称：{}，活动名称：{}", pageIndex,
                    pageSize, teamName, activeName);

            PageData page = activitiesService.getPageAll(pageIndex, pageSize, teamName, activeName);

            return R.successData(page);
        } else {

            Log.info("分页查找活动信息，当前页码：{}，"
                            + "每页数据量：{}, 模糊查询，社团名称：{}，活动名称：{}", pageIndex,
                    pageSize, teamName, activeName);

            PageData page = activitiesService.getPageByUserId(pageIndex, pageSize, user.getId(), teamName, activeName);

            return R.successData(page);
        }
    }
}

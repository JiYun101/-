package com.msb.club_management.controller;

import com.msb.club_management.handle.CacheHandle;
import com.msb.club_management.msg.PageData;
import com.msb.club_management.msg.R;
import com.msb.club_management.service.MembersService;
import com.msb.club_management.service.NoticesService;
import com.msb.club_management.service.UsersService;
import com.msb.club_management.vo.Members;
import com.msb.club_management.vo.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/members")
@Controller
public class MembersController extends BaseController{

    // 注入用户服务
    @Autowired
    private UsersService usersService;

    // 注入缓存处理服务
    @Autowired
    private CacheHandle cacheHandle;

    @Autowired
    private MembersService membersService;


    /**
     *
     * @return
     */
    @RequestMapping("")
    public String index(){
        return "pages/members";
    }

    /**
     * 获取页面信息
     *
     * @param pageIndex 请求的页码
     * @param pageSize 每页显示的数量
     * @param token 用户的令牌，用于验证用户身份
     * @param teamName 团队名称，用于筛选特定团队的数据
     * @param userName 用户名，可选参数，用于筛选特定用户的数据显示
     * @return 返回一个包含页面数据的响应对象，如果用户信息不存在，则返回错误提示
     */
    @GetMapping("/page")
    @ResponseBody
    public R getPageInfo(Long pageIndex, Long pageSize,String token, String teamName, String userName){
        // 根据令牌获取用户信息
        Users user = usersService.getOne(cacheHandle.getUserInfoCache(token));
        // 验证用户信息是否存在
        if(ObjectUtils.isEmpty(user)) {
            return R.error("用户未登录！");
        }


        // 根据用户类型来查询不同的页面数据
        if (user.getType()==0){
            // 查询所有成员的页面数据
            PageData page=membersService.getPageAll(pageIndex,pageSize,teamName,userName);
            return R.successData(page);
        }else {
            // 查询管理的成员的页面数据
            PageData page=membersService.getPageMyManId(pageIndex,pageSize,user.getId(),teamName,userName);
            return R.successData(page);
        }
    }

    /**
     * 删除成员信息
     *
     * @param id 成员的唯一标识符
     * @return 返回操作结果，如果操作对象是社团管理员，则返回无法移除的警告；否则，删除成功后返回成功信息。
     */
    @PostMapping("/del")
    @ResponseBody
    public R del(String id){
        // 根据ID获取成员信息
        Members members = membersService.selectMembes(id);

        // 检查该成员是否为社团管理员
        if(membersService.isManager(members.getTeamId(), members.getUserId())){

            // 如果是管理员，返回无法移除的警告
            return R.warn("社团管理员无法移除");
        }else{
            // 非管理员则直接删除成员信息
            members.setState("0");
            membersService.update(members);
            // 返回删除成功的信息
            return R.success();
        }
    }
}

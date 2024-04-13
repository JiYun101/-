package com.msb.club_management.controller;

import com.msb.club_management.service.UsersService;
import com.msb.club_management.utils.Md5Util;
import com.msb.club_management.utils.PhoneUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.msb.club_management.utils.DateUtils;
import com.msb.club_management.utils.IDUtils;
import com.msb.club_management.msg.R;
import com.msb.club_management.msg.PageData;

import com.msb.club_management.vo.Users;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 系统请求响应控制器
 * 系统用户
 */
@Controller
@RequestMapping("/users")
public class UsersController extends BaseController {

    protected static final Logger Log = LoggerFactory.getLogger(UsersController.class);

    @Autowired
    private UsersService usersService;



    /**
     * 处理根路径的请求，重定向到用户页面。
     *
     * @return 返回值为字符串类型，指定重定向的页面路径。
     */
    @RequestMapping("")
    public String index() {

        // 返回用户页面的路径
        return "pages/Users";
    }

    /**
     * 获取指定用户的信息
     *
     * @param id 用户的唯一标识符
     * @return R 返回一个结果对象，其中包含用户信息
     */
    @GetMapping("/info")
    @ResponseBody
    public R getInfo(String id) {
        // 记录查询指定用户信息的日志
        Log.info("查找指定系统用户，ID：{}", id);

        // 通过用户服务查询用户信息
        Users users = usersService.getOne(id);

        // 返回成功响应，包含用户信息
        return R.successData(users);
    }

    /**
     * 分页获取系统用户信息
     *
     * @param pageIndex 当前页码
     * @param pageSize 每页的数据量
     * @param users 用户信息附加参数，用于模糊查询等
     * @return 返回分页后的用户信息数据
     */
    @GetMapping("/page")
    @ResponseBody
    public R getPageInfos(Long pageIndex, Long pageSize,
                          Users users) {

        // 记录查询日志
        Log.info("分页查找系统用户，当前页码：{}，"
                    + "每页数据量：{}, 模糊查询，附加参数：{}", pageIndex,
                pageSize, users);

        // 调用服务层方法，获取分页信息
        PageData page = usersService.getPageInfo(pageIndex, pageSize, users);

        // 返回成功响应，携带分页数据
        return R.successData(page);
    }

    /**
     * 添加用户信息
     *
     * @param users 用户信息对象，包含用户名等用户详细信息
     * @return 返回操作结果，如果用户不存在则添加用户并返回成功，如果用户已存在则返回警告信息
     */
    @PostMapping("/add")
    @ResponseBody
    public R addInfo(Users users) {
        // 检查用户是否已存在
        if(usersService.getUserByUserName(users.getUserName()) == null){
            // 初始化用户ID和设置创建时间
            users.setId(IDUtils.makeIDByCurrent());
            users.setCreateTime(DateUtils.getNowDate());
            users.setUpdateTime(DateUtils.getNowDate());
            //对手机号码进行校验
            if (PhoneUtil.isMobile(users.getPhone())!=true){
                return R.warn("手机号码格式错误");
            }
            // 对密码进行加密
            String encryptedPassword = Md5Util.encode(users.getPassWord());
            users.setPassWord(encryptedPassword);

            // 记录添加用户的操作日志
            Log.info("添加系统用户，传入参数：{}", users);

            // 调用服务层方法，添加用户
            usersService.add(users);

            // 返回添加成功的响应
            return R.success();
        }else{
            // 返回用户账号已存在的警告响应
            return R.warn("用户账号已存在，请重新输入");
        }
    }


    /**
     * 更新用户信息
     *
     * @param users 包含要更新的用户信息的对象
     * @return R 返回一个表示操作结果的对象，如果操作成功，则返回成功的标识
     */
    @PostMapping("/upd")
    @ResponseBody
    public R updInfo(Users users) {
        // 记录日志，打印更新用户信息的参数
        Log.info("修改系统用户，传入参数：{}", users);
        users.setUpdateTime(DateUtils.getNowDate());

        // 调用服务层方法，执行用户信息更新
        usersService.update(users);

        // 返回操作成功的标识
        return R.success();
    }

    /**
     * 删除用户信息
     *
     * @param id 用户ID
     * @return 返回操作结果，如果删除成功则返回成功信息，如果删除失败（存在关联社团）则返回警告信息
     */
    @PostMapping("/del")
    @ResponseBody
    public R delInfo(String id) {
        // 检查用户是否可以被删除
        if(usersService.isRemove(id)){
            // 记录删除操作日志
            Log.info("删除系统用户, ID:{}", id);
            // 获取用户信息
            Users users = usersService.getOne(id);
            // 执行删除操作
            users.setStatus(0);
            users.setUpdateTime(DateUtils.getNowDate());
            usersService.update(users);
            // 返回删除成功的结果
            return R.success();
        }else{
            // 返回删除失败（存在关联社团）的结果
            return R.warn("用户存在关联社团，无法移除");
        }
    }
}
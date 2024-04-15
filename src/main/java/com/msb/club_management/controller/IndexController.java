package com.msb.club_management.controller;

import com.msb.club_management.service.UsersService;
import com.msb.club_management.utils.DateUtils;
import com.msb.club_management.utils.Md5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import com.msb.club_management.vo.Notices;
import com.msb.club_management.handle.CacheHandle;
import com.msb.club_management.msg.R;
import com.msb.club_management.vo.Users;
import com.msb.club_management.service.NoticesService;
import com.msb.club_management.utils.IDUtils;

import javax.servlet.http.HttpSession;
import java.util.List;
/**
 * IndexController 类负责处理根路径("/")的请求。
 * 继承自 BaseController，获取其共通的控制器逻辑。
 */
@Controller
@RequestMapping("/")
public class IndexController extends BaseController {



    // 日志记录器
    private static final Logger Log = LoggerFactory.getLogger(IndexController.class);

    // 注入用户服务
    @Autowired
    private UsersService usersService;

    // 注入缓存处理服务
    @Autowired
    private CacheHandle cacheHandle;

    // 注入通知服务
    @Autowired
    private NoticesService noticesService;

    /**
     * 获取通知列表
     * @param token 用户令牌，用于验证用户身份和获取用户信息
     * @return 返回通知列表的成功数据，或错误信息。成功时返回通知列表，失败时返回错误消息
     */
    @GetMapping("/sys/notices")
    @ResponseBody
    public R getNoticeList(String token){

        // 根据令牌获取用户信息
        Users user = usersService.getOne(cacheHandle.getUserInfoCache(token));
        // 验证用户信息是否存在
        if(ObjectUtils.isEmpty(user)) {
            return R.error("用户未登录！");
        }

        Integer type = user.getType();
        // 根据用户类型获取相应的通知列表
        // 用户类型为0时，获取系统通知
        if(type == 0){
            List<Notices> list = noticesService.getSysNotices();
            return R.successData(list);
        // 用户类型为1时，获取管理组通知
        }else if(type == 1){
            List<Notices> list = noticesService.getManNotices(user.getId());
            return R.successData(list);
        // 其他类型用户，获取会员通知
        }else{
            List<Notices> list = noticesService.getMemNotices(user.getId());
            return R.successData(list);
        }
    }


    /**
     * 用户登录功能实现。
     * @param userName 用户输入的用户名。
     * @param passWord 用户输入的密码。
     * @param session 用户的HttpSession对象，用于会话管理。
     * @return 返回登录结果，成功则包含成功信息和令牌，失败则包含错误信息。
     */
    @PostMapping("/login")
    @ResponseBody
    public R login(String userName, String passWord, HttpSession session){

        // 记录用户登录尝试的日志
        Log.info("用户登录，用户名：{}， 用户密码：{}", userName, passWord);

        // 根据用户名查询用户信息
        Users user = usersService.getUserByUserName(userName);
        if(user == null) {
            // 如果用户不存在，则返回错误信息
            return R.error("输入的用户名不存在");
        }else {

            //对用户输入的密码进行加密处理（与注册时相同的加密方式）
            String encryptedPassWord = Md5Util.encode(passWord);

            //将加密后的密码与数据库中存储的加密密码进行比较
            if(encryptedPassWord.equals(user.getPassWord().trim())) {
                String token = IDUtils.makeIDByUUID();
                cacheHandle.addUserCache(token, user.getId());
                return R.success("登录成功", token);
            }else {
                // 如果密码错误，则返回错误信息
                return R.error("输入的密码错误");
            }
        }
    }


    /**
     * 用户退出
     * @param token 用户令牌
     * @return 返回成功信息
     */
    @RequestMapping("/exit")
    @ResponseBody
    public R exit(String token) {

        // 记录退出日志并移除登录信息
        Log.info("用户退出系统并移除登录信息");
        cacheHandle.removeUserCache(token);

        return R.success();
    }

    /**
     * 获取指定用户令牌的用户信息。
     * @param token 用户的令牌，用于识别和验证用户。
     * @return 如果成功获取用户信息，则返回包含用户信息的成功数据；否则返回错误信息。
     */
    @GetMapping("/info")
    @ResponseBody
    public R info(String token){
        // 通过令牌获取用户信息
        Users user = usersService.getOne(cacheHandle.getUserInfoCache(token));
        // 返回成功数据，包含用户信息
        return R.successData(user);
    }


    /**
     * 修改用户信息
     * @param user 用户信息对象，包含需要修改的用户信息
     * @return 返回操作结果，如果成功则返回成功信息
     */
    @PostMapping("/info")
    @ResponseBody
    public R info(Users user){

        // 记录修改用户信息的操作日志
        Log.info("修改用户信息，{}", user);
        user.setUpdateTime(DateUtils.getNowDate());
        // 调用服务层方法，更新用户信息
        usersService.update(user);
        // 返回操作成功的响应
        return R.success();

    }


    /**
     * 校验原密码是否正确
     * @param oldPwd 原密码, 需要用户输入的当前密码
     * @param token 用户令牌, 用于识别当前操作用户
     * @return 返回一个结果对象，如果密码正确则返回成功信息，否则返回警告信息
     */
    @RequestMapping("/checkPwd")
    @ResponseBody
    public R checkPwd(String oldPwd, String token) {
        // 根据用户令牌获取用户信息
        Users user = usersService.getOne(cacheHandle.getUserInfoCache(token));
        oldPwd = Md5Util.encode(oldPwd);
        // 检查输入的原密码是否与记录中的密码一致
        if(oldPwd.equals(user.getPassWord())) {
            // 返回密码校验成功的消息
            return R.success();
        }else {
            // 返回密码校验失败的警告消息
            return R.warn("原始密码和输入密码不一致");
        }
    }


    /**
     * 修改密码接口
     * @param token 用户令牌，用于验证用户身份。
     * @param password 新密码，用户设置的新密码。
     * @return 返回操作结果，如果操作成功，则返回成功信息。
     */
    @PostMapping("/pwd")
    @ResponseBody
    public R pwd(String token, String password) {

        // 记录修改密码的操作日志
        Log.info("修改用户密码，{}", password);

        // 根据token获取用户信息，并更新密码
        Users user = usersService.getOne(cacheHandle.getUserInfoCache(token));
        user.setPassWord(Md5Util.encode(password));
        user.setUpdateTime(DateUtils.getNowDate());
        usersService.update(user);

        // 返回操作成功的响应
        return R.success();

    }


}
package com.msb.club_management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.club_management.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.msb.club_management.dao.MembersDao;
import com.msb.club_management.vo.Members;
import com.msb.club_management.msg.PageData;
import com.msb.club_management.vo.Users;
import com.msb.club_management.dao.UsersDao;
import com.msb.club_management.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("usersService")
public class UsersServiceImpl implements UsersService {

    @Autowired
    private MembersDao membersDao;

    @Autowired
    private UsersDao usersDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void add(Users users) {

        usersDao.insert(users);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void update(Users users) {

        usersDao.updateById(users);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Users users) {

        usersDao.deleteById(users);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Boolean isRemove(String userId){

        QueryWrapper<Members> qw = new QueryWrapper<Members>();
        qw.eq("user_id", userId);

        Integer total = membersDao.selectCount(qw);

        return total <= 0;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Users getOne(String id) {

        Users users = usersDao.selectById(id);

        return users;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Users getUserByUserName(String userName) {

        QueryWrapper<Users> qw = new QueryWrapper<Users>();
        qw.eq("user_name", userName);

        Users user = usersDao.selectOne(qw);

        return user;
    }

    /**
     * 获取用户信息的分页数据。
     *
     * @param pageIndex 请求的页码。
     * @param pageSize 每页显示的数据条数。
     * @param users 包含用户搜索条件的对象，可包含用户名、真实姓名和电话。
     * @return 返回经过处理的分页数据对象，包含用户信息。
     */
    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public PageData getPageInfo(Long pageIndex, Long pageSize, Users users) {

        QueryWrapper<Users> qw = new QueryWrapper<Users>();

        // 根据用户名进行查询
        if (StringUtils.isNotNullOrEmpty(users.getUserName())) {
            qw.like("user_name", users.getUserName());
        }

        // 根据真实姓名进行查询
        if (StringUtils.isNotNullOrEmpty(users.getName())) {
            qw.like("name", users.getName());
        }

        // 根据电话进行查询
        if (StringUtils.isNotNullOrEmpty(users.getPhone())) {
            qw.like("phone", users.getPhone());
        }

        // 按照创建时间倒序排列
        qw.orderByDesc("create_time");

        // 只查询状态为正常的用户
        qw.eq("status", 1);

        // 执行分页查询
        Page<Users> page =
                usersDao.selectPage(new Page<Users>(pageIndex, pageSize), qw);

        // 解析并返回分页数据
        return parsePage(page);
    }

    @Override
    public Users selectUser(String userInfoCache) {
        Users users = usersDao.selectById(userInfoCache);
        return users;
    }

    /**
     * 转化分页查询的结果
     */
    public PageData parsePage(Page<Users> p) {

        List<Map<String, Object>> resl = new ArrayList<Map<String, Object>>();

        for (Users users : p.getRecords()) {

            Map<String, Object> temp = new HashMap<String, Object>();
            temp.put("id", users.getId());
            temp.put("userName", users.getUserName());
            temp.put("passWord", users.getPassWord());
            temp.put("name", users.getName());
            temp.put("gender", users.getGender());
            temp.put("age", users.getAge());
            temp.put("phone", users.getPhone());
            temp.put("address", users.getAddress());
            temp.put("status", users.getStatus());
            temp.put("createTime", users.getCreateTime());
            temp.put("type", users.getType());
            resl.add(temp);
        }

        PageData pageData = new PageData(p.getCurrent(), p.getSize(), p.getTotal(), resl);

        return pageData;
    }
}
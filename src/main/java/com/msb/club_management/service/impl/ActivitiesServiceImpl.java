package com.msb.club_management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.club_management.dao.ActiveLogsDao;
import com.msb.club_management.dao.ActivitiesDao;
import com.msb.club_management.dao.ApplyLogsDao;
import com.msb.club_management.dao.UsersDao;
import com.msb.club_management.msg.PageData;
import com.msb.club_management.service.ActivitiesService;
import com.msb.club_management.vo.ActiveLogs;
import com.msb.club_management.vo.Activities;
import com.msb.club_management.vo.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("activitiesService")
public class ActivitiesServiceImpl implements ActivitiesService {

    @Autowired
    private UsersDao usersDao;

    @Autowired
    private ActivitiesDao activitiesDao;

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public PageData getPageAll(Long pageIndex, Long pageSize, String activeName, String teamName) {
        Page<Map<String, Object>> page=activitiesDao.qryPageAll(new Page<Map<String, Object>>(pageIndex,pageSize),activeName,teamName);
        return parsepage(page);
    }

    @Override
    public PageData getPageByUserId(Long pageIndex, Long pageSize, String id, String teamName, String activeName) {
        return null;
    }

    //转化分页的效果
    private PageData parsepage(Page<Map<String, Object>> page) {
        PageData pageData=new PageData(page.getCurrent(), page.getSize(), page.getTotal(), page.getRecords());
        return pageData;
    }

    @Override
    public void add(Activities activities) {

    }

    @Override
    public void update(Activities activities) {

    }

    @Override
    public void delete(Activities activities) {

    }

    @Override
    public Activities getOne(String id) {
        return null;
    }

}

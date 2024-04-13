package com.msb.club_management.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.club_management.dao.ActivitiesDao;
import com.msb.club_management.handle.CacheHandle;
import com.msb.club_management.msg.PageData;
import com.msb.club_management.service.ActivitiesService;
import com.msb.club_management.vo.Activities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service("activitiesService")
public class ActivitiesServiceImpl implements ActivitiesService {

    @Autowired
    private ActivitiesDao activitiesDao;

    @Autowired
    private CacheHandle cacheHandle;

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
        Activities activities = activitiesDao.selectById(id);

        return activities;
    }

    @Override
    public PageData getPageAllActivities(Long pageIndex, Long pageSize,  String activeName,String teamName) {
        Page<Map<String, Object>> page =
                activitiesDao.qryPageAll(new Page<Map<String, Object>>(pageIndex, pageSize), activeName, teamName);

        return parsePage(page);
    }

    private PageData parsePage(Page<Map<String, Object>> page) {
    // 创建PageData对象，并使用Page对象的属性初始化
        PageData pageData = new PageData(page.getCurrent(), page.getSize(), page.getTotal(), page.getRecords());

        return pageData;
    }

    //转化分页查询的效果
    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public PageData getPageByUserId(Long pageIndex, Long pageSize, String userId, String activeName, String teamName) {

        Page<Map<String, Object>> page =
                activitiesDao.qryPageByMemId(new Page<Map<String, Object>>(pageIndex, pageSize), userId, activeName, teamName);

        return parsePage(page);
    }
}

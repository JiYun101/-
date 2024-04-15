package com.msb.club_management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.club_management.dao.ActiveLogsDao;
import com.msb.club_management.dao.ActivitiesDao;
import com.msb.club_management.dao.TeamsDao;
import com.msb.club_management.msg.PageData;
import com.msb.club_management.service.ActivitiesService;
import com.msb.club_management.utils.DateUtils;
import com.msb.club_management.utils.IDUtils;
import com.msb.club_management.vo.ActiveLogs;
import com.msb.club_management.vo.Activities;
import com.msb.club_management.vo.Teams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service("activitiesService")
public class ActivitiesServiceImpl implements ActivitiesService {

    @Autowired
    private TeamsDao teamsDao;

    @Autowired
    private ActiveLogsDao activeLogsDao;

    @Autowired
    private ActivitiesDao activitiesDao;

    /**
     * 添加一个活动记录到数据库。
     * 该方法首先将活动信息插入到活动表中，然后根据活动所属的团队ID查询团队信息，
     * 并创建一条与该活动相关的活跃日志，记录团队管理员的活动。
     *
     * @param activities 活动对象，包含活动的详细信息。
     */
    @Override
    public void add(Activities activities) {
        activities.setState("1");

        // 将活动信息插入活动表
        activitiesDao.insert(activities);

        // 根据活动团队ID查询团队信息
        Teams teams = teamsDao.selectById(activities.getTeamId());

        // 创建活跃日志对象，并填充相关信息
        ActiveLogs activeLog = new ActiveLogs();
        activeLog.setId(IDUtils.makeIDByCurrent()); // 生成日志ID
        activeLog.setActiveId(activities.getId()); // 关联活动ID
        activeLog.setUserId(teams.getManager()); // 设置团队管理员ID
        activeLog.setCreateTime(DateUtils.getNowDate()); // 设置创建时间
        // 将活跃日志插入日志表
        activeLogsDao.insert(activeLog);
    }

    @Override
    public void update(Activities activities) {
        activitiesDao.updateById(activities);

    }

    @Override
    public void delete(Activities activities) {
        QueryWrapper<ActiveLogs> queryWrapper = new QueryWrapper<ActiveLogs>();

        queryWrapper.eq("active_id", activities.getId());
        activeLogsDao.delete(queryWrapper);
        activitiesDao.deleteById(activities);

    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Activities getOne(String id) {
        Activities activities = activitiesDao.selectById(id);

        return activities;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public PageData getPageAllActivities(Long pageIndex, Long pageSize,  String teamName ,String activeName) {
        Page<Map<String, Object>> page =
                activitiesDao.qryPageAll(new Page<Map<String, Object>>(pageIndex, pageSize), teamName,activeName);

        return parsePage(page);
    }

    private PageData parsePage(Page<Map<String, Object>> page) {
    // 创建PageData对象，并使用Page对象的属性初始化
        PageData pageData = new PageData(page.getCurrent(), page.getSize(), page.getTotal(), page.getRecords());

        return pageData;
    }


    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public PageData getPageByUserId(Long pageIndex, Long pageSize, String userId, String activeName, String teamName) {

        Page<Map<String, Object>> page =
                activitiesDao.qryPageByMemId(new Page<Map<String, Object>>(pageIndex, pageSize), userId, teamName, activeName);

        return parsePage(page);
    }


}

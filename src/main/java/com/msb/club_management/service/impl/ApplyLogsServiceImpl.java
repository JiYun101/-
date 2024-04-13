package com.msb.club_management.service.impl;




import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.msb.club_management.dao.ApplyLogsDao;
import com.msb.club_management.dao.MembersDao;
import com.msb.club_management.dao.TeamsDao;
import com.msb.club_management.msg.PageData;
import com.msb.club_management.service.ApplyLogsService;
import com.msb.club_management.utils.DateUtils;
import com.msb.club_management.utils.IDUtils;
import com.msb.club_management.vo.ApplyLogs;
import com.msb.club_management.vo.Members;
import com.msb.club_management.vo.Teams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service("applyLogsService")
public class ApplyLogsServiceImpl implements ApplyLogsService {

    @Autowired
    private MembersDao membersDao;

    @Autowired
    private ApplyLogsDao applyLogsDao;

    @Autowired
    private TeamsDao teamsDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void add(ApplyLogs applyLogs) {

        applyLogsDao.insert(applyLogs);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void update(ApplyLogs applyLogs) {

        if(applyLogs.getStatus() != null && applyLogs.getStatus() == 1){

            Members member = new Members();
            member.setId(IDUtils.makeIDByCurrent());
            member.setCreateTime(DateUtils.getNowDate());
            member.setUserId(applyLogs.getUserId());
            member.setTeamId(applyLogs.getTeamId());

            membersDao.insert(member);

            Teams teams = teamsDao.selectById(applyLogs.getTeamId());
            teams.setTotal(teams.getTotal() + 1);
            teamsDao.updateById(teams);
        }

        applyLogsDao.updateById(applyLogs);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(ApplyLogs applyLogs) {

        applyLogsDao.deleteById(applyLogs);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Boolean isApply(String userId, String teamId){

        QueryWrapper<ApplyLogs> qw = new QueryWrapper<ApplyLogs>();
        qw.eq("user_id", userId);
        qw.eq("team_id", teamId);
        qw.eq("status", 0);

        return applyLogsDao.selectCount(qw) <= 0;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public ApplyLogs getOne(String id) {

        ApplyLogs applyLogs = applyLogsDao.selectById(id);

        return applyLogs;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public PageData getManPageInfo(Long pageIndex, Long pageSize, String userId, String teamName, String userName) {

        Page<Map<String, Object>> page =
                applyLogsDao.qryManPageInfo(new Page<Map<String, Object>>(pageIndex, pageSize), userId, teamName, userName);

        return parsePage(page);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public PageData getPageInfo(Long pageIndex, Long pageSize, String userId, String teamName, String userName) {

        Page<Map<String, Object>> page =
                applyLogsDao.qryPageInfo(new Page<Map<String, Object>>(pageIndex, pageSize), userId, teamName, userName);

        return parsePage(page);
    }

    /**
     * 转化分页查询的结果
     */
    public PageData parsePage(Page<Map<String, Object>> p) {

        PageData pageData = new PageData(p.getCurrent(), p.getSize(), p.getTotal(), p.getRecords());

        return pageData;
    }
}

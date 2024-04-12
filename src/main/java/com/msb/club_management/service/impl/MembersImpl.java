package com.msb.club_management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.club_management.dao.MembersDao;
import com.msb.club_management.dao.TeamsDao;
import com.msb.club_management.msg.PageData;
import com.msb.club_management.service.MembersService;
import com.msb.club_management.vo.Members;
import com.msb.club_management.vo.Teams;
import com.msb.club_management.vo.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

@Service("membersService")
public class MembersImpl implements MembersService {

    @Autowired
    private MembersDao membersDao;

    @Autowired
    private TeamsDao teamsDao;

    @Override
    public void add(Members members) {
        membersDao.insert(members);
    }

    @Override
    public void update(Members members) {
        membersDao.updateById(members);
    }

    @Override
    public void delete(Members members) {
        membersDao.deleteById(members);
    }

    @Override
    public Members getOne(String id) {
        return null;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public PageData getPageAll(Long pageIndex, Long pageSize, String teamName, String userName) {
        Page<Map<String, Object>> page =
                membersDao.qryPageAll(new Page<Map<String, Object>>(pageIndex,pageSize), teamName, userName);

        return parsePage(page);
    }

    private PageData parsePage(Page<Map<String, Object>> page) {
        PageData pageData = new PageData(page.getCurrent(), page.getSize(), page.getTotal(), page.getRecords());

        return pageData;
    }

    @Override
    public PageData getPageMyManId(Long pageIndex, Long pageSize, String manId,String teamName, String userName) {
        Page<Map<String, Object>> page =
                membersDao.qryPageByManId(new Page<Map<String, Object>>(pageIndex,pageSize),manId, teamName, userName);

        return null;
    }

    @Override
    public boolean isManager(String teamId, String userId) {
        QueryWrapper<Teams> qw = new QueryWrapper<Teams>();
        qw.eq("manager", userId);
        qw.eq("id", teamId);

        return teamsDao.selectCount(qw) > 0;
    }

    @Override
    public Members selectMembes(String id) {
        Members members = membersDao.selectById(id);
        return members;
    }


}

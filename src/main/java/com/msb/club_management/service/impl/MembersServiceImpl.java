package com.msb.club_management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.club_management.dao.MembersDao;
import com.msb.club_management.dao.TeamsDao;
import com.msb.club_management.msg.PageData;
import com.msb.club_management.service.MembersService;
import com.msb.club_management.vo.Members;
import com.msb.club_management.vo.Teams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service("membersService")
public class MembersServiceImpl implements MembersService {

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

        return parsePage(page);
    }

    /**
     * 检查指定用户是否为特定团队的经理。
     *
     * @param teamId 团队的唯一标识符。
     * @param userId 用户的唯一标识符。
     * @return 如果用户是该团队的经理则返回true，否则返回false。
     */
    @Override
    public boolean isManager(String teamId, String userId) {
        // 创建查询包装器并设置查询条件，查询指定teamId和manager为userId的团队记录
        QueryWrapper<Teams> qw = new QueryWrapper<Teams>();
        qw.eq("manager", userId);
        qw.eq("id", teamId);

        // 根据查询条件查询记录数量，如果大于0则表示用户是该团队的经理
        return teamsDao.selectCount(qw) > 0;
    }

    @Override
    public Members selectMembes(String id) {
        Members members = membersDao.selectById(id);
        return members;
    }

    @Override
    public Integer selectMembers(String id,String teamId) {
        Integer membersId=membersDao.selectMembersIdByUserId(id,teamId);
        return membersId;
    }


}

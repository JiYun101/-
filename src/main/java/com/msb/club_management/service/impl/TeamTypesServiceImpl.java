package com.msb.club_management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.club_management.dao.TeamTypesDao;
import com.msb.club_management.dao.TeamsDao;
import com.msb.club_management.msg.PageData;
import com.msb.club_management.service.TeamTypesService;
import com.msb.club_management.utils.StringUtils;
import com.msb.club_management.vo.TeamTypes;
import com.msb.club_management.vo.Teams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("teamTypesService")
public class TeamTypesServiceImpl implements TeamTypesService {
    @Autowired
    private TeamTypesDao teamTypesDao;

    @Autowired
    private TeamsDao teamsDao;
    @Override
    public void add(TeamTypes teamTypes) {
        teamTypesDao.insert(teamTypes);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void update(TeamTypes teamTypes) {
        teamTypesDao.updateById(teamTypes);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(TeamTypes teamTypes) {
        teamTypesDao.deleteById(teamTypes);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public TeamTypes getOne(String id) {
        QueryWrapper<TeamTypes> qw = new QueryWrapper<TeamTypes>();
        qw.orderByDesc("create_time");

        TeamTypes teamTypes = teamTypesDao.selectById(id);

        return teamTypes;
    }

    //通过类型ID判断是否可以删除
    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Boolean isRemove(String typeId) {
        QueryWrapper<Teams> qw = new QueryWrapper<Teams>();
        qw.eq("type_id", typeId);
        qw.eq("state", "1");

        return teamsDao.selectCount(qw) <=0;
    }

    @Override
    public List<TeamTypes> getAllTeamTypes() {
        List<TeamTypes> list = teamTypesDao.selectList(null);

        return list;
    }

    @Override
    public PageData getPageInfo(Long pageIndex, Long pageSize, TeamTypes teamTypes) {
        QueryWrapper<TeamTypes> qw = new QueryWrapper<TeamTypes>();

        if (StringUtils.isNotNullOrEmpty(teamTypes.getName())) {

            qw.like("name", teamTypes.getName());
        }

        qw.orderByDesc("create_time");

        qw.eq("state", "1");
        Page<TeamTypes> page =
                teamTypesDao.selectPage(new Page<TeamTypes>(pageIndex, pageSize), qw);

        return parsePage(page);
    }

    @Override
    public TeamTypes selectTeamTypesByName(String name) {
        TeamTypes teamTypes=teamTypesDao.selectTeamTypesByName(name);
        return teamTypes;
    }

    //转化分页查询的结果
    private PageData parsePage(Page<TeamTypes> page) {
        List<Map<String, Object>> data= new ArrayList<Map<String, Object>>();
        for (TeamTypes teamTypes : page.getRecords()){
            Map<String, Object> temp = new HashMap<String, Object>();
            temp.put("id", teamTypes.getId());
            temp.put("name", teamTypes.getName());
            temp.put("createTime", teamTypes.getCreateTime());
            temp.put("updateTime", teamTypes.getUpdateTime());
            data.add(temp);
        }
        //创建并初始化PageData对象，包含当前页码、每页大小、总记录数和转换后的记录列表
        PageData pageData = new PageData(page.getCurrent(), page.getSize(), page.getTotal(), data);
        return pageData;
    }
}

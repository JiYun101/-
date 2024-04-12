package com.msb.club_management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.club_management.dao.*;
import com.msb.club_management.msg.PageData;

import com.msb.club_management.service.TeamsService;
import com.msb.club_management.utils.DateUtils;
import com.msb.club_management.utils.IDUtils;
import com.msb.club_management.utils.StringUtils;
import com.msb.club_management.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("teamsService")
public class TeamsServiceImpl implements TeamsService {

    @Autowired
    private UsersDao usersDao;

    @Autowired
    private TeamTypesDao teamTypesDao;

    @Autowired
    private TeamsDao teamsDao;

    @Autowired
    private MembersDao membersDao;

    @Autowired
    private NoticesDao noticesDao;

    @Override
    public void add(Teams teams) {
    }


    @Override
    public void update(Teams teams) {
        teamsDao.updateById(teams);
    }

    @Override
    @Transactional
    public void delete(Teams teams) {

    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Teams getOne(String id) {

        Teams teams = teamsDao.selectById(id);

        return teams;
    }


    // 查询所有
    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Teams> getAllTeams(){

        QueryWrapper<Teams> qw = new QueryWrapper<Teams>();

        qw.orderByDesc("create_time");

        List<Teams> list = teamsDao.selectList(qw);

        return list;
    }

/**
 * 将分页查询结果转换为自定义的PageData格式。
 *
 * @param p 分页对象，包含当前页、每页大小和总记录数等信息。
 * @return 转换后的PageData对象，其中包含了转换后的数据列表。
 */
public PageData parsePage(Page<Teams> p) {
    // 初始化数据列表
    List<Map<String, Object>> data= new ArrayList<Map<String, Object>>();

    // 遍历分页记录，转换每条记录的信息
    for (Teams teams : p.getRecords()) {
        // 创建临时Map存储转换后的信息
        Map<String, Object> temp = new HashMap<String, Object>();
        // 基本信息转换
        temp.put("id",teams.getId());
        temp.put("name",teams.getName());
        temp.put("createTime",teams.getCreateTime());
        temp.put("total",teams.getTotal());

        // 查询管理员信息，并添加到转换后的信息中
        Users user=usersDao.selectById(teams.getManager());
        temp.put("manager",teams.getManager());
        temp.put("managerName",user.getName());
        temp.put("managerPhone",user.getPhone());
        temp.put("managerAddress",user.getAddress());

        // 查询团队类型信息，并添加到转换后的信息中
        TeamTypes teamTypes=teamTypesDao.selectById(teams.getTypeId());
        temp.put("typeId",teams.getTypeId());
        temp.put("typeName",teamTypes.getName());
        // 将转换后的信息添加到数据列表
        data.add(temp);
    }
    // 创建并返回转换后的PageData对象
    PageData pageData = new PageData(p.getCurrent(), p.getSize(), p.getTotal(),data);

    return pageData;
}

    /**
     * 根据管理员ID查询团队列表
     */
    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Teams> getListByManId(String manId) {
        QueryWrapper<Teams> qw = new QueryWrapper<Teams>();
        qw.eq("manager", manId);
        qw.orderByDesc("create_time");

        List<Teams> list = teamsDao.selectList(qw);

        return list;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public PageData getPageInfo(Long pageIndex, Long pageSize, Teams teams) {
        QueryWrapper<Teams> qw = new QueryWrapper<Teams>();

        if(StringUtils.isNotNullOrEmpty(teams.getName())){

            qw.like("name", teams.getName());
        }

        if(StringUtils.isNotNullOrEmpty(teams.getTypeId())){

            qw.eq("type_id", teams.getTypeId());
        }

        if(StringUtils.isNotNullOrEmpty(teams.getManager())){

            qw.eq("manager", teams.getManager());
        }

        qw.orderByDesc("create_time");

        qw.eq("state", "1");

        Page<Teams> page =
                teamsDao.selectPage(new Page<Teams>(pageIndex, pageSize), qw);

        return parsePage(page);
    }

    @Override
    public Integer addTeams(Teams teams) {
        // 查询团长id是否有效
        Integer count = usersDao.selectCount(
                new QueryWrapper<Users>().eq("id", teams.getManager()).eq("type", 1)
        );
        if(count == 0) {
            return 0;
        }
        teamsDao.insert(teams);

        Members member = new Members();
        member.setId(IDUtils.makeIDByCurrent());
        member.setUserId(teams.getManager());
        member.setTeamId(teams.getId());
        member.setCreateTime(DateUtils.getNowDate());
        member.setState("1");
        membersDao.insert(member);

        return 1;
    }

    @Override
    public Integer updateTeams(Teams teams) {
        // 查询团长id是否有效
        Integer count = usersDao.selectCount(new QueryWrapper<Users>().eq("id", teams.getManager()).eq("type", 1)
        );
        if(count == 0) {
            return 0;
        }

        update(teams);
        return 1;
    }

}

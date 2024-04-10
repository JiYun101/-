package com.msb.club_management.service;


import com.msb.club_management.msg.PageData;
import com.msb.club_management.vo.Teams;
import com.msb.club_management.vo.Users;

import java.util.List;

/**
 * 业务层处理
 * 社团信息
 */
public interface TeamsService extends BaseService<Teams, String> {


    List<Teams> getAllTeams();


    // 根据管理员id获取社团信息
    public List<Teams> getListByManId(String manId);



    // 分页查询社团信息
    PageData getPageInfo(Long pageIndex, Long pageSize, Teams teams);
    Integer addTeams(Teams teams);

    Integer updateTeams(Teams teams);
}
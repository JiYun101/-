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

    /**
     * 获取所有社团信息
     * @return
     */
    List<Teams> getAllTeams();


    /**
     * 根据管理员id获取社团信息
     */
    public List<Teams> getListByManId(String manId);


    /**
     * 分页查询社团信息
     */
    PageData getPageInfo(Long pageIndex, Long pageSize, Teams teams);

    /**
     *添加社团信息
     */
    Integer addTeams(Teams teams);


    /**
     * 修改社团信息
     */
    Integer updateTeams(Teams teams);


}
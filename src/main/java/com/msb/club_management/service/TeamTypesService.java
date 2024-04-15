package com.msb.club_management.service;

import com.msb.club_management.msg.PageData;
import com.msb.club_management.vo.TeamTypes;

import java.util.List;

public interface TeamTypesService extends BaseService<TeamTypes, String>{
    //检查指定的社团类型是否可以删除 存在关联则不能删除
    public Boolean isRemove(String typeId);


    //获取全部社团信息
    public List<TeamTypes> getAllTeamTypes();

    //分页查询社团类型信息
    public PageData getPageInfo(Long pageIndex, Long pageSize, TeamTypes teamTypes);

    TeamTypes selectTeamTypesByName(String name);


}

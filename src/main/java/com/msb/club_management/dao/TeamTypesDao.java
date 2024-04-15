package com.msb.club_management.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.msb.club_management.vo.TeamTypes;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository("teamTypesDao")
public interface TeamTypesDao extends BaseMapper<TeamTypes> {


    @Select("select * from team_types where name = #{name}")
    TeamTypes selectTeamTypesByName(@Param("name") String name);
}

package com.msb.club_management.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.msb.club_management.vo.TeamTypes;
import org.springframework.stereotype.Repository;

@Repository("teamTypesDao")
public interface TeamTypesDao extends BaseMapper<TeamTypes> {

}

package com.msb.club_management.dao;

import com.msb.club_management.vo.Teams;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 数据层处理接口
 * 社团信息
 */
/**
 * TeamsDao接口，用于操作团队数据。
 * 该接口继承自BaseMapper<Teams>，提供了基本的数据增删改查方法。
 *
 * @Repository("teamsDao") 注解用于指定该接口在Spring容器中的别名为"teamsDao"。
 */
@Repository("teamsDao")
public interface TeamsDao extends BaseMapper<Teams> {

}

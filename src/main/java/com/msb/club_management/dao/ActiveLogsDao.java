package com.msb.club_management.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.msb.club_management.vo.ActiveLogs;
import org.springframework.stereotype.Repository;

@Repository("activeLogsDao")
public interface ActiveLogsDao extends BaseMapper<ActiveLogs>{
}

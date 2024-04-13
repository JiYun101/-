package com.msb.club_management.service;


import com.msb.club_management.vo.ActiveLogs;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


public interface ActiveLogsService extends BaseService<ActiveLogs,String>{
    public Boolean isActive(String activeId, String userId);

    public List<Map<String, Object>> getListByActiveId(String activeId);
}

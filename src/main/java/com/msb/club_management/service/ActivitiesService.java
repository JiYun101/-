package com.msb.club_management.service;

import com.msb.club_management.msg.PageData;
import com.msb.club_management.vo.Activities;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface ActivitiesService extends BaseService<Activities,String>{

    PageData getPageAllActivities(Long pageIndex, Long pageSize, String activeName, String teamName );


    public PageData getPageByUserId(Long pageIndex, Long pageSize, String userId, String activeName, String teamName);
}

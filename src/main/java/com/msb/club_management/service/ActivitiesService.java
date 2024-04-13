package com.msb.club_management.service;

import com.msb.club_management.msg.PageData;
import com.msb.club_management.vo.Activities;

public interface ActivitiesService extends BaseService<Activities,String>{

    PageData getPageAllActivities(Long pageIndex, Long pageSize, String teamName, String activeName);


    PageData getPageByUserId(Long pageIndex, Long pageSize, String id, String teamName, String activeName);
}

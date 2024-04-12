package com.msb.club_management.service;

import com.msb.club_management.msg.PageData;
import com.msb.club_management.vo.Activities;


public interface ActivitiesService extends BaseService<Activities,String>{
    //分页查询活动信息
    public PageData getPageAll(Long pageIndex,Long pageSize,String activeName,String teamName);

    //根据用户id分页查询活动信息
    public PageData getPageByUserId(Long pageIndex, Long pageSize, String userId,String activeName, String teamName);
}

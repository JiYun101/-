package com.msb.club_management.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.msb.club_management.msg.PageData;
import com.msb.club_management.vo.Members;
import com.msb.club_management.vo.Users;

import java.util.List;

public interface MembersService extends BaseService<Members, String>{

    PageData getPageAll(Long pageIndex, Long pageSize, String teamName, String userName);

    PageData getPageMyManId(Long pageIndex, Long pageSize,String manId, String teamName, String userName);


    boolean isManager(String teamId, String userId);

    Members selectMembes(String id);

    Integer selectMembers(String id, String teamId);
}

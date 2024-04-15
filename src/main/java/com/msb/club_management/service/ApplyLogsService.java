package com.msb.club_management.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.club_management.msg.PageData;
import com.msb.club_management.msg.R;
import com.msb.club_management.vo.ApplyLogs;

import java.util.Map;

public interface ApplyLogsService extends BaseService<ApplyLogs,String> {

    /**
     * 检查用户是否可以提交申请
     * @param userId 用户ID
     * @param teamId 团队ID
     * @return
     */
    public Boolean isApply(String userId,String teamId);

    /**
     * 团队管理员分页查询申请记录信息
     * @param pageIndex 当前页码
     * @param pageSize 每页数据量
     * @param userId 用户编号
     * @param teamName 团队名称
     * @param userName 用户姓名
     * @return
     */
    public PageData getManPageInfo(Long pageIndex,Long pageSize,String userId,String teamName,String userName);

    /**
     * 分页查询申请记录信息
     * @param pageIndex 当前页码
     * @param pageSize 每页数据量
     * @param userId 用户编号
     * @param teamName 团队名称
     * @param userName 用户姓名
     * @return
     */
    public PageData getPageInfo(Long pageIndex,Long pageSize,String userId,String teamName,String userName);

    public R updateApplyStatus(ApplyLogs applyLogs);
}

package com.msb.club_management.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.club_management.vo.Members;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 数据层处理接口
 * 成员信息
 */
@Repository("membersDao")
public interface MembersDao extends BaseMapper<Members> {

    /**
     * 分页查看社团成员信息
     * @param page 分页参数
     * @param teamName 社团名称
     * @param userName 用户姓名
     * @return
     */
    @Select("<script>" +
            "SELECT " +
            "m.id, m.create_time createTime, m.team_id teamId, m.user_id userId, " +
            "t.name teamName, t.total, u.name userName, u.gender userGender, u.age userAge, u.phone userPhone " +
            "FROM members m, teams t, users u " +
            "<where> " +
            "m.user_id = u.id AND m.team_id = t.id " +
            "<if test='teamName != null and teamName.trim() != &quot;&quot; '>" +
            "AND t.name LIKE CONCAT('%', #{teamName}, '%') " +
            "</if>" +
            "<if test='userName != null and userName.trim() != &quot;&quot; '>" +
            "AND u.name LIKE CONCAT('%', #{userName}, '%') " +
            "</if>" +
            "AND M.state=1 "+
            "AND t.state=1 "+
            "</where>" +
            "ORDER BY m.create_time DESC" +
            "</script>")
    public Page<Map<String, Object>> qryPageAll(Page<Map<String, Object>> page,
                                                 @Param("teamName") String teamName,
                                                 @Param("userName") String userName);

    /**
     * 依据社团管理员ID获取社团成员信息
     * @param page  分页参数
     * @param manId 管理员ID
     * @param teamName 社团名称
     * @param userName 用户姓名
     * @return
     */
    @Select("<script>" +
            "SELECT " +
            "m.id, m.create_time createTime, m.team_id teamId, m.user_id userId, " +
            "t.name teamName, t.total, u.name userName, u.gender userGender, u.age userAge, u.phone userPhone " +
            "FROM members m, teams t, users u " +
            "<where> " +
            "m.user_id = u.id AND m.team_id = t.id AND t.manager = #{manId} " +
            "<if test='teamName != null and teamName.trim() != &quot;&quot; '>" +
            "AND t.name LIKE CONCAT('%', #{teamName}, '%') " +
            "</if>" +
            "<if test='userName != null and userName.trim() != &quot;&quot; '>" +
            "AND u.name LIKE CONCAT('%', #{userName}, '%') " +
            "</if>" +
            "AND m.state =1 "+
            "</where>" +
            "ORDER BY m.create_time DESC" +
            "</script>")
    public Page<Map<String, Object>> qryPageByManId(Page<Map<String, Object>> page,
                                                    @Param("manId") String manId,
                                                    @Param("teamName") String teamName,
                                                    @Param("userName") String userName);

    @Select("select count(1) from members where user_id = #{id} and team_id=#{teamId}")
    Integer selectMembersIdByUserId(@Param("id") String id, @Param("teamId") String teamId);
}
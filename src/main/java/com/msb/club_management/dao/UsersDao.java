package com.msb.club_management.dao;

import com.msb.club_management.vo.Users;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 数据层处理接口
 * 系统用户
 */
/**
 * 用户数据访问接口，继承自BaseMapper，提供对用户表的基本操作方法。
 * 通过@Repository注解将其别名为"usersDao"，便于在其他组件中引用。
 */
@Repository("usersDao")
public interface UsersDao extends BaseMapper<Users> {

}

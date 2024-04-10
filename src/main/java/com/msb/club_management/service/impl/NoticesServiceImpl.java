package com.msb.club_management.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.msb.club_management.dao.TeamsDao;
import com.msb.club_management.vo.Teams;
import com.msb.club_management.msg.PageData;
import com.msb.club_management.vo.Notices;
import com.msb.club_management.dao.NoticesDao;
import com.msb.club_management.service.NoticesService;
import com.msb.club_management.utils.StringUtils;

import java.util.*;

@Service("noticesService")
public class NoticesServiceImpl implements NoticesService {


    @Autowired
    private NoticesDao noticesDao;

	@Autowired
	private TeamsDao teamsDao;

    @Override
    @Transactional
    public void add(Notices notices) {


        noticesDao.insert(notices);
    }

    @Override
    @Transactional
    public void update(Notices notices) {

        noticesDao.updateById(notices);
    }

    @Override
    @Transactional
    public void delete(Notices notices) {

        noticesDao.deleteById(notices);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Notices getOne(String id) {

        Notices notices = noticesDao.selectById(id);

        return notices;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Notices> getSysNotices(){

        List<Notices> list = noticesDao.qrySysNotices();

        return list;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Notices> getManNotices(String manId){

        List<Notices> list = noticesDao.qryManNotices(manId);

        return list;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Notices> getMemNotices(String memId){

        List<Notices> list = noticesDao.qryMemNotices(memId);

        return list;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public PageData getPageAll(Long pageIndex, Long pageSize, String title, String teamName){


        Page<Map<String, Object>>  page =
                noticesDao.qryPageAll(new Page<Map<String, Object>>(pageIndex, pageSize), title, teamName);

        return parsePage(page);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public PageData getPageById(Long pageIndex, Long pageSize, String userId, String title, String teamName){

        Page<Map<String, Object>>  page =
                noticesDao.qryPageById(new Page<Map<String, Object>>(pageIndex, pageSize), userId, title, teamName);

        return parsePage(page);
    }

    /**
     * 添加通知信息
     * @param notices
     */
   /* @Override
    public  void addNotice(Notices notices) {
        // 设置通知信息的创建时间为当前时间的字符串表示
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        notices.setCreateTime(sdf.format(new Date()));

        // 调用 MyBatis-Plus 提供的通用 Mapper 保存通知信息到数据库
        NoticesDao.insert(notices);
    }*/

    /**
     * 查询列表结果转换
     * @param notices
     * @return
     */
    public List<Map<String, Object>> parseList(List<Notices> notices){

        List<Map<String, Object>> resl = new ArrayList<Map<String, Object>>();

        for (Notices notice : notices) {

            Map<String, Object> temp = new HashMap<String, Object>();
            temp.put("id", notice.getId());
            temp.put("title", notice.getTitle());
            temp.put("detail", notice.getDetail());
            temp.put("createTime", notice.getCreateTime());

            if(StringUtils.isNotNullOrEmpty(notice.getTeamId())){

                Teams teams = teamsDao.selectById(notice.getTeamId());
                temp.put("teamId", notice.getTeamId());
                temp.put("teamName", teams.getName());
                temp.put("isTeam", true);
            }else{

                temp.put("isTeam", false);
            }

            resl.add(temp);
        }

        return resl;
    }

    /**
     * 转化分页查询的结果
     */
    public PageData parsePage(Page<Map<String, Object>> p) {

        PageData pageData = new PageData(p.getCurrent(), p.getSize(), p.getTotal(), p.getRecords());

        return pageData;
    }


}
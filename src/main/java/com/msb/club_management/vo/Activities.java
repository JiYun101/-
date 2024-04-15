package com.msb.club_management.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

@TableName(value = "activities")
public class Activities implements Serializable {

    private static final long serialVersionUID = 1L;
    //记录Id
    @TableId(value = "id")
    private String id;

    //活动名称
    @TableField(value = "name")
    private String name;

    //活动概述
    @TableField(value = "comm")
    private String comm;

    //活动详情
    @TableField(value = "detail")
    private String detail;

    //活动备注
    @TableField(value = "ask")
    private String ask;

    //报名人数
    @TableField(value = "total")
    private Integer total;

    //活动时间
    @TableField(value= "active_time")
    private String activeTime;

    //发布社团
    @TableField(value = "team_id")
    private String teamId;

    //状态
    @TableField(value = "state")
    private Integer state;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComm() {
        return comm;
    }

    public void setComm(String comm) {
        this.comm = comm;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getAsk() {
        return ask;
    }

    public void setAsk(String ask) {
        this.ask = ask;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(String activeTime) {
        this.activeTime = activeTime;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    @Override
    public String toString() {
        return "Activities{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", comm='" + comm + '\'' +
                ", detail='" + detail + '\'' +
                ", ask='" + ask + '\'' +
                ", total=" + total +
                ", activeTime='" + activeTime + '\'' +
                ", teamId='" + teamId + '\'' +
                ", state=" + state +
                '}';
    }
}

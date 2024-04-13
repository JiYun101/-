package com.msb.club_management.vo;

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
    @TableId(value = "name")
    private String name;

    //活动概述
    @TableId(value = "comm")
    private String comm;

    //活动详情
    @TableId(value = "detail")
    private String detail;

    //活动备注
    @TableId(value = "ask")
    private String ask;

    //报名人数
    @TableId(value = "total")
    private Integer total;

    //活动时间
    @TableId(value= "active_time")
    private String activeTime;

    //发布社团
    @TableId(value = "team_id")
    private String teamId;

    //状态
    @TableId(value = "state")
    private String state;

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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
                ", state='" + state + '\'' +
                '}';
    }
}

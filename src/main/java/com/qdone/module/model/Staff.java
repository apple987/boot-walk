package com.qdone.module.model;

import java.util.Date;

import com.qdone.framework.core.page.MutiSort;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.ObjectUtils;

import io.swagger.annotations.ApiModel;

import io.swagger.annotations.ApiModelProperty;

/**
  *该代码由付为地的编码机器人自动生成
  *时间：2018-05-06 16:16:56
*/
@ApiModel(value = "", description = "Staff实体类") 
public class Staff  extends MutiSort {

    private static final long serialVersionUID = 1L;

    // Fields

    @ApiModelProperty(value = "pkid", required = true) 
    private String pkid;
    @ApiModelProperty(value = "职员姓名", required = true) 
    private String sname;//职员姓名
    @ApiModelProperty(value = "职员性别", required = false) 
    private String sex;//职员性别
    @ApiModelProperty(value = "职员年龄", required = false) 
    private Integer age;//职员年龄
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建职员时间", required = false) 
    private Date createDate;//创建职员时间
    @ApiModelProperty(value = "职员创建者", required = false) 
    private String createby;//职员创建者
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新职员时间", required = false) 
    private Date updateDate;//更新职员时间
    @ApiModelProperty(value = "职员信息更新人", required = false) 
    private String updateby;//职员信息更新人
    @ApiModelProperty(value = "police", required = false) 
    private byte[] police;//police
    
    // Constructors

    /** default constructor */
    public Staff() {
    }

    // Property accessors

    public void setPkid(String pkid) {
        this.pkid = pkid;
    }

    public String getPkid() {
        return this.pkid;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getSname() {
        return this.sname;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSex() {
        return this.sex;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getAge() {
        return this.age;
    }

    public void setCreateDate(Date createDate) {
        this.createDate =ObjectUtils.isEmpty(createDate)?createDate:(Date)createDate.clone();
    }

    public Date getCreateDate() {
        return ObjectUtils.isEmpty(this.createDate)?this.createDate:(Date)this.createDate.clone();
    }

    public void setCreateby(String createby) {
        this.createby = createby;
    }

    public String getCreateby() {
        return this.createby;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = ObjectUtils.isEmpty(updateDate)?updateDate:(Date)updateDate.clone();
    }

    public Date getUpdateDate() {
        return ObjectUtils.isEmpty(updateDate)?updateDate:(Date)updateDate.clone();
    }

    public void setUpdateby(String updateby) {
        this.updateby = updateby;
    }

    public String getUpdateby() {
        return this.updateby;
    }

    public void setPolice(byte[] police) {
        this.police = ObjectUtils.isEmpty(police)?police:(byte[])police.clone();
    }

    public byte[] getPolice() {
        return ObjectUtils.isEmpty(police)?police:(byte[])police.clone();
    }

}
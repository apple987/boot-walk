package com.qdone.module.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.ObjectUtils;

import com.qdone.framework.core.page.MutiSort;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
  *该代码由付为地的编码机器人自动生成
  *时间：2017-07-09 17:22:01
*/
@ApiModel(value = "学生对象", description = "数据库实体")
public class Student  extends MutiSort {

    private static final long serialVersionUID = 1L;

    // Fields
    @ApiModelProperty(value = "主键信息", required = true)
    private Integer id;
    @ApiModelProperty(value = "学生姓名")
    private String sname;//学生姓名
    @ApiModelProperty(value = "性别")
    private String sex;//性别
    @ApiModelProperty(value = "年龄")
    private Integer age;//年龄
    @ApiModelProperty(value = "生日")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date birthday;//生日
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间")
    private Date startDate;//开始时间
    @ApiModelProperty(value = "结束时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endDate;//结束时间
    // Constructors

    /** default constructor */
    public Student() {
    }

    // Property accessors

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
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

    public void setBirthday(Date birthday) {
        this.birthday = ObjectUtils.isEmpty(birthday)?birthday:(Date)birthday.clone();
    }

    public Date getBirthday() {
        return ObjectUtils.isEmpty(this.birthday)?this.birthday:(Date)this.birthday.clone();
    }

	public Date getStartDate() {
		return ObjectUtils.isEmpty(this.startDate)?this.startDate:(Date)this.startDate.clone();
	}

	public void setStartDate(Date startDate) {
		this.startDate = ObjectUtils.isEmpty(startDate)?startDate:(Date)startDate.clone();
	}

	public Date getEndDate() {
		return ObjectUtils.isEmpty(this.endDate)?this.endDate:(Date)this.endDate.clone();
	}

	public void setEndDate(Date endDate) {
		this.endDate = ObjectUtils.isEmpty(endDate)?endDate:(Date)endDate.clone();;
	}

}
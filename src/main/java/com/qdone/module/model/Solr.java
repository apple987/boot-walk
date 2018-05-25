package com.qdone.module.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.ObjectUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 该代码由付为地的编码机器人自动生成 时间：2017-09-21 13:44:39
 */
@ApiModel(value = "Solr对象", description = "数据库实体")
public class Solr implements Serializable {

	private static final long serialVersionUID = 1L;

	// Fields
	@ApiModelProperty(value = "主键信息", required = true)
	private String id;
	@ApiModelProperty(value = "价格")
	private Integer price;// 价格
	@ApiModelProperty(value = "标题")
	private String title;// 标题
	@ApiModelProperty(value = "称呼")
	private String name;// 称呼
	@ApiModelProperty(value = "创建时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createtime;// 创建时间

	// Constructors

	/** default constructor */
	public Solr() {
	}

	// Property accessors

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getPrice() {
		return this.price;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = ObjectUtils.isEmpty(createtime)?createtime:(Date)createtime.clone();
	}

	public Date getCreatetime() {
		return ObjectUtils.isEmpty(this.createtime)?this.createtime:(Date)this.createtime.clone();
	}

}
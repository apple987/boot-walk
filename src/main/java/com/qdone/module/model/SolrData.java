package com.qdone.module.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.solr.core.mapping.SolrDocument;
import org.springframework.util.ObjectUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "SolrJ实体类", description = "SolrJ实体类")
@SolrDocument(collection = "rainsoft")
public class SolrData  implements Serializable{
     
	private static final long serialVersionUID = -1359428391586519332L;
	//@Field注解标记对应字段跟solr的schema.xml配置项对应起来
	@Field("id") 
	@ApiModelProperty(value = "主键", position=2,required = true)
	private String id;
	@Field("title")
	@ApiModelProperty(value = "标题",position=1, required = true)
	private String title;
	@Field("price") 
	@ApiModelProperty(value = "价格",allowableValues="100,200,300",example="100",position=0, required = true)
	private Long price;
	@Field("name") 
	@ApiModelProperty(value = "名称",position=5, required = true)
	private String name;
	@Field("createTime")
	@ApiModelProperty(value = "创建时间",position=4, required = true)	
    private Date createTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	
	public Long getPrice() {
		return price;
	}
	public void setPrice(Long price) {
		this.price = price;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Date getCreateTime() {
		return  ObjectUtils.isEmpty(this.createTime)?this.createTime:(Date)this.createTime.clone();
	}
	
	public void setCreateTime(Date createTime) {
		this.createTime = ObjectUtils.isEmpty(createTime)?createTime:(Date)createTime.clone();
	}
	
	
	@Override
	public String toString(){
		StringBuilder builder=new StringBuilder();
		builder.append("Foo [id=");
		builder.append(id);
		builder.append(", title=");
		builder.append(title);
		builder.append(", price=");
		builder.append(price);
		builder.append(", name=");
		builder.append(name);
		builder.append(", createTime=");
		builder.append(createTime);
		builder.append("]");
		return  builder.toString();
	 }
	
}

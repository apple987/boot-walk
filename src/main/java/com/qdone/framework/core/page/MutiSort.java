package com.qdone.framework.core.page;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * 通用针对bootstrap-table分页基类
 */
@ApiModel(value = "分页对象", description = "分页实体")
public class MutiSort implements Serializable {

	
	private static final long serialVersionUID = 1L;
	/* queryParamsType采用limit方式,分页单列排序参数 */
	@ApiModelProperty(value = "单列排序名称",hidden=true)
	private String sort;// 单列排序名称
	@ApiModelProperty(value = "单列排序命令",hidden=true)
	private String order;// 单列排序命令
	@ApiModelProperty(value = "偏移量",hidden=true)
	private int offset;// 偏移量
	@ApiModelProperty(value = "显示条数",hidden=true)
	private int limit;// 每页显示多少条
	/* queryParamsType采用''方式,分页单列排序参数 */
	@ApiModelProperty(value = "单排名称",hidden=true)
	private String sortName;// 单列排序名称
	@ApiModelProperty(value = "单排命令",hidden=true)
	private String sortOrder;// 单列排序命令
	@ApiModelProperty(value = "每页条数",hidden=true)
	private int pageSize;// 每页多少条
	@ApiModelProperty(value = "页码数",hidden=true)
	private int pageNumber;// 当前第几页
	/* 多列排序字段 */
	private List<Data> multiSort;
	/*使用springCachable注解时,针对service执行insert,update,delete方法时,返回执行结果*/
	@ApiModelProperty(value = "操作结果",hidden=true)
	private int operateResult;
	
	@ApiModel(value = "多排对象", description = "多列排序实体类")
	static class Data implements Serializable{
		
		private static final long serialVersionUID = 5299521958136561287L;
		@ApiModelProperty(value = "多排名称",hidden=true)
		private String sortName;
		@ApiModelProperty(value = "多排命令",hidden=true)
		private String sortOrder;

		public Data() {
		}

		public Data(String sortName, String sortOrder) {
			this.sortName = sortName;
			this.sortOrder = sortOrder;
		}

		public String getSortName() {
			return sortName;
		}

		public void setSortName(String sortName) {
			this.sortName = sortName;
		}

		public String getSortOrder() {
			return sortOrder;
		}

		public void setSortOrder(String sortOrder) {
			this.sortOrder = sortOrder;
		}

	}

	public MutiSort() {
		
	}
	

	public MutiSort(String sort, String order, int offset, int limit, String sortName, String sortOrder, int pageSize,
			int pageNumber, List<Data> multiSort, int operateResult) {
		super();
		this.sort = sort;
		this.order = order;
		this.offset = offset;
		this.limit = limit;
		this.sortName = sortName;
		this.sortOrder = sortOrder;
		this.pageSize = pageSize;
		this.pageNumber = pageNumber;
		this.multiSort = multiSort;
		this.operateResult = operateResult;
	}


	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public List<Data> getMultiSort() {
		return multiSort;
	}

	public void setMultiSort(List<Data> multiSort) {
		this.multiSort = multiSort;
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}


	public int getOperateResult() {
		return operateResult;
	}

	public void setOperateResult(int operateResult) {
		this.operateResult = operateResult;
	}

	
	
}

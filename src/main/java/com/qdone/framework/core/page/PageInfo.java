package com.qdone.framework.core.page;



import java.io.Serializable;

/**
 * 分页的基本参数对象
 */
public class PageInfo implements Serializable {

    
	private static final long serialVersionUID = -7872779138799315383L;
	/**
     * 每页显示记录数
     */
    private Integer pageSize;
    /**
     * 当前页码
     */
    private Integer currentNumber;
    /**
     * 总记录数
     */
    private Long recordCount;

    /**
     * 总页数
     */
    private Integer pageCount;

    /**
     * 排序字符，单个的，不支持多个字段的排序 。etc. plan_name desc
     */
    private String fieldOrder;
    /**
     * 在页面上显示的字段,Flexigrid专用
     */
    private String fields;
    
    /**
     * 统计列
     */
    private String stat;
    /**
     * 排序命令
     */
    private String sortOrder;
    /**
     * 多列排序字符串
     */
    private String mutiSortString;
    

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCurrentNumber() {
        return currentNumber;
    }

    public void setCurrentNumber(Integer currentNumber) {
        this.currentNumber = currentNumber;
    }


    public String getFieldOrder() {
        return fieldOrder;
    }

    public void setFieldOrder(String fieldOrder) {
        this.fieldOrder = fieldOrder;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public Long getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(Long recordCount) {
        this.recordCount = recordCount;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

	public String getStat() {
		return stat;
	}

	public void setStat(String stat) {
		this.stat = stat;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getMutiSortString() {
		return mutiSortString;
	}

	public void setMutiSortString(String mutiSortString) {
		this.mutiSortString = mutiSortString;
	}
    
	
	
}

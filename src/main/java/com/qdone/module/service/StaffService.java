package com.qdone.module.service;

import java.util.List;

import com.qdone.module.model.Staff;
import com.qdone.framework.core.page.PageList;
/**
 * TODO 本代码由代码生成工具生成
 *
 * @author 付为地
 * @date 2018-05-06 04:16:56
 */

public interface StaffService {

	/**
	 * 分页查询
	 * @param entity
	 * @return
	 */
	public PageList<Staff> selectPage(Staff entity);
	
	/**
	 * 新增
	 * @param object
	 * @return
	 */
	public int insert(Staff object) ;
	
	/**
	 * 修改
	 * @param object
	 * @return
	 */
	public int update(Staff object)  ;
	
	/**
	 * 查看
	 * @param pk
	 * @return
	 */
	public Staff view(String pk) ;
	
	/**
	 * 查询单个
	 * @param object
	 * @return
	 */
	public Staff query(Staff object) ;
	
	/**
	 * 查询集合
	 * @param object
	 * @return
	 */
	public List<Staff> selectList(Staff object) ;
	
	/**
	 * 删除
	 * @param pk
	 * @return
	 */
	public int delete(String pk);
	
	/**
	 * 批量修改
	 * @param object
	 */
	public int batchUpdate(List<Staff> arr);
	
	/**
	 * 批量插入
	 * @param object
	 */
	public int batchInsert(List<Staff> arr);
	
	/**
	 * 批量删除
	 */
	public int batchDelete(List<Staff> pkList);
}

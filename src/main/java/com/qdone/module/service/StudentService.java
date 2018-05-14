package com.qdone.module.service;

import java.util.List;

import com.qdone.framework.core.page.PageList;
import com.qdone.module.model.Student;

/**
 * TODO 本代码由代码生成工具生成
 *
 * @author 付为地
 * @date 2017-07-09 05:33:39
 */

public interface StudentService {

	/**
	 * 分页查询
	 * @param entity
	 * @return
	 */
	public PageList<Student> selectPage(Student entity);
	
	/**
	 * 新增
	 * @param object
	 * @return
	 */
	public Student insert(Student object) ;
	
	/**
	 * 修改
	 * @param object
	 * @return
	 */
	public Student update(Student object)  ;
	
	/**
	 * 查看
	 * @param pk
	 * @return
	 */
	public Student view(Integer pk) ;
	
	/**
	 * 查询单个
	 * @param object
	 * @return
	 */
	public Student query(Student object) ;
	
	/**
	 * 查询集合
	 * @param object
	 * @return
	 */
	public List<Student> selectList(Student object) ;
	
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
	public int batchUpdate(List<Student> arr);
	
	/**
	 * 批量插入
	 * @param object
	 */
	public int batchInsert(List<Student> arr);
	
	/**
	 * 批量删除
	 */
	public int batchDelete(List<Student> pkList);
}

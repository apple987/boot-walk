package com.qdone.framework.core.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.qdone.framework.core.page.CoreUtil;
import com.qdone.framework.core.page.PageInfo;
import com.qdone.framework.core.page.PageList;

/**
 * @author 付为地 底层公共Dao
 */
@Repository("baseDao")
public class BaseDao extends SqlSessionDaoSupport{

	final static Logger log = LoggerFactory.getLogger(BaseDao.class);
	
	/**
	 * 注入SqlSessionTemplate
	 */
	@Autowired
	public void setSqlSessionTemplate(@Qualifier("sqlSessionTemplate") SqlSessionTemplate sqlSessionTemplate) {
		super.setSqlSessionTemplate(sqlSessionTemplate);
	}
	/**
	 * 提供扩展的SqlSession，功能
	 */
	public SqlSession getSqlSession() {
		return super.getSqlSession();
	}
	
	/*@Autowired
	private SqlSession sqlSession;

	
	public SqlSession getSqlSession() {
		return sqlSession;
	}*/

	/**
	 * bootstrap-table支持多列排序分页 eg:页面采用 contentType="application/json" 后台采用
	 * public Object getPage(@RequestBody DataInterrupt param) { return
	 * responseSelectPage(dataInterruptService.selectPage(param)); }
	 * 
	 * @param sqlID:执行sql的id
	 * @param params:sql对应的实体类,必须继承MutiSort类,本基类提供常见分页方式
	 * @return
	 */
	public <T> PageList<T> selectPage(String sqlID, Object params) {
		PageList<T> pageList = new PageList<T>();
		PageInfo pageInfo = CoreUtil.createBootStrapPage(params, true);
		String orders = CoreUtil.createSort(pageInfo, true);
		PageHelper.orderBy(orders);// 插件排序
		List<T> list = this.getSqlSession().selectList(sqlID, params,
				new RowBounds(pageInfo.getCurrentNumber(), pageInfo.getPageSize()));
		// 设置总记录数
		pageList.setList(list);
		Page<T> pg = (Page<T>) list;
		pageInfo.setRecordCount(pg.getTotal());
		pageList.setPageInfo(pageInfo);
		return pageList;
	}

	/**
	 * bootstrap-table支持多列排序分页 eg:页面采用 contentType="application/json" 后台采用
	 * public Object getPage(@RequestBody DataInterrupt param) { return
	 * responseSelectPage(dataInterruptService.selectPage(param)); }
	 * 
	 * @param sqlID:执行sql的id
	 * @param params:sql对应的实体类,必须继承MutiSort类,本基类提供常见分页方式
	 * @param isAuto:是否自动将数据库查询结果转成驼峰格式,如果设置false则不转换,默认转换
	 * @return
	 */
	public <T> PageList<T> selectPage(String sqlID, Object params, boolean isAuto) {
		PageList<T> pageList = new PageList<T>();
		PageInfo pageInfo = CoreUtil.createBootStrapPage(params, isAuto);
		String orders = CoreUtil.createSort(pageInfo, isAuto);
		PageHelper.orderBy(orders);// 插件排序
		List<T> list = this.getSqlSession().selectList(sqlID, params,
				new RowBounds(pageInfo.getCurrentNumber(), pageInfo.getPageSize()));
		// 设置总记录数
		pageList.setList(list);
		Page<T> pg = (Page<T>) list;
		pageInfo.setRecordCount(pg.getTotal());
		pageList.setPageInfo(pageInfo);
		return pageList;
	}

	/**
	 * selectPage 默认自动将:areaName转换成area_name表字段排序
	 * 
	 * @param <T>
	 * @warn(注意事项 – 可选)
	 * @param sqlID
	 * @param params
	 *            默认自动转换
	 * @return
	 */
	public <T> PageList<T> selectPage(String sqlID, Map<String, Object> params) {
		PageList<T> pageList = new PageList<T>();
		PageInfo pageInfo = CoreUtil.buildBootStrapPage(params, true);
		String orders = CoreUtil.createSort(pageInfo, true);
		PageHelper.orderBy(orders);// 插件排序
		List<T> list = this.getSqlSession().selectList(sqlID, params,
				new RowBounds(pageInfo.getCurrentNumber(), pageInfo.getPageSize()));
		// 设置总记录数
		pageList.setList(list);
		Page<T> pg = (Page<T>) list;
		pageInfo.setRecordCount(pg.getTotal());
		pageList.setPageInfo(pageInfo);
		return pageList;
	}

	/**
	 * 分页方法 是否自动将:areaName转换成area_name表字段排序
	 * 
	 * @param <T>
	 * @param sqlID
	 * @param params
	 * @param isAuto:true自动|false不转换(默认转换)
	 */

	public <T> PageList<T> selectPage(String sqlID, Map<String, Object> params, boolean isAuto) {
		PageList<T> pageList = new PageList<T>();
		PageInfo pageInfo = CoreUtil.buildBootStrapPage(params, isAuto);
		String orders = CoreUtil.createSort(pageInfo, isAuto);
		PageHelper.orderBy(orders);// 插件排序
		List<T> list = this.getSqlSession().selectList(sqlID, params,
				new RowBounds(pageInfo.getCurrentNumber(), pageInfo.getPageSize()));
		// 设置总记录数
		pageList.setList(list);
		Page<T> pg = (Page<T>) list;
		pageInfo.setRecordCount(pg.getTotal());
		pageList.setPageInfo(pageInfo);
		return pageList;
	}

	/**
	 * 新增
	 */
	public int insert(String sqlID, Object object) {
		return this.getSqlSession().insert(sqlID, object);

	}

	/**
	 * 更新
	 */
	public int update(String sqlID, Object object) {
		return this.getSqlSession().update(sqlID, object);
	}

	/**
	 * 浏览单个
	 */
	public Object view(String sqlID, Object pk) {
		return this.getSqlSession().selectOne(sqlID, pk);
	}

	/**
	 * 删除
	 */
	public int delete(String sqlID, Object pk) {
		return this.getSqlSession().delete(sqlID, pk);
	}

	/**
	 * 查询列表
	 * 
	 * @param <T>
	 */
	public <T> List<T> selectList(String sqlID, Object obj) {
		return this.getSqlSession().selectList(sqlID, obj);
	}

	/**
	 * 查询单条
	 */
	public Object selectOne(String SqlID, Object obj) {
		return this.getSqlSession().selectOne(SqlID, obj);
	}

	/**
	 * 查询map
	 */
	public Map<String, Object> selectMap(String SqlID, String args) {
		return this.getSqlSession().selectMap(SqlID, args);
	}

	/**
	 * 批量更新
	 * 
	 * @param <T>
	 */
	public <T> int batchUpdate(String sqlID, List<T> object) {
		return this.getSqlSession().update(sqlID, object);
	}

	/**
	 * 批量插入
	 * 
	 * @param <T>
	 */
	public <T> int batchInsert(String sqlID, List<T> object) {
		return this.getSqlSession().insert(sqlID, object);
	}

	/**
	 * 批量删除
	 * 
	 * @param <T>
	 */
	public <T> int batchDelete(String sqlID, List<T> pkList) {
		return this.getSqlSession().delete(sqlID, pkList);
	}

}

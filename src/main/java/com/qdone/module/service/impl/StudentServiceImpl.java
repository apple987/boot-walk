package com.qdone.module.service.impl;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSON;
import com.qdone.framework.core.dao.BaseDao;
import com.qdone.framework.core.page.PageList;
import com.qdone.module.model.Student;
import com.qdone.module.service.StudentService;

/**
 * TODO 本代码由代码生成工具生成
 * @author 付为地
 * @date 2017-07-09 05:33:39
 */

@Service("studentService")
@Transactional(readOnly=true)
public class StudentServiceImpl implements StudentService{

    static  Logger log=LoggerFactory.getLogger(StudentServiceImpl.class);
    
    @Resource(name = "baseDao")
	private BaseDao baseDao;
       
	
	
	/**
	 * 分页查询
	 */
	@Override
	public PageList<Student> selectPage(Student object) {
	   log.info("执行StudentServiceImpl.selectPage参数：entity==>" +JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss"));
       return baseDao.selectPage("com.qdone.module.mapper.Student.select", object);
	}

	/**
	 * 新增
	 */
	@Override
	@Transactional
	@CachePut(value="view",key="#object.getId()")
	public Student insert(Student object) {
		log.info("StudentServiceImpl.insert参数： entity=>" + JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss"));
		object.setOperateResult(baseDao.insert("com.qdone.module.mapper.Student.insert", object));
		return object;
	}

	/**
	 * 修改
	 */
	@Override
	@Transactional
	@CacheEvict(value="view",key="#object.getId()")
	public Student update(Student object){
		log.info("执行StudentServiceImpl.update参数： entity==>" + JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss"));
		object.setOperateResult(baseDao.update("com.qdone.module.mapper.Student.update", object));
        return object;
	}

	/**
	 * 浏览单个
	 */
	@Override
	@Cacheable(value="view",key="#pk")
	public Student view(Integer pk) {
		log.info("执行StudentServiceImpl.view参数： pk=>" + pk);
        return (Student)baseDao.view("com.qdone.module.mapper.Student.view", pk);
	} 
	
    /**
	 * 查询单个
	 * @param object
	 * @return
	 */
	public Student query(Student object){
		log.info("执行StudentServiceImpl.query参数： entity=>" + JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss"));
        return (Student)baseDao.selectOne("com.qdone.module.mapper.Student.query", object);
	}
	 
    /**
	 * 查询集合
	 * @param object
	 * @return
	 */
	@Override
	public List<Student> selectList(Student object) {
		log.info("执行StudentServiceImpl.selectList参数： entity=>" + JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss"));
        return baseDao.selectList("com.qdone.module.mapper.Student.select", object);
	}
	
    /**
	 * 删除
	 * @param pk
	 * @return
	 */
	@Override
	@CacheEvict(value="view",key="#pk")
	@Transactional
	public int delete(String pk) {
		log.info("执行StudentServiceImpl.delete参数： pk=>" + pk);
		return baseDao.delete("com.qdone.module.mapper.Student.delete", Integer.parseInt(pk));
	}
    /**
	 * 批量修改
	 * @param object
	 */
	@Override
	@Transactional
	public int batchUpdate(List<Student> object) {
		log.info("执行StudentServiceImpl.batchUpdate参数： List=>" + JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss"));
        return baseDao.batchUpdate("com.qdone.module.mapper.Student.batchUpdate", object);
	}
	
    /**
	 * 批量插入
	 * @param object
	 */
	@Override
	@Transactional
	public int batchInsert(List<Student> object) {
	     log.info("执行StudentServiceImpl.batchInsert参数： List=>" + JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss"));
         return baseDao.batchInsert("com.qdone.module.mapper.Student.batchInsert", object);
	}
	
    /**
	 * 批量删除
	 */
	@Override
	@Transactional
	public int batchDelete(List<Student> pkList) {
		log.info("执行StudentServiceImpl.batchDelete参数： pkList=>" + JSON.toJSONStringWithDateFormat(pkList, "yyyy-MM-dd HH:mm:ss"));
        return baseDao.batchDelete("com.qdone.module.mapper.Student.batchDelete", pkList);
	}   
	
}

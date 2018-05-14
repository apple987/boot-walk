package com.qdone.module.service.impl;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import java.util.List;
import com.qdone.framework.core.dao.BaseDao;
import com.qdone.framework.core.page.PageList;
import com.qdone.module.model.Staff;
import com.qdone.module.service.StaffService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Isolation;
/**
 * TODO 本代码由代码生成工具生成
 *
 * @author 付为地
 * @date 2018-05-06 04:16:56
 */

@Service("staffService")
@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT)
public class StaffServiceImpl implements StaffService{

    static  Logger log=LoggerFactory.getLogger(StaffServiceImpl.class);
    
    @Resource(name = "baseDao")
	private BaseDao baseDao;
       
	
	
	/**
	 * 分页查询
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public PageList<Staff> selectPage(Staff object) {
	   log.info("执行StaffServiceImpl.selectPage参数：entity==>" +JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss"));
       return baseDao.selectPage("com.qdone.module.mapper.Staff.select", object);
	}

	/**
	 * 新增
	 */
	@Override
	@Transactional(readOnly = false,rollbackFor = Exception.class)
	public int insert(Staff object) {
		log.info("StaffServiceImpl.insert参数： entity=>" + JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss"));
        return baseDao.insert("com.qdone.module.mapper.Staff.insert", object);
	}

	/**
	 * 修改
	 */
	@Override
	@Transactional(readOnly = false,rollbackFor = Exception.class)
	public int update(Staff object){
		log.info("执行StaffServiceImpl.update参数： entity==>" + JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss"));
        return baseDao.update("com.qdone.module.mapper.Staff.update", object);
	}

	/**
	 * 浏览单个
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public Staff view(String pk) {
		log.info("执行StaffServiceImpl.view参数： pk=>" + pk);
        return (Staff)baseDao.view("com.qdone.module.mapper.Staff.view", pk);
	} 
	
    /**
	 * 查询单个
	 * @param object
	 * @return
	 */
	 @Transactional(propagation = Propagation.NOT_SUPPORTED)
	public Staff query(Staff object){
		log.info("执行StaffServiceImpl.query参数： entity=>" + JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss"));
        return (Staff)baseDao.selectOne("com.qdone.module.mapper.Staff.query", object);
	}
	 
    /**
	 * 查询集合
	 * @param object
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<Staff> selectList(Staff object) {
		log.info("执行StaffServiceImpl.selectList参数： entity=>" + JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss"));
        return baseDao.selectList("com.qdone.module.mapper.Staff.select", object);
	}
	
    /**
	 * 删除
	 * @param pk
	 * @return
	 */
	@Override
	@Transactional(readOnly = false,rollbackFor = Exception.class)
	public int delete(String pk) {
		log.info("执行StaffServiceImpl.delete参数： pk=>" + pk);
		return baseDao.delete("com.qdone.module.mapper.Staff.delete", pk);
	}
    /**
	 * 批量修改
	 * @param object
	 */
	@Override
	@Transactional(readOnly = false,rollbackFor = Exception.class)
	public int batchUpdate(List<Staff> object) {
		log.info("执行StaffServiceImpl.batchUpdate参数： List=>" + JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss"));
        return baseDao.batchUpdate("com.qdone.module.mapper.Staff.batchUpdate", object);
	}
	
    /**
	 * 批量插入
	 * @param object
	 */
	@Override
	@Transactional(readOnly = false,rollbackFor = Exception.class)
	public int batchInsert(List<Staff> object) {
	     log.info("执行StaffServiceImpl.batchInsert参数： List=>" + JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss"));
         return baseDao.batchInsert("com.qdone.module.mapper.Staff.batchInsert", object);
	}
	
    /**
	 * 批量删除
	 */
	@Override
	@Transactional(readOnly = false,rollbackFor = Exception.class)
	public int batchDelete(List<Staff> pkList) {
		log.info("执行StaffServiceImpl.batchDelete参数： pkList=>" + JSON.toJSONStringWithDateFormat(pkList, "yyyy-MM-dd HH:mm:ss"));
        return baseDao.batchDelete("com.qdone.module.mapper.Staff.batchDelete", pkList);
	}   
	
}

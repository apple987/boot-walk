package com.qdone.module.service.impl;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import java.util.List;
import com.qdone.framework.core.dao.BaseDao;
import com.qdone.framework.core.page.PageList;
import com.qdone.module.model.Solr;
import com.qdone.module.service.SolrService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Isolation;
/**
 * TODO 本代码由代码生成工具生成
 *
 * @author 付为地
 * @date 2018-05-15 03:16:12
 */

@Service("solrService")
@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT)
public class SolrServiceImpl implements SolrService{

    static  Logger log=LoggerFactory.getLogger(SolrServiceImpl.class);
    
    @Resource(name = "baseDao")
	private BaseDao baseDao;
       
	
	
	/**
	 * 分页查询
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public PageList<Solr> selectPage(Solr object) {
	   log.info("执行SolrServiceImpl.selectPage参数：entity==>" +JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss"));
       return baseDao.selectPage("com.qdone.module.mapper.Solr.select", object);
	}

	/**
	 * 新增
	 */
	@Override
	@Transactional(readOnly = false,rollbackFor = Exception.class)
	public int insert(Solr object) {
		log.info("SolrServiceImpl.insert参数： entity=>" + JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss"));
        return baseDao.insert("com.qdone.module.mapper.Solr.insert", object);
	}

	/**
	 * 修改
	 */
	@Override
	@Transactional(readOnly = false,rollbackFor = Exception.class)
	public int update(Solr object){
		log.info("执行SolrServiceImpl.update参数： entity==>" + JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss"));
        return baseDao.update("com.qdone.module.mapper.Solr.update", object);
	}

	/**
	 * 浏览单个
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public Solr view(String pk) {
		log.info("执行SolrServiceImpl.view参数： pk=>" + pk);
        return (Solr)baseDao.view("com.qdone.module.mapper.Solr.view", pk);
	} 
	
    /**
	 * 查询单个
	 * @param object
	 * @return
	 */
	 @Transactional(propagation = Propagation.NOT_SUPPORTED)
	public Solr query(Solr object){
		log.info("执行SolrServiceImpl.query参数： entity=>" + JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss"));
        return (Solr)baseDao.selectOne("com.qdone.module.mapper.Solr.query", object);
	}
	 
    /**
	 * 查询集合
	 * @param object
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<Solr> selectList(Solr object) {
		log.info("执行SolrServiceImpl.selectList参数： entity=>" + JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss"));
        return baseDao.selectList("com.qdone.module.mapper.Solr.select", object);
	}
	
    /**
	 * 删除
	 * @param pk
	 * @return
	 */
	@Override
	@Transactional(readOnly = false,rollbackFor = Exception.class)
	public int delete(String pk) {
		log.info("执行SolrServiceImpl.delete参数： pk=>" + pk);
		return baseDao.delete("com.qdone.module.mapper.Solr.delete", pk);
	}
    /**
	 * 批量修改
	 * @param object
	 */
	@Override
	@Transactional(readOnly = false,rollbackFor = Exception.class)
	public int batchUpdate(List<Solr> object) {
		log.info("执行SolrServiceImpl.batchUpdate参数： List=>" + JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss"));
        return baseDao.batchUpdate("com.qdone.module.mapper.Solr.batchUpdate", object);
	}
	
    /**
	 * 批量插入
	 * @param object
	 */
	@Override
	@Transactional(readOnly = false,rollbackFor = Exception.class)
	public int batchInsert(List<Solr> object) {
	     log.info("执行SolrServiceImpl.batchInsert参数： List=>" + JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss"));
         return baseDao.batchInsert("com.qdone.module.mapper.Solr.batchInsert", object);
	}
	
    /**
	 * 批量删除
	 */
	@Override
	@Transactional(readOnly = false,rollbackFor = Exception.class)
	public int batchDelete(List<Solr> pkList) {
		log.info("执行SolrServiceImpl.batchDelete参数： pkList=>" + JSON.toJSONStringWithDateFormat(pkList, "yyyy-MM-dd HH:mm:ss"));
        return baseDao.batchDelete("com.qdone.module.mapper.Solr.batchDelete", pkList);
	}   
	
}

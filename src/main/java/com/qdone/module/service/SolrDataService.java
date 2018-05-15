package com.qdone.module.service;

import java.util.List;

import org.springframework.data.solr.repository.SolrCrudRepository;

import com.qdone.module.model.SolrData;
/**
 * solr简单CRUD操作
 */
public interface SolrDataService extends SolrCrudRepository<SolrData, String> {
	List<SolrData> findByNameStartingWith(String name);
}

package com.qdone.module.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.List;
import java.util.Map;
import com.qdone.module.model.Solr;
import com.qdone.module.service.SolrService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import com.qdone.framework.annotation.Function;
import com.qdone.framework.core.BaseController;
import com.qdone.common.util.SerialNo;

/**
 *solr管理
 * @付为地
 * @date 2018-05-15 03:16:12
 */
@Controller
@RequestMapping("/solr")
@Api(tags = "管理",description = "信息管理")
public class SolrController extends BaseController{
  

    @Autowired
	private SolrService solrService;

    /**
	 * 页面初始化
	 */
	@ApiOperation(value = "列表",notes = "进入列表页", httpMethod = "GET")
	@RequestMapping(value = "init",method = RequestMethod.GET)
	public String init(){
		return "solr/selectSolr";
	}
	
    /**
	 * 分页查询数据
	 */
	@RequestMapping(value="/selectPage",method=RequestMethod.POST)
	@ResponseBody
	@Function("分页")
	@ApiOperation(value = "分页列表", notes = "分页列表", httpMethod = "POST",response = Map.class)
	public Map<String, Object> selectPage(@RequestBody Solr entity){
		return responseSelectPage(solrService.selectPage(entity));
	}
	
	/**
	 * 跳转添加
	*/
    @RequestMapping(value="/preInsert",method=RequestMethod.GET)
    @ApiOperation(value = "跳转添加", notes = "进入添加页面", httpMethod = "GET")
	public String preInsert(HttpServletRequest req){
		return "solr/insertSolr";
	} 
	
    /**
     * 添加数据
     */
	@RequestMapping(value="/insert",method=RequestMethod.PUT)
	@ResponseBody
	@Function("添加")
	@ApiOperation(value = "添加", notes = "创建", httpMethod = "PUT",response = Boolean.class)
	public Boolean insert(@ApiParam(name = "对象", value = "传入json格式", required = true)  @RequestBody Solr entity) {
		entity.setId(SerialNo.getUNID());
		return solrService.insert(entity)>0?Boolean.TRUE:Boolean.FALSE;
	}
	
	/**
	 * 跳转更新
	*/
    @RequestMapping(value="/preUpdate",method=RequestMethod.GET)
    @ApiOperation(value = "跳转更新", notes = "进入更新页面", httpMethod = "GET")
	public String preUpdate(HttpServletRequest request){
	    request.setAttribute("solr", solrService.view(request.getParameter("id")));
		return "solr/updateSolr";
	} 
	
    /**
     * 更新数据
     */
	@RequestMapping(value="/update",method=RequestMethod.POST)
	@ResponseBody
	@Function("更新")
	@ApiOperation(value = "更新", notes = "更新信息", httpMethod = "POST",response = Boolean.class)
	public Boolean update(@ApiParam(name = "对象", value = "传入json格式", required = true) Solr entity) {
		return solrService.update(entity)>0?Boolean.TRUE:Boolean.FALSE;
	}
	
    /**
     * 删除数据
     */
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	@ResponseBody
	@Function("删除")
	@ApiOperation(value = "删除",notes = "删除", httpMethod = "POST",response = Boolean.class)
	public Boolean delete(@RequestBody List<Solr> ids) {
		return solrService.batchDelete(ids)>0?Boolean.TRUE:Boolean.FALSE;
	}
    
}

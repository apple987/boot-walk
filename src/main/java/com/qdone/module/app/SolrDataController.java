package com.qdone.module.app;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.qdone.common.util.Result;
import com.qdone.framework.core.BaseController;
import com.qdone.module.model.SolrData;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@Api(tags = "Solrj服务接口", description = "演示Solrj服务接口,(SolrClient方式)")
@RequestMapping("/solrj")
public class SolrDataController extends BaseController {

	@Autowired
    private SolrClient client;
	

	@RequestMapping(value = "/testAdd", method = RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(value = "Solrj添加数据", httpMethod = "PUT", notes = "测试Solrj添加数据", response = Void.class)
	public void testAdd(@ApiParam(required = true, value = "传入JSON对象参数", name = "msg") @RequestBody SolrData msg)throws Exception {
		/*HttpSolrClient*/
		client.addBean(msg);
		client.commit();
	}

	@ApiOperation(value = "SolrJ多条件查询", notes = "SolrJ多条件列表信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@RequestMapping(value = "testMutiSearch", method = RequestMethod.POST)
	@ApiResponse(code = 200, message = "请求成功", response = SolrData.class, responseContainer = "Map")
	/*public Result<List<SolrData>> testMutiSearch(@ApiParam(name = "condition", value = "复杂条件表达式", required = true, example = "title:永汉 OR price:400 AND id:1497854074330") @RequestParam String condition,
			@ApiParam(name = "start", value = "开始记录数", required = true, example = "1",defaultValue="1")  int start,
			@ApiParam(name = "end", value = "截止记录数", required = true, example = "10",defaultValue="10")  int end) throws Exception {
	 */ 
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌",dataType="String",paramType ="header", required = true, example = "title:永汉 OR price:400 AND id:1497854074330"),
		@ApiImplicitParam(name = "condition", value = "复杂条件表达式",dataType="String",paramType ="query", required = true, example = "title:永汉 OR price:400 AND id:1497854074330"),
		@ApiImplicitParam(name = "start", value = "开始记录数",dataType="int",paramType ="query", required = true, example = "1",defaultValue="1"),
		@ApiImplicitParam(name = "end", value = "截止记录数",dataType="int",paramType ="query", required = true, example = "10",defaultValue="10")
	})
	public Result<List<SolrData>> testMutiSearch(String token,String condition,int start,int end)throws Exception{
			Result<List<SolrData>> res = new Result<List<SolrData>>();
			List<SolrData> foos = search(condition, start, end);
			for (SolrData SolrData : foos) {
				System.out.println(JSON.toJSONString(SolrData));
			}
			res.setData(foos);
			return res;
	}

	@RequestMapping(value = "/testDeleteByQuery", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Solrj根据条件删除", httpMethod = "GET", notes = "测试Solrj根据条件删除", response = Void.class)
	public void testDeleteByQuery(
			@ApiParam(name = "arg", value = "条件表达式", required = true, example = "title:玩耍") @RequestParam String arg)
			throws Exception {
		client.deleteByQuery(arg);
		client.commit();
	}

	@RequestMapping(value = "/testUpdate", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "Solrj更新数据", httpMethod = "POST", notes = "测试Solrj更新数据", response = Void.class)
	public void testUpdate(@ApiParam(required = true, value = "传入JSON对象参数", name = "msg") @RequestBody SolrData msg) throws Exception {
		client.addBean(msg);
		client.commit();
	}
    /*
     * 接口不想暴露可以添加hidden=true或者@ApiIgnore注解添加方法上即可
     */
	/*@ApiOperation(value = "Solrj单条件查询",httpMethod = "GET", notes = "Solrj单条件查询", response = SolrData.class, hidden = true)*/
	@ApiOperation(value = "Solrj单条件查询",httpMethod = "GET", notes = "Solrj单条件查询", response = SolrData.class)
	@ResponseBody
	@RequestMapping(value = "/testSingleSearch", method = RequestMethod.GET)
	@ApiResponse(code = 200, message = "Success", response = SolrData.class, responseContainer = "Map")
	public Result<List<SolrData>> testSingleSearch(
			@ApiParam(name = "token", value = "令牌", required = true, example = "token_123456") @RequestHeader String token,
			@ApiParam(name = "condition", value = "条件表达式", required = true, example = "title:玩耍") @RequestParam String condition,
			@ApiParam(name = "start", value = "开始记录数", required = true, example = "1",defaultValue="1",allowableValues="1,2,3") @RequestParam int start,
			@ApiParam(name = "end", value = "截止记录数", required = true, example = "10",defaultValue="10") @RequestParam int end)
			throws Exception {
		System.err.println("token:"+token);
		Result<List<SolrData>> res = new Result<List<SolrData>>();
		List<SolrData> foos = search(condition, start, end);
		for (SolrData SolrData : foos) {
			System.out.println(JSON.toJSONString(SolrData));
		}
		res.setData(foos);
		return res;
	}
	
	/**
	 * 单条件查询
	 * eg:
	 *   title:张飞
	 * @keywords:关键字
	 */
	@ApiIgnore
	private  List<SolrData> search(String keywords,Integer page,Integer rows) throws Exception{
		SolrQuery solrQuery=new SolrQuery();
		/*solrQuery.setQuery("title:"+keywords);//搜索关键字,不指定就使用默认字段查询*/
		solrQuery.setQuery(keywords);//搜索关键字,不指定就使用默认字段查询
		solrQuery.setStart((Math.max(page,1)-1)*rows);
		solrQuery.setRows(rows);
		/**
		 * 是否高亮显示
		 *  针对单条件查询时，高亮设置没有问题
		 *    eg: title:张飞  这种只有一个参数查询正常
		 *  针对多条件查询时，高亮设置会有问题
		 *    eg: title:张飞  and price:200  这种只有多个参数查询，设置高亮显示时，会有问题
		 */
		boolean isHighlighting=keywords.indexOf(":")!=-1&&(StringUtils.indexOf(keywords, ":")==StringUtils.lastIndexOf(keywords, ":"));
		if(isHighlighting){
			//设置高亮
			solrQuery.setHighlight(Boolean.TRUE);//开启高亮
			solrQuery.addHighlightField("title");//设置高亮字段
			solrQuery.setHighlightSimplePre("<em>");//标记高亮关键字前缀
			solrQuery.setHighlightSimplePost("</em>");//标记高亮关键字后缀
		}
		//执行查询
		QueryResponse queryResponse=client.query(solrQuery);
	    List<SolrData> foos=queryResponse.getBeans(SolrData.class);
	    if(isHighlighting){
	    	//将高亮标题数据写入数据对象中
	    	Map<String,Map<String,List<String>>> map=queryResponse.getHighlighting();
	    	for(Map.Entry<String,Map<String,List<String>>> highlighting:map.entrySet()){
	    		for(SolrData data:foos){
	    			if(!highlighting.getKey().equals(data.getId().toString())){
	    				continue;
	    			}
	    			data.setTitle(StringUtils.join(highlighting.getValue().get("title"),""));
	    			break;
	    		}
	    	}
	    }
		return foos;
	}
	
	
}

package com.qdone.framework.core.page;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.ObjectUtils;

import com.alibaba.fastjson.JSON;
import com.qdone.framework.core.constant.Constants;



/**
 * 系统常用工具类
 * 
 * @author 傅为地
 */

public class CoreUtil {
	
	/**
	 * 列转换成驼峰
	 *  eg:a_bb_c转换成aBbCc
	 */
	public static final String convertColumnToBean(String column) {
		StringBuilder result = new StringBuilder();
		// 快速检查
		if (column == null || column.isEmpty()) {
			// 没必要转换
			return "";
		} else if (!column.contains("_")) {
			// 不含下划线，仅将首字母小写
			return column.substring(0, 1).toLowerCase() + column.substring(1);
		} else {
			// 用下划线将原始字符串分割
			String[] columns = column.split("_");
			for (String columnSplit : columns) {
				// 跳过原始字符串中开头、结尾的下换线或双重下划线
				if (columnSplit.isEmpty()) {
					continue;
				}
				// 处理真正的驼峰片段
				if (result.length() == 0) {
					// 第一个驼峰片段，全部字母都小写
					result.append(columnSplit.toLowerCase());
				} else {
					// 其他的驼峰片段，首字母大写
					result.append(columnSplit.substring(0, 1).toUpperCase()).append(columnSplit.substring(1).toLowerCase());
				}
			}
			return result.toString();
		}

	}
    /**
     * 驼峰转换成列类型(只转换_标注驼峰，其他.区分的不操作) 
     * eg:aBbCc转换成a_bb_c
     */
    public static final String convertBeanToCloumn(String entityName){
		StringBuffer buffer = new StringBuffer(2048);
	    List<Object> arr=new ArrayList<Object>(100);
	    //先将结果暂时存入list中
		for(int i=0;i<entityName.length();i++){
				if(i>0&&i<entityName.length()-1){
					if(Character.isUpperCase(entityName.charAt(i))){
						arr.add("_");
						arr.add(entityName.charAt(i));
					}else{
						arr.add(entityName.charAt(i));
					}
				}else{
					arr.add(entityName.charAt(i));
				}
		}
		//将list转换成
		for (int i = 0; i < arr.size(); i++) {
			buffer.append(arr.get(i));
		}
		return buffer.toString();
	} 
	/**
	 * 得到非空字符串
	 * 
	 * @param obj
	 * @return
	 */
	public static final String getNotNullStr(Object obj) {
		return isEmpty(obj) ? "" : obj.toString().trim();
	}

	/**
	 * 是不是为空
	 */
	public static final boolean isEmpty(Object obj) {
		return obj == null || obj.toString().trim().equalsIgnoreCase("") || obj.toString().trim().length() == 0;
	}

	/**
	 * 处理页面URL中中文乱码问题
	 */
	public static final String ISO2Utf8(String str) {
		if (str == null || str.trim().equals("")) {
			return "";
		}
		String regEx = "[\\u4e00-\\u9fa5]";// 匹配乱码正则表达式
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		if (!m.find()) {
			try {
				str = new String(str.getBytes("ISO-8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				return "";
			}
		}
		return str;
	}
	
	/**
	 * 将字符串""转换成null
	 * @param account
	 * @return
	 */
	public static String conventEmpty2Null(Object obj)
	{
		if(obj==null){
			return null;
		}else{
			if(obj.toString().equals("")){
					return null;
			}else{
					return obj.toString();
			}
		}
	}
	/***********开始***********************bootstrap-table分页page-helper插件 核心代码*********************************************************/
	/**
	 * 创建page-helper排序字段信息
	 *  支持多表连接排序eg:
	 *  select a.name,b.status
	 *     from table a,table b 
	 *   where a.fkid=b.pkid 
	 *   order by a.name b.status desc
	 * createSort
	 * @warn(页面bootstrap-table默认获取
	 * 列的field:"memo"内容，
	 * 可自定义指定:sortName:"a.name"
	 * 排序时，会生成排序名称:a.name desc|a.name asc格式
	 * 使用时，请注意sortName支持的格式(table.cloumName desc/asc)格式
	 * 务必不要写(table_cloumName desc/asc)别名中不要传递"_",会被转换驼峰
	 * )
	 * @param pageInfo
	 * @param isAuto
	 * @return
	 */
	public static final String createSort(PageInfo pageInfo,boolean isAuto) {
		String sortString="";
		if(CoreUtil.isEmpty(pageInfo.getMutiSortString())){//单列排序
			sortString= handleSingleSort(pageInfo,isAuto);
		}else{//多列排序
			sortString=pageInfo.getMutiSortString();
		}
		return sortString;
	}
	
	/**
	 * 处理单列排序
	 * @param pageInfo
	 * @return
	 */
	public static final String handleSingleSort(PageInfo pageInfo,boolean isAuto) {
		String sortString = "";
		if (pageInfo != null && pageInfo.getFieldOrder() != null
				&& pageInfo.getSortOrder() != null) {
			if (!pageInfo.getFieldOrder().equals("")
					&& !pageInfo.getSortOrder().equals("")) {
					// 单列排序处理
					if (pageInfo.getFieldOrder().indexOf(",") == -1
							&& pageInfo.getSortOrder().indexOf(",") == -1) {
						if(isAuto){
							sortString = CoreUtil.convertBeanToCloumn(pageInfo.getFieldOrder()) + Constants.Paginator.MYBATIS_PAGEHELPER_SPLIT+ pageInfo.getSortOrder();
						}else{
							sortString = pageInfo.getFieldOrder()+ Constants.Paginator.MYBATIS_PAGEHELPER_SPLIT+ pageInfo.getSortOrder();
						}
					} 
			}
		}
		return sortString;
	}
	
	 /**
	 * buildBootStrapPage
	 * 组装Bootstrap-table分页方式有两种/本处都做了适配
	 * @warn(注意事项 – 可选)
	 * 是否自动将:areaName转换成area_name表字段排序
	 * @param param
	 * @param isAuto:true自动|false不转换(默认转换)
	 * @return
	 */
	  public static final PageInfo buildBootStrapPage(Map<String, Object> param,boolean isAuto) {
			boolean isMutiSort=false;//默认单列排序
			//将map中文参数转码
			if(!param.isEmpty()){//中文强制编码，防止URL传参乱码,直接在tomcat配置文件
				//本次执行可能执行多列排序
				Iterator<String> itMuti= param.keySet().iterator();
				while(itMuti.hasNext()){
					String key=(String) itMuti.next();
					if((key.indexOf("multiSort[")!=-1&&key.indexOf("][sortName]")!=-1&&key.indexOf("multiSort[")==key.lastIndexOf("multiSort[")&&key.indexOf("][sortName]")==key.lastIndexOf("][sortName]"))
							||(key.indexOf("multiSort[")!=-1&&key.indexOf("][sortOrder]")!=-1&&key.indexOf("multiSort[")==key.lastIndexOf("multiSort[")&&key.indexOf("][sortOrder]")==key.lastIndexOf("][sortOrder]"))){
						isMutiSort=true;
						break;
					}
				}
			}
			// 修改分页数目
			PageInfo pageInfo = new PageInfo();
			// 当前页,每页条数
			int pageIndex = 1, pageSize = Integer.MAX_VALUE;
			//排序处理
			String sortField = "", sortOrder = "",mutiSortString="";
			/**传递data-query-params-type参数为:limit时的分页方式offset,limit  注意：sort,order非必填*/
			if(param.containsKey("offset")&&param.containsKey("limit")){
				pageIndex = Integer.parseInt(CoreUtil.getNotNullStr(param.get("offset")));
				pageSize = Integer.parseInt(CoreUtil.getNotNullStr(param.get("limit")));
				pageIndex =pageIndex / pageSize + 1;//重新计算当前页
				sortField = CoreUtil.getNotNullStr(param.get("sort"));
				sortOrder = CoreUtil.getNotNullStr(param.get("order"));
				if(CoreUtil.isEmpty(param.get("sort"))&&isMutiSort){//本次使用多列排序
					mutiSortString=handleMutiSort(param,isAuto);
					pageInfo.setMutiSortString(mutiSortString);
					pageInfo.setFieldOrder(null);
					pageInfo.setSortOrder(null);
				}
				if(!CoreUtil.isEmpty(param.get("sort"))){//本次使用单列排序
					pageInfo.setFieldOrder(sortField);// 排序字段
					pageInfo.setSortOrder(sortOrder);// 排序命令
					pageInfo.setMutiSortString(null);
				}
			}
			/**传递data-query-params-type参数为:''空字符串时的分页方式:pageNumber,pageSize 注意：sortName,sortOrder非必填项*/
			if(param.containsKey("pageNumber") &&param.containsKey("pageSize")){
				pageIndex = Integer.parseInt(CoreUtil.getNotNullStr(param.get("pageNumber")));
				pageSize = Integer.parseInt(CoreUtil.getNotNullStr(param.get("pageSize")));
				sortField = CoreUtil.getNotNullStr(param.get("sortName"));
				sortOrder = CoreUtil.getNotNullStr(param.get("sortOrder"));
				if(CoreUtil.isEmpty(param.get("sortName"))&&isMutiSort){//本次使用多列排序
					mutiSortString=handleMutiSort(param,isAuto);
					pageInfo.setMutiSortString(mutiSortString);
					pageInfo.setFieldOrder(null);
					pageInfo.setSortOrder(null);
				}
				if(!CoreUtil.isEmpty(param.get("sortName"))){//本次使用单列排序
					pageInfo.setFieldOrder(sortField);// 排序字段
					pageInfo.setSortOrder(sortOrder);// 排序命令
					pageInfo.setMutiSortString(null);
				}
			}
			pageInfo.setCurrentNumber(pageIndex);// 当前页
			pageInfo.setPageSize(pageSize);// 每页显示条数
			return pageInfo;
		}
	  
	  /** 处理bootstrap-table多列排序:multiSort[0][sortName]=sname, multiSort[0][sortOrder]=asc此种形式  **/
	  /**
	   * handleMutiSort
	   * 处理bootstrap-table多列排序:multiSort[0][sortName]=sname, multiSort[0][sortOrder]=asc此种形式 
	   * @warn(注意事项 – 可选)
	   * @param param
	   * @param isAuto:是否自动将:areaName转换成area_name表字段排序 
	   *           true自动|false不转换(默认转换)
	   * @return
	   */
	  public static final  String handleMutiSort(Map<String, Object> param,boolean isAuto){
				String mutiSort="";
				List<String> sorts=new ArrayList<String>();		
				Iterator<String> it=param.keySet().iterator();
				List<String> keys=new ArrayList<String>();
				//只装载多列排序参数(非法参数将在本处被过滤掉)
				while(it.hasNext()){
					String str=it.next();
					if((str.indexOf("multiSort[")!=-1&&str.indexOf("][sortName]")!=-1&&str.indexOf("multiSort[")==str.lastIndexOf("multiSort[")&&str.indexOf("][sortName]")==str.lastIndexOf("][sortName]"))
							||(str.indexOf("multiSort[")!=-1&&str.indexOf("][sortOrder]")!=-1&&str.indexOf("multiSort[")==str.lastIndexOf("multiSort[")&&str.indexOf("][sortOrder]")==str.lastIndexOf("][sortOrder]"))){
						keys.add(str);
					}
				}
				//优化算法
				int total=keys.size()/2;
				for (int i = 0; i <total; i++) {
					String sortName="multiSort["+i+"][sortName]";
					String sortOrder="multiSort["+i+"][sortOrder]";
					if(param.containsKey(sortName)&&param.containsKey(sortOrder)){//保证同时出现
						if(!CoreUtil.isEmpty(param.get(sortName))&&!CoreUtil.isEmpty(param.get(sortOrder))){//保证内容不为空,为空处理没有意义
							if(isAuto){//是否自动转换成bean类型
								sorts.add(CoreUtil.convertBeanToCloumn(param.get(sortName).toString())+Constants.Paginator.MYBATIS_PAGEHELPER_SPLIT+param.get(sortOrder).toString());
							}else{
								sorts.add(param.get(sortName).toString()+Constants.Paginator.MYBATIS_PAGEHELPER_SPLIT+param.get(sortOrder).toString());
							}
						}
					}
				}
				mutiSort=CoreUtil.getNotNullStr(sorts);
				mutiSort=mutiSort.length()>2?mutiSort.substring(1, mutiSort.length()-1):"";
				return mutiSort;
			}
	  
	  
	 /***********结束***********************bootstrap-table分页核心代码*********************************************************/
	 /***********开始***********************bootstrap-table分页核心代码*********************************************************/
     /*采用统一mutiSort实体类方式,传入参数不在使用map,提高代码可读性
      * 代码eg:
      * public class DataInterrupt extends MutiSort {
      *         //实体类中扩展自定义查询,排序参数
      *         private Date startTime;
	  *			private Date endTime;
	  *		    private Date importTime;
	  *			private String breakSource;
	  *		    private String status;
      * }
      * MutiSort:针对分页排序,做了集中封装,子类必须继承MutiSort类,不允许扩展分页排序信息
      *    针对上面controller传入map方式做了优化,提高代码可读性
      * */
	 public static final PageInfo createBootStrapPage(Object param,boolean isAuto) {
			// 修改分页数目
			PageInfo pageInfo = new PageInfo();
			// 当前页,每页条数(当页面设置,不分页时,offset,limit,pageNumber,pageSize全为0,这里就默认设置最大分页数)
			int pageIndex = 1, pageSize = Integer.MAX_VALUE;
			//排序处理
			String sortField = "", sortOrder = "",mutiSortString="";
			if(param instanceof MutiSort){//启用多列排序
				MutiSort sort=(MutiSort)param;
				/**传递data-query-params-type参数为:limit时的分页方式offset,limit  注意：sort,order非必填*/
				if((sort.getPageNumber()==0&&sort.getPageSize()==0)&&(sort.getOffset()!=0||sort.getLimit()!=0)){
					pageIndex = sort.getOffset();
					pageSize = sort.getLimit();
					pageIndex =pageIndex / pageSize + 1;//重新计算当前页
					sortField = CoreUtil.getNotNullStr(sort.getSort());
					sortOrder = CoreUtil.getNotNullStr(sort.getOrder());
					if(StringUtils.isNotEmpty(sort.getSort())){//本次使用单列排序
						pageInfo.setFieldOrder(sortField);// 排序字段
						pageInfo.setSortOrder(sortOrder);// 排序命令
						pageInfo.setMutiSortString(null);
					}else{//本次查询使用多列排序
						mutiSortString=createMutiSort(sort,isAuto);
						pageInfo.setMutiSortString(mutiSortString);
						pageInfo.setFieldOrder(null);
						pageInfo.setSortOrder(null);
					}
				}
				/**传递data-query-params-type参数为:''空字符串时的分页方式:pageNumber,pageSize 注意：sortName,sortOrder非必填项*/
				if((sort.getOffset()==0&&sort.getLimit()==0)&&(sort.getPageNumber()!=0||sort.getPageSize()!=0)){
					pageIndex = sort.getPageNumber();
					pageSize = sort.getPageSize();
					sortField = CoreUtil.getNotNullStr(sort.getSortName());
					sortOrder = CoreUtil.getNotNullStr(sort.getSortOrder());
					if(StringUtils.isNotEmpty(sort.getSortName())){//本次使用单列排序
						pageInfo.setFieldOrder(sortField);// 排序字段
						pageInfo.setSortOrder(sortOrder);// 排序命令
						pageInfo.setMutiSortString(null);
					}else{//本次查询使用多列排序
						mutiSortString=createMutiSort(sort,isAuto);
						pageInfo.setMutiSortString(mutiSortString);
						pageInfo.setFieldOrder(null);
						pageInfo.setSortOrder(null);
					}
				}
			}
			pageInfo.setCurrentNumber(pageIndex);// 当前页
			pageInfo.setPageSize(pageSize);// 每页显示条数
			return pageInfo;
		}
	 /*采用统一mutiSort实体类方式,处理多列排序
	  * MutiSort:针对分页排序,做了集中封装,子类必须继承MutiSort类,不允许扩展分页排序信息
      *    针对上面controller传入map方式做了优化,提高代码可读性
      *    辅助方法  
	  * */
	 public static final  String createMutiSort(MutiSort param,boolean isAuto){
			String mutiSort="";
			List<String> sorts=new ArrayList<String>();
			if(!ObjectUtils.isEmpty(param.getMultiSort())){
				for (int i = 0; i < param.getMultiSort().size(); i++) {
					MutiSort.Data data=param.getMultiSort().get(i);
					if(StringUtils.isNotEmpty(data.getSortName())&&StringUtils.isNotEmpty(data.getSortOrder())){//保证内容不为空,为空处理没有意义
						if(isAuto){//是否自动转换成bean类型
							sorts.add(CoreUtil.convertBeanToCloumn(data.getSortName())+Constants.Paginator.MYBATIS_PAGEHELPER_SPLIT+data.getSortOrder());
						}else{
							sorts.add(data.getSortName()+Constants.Paginator.MYBATIS_PAGEHELPER_SPLIT+data.getSortOrder());
						}
					}
				}
			}
			mutiSort=CoreUtil.getNotNullStr(sorts);
			mutiSort=mutiSort.length()>2?mutiSort.substring(1, mutiSort.length()-1):"";
			return mutiSort;
		}
	 /***********开始***********************通用日志打印处理参数核心代码*********************************************************/
	 /**
	  * 格式化传入参数
	  */
	 public static final String buildInputParamToString(Object[] args,Class<?>[] paremClassTypes){
		    String inputArgString="";//传入参数格式化结果字符串
		    List<Object> inputParamMap = new ArrayList<Object>(); // 传入参数
			//优先级打印，request跟map和MutiSort以及其他参数共存时，优先打印request，不在打印map和mutiSort
			int isReq=0,isMap=0,isMutiSort=0;//是否包含request
			//优先级打印，request跟map和MutiSort对应的args下表位置
			int isReqIndex=0,isMapIndex=0,isMutiSortIndex=0;//是否包含request
			for (int i = 0; i < args.length; i++) {
				//自身类.class.isAssignableFrom(自身类或子类.class)  返回true 
				if (HttpServletRequest.class.isAssignableFrom(paremClassTypes[i])) {//优先request
					isReq=1;
					isReqIndex=i;
				}
				else if (Map.class.isAssignableFrom(paremClassTypes[i])) {//优先Map
					isMap=1;
					isMapIndex=i;
				}
				else if (MutiSort.class.isAssignableFrom(paremClassTypes[i])) {//MutiSort
					isMutiSort=1;
					isMutiSortIndex=i;
				}
			}
			//打印数据处理
		    if (isReq == 1&&HttpServletRequest.class.isAssignableFrom(paremClassTypes[isReqIndex])) {//打印request，最高优先级
				HttpServletRequest req = (HttpServletRequest) args[isReqIndex];
				inputParamMap.add(getParameterMap(req));
			} 
			else if(isMap == 1&& Map.class.isAssignableFrom(paremClassTypes[isMapIndex])) {//打印map，第二优先级
				inputParamMap.add(args[isMapIndex]);
			}else{//打印String,Integer,long常规数据类型
				for (int i = 0; i < args.length; i++) {
					    //排除Request,map,mutiSort剩下的参数打印处理
					    if(isReq==1&&i==isReqIndex){//Request
					    	continue;
					    }else if(isMap==1&&i==isMapIndex){//map
					    	continue;
					    }else if(isMutiSort==1&&i==isMutiSortIndex){
					    	continue;
					    }else{
					    	if (!HttpServletResponse.class.isAssignableFrom(paremClassTypes[i])) {// 排除response
								inputParamMap.add(args[i]);
							}
					    }
					    
					    
				}
			}
		    if (isMutiSort == 1&& MutiSort.class.isAssignableFrom(paremClassTypes[isMutiSortIndex])) {//打印MutiSort最后显示
				inputParamMap.add(args[isMutiSortIndex]);
			}
		    StringBuffer sb=new StringBuffer(1024);
		    inputParamMap.forEach(mp->{
		    	if(Map.class.isAssignableFrom(mp.getClass())||mp instanceof Map){//如果是map类型
		    		sb.append(mp.toString()).append("\t,\t");
		    	}else if(MutiSort.class.isAssignableFrom(mp.getClass())||mp instanceof MutiSort){//MutiSort分页排序
		    		sb.append(JSON.toJSONStringWithDateFormat(mp, "yyyy-MM-dd HH:mm:ss")).append("\t,\t");
		    	}else if(String.class.isAssignableFrom(mp.getClass())||mp instanceof String){//String类型参数
		    		 sb.append(mp).append("\t,\t");;
		    	}else{
		    		sb.append(JSON.toJSONStringWithDateFormat(mp, "yyyy-MM-dd HH:mm:ss")).append("\t,\t");
		    	}
		    });
		    inputParamMap.clear();
		    if(sb.indexOf("\t,\t")!=-1){
		    	inputArgString=sb.substring(0, sb.lastIndexOf("\t,\t")).replaceAll("\t,\t", ", ");
		    }
		   return inputArgString; 
	   }
		
		/**
	     * 读取request参数到map
	     */
		public static final Map<String, Object> getParameterMap(HttpServletRequest request) {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			Set<String> keys = request.getParameterMap().keySet();
			for (String key : keys) {
				resultMap.put(key, printString(request.getParameterValues(key)));
			}
			return resultMap;
		}
		
	    /**
		 * 字符串数组，格式化输出字符串
		*/
		public static final String printString(String[] arr) {
			StringBuffer sb = new StringBuffer(1024);
			for (int i = 0; i < arr.length; i++) {
				if (i == arr.length - 1) {
					sb.append(arr[i]);
				} else {
					sb.append(arr[i]).append(",");
				}
			}
			return sb.toString();
		}
	 /***********结束***********************通用日志打印处理参数核心代码*********************************************************/
}

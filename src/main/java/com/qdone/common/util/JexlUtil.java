package com.qdone.common.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;

/**
 * @author 付为地 
 *    动态计算参数规则表达式结果
 *    类似替换:drools积分规则引擎
 */
public class JexlUtil {
	public static void main(String[] args) {
		Map<String,Object> map=new HashMap<String,Object>();  
		map.put("testService",new JexlUtil());  
		map.put("name",5);  
		String expression="testService.save(name)";  
		invokeMethod(expression,map); 
		System.err.println("计算结果是:"+revoke("age","if(age>=0&&age<5){return 100;}else if(age<10){return 50;}else{return 0;}","50"));
	}
	
	/**
	 * 动态调用参数方法
	 * @warn(注意事项 – 可选)
	 * @param paramName:传入参数A的名称
	 * @param expression:传入参数A的value的计算表达式
	 * @param paramNameValue:传入参数A的value值
	 * @return:返回传入参数A的value在表达式中计算结果
	 */
	public static final Object revoke(String paramName,String expression,Object paramNameValue) {
		Map<String,Object> map=new HashMap<String,Object>();  
		map.put(paramName,paramNameValue);  
		return invokeMethod(expression,map);
	}
	/**
	 * 执行jexl表达式方法
	 * invokeMethod
	 * @param jexlExp:参数value运算表达式
	 * @param map:参数信息运算结果
	 * @return
	 */
	public static final Object invokeMethod(String jexlExp, Map<String, Object> map) {
		JexlEngine jexl = new JexlEngine();
		Expression e = jexl.createExpression(jexlExp);
		JexlContext jc = new MapContext();
		for (String key : map.keySet()) {
			jc.set(key, map.get(key)); 
		}
		Object result=e.evaluate(jc);
		if(result==null){
			return "";
		}else{
			return result;
		}
	}
	
	public int save(int name){
		System.err.println("保存"+name);
		return name;
	}
}

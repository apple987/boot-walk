package com.qdone.framework.core;

import com.qdone.framework.core.constant.Constants;
import com.qdone.framework.core.page.CoreUtil;
import com.qdone.framework.core.page.PageList;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.*;

public class BaseController {


	protected static final List<Object> noRrcords = new ArrayList<Object>(0);

	/**
	 * 所有ActionMap 统一从这里获取
	 * 
	 * @return
	 */
	public Map<String, Object> getRootMap() {
		Map<String, Object> rootMap = new LinkedHashMap<String, Object>();
		return rootMap;
	}

	/**
	 * 所有ActionMap 统一从这里获取
	 * 
	 * @return
	 */
	public Map<String, Object> getRootMap(int length) {
		Map<String, Object> rootMap = new HashMap<String, Object>(length);
		return rootMap;
	}

	public Map<String, Object> getRootMap(Map<String, Object> map) {
		if (map == null) {
			map = new HashMap<String, Object>();
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	public ModelAndView forword(String viewName, @SuppressWarnings("rawtypes") Map context) {
		viewName = Constants.TEMPLATE + viewName;
		return new ModelAndView(viewName, context);
	}

	public static String forword(String page) {
		return Constants.TEMPLATE + page;
	}

	@SuppressWarnings("unchecked")
	public ModelAndView redirect(String viewName, @SuppressWarnings("rawtypes") Map context) {
		return new ModelAndView(new RedirectView(viewName), context);
	}

	public ModelAndView redirect(String viewName) {
		return new ModelAndView(new RedirectView(viewName));
	}

	public ModelAndView error(String errMsg) {
		return new ModelAndView("error");
	}

	/**
	 * 过滤查询参数的字符串""
	 * 
	 * @param mp
	 * @return
	 */
	public Map<String, Object> filterEmpty(Map<String, Object> mp) {
		Iterator<Map.Entry<String, Object>> entryKeyIterator = mp.entrySet().iterator();
		while (entryKeyIterator.hasNext()) {
			Map.Entry<String, Object> e = entryKeyIterator.next();
			mp.put(e.getKey(), CoreUtil.conventEmpty2Null(e.getValue()));
		}
		return mp;
	}

	/**
	 * 响应bootstrap-table分页
	 * 
	 * @param <T>
	 * @param <T>
	 */
	public final Map<String, Object> responseSelectPage(PageList<?> pageNoList) {
		Map<String, Object> result = getRootMap();
		if (null != pageNoList && null != pageNoList.getList()) {
			result.put("rows", pageNoList.getList());
		} else {
			result.put("rows", noRrcords);
		}
		// 总记录条数
		if (null != pageNoList && null != pageNoList.getPageInfo().getRecordCount()) {
			result.put("total", pageNoList.getPageInfo().getRecordCount());
		} else {
			result.put("total", 0);
		}
		return result;
	}

	/**
	 * 不用分页使用此方法封装返回值
	 * 
	 * @param list
	 * @return
	 */
	public final Map<String, Object> responseSelectNoPage(List<?> list) {
		Map<String, Object> result = getRootMap();
		if (list == null) {
			result.put("rows", noRrcords);
			result.put("total", 0);
		} else {
			result.put("rows", list);
			result.put("total", list.size());
		}
		return result;
	}

}

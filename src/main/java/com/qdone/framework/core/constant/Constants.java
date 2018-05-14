package com.qdone.framework.core.constant;
/**
 * 常量类
 * @author 傅为地
 */
public  class Constants {

	public static final String CURRENT_USER = "user";//当前登录用户
	public static final String CURRENT_WEB_PROJECT = "NGINX.WEB.GROUP";// 项目集群的本项目key名称
	public static final String SESSION_FORCE_LOGOUT_KEY = "SESSION.FORCE.LOGOUT";// 1:页面强制剔除
																					// 2:后来登陆账户剔除前者,其他表示正常登录
	public static final String RandomValidateCode = "validateCode";// 验证码
	public static final boolean KickOutBeforeLogin = true;// 是否剔除前面登录的账户
	/* shiro-redis配置默认缓存key前缀,后续加上当前项目名称，多项目使用时会更好 */
	/*excel导出文件名*/
	public static final String EXCEL_DAILYCHECK_NAME = "每日巡检导出.xls";
	public static final String TEMPLATE = "view";//页面根目录
	public static interface ShiroRedisKey {
		String RedisCache = "SHIRO_REDIS_SESSION:";// RedisCache默认key前缀，
		String RedisCacheManager = "SHIRO_REDIS_CACHE:";// RedisCacheManager默认前缀
		String RedisSessionDAO = "SHIRO_REDIS_SESSION:";// RedisSessionDAO默认前缀
	}

	/* 用户登录授权验证结果信息*/
	public static final String Realm_Login_Message = "loginMessage";
	// 分隔符
	public static final String ID_SPLIT = ",";
	
	//系统快速布控默认案件ID的key
	public static final String QUICKLY_DEFAULT_CASEID = "QUICKLY_DEFAULT_CASEID";
	//系统快速布控默认对象ID的key
	public static final String QUICKLY_DEFAULT_OBJECTID = "QUICKLY_DEFAULT_OBJECTID";
	//信息发送文件保存路径
	public static final String  FILE_PATH = "D:/test/";

	/**
	 * mybatis-Paginator分页插件分割符
	 */
	public static interface Paginator{
		public static String MYBATIS_PAGINATOR_SPLIT=".";//mybatis-paginator采用.(顿号)方式
		public static String MYBATIS_PAGEHELPER_SPLIT=" ";//mybatis-pagehelper采用  (空格)方式
	}
	
	/**
	 * 字符集
	 */
	public static interface CHARSET {
		String GBK = "GBK";
		String UTF8 = "UTF-8";
		String GB2312 = "GB2312";
		String DEFAULT = UTF8;
	}
	
	/**
	 * 菜单等级
	 */
	public static interface MENUSORT {
		final String MENUSORT_ONE = "1";
		final String MENUSORT_TWO = "2";
		final String MENUSORT_THREE = "3";
	}
	/**
	 * 页面字符集
	 */
	public static  interface ContentType {
		String GBK = "text/xml; charset=GBK";
		String UTF8 = "text/xml; charset=UTF-8";
		String DEFAULT = UTF8;
		String JSON = "application/json; charset=UTF-8";
	}
	/**
	 * LogPrinter异常日志类型
	 */
	public enum LogType 
	{
		NORMAL("1","正常"),
		ABNORMAL("2","异常");
		
		String val;
		String desc;
		
		LogType(String val,String desc)
		{
			this.val=val;
			this.desc=desc;
		}		
		public String getVal(){
			return this.val;
		}
		public String getDesc(){
			return this.desc;
		}
	}
	/**
     * 定时任务状态
     * 
     * @author chenshun
     * @email sunlightcs@gmail.com
     * @date 2016年12月3日 上午12:07:22
     */
    public enum ScheduleStatus {
        /**
         * 正常
         */
    	NORMAL(0),
        /**
         * 暂停
         */
    	PAUSE(1);

        private int value;

        private ScheduleStatus(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
    }


}

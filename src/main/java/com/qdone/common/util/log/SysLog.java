package com.qdone.common.util.log;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.qdone.framework.core.page.MutiSort;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
  *该代码由付为地的编码机器人自动生成
  *时间：2018-01-21 09:40:29
*/
@ApiModel(value = "日志表", description = "SysLog实体类") 
public class SysLog  extends MutiSort {

    private static final long serialVersionUID = 1L;

    // Fields

    @ApiModelProperty(value = "编号", required = true) 
    private Integer id;
    @ApiModelProperty(value = "日志标题", required = false) 
    private String title;//日志标题
    @ApiModelProperty(value = "日志类型 1:正常 2：异常", required = false) 
    private String type;//日志类型 1:正常 2：异常
    @ApiModelProperty(value = "执行操作用户", required = false) 
    private String userId;//执行操作用户
    @ApiModelProperty(value = "请求URI", required = false) 
    private String requestUri;//请求URI
    @ApiModelProperty(value = "执行类名", required = false) 
    private String className;//执行类名
    @ApiModelProperty(value = "执行方法名称", required = false) 
    private String methodName;//执行方法名称
    @ApiModelProperty(value = "功能模块名称", required = false) 
    private String functionName;//功能模块名称
    @ApiModelProperty(value = "用户代理", required = false) 
    private String userAgent;//用户代理
    @ApiModelProperty(value = "操作IP地址", required = false) 
    private String remoteIp;//操作IP地址
    @ApiModelProperty(value = "操作方式", required = false) 
    private String requestMethod;//操作方式
    @ApiModelProperty(value = "请求参数", required = false) 
    private String requestParams;//请求参数
    @ApiModelProperty(value = "设备MAC", required = false) 
    private String requestMac;//设备MAC
    @ApiModelProperty(value = "异常信息", required = false) 
    private String exception;//异常信息
    @ApiModelProperty(value = "执行线程", required = false) 
    private String actionThread;//执行线程
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始执行时刻", required = false) 
    private Date actionStartTime;//开始执行时刻
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束执行时刻", required = false) 
    private Date actionEndTime;//结束执行时刻
    @ApiModelProperty(value = "执行耗时 单位(毫秒)", required = false) 
    private Long actionTime;//执行耗时 单位(毫秒)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日志时间", required = false) 
    private Date createDate;//创建日志时间
    @ApiModelProperty(value = "数据库实例", required = false) 
    private String database;//数据库实例
    
    // Constructors

    /** default constructor */
    public SysLog() {
    }

    // Property accessors

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public String getRequestUri() {
        return this.requestUri;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        return this.className;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getFunctionName() {
        return this.functionName;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getUserAgent() {
        return this.userAgent;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

    public String getRemoteIp() {
        return this.remoteIp;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getRequestMethod() {
        return this.requestMethod;
    }

    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams;
    }

    public String getRequestParams() {
        return this.requestParams;
    }

    public void setRequestMac(String requestMac) {
        this.requestMac = requestMac;
    }

    public String getRequestMac() {
        return this.requestMac;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getException() {
        return this.exception;
    }

    public void setActionThread(String actionThread) {
        this.actionThread = actionThread;
    }

    public String getActionThread() {
        return this.actionThread;
    }

    public void setActionStartTime(Date actionStartTime) {
        this.actionStartTime = actionStartTime;
    }

    public Date getActionStartTime() {
        return this.actionStartTime;
    }

    public void setActionEndTime(Date actionEndTime) {
        this.actionEndTime = actionEndTime;
    }

    public Date getActionEndTime() {
        return this.actionEndTime;
    }

    public void setActionTime(Long actionTime) {
        this.actionTime = actionTime;
    }

    public Long getActionTime() {
        return this.actionTime;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getDatabase() {
        return this.database;
    }

}
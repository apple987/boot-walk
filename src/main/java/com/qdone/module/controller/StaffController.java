package com.qdone.module.controller;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.qdone.common.util.SerialNo;
import com.qdone.framework.annotation.Function;
import com.qdone.framework.core.BaseController;
import com.qdone.module.model.Staff;
import com.qdone.module.service.StaffService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 *staff管理
 * @付为地
 * @date 2018-05-06 04:16:56
 */
@Controller
@RequestMapping("/staff")
@Api(tags = "职员管理",description = "职员信息管理")
public class StaffController extends BaseController{
  

    @Autowired
	private StaffService staffService;
    
    @Value("${upload.file.dir}")
	private String fileDir;
    
    /**
	 * 页面初始化
	 */
    @ApiOperation(value = "列表",notes = "进入列表页", httpMethod = "GET")
	@RequestMapping(value = "init",method = RequestMethod.GET)
	public String init(){
		return "staff/selectStaff";
	}
	
    /**
	 * 分页查询数据
	 */
	@RequestMapping(value="/selectPage",method=RequestMethod.POST)
	@ResponseBody
	@Function("分页")
	@ApiOperation(value = "分页列表", notes = "分页列表", httpMethod = "POST",response = Map.class)
	public Map<String, Object> selectPage(@RequestBody Staff entity){
		return responseSelectPage(staffService.selectPage(entity));
	}
	
	/**
	 * 跳转添加
	*/
    @RequestMapping(value="/preInsert",method=RequestMethod.GET)
    @ApiOperation(value = "跳转添加", notes = "进入添加页面", httpMethod = "GET")
	public String preInsert(HttpServletRequest req){
		return "staff/insertStaff";
	} 
	
    /**
     * 添加数据
     */
	@RequestMapping(value="/insert",method=RequestMethod.POST)
	@ResponseBody
	@Function("添加")
	@ApiOperation(value = "添加", notes = "创建", httpMethod = "POST",response = Boolean.class)
	public Boolean insert(
			@ApiParam(name = "对象", value = "传入json格式", required = true)   Staff entity,
			@RequestParam("policeFile") MultipartFile file) throws IOException {
		if(entity!=null&&file!=null&&!file.isEmpty()){
			 byte[] bytes = file.getBytes();
			 entity.setPolice(bytes);
		}
		entity.setPkid(SerialNo.getUNID());
		return staffService.insert(entity)>0?Boolean.TRUE:Boolean.FALSE;
	}
	
	/**
	 * 跳转更新
	*/
    @RequestMapping(value="/preUpdate",method=RequestMethod.GET)
    @ApiOperation(value = "跳转更新", notes = "进入更新页面", httpMethod = "GET")
	public String preUpdate(HttpServletRequest request){
	    request.setAttribute("staff", staffService.view(request.getParameter("pkid")));
		return "staff/updateStaff";
	}
    
    /**
     * 渲染职员图片ID
     * 针对police字段(BLOB),页面显示该图片
     */
    @RequestMapping(value="/getPhoto",method=RequestMethod.GET)
    public void getPhotoByPkid(String pkid, final HttpServletResponse response) throws IOException{  
        Staff staff=staffService.view(pkid);
        if(staff!=null&&staff.getPolice()!=null){
        	byte[] data = staff.getPolice();  
            response.setContentType("image/jpeg");  
            response.setCharacterEncoding("UTF-8");  
            OutputStream outputSream = response.getOutputStream();  
            InputStream in = new ByteArrayInputStream(data);  
            int len = 0;  
            byte[] buf = new byte[1024];  
            while ((len = in.read(buf, 0, 1024)) != -1) {  
                outputSream.write(buf, 0, len);  
            }  
            outputSream.close();  
        }
    }  
	
    /**
     * 更新数据
     */
	@RequestMapping(value="/update",method=RequestMethod.POST)
	@ResponseBody
	@Function("更新")
	@ApiOperation(value = "更新", notes = "更新信息", httpMethod = "POST",response = Boolean.class)
	public Boolean update(@ApiParam(name = "对象", value = "传入json格式", required = true) Staff entity,
			@RequestParam("policeFile") MultipartFile file) throws IOException {
		if(entity!=null&&file!=null&&!file.isEmpty()){
			 byte[] bytes = file.getBytes();
			 entity.setPolice(bytes);
		}
		return staffService.update(entity)>0?Boolean.TRUE:Boolean.FALSE;
	}
	
    /**
     * 删除数据
     */
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	@ResponseBody
	@Function("删除")
	@ApiOperation(value = "删除",notes = "删除", httpMethod = "POST",response = Boolean.class)
	public Boolean delete(@RequestBody List<Staff> ids) {
		return staffService.batchDelete(ids)>0?Boolean.TRUE:Boolean.FALSE;
	}
    
}

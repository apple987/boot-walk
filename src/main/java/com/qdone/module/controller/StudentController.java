package com.qdone.module.controller;

import com.alibaba.fastjson.JSON;
import com.qdone.common.util.CacheUtil;
import com.qdone.common.util.ExcelUtil;
import com.qdone.common.util.SerialNo;
import com.qdone.common.util.SessionUtil;
import com.qdone.common.util.mail.MailService;
import com.qdone.framework.annotation.Function;
import com.qdone.framework.core.BaseController;
import com.qdone.framework.core.constant.Constants;
import com.qdone.framework.exception.RRException;
import com.qdone.framework.util.lock.RedisLock;
import com.qdone.framework.util.lock.RedisLockKey;
import com.qdone.module.model.Student;
import com.qdone.module.model.User;
import com.qdone.module.service.StudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.aspectj.util.FileUtil;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * student管理
 * 
 * @付为地
 * @date 2017-07-09 06:53:38
 */
@Api(description = "学生信息管理", tags = "学生管理")
@Controller
@RequestMapping("/student")
public class StudentController extends BaseController {
	static Logger log = LoggerFactory.getLogger(StudentController.class);

	@Autowired
	private StudentService studentService;

	@Autowired
	private RedissonClient redissonClient;

	@Value("${upload.file.dir}")
	private String fileDir;

	@Autowired
	private MailService mailService;

	@Autowired
	CacheUtil cacheUtil;

	/**
	 * 页面初始化
	 */
	@ApiOperation(value = "学生列表", notes = "进入学生列表页", httpMethod = "GET")
	@RequestMapping(value = "init", method = RequestMethod.GET)
	public String init(HttpServletRequest request) {
		/*
		 * String token=UUID.randomUUID().toString();
		 * request.setAttribute("clientToken",token);
		 * request.getSession().setAttribute("serverToken",token);
		 */
		SessionUtil.setSessionObject(Constants.CURRENT_USER,new User("灭霸","123456",1,""));
		RAtomicLong atomicLong = redissonClient.getAtomicLong("test");
		System.err.println(atomicLong.getAndAdd(10));
		atomicLong.incrementAndGet();
		System.err.println("addAndGet:" + atomicLong.addAndGet(5));
		System.err.println("decrementAndGet:" + atomicLong.decrementAndGet());
		System.err.println("getAndDecrement:" + atomicLong.getAndDecrement());
		System.err.println("获取的分布式uuid是:" + atomicLong.get());
		atomicLong.set(0);
		System.err.println(atomicLong.isExists());
		atomicLong.delete();
		System.err.println(atomicLong.isExists());
		String[] arr = FileUtil.listFiles(new File(fileDir));
		request.setAttribute("fileNames", arr);
		cacheUtil.put("apple", "123456");
		return "student/selectStudent";
	}

	/**
	 * 分页查询数据
	 */
	/* @RequestMapping(value="/selectPage",headers="Accept=application/json") */
	@RequestMapping(value = "/selectPage", method = RequestMethod.POST)
	@ResponseBody
	@Function("查询分页")
	@ApiOperation(value = "分页列表", notes = "分页列表", httpMethod = "POST", response = Map.class)
	public Map<String, Object> selectPage(@RequestHeader("Accept") String encoding, @RequestBody Student entity) {
		System.err.println(encoding);
		System.err.println(cacheUtil.get("apple"));
	    /*System.err.println(1/0); */
		return responseSelectPage(studentService.selectPage(entity));
	}

	/**
	 * 跳转添加
	 */
	@ApiOperation(value = "跳转添加", notes = "进入添加页面", httpMethod = "GET")
	@RequestMapping(value = "/preInsert", method = RequestMethod.GET)
	public String preInsert(HttpServletRequest req) {
		return "student/insertStudent";
	}

	/**
	 * 添加数据
	 */
	@RequestMapping(value = "/insert", method = RequestMethod.PUT)
	@ResponseBody
	@Function("添加数据")
	@ApiOperation(value = "添加学生", notes = "创建学生", httpMethod = "PUT", response = Boolean.class)
	public Boolean insert(@ApiParam(name = "学生对象", value = "传入json格式", required = true) @RequestBody Student entity,
			HttpServletRequest request) {
		entity.setId(SerialNo.getRomdomID());
		Boolean bool = studentService.insert(entity).getOperateResult() > 0 ? Boolean.TRUE : Boolean.FALSE;
		return bool;
	}

	/**
	 * 跳转更新
	 */
	@ApiOperation(value = "跳转更新", notes = "进入更新页面", httpMethod = "GET")
	@RequestMapping(value = "/preUpdate", method = RequestMethod.GET)
	public String preUpdate(HttpServletRequest request) {
		request.setAttribute("student", studentService.view(Integer.parseInt(request.getParameter("id"))));
		return "student/updateStudent";
	}

	/**
	 * 更新数据
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	@Function("更新数据")
	@ApiOperation(value = "更新学生", notes = "更新学生", httpMethod = "POST", response = Boolean.class)
	public Boolean update(@ApiParam(name = "学生对象", value = "传入json格式", required = true) Student entity) {
		return studentService.update(entity).getOperateResult() > 0 ? Boolean.TRUE : Boolean.FALSE;
	}

	/**
	 * 删除数据
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "删除学生", position = 6, notes = "删除学生", httpMethod = "POST", response = Boolean.class)
	public Boolean delete(
			@ApiParam(name = "学生对象", value = "传入json格式", required = true) @RequestBody List<Student> ids) {
		int total = 0;
		for (Student s : ids) {
			total += studentService.delete(String.valueOf(s.getId()));
		}
		return total > 0 ? Boolean.TRUE : Boolean.FALSE;
	}

	@ApiOperation(value = "学生列表test", notes = "test进入学生列表页", httpMethod = "GET")
	@RequestMapping(value = "test", method = RequestMethod.GET)
	public ModelAndView test(HttpServletRequest request) {
		System.err.println("进入test进入学生列表页");
		return new ModelAndView("login");
	}

	/**
	 * 测试freemarker
	 */
	@ApiOperation(value = "测试freemarker", notes = "测试freemarker", httpMethod = "GET")
	@RequestMapping(value = "/freemarker", method = RequestMethod.GET)
	public String freemarker(HttpServletRequest req) {
		System.err.println("freemarker");
		List<Student> arr = studentService.selectList(null);
		req.setAttribute("list", arr);
		if (CollectionUtils.isNotEmpty(arr)) {
			req.setAttribute("map", arr.get(0));
		}
		// 测试简单，特殊字符
		req.setAttribute("special", "><");
		return "freemarker";
	}

	/**
	 * 测试redisLock
	 * 
	 * @throws InterruptedException
	 *             DistRedisLock:lockKey1lockKey
	 */
	@RequestMapping(value = "/redisLock")
	@ResponseBody
	@RedisLock(lockKey = "lockKey")
	@ApiOperation(value = "简单模拟redisLock", httpMethod = "POST", notes = "简单模拟redisLock", response = Boolean.class)
	public Boolean redisLock(
			@ApiParam(required = true, value = "RedisLockKey", name = "RedisLockKey") @RequestParam(value = "RedisLockKey") @RedisLockKey(order = 1) String key,
			@ApiParam(required = true, value = "key1", name = "key1") @RequestParam(value = "key1") @RedisLockKey(order = 0) int key1)
			throws InterruptedException {
		System.err.println("开始执行业务逻辑");
		TimeUnit.SECONDS.sleep(20);
		System.err.println("业务逻辑执行完毕");
		return true;
	}

	/**
	 * 文件上传
	 * 
	 * @param file
	 * @param req
	 * @param resp
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/fileupload")
	@ResponseBody
	public Object fileupload(@RequestParam("vitalPeople") MultipartFile file, HttpServletRequest req, Student student,
			HttpServletResponse resp) {
		Map<String, Object> result = getRootMap();
		if (student != null) {
			System.err.println(JSON.toJSONString(student));
		}
		if (file != null) {
			File dist = new File(fileDir + "\\" + file.getOriginalFilename());
			try {
				if (!dist.exists()) {
					dist.getParentFile().mkdirs();
					dist.createNewFile();
				}
				file.transferTo(dist);
				result.put("status", "成功");
				result.put("msg", "成功上传");
			} catch (IllegalStateException e) {
				e.printStackTrace();
				result.put("status", "失败");
				result.put("msg", e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				result.put("status", "失败");
				result.put("msg", e.getMessage());
			}
		}
		result.put("total", 1);
		return result;
	}

	/**
	 * 文件下载
	 */
	@RequestMapping("/downLoad")
	public void downLoad(HttpServletRequest req, HttpServletResponse resp, String fileId) {
		try {
			/* fileId = Base64Utils.decode(fileId.getBytes(), "UTF-8"); */
			/* fileId = new String(Base64Utils.decode(fileId.getBytes())); */
			InputStream is = new FileInputStream(new File(fileDir + "/" + fileId));
			String fileName = fileId.substring(fileId.lastIndexOf("/") + 1);
			resp.setContentType("application/octet-stream");
			resp.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
			resp.setCharacterEncoding("UTF-8");
			IOUtils.copy(is, resp.getOutputStream());
			is.close();
			FileUtils.forceDelete(new File(fileDir + "/" + fileId));
		} catch (Exception e) {
			log.error("文件下载异常:" + e.getMessage());
			throw new RRException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}

	/**
	 * 发邮件
	 */
	@RequestMapping("/sendEMail")
	@ResponseBody
	public Object baseMailTest() {
		Map<String, Object> result = getRootMap();
		mailService.sendAttachmentsMail("1335157415@qq.com", ">王大锤<邀请你一起听歌曲", "有附件，请查收!", "D:\\mail\\upload\\timg.jpg");
		System.out.println("email send ok");
		result.put("status", "200");
		return result;
	}

	/**
	 * 导出CSV
	 */
	@RequestMapping("/exportCsv")
	public void exportCsv(HttpServletResponse response) throws UnsupportedEncodingException {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String csvFileName = "学生列表" + format.format(new Date()) + ".csv";
		csvFileName = new String(csvFileName.getBytes("GB2312"), "8859_1");
		response.setContentType("text/html;charset=GBK");
		// createsmockdata
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment;filename=\"%s\"", csvFileName);
		response.setHeader(headerKey, headerValue);
		// CSV文件头
		Object[] FILE_HEADER = { "序号", "姓名", "性别", "年龄", "生日" };
		CSVPrinter csvFilePrinter = null;
		// 创建CSVFormat
		CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator("\n");
		try {
			// 初始化CSVPrinter
			csvFilePrinter = new CSVPrinter(response.getWriter(), csvFileFormat);
			// 创建CSV文件头
			csvFilePrinter.printRecord(FILE_HEADER);

			// 用户对象放入List
			List<Student> list = (List<Student>) studentService.selectList(null);
			// 遍历List写入CSV
			for (Student dt : list) {
				List<Object> record = new ArrayList<Object>();
				record.add(dt.getId());
				record.add(dt.getSname());
				record.add(dt.getSex().equals("1") ? "男" : "女");
				record.add(dt.getAge());
				record.add(JSON.toJSONStringWithDateFormat(dt.getBirthday(), "yyyy-MM-dd HH:mm:ss"));
				csvFilePrinter.printRecord(record);
			}
		} catch (Exception e) {

		} finally {
			try {
				csvFilePrinter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 导出Excel
	 */
	@RequestMapping("/exportPoiExcel")
	public void exportPoiExcel(HttpServletRequest request, HttpServletResponse response) {
		try {

			int size = 50000;
			int totalPageNum = 0;
			int total = 0;
			totalPageNum = 50000 * 3;
			if (totalPageNum > 0) {
				if (totalPageNum % size > 0) {
					total = totalPageNum / size + 1;
				} else {
					total = totalPageNum / size;
				}
			}
			OutputStream os = response.getOutputStream();
			response.reset();// 清空输出流
			/* 导出文件名 */
			ExcelUtil.setFileDownloadHeader(request, response, "学生列表.xls");
			response.setContentType("application/msexcel");// 定义输出类型
			SXSSFWorkbook workbook = new SXSSFWorkbook(15000);

			String[] titles = { "序号", "姓名", "性别", "年龄", "生日" };
			for (int i = 1; i <= total; i++) {
				List<Student> userList = (List<Student>) studentService.selectList(null);
				List<Object[]> contents = new ArrayList<Object[]>();
				for (Student data : userList) {
					Object[] conList = new Object[titles.length];
					conList[0] = data.getId();
					conList[1] = data.getSname();
					conList[2] = data.getSex().equals("1") ? "男" : "女";
					conList[3] = data.getAge();
					conList[4] = JSON.toJSONStringWithDateFormat(data.getBirthday(), "yyyy-MM-dd HH:mm:ss");
					contents.add(conList);
				}
				ExcelUtil.buildExcel(workbook, "POI学生列表", titles, contents, i, total, os);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Scheduled(fixedRate = 5000)
	public void reportCurrentTime() {
		System.out.println("现在时间：" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
	}
	
	
	@ApiOperation(value = "测试格式化显示", notes = "测试格式化显示", httpMethod = "GET")
	@RequestMapping(value = "/formatCode", method = RequestMethod.GET)
	public String formatCode(HttpServletRequest req) {
		System.err.println("formatJson");
		List<Student> arr = studentService.selectList(null);
		if (CollectionUtils.isNotEmpty(arr)) {
			req.setAttribute("jsonData", JSON.toJSONStringWithDateFormat(arr.get(0), "yyyy-MM-dd hh:mm:ss"));
			req.setAttribute("xmlData", "<code name=\"id\"><data name=\"apple\">我是内容</data></code>");
			req.setAttribute("cssData", "p.no2 {background-color: gray; padding: 20px;}");
			req.setAttribute("sqlData", "select * from student where 1=1 and id=\"123456\" ");
		}
		return "format_code";
	}

}

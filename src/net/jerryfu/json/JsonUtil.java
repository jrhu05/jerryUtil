package net.jerryfu.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * JSON工具类
 * @author jerryfu
 *
 */
public class JsonUtil {

	/**
	 * LOGger.
	 */
	private static final Log LOG = LogFactory.getLog(JsonUtil.class);

	/**
	 * 私有构造方法,将该工具类设为单例模式.
	 */
	private JsonUtil() {

	}
	
	/**
	 * 函数功能说明 ：  读取post请求的json数据<br/>
	 * 修改者名字： <br/>
	 * 修改日期： <br/>
	 * 修改内容：<br/>
	 * 作者：roncoo-lrx <br/>
	 * 参数：@param request
	 * 参数：@return <br/>
	 * return：JSONObject <br/>
	 */
	public static JSONObject requestJson(HttpServletRequest request) {
		StringBuffer buffer = new StringBuffer();
		String line = null;
		JSONObject jsonObject = null;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			reader.close();
			jsonObject = JSONObject.parseObject(buffer.toString()) ;  
		} catch (Exception e) {
			LOG.error(e);
		}
		return jsonObject;
	}
	
	/**
	 * 函数功能说明 ：返回数据转JSON格式字符串并指定UTF-8编码 <br/>
	 * 修改者名字： <br/>
	 * 修改日期： <br/>
	 * 修改内容：<br/>
	 * 作者：roncoo-lrx <br/>
	 * 参数：@param response
	 * 参数：@param result <br/>
	 * return：void <br/>
	 */
	public static void responseJson(HttpServletResponse response, Map<String, Object> result) {
		String jsonStr = JSON.toJSONStringWithDateFormat(result, "yyyy-MM-dd HH:mm:ss");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter p = null;
		try {
			p = response.getWriter();
			p.write(jsonStr);
		} catch (IOException e) {
			LOG.error(e);
		} finally {
			if (p != null)
				p.close();
		}
	}
	
	
	/**
	 * 函数功能说明 ：将实体转换为Json格式输出 <br/>
	 * 修改者名字： <br/>
	 * 修改日期： <br/>
	 * 修改内容：<br/>
	 * 作者：roncoo-lrx <br/>
	 * 参数：@param response
	 * 参数：@param object <br/>
	 * return：void <br/>
	 */
	public static void responseJson(HttpServletResponse response, Object object) {
		String jsonStr = JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");

		PrintWriter p = null;
		try {
			p = response.getWriter();
			p.write(jsonStr);
		} catch (IOException e) {
			LOG.error(e);
		} finally {
			if (p != null)
				p.close();
		}
	}
	
	/**
	 * json转object
	 */
	public static <T> T jsonToObject(String jsonString, Class<T> pojoCalss) {
		return JSONObject.parseObject(jsonString, pojoCalss);
	}

	/**
	 * json转 List
	 */
	public static <T> List<T> jsonToListObject(JSONArray jsonArray, Class<T> pojoCalss) {
		return JSONArray.parseArray(jsonArray.toString(), pojoCalss);
	}
}

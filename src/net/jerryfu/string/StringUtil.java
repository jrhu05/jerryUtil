/*
 * Copyright 2015-2102 RonCoo(http://www.roncoo.com) Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.jerryfu.string;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * String字符串工具类.
 * @author jerryfu
 *
 */
public final class StringUtil {

	private static final Logger LOG = LoggerFactory.getLogger(StringUtil.class);

	/**
	 * 私有构造方法,将该工具类设为单例模式.
	 */
	private StringUtil() {
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotNull(String str) {
		if (str != null && !"".equals(str.trim())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断对象是否为空
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNotNull(Object obj) {
		if (obj != null && obj.toString() != null && !"".equals(obj.toString().trim())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 函数功能说明 ： 判断字符串是否为空 . 修改者名字： 修改日期： 修改内容：
	 * 
	 * @参数： @param str
	 * @参数： @return
	 * @return boolean
	 * @throws
	 */
	public static boolean isEmpty(String str) {
		return null == str || "".equals(str.trim());
	}

	/**
	 * 函数功能说明 ： 判断对象数组是否为空. 修改者名字： 修改日期： 修改内容：
	 * 
	 * @参数： @param obj
	 * @参数： @return
	 * @return boolean
	 * @throws
	 */
	public static boolean isEmpty(Object[] obj) {
		return null == obj || 0 == obj.length;
	}

	/**
	 * 函数功能说明 ： 判断对象是否为空. 修改者名字： 修改日期： 修改内容：
	 * 
	 * @参数： @param obj
	 * @参数： @return
	 * @return boolean
	 * @throws
	 */
	public static boolean isEmpty(Object obj) {
		if (null == obj) {
			return true;
		}
		if (obj instanceof String) {
			return ((String) obj).trim().isEmpty();
		}
		return !(obj instanceof Number) ? false : false;
	}

	/**
	 * 函数功能说明 ： 判断集合是否为空. 修改者名字： 修改日期： 修改内容：
	 * 
	 * @参数： @param obj
	 * @参数： @return
	 * @return boolean
	 * @throws
	 */
	public static boolean isEmpty(List<?> obj) {
		return null == obj || obj.isEmpty();
	}

	/**
	 * 函数功能说明 ： 判断Map集合是否为空. 修改者名字： 修改日期： 修改内容：
	 * 
	 * @参数： @param obj
	 * @参数： @return
	 * @return boolean
	 * @throws
	 */
	public static boolean isEmpty(Map<?, ?> obj) {
		return null == obj || obj.isEmpty();
	}

	/**
	 * 函数功能说明 ： 获得文件名的后缀名. 修改者名字： 修改日期： 修改内容：
	 * 
	 * @参数： @param fileName
	 * @参数： @return
	 * @return String
	 * @throws
	 */
	public static String getExt(String fileName) {
		return fileName.substring(fileName.lastIndexOf(".") + 1);
	}

	/**
	 * 获取去掉横线的长度为32的UUID串.
	 * 
	 * @author WuShuicheng.
	 * @return uuid.
	 */
	public static String get32UUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * 获取带横线的长度为36的UUID串.
	 * 
	 * @author WuShuicheng.
	 * @return uuid.
	 */
	public static String get36UUID() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 验证一个字符串是否完全由纯数字组成的字符串，当字符串为空时也返回false.
	 * 
	 * @author WuShuicheng .
	 * @param str
	 *            要判断的字符串 .
	 * @return true or false .
	 */
	public static boolean isNumeric(String str) {
		if (StringUtils.isBlank(str)) {
			return false;
		} else {
			return str.matches("\\d*");
		}
	}

	/**
	 * 计算采用utf-8编码方式时字符串所占字节数
	 * 
	 * @param content
	 * @return
	 */
	public static int getByteSize(String content) {
		int size = 0;
		if (null != content) {
			try {
				// 汉字采用utf-8编码时占3个字节
				size = content.getBytes("utf-8").length;
			} catch (UnsupportedEncodingException e) {
				LOG.error("UnsupportedEncodingException:" , e);
			}
		}
		return size;
	}

	/**
	 * 函数功能说明 ： 截取字符串拼接in查询参数. 修改者名字： 修改日期： 修改内容：
	 * 
	 * @参数： @param ids
	 * @参数： @return
	 * @return String
	 * @throws
	 */
	public static List<String> getInParam(String param) {
		boolean flag = param.contains(",");
		List<String> list = new ArrayList<String>();
		if (flag) {
			list = Arrays.asList(param.split(","));
		} else {
			list.add(param);
		}
		return list;
	}

	/**
	 * 将字符串解析成Map , 只适用于键值对拼接的字符串,例如:
	 * ssss=222&bbb=333&ccc=888
	 * @return
	 */
	public static Map<String , Object> parseStringToMap(String sourceString){

		return parseStringToMap(sourceString , "&");
	}

	/**
	 * 将字符串解析成Map , 只适用于键值对拼接的字符串,例如:
	 *
	 * @return
	 */
	public static Map<String , Object> parseStringToMap(String sourceString , String splitChar){
		if (isEmpty(sourceString)){
			return null;
		}

		Map<String , Object> resultMap = new HashMap<String , Object>();
		String[] sourceArr = sourceString.split(splitChar);
		for (String s : sourceArr){
			int firstIndex = s.indexOf("=");
			if (firstIndex > 0){
				String key = s.substring(0 , s.indexOf("="));
				String value = s.substring(s.indexOf("=") + 1);
				resultMap.put(key,value);
			}
		}
		return resultMap;
	}


	/**
	 *	根据传入的实体获得到toString 方法
	 * @param classObj
	 * @return
	 */
	public static String getObjectToStringCode(Class classObj){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("@Override\r");
		stringBuilder.append("\t\tpublic String toString() {").append("\r");
		stringBuilder.append("\t\t\tStringBuilder sb = new StringBuilder();").append("\r");
//		stringBuilder.append("\t\t\tsb.append(getClass().getSimpleName());").append("\r");
		stringBuilder.append("\t\t\tsb.append(\"").append(classObj.getSimpleName()).append("\");\r");
		stringBuilder.append("\t\t\tsb.append(\" [\");").append("\r");
		stringBuilder.append("\t\t\tsb.append(\"Hash = \").append(hashCode());").append("\r");

		Field[] fields = classObj.getDeclaredFields();
		for (Field f : fields){
			f.setAccessible(true);
			stringBuilder.append("\t\t\tsb.append(\", ").append(f.getName()).append("=\").append(").append(f.getName()).append(");").append("\r");
		}

		stringBuilder.append("\t\t\tsb.append(\"]\");").append("\r");
		stringBuilder.append("\t\t\treturn sb.toString();").append("\r");
		stringBuilder.append("\t\t}").append("\r");

		return stringBuilder.toString();
	}

	/**
	 * 生成Form表单字符串
	 * @param url
	 * @param method
	 * @param paramMap
	 * @return
	 */
	public static String generateFormString(String url , String method , Map<String, Object> paramMap ,String codeType){
		String html = "<form action=\"" + url + "\"" + " id=\"toPay\" method=\""+method+"\" accept-charset=\""+codeType+"\">";
		for (String key : paramMap.keySet()) {
			html += "<input name=\"" + key + "\" value=\"" + paramMap.get(key) + "\" type=\"hidden\" />";
		}
		html += "</form><script type=\"text/javascript\">document.getElementById(\"toPay\").submit();</script>";
		return html;
	}


	/**
	 * 编码类型转换
	 * @param sourceString	源字符串
	 * @param sourceCodeType	源编码类型
	 * @param targetCodeType	目标编码类型
	 * @return
	 */
	public static String codeTypeChange(String sourceString , String sourceCodeType , String targetCodeType){

		try {
			byte[] sourceStringBytes = sourceString.getBytes(sourceCodeType);

			return new String(sourceStringBytes, targetCodeType);
		} catch (UnsupportedEncodingException e) {
			LOG.info("原字符串{} 做编码转换{} ===> {} 异常" ,  sourceString ,  sourceCodeType , targetCodeType);
			return null;
		}
	}

}

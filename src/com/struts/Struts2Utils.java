package com.struts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.xtsoft.Constants;
import com.xtsoft.kernel.user.model.UserShort;

import flexjson.JSONSerializer;

/**
 * Struts2 Utils类.
 * 
 * 实现获取Request/Response/Session与绕过jsp/freemaker直接输出文本的简化函数.
 * 
 * @author calvin, huangyanqing
 */
public class Struts2Utils {

	// header 常量定义
	private static final String ENCODING_PREFIX = "encoding";
	private static final String NOCACHE_PREFIX = "no-cache";
	private static final String ENCODING_DEFAULT = "UTF-8";
	private static final boolean NOCACHE_DEFAULT = true;

	public static UserShort getCurrentUser() {
		return (UserShort) getRequest().getSession().getAttribute(Constants.CURRENTUSER);

	}

	// 取得Request/Response/Session的简化函数 //

	/**
	 * 取得HttpSession的简化方法.
	 */
	public static HttpSession getSession() {
		return ServletActionContext.getRequest().getSession();
	}

	/**
	 * 以Map对象返回一个Session
	 * 
	 * @return Map
	 */
	@SuppressWarnings("unchecked")
	public static Map getSessionByMap() {
		return ActionContext.getContext().getSession();
	}

	/**
	 * 取得HttpRequest的简化方法.
	 */
	public static HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	/**
	 * 取得HttpResponse的简化方法.
	 */
	public static HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}

	/**
	 * 取得Request Parameter的简化方法.
	 */
	public static String getParameter(String name) {
		if (name != null && name.length() > 0) {
			return getRequest().getParameter(name);
		}
		return null;
	}

	public static String getParameter2(String name) {
		String param = getRequest().getParameter(name);
		try {
			String param2 = null;
			if (param != null) {
				param2 = new String(param.getBytes("ISO8859-1"), "GBK");
				return param2;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	/*
	 * 把JSON对象转换成JAVA对象
	 */

	public static JSONObject getJSONObject() throws Exception {
		HttpServletRequest request = getRequest();

		BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream()));
		String line = null;
		StringBuilder sb = new StringBuilder();
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		JSONObject jsonObj = JSONObject.fromObject(sb.toString());
		return jsonObj;
	}

	// 绕过jsp/freemaker直接输出文本的函数 //

	/**
	 * 直接输出内容的简便函数.
	 * 
	 * eg. render("text/plain", "hello", "encoding:GBK"); render("text/plain",
	 * "hello", "no-cache:false"); render("text/plain", "hello", "encoding:GBK",
	 * "no-cache:false");
	 * 
	 * @param headers
	 *            可变的header数组，目前接受的值为"encoding:"或"no-cache:",默认值分别为UTF-8和true.
	 */
	public static void render(final String contentType, final String content, final String... headers) {
		try {
			// 分析headers参数
			String encoding = ENCODING_DEFAULT;
			boolean noCache = NOCACHE_DEFAULT;
			for (String header : headers) {
				String headerName = StringUtils.substringBefore(header, ":");
				String headerValue = StringUtils.substringAfter(header, ":");

				if (StringUtils.equalsIgnoreCase(headerName, ENCODING_PREFIX)) {
					encoding = headerValue;
				} else if (StringUtils.equalsIgnoreCase(headerName, NOCACHE_PREFIX)) {
					noCache = Boolean.parseBoolean(headerValue);
				} else
					throw new IllegalArgumentException(headerName + "不是一个合法的header类型");
			}

			HttpServletResponse response = ServletActionContext.getResponse();

			// 设置headers参数
			String fullContentType = contentType + ";charset=" + encoding;
			response.setContentType(fullContentType);
			if (noCache) {
				response.setHeader("Pragma", "No-cache");
				response.setHeader("Cache-Control", "no-cache");
				response.setDateHeader("Expires", 0);
			}

			response.getWriter().write(content);
			response.getWriter().flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 直接输出文本.
	 * 
	 * @see #render(String, String, String...)
	 */
	public static void renderText(final String text, final String... headers) {
		render("text/plain", text, headers);
	}

	/**
	 * 直接输出HTML.
	 * 
	 * @see #render(String, String, String...)
	 */
	public static void renderHtml(final String html, final String... headers) {
		render("text/html", html, headers);
	}

	/**
	 * 直接输出XML.
	 * 
	 * @see #render(String, String, String...)
	 */
	public static void renderXml(final String xml, final String... headers) {
		render("text/xml", xml, headers);
	}

	/**
	 * ==============================JSON格式的处理==================================
	 * ===
	 */

	/**
	 * 把Object中某几个属性转换成JSON格式的字符串，并输出到客户端
	 * 
	 * @param object
	 *            Java对象,将被转化为json字符串.
	 * @param property
	 *            部分属性
	 * @param headers
	 *            为空时"encoding:UTF-8"或"no-cache:true"
	 */
	public static void renderPartJson(final Object object, final String[] property, final String... headers) {
		String jsonString = new JSONSerializer().include(property).exclude("*").serialize(object);
		render("application/json", jsonString, headers);
	}

	/**
	 * 把Object类型转换成JSON格式的字符串，并输出到客户端
	 * 
	 * @param object
	 *            Java对象,将被转化为json字符串.
	 * @param headers
	 *            为空时"encoding:UTF-8"或"no-cache:true"
	 */
	public static void renderJson(final Object object, final String... headers) {
		String jsonString = new JSONSerializer().exclude("class").serialize(object);
		render("application/json", jsonString, headers);
	}

	/**
	 * 把Object类型深度转换成JSON格式的字符串，并输出到客户端(如：object含有List类型，并且把它也序列化)
	 * 
	 * @param object
	 *            Java对象,将被转化为json字符串.
	 * @param headers
	 *            为空时"encoding:UTF-8"或"no-cache:true"
	 */
	public static void renderDeepJson(final Object object, final String... headers) {
		String jsonString = new JSONSerializer().exclude("class").deepSerialize(object);
		render("application/json", jsonString, headers);
	}

}

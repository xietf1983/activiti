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
 * Struts2 Utils��.
 * 
 * ʵ�ֻ�ȡRequest/Response/Session���ƹ�jsp/freemakerֱ������ı��ļ򻯺���.
 * 
 * @author calvin, huangyanqing
 */
public class Struts2Utils {

	// header ��������
	private static final String ENCODING_PREFIX = "encoding";
	private static final String NOCACHE_PREFIX = "no-cache";
	private static final String ENCODING_DEFAULT = "UTF-8";
	private static final boolean NOCACHE_DEFAULT = true;

	public static UserShort getCurrentUser() {
		return (UserShort) getRequest().getSession().getAttribute(Constants.CURRENTUSER);

	}

	// ȡ��Request/Response/Session�ļ򻯺��� //

	/**
	 * ȡ��HttpSession�ļ򻯷���.
	 */
	public static HttpSession getSession() {
		return ServletActionContext.getRequest().getSession();
	}

	/**
	 * ��Map���󷵻�һ��Session
	 * 
	 * @return Map
	 */
	@SuppressWarnings("unchecked")
	public static Map getSessionByMap() {
		return ActionContext.getContext().getSession();
	}

	/**
	 * ȡ��HttpRequest�ļ򻯷���.
	 */
	public static HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	/**
	 * ȡ��HttpResponse�ļ򻯷���.
	 */
	public static HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}

	/**
	 * ȡ��Request Parameter�ļ򻯷���.
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
	 * ��JSON����ת����JAVA����
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

	// �ƹ�jsp/freemakerֱ������ı��ĺ��� //

	/**
	 * ֱ��������ݵļ�㺯��.
	 * 
	 * eg. render("text/plain", "hello", "encoding:GBK"); render("text/plain",
	 * "hello", "no-cache:false"); render("text/plain", "hello", "encoding:GBK",
	 * "no-cache:false");
	 * 
	 * @param headers
	 *            �ɱ��header���飬Ŀǰ���ܵ�ֵΪ"encoding:"��"no-cache:",Ĭ��ֵ�ֱ�ΪUTF-8��true.
	 */
	public static void render(final String contentType, final String content, final String... headers) {
		try {
			// ����headers����
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
					throw new IllegalArgumentException(headerName + "����һ���Ϸ���header����");
			}

			HttpServletResponse response = ServletActionContext.getResponse();

			// ����headers����
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
	 * ֱ������ı�.
	 * 
	 * @see #render(String, String, String...)
	 */
	public static void renderText(final String text, final String... headers) {
		render("text/plain", text, headers);
	}

	/**
	 * ֱ�����HTML.
	 * 
	 * @see #render(String, String, String...)
	 */
	public static void renderHtml(final String html, final String... headers) {
		render("text/html", html, headers);
	}

	/**
	 * ֱ�����XML.
	 * 
	 * @see #render(String, String, String...)
	 */
	public static void renderXml(final String xml, final String... headers) {
		render("text/xml", xml, headers);
	}

	/**
	 * ==============================JSON��ʽ�Ĵ���==================================
	 * ===
	 */

	/**
	 * ��Object��ĳ��������ת����JSON��ʽ���ַ�������������ͻ���
	 * 
	 * @param object
	 *            Java����,����ת��Ϊjson�ַ���.
	 * @param property
	 *            ��������
	 * @param headers
	 *            Ϊ��ʱ"encoding:UTF-8"��"no-cache:true"
	 */
	public static void renderPartJson(final Object object, final String[] property, final String... headers) {
		String jsonString = new JSONSerializer().include(property).exclude("*").serialize(object);
		render("application/json", jsonString, headers);
	}

	/**
	 * ��Object����ת����JSON��ʽ���ַ�������������ͻ���
	 * 
	 * @param object
	 *            Java����,����ת��Ϊjson�ַ���.
	 * @param headers
	 *            Ϊ��ʱ"encoding:UTF-8"��"no-cache:true"
	 */
	public static void renderJson(final Object object, final String... headers) {
		String jsonString = new JSONSerializer().exclude("class").serialize(object);
		render("application/json", jsonString, headers);
	}

	/**
	 * ��Object�������ת����JSON��ʽ���ַ�������������ͻ���(�磺object����List���ͣ����Ұ���Ҳ���л�)
	 * 
	 * @param object
	 *            Java����,����ת��Ϊjson�ַ���.
	 * @param headers
	 *            Ϊ��ʱ"encoding:UTF-8"��"no-cache:true"
	 */
	public static void renderDeepJson(final Object object, final String... headers) {
		String jsonString = new JSONSerializer().exclude("class").deepSerialize(object);
		render("application/json", jsonString, headers);
	}

}

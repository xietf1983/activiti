package com.xtsoft.kernel.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.xtsoft.kernel.security.ActServiceUtil;
import com.xtsoft.kernel.user.model.UserShort;
import com.xtsoft.util.StringUtil;

public class AclFilter implements Filter {
	private FilterConfig filterConfig;

	public void init(FilterConfig filterConfig) {
		this.filterConfig = filterConfig;
	}

	public void destroy() {

	}

	public final void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filter) throws IOException, ServletException {
		HttpServletRequest httpRequst = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String head = httpRequst.getHeader("x-requested-with");
		String remoteAddr = httpRequst.getRequestURI();
		String[] urls = StringUtils.split(remoteAddr, "/");
		String url = urls[(urls.length - 1)];
		url = StringUtil.substringBefore(url, ".");
		String urlreques ="http://"+httpRequst.getServerName()+":"+httpRequst.getServerPort()  +httpRequst.getContextPath()+"/login/index.html";

		try {
			if (remoteAddr.indexOf("index.html") > -1) {

			} else if (remoteAddr.indexOf("checkUserAccount.do") == -1) {
				if (head != null && (head.equalsIgnoreCase("XMLHttpRequest"))) {
					UserShort sessionUser = (UserShort) httpRequst.getSession().getAttribute(com.xtsoft.Constants.CURRENTUSER);
					if (sessionUser == null) {
						response.getWriter().write("{\"sessiontimeout\":\"yes\",\"redirectUri\":\""+urlreques+"\"}");
						response.getWriter().flush();
						// httpResponse.sendRedirect("../login/index.html");
						// filterConfig.getServletContext().getRequestDispatcher("/login/index.html").forward(request,
						// response);
						// httpRequst.getRequestDispatcher("/login/index.html").forward(request,
						// response);
						// httpResponse.sendRedirect("../login/index.html");
						// filter.doFilter(request, response);
						return;
					} else {
						if (ActServiceUtil.getService().hasRight(url, sessionUser.getUserId())) {
							filter.doFilter(request, response);
						} else {
							response.getWriter().write("{\"sessiontimeout\":\"yes\",\"redirectUri\":\""+urlreques+"\"}");
							response.getWriter().write("{\"msg\":\"\"}");
							// response.getWriter().flush();
							// filterConfig.getServletContext().getRequestDispatcher("").forward(request,
							// response);
							return;
						}
					}
				} else {
					// URL ×ÊÔ´
					UserShort sessionUser = (UserShort) httpRequst.getSession().getAttribute(com.xtsoft.Constants.CURRENTUSER);
					if (sessionUser == null) {
						httpRequst.getRequestDispatcher("/login/index.html").forward(request, response);
						// httpResponse.sendRedirect("../login/index.html");
						return;
					} else {

					}
				}
			} else {
				//
			}
			filter.doFilter(request, response);
		} catch (Exception e) {
			int a = 0;
		}
	}
}

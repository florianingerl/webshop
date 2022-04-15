package com.florianingerl.nachhilfe.webshop.filter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;



import com.florianingerl.nachhilfe.webshop.beans.UserAccount;
import com.florianingerl.nachhilfe.webshop.utils.DBUtils;
import com.florianingerl.nachhilfe.webshop.utils.MyUtils;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@WebFilter(filterName = "cookieFilter", urlPatterns = { "/*" })
public class CookieFilter implements Filter {

	public CookieFilter() {
	}

	@Override
	public void init(FilterConfig fConfig) throws ServletException {

	}

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("Cookie Filter");
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();

		UserAccount userInSession = MyUtils.getLoginedUser(session);
		// 
		if (userInSession != null) {
			session.setAttribute("COOKIE_CHECKED", "CHECKED");
			chain.doFilter(request, response);
			return;
		}

		// La Connecxion a été créée dans JDBCFilter.
		Connection conn = MyUtils.getStoredConnection(request);

		// Le drapeau (flag) sert à vérifier Cookie.
		String checked = (String) session.getAttribute("COOKIE_CHECKED");
		if (checked == null && conn != null) {
			String userName = MyUtils.getUserNameInCookie(req);
			try {
				UserAccount user = DBUtils.findUser(conn, userName);
				MyUtils.storeLoginedUser(session, user);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			// Marquez qu'il a vérifié Cookie.
			session.setAttribute("COOKIE_CHECKED", "CHECKED");
		}

		chain.doFilter(request, response);
	}

}

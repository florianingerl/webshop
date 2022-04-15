package com.florianingerl.nachhilfe.webshop.utils;

import java.sql.Connection;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.florianingerl.nachhilfe.webshop.beans.UserAccount;

public class MyUtils {

	public static final String ATT_NAME_CONNECTION = "ATTRIBUTE_FOR_CONNECTION";

	private static final String ATT_NAME_USER_NAME = "ATTRIBUTE_FOR_STORE_USER_NAME_IN_COOKIE";

	// Sockez Connection dans l'attribut de la demande.
	// Les informations stockées n'existent que pendant les demandes (request)
	// jusqu'à quand des données sont envoyées au navigateur de l'utilisateur.
	public static void storeConnection(ServletRequest request, Connection conn) {
		request.setAttribute(ATT_NAME_CONNECTION, conn);
	}

	// L'objet de connexion a été enregistré dans l'attribut de la demande.
	public static Connection getStoredConnection(ServletRequest request) {
		Connection conn = (Connection) request.getAttribute(ATT_NAME_CONNECTION);
		return conn;
	}

	// Conservez les informations de l'utilisateur en Session.
	public static void storeLoginedUser(HttpSession session, UserAccount loginedUser) {
		// Sur JSP l'utilisateur peut accéder via ${loginedUser}
		session.setAttribute("loginedUser", loginedUser);
	}

	// Obtenez les informations de l'utilisateur stockées dans la Session.
	public static UserAccount getLoginedUser(HttpSession session) {
		UserAccount loginedUser = (UserAccount) session.getAttribute("loginedUser");
		return loginedUser;
	}

	// Stockez les informations de l'utilisateur dans Cookie.
	public static void storeUserCookie(HttpServletResponse response, UserAccount user) {
		System.out.println("Store user cookie");
		Cookie cookieUserName = new Cookie(ATT_NAME_USER_NAME, user.getUserName());
		// 1 jour (converti en secondes)
		cookieUserName.setMaxAge(24 * 60 * 60);
		response.addCookie(cookieUserName);
	}

	public static String getUserNameInCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (ATT_NAME_USER_NAME.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	// Supprimez les cookies de l'utilisateur.
	public static void deleteUserCookie(HttpServletResponse response) {
		Cookie cookieUserName = new Cookie(ATT_NAME_USER_NAME, null);
		// 0 seconde. (ce cookie expirera immédiatement)
		cookieUserName.setMaxAge(0);
		response.addCookie(cookieUserName);
	}

}

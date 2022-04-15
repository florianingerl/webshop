package com.florianingerl.nachhilfe.webshop.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.florianingerl.nachhilfe.webshop.beans.UserAccount;
import com.florianingerl.nachhilfe.webshop.utils.DBUtils;
import com.florianingerl.nachhilfe.webshop.utils.MyUtils;

@WebServlet(urlPatterns = { "/login" })
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 5L;

	public LoginServlet() {
		super();
	}

	// Affichez la page de connexion.
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Transmettez vers la page /WEB-INF/views/loginView.jsp
		// (L'utilisateur ne peut pas accéder directement
		// à la page JSP qui se trouve dans le dossier WEB-INF).
		RequestDispatcher dispatcher //
				= this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");

		dispatcher.forward(request, response);

	}

	// Lorsque l'utilisateur saisit userName & password, et presse le bouton Submit.
	// Cette méthode sera exécutée.
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		String rememberMeStr = request.getParameter("rememberMe");
		boolean remember = "Y".equals(rememberMeStr);

		UserAccount user = null;
		boolean hasError = false;
		String errorString = null;

		if (userName == null || password == null || userName.length() == 0 || password.length() == 0) {
			hasError = true;
			errorString = "Required username and password!";
		} else {
			Connection conn = MyUtils.getStoredConnection(request);
			try {
				// Cherchez user dans DB.
				user = DBUtils.findUser(conn, userName, password);

				if (user == null) {
					hasError = true;
					errorString = "User Name or password invalid";
				}
			} catch (SQLException e) {
				e.printStackTrace();
				hasError = true;
				errorString = e.getMessage();
			}
		}
		// Au cas où il y a des erreurs,
		// forward (transmettre) vers /WEB-INF/views/login.jsp
		if (hasError) {
			user = new UserAccount();
			user.setUserName(userName);
			user.setPassword(password);

			// Enregistrez des données dans l'attribut de la demande avant de les transmettre.
			request.setAttribute("errorString", errorString);
			request.setAttribute("user", user);

			// Forward (Transmettre) vers la page /WEB-INF/views/login.jsp
			RequestDispatcher dispatcher //
					= this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");

			dispatcher.forward(request, response);
		}
		// S'il n'y a pas de l'erreur.
		// Enregistrez les informations de l'utilisateur dans Session.
		// Et transmettez vers la page userInfo.
		else {
			HttpSession session = request.getSession();
			MyUtils.storeLoginedUser(session, user);

			// Si l'utilisateur sélectionne la fonction "Remember me".
			if (remember) {
				MyUtils.storeUserCookie(response, user);
			}
			// Si non, supprimez Cookie
			else {
				MyUtils.deleteUserCookie(response);
			}

			// Redirect (Réorienter) vers la page /userInfo.
			response.sendRedirect(request.getContextPath() + "/userInfo");
		}
	}

}

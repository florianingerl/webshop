package com.florianingerl.nachhilfe.webshop.servlet;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.florianingerl.nachhilfe.webshop.beans.UserAccount;
import com.florianingerl.nachhilfe.webshop.utils.MyUtils;


@WebServlet(urlPatterns = { "/userInfo" })
public class UserInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 7L;

	public UserInfoServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();

		// Vérifiez si l'utilisateur s'est connecté (login) ou pas.
		UserAccount loginedUser = MyUtils.getLoginedUser(session);

		// Pas connecté (login).
		if (loginedUser == null) {
			// Redirect (Réorenter) vers la page de connexion.
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}
		// Enregistrez des informations à l'attribut de la demande avant de forward (transmettre).
		request.setAttribute("user", loginedUser);

		// Si l'utilisateur s'est connecté, forward (transmettre) vers la page
		// /WEB-INF/views/userInfoView.jsp
		RequestDispatcher dispatcher //
				= this.getServletContext().getRequestDispatcher("/WEB-INF/views/userInfoView.jsp");
		dispatcher.forward(request, response);

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}

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

import com.florianingerl.nachhilfe.webshop.utils.DBUtils;
import com.florianingerl.nachhilfe.webshop.utils.MyUtils;

@WebServlet(urlPatterns = { "/deleteProduct" })
public class DeleteProductServlet extends HttpServlet {
	private static final long serialVersionUID = 2L;

	public DeleteProductServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Connection conn = MyUtils.getStoredConnection(request);

		String code = (String) request.getParameter("code");

		String errorString = null;

		try {
			DBUtils.deleteProduct(conn, code);
		} catch (SQLException e) {
			e.printStackTrace();
			errorString = e.getMessage();
		} 
		
		// S'il y a une erreur, forward (transmettez) vers la page d'erreur\.
		if (errorString != null) {
			// Enregistrez des informations dans l'attribut de la demande avant de transmettre aux vues.
			request.setAttribute("errorString", errorString);
			// 
			RequestDispatcher dispatcher = request.getServletContext()
					.getRequestDispatcher("/WEB-INF/views/deleteProductErrorView.jsp");
			dispatcher.forward(request, response);
		}
		// S'il n'y a aucun problème.
		// Redirect (réorientez) vers la page de la liste des produits.
		else {
			response.sendRedirect(request.getContextPath() + "/productList");
		}

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}

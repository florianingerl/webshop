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


import com.florianingerl.nachhilfe.webshop.beans.Product;
import com.florianingerl.nachhilfe.webshop.utils.DBUtils;
import com.florianingerl.nachhilfe.webshop.utils.MyUtils;


@WebServlet(urlPatterns = { "/editProduct" })
public class EditProductServlet extends HttpServlet {
	private static final long serialVersionUID = 3L;

	public EditProductServlet() {
		super();
	}

	// Affichez la page d'édition du produit.
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Connection conn = MyUtils.getStoredConnection(request);

		String code = (String) request.getParameter("code");

		Product product = null;

		String errorString = null;

		try {
			product = DBUtils.findProduct(conn, code);
		} catch (SQLException e) {
			e.printStackTrace();
			errorString = e.getMessage();
		}

		// Aucune erreur.
		// Le produit n'existe pas à éditer.
		// Réorientez vers la page la liste des produits.
		if (errorString != null && product == null) {
			response.sendRedirect(request.getServletPath() + "/productList");
			return;
		}

		// Enregistrez des informations à l'attribut de la demande avant de transmettre aux vues.
		request.setAttribute("errorString", errorString);
		request.setAttribute("product", product);

		RequestDispatcher dispatcher = request.getServletContext()
				.getRequestDispatcher("/WEB-INF/views/editProductView.jsp");
		dispatcher.forward(request, response);

	}

	// Une fois que l'utilisateur modifie les informations sur le produit, cliquez sur Submit.
	// Cette méthode sera exécutée.
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Connection conn = MyUtils.getStoredConnection(request);

		String code = (String) request.getParameter("code");
		String name = (String) request.getParameter("name");
		String priceStr = (String) request.getParameter("price");
		float price = 0;
		try {
			price = Float.parseFloat(priceStr);
		} catch (Exception e) {
		}
		Product product = new Product(code, name, price);

		String errorString = null;

		try {
			DBUtils.updateProduct(conn, product);
		} catch (SQLException e) {
			e.printStackTrace();
			errorString = e.getMessage();
		}
		// Enregistrez des informations à l'attribut de la demande avant de transmettre aux vues.
		request.setAttribute("errorString", errorString);
		request.setAttribute("product", product);

		// S'il n'y a pas d'erreur, transmettez à la page d'édition.
		if (errorString != null) {
			RequestDispatcher dispatcher = request.getServletContext()
					.getRequestDispatcher("/WEB-INF/views/editProductView.jsp");
			dispatcher.forward(request, response);
		}
		// S'il n'y a aucun problème.
		// Réorientez vers la page de la liste des produits.
		else {
			response.sendRedirect(request.getContextPath() + "/productList");
		}
	}

}

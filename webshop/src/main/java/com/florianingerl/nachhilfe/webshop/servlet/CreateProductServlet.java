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


@WebServlet(urlPatterns = { "/createProduct" })
public class CreateProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public CreateProductServlet() {
		super();
	}

	// Affichez la page de création du produit.
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		RequestDispatcher dispatcher = request.getServletContext()
				.getRequestDispatcher("/WEB-INF/views/createProductView.jsp");
		dispatcher.forward(request, response);
	}

	// Lorsque l'utilisateur saisit des informations sur le produit, cliquez sur Submit.
	// Cette méthode sera appelée.
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

		// ID du produit est la chaîne littérale [a-zA-Z_0-9]
		// Avec au moins une lettre.
		String regex = "\\w+";

		if (code == null || !code.matches(regex)) {
			errorString = "Product Code invalid!";
		}

		if (errorString == null) {
			try {
				DBUtils.insertProduct(conn, product);
			} catch (SQLException e) {
				e.printStackTrace();
				errorString = e.getMessage();
			}
		}

		// Enregistrez des informations dans l'attribut de la demande, avant de passer aux vues.
		request.setAttribute("errorString", errorString);
		request.setAttribute("product", product);

		// S'il y a des erreurs, forward (transmettez) vers la page 'edit'.
		if (errorString != null) {
			RequestDispatcher dispatcher = request.getServletContext()
					.getRequestDispatcher("/WEB-INF/views/createProductView.jsp");
			dispatcher.forward(request, response);
		}
		// S'il n'y a aucun problème.
		// Redirect (Réorientez) vers la page de la liste des produits.
		else {
			response.sendRedirect(request.getContextPath() + "/productList");
		}
	}

}
package com.florianingerl.nachhilfe.webshop.filter;

import java.io.IOException;
import java.sql.Connection;
import java.util.Collection;
import java.util.Map;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import com.florianingerl.nachhilfe.webshop.conn.ConnectionUtils;
import com.florianingerl.nachhilfe.webshop.utils.MyUtils;

@WebFilter(filterName = "jdbcFilter", urlPatterns = { "/*" })
public class JDBCFilter implements Filter {

	public JDBCFilter() {
	}

	@Override
	public void init(FilterConfig fConfig) throws ServletException {

	}

	@Override
	public void destroy() {

	}

	// Vérifiez que la cible de la requête est une servlet??
	private boolean needJDBC(HttpServletRequest request) {
		System.out.println("JDBC Filter");
		// 
		// Servlet Url-pattern: /spath/*
		// 
		// => /spath
		String servletPath = request.getServletPath();
		// => /abc/mnp
		String pathInfo = request.getPathInfo();

		String urlPattern = servletPath;

		if (pathInfo != null) {
			// => /spath/*
			urlPattern = servletPath + "/*";
		}

		// Key: servletName.
		// Value: ServletRegistration
		Map<String, ? extends ServletRegistration> servletRegistrations = request.getServletContext()
				.getServletRegistrations();

		// La collection de tous les servlets dans votre Webapp.
		Collection<? extends ServletRegistration> values = servletRegistrations.values();
		for (ServletRegistration sr : values) {
			Collection<String> mappings = sr.getMappings();
			if (mappings.contains(urlPattern)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;

		// N'ouvrez que connection (la connexion) pour des demandes ayant des chemins spéciaux.
		// (Par exemple: Des chemins vont vers servlet, jsp, ..)
		// Évitez d'ouvrir Connection pour des demandes demandes normales.
		// (Par exemple: image, css, javascript,... )
		if (this.needJDBC(req)) {

			System.out.println("Open Connection for: " + req.getServletPath());

			Connection conn = null;
			try {
				// Créez des objets Connection se connecte à la base de données.
				conn = ConnectionUtils.getConnection();
				// Définissez automatiquement commit false.
				conn.setAutoCommit(false);

				// Enregistrez l'objet Connection dans l'attribut de la demande.
				MyUtils.storeConnection(request, conn);

				// Autorisez la demande d'aller en avant.
				// (Allez au filtre suivant ou à la cible).
				chain.doFilter(request, response);

				// Appelez la méthode commit() pour finir la transaction avec DB.
				conn.commit();
			} catch (Exception e) {
				e.printStackTrace();
				ConnectionUtils.rollbackQuietly(conn);
				throw new ServletException();
			} finally {
				ConnectionUtils.closeQuietly(conn);
			}
		}
		// Pour des demandes communes (image,css,html,..)
		// ce n'est pas obligatoire d'ouvrir connection.
		else {
			// Permettez la demande d'aller en avant.
			// (Allez au filtre suivant ou à la cible).
			chain.doFilter(request, response);
		}

	}

}

package com.florianingerl.nachhilfe.webshop.filter;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@WebFilter(filterName = "encodingFilter", urlPatterns = { "/*" })
public class EncodingFilter implements Filter {

  public EncodingFilter() {
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
	  System.out.println("Encoding filter");
      request.setCharacterEncoding("UTF-8");

      chain.doFilter(request, response);
  }

}
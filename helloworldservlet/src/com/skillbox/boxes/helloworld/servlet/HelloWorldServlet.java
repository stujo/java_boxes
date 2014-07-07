package com.skillbox.boxes.helloworld.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class HelloWorldServlet
 */
public class HelloWorldServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  /**
   * @see HttpServlet#HttpServlet()
   */
  public HelloWorldServlet() {
    super();
    // TODO Auto-generated constructor stub
  }

  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
   *      response)
   */
  @Override
  protected void doGet(final HttpServletRequest request,
      final HttpServletResponse response) throws ServletException, IOException {
    // Set the response message's MIME type
    response.setContentType("text/html;charset=UTF-8");
    // Allocate a output writer to write the response message into the network
    // socket
    final PrintWriter out = response.getWriter();

    final String pref_greet = getServletContext()
        .getInitParameter("pref_greet");

    final String greeter_name = getServletConfig().getInitParameter(
        "greeter_name");

    // Write the response message, in an HTML page
    try {
      out.println("<!DOCTYPE html>");
      out.println("<html><head>");
      out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
      out.println("<title>Hello, World</title></head>");
      out.println("<body>");
      out.println("<h1>Hello, world! Servlet</h1>"); // says Hello
      out.println("<p>" + pref_greet + " from " + greeter_name + "</p>");

      // Echo client's request information
      out.println("<p>Request URI: " + request.getRequestURI() + "</p>");
      out.println("<p>Protocol: " + request.getProtocol() + "</p>");
      out.println("<p>PathInfo: " + request.getPathInfo() + "</p>");
      out.println("<p>Remote Address: " + request.getRemoteAddr() + "</p>");
      // Generate a random number upon each request
      out.println("<p>A Random Number: <strong>" + Math.random()
          + "</strong></p>");
      out.println("</body>");
      out.println("</html>");
    } finally {
      out.close(); // Always close the output writer
    }
  }

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
   *      response)
   */
  @Override
  protected void doPost(final HttpServletRequest request,
      final HttpServletResponse response) throws ServletException, IOException {
    // TODO Auto-generated method stub
  }

}

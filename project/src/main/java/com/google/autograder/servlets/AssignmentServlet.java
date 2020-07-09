package com.google.autograder.servlets;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import com.google.autograder.data.Database;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

/** Servlet that encapsulates the subtraction game. */
@WebServlet("/assignment")
public final class AssignmentServlet extends HttpServlet {

  private Database d = new Database();

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get the input from form
    String name = request.getParameter("name");
    int totalPoints = Integer.parseInt(request.getParameter("total-points"));
    System.out.println(name);

    d.addAssignment(name, totalPoints);

    // Redirect back to the HTML page.
    response.sendRedirect("/pages/assignments.html");
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("Assignment").addSort("name", SortDirection.DESCENDING);
    PreparedQuery results = d.queryDatabase(query);

    List<String> assignments = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
      String name = (String) entity.getProperty("name");
    }

    response.getWriter().println(assignments);
  }
}
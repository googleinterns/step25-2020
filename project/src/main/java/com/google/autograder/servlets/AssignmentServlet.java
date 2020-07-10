package com.google.autograder.servlets;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import com.google.autograder.data.Database;
import com.google.autograder.data.Assignment;
import com.google.gson.Gson;
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
    String totalPointsString = request.getParameter("total-points");
    int totalPoints = Integer.parseInt(totalPointsString);
    System.out.println(name);

    d.addAssignment(name, totalPoints);

    // Redirect back to the HTML page.
    response.sendRedirect("/pages/assignments.html");
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("Assignment");
    PreparedQuery results = d.queryDatabase(query);

    List<Assignment> assignments = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
      String name = (String) entity.getProperty("name");
      String pointsString = (String) entity.getProperty("points");
      int points = Integer.parseInt(pointsString);
      String status = (String) entity.getProperty("status");
      Assignment currAssignment = new Assignment(name, points, status);
      assignments.add(currAssignment);
    }
    response.setContentType("application/json;");
    String json = new Gson().toJson(assignments);
    System.out.println(json);
    response.getWriter().println(json);
  }
}
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

// @WebServlet("/assignment")
public final class AssignmentServlet extends HttpServlet {

  private Database d = new Database();

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get the input from form
    String name = request.getParameter("name");
    String totalPointsString = request.getParameter("total-points");
    if (name != null && totalPointsString != null && totalPointsString != "") {
        int totalPoints = Integer.parseInt(totalPointsString);
        d.addAssignment(name, totalPoints);
        response.sendRedirect("/pages/assignments.html");
    }
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String json = d.getAllAssignmentsJSON();
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }
}
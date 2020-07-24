package com.google.autograder.servlets;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import com.google.autograder.data.Database;
import com.google.autograder.data.Answer;
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

@WebServlet("/group")
public final class GroupServlet extends HttpServlet {

  private Database d = new Database();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // get all the answer entities based on answer + question keys
    String assignmentKey = request.getParameter("assignment-key");
    String questionKey = request.getParameter("question-key");

    String json = d.createGroups(assignmentKey, questionKey);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }
  
}
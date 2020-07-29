package com.google.autograder.servlets;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import com.google.autograder.data.Database;
import com.google.autograder.data.Question;
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

public final class QuestionServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String assignmentKey = request.getParameter("assignment-key");
    String json = Database.getAllQuestionsJSON(assignmentKey);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }
}
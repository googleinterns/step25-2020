package com.google.autograder.servlets;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import com.google.autograder.data.Database;
import com.google.autograder.data.Detect;
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
import java.io.File;

public final class GradeServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String score = request.getParameter("score");
    String comments = request.getParameter("comments");
    String groupKey = request.getParameter("groupKey");
    Database.gradeGroup(groupKey, score, comments);
  }
}
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

public final class AnswerServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // get all the answer entities based on answer + question keys
    String assignmentKey = request.getParameter("assignment-key");
    String questionKey = request.getParameter("question-key");

    //create hard-coded answer data right now
    //Database.addAnswer(filePath, parsedAnswer, score, assignmentKey, questionKey);
    Database.addAnswer("images/eight1.jpeg", parseAnswer("images/eight1.jpeg"), 0, assignmentKey, questionKey);
    Database.addAnswer("images/eight2.jpeg", parseAnswer("images/eight2.jpeg"), 0, assignmentKey, questionKey);
    Database.addAnswer("images/eight3.jpeg", parseAnswer("images/eight3.jpeg"), 0, assignmentKey, questionKey);
    Database.addAnswer("images/three1.jpeg", parseAnswer("images/three1.jpeg"), 0, assignmentKey, questionKey);
    Database.addAnswer("images/three2.jpeg", parseAnswer("images/three2.jpeg"), 0, assignmentKey, questionKey);
    Database.addAnswer("images/three3.jpeg", parseAnswer("images/three3.jpeg"), 0, assignmentKey, questionKey);



    String json = Database.getAllAnswersJSON(assignmentKey, questionKey);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  public String parseAnswer(String filePath) {
      if (filePath.startsWith("images/e")) {
          return "8";
      }
      else {
          return "3";
      }
  }
}
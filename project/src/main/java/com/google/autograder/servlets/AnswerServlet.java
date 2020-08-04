package com.google.autograder.servlets;

import java.io.File;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.IOException;
import com.google.gson.Gson;
import javax.servlet.http.HttpServlet;
import com.google.autograder.data.Detect;
import com.google.autograder.data.Answer;
import com.google.autograder.data.Database;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query.SortDirection;

public final class AnswerServlet extends HttpServlet {
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // get all the answer entities based on answer + question keys
        String assignmentKey = request.getParameter("assignment-key");
        String questionKey = request.getParameter("question-key");

        String json = Database.getAllAnswersJSON(assignmentKey, questionKey);
        
        System.out.println(json);
        
        response.setContentType("application/json;");
        response.getWriter().println(json);
    }
    
}
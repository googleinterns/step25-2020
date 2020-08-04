package com.google.autograder.servlets;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import com.google.autograder.data.Database;
import com.google.autograder.data.Question;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class QuestionServlet extends HttpServlet {
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String assignmentKey = request.getParameter("assignment-key");
        String json = Database.getAllQuestionsJSON(assignmentKey);
        response.setContentType("application/json;");
        response.getWriter().println(json);
    }
    
}
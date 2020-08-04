package com.google.autograder.servlets;

import java.util.List;
import java.util.Arrays;
import java.io.IOException;
import java.util.ArrayList;
import com.google.gson.Gson;
import javax.servlet.http.HttpServlet;
import com.google.autograder.data.Answer;
import com.google.autograder.data.Database;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class GroupServlet extends HttpServlet {
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // get all the answer entities based on answer + question keys
        System.out.println("Creating groups...");
        
        String assignmentKey = request.getParameter("assignment-key");
        String questionKey = request.getParameter("question-key");
        
        String json = Database.createGroups(assignmentKey, questionKey);
        
        System.out.println("Done!");
        System.out.println(json);
        
        response.setContentType("application/json;");
        response.getWriter().println(json);
    }

}
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

public final class AddAnswersServlet extends HttpServlet {
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // add answer entities (hardcoded rn)
        System.out.println("Adding answers...");
        
        String assignmentKey = request.getParameter("assignment-key");
        String questionKey = request.getParameter("question-key");
        
        File folder = new File("images/answers");
        File[] listOfFiles = folder.listFiles();
        
        for (File file : listOfFiles) {
            String path = "images/answers/" + file.getName();
            Database.addAnswer(path, parseAnswer(path), 0, assignmentKey, questionKey);
        }
        
        System.out.println("Done!");
        
        response.setContentType("application/json;");
        response.getWriter().println("");
    }
    
    public String parseAnswer(String filePath) throws IOException {
        Detect d = new Detect();
        return d.detectDocumentText(filePath);
    }
    
}
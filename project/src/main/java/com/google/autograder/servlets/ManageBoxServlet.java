package com.google.autograder.servlets;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import com.google.autograder.data.Database;
import com.google.autograder.data.Question;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.Entity;

public class ManageBoxServlet extends HttpServlet {
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {     
        response.setContentType("text/html");
        response.getWriter().println("hello world");
    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int leftXCoord = Integer.parseInt(request.getParameter("leftXCoord"));
        int topYCoord = Integer.parseInt(request.getParameter("topYCoord"));
        int rightXCoord = Integer.parseInt(request.getParameter("rightXCoord"));
        int lowerYCoord = Integer.parseInt(request.getParameter("lowerYCoord"));
        
        String questionName = request.getParameter("qName");
        String questionType = request.getParameter("qType");
        int questionPoints = Integer.parseInt(request.getParameter("qPoints"));
        String assignmentKey = request.getParameter("assignment-key");
        
        Entity questionEntity = Database.addQuestion(questionName, questionType, questionPoints, assignmentKey);
        Database.addLocation(questionEntity, leftXCoord, topYCoord, rightXCoord, lowerYCoord);
        response.setStatus(200);
    }

}
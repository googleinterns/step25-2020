package com.google.autograder.servlets;

import java.net.URL;
import java.util.Iterator;
import java.io.IOException;
import org.json.simple.JSONObject;
import javax.servlet.http.HttpServlet;
import javax.servlet.annotation.WebServlet;
import com.google.autograder.data.Database;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

public final class GetAssignmentDetailsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String courseID = request.getParameter("courseID");
        String assignmentID = request.getParameter("assignmentID");
        Filter assignmentIDFilter = new FilterPredicate("id", FilterOperator.EQUAL, assignmentID);
        Filter courseIDFilter = new FilterPredicate("courseID", FilterOperator.EQUAL, courseID);

        Query query = new Query("Assignment").setFilter(courseIDFilter).setFilter(assignmentIDFilter);
        Iterator<Entity> results = Database.query(query).iterator();
        Entity assignment = results.next();

        // TODO: What happens if there are no results in the Datastore?

        String title = (String) assignment.getProperty("title");
        String description = (String) assignment.getProperty("description");

        JSONObject assignmentDetailsObject = new JSONObject();
        JSONObject assignmentObject = new JSONObject();
        
        assignmentObject.put("title", title);
        assignmentObject.put("description", description);
        assignmentDetailsObject.put("assignment", assignmentObject);

        response.setContentType("application/json");
        response.getWriter().println(assignmentDetailsObject.toString());
    }

}
package com.google.autograder.servlets;

import java.net.URL;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Entity;
import com.google.autograder.servlets.helpers.Services;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

@WebServlet("/getCourseDetails")
public final class GetCourseDetailsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userEmail = Services.USER_SERVICE.getCurrentUser().getEmail();
        String userID = Services.USER_SERVICE.getCurrentUser().getUserId();

        String requestURL = (String) request.getHeader("Referer");
        String courseID = requestURL.substring (requestURL.indexOf("?courseID=") + 10);

        Filter userEmailFilter = new FilterPredicate("userEmail", FilterOperator.EQUAL, userEmail);
        Filter courseIDFilter = new FilterPredicate("id", FilterOperator.EQUAL, courseID);

        Query query = new Query("Course").setFilter(userEmailFilter).setFilter(courseIDFilter);
        PreparedQuery results = Services.DATA_STORE.prepare(query);
        Entity course = results.asIterable().iterator().next();
        
        String name = (String) course.getProperty("name");
        String description = (String) course.getProperty("description");
        String courseDetails = "{ \"course\" : { \"name\": \"" + name + "\" , \"description\": \"" + description + "\" }}";

        response.setContentType("application/json");
        response.getWriter().println(courseDetails);
    }

}
package com.google.autograder.servlets;

import java.net.URL;
import java.util.Iterator;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.simple.JSONArray;
import java.net.HttpURLConnection;
import org.json.simple.JSONObject;
import javax.servlet.http.HttpServlet;
import org.json.simple.parser.JSONParser;
import java.nio.charset.StandardCharsets;
import javax.servlet.annotation.WebServlet;
import java.io.UnsupportedEncodingException;
import org.json.simple.parser.ParseException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Entity;
import com.google.autograder.servlets.helpers.API;
import com.google.autograder.servlets.helpers.Services;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.autograder.servlets.auth.AccessTokenResponse;
import com.google.appengine.api.datastore.Query.FilterPredicate;

@WebServlet("/listCourses")
public final class ListCoursesServlet extends HttpServlet {

    private static String END_POINT = "https://classroom.googleapis.com/v1/courses";
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (Services.USER_SERVICE.isUserLoggedIn()) {
            response.setContentType("application/json");

            String authorization = API.getCurrentUserAPIAuthorization();

            if (authorization != null) {
                HttpURLConnection connection = (HttpURLConnection) new URL(END_POINT + "?key=" + API.API_KEY).openConnection();

                connection.setRequestMethod("GET");
                connection.setRequestProperty("courseStates", "ACTIVE");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Authorization", authorization);

                String json = API.getJSON(connection);

                storeCourseDataInDatastore(json);

                String coursesData = getCoursesDataFromDatastore();

                response.getWriter().println(coursesData);
            } else {
                response.setHeader("redirect", "/pages/auth/googleAuthenticator.html");
            }

        } else {
            response.setHeader("redirect", "/index.html");
        }
    }

    private void storeCourseDataInDatastore(String courseJSON) {
        try {
            String userEmail = Services.USER_SERVICE.getCurrentUser().getEmail();
            String userID = Services.USER_SERVICE.getCurrentUser().getUserId();

            Filter userEmailFilter = new FilterPredicate("userEmail", FilterOperator.EQUAL, userEmail);
            Query query = new Query("Course").setFilter(userEmailFilter);
            PreparedQuery results = Services.DATA_STORE.prepare(query);

            for (Entity course : results.asIterable()) {
                Services.DATA_STORE.delete(course.getKey());
            }

            JSONObject coursesObject = (JSONObject) new JSONParser().parse(courseJSON);; 
            JSONArray courses = (JSONArray) coursesObject.get("courses");
            Iterator coursesIterator = courses.iterator();

            while (coursesIterator.hasNext()) {
                JSONObject course = (JSONObject) coursesIterator.next();

                String id = (String) course.get("id");
                String name = (String) course.get("name");
                String description = (String) course.get("description");
                String creationTime = (String) course.get("creationTime");

                Entity courseEntity = new Entity("Course");

                courseEntity.setProperty("id", id);
                courseEntity.setProperty("name", name);
                courseEntity.setProperty("description", description);
                courseEntity.setProperty("creationTime", creationTime);
                courseEntity.setProperty("userEmail", userEmail);
                courseEntity.setProperty("userID", userID);

                Services.DATA_STORE.put(courseEntity);            
            } 

        } catch (Exception e) {

            e.printStackTrace();
        
        } finally {

        }
    }

    private String getCoursesDataFromDatastore() {
        String userEmail = Services.USER_SERVICE.getCurrentUser().getEmail();

        Filter userEmailFilter = new FilterPredicate("userEmail", FilterOperator.EQUAL, userEmail);
        Query query = new Query("Course").setFilter(userEmailFilter).addSort("creationTime", SortDirection.DESCENDING);
        PreparedQuery results = Services.DATA_STORE.prepare(query);

        String coursesData = "{\"courses\": [";

        for (Entity course : results.asIterable()) {
            String name = (String) course.getProperty("name");
            String courseID = (String) course.getProperty("id");

            String courseData = "{";
            
            courseData += ("\"name\":" + "\"" + name + "\"");
            courseData += (",");
            courseData += ("\"id\":" + "\"" + courseID + "\"");

            courseData += "},";

            coursesData += courseData;
        }

        coursesData = coursesData.substring(0, coursesData.length() - 1);

        coursesData += "]}";
        
        return coursesData;
    }

}

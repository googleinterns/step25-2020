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

@WebServlet("/listAssignments")
public final class ListAssignmentsServlet extends HttpServlet {

    private static String END_POINT = "https://classroom.googleapis.com/v1/courses/{courseId}/courseWork";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (Services.USER_SERVICE.isUserLoggedIn()) {
            response.setContentType("application/json");

            String authorization = API.getCurrentUserAPIAuthorization();

            if (authorization != null) {

                String requestURL = (String) request.getHeader("Referer");
                String courseID = requestURL.substring (requestURL.indexOf("?courseID=") + 10);
                String endpoint = END_POINT.replace("{courseId}", courseID) + "?key=" + API.API_KEY;

                URL url = new URL(endpoint);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Authorization", authorization);

                String json = API.getJSON(connection);

                storeAssignmentsDataInDatastore(json, courseID);

                String assignmentsData = getAssignmentsDataFromDatastore(courseID);

                System.out.println("\n\n" + assignmentsData + "\n\n");
                
                response.getWriter().println(assignmentsData);
            } else {
                response.setHeader("redirect", "/pages/auth/googleAuthenticator.html");
            }

        } else {
            response.setHeader("redirect", "/index.html");
        }
    }

    private void storeAssignmentsDataInDatastore(String courseJSON, String courseID) {
        try {
            Filter courseIDFilter = new FilterPredicate("courseID", FilterOperator.EQUAL, courseID);
            Query query = new Query("Assignment").setFilter(courseIDFilter);
            PreparedQuery results = Services.DATA_STORE.prepare(query);

            for (Entity assignment : results.asIterable()) {
                Services.DATA_STORE.delete(assignment.getKey());
            }

            JSONObject courseWorkObject = (JSONObject) new JSONParser().parse(courseJSON);; 
            JSONArray courseWork = (JSONArray) courseWorkObject.get("courseWork");
            Iterator courseWorkIterator = courseWork.iterator();

            while (courseWorkIterator.hasNext()) {

                JSONObject courseWorkTempObject = (JSONObject) courseWorkIterator.next();
                String courseWorkType = (String) courseWorkTempObject.get("workType");

                if (courseWorkType.equals("ASSIGNMENT")) {
                    JSONObject assignment = courseWorkTempObject;

                    String id = (String) assignment.get("id");
                    String title = (String) assignment.get("title");
                    long maxPoints = (long) assignment.get("maxPoints");
                    String description = (String) assignment.get("description");
                    String creationTime = (String) assignment.get("creationTime");

                    Entity assignmentEntity = new Entity("Assignment");

                    assignmentEntity.setProperty("id", id);
                    assignmentEntity.setProperty("title", title);
                    assignmentEntity.setProperty("courseID", courseID);
                    assignmentEntity.setProperty("description", description);
                    assignmentEntity.setProperty("creationTime", creationTime);

                    Services.DATA_STORE.put(assignmentEntity);
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
        
        } finally {

        }
    }

    private String getAssignmentsDataFromDatastore(String courseID) {
        Filter courseIDFilter = new FilterPredicate("courseID", FilterOperator.EQUAL, courseID);
        Query query = new Query("Assignment").setFilter(courseIDFilter).addSort("creationTime", SortDirection.DESCENDING);
        PreparedQuery results = Services.DATA_STORE.prepare(query);

        String assignmentsData = "{\"courseWork\": [";

        for (Entity assignment : results.asIterable()) {
            String title = (String) assignment.getProperty("title");
            String assignmentID = (String) assignment.getProperty("id");
            String description = (String) assignment.getProperty("description");

            String assignmentData = "{";
            
            assignmentData += ("\"title\":" + "\"" + title + "\"");
            assignmentData += (",");
            assignmentData += ("\"id\":" + "\"" + assignmentID + "\"");
            assignmentData += (",");
            assignmentData += ("\"courseId\":" + "\"" + courseID + "\"");
            assignmentData += (",");
            assignmentData += ("\"description\":" + "\"" + description + "\"");

            assignmentData += "},";

            assignmentsData += assignmentData;
        }

        assignmentsData = assignmentsData.substring(0, assignmentsData.length() - 1);

        assignmentsData += "]}";
        
        return assignmentsData;
    }

}

package com.google.autograder.servlets;

import java.net.URL;
import java.io.IOException;
import java.net.URLConnection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import javax.servlet.http.HttpServlet;
import java.net.MalformedURLException;
import org.json.simple.parser.JSONParser;
import java.nio.charset.StandardCharsets;
import javax.servlet.annotation.WebServlet;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Entity;
import com.google.autograder.servlets.helpers.API;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

public final class CreateNewAssignmentServlet extends HttpServlet {

    private static final String END_POINT = "https://classroom.googleapis.com/v1/courses/{courseId}/courseWork";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestURL = (String) request.getHeader("Referer");
        int courseIDIndex = requestURL.indexOf("?courseID=") + 10;
        String courseID = requestURL.substring(courseIDIndex);

        String authorization = API.getCurrentUserAPIAuthorization();

        if (authorization == null) {
            // The user is not logged in. Redirect to login page.
            response.sendRedirect("/index.html");
        }

        HttpURLConnection connection = null;
        int responseCode = 200;

        try {
            byte[] postBodyData = buildPostBody(request).getBytes(StandardCharsets.UTF_8);
            connection = buildHttpURLConnection(courseID, authorization, postBodyData.length);
            connection.getOutputStream().write(postBodyData);

            String json = API.getJSON(connection);

            System.out.println("\n\n" + json + "\n\n");

            responseCode = connection.getResponseCode();
        } catch(IOException e) {
            // TODO: Handle error response
            responseCode = connection.getResponseCode();   
        }

        response.setHeader("responseCode", String.valueOf(responseCode));
    }

    private String buildPostBody(HttpServletRequest request) throws UnsupportedEncodingException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String maxPoints = request.getParameter("maxPoints");
        String dueDate = request.getParameter("dueDate");
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        String day = request.getParameter("day");
        String hours = request.getParameter("hours");
        String minutes = request.getParameter("minutes");

        JSONObject assignmentObject = new JSONObject();
        JSONObject dueDateObject = new JSONObject();
        JSONObject dueTimeObject = new JSONObject();

        assignmentObject.put("title", title);
        assignmentObject.put("description", description);
        assignmentObject.put("maxPoints", maxPoints);
        assignmentObject.put("state", "DRAFT");
        assignmentObject.put("workType", "ASSIGNMENT");
        assignmentObject.put("assigneeMode", "ALL_STUDENTS");
        assignmentObject.put("submissionModificationMode", "MODIFIABLE_UNTIL_TURNED_IN");

        dueDateObject.put("year", year);
        dueDateObject.put("month", month);
        dueDateObject.put("day", day);

        dueTimeObject.put("hours", hours);
        dueTimeObject.put("minutes", minutes);

        assignmentObject.put("dueDate", dueDateObject);
        assignmentObject.put("dueTime", dueTimeObject);

        return assignmentObject.toString();
    }

    private HttpURLConnection buildHttpURLConnection(String courseID, String authorization, int postBodyDataLength) throws IOException, MalformedURLException, ProtocolException {
        HttpURLConnection connection = (HttpURLConnection) new URL(END_POINT.replace("{courseId}", courseID)).openConnection();

        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("courseId", courseID);
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", authorization);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Content-Length", String.valueOf(postBodyDataLength));

        return connection;
    }    

}
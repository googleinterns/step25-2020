package com.google.autograder.servlets;

import java.net.URL;
import java.io.IOException;
import java.net.URLConnection;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import javax.servlet.http.HttpServlet;
import java.net.MalformedURLException;
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
            response.sendRedirect("/pages/course.html?courseID=" + courseID);
        }

        byte[] postBodyData = buildPostBody(request).getBytes(StandardCharsets.UTF_8);
        HttpURLConnection connection = buildHttpURLConnection(courseID, authorization, postBodyData.length);
        connection.getOutputStream().write(postBodyData);
        connection.connect();

        int responseCode = connection.getResponseCode();

        if (responseCode == 200) {
            response.sendRedirect("/pages/course.html?courseID=" + courseID);
        } else {
            // TODO: Handle error response
            System.out.println("\n\nBAD\tRESPONSE\tCODE:\t" + responseCode + "\n\n");
        }
    }

    private int[] getDayMonthYear(String date) {
        int firstSeperator = date.indexOf('-');
        int secondSeperator = date.lastIndexOf('-');
        int[] values = new int[3];

        values[0] = Integer.parseInt(date.substring(firstSeperator + 1, secondSeperator));
        values[1] = Integer.parseInt(date.substring(secondSeperator + 1));
        values[2] = Integer.parseInt(date.substring(0, firstSeperator));

        return values;
    }

    private int[] getHoursMinutes(String time) {
        int seperator = time.indexOf(':');
        int[] values = new int[2];

        values[0] = Integer.parseInt(time.substring(0, seperator));
        values[1] = Integer.parseInt(time.substring(seperator + 1));

        return values;
    }

    private String buildPostBody(HttpServletRequest request) throws UnsupportedEncodingException {
        String description = request.getParameter("description");
        String maxPoints = request.getParameter("maxPoints");
        String dueDate = request.getParameter("dueDate");
        String dueTime = request.getParameter("dueTime");
        String title = request.getParameter("title");

        int[] dayMonthYear = getDayMonthYear(dueDate);
        int[] hoursMinutes = getHoursMinutes(dueTime);

        StringBuilder postBody = new StringBuilder();

        postBody.append("{");
        postBody.append("\"title\":\"" + title + "\",");
        postBody.append("\"description\":\"" + description + "\",");
        postBody.append("\"maxPoints\":" + maxPoints + ",");
        postBody.append("\"state\":\"" + "DRAFT" + "\",");
        postBody.append("\"workType\":\"" + "ASSIGNMENT" + "\",");
        postBody.append("\"assigneeMode\":\"" + "ALL_STUDENTS" + "\",");
        postBody.append("\"submissionModificationMode\":\"" + "MODIFIABLE_UNTIL_TURNED_IN" + "\",");
        postBody.append("\"dueDate\":{");
        postBody.append("\"day\":" + dayMonthYear[0] + ",");
        postBody.append("\"month\":" + dayMonthYear[1] + ",");
        postBody.append("\"year\":" + dayMonthYear[2]);
        postBody.append("},");
        postBody.append("\"dueTime\":{");
        postBody.append("\"hours\":" + hoursMinutes[0] + ",");
        postBody.append("\"minutes\":" + hoursMinutes[1]);
        postBody.append("}");
        postBody.append("}");

        return postBody.toString();
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
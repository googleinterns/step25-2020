package com.google.autograder.servlets;

import java.net.URL;
import java.io.IOException;
import java.net.HttpURLConnection;
import javax.servlet.http.HttpServlet;
import javax.servlet.annotation.WebServlet;
import com.google.autograder.data.Database;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.autograder.data.UserHandler;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Entity;
import com.google.autograder.servlets.helpers.API;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.List;
import java.util.ArrayList;

// @WebServlet("/listAssignmentSubmissions")
public final class ListAssignmentSubmissionsServlet extends HttpServlet {

    private static String END_POINT = "https://classroom.googleapis.com/v1/courses/{courseId}/courseWork/{courseWorkId}/studentSubmissions";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!UserHandler.isUserLoggedIn()) {
            // User is not logged. Redirect to login page.
            response.setHeader("redirect", "/index.html");
        }

        String authorization = API.getCurrentUserAPIAuthorization();

        if (authorization == null) {
            // User is not authenticated. Authenticate them.
            // TODO: Send the redirect url to the OAuth handler to resume user flow after authentication.
            response.setHeader("redirect", "/pages/auth/googleAuthenticator.html");
        }

        response.setContentType("application/json");

        String courseID = request.getParameter("courseID");
        String assignmentID = request.getParameter("assignmentID");
        
        String endpoint = END_POINT.replace("{courseId}", courseID);
        endpoint = endpoint.replace("{courseWorkId}", assignmentID);
        endpoint = endpoint += "?key=" + API.API_KEY;
        
        System.out.println("\n\nCourse ID:\t\t" + courseID + "\n\n");
        System.out.println("\n\nAssignment ID:\t\t" + assignmentID + "\n\n");
        System.out.println("\n\nRequest Endpoint:\t" + endpoint + "\n\n");

        HttpURLConnection connection = (HttpURLConnection) new URL(endpoint).openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", authorization);
        
        String json = API.getJSON(connection);

        System.out.println("\n\n" + json + "\n\n");

        List<String> driveFileIDs = getDriveFileIDs(json);

        for(String id : driveFileIDs) {
            System.out.println("GOOGLE DRIVE FILE ID:\t" + id + "\n");
        }

        storeAssignmentSubmissionsData(json, courseID, assignmentID);
        // Database.storeAssignmentSubmissionsData(json, courseID, assignmentID);

        String assignmentSubmissionsData = getAssignmentSubmissionsData(courseID, assignmentID);
        // Database.getAssignmentSubmissionsData(courseID, assignmentID);
                
        response.getWriter().println(assignmentSubmissionsData);
    }

    private List<String> getDriveFileIDs(String json) {
        List<String> driveFileIDs = new ArrayList<>();
        JSONObject jsonObject = null;

        try {
            jsonObject = (JSONObject) new JSONParser().parse(json);
        } catch (Exception e) {
            // handle error
            return null;
        }

        JSONArray studentSubmissionsArray = (JSONArray) jsonObject.get("studentSubmissions");
        Iterator studentSubmissionsIterator = studentSubmissionsArray.iterator();

        while (studentSubmissionsIterator.hasNext()) {
            JSONObject studentSubmission = (JSONObject) studentSubmissionsIterator.next();
            JSONObject assignmentSubmission = (JSONObject) studentSubmission.get("assignmentSubmission");
            JSONArray attachmentsArray = (JSONArray) assignmentSubmission.get("attachments");

            if (attachmentsArray != null) {
                Iterator attachmentsIterator = attachmentsArray.iterator();

                if (attachmentsIterator.hasNext()) {
                    JSONObject attachment = (JSONObject) attachmentsIterator.next();
                    JSONObject driveFile = (JSONObject) attachment.get("driveFile");

                    driveFileIDs.add(driveFile.get("id").toString());
                }
            }
        }

        return driveFileIDs;
    }


    public static void storeAssignmentSubmissionsData(String submissionsJSON, String courseID, String assignmentID) {}

    public static String getAssignmentSubmissionsData(String courseID, String assignmentID) {
        return null;
    }

}
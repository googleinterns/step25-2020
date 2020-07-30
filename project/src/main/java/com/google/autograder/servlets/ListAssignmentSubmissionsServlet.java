package com.google.autograder.servlets;

import java.net.URL;
import java.util.List;
import java.util.Iterator;
import java.io.IOException;
import java.util.ArrayList;
import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.net.HttpURLConnection;
import javax.servlet.http.HttpServlet;
import org.json.simple.parser.JSONParser;
import com.google.gson.reflect.TypeToken;
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

// @WebServlet("/listAssignmentSubmissions")
public final class ListAssignmentSubmissionsServlet extends HttpServlet {

    private static String CLASSROOM_END_POINT = "https://classroom.googleapis.com/v1/courses/{courseId}/courseWork/{courseWorkId}/studentSubmissions";
    private static String DRIVE_PREVIEW_LINK = "https://drive.google.com/file/d/{fileId}/preview";

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
        
        String classroomEndpoint = CLASSROOM_END_POINT.replace("{courseId}", courseID);
        classroomEndpoint = classroomEndpoint.replace("{courseWorkId}", assignmentID);
        classroomEndpoint = classroomEndpoint += "?key=" + API.API_KEY;

        HttpURLConnection classroomConnection = (HttpURLConnection) new URL(classroomEndpoint).openConnection();

        classroomConnection.setRequestMethod("GET");
        classroomConnection.setRequestProperty("Accept", "application/json");
        classroomConnection.setRequestProperty("Authorization", authorization);
        
        String json = API.getJSON(classroomConnection);

        List<String> driveFileIDs = getDriveFileIDs(json);
        List<String> driveFilePreviewLinks = new ArrayList<>();

        for(String driveFileID : driveFileIDs) {
            driveFilePreviewLinks.add("\"" + DRIVE_PREVIEW_LINK.replace("{fileId}", driveFileID) + "\"");
        }

        String responseJSON = new Gson().toJson(driveFilePreviewLinks);

        // storeAssignmentSubmissionsData(json, courseID, assignmentID);
        // Database.storeAssignmentSubmissionsData(json, courseID, assignmentID);

        // String assignmentSubmissionsData = getAssignmentSubmissionsData(courseID, assignmentID);
        // Database.getAssignmentSubmissionsData(courseID, assignmentID);
                
        response.getWriter().println(driveFilePreviewLinks);
    }

    private List<String> getDriveFileIDs(String json) {
        List<String> driveFileIDs = new ArrayList<>();
        JSONObject jsonObject = null;

        try {
            jsonObject = (JSONObject) new JSONParser().parse(json);
        } catch (Exception e) {
            // handle error
            return driveFileIDs;
        }

        JSONArray studentSubmissionsArray = (JSONArray) jsonObject.get("studentSubmissions");

        if (studentSubmissionsArray.iterator() != null) {
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
        }

        return driveFileIDs;
    }


    public static void storeAssignmentSubmissionsData(String submissionsJSON, String courseID, String assignmentID) {}

    public static String getAssignmentSubmissionsData(String courseID, String assignmentID) {
        return null;
    }

}
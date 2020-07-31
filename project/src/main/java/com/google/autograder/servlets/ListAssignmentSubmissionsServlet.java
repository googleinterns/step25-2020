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

public final class ListAssignmentSubmissionsServlet extends HttpServlet {

    private static String CLASSROOM_END_POINT = "https://classroom.googleapis.com/v1/courses/{courseId}/courseWork/{courseWorkId}/studentSubmissions";
    private static String STUDENT_INFO_END_POINT = "https://classroom.googleapis.com/v1/courses/{courseId}/students/{userId}";    
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

        List<String> studentNames = new ArrayList<>();
        List<String> studentEmails = new ArrayList<>();
        List<String> studentUserIDs = new ArrayList<>();
        List<String> studentSubmissionDriveFileLinks = new ArrayList<>();

        parseStudentSubmissionInfo(json, studentNames, studentUserIDs, studentSubmissionDriveFileLinks);
        parseStudentInfo(authorization, courseID, studentUserIDs, studentNames, studentEmails);

        JSONArray jsonArray = new JSONArray();

        for (int index = 0; index < studentSubmissionDriveFileLinks.size(); index++) {
            String studentName = studentNames.get(index);
            String studentEmail = studentEmails.get(index);
            String studentSubmissionDriveFileLink = studentSubmissionDriveFileLinks.get(index);

            JSONObject studentSubmissionData = new JSONObject();

            studentSubmissionData.put("name", studentName);
            studentSubmissionData.put("email", studentEmail);
            studentSubmissionData.put("link", studentSubmissionDriveFileLink);

            jsonArray.add(studentSubmissionData);
        }

        // storeAssignmentSubmissionsData(json, courseID, assignmentID);
        // Database.storeAssignmentSubmissionsData(json, courseID, assignmentID);

        // String assignmentSubmissionsData = getAssignmentSubmissionsData(courseID, assignmentID);
        // Database.getAssignmentSubmissionsData(courseID, assignmentID);
                
        response.setContentType("application/json");
        response.getWriter().println(jsonArray.toString());
    }

    private void parseStudentSubmissionInfo(String json, List<String> studentNames, List<String> studentUserIDs, List<String> studentSubmissionDriveFileLinks) {
        JSONObject jsonObject = null;
        
        try {
            jsonObject = (JSONObject) new JSONParser().parse(json);
        } catch (Exception e) {
            return;
        }

        JSONArray studentSubmissionsArray = (JSONArray) jsonObject.get("studentSubmissions");

        if (studentSubmissionsArray.iterator() == null) {
            return;
        }
        
        Iterator studentSubmissionsIterator = studentSubmissionsArray.iterator();

        while (studentSubmissionsIterator.hasNext()) {
            JSONObject studentSubmission = (JSONObject) studentSubmissionsIterator.next();
            JSONObject assignmentSubmission = (JSONObject) studentSubmission.get("assignmentSubmission");
            JSONArray attachmentsArray = (JSONArray) assignmentSubmission.get("attachments");
            String studentUserID = studentSubmission.get("userId").toString();

            if (attachmentsArray == null) {
                continue;
            }

            Iterator attachmentsIterator = attachmentsArray.iterator();

            if (!attachmentsIterator.hasNext()) {
                continue;
            }
                
            JSONObject attachment = (JSONObject) attachmentsIterator.next();
            JSONObject driveFileObject = (JSONObject) attachment.get("driveFile");
            String driveFileID = driveFileObject.get("id").toString();

            studentUserIDs.add(studentUserID);
            studentSubmissionDriveFileLinks.add(DRIVE_PREVIEW_LINK.replace("{fileId}", driveFileID));
        }
    }

    private void parseStudentInfo(String authorization, String courseID, List<String> studentUserIDs, List<String> studentNames, List<String> studentEmails) {
        for (String studentUserID : studentUserIDs) {
            String studentInfoEndpoint = STUDENT_INFO_END_POINT.replace("{courseId}", courseID);
            studentInfoEndpoint = studentInfoEndpoint.replace("{userId}", studentUserID);
            studentInfoEndpoint = studentInfoEndpoint += "?key=" + API.API_KEY;

            String json = null;
            JSONObject jsonObject = null;

            try {
                HttpURLConnection studentInfoConnection = (HttpURLConnection) new URL(studentInfoEndpoint).openConnection();
                
                studentInfoConnection.setRequestMethod("GET");
                studentInfoConnection.setRequestProperty("Accept", "application/json");
                studentInfoConnection.setRequestProperty("Authorization", authorization);
                
                json = API.getJSON(studentInfoConnection);
            } catch(Exception e) {
                continue;
            }

            try {
                jsonObject = (JSONObject) new JSONParser().parse(json);
            } catch (Exception e) {
                continue;
            }

            JSONObject profile = (JSONObject) jsonObject.get("profile");
            String emailAddress = profile.get("emailAddress").toString();
            String fullName = ((JSONObject) profile.get("name")).get("fullName").toString();

            studentNames.add(fullName);
            studentEmails.add(emailAddress);
        }
    }

    public static void storeAssignmentSubmissionsData(String submissionsJSON, String courseID, String assignmentID) {}

    public static String getAssignmentSubmissionsData(String courseID, String assignmentID) {
        return null;
    }

}
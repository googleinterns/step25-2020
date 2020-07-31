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
import com.google.autograder.data.Database;
import javax.servlet.annotation.WebServlet;
import com.google.autograder.data.Database;
import com.google.autograder.data.Submission;
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

public final class GetStudentServlet extends HttpServlet {

    private static String GET_STUDENT_END_POINT = "https://classroom.googleapis.com/v1/courses/{courseId}/students/{userId}";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    }

    private void parseStudentInfo(String authorization, String courseID, List<String> studentUserIDs, List<String> studentNames, List<String> studentEmails) {
        // for (String studentUserID : studentUserIDs) {
        //     String studentInfoEndpoint = STUDENT_INFO_END_POINT.replace("{courseId}", courseID);
        //     studentInfoEndpoint = studentInfoEndpoint.replace("{userId}", studentUserID);
        //     studentInfoEndpoint = studentInfoEndpoint += "?key=" + API.API_KEY;

        //     String json = null;
        //     JSONObject jsonObject = null;

        //     try {
        //         HttpURLConnection studentInfoConnection = (HttpURLConnection) new URL(studentInfoEndpoint).openConnection();
                
        //         studentInfoConnection.setRequestMethod("GET");
        //         studentInfoConnection.setRequestProperty("Accept", "application/json");
        //         studentInfoConnection.setRequestProperty("Authorization", authorization);
                
        //         json = API.getJSON(studentInfoConnection);
        //     } catch(Exception e) {
        //         continue;
        //     }

        //     try {
        //         jsonObject = (JSONObject) new JSONParser().parse(json);
        //     } catch (Exception e) {
        //         continue;
        //     }

        //     JSONObject profile = (JSONObject) jsonObject.get("profile");
        //     String emailAddress = profile.get("emailAddress").toString();
        //     String fullName = ((JSONObject) profile.get("name")).get("fullName").toString();

        //     studentNames.add(fullName);
        //     studentEmails.add(emailAddress);
        // }
    }

}
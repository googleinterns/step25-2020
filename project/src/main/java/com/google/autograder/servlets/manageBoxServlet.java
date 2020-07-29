// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.autograder.data.Database;
import com.google.autograder.data.Question;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson; 
import com.google.gson.GsonBuilder;  
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import java.util.Map;
import java.util.HashMap;

@WebServlet("/manageBox")
public class manageBoxServlet extends HttpServlet {
  
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {     
      
// TODO: rewrite with database class
    // Query query = new Query("Coordinates");
    // DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    // PreparedQuery results = datastore.prepare(query);
    // Map<String, String> mapCoordinates = new HashMap<>();
// TODO: put coordinates and points as fields for questionName
    // for (Entity entity : results.asIterable()) {
        // String lx = (String) entity.getProperty("lx");
        // String ly = (String) entity.getProperty("ly");
        // String rx = (String) entity.getProperty("ry");
        // String ry = (String) entity.getProperty("ry");
        // String questionName = (String) entity.getProperty("questionName");
        // String points = (String) entity.getProperty("question-points");

        // mapCoordinates.put("lx", lx);
        // mapCoordinates.put("ly", ly);
        // mapCoordinates.put("rx", rx);
        // mapCoordinates.put("ry", ry);
        // mapCoordinates.put("questionName", questionName);
        // mapCoordinates.put("points", points);
    // }

    // Gson gson = new Gson();
    response.setContentType("text/html");
    response.getWriter().println("hello world");
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String leftXCoord = request.getParameter("leftXCoord");
    String topYCoord = request.getParameter("topYCoord");
    String rightXCoord = request.getParameter("rightXCoord");
    String lowerYCoord = request.getParameter("lowerYCoord");

    String questionName = request.getParameter("qName");
    String questionType = request.getParameter("qType");
    int questionPoints = Integer.parseInt(request.getParameter("qPoints"));
    String assignmentKey = request.getParameter("assignment-key");

    Database.addQuestion(questionName, questionType, questionPoints, assignmentKey);

    response.sendRedirect("/manageBox");
  }

}

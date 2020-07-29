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

package com.google.autograder.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
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

// @WebServlet("/manageBox")
public class ManageBoxServlet extends HttpServlet {
  
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {  
      System.out.println("bruh");   
    // Query query = new Query("Coordinates");

    // DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    // PreparedQuery results = datastore.prepare(query);
    
    // Map<String, String> mapCoordinates = new HashMap<>();
    // // TODO: put coordinates and points as fields for questionName

    // for (Entity entity : results.asIterable()) {
    //     String lx = (String) entity.getProperty("lx");
    //     String ly = (String) entity.getProperty("ly");
    //     String rx = (String) entity.getProperty("ry");
    //     String ry = (String) entity.getProperty("ry");
    //     // String questionName = (String) entity.getProperty("questionName");
    //     // String points = (String) entity.getProperty("question-points");

    //     mapCoordinates.put("lx", lx);
    //     mapCoordinates.put("ly", ly);
    //     mapCoordinates.put("rx", rx);
    //     mapCoordinates.put("ry", ry);
    //     // mapCoordinates.put("questionName", questionName);
    //     // mapCoordinates.put("points", points);
    // }

    // Gson gson = new Gson();

    // response.setContentType("application/json");
    // response.getWriter().println(gson.toJson(mapCoordinates));

  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

      System.out.println("weewoo");

    String lx = request.getParameter("lx");
    String ly = request.getParameter("ly");
    String rx = request.getParameter("rx");
    String ry = request.getParameter("ry");
    String questionName = request.getParameter("question-name");
    String pts = request.getParameter("question-points");

    Entity questionEntity = new Entity("Coordinates");
    questionEntity.setProperty("lx", lx);
    questionEntity.setProperty("ly", ly);
    questionEntity.setProperty("rx", rx);
    questionEntity.setProperty("ry", ry);
    // questionEntity.setProperty("questionName", questionName);
    // questionEntity.setProperty("question-points", pts);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(questionEntity);

    response.sendRedirect("/pages/editOutline.html");
  }

}
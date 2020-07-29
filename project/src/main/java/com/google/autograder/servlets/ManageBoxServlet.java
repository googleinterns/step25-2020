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

public class ManageBoxServlet extends HttpServlet {
  
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {     
    response.setContentType("text/html");
    response.getWriter().println("hello world");
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    int leftXCoord = Integer.parseInt(request.getParameter("leftXCoord"));
    int topYCoord = Integer.parseInt(request.getParameter("topYCoord"));
    int rightXCoord = Integer.parseInt(request.getParameter("rightXCoord"));
    int lowerYCoord = Integer.parseInt(request.getParameter("lowerYCoord"));

    String questionName = request.getParameter("qName");
    String questionType = request.getParameter("qType");
    int questionPoints = Integer.parseInt(request.getParameter("qPoints"));
    String assignmentKey = request.getParameter("assignment-key");

    Entity questionEntity = Database.addQuestion(questionName, questionType, questionPoints, assignmentKey);
    Database.addLocation(questionEntity, leftXCoord, topYCoord, rightXCoord, lowerYCoord);
    response.setStatus(200);
  }

}

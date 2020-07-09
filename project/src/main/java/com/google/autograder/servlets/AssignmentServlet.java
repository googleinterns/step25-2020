// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
package com.google.autograder.servlets;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import com.google.autograder.data.Assignment;
import com.google.gson.Gson;
import java.io.IOException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

@WebServlet("/assignment")
public final class AssignmentServlet extends HttpServlet {

  DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get the input from form
    String name = request.getParameter("name");
    String totalPoints = request.getParameter("total-points");
    Entity newAssignment = new Entity("Assignment");
    newAssignment.setProperty("name", name);
    newAssignment.setProperty("points", totalPoints);
    newAssignment.setProperty("status", "SAMPLE_PENDING");
    datastore.put(newAssignment);
    // Redirect back to the HTML page.
    response.sendRedirect("/pages/assignments.html");
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("Assignment");
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    List<Assignment> assignments = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
      String name = (String) entity.getProperty("name");
      int points = (int) entity.getProperty("points");
      String status = (String) entity.getProperty("status");
      Assignment currAssignment = new Assignment(name, points, status);
      assignments.add(currAssignment);
    }
    response.setContentType("application/json;");
    String json = new Gson().toJson(assignments);
    response.getWriter().println(json);
  }
}
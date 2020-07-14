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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/manageBox")
public class manageBoxServlet extends HttpServlet {
  
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {     
    Query query = new Query("Coordinates");

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    
    String coordinates = "lx: ly: rx: ry:";
    for (Entity entity : results.asIterable()) {
        String lx = (String) entity.getProperty("lx");
        String ly = (String) entity.getProperty("ly");
        String rx = (String) entity.getProperty("ry");
        String ry = (String) entity.getProperty("ry");

        coordinates = "lx:"+lx+" ly:"+ly+" rx:"+rx+" ry:"+ry;       
    }

    Gson gson = new Gson();

    // its a string instead of a json for now
    response.setContentType("application/json");
    response.getWriter().println(gson.toJson(coordinates));

  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    String lx = request.getParameter("lx");
    String ly = request.getParameter("ly");
    String rx = request.getParameter("rx");
    String ry = request.getParameter("ry");

    System.out.println(lx+ly+rx+ry);

    Entity messageEntity = new Entity("Coordinates");
    messageEntity.setProperty("lx", lx);
    messageEntity.setProperty("ly", ly);
    messageEntity.setProperty("rx", rx);
    messageEntity.setProperty("ry", ry);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(messageEntity);

    response.sendRedirect("/manageBox");
  }

}

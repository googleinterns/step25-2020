import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;

import com.google.sps.servlets.FavoriteRecipeServlet;
import org.junit.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;

import com.google.common.collect.ImmutableMap;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

package com.google.sps;

// @RunWith(MockitoJUnitRunner.class)
public final class FavoriteServletTest {
    
    private LocalServiceTestHelper helper;

    private final LocalServiceTestHelper helper = 
        new LocalServiceTestHelper(new )

  private DatastoreService addEntitiesAndGetDatastore() {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    Entity course1 = new Entity("Course");
    course1.setProperty("id", "12345");
    course1.getProperty("name", "Autograder Test Class #1");
    course1.getProperty("description", "1st course description here");
    course1.getProperty("creationTime", "2020-07-15T21:36:24.114Z"); 
    course1.getProperty("userEmail", "email1@company.com");  
    course1.getProperty("userID", "0123456789"); 
    datastore.put(course1);

    Entity course2 = new Entity("Course");
    course2.setProperty("id", "09876");
    course2.getProperty("name", "Autograder Test Class #2");
    course2.getProperty("description", "2nd course description here");
    course2.getProperty("creationTime", "2222-07-15T21:36:24.114Z"); 
    course2.getProperty("userEmail", "email2@company.com");  
    course2.getProperty("userID", "9876543210"); 
    datastore.put(course2);

    return datastore;
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }



}
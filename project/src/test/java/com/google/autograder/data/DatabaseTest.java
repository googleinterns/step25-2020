package com.google.autograder.data;

import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;

import org.junit.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

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

import com.google.autograder.*;

public final class DatabaseTest {

  private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  @Before
  public void setUp() {
      helper.setUp();
  }

  @After
  public void tearDown() {
      helper.tearDown();
  }

  /**
   * Tests method getCoursesData
   */
//   @Test
//   public void getCoursesDataTest() {
//     LocalUserServiceTestConfig userServiceConfig = new LocalUserServiceTestConfig();
//     userServiceConfig.setOAuthEmail("myemail@email.com");
//     userServiceConfig.setOAuthEmail("user1");

//     String actualCoursesData = Database.getCoursesData();
//     String expectedCoursesData = "badString";

//     Assert.assertEquals(actualCoursesData, expectedCoursesData);
//   }

  @Test
  public void saveASingleEntity() {
    int courseCountBeforeSaving = 0;
    int courseCountAfterSaving = 0;

    Iterable<Entity> before = Database.query(new Query("Course"));
    for (Entity entity : before) {
        courseCountBeforeSaving++;
    }

    Assert.assertEquals(0, courseCountBeforeSaving);

    Database.save(new Entity("Course"));

    Iterable<Entity> after = Database.query(new Query("Course"));

    for (Entity entity : after) {
        courseCountAfterSaving++;
    }

    Assert.assertEquals(1, courseCountAfterSaving);
  }

 /**
  * Test addAnswer Method 
  */
  @Test
  public void addAnswerTest() {
    Database.addAnswer("filePath1", "parsedAnswer1", 5, "assignmentKey1", "questionKey1");
  }

}
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
import java.util.List;
import java.util.ArrayList;

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

  private static Entity getOnlyEntity(Query query) {
    Iterator<Entity> entities = Database.query(query).iterator();
    if (!entities.hasNext()) {
        Assert.fail("No entities in datastore");
    }
    Entity entity = entities.next();
    if (entities.hasNext()) {
        Assert.fail("More than 1 entity"); 
    }
    return entity;
  }

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
  * Test addAnswer Method to test single answer submitted correctly
  */
  @Test
  public void addAnswerTest() {
    int count = 0;
    Database.addAnswer("filePath1", "parsedAnswer1", 5, "assignmentKey1", "questionKey1");
    
    Entity entity = new Entity("Answer");
    Iterable<Entity> query = Database.query(new Query("Answer"));
    for (Entity e : query) {
        count++;
        entity = e;
    }
    Assert.assertEquals(1, count);
    Assert.assertEquals("filePath1", entity.getProperty("filePath").toString());
    Assert.assertEquals("parsedAnswer1", entity.getProperty("parsedAnswer").toString());
    Assert.assertEquals("5", entity.getProperty("score").toString());
    Assert.assertEquals("assignmentKey1", entity.getProperty("assignmentKey").toString());
    Assert.assertEquals("questionKey1", entity.getProperty("questionKey").toString());
  }

 /**
  * Test addAnswer Method with multiple answers
  */
  @Test
  public void addMultipleAnswersTest() {
    Database.addAnswer("filePath1", "parsedAnswer1", 5, "assignmentKey1", "questionKey1");
    Database.addAnswer("filePath2", "parsedAnswer3", 6, "assignmentKey1", "questionKey1");
    Database.addAnswer("filePath3", "parsedAnswer2", 7, "assignmentKey1", "questionKey1");
    Database.addAnswer("filePath4", "parsedAnswer5", 8, "assignmentKey1", "questionKey1");
    Database.addAnswer("filePath5", "parsedAnswer4", 9, "assignmentKey1", "questionKey1");
    Iterable<Entity> query = Database.query(new Query("Answer"));

    List<String> expectedAnswers = new ArrayList<>();
    expectedAnswers.add("parsedAnswer1");
    expectedAnswers.add("parsedAnswer3");
    expectedAnswers.add("parsedAnswer2");
    expectedAnswers.add("parsedAnswer5");
    expectedAnswers.add("parsedAnswer4");

    int count = 0;
    List<String> actualAnswers = new ArrayList<>();
    for (Entity entity : query) {
        count++;
        actualAnswers.add(entity.getProperty("parsedAnswer").toString());
    }

    Assert.assertEquals(expectedAnswers, actualAnswers);
  }

}
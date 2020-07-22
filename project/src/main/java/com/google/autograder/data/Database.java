package com.google.autograder.data;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import java.util.Arrays;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;


/** Class containing basic database functionalities. */
public class Database {

  public final DatastoreService datastore;
  public final List<String> answerGradingStatuses;

  public Database() {
      this.datastore = DatastoreServiceFactory.getDatastoreService();
      this.answerGradingStatuses = Arrays.asList("NOT_GRADED", "GRADED");
  }
    
  //create operations for all entities (with pk and fk restraints)
  //FOR FUTURE: ensure new objects have diff main names

  public void addAssignment(String name, int totalPoints) {
    Entity assignmentEntity = new Entity("Assignment");
    assignmentEntity.setProperty("name", name);
    assignmentEntity.setProperty("points", totalPoints);
    assignmentEntity.setProperty("status", "SAMPLE_PENDING");
    this.datastore.put(assignmentEntity);
  }

  public void addQuestion(String questionName, String questionType, int questionPoints, String assignmentKey) {
      Entity questionEntity = new Entity("Question");
      questionEntity.setProperty("name", questionName);
      questionEntity.setProperty("type", questionType);
      questionEntity.setProperty("points", questionPoints);
      questionEntity.setProperty("assignmentKey", assignmentKey);
      this.datastore.put(questionEntity);
      System.out.println("Question " + questionName + " has been added.");
  }

  public void addSubmission(Entity assignmentEntity) {
      Entity submissionEntity = new Entity("Submission");
      submissionEntity.setProperty("graded", "NOT_GRADED");
      submissionEntity.setProperty("assignmentKey", assignmentEntity.getKey());
      this.datastore.put(submissionEntity);
  }

  public void addLocation(Entity questionEntity, int topLeft, int bottomRight) {
      Entity locationEntity = new Entity("Location");
      locationEntity.setProperty("topLeft", topLeft);
      locationEntity.setProperty("bottomRight", bottomRight);
      locationEntity.setProperty("questionKey", questionEntity.getKey());
      this.datastore.put(locationEntity);
  }

  public void addAnswer(Entity questionEntity, Entity submissionEntity, String parsedAnswer, int points) {
      Entity answerEntity = new Entity("Answer");
      answerEntity.setProperty("parsedAnswer", parsedAnswer);
      answerEntity.setProperty("points", points);
      answerEntity.setProperty("graded", "NOT_GRADED");
      answerEntity.setProperty("questionKey", questionEntity.getKey());
      answerEntity.setProperty("submissionKey", submissionEntity.getKey());
      this.datastore.put(answerEntity);
  }

  public void addGroup(Entity questionEntity) {
      Entity groupEntity = new Entity("Group");
      groupEntity.setProperty("questionKey", questionEntity.getKey());
      this.datastore.put(groupEntity);
  }

  public void updateGroupForAnswer(Entity answerEntity, Entity groupEntity) {
      answerEntity.setProperty("groupKey", groupEntity.getKey());
      this.datastore.put(answerEntity);
  }

  public void updateGradedForAnswer(Entity answerEntity, String status) {
      if (this.answerGradingStatuses.contains(status)) {
        answerEntity.setProperty("graded", status);
        this.datastore.put(answerEntity); }
  }

  public void updateScoreForAnswer(Entity groupEntity, int score) {
      groupEntity.setProperty("score", score);
      this.datastore.put(groupEntity);
  }

  public PreparedQuery queryDatabase(Query query) {
      return this.datastore.prepare(query);
  }

  public String getAllAssignmentsJSON() {
    Query query = new Query("Assignment");
    PreparedQuery results = this.queryDatabase(query);
    List<Assignment> assignments = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
      String name = (String) entity.getProperty("name");
      Long pointsLong = (Long) entity.getProperty("points");
      int points = Math.toIntExact(pointsLong);
      String status = (String) entity.getProperty("status");
      Key key = entity.getKey();
      Assignment currAssignment = new Assignment(name, points, status, key);
      boolean test = (currAssignment.key).equals(KeyFactory.keyToString(KeyFactory.stringToKey(currAssignment.key)));
      if (test == true) {
          System.out.println("sanity check passes");
      }
      else {
          System.out.println("sanity check fails");
      }
      assignments.add(currAssignment);
    }
    String json = new Gson().toJson(assignments);
    return json;
  }

  public String getAllQuestionsJSON(String key) {
    // query all questions with this key at assignment_id key
    System.out.println("key is " + key);
    try {
        // Key aKey = KeyFactory.stringToKey(key);
        //Entity assignment = this.datastore.get(aKey);
        Filter propertyFilter = new FilterPredicate("assignmentKey", FilterOperator.EQUAL, key);
        Query query = new Query("Question").setFilter(propertyFilter);

        //Query query = new Query("Question").setFilter(propertyFilter);
        PreparedQuery results = this.queryDatabase(query);
        List<Question> questions = new ArrayList<>();
        for (Entity entity : results.asIterable()) {
            String name = (String) entity.getProperty("name");
            Long pointsLong = (Long) entity.getProperty("points");
            int points = Math.toIntExact(pointsLong);
            String status = (String) entity.getProperty("status");
            String assignmentKey = (String) entity.getProperty("assignmentKey");
            Question currQuestion = new Question(name, points, status, assignmentKey);
            questions.add(currQuestion);
        }
        String json = new Gson().toJson(questions);
        System.out.println(json);
        return json;
    }
    catch (Exception e) {
        System.out.println("assignment wasn't found");
        return "error";
    }
    
  }

  //get all submissions for an assignment

  //get all answers for a question

  //set/edit location for a question

  //get all groups for a question

  //change answer's group
}
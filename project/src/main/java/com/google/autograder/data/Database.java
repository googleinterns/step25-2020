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

  public Entity addQuestion(String questionName, String questionType, int questionPoints, String assignmentKey) {
      Entity questionEntity = new Entity("Question");
      questionEntity.setProperty("name", questionName);
      questionEntity.setProperty("type", questionType);
      questionEntity.setProperty("points", questionPoints);
      questionEntity.setProperty("assignmentKey", assignmentKey);
      this.datastore.put(questionEntity);
      return questionEntity;
  }

  public void addSubmission(Entity assignmentEntity, String blobKey) {
      Entity submissionEntity = new Entity("Submission");
      submissionEntity.setProperty("graded", "NOT_GRADED");
      submissionEntity.setProperty("assignmentKey", assignmentEntity.getKey().toString());
      submissionEntity.setProperty("blobKey", blobKey);
    //   Contructor: new BlobKey(String blobKey)
      this.datastore.put(submissionEntity);
  }

  public void addLocation(Entity questionEntity, int leftXCoord, int topYCoord, int rightXCoord, int lowerYCoord) {
      Entity locationEntity = new Entity("Location");
      locationEntity.setProperty("leftXCoord", leftXCoord);
      locationEntity.setProperty("topYCoord", topYCoord);
      locationEntity.setProperty("rightXCoord", rightXCoord);
      locationEntity.setProperty("lowerYCoord", lowerYCoord);
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
      assignments.add(currAssignment);
    }
    String json = new Gson().toJson(assignments);
    return json;
  }

  public String getAllQuestionsJSON(String key) {
    // query all questions with this key at assignment_id key
    if (key != null) {
        Filter propertyFilter = new FilterPredicate("assignmentKey", FilterOperator.EQUAL, key);
        Query query = new Query("Question").setFilter(propertyFilter);
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
        return json;
    }
    return new Gson().toJson("");
  }

/** 
 * this function takes the assignment Key and answer group key as parameters
 * returns list of blobKeys to submissions that fit in specified answer group for specific question
 */ 
  public List blobkeysFromAnswerGroup(String assignKey, String groupKey) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    // query answer entities filtered only in group from parameter
    Filter groupFilter = new FilterPredicate("groupKey", FilterOperator.EQUAL, groupKey);
    Query answersQuery = new Query("Answer").setFilter(groupFilter);
    PreparedQuery answerEntities = datastore.prepare(answersQuery);

    // generate list of submissionKeys for the group
    List<String> submissionKeys = new ArrayList<>();
    for (Entity entity : answerEntities.asIterable()) {
        String submissionKey = (String) entity.getProperty("submissionKey");
        submissionKeys.add(submissionKey);
    }  

    // query submissions filtered to the assignment
    Filter assignmentKeyFilter = new FilterPredicate("assignmentKey", FilterOperator.EQUAL, assignkey);
    Query submissionsQuery = new Query("Submission").setFilter(assignmentKeyFilter);
    PreparedQuery submissionEntities = datastore.prepare(submissionsQuery);
    // assignmentKeyFilter is unneeded. I'm unclear if adding this filter will speedup or slowdown the program
    // by reducing the number of submissions that will be iterated through to check if the submission's key is
    // in the answer group

    // iterate through submissions. if the submission's key is in the list of submissionKeys from the answer bucket,
    // add the submission's blobKet to blobKeyList, which will be returned
    List<String> blobKeyList = new ArrayList<>();
    for (Entity submissionEntity : submissionEntities.asIterable()) {
        if (submissionKeys.contains(submissionEntity.getKey())) {
            blobKeyList.add(entity.getProperty(blobKey));
        }
    }

    return blobKeysList;
  }

  //get all submissions for an assignment

  //get all answers for a question

  //set/edit location for a question

  //get all groups for a question

  //change answer's group
}
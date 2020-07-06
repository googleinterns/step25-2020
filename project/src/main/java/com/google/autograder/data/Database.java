package com.google.autograder.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

/** Class containing basic database functionalities. */
public class Database {
    
  //connect to database
  DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  
  //TODO: add variables

  //store item in database (general)
  private void putEntity(Entity entity) {
      datastore.put(entity);
  }

  //load classes

  //create operations for all entities (with pk and fk restraints)
  //FOR FUTURE: ensure new objects have diff main names

  public void addAssignment(int totalPoints) {
    Entity assignmentEntity = new Entity("Assignment");
    assignmentEntity.setProperty("totalPoints", totalPoints);
    assignmentEntity.setProperty("status", "SAMPLE_PENDING");
    datastore.put(assignmentEntity);
  }

  public void addQuestion(Entity assignmentEntity, String questionName, String questionType, int questionPoints) {
      Entity questionEntity = new Entity("Question");
      questionEntity.setProperty("name", questionName);
      questionEntity.setProperty("type", questionType);
      questionEntity.setProperty("points", questionPoints);
      questionEntity.setProperty("assignmentID", assignmentEntity.id());
      datastore.put(questionEntity);
  }


  //get all assignments

  //get all questions for an assignment

  //get all submissions for an assignment

  //get all answers for a question

  //set/edit location for a question

  //get all groups for a question

  //change answer's group
}
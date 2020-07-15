package com.google.autograder.data;

import com.google.appengine.api.datastore.Key;

/** Class containing question data. */
public class Question {

  public String name;
  public String type;
  public int points;
  public String assignmentKey;

  public Question(String name, int points, String type, String assignmentKey) {
    this.name = name;
    this.type = type;
    this.points = points;
    this.assignmentKey = assignmentKey;
  }

}
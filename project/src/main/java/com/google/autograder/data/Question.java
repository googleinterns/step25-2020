package com.google.autograder.data;

import com.google.appengine.api.datastore.Key;

/** Class containing question data. */
public class Assignment {

  public String name;
  public String type;
  public int points;
  public int assignmentKey;

  public Question(String name, String type, int points, int assignmentKey) {
    this.name = name;
    this.type = type;
    this.points = points;
    this.assignmentKey = assignmentKey;
  }

}
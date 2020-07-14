package com.google.autograder.data;

import com.google.appengine.api.datastore.Key;

/** Class containing assignment data. */
public class Assignment {

  public String name;
  public int points;
  public String status;
  public Key key;

  public Assignment(String name, int points, String status, Key key) {
    this.name = name;
    this.points = points;
    this.status = status;
    this.key = key;
  }

}
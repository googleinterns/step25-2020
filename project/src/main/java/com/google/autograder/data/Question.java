package com.google.autograder.data;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/** Class containing question data. */
public class Question {

  public String name;
  public String type;
  public int points;
  public String assignmentKey;
  public String key;

  public Question(String name, int points, String type, String assignmentKey, Key key) {
    this.name = name;
    this.type = type;
    this.points = points;
    this.assignmentKey = assignmentKey;
    this.key = KeyFactory.keyToString(key);
  }

  public String getKeyString() {
      return this.key;
  }

  public Key getKey() {
      return KeyFactory.stringToKey(this.key);
  }

}
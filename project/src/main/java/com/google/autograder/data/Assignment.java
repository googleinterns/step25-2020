package com.google.autograder.data;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/** Class containing assignment data. */
public class Assignment {

  public String name;
  public int points;
  public String status;
  public String key;

  public Assignment(String name, int points, String status, Key key) {
    this.name = name;
    this.points = points;
    this.status = status;
    this.key = KeyFactory.keyToString(key);
   }

  public String getKeyString() {
      return this.key;
  }

  public Key getKey() {
      return KeyFactory.stringToKey(this.key);
  }


}
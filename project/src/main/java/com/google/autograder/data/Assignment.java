package com.google.autograder.data;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/** Class containing assignment data. */
public final class Assignment {

  public String title;
  public String id;
  public String description;
  public int maxPoints;
  public String creationTime;
  public String key;

  public Assignment(String title, String id, String description, String creationTime, String courseID, int maxPoints, Key key) {
    this.title = title;
    this.id = id;
    this.description = description;
    this.maxPoints = maxPoints;
    this.creationTime = creationTime;
    this.key = KeyFactory.keyToString(key);
   }

  public String getKeyString() {
      return this.key;
  }

  public Key getKey() {
      return KeyFactory.stringToKey(this.key);
  }


}
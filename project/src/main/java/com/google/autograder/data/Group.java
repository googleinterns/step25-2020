package com.google.autograder.data;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/** Class containing group data. */
public class Group {

  public int score;
  public String questionKey;
  public String key;

  public Group(int score, String questionKey, Key key) {
    this.score = score;
    this.questionKey = questionKey;
    this.key = KeyFactory.keyToString(key);
  }

  public String getKeyString() {
      return this.key;
  }

  public Key getKey() {
      return KeyFactory.stringToKey(this.key);
  }

}
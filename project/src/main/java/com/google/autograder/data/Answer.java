package com.google.autograder.data;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/** Class containing answer data. */
public final class Answer {

  public String filePath;
  public String parsedAnswer;
  public int score;
  public String assignmentKey;
  public String questionKey;
  public String key;
  public String groupKey;

  public Answer(String filePath, String parsedAnswer, int score, String assignmentKey, String questionKey, Key key) {
    this.filePath = filePath;
    this.parsedAnswer = parsedAnswer;
    this.score = score;
    this.assignmentKey = assignmentKey;
    this.questionKey = questionKey;
    this.key = KeyFactory.keyToString(key);
    this.groupKey = "";
  }

  public String getKeyString() {
      return this.key;
  }

  public Key getKey() {
      return KeyFactory.stringToKey(this.key);
  }

  public void addGroup(Key newGroupKey) {
      this.groupKey = KeyFactory.keyToString(newGroupKey);
  }

  public String getGroupKey() {
      return this.groupKey;
  }

}
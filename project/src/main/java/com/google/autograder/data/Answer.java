package com.google.autograder.data;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/** Class containing answer data. */
public class Answer {
    
    public int score;
    public String key;
    public String filePath;
    public String groupKey;
    public String questionKey;
    public String parsedAnswer;
    public String assignmentKey;
    
    public Answer(String filePath, String parsedAnswer, int score, String assignmentKey, String questionKey, Key key) {
        this.key = KeyFactory.keyToString(key);
        this.assignmentKey = assignmentKey;
        this.parsedAnswer = parsedAnswer;
        this.questionKey = questionKey;
        this.filePath = filePath;
        this.groupKey = "";
        this.score = score;
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
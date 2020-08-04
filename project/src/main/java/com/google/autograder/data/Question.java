package com.google.autograder.data;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/** Class containing question data. */
public class Question {
    
    public int points;
    public String key;
    public String name;
    public String type;
    public String assignmentKey;
    
    public Question(String name, int points, String type, String assignmentKey, Key key) {
        this.key = KeyFactory.keyToString(key);
        this.assignmentKey = assignmentKey;
        this.points = points;
        this.name = name;
        this.type = type;
    }
    
    public String getKeyString() {
        return this.key;
    }
    
    public Key getKey() {
        return KeyFactory.stringToKey(this.key);
    }
    
}
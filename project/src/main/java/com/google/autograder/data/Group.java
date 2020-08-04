package com.google.autograder.data;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/** Class containing group data. */
public class Group {
    
    public int score;
    public String key;
    public String questionKey;
    
    public Group(int score, String questionKey, Key key) {
        this.key = KeyFactory.keyToString(key);
        this.questionKey = questionKey;
        this.score = score;
    }
    
    public String getKeyString() {
        return this.key;
    }
    
    public Key getKey() {
        return KeyFactory.stringToKey(this.key);
    }
    
}
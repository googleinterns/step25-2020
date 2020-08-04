package com.google.autograder.data;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/** Class containing assignment data. */
public final class Assignment {
    
    public String id;
    public String key;
    public String title;
    public int maxPoints;
    public String description;
    public String creationTime;
    
    public Assignment(String title, String id, String description, String creationTime, String courseID, int maxPoints, Key key) {
        this.key = KeyFactory.keyToString(key);
        this.creationTime = creationTime;
        this.description = description;
        this.maxPoints = maxPoints;
        this.title = title;
        this.id = id;
    }
    
    public String getKeyString() {
        return this.key;
    }
    
    public Key getKey() {
        return KeyFactory.stringToKey(this.key);
    }

}
package com.google.autograder.data;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

// Class containing assignment submission data.

public final class Submission {

    public String key;
    public String graded;
    public String userID;
    public String courseID;
    public String assignmentID;
    public String submissionID;
    public String assignmentKey;
    public String driveFilePreviewLink;
    public String driveFileDownloadLink;

    public Submission(String key, String graded, String userID, String courseID, String assignmentID, String submissionID, String assignmentKey, String driveFilePreviewLink, String driveFileDownloadLink) {
        this.driveFileDownloadLink = driveFileDownloadLink;
        this.driveFilePreviewLink = driveFilePreviewLink;
        this.assignmentKey = assignmentKey;
        this.submissionID = submissionID;
        this.assignmentID = assignmentID;
        this.courseID = courseID;
        this.userID = userID;
        this.graded = graded;
        this.key = key;
    }

    public String getKeyString() {
        return this.key;
    }
    
    public Key getKey() {
        return KeyFactory.stringToKey(this.key);
    }

}
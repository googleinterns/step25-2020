package com.google.autograder.data;

import java.util.List;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ArrayList;
import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.google.appengine.api.datastore.Key;
import com.google.autograder.data.UserHandler;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;

/** Class containing basic database functionalities. */
public final class Database {

    private static final DatastoreService DATA_STORE = DatastoreServiceFactory.getDatastoreService();
    private static final Gson GSON = new Gson();

    public static final List<String> ANSWER_GRADING_STATUSES = Arrays.asList("NOT_GRADED", "GRADED");

    public static void save(Entity entity) {
        DATA_STORE.put(entity);
    }

    public static Iterator<Entity> query(Query query) {
        return DATA_STORE.prepare(query).asIterable().iterator();
    }

    // Takes in a courses array in JSON format and then stores that courses data for the current user.

    public static void storeCoursesData(String coursesJSON) {
        String userEmail = UserHandler.getCurrentUserEmail();
        String userID = UserHandler.getCurrentUserID();

        Filter userEmailFilter = new FilterPredicate("userEmail", FilterOperator.EQUAL, userEmail);
        Query query = new Query("Course").setFilter(userEmailFilter);
        PreparedQuery results = DATA_STORE.prepare(query);

        // TODO: Compare Datastore contents with the courses data from Google Classroom

        for (Entity course : results.asIterable()) {
            DATA_STORE.delete(course.getKey());
        }

        Iterator coursesIterator = null;

        try {
            JSONObject coursesObject = (JSONObject) new JSONParser().parse(coursesJSON);
            JSONArray courses = (JSONArray) coursesObject.get("courses");
            coursesIterator = courses.iterator();
        } catch(ParseException exception) {
            System.out.println("\n\n" + "Error parsing JSON Course data from the Google Classroom API" + "\n\n");
        }

        if (coursesIterator != null) {
            while (coursesIterator.hasNext()) {
                JSONObject course = (JSONObject) coursesIterator.next();
                Entity courseEntity = new Entity("Course");

                courseEntity.setProperty("id", course.get("id"));
                courseEntity.setProperty("name", course.get("name"));
                courseEntity.setProperty("description", course.get("description"));
                courseEntity.setProperty("creationTime", course.get("creationTime"));
                courseEntity.setProperty("userEmail", userEmail);
                courseEntity.setProperty("userID", userID);

                DATA_STORE.put(courseEntity);
            }
        }        
    }

    // Retrives the current user's courses data as JSON.

    public static String getCoursesData() {
        String userEmail = UserHandler.getCurrentUserEmail();

        Filter userEmailFilter = new FilterPredicate("userEmail", FilterOperator.EQUAL, userEmail);
        Query query = new Query("Course").setFilter(userEmailFilter).addSort("creationTime", SortDirection.DESCENDING);
        PreparedQuery results = DATA_STORE.prepare(query);

        StringBuilder coursesData = new StringBuilder("{\"courses\": [");

        for (Entity course : results.asIterable()) {
            String name = (String) course.getProperty("name");
            String courseID = (String) course.getProperty("id");

            StringBuilder courseData = new StringBuilder("{");
            
            courseData.append("\"name\":" + "\"" + name + "\"");
            courseData.append(",");
            courseData.append("\"id\":" + "\"" + courseID + "\"");
            courseData.append("},");

            coursesData.append(courseData.toString());
        }

        coursesData = new StringBuilder(coursesData.toString().substring(0, coursesData.length() - 1));
        coursesData.append("]}");
        
        return coursesData.toString();
    }

    // Takes in a course work array in JSON format and then stores the assignments data for the current user.

    public static void storeAssignmentsData(String courseWorkJSON, String courseID) {        
        Filter courseIDFilter = new FilterPredicate("courseID", FilterOperator.EQUAL, courseID);
        Query query = new Query("Assignment").setFilter(courseIDFilter);
        PreparedQuery results = DATA_STORE.prepare(query);

        // TODO: Compare Datastore contents with the course work data from Google Classroom

        for (Entity assignment : results.asIterable()) {
            DATA_STORE.delete(assignment.getKey());
        }

        Iterator courseWorkIterator = null;

        try {
            JSONObject courseWorkObject = (JSONObject) new JSONParser().parse(courseWorkJSON); 
            JSONArray courseWork = (JSONArray) courseWorkObject.get("courseWork");
            courseWorkIterator = courseWork.iterator();
        } catch (ParseException exception) {
            System.out.println("\n\n" + "Error parsing JSON Course Work data from the Google Classroom API" + "\n\n");
        }

        if (courseWorkIterator != null) {
            while (courseWorkIterator.hasNext()) {
                JSONObject courseWorkTempObject = (JSONObject) courseWorkIterator.next();
                String courseWorkType = (String) courseWorkTempObject.get("workType");

                if (courseWorkType.equals("ASSIGNMENT")) {
                    JSONObject assignment = courseWorkTempObject;
                    Entity assignmentEntity = new Entity("Assignment");

                    assignmentEntity.setProperty("courseID", courseID);
                    assignmentEntity.setProperty("id", assignment.get("id"));
                    assignmentEntity.setProperty("title", assignment.get("title"));
                    assignmentEntity.setProperty("maxPoints", assignment.get("maxPoints"));
                    assignmentEntity.setProperty("description", assignment.get("description"));
                    assignmentEntity.setProperty("creationTime", assignment.get("creationTime"));

                    DATA_STORE.put(assignmentEntity);
                }
            }
        }
    }
    
    // Retrives the current user's assignments for a course data as JSON.

    public static String getAssignmentsData(String courseID) {
        Filter courseIDFilter = new FilterPredicate("courseID", FilterOperator.EQUAL, courseID);
        Query query = new Query("Assignment").setFilter(courseIDFilter).addSort("creationTime", SortDirection.DESCENDING);
        PreparedQuery results = DATA_STORE.prepare(query);

        StringBuilder assignmentsData = new StringBuilder("{\"courseWork\": [");

        for (Entity assignment : results.asIterable()) {
            StringBuilder assignmentData = new StringBuilder("{");
            
            assignmentData.append("\"title\":" + "\"" + assignment.getProperty("title") + "\"");
            assignmentData.append(",");
            assignmentData.append("\"id\":" + "\"" + assignment.getProperty("id") + "\"");
            assignmentData.append(",");
            assignmentData.append("\"courseId\":" + "\"" + courseID + "\"");
            assignmentData.append(",");
            assignmentData.append("\"description\":" + "\"" + assignment.getProperty("description") + "\"");
            assignmentData.append("},");

            assignmentsData.append(assignmentData);
        }

        assignmentsData = new StringBuilder(assignmentsData.substring(0, assignmentsData.length() - 1));
        assignmentsData.append("]}");
        
        return assignmentsData.toString();
    }

    // Create operations for all entities (with pk and fk restraints)
    // TODO: ensure new objects have diff main names
    
    public static void addAssignment(String name, int totalPoints) {
        Entity assignmentEntity = new Entity("Assignment");
        assignmentEntity.setProperty("name", name);
        assignmentEntity.setProperty("points", totalPoints);
        assignmentEntity.setProperty("status", "SAMPLE_PENDING");
        DATA_STORE.put(assignmentEntity);
    }

    public static void addQuestion(String questionName, String questionType, int questionPoints, String assignmentKey) {
        Entity questionEntity = new Entity("Question");
        questionEntity.setProperty("name", questionName);
        questionEntity.setProperty("type", questionType);
        questionEntity.setProperty("points", questionPoints);
        questionEntity.setProperty("assignmentKey", assignmentKey);
        DATA_STORE.put(questionEntity);
    }

    public static void addSubmission(Entity assignmentEntity) {
        Entity submissionEntity = new Entity("Submission");
        submissionEntity.setProperty("graded", "NOT_GRADED");
        submissionEntity.setProperty("assignmentKey", assignmentEntity.getKey());
        DATA_STORE.put(submissionEntity);
    }

    public static void addLocation(Entity questionEntity, int topLeft, int bottomRight) {
        Entity locationEntity = new Entity("Location");
        locationEntity.setProperty("topLeft", topLeft);
        locationEntity.setProperty("bottomRight", bottomRight);
        locationEntity.setProperty("questionKey", questionEntity.getKey());
        DATA_STORE.put(locationEntity);
    }

    public static void addAnswer(Entity questionEntity, Entity submissionEntity, String parsedAnswer, int points) {
        Entity answerEntity = new Entity("Answer");
        answerEntity.setProperty("parsedAnswer", parsedAnswer);
        answerEntity.setProperty("points", points);
        answerEntity.setProperty("graded", "NOT_GRADED");
        answerEntity.setProperty("questionKey", questionEntity.getKey());
        answerEntity.setProperty("submissionKey", submissionEntity.getKey());
        DATA_STORE.put(answerEntity);
    }

    public static void addGroup(Entity questionEntity) {
        Entity groupEntity = new Entity("Group");
        groupEntity.setProperty("questionKey", questionEntity.getKey());
        DATA_STORE.put(groupEntity);
    }

    public static void updateGroupForAnswer(Entity answerEntity, Entity groupEntity) {
        answerEntity.setProperty("groupKey", groupEntity.getKey());
        DATA_STORE.put(answerEntity);
    }
    
    public static void updateGradedForAnswer(Entity answerEntity, String status) {
        if (ANSWER_GRADING_STATUSES.contains(status)) {
            answerEntity.setProperty("graded", status);
            DATA_STORE.put(answerEntity);
        }
    }

    public static void updateScoreForAnswer(Entity groupEntity, int score) {
        groupEntity.setProperty("score", score);
        DATA_STORE.put(groupEntity);
    }

    public static String getAllAssignmentsJSON() {
        Query query = new Query("Assignment");
        PreparedQuery results = DATA_STORE.prepare(query);
        List<Assignment> assignments = new ArrayList<>();
        
        for (Entity entity : results.asIterable()) {
            String name = (String) entity.getProperty("name");
            Long pointsLong = (Long) entity.getProperty("points");
            int points = Math.toIntExact(pointsLong);
            String status = (String) entity.getProperty("status");
            Assignment currentAssignment = new Assignment(name, points, status, entity.getKey());
            assignments.add(currentAssignment);
        }

        return GSON.toJson(assignments);
    }

    public static String getAllQuestionsJSON(String key) {
        // query all questions with this key at assignment_id key
        if (key != null) {
            Filter propertyFilter = new FilterPredicate("assignmentKey", FilterOperator.EQUAL, key);
            Query query = new Query("Question").setFilter(propertyFilter);
            PreparedQuery results = DATA_STORE.prepare(query);
            List<Question> questions = new ArrayList<>();
            
            for (Entity entity : results.asIterable()) {
                String name = (String) entity.getProperty("name");
                Long pointsLong = (Long) entity.getProperty("points");
                int points = Math.toIntExact(pointsLong);
                String status = (String) entity.getProperty("status");
                String assignmentKey = (String) entity.getProperty("assignmentKey");
                Question currentQuestion = new Question(name, points, status, assignmentKey);
                questions.add(currentQuestion);
            }
            
            return GSON.toJson(questions);
        }
        
        return GSON.toJson("");
    }

    // Get all submissions for an assignment

    // Get all answers for a question

    // Set/edit location for a question

    // Get all groups for a question

    // Change answer's group
}
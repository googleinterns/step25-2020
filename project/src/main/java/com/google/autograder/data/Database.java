package com.google.autograder.data;

import java.util.List;
import java.util.Arrays;
import java.lang.Iterable;
import java.util.Iterator;
import java.util.ArrayList;
import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
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

    public static void delete(Entity entity) {
        DATA_STORE.delete(entity.getKey());
    }

    public static Iterable<Entity> query(Query query) {
        return DATA_STORE.prepare(query).asIterable();
    }

    // Takes in a courses array in JSON format and then stores that courses data for the current user.

    public static void storeCoursesData(String coursesJSON) {
        String userEmail = UserHandler.getCurrentUserEmail();
        String userID = UserHandler.getCurrentUserID();

        Filter userEmailFilter = new FilterPredicate("userEmail", FilterOperator.EQUAL, userEmail);
        Query coursesQuery = new Query("Course").setFilter(userEmailFilter);

        // TODO: Compare Datastore contents with the courses data from Google Classroom

        for (Entity course : query(coursesQuery)) {
            delete(course);
        }

        Iterator coursesIterator;

        try {
            JSONObject coursesObject = (JSONObject) new JSONParser().parse(coursesJSON);
            JSONArray courses = (JSONArray) coursesObject.get("courses");
            coursesIterator = courses.iterator();
        } catch(ParseException exception) {
            System.out.println("\n\n" + "Error parsing JSON Course data from the Google Classroom API" + "\n\n");
            return;
        }
        
        while (coursesIterator.hasNext()) {
            JSONObject course = (JSONObject) coursesIterator.next();
            Entity courseEntity = new Entity("Course");

            courseEntity.setProperty("id", course.get("id"));
            courseEntity.setProperty("name", course.get("name"));
            courseEntity.setProperty("description", course.get("description"));
            courseEntity.setProperty("creationTime", course.get("creationTime"));
            courseEntity.setProperty("userEmail", userEmail);
            courseEntity.setProperty("userID", userID);

            save(courseEntity);
        }        
    }

    // Retrives the current user's courses data as JSON.

    public static String getCoursesData() {
        String userEmail = UserHandler.getCurrentUserEmail();

        Filter userEmailFilter = new FilterPredicate("userEmail", FilterOperator.EQUAL, userEmail);
        Query coursesQuery = new Query("Course").setFilter(userEmailFilter).addSort("creationTime", SortDirection.DESCENDING);

        StringBuilder coursesData = new StringBuilder("{\"courses\": [");

        for (Entity course : query(coursesQuery)) {
            String name = (String) course.getProperty("name");
            String courseID = (String) course.getProperty("id");

            coursesData.append("{");
            coursesData.append("\"name\":" + "\"" + name + "\"");
            coursesData.append(",");
            coursesData.append("\"id\":" + "\"" + courseID + "\"");
            coursesData.append("},");
        }
        
        coursesData = new StringBuilder(coursesData.deleteCharAt(coursesData.length() - 1));
        coursesData.append("]}");
        
        return coursesData.toString();
    }

    // Takes in a course work array in JSON format and then stores the assignments data for the current user.

    public static void storeAssignmentsData(String courseWorkJSON, String courseID) {        
        Filter courseIDFilter = new FilterPredicate("courseID", FilterOperator.EQUAL, courseID);
        Query assignmentsQuery = new Query("Assignment").setFilter(courseIDFilter);

        // TODO: Compare Datastore contents with the course work data from Google Classroom

        for (Entity assignment : query(assignmentsQuery)) {
            delete(assignment);
        }

        Iterator courseWorkIterator;

        try {
            JSONObject courseWorkObject = (JSONObject) new JSONParser().parse(courseWorkJSON); 
            JSONArray courseWork = (JSONArray) courseWorkObject.get("courseWork");
            courseWorkIterator = courseWork.iterator();
        } catch (ParseException exception) {
            System.out.println("\n\n" + "Error parsing JSON Course Work data from the Google Classroom API" + "\n\n");
            return;
        }

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

                save(assignmentEntity);
            }
        }
    }
    
    // Retrives the current user's assignments for a course data as JSON.

    public static String getAssignmentsData(String courseID) {
        Filter courseIDFilter = new FilterPredicate("courseID", FilterOperator.EQUAL, courseID);
        Query assignmentsQuery = new Query("Assignment").setFilter(courseIDFilter).addSort("creationTime", SortDirection.DESCENDING);

        StringBuilder assignmentsData = new StringBuilder("{\"courseWork\": [");

        List<Assignment> assignments = new ArrayList<>();
        for (Entity assignment : query(assignmentsQuery)) {
            String title = assignment.getProperty("title");
            String id = assignment.getProperty("id");
            String description = assignment.getProperty("description");
            String creationTime = assignment.getProperty("creationTime");
            String courseID = assignment.getProperty("courseID");
            int maxPoints = Math.toIntExact( (Long) assignment.getProperty("maxPoints"));
            Key key = assignment.getKey();
            currAssignment = new Assignment(title, id, description, creationTime, courseID, maxPoints, key)
            assignments.add(currAssignment);
        }

        String json = new Gson().toJson(assignments);
        return json;
    }

    public static void addQuestion(String questionName, String questionType, int questionPoints, String assignmentKey) {
        Entity questionEntity = new Entity("Question");
        questionEntity.setProperty("name", questionName);
        questionEntity.setProperty("type", questionType);
        questionEntity.setProperty("points", questionPoints);
        questionEntity.setProperty("assignmentKey", assignmentKey);
        save(questionEntity);
    }

    public static void addSubmission(Entity assignmentEntity) {
        Entity submissionEntity = new Entity("Submission");
        submissionEntity.setProperty("graded", "NOT_GRADED");
        submissionEntity.setProperty("assignmentKey", assignmentEntity.getKey());
        save(submissionEntity);
    }

    public static void addLocation(Entity questionEntity, int topLeft, int bottomRight) {
        Entity locationEntity = new Entity("Location");
        locationEntity.setProperty("topLeft", topLeft);
        locationEntity.setProperty("bottomRight", bottomRight);
        locationEntity.setProperty("questionKey", questionEntity.getKey());
        save(locationEntity);
    }

    public static void addAnswer(Entity questionEntity, Entity submissionEntity, String parsedAnswer, int points) {
        Entity answerEntity = new Entity("Answer");
        answerEntity.setProperty("parsedAnswer", parsedAnswer);
        answerEntity.setProperty("points", points);
        answerEntity.setProperty("graded", "NOT_GRADED");
        answerEntity.setProperty("questionKey", questionEntity.getKey());
        answerEntity.setProperty("submissionKey", submissionEntity.getKey());
        save(answerEntity);
    }

    public static void addGroup(Entity questionEntity) {
        Entity groupEntity = new Entity("Group");
        groupEntity.setProperty("questionKey", questionEntity.getKey());
        save(groupEntity);
    }

    public static void updateGroupForAnswer(Entity answerEntity, Entity groupEntity) {
        answerEntity.setProperty("groupKey", groupEntity.getKey());
        save(answerEntity);
    }
    
    public static void updateGradedForAnswer(Entity answerEntity, String status) {
        if (ANSWER_GRADING_STATUSES.contains(status)) {
            answerEntity.setProperty("graded", status);
            save(answerEntity);
        }
    }

    public static void updateScoreForAnswer(Entity groupEntity, int score) {
        groupEntity.setProperty("score", score);
        save(groupEntity);
    }

    public static String getAllQuestionsJSON(String key) {
        // query all questions with this key at assignment_id key
        if (key != null) {
            Filter propertyFilter = new FilterPredicate("assignmentKey", FilterOperator.EQUAL, key);
            Query questionsQuery = new Query("Question").setFilter(propertyFilter);
            List<Question> questions = new ArrayList<>();
            
            for (Entity entity : query(questionsQuery)) {
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

}
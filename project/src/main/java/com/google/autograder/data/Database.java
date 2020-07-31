package com.google.autograder.data;

import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.lang.Iterable;
import java.util.ArrayList;
import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.google.autograder.data.Submission;
import org.json.simple.parser.ParseException;
import com.google.appengine.api.datastore.Key;
import com.google.autograder.data.UserHandler;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
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

    public static void storeAssignmentSubmissionsData(String submissionsJSON, String courseID, String assignmentID, String assignmentKey) {
        Filter courseIDFilter = new FilterPredicate("courseID", FilterOperator.EQUAL, courseID);
        Filter assignmentIDFilter = new FilterPredicate("assignmentID", FilterOperator.EQUAL, assignmentID);
        Filter assignmentKeyFilter = new FilterPredicate("assignmentKey", FilterOperator.EQUAL, assignmentKey);
        Query submissionsQuery = new Query("Submission").setFilter(courseIDFilter).setFilter(assignmentIDFilter).setFilter(assignmentKeyFilter);

        // TODO: Compare Datastore contents with the submissions data from Google Classroom

        for (Entity submission : query(submissionsQuery)) {
            delete(submission);
        }

        JSONObject submissionsJSONObject = null;
        
        try {
            submissionsJSONObject = (JSONObject) new JSONParser().parse(submissionsJSON);
        } catch (Exception e) {
            return;
        }

        JSONArray studentSubmissionsArray = (JSONArray) submissionsJSONObject.get("studentSubmissions");

        if (studentSubmissionsArray.iterator() == null) {
            return;
        }

        Iterator studentSubmissionsIterator = studentSubmissionsArray.iterator();
        String drivePreviewLink = "https://drive.google.com/file/d/{fileId}/preview";
        String driveDownloadLink = "https://drive.google.com/uc?export=download&id={fileId}";

        while (studentSubmissionsIterator.hasNext()) {
            JSONObject studentSubmission = (JSONObject) studentSubmissionsIterator.next();
            JSONObject assignmentSubmission = (JSONObject) studentSubmission.get("assignmentSubmission");
            JSONArray attachmentsArray = (JSONArray) assignmentSubmission.get("attachments");
            String studentUserID = studentSubmission.get("userId").toString();
            String submissionID = studentSubmission.get("id").toString();

            if (attachmentsArray == null) {
                continue;
            }

            Iterator attachmentsIterator = attachmentsArray.iterator();

            if (!attachmentsIterator.hasNext()) {
                continue;
            }
                
            JSONObject attachment = (JSONObject) attachmentsIterator.next();
            JSONObject driveFileObject = (JSONObject) attachment.get("driveFile");

            String driveFileID = driveFileObject.get("id").toString();
            String driveFilePreviewLink = drivePreviewLink.replace("{fileId}", driveFileID);
            String driveFileDownloadLink = driveDownloadLink.replace("{fileId}", driveFileID);

            Submission submission = new Submission(null, null, studentUserID, courseID, assignmentID, submissionID, assignmentKey, driveFilePreviewLink, driveFileDownloadLink);

            addSubmission(submission);
        }
    }

    public static String getAssignmentSubmissionsData(String courseID, String assignmentID, String assignmentKey) {
        Filter courseIDFilter = new FilterPredicate("courseID", FilterOperator.EQUAL, courseID);
        Filter assignmentIDFilter = new FilterPredicate("assignmentID", FilterOperator.EQUAL, assignmentID);
        Filter assignmentKeyFilter = new FilterPredicate("assignmentKey", FilterOperator.EQUAL, assignmentKey);
        Query submissionsQuery = new Query("Submission").setFilter(courseIDFilter).setFilter(assignmentIDFilter).setFilter(assignmentKeyFilter);

        List<Submission> submissions = new ArrayList<>();

        for (Entity submissionEntity : query(submissionsQuery)) {
            Submission submission = new Submission(
                submissionEntity.getKey().toString(),
                submissionEntity.getProperty("graded").toString(),
                submissionEntity.getProperty("userID").toString(),
                submissionEntity.getProperty("courseID").toString(),
                submissionEntity.getProperty("assignmentID").toString(),
                submissionEntity.getProperty("submissionID").toString(),
                submissionEntity.getProperty("assignmentKey").toString(),
                submissionEntity.getProperty("driveFilePreviewLink").toString(),
                submissionEntity.getProperty("driveFileDownloadLink").toString()
            );

            submissions.add(submission);
        }

        return new Gson().toJson(submissions);
    }
  
  public static Entity addQuestion(String questionName, String questionType, int questionPoints, String assignmentKey) {
      Entity questionEntity = new Entity("Question");
      questionEntity.setProperty("name", questionName);
      questionEntity.setProperty("type", questionType);
      questionEntity.setProperty("points", questionPoints);
      questionEntity.setProperty("assignmentKey", assignmentKey);
      save(questionEntity);
      System.out.println("question " + questionName + " has been added");
      return questionEntity;
  }
  
  
  // sample add Answer, will change in future
  public static void addAnswer(String filePath, String parsedAnswer, int score, String assignmentKey, String questionKey) {
      Entity answerEntity = new Entity("Answer");
      answerEntity.setProperty("parsedAnswer", parsedAnswer);
      answerEntity.setProperty("score", score);
      answerEntity.setProperty("filePath", filePath);
      answerEntity.setProperty("questionKey", questionKey);
      answerEntity.setProperty("assignmentKey", assignmentKey);
      save(answerEntity);
  }
  
  public static Entity addGroup(int score, String questionKey) {
      Entity groupEntity = new Entity("Group");
      groupEntity.setProperty("score", score);
      groupEntity.setProperty("questionKey", questionKey);
      save(groupEntity);
      return groupEntity;
  }
  
  //unused right now
  public static void updateGroupForAnswer(Entity answerEntity, Entity groupEntity) {
      answerEntity.setProperty("groupKey", groupEntity.getKey());
      save(answerEntity);
  }
  
  //unused right now
  public static void updateScoreForAnswer(Entity groupEntity, int score) {
      groupEntity.setProperty("score", score);
      save(groupEntity);
  }
  
  public static String getAllQuestionsJSON(String key) {
    // query all questions with this key at assignment_id key
    try {
        Filter propertyFilter = new FilterPredicate("assignmentKey", FilterOperator.EQUAL, key);
        Query query = new Query("Question").setFilter(propertyFilter);
        List<Question> questions = new ArrayList<>();
        for (Entity entity : query(query)) {
            String name = (String) entity.getProperty("name");
            Long pointsLong = (Long) entity.getProperty("points");
            int points = Math.toIntExact(pointsLong);
            String status = (String) entity.getProperty("status");
            String assignmentKey = (String) entity.getProperty("assignmentKey");
            Key questionKey = entity.getKey();
            Question currQuestion = new Question(name, points, status, assignmentKey, questionKey);
            questions.add(currQuestion); }
        String json = new Gson().toJson(questions);
        System.out.println(json);
        return json;   
        
    }
    catch (Exception e) {
        System.out.println("assignment wasn't found");
        return "error";
    }
  }
    

    // Retrives the current user's assignments for a course data as JSON.

    public static String getAssignmentsData (String courseID) {
        Filter courseIDFilter = new FilterPredicate("courseID", FilterOperator.EQUAL, courseID);
        Query assignmentsQuery = new Query("Assignment").setFilter(courseIDFilter).addSort("creationTime", SortDirection.DESCENDING);

        List<Assignment> assignments = new ArrayList<>();
        for (Entity assignment : query(assignmentsQuery)) {
            String title = (String) assignment.getProperty("title");
            String id = (String) assignment.getProperty("id");
            String description = (String) assignment.getProperty("description");
            String creationTime = (String) assignment.getProperty("creationTime");
            int maxPoints = Math.toIntExact( (Long) assignment.getProperty("maxPoints"));
            Key key = assignment.getKey();
            Assignment currAssignment = new Assignment(title, id, description, creationTime, courseID, maxPoints, key);
            assignments.add(currAssignment);
        }

        String json = new Gson().toJson(assignments);
        return json;
    }

  public static String getAllAnswersJSON(String assignmentKey, String questionKey) {
    // query all answers with these keys
    try {
        Filter propertyFilterAssignment = new FilterPredicate("assignmentKey", FilterOperator.EQUAL, assignmentKey);
        Filter propertyFilterQuestion = new FilterPredicate("questionKey", FilterOperator.EQUAL, questionKey);
        CompositeFilter propertyFilter = CompositeFilterOperator.and(propertyFilterAssignment, propertyFilterQuestion);
        Query query = new Query("Answer").setFilter(propertyFilter);
        List<Answer> answers = new ArrayList<>();
        for (Entity entity : query(query)) {
            String filePath = (String) entity.getProperty("filePath");
            String parsedAnswer = (String) entity.getProperty("parsedAnswer");
            Long scoreLong = (Long) entity.getProperty("score");
            int score = Math.toIntExact(scoreLong);
            Key answerKey = entity.getKey();
            Answer currAnswer = new Answer(filePath, parsedAnswer, score, assignmentKey, questionKey, answerKey);
            answers.add(currAnswer);
        }
        String json = new Gson().toJson(answers);
        return json;
    }
    catch (Exception e) {
        System.out.println("answers weren't found");
        e.printStackTrace(System.out);
        return "error";
    }
  }

  public static String createGroups(String assignmentKey, String questionKey) {
    Filter propertyFilterAssignment = new FilterPredicate("assignmentKey", FilterOperator.EQUAL, assignmentKey);
    Filter propertyFilterQuestion = new FilterPredicate("questionKey", FilterOperator.EQUAL, questionKey);
    CompositeFilter propertyFilter = CompositeFilterOperator.and(propertyFilterAssignment, propertyFilterQuestion);
    Query query = new Query("Answer").setFilter(propertyFilter);
    HashMap<String, ArrayList<Entity>> parsedAnswerGroups = new HashMap<String, ArrayList<Entity>>();
    // loop through answers and see if parsed answer already exists. if so, add answer entity to arraylist. if not exists, init new array and add
    for (Entity answer : query(query)) {
        String parsedAnswer = (String) answer.getProperty("parsedAnswer");
        if (parsedAnswerGroups.get(parsedAnswer) == null) {
            ArrayList<Entity> temp = new ArrayList<Entity>();
            temp.add(answer);
            parsedAnswerGroups.put(parsedAnswer, temp);
        }
        else {
            parsedAnswerGroups.get(parsedAnswer).add(answer);
        }
    }
    HashMap<Group, ArrayList<Answer>> groups = new HashMap<Group, ArrayList<Answer>>();
    for (HashMap.Entry<String, ArrayList<Entity>> entry : parsedAnswerGroups.entrySet()) {
        Entity groupEntity = addGroup((int) 0, questionKey);
        int score = (int) groupEntity.getProperty("score");
        Key groupKey = groupEntity.getKey();
        Group group = new Group(score, questionKey, groupKey);
        ArrayList<Answer> answerList = new ArrayList<Answer>();
        // created group entity and class @ this point
        // now we loop through all the answers and map them to a group
        for (Entity answer : entry.getValue()) {
            String filePath = (String) answer.getProperty("filePath");
            String parsedAnswer = (String) answer.getProperty("parsedAnswer");
            Long answerScoreLong = (Long) answer.getProperty("score");
            int answerScore = Math.toIntExact(answerScoreLong);
            Key answerKey = answer.getKey();
            Answer currAnswer = new Answer(filePath, parsedAnswer, answerScore, assignmentKey, questionKey, answerKey);
            currAnswer.addGroup(groupKey);
            System.out.println(currAnswer.getGroupKey());
            answer.setProperty("groupKey", currAnswer.getGroupKey());
            save(answer);
            answerList.add(currAnswer);
        }
        groups.put(group, answerList);
    }
    String json = new Gson().toJson(groups);
    System.out.println(json);
    return json;
  }

    public static void addSubmission(Submission submission) {
        Entity submissionEntity = new Entity("Submission");
        submissionEntity.setProperty("graded", "NOT_GRADED");
        submissionEntity.setProperty("userID", submission.userID);
        submissionEntity.setProperty("courseID", submission.courseID);
        submissionEntity.setProperty("assignmentID", submission.assignmentID);
        submissionEntity.setProperty("submissionID", submission.submissionID);
        submissionEntity.setProperty("assignmentKey", submission.assignmentKey);
        submissionEntity.setProperty("driveFilePreviewLink", submission.driveFilePreviewLink);
        submissionEntity.setProperty("driveFileDownloadLink", submission.driveFileDownloadLink);
        save(submissionEntity);
    }

    public static void addLocation(Entity questionEntity, int leftXCoord, int topYCoord, int rightXCoord, int lowerYCoord) {
        Entity locationEntity = new Entity("Location");
        locationEntity.setProperty("leftXCoord", leftXCoord);
        locationEntity.setProperty("topYCoord", topYCoord);
        locationEntity.setProperty("rightXCoord", rightXCoord);
        locationEntity.setProperty("lowerYCoord", lowerYCoord);
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
    
    public static void updateGradedForAnswer(Entity answerEntity, String status) {
        if (ANSWER_GRADING_STATUSES.contains(status)) {
            answerEntity.setProperty("graded", status);
            save(answerEntity);
        }
    }

/** 
 * this function takes the assignment Key and answer group key as parameters
 * returns list of blobKeys to submissions that fit in specified answer group for specific question
 */ 
  public String blobkeysFromAnswerGroupJson(String assignKey, String groupKey) {

    // query answer entities filtered only in group from parameter
    Filter groupFilter = new FilterPredicate("groupKey", FilterOperator.EQUAL, groupKey);
    Query answersQuery = new Query("Answer").setFilter(groupFilter);    
    
    // generate list of submissionKeys for the group
    List<String> submissionKeys = new ArrayList<>();
    for (Entity entity : query(answersQuery)) {
        String submissionKey = (String) entity.getProperty("submissionKey");
        submissionKeys.add(submissionKey);
    }  

    // query submissions filtered to the assignment
    Query submissionsQuery = new Query("Submission");

    // iterate through submissions. if the submission's key is in the list of submissionKeys from the answer bucket,
    // add the submission's blobKet to blobKeyMap (submissionKey, blobKey)
    Map<Key, String> blobKeyMap = new HashMap<>();
    for (Entity submissionEntity : query(submissionsQuery)) {
        if (submissionKeys.contains(submissionEntity.getKey())) {
            String blobKey = (String) submissionEntity.getProperty("blobKey");
            Key submissionKey = submissionEntity.getKey();
            blobKeyMap.put(submissionKey, blobKey);
        }
    }

    String blobKeyJson = new Gson().toJson(blobKeyMap);
    return blobKeyJson;
  }

  //get all submissions for an assignment

    // Get all answers for a question

    // Set/edit location for a question

    // Get all groups for a question

    // Change answer's group
}


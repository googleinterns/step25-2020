package com.google.autograder.servlets;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import java.lang.Iterable;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.StringWriter;
import org.json.simple.JSONObject;
import com.google.autograder.data.Database;
import javax.servlet.http.HttpServletRequest;
import com.google.appengine.api.datastore.Key;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.autograder.servlets.GetAssignmentDetailsServlet;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;

public final class GetAssignmentDetailsServletTest {
    
    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
    
    @Before
    public void setUp() {
        helper.setUp();
    }
    
    @After
    public void tearDown() {
        helper.tearDown();
    }

    @Test
    public void getAssignmentDetails() throws IOException {
        String title = "Title";
        String courseID = "999";
        String assignmentID = "999";
        String description = "Description";

        Entity testAssignment = new Entity("Assignment");
        testAssignment.setProperty("title", title);
        testAssignment.setProperty("id", assignmentID);
        testAssignment.setProperty("courseID", courseID);
        testAssignment.setProperty("description", description);

        Database.save(testAssignment);

        HttpServletRequest request = mock(HttpServletRequest.class);       
        HttpServletResponse response = mock(HttpServletResponse.class); 

        when(request.getParameter("courseID")).thenReturn(String.valueOf(courseID));
        when(request.getParameter("assignmentID")).thenReturn(String.valueOf(assignmentID));

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(writer);

        new GetAssignmentDetailsServlet().doGet(request, response);

        verify(request, atLeast(1)).getParameter("courseID");
        verify(request, atLeast(1)).getParameter("assignmentID");

        JSONObject assignmentDetailsObject = new JSONObject();
        JSONObject assignmentObject = new JSONObject();
        
        assignmentObject.put("title", title);
        assignmentObject.put("description", description);
        assignmentDetailsObject.put("assignment", assignmentObject);

        writer.flush();

        assertTrue(stringWriter.toString().contains(assignmentDetailsObject.toString()));
    }

}
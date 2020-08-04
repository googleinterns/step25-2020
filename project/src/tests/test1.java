import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;

import com.google.sps.servlets.FavoriteRecipeServlet;
import org.junit.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;

import com.google.common.collect.ImmutableMap;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;

package com.google.sps;

@RunWith(MockitoJUnitRunner.class)
public final class FavoriteServletTest {
  // helper to mimic user authentication
  private final LocalServiceTestHelper helper =
    new LocalServiceTestHelper(new LocalUserServiceTestConfig()).setEnvAuthDomain("gmail.com")
      .setEnvEmail("test@gmail.com")
      .setEnvAttributes(ImmutableMap.of("com.google.appengine.api.users.UserService.user_id_key", "123"));


    @Mock private FavoriteRecipeServlet servlet;
    @Mock private HttpServletRequest request;
  
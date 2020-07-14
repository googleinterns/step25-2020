package com.google.autograder.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public final class Services {

    public static final UserService USER_SERVICE = UserServiceFactory.getUserService();

}

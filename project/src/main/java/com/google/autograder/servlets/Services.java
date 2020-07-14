package com.google.autograder.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

public final class Services {

    public static final UserService USER_SERVICE = UserServiceFactory.getUserService();
    public static final DatastoreService DATA_STORE = DatastoreServiceFactory.getDatastoreService();

}

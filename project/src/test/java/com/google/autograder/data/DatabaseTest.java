package com.google.autograder.data;

import static org.junit.Assert.assertEquals;
import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import java.lang.Iterable;
import com.google.autograder.data.Database;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;

public final class DatabaseTest {
    
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
    public void saveASingleEntity() {
        int entityCountBeforeSaving = 0;
        int entityCountAfterSaving = 0;

        Iterable<Entity> before = Database.query(new Query("Entity"));

        for (Entity entity : before) {
            entityCountBeforeSaving++;
        }

        assertEquals(0, entityCountBeforeSaving);

        Database.save(new Entity("Entity"));

        Iterable<Entity> after = Database.query(new Query("Entity"));

        for (Entity entity : after) {
            entityCountAfterSaving++;
        }

        assertEquals(1, entityCountAfterSaving);
    }

    @Test
    public void deleteASingleEntity() {
        int entityCountAfterDeleting = 0;
        int entityCountBeforeSaving = 0;
        int entityCountAfterSaving = 0;

        Iterable<Entity> beforeSaving = Database.query(new Query("Entity"));

        for (Entity entity : beforeSaving) {
            entityCountBeforeSaving++;
        }

        assertEquals(0, entityCountBeforeSaving);

        Database.save(new Entity("Entity"));

        Iterable<Entity> afterSaving = Database.query(new Query("Entity"));

        for (Entity entity : afterSaving) {
            entityCountAfterSaving++;
        }

        assertEquals(1, entityCountAfterSaving);

        Entity tempEntity = Database.query(new Query("Entity")).iterator().next();

        Database.delete(tempEntity);

        Iterable<Entity> afterDeleting = Database.query(new Query("Entity"));

        for (Entity entity : afterDeleting) {
            entityCountAfterDeleting++;
        }

        assertEquals(0, entityCountAfterDeleting);
    }

}
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

    private void assertNumberOfEntities(int numberOfEntities, Iterable<Entity> entities) {
        int count = 0;

        for (Entity entity : entities) {
            count++;
        }

        assertEquals(count, numberOfEntities);
    }

    @Test
    public void saveASingleEntity() {
        Iterable<Entity> before = Database.query(new Query("Entity"));

        assertNumberOfEntities(0, before);

        Database.save(new Entity("Entity"));

        Iterable<Entity> after = Database.query(new Query("Entity"));

        assertNumberOfEntities(1, after);
    }

    @Test
    public void deleteASingleEntity() {
        Iterable<Entity> beforeSaving = Database.query(new Query("Entity"));

        assertNumberOfEntities(0, beforeSaving);

        Database.save(new Entity("Entity"));

        Iterable<Entity> afterSaving = Database.query(new Query("Entity"));

        assertNumberOfEntities(1, afterSaving);

        Entity tempEntity = Database.query(new Query("Entity")).iterator().next();

        Database.delete(tempEntity);

        Iterable<Entity> afterDeleting = Database.query(new Query("Entity"));

        assertNumberOfEntities(0, afterDeleting);
    }

}
package ru.elite.store.jpa;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import ru.elite.store.jpa.entity.SimpleGalaxy;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPATest extends Assert {
    protected static EntityManagerFactory EMF;
    protected static SimpleGalaxy DEFAULT_GALAXY;


    @BeforeClass
    public static void setUpClass() throws Exception {
        EMF = Persistence.createEntityManagerFactory("ru.elite.JPATestUnit");
        DEFAULT_GALAXY = new SimpleGalaxy(EMF);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        EMF.close();
    }
}

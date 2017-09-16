package org.apache.apr;

import org.apache.tomcat.jni.Library;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LibraryTest {

    @Before
    public void setUp() throws Exception {
        String librarySearchPath = System.getProperty("java.library.path");
        System.out.printf("JVM is searching native libraries in: %s%n", librarySearchPath);

        boolean apr = Library.initialize(null);
        Assert.assertTrue(apr);
    }

    @Test
    public void test() throws Exception {
        Assert.assertNotNull(Library.aprVersionString());
        Assert.assertNotNull(Library.versionString());
    }
}

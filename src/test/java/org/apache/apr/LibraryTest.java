package org.apache.apr;

import org.apache.tomcat.jni.Library;
import org.apache.tomcat.jni.Stdlib;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LibraryTest {

    @Before
    public void setUp() throws Exception {
        String nativeLibPath = System.getProperty("java.library.path");
        System.out.printf("JVM is searching native libraries in: %s%n", nativeLibPath);

        boolean apr = Library.initialize(null);
        if (!apr) {
            throw new IllegalStateException("Fail to initialize Apache Tomcat Native library");
        }
    }

    @Test
    public void test() throws Exception {
        Assert.assertNotNull(Library.aprVersionString());
        Assert.assertNotNull(Library.versionString());

        System.out.printf("Process PID=%d%n", Stdlib.getpid());
        System.out.printf("Parent  PID=%d%n", Stdlib.getppid());
    }
}

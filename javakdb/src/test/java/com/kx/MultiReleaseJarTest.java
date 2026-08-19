package com.kx;

import java.net.URL;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;

/**
 * Verifies that the Java 9+ test pass is running against the packaged
 * multi-release jar rather than target/classes.
 *
 * <p>The test is skipped during the normal Surefire run. The Java 9+
 * Failsafe execution enables it with -Djavakdb.verifyMultiReleaseJar=true.</p>
 */
public class MultiReleaseJarTest {
    @Test
    public void testJava9ImplementationLoadedFromPackagedJar() {
        Assume.assumeTrue(Boolean.getBoolean("javakdb.verifyMultiReleaseJar"));

        URL cResource = c.class.getResource("c.class");
        Assert.assertNotNull("Could not locate com/kx/c.class", cResource);

        String cLocation = cResource.toExternalForm();
        Assert.assertTrue(
            "c.class was not loaded from the packaged javakdb jar: " + cLocation,
            cLocation.startsWith("jar:")
                && cLocation.contains(".jar!/com/kx/c.class"));

        URL accessResource = ByteArrayAccess.class.getResource("ByteArrayAccess.class");
        Assert.assertNotNull(
            "Could not locate com/kx/ByteArrayAccess.class", accessResource);

        String accessLocation = accessResource.toExternalForm();
        Assert.assertTrue(
            "Java 9+ did not load the versioned ByteArrayAccess class: "
                + accessLocation,
            accessLocation.startsWith("jar:")
                && accessLocation.contains(
                    "!/META-INF/versions/9/com/kx/ByteArrayAccess.class"));
    }
}

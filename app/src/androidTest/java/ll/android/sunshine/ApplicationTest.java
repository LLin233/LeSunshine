package ll.android.sunshine;

import android.test.suitebuilder.TestSuiteBuilder;

import junit.framework.Test;

/*
 * Create TestSuite for app
 */
public class ApplicationTest {
    public static Test suite() {
        return new TestSuiteBuilder(ApplicationTest.class).includeAllPackagesUnderHere().build();
    }

    public ApplicationTest() {
        super();
    }
}
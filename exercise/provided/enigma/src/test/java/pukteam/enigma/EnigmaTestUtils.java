package pukteam.enigma;

import org.junit.Assert;

public class EnigmaTestUtils {

    public static void ensureExceptionIsThrown(Runnable r, String failureMessage) {
        try {
            r.run();
            Assert.fail(failureMessage);
        } catch (Exception ignored) {
            System.out.println(ignored.getMessage());
        }
    }

}

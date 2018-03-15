package pukteam.enigma.util;

import org.junit.Assert;
import org.junit.Test;

public class EnigmaUtilsTest {

    @Test
    public void testCreateWiringFromStrings() {
        String source = "ABCDEF";
        String target = "FEDCBA";
        byte[] wiring = EnigmaUtils.createRotorWiringFromStrings(source, target);
        testWiringAreAsExpected(wiring, 5, 4, 3, 2, 1, 0);

        target = "ABCDEF";
        wiring = EnigmaUtils.createRotorWiringFromStrings(source, target);
        testWiringAreAsExpected(wiring, 0, 1, 2, 3, 4, 5);

        target = "AFCDBE";
        wiring = EnigmaUtils.createRotorWiringFromStrings(source, target);
        testWiringAreAsExpected(wiring, 0, 4, 2, 3, 5, 1);

        source = "A";
        wiring = EnigmaUtils.createRotorWiringFromStrings(source, source);
        testWiringAreAsExpected(wiring, 0);
    }

    @Test
    public void testCompareTwoStringHoldsTheSameCharacters() {
        Assert.assertTrue(EnigmaUtils.doesTwoStringHoldsTheSameChars("abc", "bca"));
        Assert.assertTrue(EnigmaUtils.doesTwoStringHoldsTheSameChars("cab", "bca"));
        Assert.assertTrue(EnigmaUtils.doesTwoStringHoldsTheSameChars("bca", "bca"));
        Assert.assertTrue(EnigmaUtils.doesTwoStringHoldsTheSameChars("", ""));

        Assert.assertFalse(EnigmaUtils.doesTwoStringHoldsTheSameChars("a", "ab"));
        Assert.assertFalse(EnigmaUtils.doesTwoStringHoldsTheSameChars("ab", "b"));
    }

    @Test
    public void testEnsureStringHasOnlyDistinctCharacters() {
        Assert.assertTrue(EnigmaUtils.ensureDistinctCharactersInString("abc"));
        Assert.assertTrue(EnigmaUtils.ensureDistinctCharactersInString(""));

        Assert.assertFalse(EnigmaUtils.ensureDistinctCharactersInString("abcc"));
        Assert.assertFalse(EnigmaUtils.ensureDistinctCharactersInString("cabc"));
        Assert.assertFalse(EnigmaUtils.ensureDistinctCharactersInString("acbcd"));
    }

    private void testWiringAreAsExpected(byte [] actual, int... expected) {
        if (actual.length != expected.length) {
            Assert.fail();
        }
        for (int i=0; i<actual.length; i++) {
            Assert.assertEquals(actual[i], expected[i]);
        }
    }

}

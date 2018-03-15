package pukteam.enigma.util;

import java.util.HashSet;
import java.util.Set;

public class EnigmaUtils {

    /**
     * creates wiring based on two strings.
     * this method assumes that the two strings are equal in size, have no duplicates within them and holds a relationship
     * of 1:1 between all their characters (that is each char in 'source' appears exactly once in 'target' and vice versa)
     * the wiring is denoted by a byte array. the indices of the array corresponds to the source string characters positions
     * while the value at each cell corresponds to the location of the same character in the target string
     * for example:
     * source = "abc" ; target = "bac"
     * the resulting byte array will be:
     * index:   0   1   2
     * value:   1   0   2
     *
     * the wiring is case insensitive. (both source and target will be upper cased.)
     *
     * @param source the source string
     * @param target the target string
     * @return byte arrays denotes the wiring from the source to target
     */
    public static byte[] createRotorWiringFromStrings(String source, String target) {

        source = source.toUpperCase();
        target = target.toUpperCase();
        byte[] result = new byte[source.length()];

        for (int i = 0; i < source.length(); i++) {
            char sourceChar = source.charAt(i);
            byte indexAtShuffle = (byte) target.indexOf(sourceChar);
            result[i] = indexAtShuffle;
        }

        return result;
    }

    /**
     * verifies two given strings have the same length and have exactly the same characters (not necessarily the same order)
     * that is for each char belong to str1, it exists exactly once in str2 and vice versa
     * @param str1 first string
     * @param str2 second string
     * @return true if both strings share this characteristics, false otherwise
     */
    public static boolean doesTwoStringHoldsTheSameChars(String str1, String str2) {

        if (str1.length() != str2.length()) {
            return false;
        }

        Set<Integer> store = new HashSet<>(str1.length());
        str1.chars().forEach(store::add);
        str2.chars().forEach(store::remove);

        return store.isEmpty();
    }

    /**
     * ensures a given string does not have duplicate characters inside it
     * @param input the string to test
     * @return true if the string DO NOT have any duplicate characters within it, false otherwise
     */
    public static boolean ensureDistinctCharactersInString(String input) {
        Set<Character> distinctChars = new HashSet<>(input.length());
        int index = 0;
        while (index < input.length()) {
            if (!distinctChars.add(input.charAt(index++))) {
                return false;
            }
        }
        return true;
    }
}

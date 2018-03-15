package pukteam.enigma.component.machine.keyboard;

import org.junit.Assert;
import org.junit.Test;
import pukteam.enigma.EnigmaTestUtils;
import pukteam.enigma.component.keyboard.Keyboard;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class KeyboardTest {

    @Test
    public void testKeyboardHappyPath() {
        Keyboard keyboard = new Keyboard("abcde");

        verifyMatchingKeyboardInputs(keyboard, "adbce", new Byte[] {0, 3, 1, 2, 4});
        verifyMatchingKeyboardLighting(keyboard, new Byte [] {0, 4, 2, 1, 3}, "AECBD");
    }

    @Test
    public void testUnUsualCharacters() {
        String alphabet = "!@#$%^&*_+=~. ?[]{}()/\\;:'\"";
        Keyboard keyboard = new Keyboard(alphabet);

        Byte[] ts = IntStream
                .range(0, alphabet.length())
                .mapToObj(i -> (byte) i)
                .collect(Collectors.toList())
                .toArray(new Byte[] {});

        verifyMatchingKeyboardInputs(keyboard, alphabet, ts);
        verifyMatchingKeyboardLighting(keyboard, ts, alphabet);
    }

    @Test
    public void testKeyboardFails() {

        Keyboard keyboard = new Keyboard("abcde");
        EnigmaTestUtils.ensureExceptionIsThrown(() -> keyboard.toDigitInput('q'), "Failed on accepting unknown character");
        EnigmaTestUtils.ensureExceptionIsThrown(() -> keyboard.toLightKeyboard((byte)-1), "Failed on accepting unknown index");
        EnigmaTestUtils.ensureExceptionIsThrown(() -> keyboard.toLightKeyboard((byte)5), "Failed on accepting unknown index");
    }

    private void verifyMatchingKeyboardInputs(Keyboard keyboard, String input, Byte[] output) {
        byte o;
        for (int i = 0; i < input.length(); i++) {
            o = keyboard.toDigitInput(input.charAt(i));
            Assert.assertEquals(output[i].byteValue(), o);
        }
    }

    private void verifyMatchingKeyboardLighting(Keyboard keyboard, Byte[] inputs, String output) {
        char o;
        for (int i = 0; i < inputs.length; i++) {
            o = keyboard.toLightKeyboard(inputs[i]);
            Assert.assertEquals(output.charAt(i), o);
        }
    }

}

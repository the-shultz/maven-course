package pukteam.enigma.component.keyboard;

import pukteam.enigma.exceptions.input.EnigmaInvalidInputException;

import java.util.HashMap;
import java.util.Map;

public class Keyboard {

    private final Map<Character, Byte> incomingKeys;
    private final Map<Byte, Character> lightningKeys;

    public Keyboard(String alphabet) {
        incomingKeys = new HashMap<>(alphabet.length());
        lightningKeys = new HashMap<>(alphabet.length());

        buildKeyboardMapping(alphabet);
    }

    public byte toDigitInput(char c) {
        c = Character.toUpperCase(c);
        Byte output = incomingKeys.get(c);

        if (output == null) {
            throw new EnigmaInvalidInputException("BAD Character ! " + c + " does not appear in the ALPHABET");
        }

        return output;
    }

    public char toLightKeyboard(byte b) {
        Character output = lightningKeys.get(b);

        if (output == null) {
            throw new EnigmaInvalidInputException("BAD output ! " + b + " is out of scope of the ALPHABET boundaries");
        }

        return output;
    }

    private void buildKeyboardMapping(String alphabet) {
        char[] toStore = alphabet.toUpperCase().toCharArray();
        for (byte i = 0; i < toStore.length; i++) {
            incomingKeys.put(toStore[i], i);
            lightningKeys.put(i, toStore[i]);
        }
    }
}

package pukteam.enigma.component.machine.impl;

import pukteam.enigma.component.keyboard.Keyboard;
import pukteam.enigma.component.reflector.Reflector;
import pukteam.enigma.component.rotor.Direction;
import pukteam.enigma.component.rotor.Rotor;

import java.util.List;

public enum CharProcessor {
    STANDARD {
        @Override
        char process(char character, Keyboard keyboard, List<Rotor> executionRotors, Reflector executionReflector) {

            // convert from keyboard to byte input
            byte intermediate = keyboard.toDigitInput(character);;

            // perform rotors advance
            executionRotors.get(0).advance();

            // run forward, taking each output of rotor i as input for rotor i+1 using the 'intermediate' variable
            for (Rotor executionRotor : executionRotors) {
                intermediate = executionRotor.translate(intermediate, Direction.FORWARD);
            }

            // reflector
            intermediate = executionReflector.reflect(intermediate);

            // run backward, taking each output of rotor i as input for rotor i-1 using the 'intermediate' variable
            for (int i = executionRotors.size() - 1; i >= 0 ; i--) {
                intermediate = executionRotors.get(i).translate(intermediate, Direction.BACKWARD);
            }

            // transform byte back to character by the keyboard
            return keyboard.toLightKeyboard(intermediate);
        }
    }, DEBUG {

        private final String DEBUG_SEPARATOR = " -> ";
        private final String NEW_LINE = "\n";
        private final String PLACE_HOLDER = "!@#";

        @Override
        char process(char character, Keyboard keyboard, List<Rotor> executionRotors, Reflector executionReflector) {

            StringBuilder sb = new StringBuilder();
            sb.ensureCapacity(280);

            sb.append("Rotors position (BEFORE advance)").append(DEBUG_SEPARATOR).append("Rotors position (AFTER advance)").append(NEW_LINE);
            for (Rotor rotor : executionRotors) {
                sb.append(rotor).append(DEBUG_SEPARATOR).append(PLACE_HOLDER).append(NEW_LINE);
            }

            // perform rotors advance
            executionRotors.get(0).advance();
            for (Rotor rotor : executionRotors) {
                int nextPlaceHolderIndex = sb.indexOf(PLACE_HOLDER);
                String matchingRotor = rotor.toString();
                sb.delete(nextPlaceHolderIndex, nextPlaceHolderIndex + PLACE_HOLDER.length());
                sb.insert(nextPlaceHolderIndex, matchingRotor);
            }

            // convert from keyboard to byte input
            sb.append(Character.toUpperCase(character)).append(DEBUG_SEPARATOR);
            byte intermediate = keyboard.toDigitInput(character);;

            // run forward, taking each output of rotor i as input for rotor i+1 using the 'intermediate' variable
            for (Rotor executionRotor : executionRotors) {
                sb.append(intermediate + 1)
                        .append("(")
                        .append(executionRotor.calculateCharFromPosition((byte)(intermediate + 1), Direction.FORWARD))
                        .append(")")
                        .append(DEBUG_SEPARATOR)
                        .append("R")
                        .append(executionRotor.getId())
                        .append(DEBUG_SEPARATOR);
                intermediate = executionRotor.translate(intermediate, Direction.FORWARD);
            }

            // reflector
            sb.append(intermediate + 1).append(DEBUG_SEPARATOR).append("RF").append(executionReflector.getId()).append(DEBUG_SEPARATOR);
            intermediate = executionReflector.reflect(intermediate);

            // run backward, taking each output of rotor i as input for rotor i-1 using the 'intermediate' variable
            for (int i = executionRotors.size() - 1; i >= 0 ; i--) {
                Rotor current = executionRotors.get(i);
                sb.append(intermediate + 1)
                        .append("(")
                        .append(current.calculateCharFromPosition((byte)(intermediate + 1), Direction.BACKWARD))
                        .append(")")
                        .append(DEBUG_SEPARATOR)
                        .append("R")
                        .append(current.getId())
                        .append(DEBUG_SEPARATOR);
                intermediate = current.translate(intermediate, Direction.BACKWARD);
            }
            sb.append(intermediate + 1).append(DEBUG_SEPARATOR);

            // transform byte back to character by the keyboard
            char output = keyboard.toLightKeyboard(intermediate);
            sb.append(output).append(NEW_LINE);

            sb.insert(0, String.valueOf(Character.toUpperCase(character)) + DEBUG_SEPARATOR + output + NEW_LINE);

            System.out.println(sb.toString());
            return output;
        }
    };

    abstract char process(char character, Keyboard keyboard, List<Rotor> executionRotors, Reflector executionReflector);
}

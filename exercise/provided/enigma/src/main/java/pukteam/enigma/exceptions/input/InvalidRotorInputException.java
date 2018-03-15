package pukteam.enigma.exceptions.input;

import pukteam.enigma.component.rotor.Direction;

public class InvalidRotorInputException extends RuntimeException {

    public InvalidRotorInputException(String message) {
        super(message);
    }

    public InvalidRotorInputException(int id, byte input, Direction direction, int pace) {
        super("BAD WIRING ! Rotor " + id + " " + direction + " input " + input + " pace " + pace);
    }
}

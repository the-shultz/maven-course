package pukteam.enigma.exceptions.input;

public class InvalidReflectorInputException extends RuntimeException {

    public InvalidReflectorInputException(byte input) {
        super("BAD Reflector input: " + input + " Not exists !");
    }
}

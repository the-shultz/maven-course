package pukteam.enigma.component.rotor;

import pukteam.enigma.exceptions.input.InvalidRotorInputException;

import java.util.HashMap;
import java.util.Map;

public class RotorImpl implements Rotor {

    private final int id;
    private final Map<Byte, Byte> forwardWiringMapping;
    private final Map<Byte, Byte> backwardWiringMapping;
    private final int rotorSize;
    private String inputWiring;
    private String outputWiring;

    private final int originalNotchPosition;
    private int notch;
    private AdvanceSelf nextRotor;
    private int pace;

    private RotorImpl(int id, int notch, int rotorSize, String inputWiring, String outputWiring) {
        this.id = id;
        this.originalNotchPosition = notch;
        this.notch = notch;
        this.rotorSize = rotorSize;
        this.inputWiring = inputWiring.toUpperCase();
        this.outputWiring = outputWiring.toUpperCase();

        this.pace = 0;
        forwardWiringMapping = new HashMap<>(this.rotorSize);
        backwardWiringMapping = new HashMap<>(this.rotorSize);
    }

    public RotorImpl(int id, byte[] wiring, int notch, String inputWiring, String outputWiring) {
        this.id = id;
        this.originalNotchPosition = notch;
        this.notch = notch;
        this.pace = 0;
        this.rotorSize = wiring.length;
        this.inputWiring = inputWiring.toUpperCase();
        this.outputWiring = outputWiring.toUpperCase();

        forwardWiringMapping = new HashMap<>(this.rotorSize);
        backwardWiringMapping = new HashMap<>(this.rotorSize);
        buildWiring(wiring);
    }

    @Override
    public Rotor createClone() {
        RotorImpl copy = new RotorImpl(this.id, this.notch, this.rotorSize, this.inputWiring, this.outputWiring);
        copy.forwardWiringMapping.putAll(this.forwardWiringMapping);
        copy.backwardWiringMapping.putAll(this.backwardWiringMapping);
        return copy;
    }

    @Override
    public void setTo(byte position) {
        position--;
        pace = position % rotorSize;

        notch = this.originalNotchPosition - (position % rotorSize);
        if (notch < 0) {
            notch += rotorSize;
        }
    }

    @Override
    public byte setTo(char character) {
        int position = inputWiring.indexOf(Character.toUpperCase(character));

        if (position == -1) {
            throw new InvalidRotorInputException("Error ! Trying to set rotor " + id + " to initial position by alphabet character " + character + " which is not part of this rotor alphabet [" + inputWiring + "]");
        }
        position++;
        setTo((byte) position);
        return (byte) position ;
    }

    @Override
    public int calculatePositionFromChar(char rotorPosition) {
        // if it does not exists will return 0 (-1 + 1)
        // and it's ok since valid rotor position must be bigger than 0...
        return inputWiring.indexOf(Character.toUpperCase(rotorPosition)) + 1;
    }

    private byte getPosition() {
        return (byte) (pace % rotorSize);
    }

    private void buildWiring(byte[] wiring) {
        for (byte i=0; i<wiring.length; i++) {
            forwardWiringMapping.put(i, wiring[i]);
            backwardWiringMapping.put(wiring[i], i);
        }
    }

    @Override
    public void setNextRotor(AdvanceSelf nextRotor) {
        this.nextRotor = nextRotor;
    }

    @Override
    public void advance() {

        pace = (++pace)%rotorSize;

        --notch;
        if (notch < 0) {
            notch += rotorSize;
        }

        if (notch == 0 & nextRotor != null) {
            nextRotor.advance();
        }
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public byte getRotorSize() {
        return (byte)rotorSize;
    }

    @Override
    public byte translate(byte input, Direction direction) {

        byte translatedInputAfterPace = (byte) ((input + pace) % rotorSize);
        Byte output = Direction.FORWARD.equals(direction) ?
                forwardWiringMapping.get(translatedInputAfterPace) :
                backwardWiringMapping.get(translatedInputAfterPace);

        if (output == null) {
            throw new InvalidRotorInputException(id, input, direction, pace);
        }

        byte translatedAfterPace = (byte)(output - pace);
        if (translatedAfterPace < 0) {
            translatedAfterPace += rotorSize;
        }
        return translatedAfterPace;

    }

    @Override
    public char calculateCharFromPosition(byte position, Direction direction) {
        position--;
        byte actualPosition = (byte) ((position + pace) % rotorSize);
        return getCharAtPosition(actualPosition, direction);
    }

    @Override
    public String getInputWiring() {
        return inputWiring;
    }

    private char getCharAtPosition(byte position, Direction direction) {
        return Character.toUpperCase(
                Direction.FORWARD.equals(direction) ?
                        inputWiring.charAt(position) :
                        outputWiring.charAt(position)
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RotorImpl rotor = (RotorImpl) o;

        return id == rotor.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        byte position = getPosition();
        char charAtWindow = getCharAtPosition(position, Direction.FORWARD);
        return "Rotor " + id + ": Position " + (position + 1) + "(" + charAtWindow + ") Notch " + (notch + 1);
    }
}
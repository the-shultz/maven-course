package pukteam.enigma.component.reflector;

import pukteam.enigma.exceptions.creation.ReflectionCreationException;
import pukteam.enigma.exceptions.input.InvalidReflectorInputException;

import java.util.HashMap;
import java.util.Map;

public class ReflectorImpl implements Reflector {

    private final int id;
    private final Map<Byte, Byte> reflections;

    public ReflectorImpl(int id, byte[] input, byte[] output) {

        if (input.length != output.length || input.length == 0) {
            throw new ReflectionCreationException("BAD Reflection array inputs ! inputs length " + input.length + " output length " + output.length);
        }

        this.id = id;
        this.reflections = new HashMap<>();
        buildReflections(input, output);
    }

    private void buildReflections(byte[] input, byte[] output) {
        for (int i = 0; i < input.length; i++) {
            reflections.put(input[i], output[i]);
            reflections.put(output[i], input[i]);
        }
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getReflectorSize() {
        return reflections.size();
    }

    @Override
    public byte reflect(byte input) {
        byte oneBasedInput = (byte) (input + 1);
        Byte output = reflections.get(oneBasedInput);

        if (output == null) {
            throw new InvalidReflectorInputException(input);
        }

        // convert output to 0 based
        return (byte) (output - 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReflectorImpl reflector = (ReflectorImpl) o;

        return id == reflector.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}

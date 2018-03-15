package pukteam.enigma.factory;

import pukteam.enigma.component.machine.builder.EnigmaMachineBuilder;
import pukteam.enigma.component.reflector.Reflector;
import pukteam.enigma.component.reflector.ReflectorImpl;
import pukteam.enigma.component.rotor.Rotor;
import pukteam.enigma.component.rotor.RotorImpl;
import pukteam.enigma.exceptions.creation.RotorCreationException;
import pukteam.enigma.util.EnigmaUtils;

import java.util.Random;

public class EnigmaComponentFactoryImpl implements EnigmaComponentFactory {

    @Override
    public EnigmaMachineBuilder buildMachine(int rotorsCount, String alphabet) {
        return EnigmaMachineFactory.buildMachine(rotorsCount, alphabet);
    }

    @Override
    public Rotor createFromString(int id, String source, String target) {
        return RotorFactory.createFromString(id, source, target);
    }

    @Override
    public Rotor createFromString(int id, String source, String target, int notch) {
        return RotorFactory.createFromString(id, source, target, notch);
    }

    @Override
    public Reflector createReflector(int id, byte[] input, byte[] output) {
        return ReflectorFactory.createReflector(id, input, output);
    }

    private static class EnigmaMachineFactory {

        public static EnigmaMachineBuilder buildMachine(int rotorsCount, String alphabet) {
            return new EnigmaMachineBuilder(rotorsCount, alphabet);
        }

    }

    private static class RotorFactory {

        public static Rotor createFromString(int id, String source, String target) {
            return createFromString(id, source, target, source.length() == 0 ? 0 : new Random().nextInt(source.length()) + 1);
        }

        public static Rotor createFromString(int id, String source, String target, int notch) {
            source = source.toUpperCase();
            target = target.toUpperCase();

            if (source.length() != target.length()) {
                throw new RotorCreationException("No match between source and target length: " + source.length() + ":" + target.length());
            }

            if (!EnigmaUtils.ensureDistinctCharactersInString(source) || !EnigmaUtils.ensureDistinctCharactersInString(target)) {
                throw new RotorCreationException("Either the source [" + source + "] or target [" + target + "] have duplicate chars in them");
            }

            if (!EnigmaUtils.doesTwoStringHoldsTheSameChars(source, target)) {
                throw new RotorCreationException("Source [" + source + "] and target [" + target + "] Do not have the same input");
            }

            if (source.length() > 255 || source.length() == 0) {
                throw new RotorCreationException("Strings length (" + source.length() + ") is bigger then byte containment or 0");
            }

            byte[] wiring = EnigmaUtils.createRotorWiringFromStrings(source, target);
            notch--;
            if (notch < 0 || notch >= source.length()) {
                throw new RotorCreationException("Notch (" + notch + ") is not within range of rotor size (" + wiring.length + ")");
            }

            return new RotorImpl(id, wiring, notch, source, target);
        }

    }

    private static class ReflectorFactory {

        public static Reflector createReflector(int id, byte[] input, byte[] output) {
            return new ReflectorImpl(id, input, output);
        }

    }
}

package pukteam.enigma.factory;

import org.junit.Assert;
import org.junit.Test;
import pukteam.enigma.EnigmaTestUtils;
import pukteam.enigma.exceptions.creation.ReflectionCreationException;
import pukteam.enigma.exceptions.creation.RotorCreationException;

public class EnigmaMachineFactoryTest {

    @Test
    public void testMachineCreationExceptions() {

        // 0 rotors in use
        EnigmaTestUtils.ensureExceptionIsThrown(() -> {
            EnigmaComponentFactory.INSTANCE
                    .buildMachine(0, "abcdef")
                    .create();
        }, "Allows build a machine with 0 rotors are in use");

        // alphabet not even
        EnigmaTestUtils.ensureExceptionIsThrown(() -> {
            EnigmaComponentFactory.INSTANCE
                    .buildMachine(1, "aqw")
                    .create();
        }, "Allows build a machine with non even alphabet aqw");

        // fewer available rotors then needed
        EnigmaTestUtils.ensureExceptionIsThrown(() -> {
            EnigmaComponentFactory.INSTANCE
                    .buildMachine(2, "abcdef")
                    .defineRotor(1, "abcdef", "fedcba", 3)
                    .create();
        }, "Allows build a machine that needs 2 rotors but supplies only 1");

        // not even one reflector is defined
        EnigmaTestUtils.ensureExceptionIsThrown(() -> {
            EnigmaComponentFactory.INSTANCE
                    .buildMachine(2, "abcdef")
                    .defineRotor(1, "abcdef", "fedcba", 3)
                    .defineRotor(2, "abcdef", "fedcba", 1)
                    .create();
        }, "Allows build a machine without supplying at least one reflector definition");

        // all rotors are at the correct size
        EnigmaTestUtils.ensureExceptionIsThrown(() -> {
            EnigmaComponentFactory.INSTANCE
                    .buildMachine(2, "abcdef")
                    .defineRotor(1, "abcdefq", "fedcbaq", 3)
                    .defineRotor(2, "abcdf", "fedca", 1)
                    .defineReflector(1, new byte[]{1, 2, 3}, new byte[]{4, 5, 6})
                    .create();
        }, "Allows build a machine with non matching rotors to the alphabet size");

        // duplicate chars between alphabet and rotor input source
        EnigmaTestUtils.ensureExceptionIsThrown(() -> {
            EnigmaComponentFactory.INSTANCE
                    .buildMachine(2, "abcdef")
                    .defineRotor(1, "abbdef", "fedbca", 3)
                    .defineRotor(2, "abcddf", "fedbca", 1)
                    .defineReflector(1, new byte[]{1, 2, 3}, new byte[]{4, 5, 6})
                    .create();
        }, "Allows build a machine with duplicate characters in the rotor comparing the alphabet");

        // rotor and alphabet differ in their characters (though match in size)
        EnigmaTestUtils.ensureExceptionIsThrown(() -> {
            EnigmaComponentFactory.INSTANCE
                    .buildMachine(2, "abcdef")
                    .defineRotor(1, "abqdef", "fedbca", 3)
                    .defineRotor(2, "abcdqf", "fedbca", 1)
                    .defineReflector(1, new byte[]{1, 2, 3}, new byte[]{4, 5, 6})
                    .create();
        }, "Allows build a machine with rotor that does not holds exactly all the the alphabet chars");

        // all reflectors are at the correct size
        EnigmaTestUtils.ensureExceptionIsThrown(() -> {
            EnigmaComponentFactory.INSTANCE
                    .buildMachine(2, "abcdef")
                    .defineRotor(1, "abcdefq", "fedcbaq", 3)
                    .defineRotor(2, "abcdf", "fedca", 1)
                    .defineReflector(1, new byte[]{1, 3}, new byte[]{4, 6})
                    .create();
        }, "Allows build a machine with non matching reflectors to the alphabet size");

    }

    @Test()
    public void testCreateWiringFailures() {
        String source = "ABCDEF";
        String target = "FEDCBAQ";
        boolean exceptionThrown = true;
        try {
            EnigmaComponentFactory.INSTANCE.createFromString(1, source, target);
        } catch (RotorCreationException e) {
            Assert.assertTrue(exceptionThrown);
        }

        // 270 characters
        source = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

        try {
            EnigmaComponentFactory.INSTANCE.createFromString(1, source, source);
        } catch (RotorCreationException e) {
            Assert.assertTrue(exceptionThrown);
        }

        try {
            EnigmaComponentFactory.INSTANCE.createFromString(1, "", "");
        } catch (RotorCreationException e) {
            Assert.assertTrue(exceptionThrown);
        }

    }

    @Test()
    public void testFailCreateReflector() {
        boolean exceptionThrown = true;

        try {
            EnigmaComponentFactory.INSTANCE.createReflector(1, new byte[]{1, 2, 3}, new byte[]{4, 5, 6, 7});
            exceptionThrown = false;
        } catch (ReflectionCreationException e) {
            Assert.assertTrue(exceptionThrown);
        }

        try {
            EnigmaComponentFactory.INSTANCE.createReflector(1, new byte[]{}, new byte[]{});
            exceptionThrown = false;
        } catch (ReflectionCreationException e) {
            Assert.assertTrue(exceptionThrown);
        }

    }
}

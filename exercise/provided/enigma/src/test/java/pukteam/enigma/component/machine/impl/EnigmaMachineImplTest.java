package pukteam.enigma.component.machine.impl;

import org.junit.Assert;
import org.junit.Test;
import pukteam.enigma.EnigmaTestUtils;
import pukteam.enigma.component.machine.api.EnigmaMachine;
import pukteam.enigma.component.machine.api.Secret;
import pukteam.enigma.factory.EnigmaComponentFactory;

import java.util.Arrays;

public class EnigmaMachineImplTest {

    @Test
    public void testCreateSecretException() {
        EnigmaMachine enigmaMachine =
                EnigmaComponentFactory.INSTANCE
                .buildMachine(2, "abcdef")
                .defineRotor(1, "abcdef", "fedcba", 3)
                .defineRotor(2, "abcdef", "fedcba", 1)
                .defineRotor(3, "abcdef", "acefdb", 2)
                .defineRotor(4, "abcdef", "abcdef", 2)
                .defineReflector(1, new byte[]{1, 2, 3}, new byte[]{4, 5, 6})
                .defineReflector(2, new byte[]{1, 3, 5}, new byte[]{6, 4, 2})
                .create();

        EnigmaTestUtils.ensureExceptionIsThrown(() ->
                        enigmaMachine
                                .createSecret()
                                .selectRotor(5, 9),
                "Error ! you have selected non existing rotor (5) for secret");

        EnigmaTestUtils.ensureExceptionIsThrown(() ->
                        enigmaMachine
                                .createSecret()
                                .selectRotor(1, 1)
                                .selectRotor(1, 1),
                "Error ! rotor 1 is already in use");

        EnigmaTestUtils.ensureExceptionIsThrown(() ->
                        enigmaMachine
                                .createSecret()
                                .selectRotor(1, 7),
                "Error ! Bad position (7) for rotor 1, more then it's limits");

        EnigmaTestUtils.ensureExceptionIsThrown(() ->
                        enigmaMachine
                                .createSecret()
                                .selectReflector(5),
                "Error ! No such reflector (");

        EnigmaTestUtils.ensureExceptionIsThrown(() ->
                        enigmaMachine
                                .createSecret()
                                .selectRotor(1, 3)
                                .create(),
                "Error ! Not enough rotors (1) were selected for the secret. needs 2");

        EnigmaTestUtils.ensureExceptionIsThrown(() ->
                        enigmaMachine
                                .createSecret()
                                .selectRotor(1, 3)
                                .selectRotor(2, 1)
                                .create(),
                "Error ! No reflector was selected");
    }

    @Test
    public void testSingleCharacterProcessing() {
        EnigmaMachine machine = createMachineForTesting();

        machine.setDebug(true);

        // encrypt
        Assert.assertEquals('A', machine.process('b'));
        Assert.assertEquals('A', machine.process('d'));
        Assert.assertEquals('F', machine.process('a'));
        machine.consumeState(System.out::println);
        
        // decrypt
        machine.resetToInitialPosition();

        Assert.assertEquals('B', machine.process('A'));
        Assert.assertEquals('D', machine.process('A'));
        Assert.assertEquals('A', machine.process('F'));

        // encrypt again from the initial position other letters
        machine.resetToInitialPosition();

        Assert.assertEquals('F', machine.process('C'));
        Assert.assertEquals('C', machine.process('B'));
        Assert.assertEquals('E', machine.process('B'));

    }

    @Test
    public void testProcessWithManualRotorsPosition() {
        EnigmaMachine machine = createMachineForTesting();
        machine
            .createSecret()
            .selectRotor(1, 1)
            .selectRotor(2, 1)
            .selectReflector(1)
            .create();

        // selecting position 3,3 for both rotors, which according to the rotors alphabet is C C
        machine.setInitialPosition("cC");

        Assert.assertEquals('A', machine.process('b'));
        Assert.assertEquals('A', machine.process('d'));
        Assert.assertEquals('F', machine.process('a'));

        // decrypt (resets back to cc)
        machine.resetToInitialPosition();

        Assert.assertEquals('B', machine.process('A'));
        Assert.assertEquals('D', machine.process('A'));
        Assert.assertEquals('A', machine.process('F'));

        // repeat the test only this time change positions directly through the secret object
        machine
                .createSecret()
                .selectRotor(1, 1)
                .selectRotor(2, 1)
                .selectReflector(1)
                .create();
        Secret secret = machine.getSecret().setInitialPosition(Arrays.asList(3,3));
        machine.initFromSecret(secret);

        Assert.assertEquals('A', machine.process('b'));
        Assert.assertEquals('A', machine.process('d'));
        Assert.assertEquals('F', machine.process('a'));

        // decrypt (resets back to cc)
        machine.resetToInitialPosition();

        Assert.assertEquals('B', machine.process('A'));
        Assert.assertEquals('D', machine.process('A'));
        Assert.assertEquals('A', machine.process('F'));

    }

    @Test
    public void testProcessCompleteString() {
        EnigmaMachine machine = createMachineForTesting();
        machine.setDebug(true);

        String input = "bda";
        String output = machine.process(input);

        Assert.assertEquals("AAF", output);

        // decrypt
        machine.resetToInitialPosition();
        input = "AAF";
        output = machine.process(input);
        Assert.assertEquals("BDA", output);
    }

    private EnigmaMachine createMachineForTesting() {
        EnigmaMachine machine =
                EnigmaComponentFactory.INSTANCE
                .buildMachine(2, "abcdef")
                .defineRotor(1, "abcdef", "fedcba", 4)
                .defineRotor(2, "abcdef", "ebdfca", 1)
                .defineReflector(1, new byte[]{1, 2, 3}, new byte[]{4, 5, 6})
                .defineReflector(2, new byte[]{1, 3, 5}, new byte[]{6, 4, 2})
                .create();

        Secret secret =
                machine
                .createSecret()
                .selectRotor(1, 3)
                .selectRotor(2, 'C')
                .selectReflector(1)
                .create();

        return machine;
    }

}

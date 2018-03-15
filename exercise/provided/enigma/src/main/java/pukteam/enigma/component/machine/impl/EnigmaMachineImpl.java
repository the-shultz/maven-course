package pukteam.enigma.component.machine.impl;

import pukteam.enigma.component.keyboard.Keyboard;
import pukteam.enigma.component.machine.api.EnigmaMachine;
import pukteam.enigma.component.machine.api.Secret;
import pukteam.enigma.component.machine.repository.Repository;
import pukteam.enigma.component.machine.secret.SecretBuilder;
import pukteam.enigma.component.reflector.Reflector;
import pukteam.enigma.component.rotor.Rotor;
import pukteam.enigma.exceptions.input.EnigmaInvalidInputException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EnigmaMachineImpl implements EnigmaMachine {
    

    private int rotorsCount;
    private Secret currentSecret;
    private Repository repository;
    private Keyboard keyboard;

    private boolean debug;
    private List<Rotor> executionRotors;
    private Reflector executionReflector;

    public EnigmaMachineImpl(int rotorsCount, String alphabet, Repository repository) {
        this.rotorsCount = rotorsCount;
        this.keyboard = new Keyboard(alphabet);
        this.repository = repository;
        this.debug = false;
    }

    @Override
    public String process(String plainText) {

        char[] source = plainText.toCharArray();
        char[] processed = new char[source.length];

        for (int i = 0; i < source.length; i++) {
            processed[i] = process(source[i]);
        }

        return new String(processed);
    }

    @Override
    public char process(char character) {

        CharProcessor selectedMethod = CharProcessor.STANDARD;
        if (debug) {
            selectedMethod = CharProcessor.DEBUG;
        }

        return selectedMethod.process(character, keyboard, executionRotors, executionReflector);
    }


    @Override
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    @Override
    public SecretBuilder createSecret() {
        return new SecretBuilder(rotorsCount, repository, this::initFromSecret);
    }

    @Override
    public void initFromSecret(Secret secret) {

        currentSecret = secret;
        executionRotors = new ArrayList<>(rotorsCount);

        // get a clone of rotors for use
        secret.getSelectedRotorsInOrder()
                .forEach(rotorId -> {
                    executionRotors.add(repository.getClonedRotor(rotorId));
                });

        // connect rotors for auto advance by notch
        for (int i = 1; i < executionRotors.size(); i++) {
            Rotor last = executionRotors.get(i-1);
            Rotor current = executionRotors.get(i);
            last.setNextRotor(current);
        }

        resetToInitialPosition();

        executionReflector = repository.pickReflector(currentSecret.getSelectedReflector());
    }

    @Override
    public Secret getSecret() {
        return currentSecret;
    }

    @Override
    public void resetToInitialPosition() {
        // set the current position
        for (int i = 0; i < executionRotors.size(); i++) {
            Rotor current = executionRotors.get(i);
            Integer position = currentSecret.getSelectedRotorsPositions().get(i);
            current.setTo((byte)position.intValue());
        }
    }

    @Override
    public void setInitialPosition(String position) {

        if (position.length() != executionRotors.size()) {
            throw new EnigmaInvalidInputException("Error ! tyring to set initial position to " + position + " while there are only " + executionRotors.size() + " rotors");
        }

        List<Integer> newPosition = new ArrayList<>(position.length());

        for (int i = 0; i <position.length(); i++) {
            Rotor rotor = executionRotors.get(i);
            char pos = position.charAt(i);
            newPosition.add( (int)rotor.setTo(pos) );
        }

        currentSecret = currentSecret.setInitialPosition(newPosition);
    }

    @Override
    public void consumeState(Consumer<String> stateConsumer) {
        StringBuilder sb = new StringBuilder();
        sb.append("Rotors:\n");
        for (Rotor rotor : executionRotors) {
            sb.append(rotor).append("\n");
        }
        sb.append("Reflector ").append(executionReflector.getId());

        stateConsumer.accept(sb.toString());
    }
}

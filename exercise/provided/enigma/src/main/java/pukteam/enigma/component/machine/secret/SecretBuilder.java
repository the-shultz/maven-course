package pukteam.enigma.component.machine.secret;

import pukteam.enigma.component.machine.api.Secret;
import pukteam.enigma.component.machine.repository.Repository;
import pukteam.enigma.component.rotor.Rotor;
import pukteam.enigma.exceptions.creation.SecretCreationException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class SecretBuilder {

    private Map<Integer, Integer> selectedRotors;
    private int selectedReflector;
    private final int rotorsCount;
    private final Repository repository;
    private final Consumer<Secret> secretConsumer;

    public SecretBuilder(int rotorsCount, Repository repository, Consumer<Secret> secretConsumer) {
        this.rotorsCount = rotorsCount;
        this.secretConsumer = secretConsumer;
        this.repository = repository;
        this.selectedReflector = -1;
        selectedRotors = new LinkedHashMap<>(); // to preserve insertion order
    }

    public SecretBuilder selectRotor(int rotorId, int rotorPosition) {

        Rotor rotor = repository.getRotor(rotorId);
        if (rotor == null) {
            throw new SecretCreationException("Rotor with id " + rotorId + " Does not exists !");
        }

        if (selectedRotors.containsKey(rotorId)) {
            throw new SecretCreationException("rotor " + rotorId + " is already in use");
        }

        byte selectedRotorSize = rotor.getRotorSize();
        if (rotorPosition < 1 || rotorPosition > selectedRotorSize) {
            throw new SecretCreationException("Bad rotor position ! Rotor " + rotorId + " size " + selectedRotorSize + " and requested position is " + rotorPosition);
        }
        selectedRotors.put(rotorId, rotorPosition);

        return this;
    }

    public SecretBuilder selectRotor(int rotorId, char rotorPosition) {
        Rotor rotor = repository.getRotor(rotorId);

        if (rotor == null) {
            throw new SecretCreationException("Rotor with id " + rotorId + " Does not exists !");
        }

        return selectRotor(rotorId, rotor.calculatePositionFromChar(rotorPosition));
    }

    public SecretBuilder selectReflector(int reflectorId) {

        if (!repository.getAvailableReflectors().contains(reflectorId)) {
            throw new SecretCreationException("Reflector with id " + reflectorId + " Does not exists !");
        }

        selectedReflector = reflectorId;
        
        return this;
    }

    public Secret create() {

        // verify enough rotors where selected
        if (selectedRotors.size() != rotorsCount) {
            throw new SecretCreationException("Not enough rotors were defined for the secret. requires " + rotorsCount + " rotors but only " + selectedRotors.size() + " were defined");
        }

        // verify single reflector was selected
        if (selectedReflector == -1) {
            throw new SecretCreationException("No Reflector was selected !");
        }

        List<Integer> selectedRotorsByOrder = new ArrayList<>();
        List<Integer> selectedRotorsPositions = new ArrayList<>();

        selectedRotors
                .forEach((key, value) -> {
                    selectedRotorsByOrder.add(key);
                    selectedRotorsPositions.add(value);
                });

        Secret secret = new SecretImpl(selectedRotorsByOrder, selectedRotorsPositions, selectedReflector);
        secretConsumer.accept(secret);
        return secret;
    }
}

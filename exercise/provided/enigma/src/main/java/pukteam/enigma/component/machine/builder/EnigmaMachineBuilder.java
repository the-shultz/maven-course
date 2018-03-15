package pukteam.enigma.component.machine.builder;

import pukteam.enigma.component.machine.api.EnigmaMachine;
import pukteam.enigma.component.machine.impl.EnigmaMachineImpl;
import pukteam.enigma.component.machine.repository.Repository;
import pukteam.enigma.component.reflector.Reflector;
import pukteam.enigma.component.rotor.Rotor;
import pukteam.enigma.exceptions.creation.EnigmaMachineCreationException;
import pukteam.enigma.factory.EnigmaComponentFactory;
import pukteam.enigma.util.EnigmaUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EnigmaMachineBuilder {

    private int rotorsCount;
    private String alphabet;
    private List<RotorDefinitionContainer> rotorsDefinition;
    private List<ReflectorDefinitionContainer> reflectorsDefinition;

    public EnigmaMachineBuilder(int rotorsCount, String alphabet) {
        this.rotorsCount = rotorsCount;
        this.alphabet = alphabet.toUpperCase();
        rotorsDefinition = new ArrayList<>();
        reflectorsDefinition = new ArrayList<>();
    }

    public EnigmaMachineBuilder defineRotor(int id, String source, String target, int notch) {
        rotorsDefinition.add(new RotorDefinitionContainer(id, source, target, notch));
        return this;
    }

    public EnigmaMachineBuilder defineReflector(int id, byte[] input, byte[] output) {
        reflectorsDefinition.add(new ReflectorDefinitionContainer(id, input, output));
        return this;
    }

    public EnigmaMachine create() {

        // verify rotors count is bigger then 0
        if (rotorsCount <= 0) {
            throw new EnigmaMachineCreationException("Rotors count must be bigger than 0");
        }

        // verify alphabet size is even
        if (alphabet.length() % 2 != 0) {
            throw new EnigmaMachineCreationException("Alphaet size (" + alphabet.length() + ") must be of even size (in order to enable proper reflection operation");
        }

        // verify we have enough rotors
        if (rotorsDefinition.size() < rotorsCount) {
            throw new EnigmaMachineCreationException("Error ! Machine requires " + rotorsCount + " rotors but only " + rotorsDefinition.size() + " were defined...");
        }

        // verify we have at least one reflector
        if (reflectorsDefinition.isEmpty()) {
            throw new EnigmaMachineCreationException("Error ! Machine requires at least 1 reflector to be defined !");
        }

        Repository repository = createRepositoryFromDefinitions();

        List<Rotor> corruptedRotors = repository.streamRotors().filter(rotor ->
             !EnigmaUtils.ensureDistinctCharactersInString(rotor.getInputWiring()) ||
                    !EnigmaUtils.doesTwoStringHoldsTheSameChars(alphabet, rotor.getInputWiring() )
        ).collect(Collectors.toList());
        if (corruptedRotors.size() != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("Found rotors that are not ALPHABET complete. ALphabet [").append(alphabet).append("]:\n");
            corruptedRotors.forEach(r -> sb.append("Rotor ").append(r.getId()).append(" input wiring is [").append(r.getInputWiring()).append("]\n"));
            throw new EnigmaMachineCreationException(sb.toString());
        }

        // verify all reflectors are at the size of the alphabet
        List<Reflector> corruptedReflectors = repository.streamReflectors()
                .filter(r -> r.getReflectorSize() != alphabet.length())
                .collect(Collectors.toList());
        if (corruptedReflectors.size() != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("Alphabet size is ").append(alphabet.length()).append(" while the next reflector(s) do not match it's size:\\n");
            corruptedReflectors.forEach(r -> sb.append("Reflector ").append(r.getId()).append(" size is ").append(r.getReflectorSize()).append(" ; "));
            throw new EnigmaMachineCreationException(sb.toString());
        }

        return new EnigmaMachineImpl(rotorsCount, alphabet, repository);
    }

    private Repository createRepositoryFromDefinitions() {
        Repository repository = Repository.obtain();

        for (RotorDefinitionContainer r : rotorsDefinition) {
            repository.addRotor(EnigmaComponentFactory.INSTANCE.createFromString(r.id, r.inputWiring, r.outputWiring, r.notch));
        }

        for (ReflectorDefinitionContainer r : reflectorsDefinition) {
            repository.addReflector(EnigmaComponentFactory.INSTANCE.createReflector(r.id, r.input, r.output));
        }
        return repository;
    }

    private static class RotorDefinitionContainer {

        public String inputWiring;
        public String outputWiring;
        public int id;
        public int notch;

        public RotorDefinitionContainer(int id, String inputWiring, String outputWiring, int notch) {
            this.inputWiring = inputWiring;
            this.outputWiring = outputWiring;
            this.id = id;
            this.notch = notch;
        }
    }

    private static class ReflectorDefinitionContainer {

        public int id;
        public byte [] input;
        public byte [] output;

        public ReflectorDefinitionContainer(int id, byte[] input, byte[] output) {
            this.id = id;
            this.input = input;
            this.output = output;
        }
    }
}
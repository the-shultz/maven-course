package pukteam.enigma.component.machine.repository;

import pukteam.enigma.component.reflector.Reflector;
import pukteam.enigma.component.rotor.Rotor;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Stream;

public class RepositoryImpl implements Repository{

    private final Map<Integer, Rotor> availableRotors;
    private final Map<Integer, Reflector> availableReflectors;

    RepositoryImpl() {
        availableRotors = new HashMap<>();
        availableReflectors = new HashMap<>();
    }

    @Override
    public void addRotor(Rotor rotor) {
        availableRotors.put(rotor.getId(), rotor);
    }

    @Override
    public void addReflector(Reflector reflector) {
        availableReflectors.put(reflector.getId(), reflector);
    }

    @Override
    public Rotor getClonedRotor(int id) {

        Rotor rotor = availableRotors.get(id);

        if (rotor == null) {
            throw new NoSuchElementException("Error ! Can't find Rotor with ID " + id);
        }

        return rotor.createClone();
    }

    @Override
    public Reflector pickReflector(int id) {
        Reflector reflector = availableReflectors.get(id);

        if (reflector == null) {
            throw new NoSuchElementException("Error ! Can't find Reflector with ID " + id);
        }

        return reflector;
    }

    @Override
    public Set<Integer> getAvailableRotors() {
        return availableRotors.keySet();
    }

    @Override
    public Set<Integer> getAvailableReflectors() {
        return availableReflectors.keySet();
    }

    @Override
    public Stream<Rotor> streamRotors() {
        return availableRotors.values().stream();
    }

    @Override
    public Stream<Reflector> streamReflectors() {
        return availableReflectors.values().stream();
    }

    @Override
    public Rotor getRotor(int rotorId) {
        return availableRotors.get(rotorId);
    }
}

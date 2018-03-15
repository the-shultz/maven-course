package pukteam.enigma.component.machine.repository;

import pukteam.enigma.component.reflector.Reflector;
import pukteam.enigma.component.rotor.Rotor;

import java.util.Set;
import java.util.stream.Stream;

public interface Repository {

    void addRotor(Rotor rotor);
    void addReflector(Reflector reflector);

    Rotor getClonedRotor(int id);
    Rotor getRotor(int rotorId);
    Reflector pickReflector(int id);

    Set<Integer> getAvailableRotors();
    Set<Integer> getAvailableReflectors();
    Stream<Rotor> streamRotors();
    Stream<Reflector> streamReflectors();

    static Repository obtain() {
        return new RepositoryImpl();
    }
}

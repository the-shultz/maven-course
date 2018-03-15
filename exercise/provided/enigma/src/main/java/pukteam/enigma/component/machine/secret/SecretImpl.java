package pukteam.enigma.component.machine.secret;

import pukteam.enigma.component.machine.api.Secret;

import java.util.List;

public class SecretImpl implements Secret {

    private final List<Integer> selectedRotorsInOrder;
    private final List<Integer> selectedRotorsPositions;
    private final int selectedReflector;

    SecretImpl(List<Integer> selectedRotorsInOrder, List<Integer> selectedRotorsPositions, int selectedReflector) {
        this.selectedRotorsInOrder = selectedRotorsInOrder;
        this.selectedRotorsPositions = selectedRotorsPositions;
        this.selectedReflector = selectedReflector;
    }

    @Override
    public List<Integer> getSelectedRotorsInOrder() {
        return selectedRotorsInOrder;
    }

    @Override
    public List<Integer> getSelectedRotorsPositions() {
        return selectedRotorsPositions;
    }

    @Override
    public int getSelectedReflector() {
        return selectedReflector;
    }

    @Override
    public String toString() {
        return "Secret{" +
                "selectedRotorsInOrder=" + selectedRotorsInOrder +
                ", selectedRotorsPositions=" + selectedRotorsPositions +
                ", selectedReflector=" + selectedReflector +
                '}';
    }

    @Override
    public Secret setInitialPosition(List<Integer> newPosition) {
        return new SecretImpl(selectedRotorsInOrder, newPosition, selectedReflector);
    }

}

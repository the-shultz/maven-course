package pukteam.enigma;

import pukteam.enigma.component.machine.api.EnigmaMachine;
import pukteam.enigma.factory.EnigmaComponentFactory;

public class EnigmaCreator {

    public static EnigmaMachine createEnigmaMachine() {

        /* verify all string are the same length
        abcdefghijklmnopqrstuvwxyz
        ABCDEFGHIJKLMNOPQRSTUVWXYZ
        EKMFLGDQVZNTOWYHXUSPAIBRCJ
        AJDKSIRUXBLHWTMCQGZNPYFVOE
        BDFHJLCPRTXVZNYEIWGAKMUSQO
         */
        EnigmaMachine machine =
                EnigmaComponentFactory.INSTANCE
                        .buildMachine(3, "abcdefghijklmnopqrstuvwxyz .")
                        .defineRotor(1, "EKMFLGDQVZNTOWYHXUSPAIBRCJ .", "ABCDEFGHIJKLMNOPQRSTUVWXYZ .", 17)
                        .defineRotor(2, "AJDKSIRUXBLHWTMCQGZNPYFVOE .", "ABCDEFGHIJKLMNOPQRSTUVWXYZ .", 5)
                        .defineRotor(3, "BDFHJLCPRTXVZNYEIWGAKMUSQO .", "ABCDEFGHIJKLMNOPQRSTUVWXYZ .", 22)
                        .defineReflector(1,
                                new byte[]{1,  2,  3,  4, 5,  6,  7,  9,  10, 11, 13, 20, 22, 27},
                                new byte[]{25, 18, 21, 8, 17, 19, 12, 16, 24, 14, 15, 26, 23, 28})
                        .create();

        machine
                .createSecret()
                .selectRotor(3, 'X')
                .selectRotor(2, 'D')
                .selectRotor(1, 'O')
                .selectReflector(1)
                .create();

        return machine;
    }
}

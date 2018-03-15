package pukteam.maven.course.exercise.impl;

import pukteam.enigma.EnigmaCreator;
import pukteam.maven.course.exercise.provided.api.encrypt.Encryptor;
import pukteam.maven.course.exercise.provided.api.encrypt.EncryptorMethod;

public class SampleEncryptor implements Encryptor {

    public String encrypt(String s, EncryptorMethod encryptorMethod) {
        switch (encryptorMethod) {
            case ENIGMA:
                return handleEnigmaProcessing(s);
            default:
                throw new UnsupportedOperationException("Unknown encryption method " + encryptorMethod);
        }
    }

    private String handleEnigmaProcessing(String s) {
        return EnigmaCreator.createEnigmaMachine().process(s);
    }

    public String decrypt(String s, EncryptorMethod encryptorMethod) {
        switch (encryptorMethod) {
            case ENIGMA:
                return handleEnigmaProcessing(s);
            default:
                throw new UnsupportedOperationException("Unknown encryption method " + encryptorMethod);
        }

    }
}

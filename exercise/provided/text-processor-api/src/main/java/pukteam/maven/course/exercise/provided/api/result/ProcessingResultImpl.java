package pukteam.maven.course.exercise.provided.api.result;

import pukteam.maven.course.exercise.provided.api.encrypt.EncryptorMethod;
import pukteam.maven.course.exercise.provided.api.hash.HashMethod;

public class ProcessingResultImpl implements ProcessingResult {

    private final String hash;
    private final HashMethod hashMethod;
    private final String encryptedData;
    private final EncryptorMethod encryptorMethod;

    public ProcessingResultImpl(String hash, HashMethod hashMethod, String encryptedData, EncryptorMethod encryptorMethod) {
        this.hash = hash;
        this.hashMethod = hashMethod;
        this.encryptedData = encryptedData;
        this.encryptorMethod = encryptorMethod;
    }

    @Override
    public String getHash() {
        return null;
    }

    @Override
    public HashMethod getHashMethod() {
        return null;
    }

    @Override
    public String getEncryptedData() {
        return null;
    }

    @Override
    public EncryptorMethod getEncryptorMethod() {
        return null;
    }
}

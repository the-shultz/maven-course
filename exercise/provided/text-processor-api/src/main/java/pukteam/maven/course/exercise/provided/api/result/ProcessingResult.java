package pukteam.maven.course.exercise.provided.api.result;

import pukteam.maven.course.exercise.provided.api.encrypt.EncryptorMethod;
import pukteam.maven.course.exercise.provided.api.hash.HashMethod;

public interface ProcessingResult {
    String getChecksum();
    HashMethod getChecksumMethod();
    String getEncryptedData();
    EncryptorMethod getEncryptorMethod();
}

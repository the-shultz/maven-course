package pukteam.maven.course.exercise.provided.api.processor;

import pukteam.maven.course.exercise.provided.api.encrypt.EncryptorMethod;
import pukteam.maven.course.exercise.provided.api.hash.HashMethod;
import pukteam.maven.course.exercise.provided.api.result.ProcessingResult;

public interface TextProcessor {

    void encryptFileToFile(String sourceFileName, String targetFileName, EncryptorMethod encryptorMethod, HashMethod hashMethod);
    void decryptFromFileToFile(String sourceFileName, String targetFileName, ProcessingResult processingResult);

    ProcessingResult processTextLine(String line, EncryptorMethod encryptorMethod, HashMethod hashMethod);
    String restore(ProcessingResult processingResult);
}

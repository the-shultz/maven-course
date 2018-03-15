package pukteam.maven.course.exercise.provided.api.encrypt;

public interface Encryptor {
    String encrypt(String input, EncryptorMethod method);
    String decrypt(String input, EncryptorMethod method);
}

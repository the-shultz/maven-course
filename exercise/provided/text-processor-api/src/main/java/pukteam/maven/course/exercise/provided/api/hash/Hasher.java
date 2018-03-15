package pukteam.maven.course.exercise.provided.api.hash;

public interface Hasher {
    String hash(String input, HashMethod method);
}

package pukteam.maven.course.exercise.provided;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;

public class FileHandler {

    public static List<String> getFileLines(String fileName) {

        try (FileInputStream fin = new FileInputStream(fileName)) {
            return IOUtils.readLines(fin, "utf-8");
        } catch (FileNotFoundException e) {
            System.out.println("Error occur: can't find a file: " + fileName + ". Exception message: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error occur: while reading file lines: " + fileName + ". Exception message: " + e.getMessage());
        }

        return null;
    }

    public static boolean createFileWithContent(String fileName, List<String> lines) {

        try (FileOutputStream fos = new FileOutputStream(fileName)){
            IOUtils.writeLines(lines, IOUtils.LINE_SEPARATOR_WINDOWS, fos, Charset.defaultCharset());
            return true;
        } catch (IOException e) {
            System.out.println("Error occur: while writing lines to file: " + fileName + ". Exception message: " + e.getMessage());
        }

        return false;
    }

}

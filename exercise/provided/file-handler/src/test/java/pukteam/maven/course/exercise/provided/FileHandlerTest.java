package pukteam.maven.course.exercise.provided;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileHandlerTest {

    private final String FILE_NAME = "foo.txt";
    private List<String> lines;

    @Before
    public void beforeTest() {
        lines = new ArrayList<>();
        lines.add("this is a test");
        lines.add("second line");
        lines.add("third line");
        lines.add("yet another line");
        lines.add("and that's the last one !");
    }

    @Test
    public void testGetLinesFromFile() throws IOException {
        createFileWithText(FILE_NAME, lines);
        List<String> fileLines = FileHandler.getFileLines(FILE_NAME);
        verifyMatchingLines(lines, fileLines);
        Files.deleteIfExists(Paths.get(FILE_NAME));
    }

    @Test
    public void testCreateFileWithContent() throws IOException {


        FileHandler.createFileWithContent(FILE_NAME, lines);
        Path newFile = Paths.get(FILE_NAME);
        if (!Files.exists(newFile)) {
            Assert.fail("File " + FILE_NAME + " cannot be found !");
        } else {
            verifyFileLines(FILE_NAME, lines);
            Files.delete(newFile);
        }

    }

    // a method to fetch lines from file. technically it needed not to use the same techniqe the actual method under test uses.
    // this is just for demonstration purposes only...
    private void verifyFileLines(String fileName, List<String> originalLines) {
        try (FileInputStream fin = new FileInputStream(fileName)) {
            List<String> linesFromFile = IOUtils.readLines(fin, "utf-8");
            verifyMatchingLines(originalLines, linesFromFile);
        } catch (FileNotFoundException e) {
            Assert.fail("File foo.txt cannot be found !");
        } catch (IOException e) {
            Assert.fail("Error processing lines from file");
        }

    }

    private void verifyMatchingLines(List<String> originalLines, List<String> linesFromFile) {
        Assert.assertEquals(originalLines.size(), linesFromFile.size());
        for (int i = 0; i <linesFromFile.size(); i++) {
            Assert.assertEquals(originalLines.get(i), linesFromFile.get(i));
        }
    }

    private void createFileWithText(String fileName, List<String> lines) {
        try (FileOutputStream fos = new FileOutputStream(fileName)){
            IOUtils.writeLines(lines, IOUtils.LINE_SEPARATOR_WINDOWS, fos, Charset.defaultCharset());
        } catch (IOException e) {
            System.out.println("Error occur: while writing lines to file: " + fileName + ". Exception message: " + e.getMessage());
        }

    }

}

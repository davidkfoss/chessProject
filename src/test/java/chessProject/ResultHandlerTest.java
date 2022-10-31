package chessProject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ResultHandlerTest {
    private static IResultHandler resultHandler;
    private static File testFile;

    @BeforeAll
    public static void setUp() {
        resultHandler = new ResultHandler();
        testFile = new File("TestFile.txt");
    }

    @Test
    @DisplayName("Testing the method which writes to a text file")
    public void testWriteResult() {
        try {
            resultHandler.writeResult("TestFile", "This got written to the file 'TestFile'");
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        boolean written = false;

        try (Scanner scanner = new Scanner(testFile)) {
            if (scanner.hasNextLine()) {
                if (scanner.nextLine().equals("This got written to the file 'TestFile'")) {
                    written = true;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        try (PrintWriter writer = new PrintWriter(testFile)) {
            writer.print("");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assertTrue(written, "The text did not get written to the text file");   
    }

    @Test
    @DisplayName("Testing the method which reads from a text file")
    public void testReadResult() throws FileNotFoundException {

        try(PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(testFile, true)))) {
            writer.println("Can");
            writer.println("you");
            writer.println("read");
            writer.println("this?");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        String expectedString = "Previous matches:" + "\n\n" + "Can" + "\n" + "you" + "\n" + "read" + "\n" + "this?" + "\n";
        String actuaString = resultHandler.readResults("TestFile");

        try (PrintWriter writer = new PrintWriter(testFile)) {
            writer.print("");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assertEquals(expectedString, actuaString, "Strings mismatched");
        assertThrows(FileNotFoundException.class, () -> resultHandler.readResults("this_File_Does_Not_Exits"), "Should throw filenotfoundexception when trying to read from a non existing file");
    }

}

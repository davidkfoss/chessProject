package chessProject;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;




public class ResultHandler implements IResultHandler {

    @Override
    public String readResults(String filename) throws FileNotFoundException {
        String matchesString = "Previous matches:" + "\n\n";
        try (Scanner scanner = new Scanner(getFile(filename))) {
            while (scanner.hasNextLine()) {
                matchesString += scanner.nextLine() + "\n";
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            throw new FileNotFoundException("File " + filename + " does not exist");
        }
        return matchesString;
    }

    @Override
    public void writeResult(String filename, String gameState) throws FileNotFoundException {
        
        try(PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(getFile(filename), true)))) {
            writer.println(gameState);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    private static File getFile(String filename) {
        return new File(filename + ".txt");

    }

    
}

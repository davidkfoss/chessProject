package chessProject;

import java.io.FileNotFoundException;



public interface IResultHandler {

    public String readResults(String filename) throws FileNotFoundException;

    public void writeResult(String filename, String gameState) throws FileNotFoundException;
    
}

package chessProject;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;



public class Game {

    private Board board;
    private Rules rules;
    private boolean isWhitesTurn;
    private boolean isBlacksTurn;
    private String gameState;
    private IResultHandler write;
    private String gameStarted;

    public Rules getRules() {
        return rules;
    }


    public void move(int from, int to) throws FileNotFoundException {
        if (isGameOver()) {
            throw new IllegalAccessError("Game is over");
        }
        if (from < 0 || from > 63 || to < 0 || to > 63) {
            throw new IllegalArgumentException("Illegal to move from/to a non existing location");
        }
        if (from == to) {
            throw new IllegalArgumentException("Should not be able to move to the location you currently are occupying");
        }
        if (!getBoard().getSquareList().get(from).isOccupied()) {
            throw new IllegalAccessError("Have to move from a square occupied by a piece");
        }
        if (isWhitesTurn() && board.getSquareList().get(from).getOccupatorPiece().getTeam() != 'w') {
            throw new IllegalAccessError("Whites turn");
        }
        if (isBlacksTurn() && board.getSquareList().get(from).getOccupatorPiece().getTeam() != 'b') {
            throw new IllegalAccessError("Blacks turn");
        }

        Piece piece = board.getSquareList().get(from).getOccupatorPiece();
        if (!getRules().getLegalMoves(board, piece, true, true).contains(to)) { 
            throw new IllegalArgumentException("Illegal move");
        }
        if (board.getSquareList().get(to).isOccupied() && getRules().isOpposition(board.getSquareList().get(to), piece)) {
            board.getSquareList().get(to).getOccupatorPiece().setCaptured();
            if (board.getSquareList().get(to).getOccupatorPiece().getTeam() == 'w') {
                board.getWhitePieces().remove(board.getSquareList().get(to).getOccupatorPiece());
            }
            else {
                board.getBlackPieces().remove(board.getSquareList().get(to).getOccupatorPiece());
            }
            board.getSquareList().get(to).removeOccupation();
        }
        board.getSquareList().get(piece.getLocation()).removeOccupation();
        piece.setLocation(to);
        board.getSquareList().get(to).setOccupator(piece);
        
        setBlacksTurn();
        setWhitesTurn();
        if (isGameOver()) {
            write.writeResult("PlayedMatches", "Result: " + getGameState() + "\nGame started:  " + getGameStarted() + "\nGame ended: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()) + "\n\n");
        }

    }


    public boolean isGameOver() {
        if(isBlacksTurn()) {
            if (getRules().isCheckMateOnBlack()) {
                setGameState("WHITE WON!");
                return true;
            }
            else if (getRules().isStaleMateBlack()) {
                setGameState("REMIS\nby stalemate");
                return true;
            }
            else if (getRules().isInsufficientMaterial()) {
                setGameState("REMIS\nby insufficient\nmaterial");
                return true;
            }
        }
        else {
            if (getRules().isCheckMateOnWhite()) {
                setGameState("BLACK WON!");
                return true;
            }
            else if (getRules().isStaleMateWhite()) {
                setGameState("REMIS\nby stalemate");
                return true;
            }
            else if (getRules().isInsufficientMaterial()) {
                setGameState("REMIS\nby insufficient\nmaterial");
                return true;
            }
        }
        return false;
    }

    public void setGameState(String state) {
        this.gameState = state;
    }

    public String getGameState() {
        return gameState;
    }

    public Board getBoard() {
        return board;
    }

    private void setBlacksTurn() {
        this.isBlacksTurn = ! isBlacksTurn();
    }

    private void setWhitesTurn() {
        this.isWhitesTurn = ! isWhitesTurn();
    }

    public boolean isWhitesTurn() {
        return isWhitesTurn;
    }

    public boolean isBlacksTurn() {
        return isBlacksTurn;
    }

    private String getGameStarted() {
        return gameStarted;
    }

    public Game() {
        this.board = new Board();
        this.rules = new Rules(this.board);
        this.isWhitesTurn = true;
        this.isBlacksTurn = false;
        this.write = new ResultHandler();
        this.gameStarted = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
    }

    public static void main(String[] args) throws FileNotFoundException {
        Game game = new Game();
        game.move(12, 12+8);
        
    }
    
}

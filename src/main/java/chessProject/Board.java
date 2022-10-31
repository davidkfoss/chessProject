package chessProject;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private List<Square> squareList = new ArrayList<>();
    private List<Piece> whitePieces = new ArrayList<>();
    private List<Piece> blackPieces = new ArrayList<>();

    
    private void createBoard() {
        for (int i=0; i < 64; i++) {
            squareList.add(new Square(i));
        }
    }
    
    public List<Square> getSquareList() {
        return squareList;
    }

    public List<Piece> getBlackPieces() {
        return blackPieces;
    }

    public List<Piece> getWhitePieces() {
        return whitePieces;
    }

    public Board(List<Piece> black, List<Piece> white, List<Square> squares) {
        this.squareList = squares;
        this.blackPieces = black;
        this.whitePieces = white;
        setOccupied();
    }

    public void addPiece(Piece piece) {
        if (piece.getTeam() == 'w') {
            whitePieces.add(piece);
        }
        else {
            blackPieces.add(piece);
        }
        squareList.get(piece.getLocation()).setOccupator(piece);
    }

    private void createPieces() {
        for (int i=8; i<16; i++) {
            whitePieces.add(new Piece(i, 'w', 'p'));
        }
        for (int i=48; i<56; i++) {
            blackPieces.add(new Piece(i, 'b', 'p'));
        }
        whitePieces.add(new Piece(0, 'w', 'r'));
        whitePieces.add(new Piece(1, 'w', 'h'));
        whitePieces.add(new Piece(2, 'w', 'b'));
        whitePieces.add(new Piece(3, 'w', 'q'));
        whitePieces.add(new Piece(4, 'w', 'k'));
        whitePieces.add(new Piece(5, 'w', 'b'));
        whitePieces.add(new Piece(6, 'w', 'h'));
        whitePieces.add(new Piece(7, 'w', 'r'));
        blackPieces.add(new Piece(56, 'b', 'r'));
        blackPieces.add(new Piece(57, 'b', 'h'));
        blackPieces.add(new Piece(58, 'b', 'b'));
        blackPieces.add(new Piece(59, 'b', 'q'));
        blackPieces.add(new Piece(60, 'b', 'k'));
        blackPieces.add(new Piece(61, 'b', 'b'));
        blackPieces.add(new Piece(62, 'b', 'h'));
        blackPieces.add(new Piece(63, 'b', 'r'));
    }

    private void setOccupied() {
        for (Square square : squareList) {
            for (Piece piece : whitePieces) {
                if (piece.getLocation() == square.getSquareId()) {
                    square.setOccupator(piece);
                }
            }
            for (Piece piece : blackPieces) {
                if (piece.getLocation() == square.getSquareId()) {
                    square.setOccupator(piece);
                }
            }
        }
    }

    public Board() {
        this.createBoard();
        this.createPieces();
        this.setOccupied();
    }
    
}

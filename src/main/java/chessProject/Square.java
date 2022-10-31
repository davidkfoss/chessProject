package chessProject;

import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class Square {
    
    private int squareId;
    private boolean occupied;
    private Piece occupatorPiece;
    private AnchorPane pane;
    private ImageView pieceImage;


    public int getSquareId() {
        return this.squareId;
    }

    public AnchorPane getPane() {
        return this.pane;
    }

    public ImageView getImage() {
        return this.pieceImage;
    }

    public void setPieceImage(ImageView image) {
        this.pieceImage = image;
    }

    public Piece getOccupatorPiece() {
        if (this.isOccupied()) {
            return occupatorPiece;
        }
        else {
            throw new IllegalAccessError("Should not be able to call 'getOccupatorPiece' since this instance is not occupied");
        }
    }

    public void setOccupator(Piece piece) {
        if (piece.getLocation() == this.getSquareId()) {
            this.occupatorPiece = piece;
            this.occupied = true;
        }
        else {
            throw new IllegalArgumentException("Piece is not on this square");
        }
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void removeOccupation() {
        this.occupatorPiece = null;
        this.occupied = false;
    }

    public Square(int squareId) {
        if (squareId>= 0 && squareId <64) {
            this.squareId = squareId;
            this.occupied = false;
            this.pieceImage = new ImageView();
            this.pane = new AnchorPane();
        }
        else {
            throw new IllegalArgumentException("Illegal square id");
        }
    }

    public Square(Square square) {
        this.squareId = square.getSquareId();
        this.occupied = false;
        this.pieceImage = new ImageView();
        this.pane = new AnchorPane();
    }
}

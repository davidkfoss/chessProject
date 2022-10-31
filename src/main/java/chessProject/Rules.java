package chessProject;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Rules {
    private Board board;
    
    private static List<Integer> legalMovesWhitePawn = new ArrayList<Integer>();
    private static List<Integer> legalMovesBlackPawn = new ArrayList<Integer>();
    private static List<Integer> legalMovesRook = new ArrayList<Integer>();
    private static List<Integer> legalMovesBishop = new ArrayList<Integer>();
    private static List<Integer> legalMovesHorse = new ArrayList<Integer>();
    private static List<Integer> legalMovesKing = new ArrayList<Integer>();
    private static List<Integer> legalMovesQueen = new ArrayList<Integer>();

    private final static Integer[] orthogonally = {8,-8,1,-1};
    private final static Integer[] diagonally = {7,-7,9,-9};



    public Board getBoard() {
        return board;
    }
    private static List<Integer> getLegalMovesBishop() {
        return legalMovesBishop;
    }
    private static List<Integer> getLegalMovesBlackPawn() {
        return legalMovesBlackPawn;
    }
    private static List<Integer> getLegalMovesHorse() {
        return legalMovesHorse;
    }
    private static List<Integer> getLegalMovesKing() {
        return legalMovesKing;
    }
    private static List<Integer> getLegalMovesQueen() {
        return legalMovesQueen;
    }
    private static List<Integer> getLegalMovesRook() {
        return legalMovesRook;
    }
    private static List<Integer> getLegalMovesWhitePawn() {
        return legalMovesWhitePawn;
    }

    public List<Integer> getLegalMoves(Board board, Piece piece, boolean checkForSelfCheck, boolean checkCastling) {
        List<Integer> legalMoves = new ArrayList<Integer>();
        int location = piece.getLocation();
        List<Integer> legalDirections = getLegalDirections(piece);
        int destination = location;

        if (piece.getType() == 'h') {
            for (Integer move : legalDirections) {
                if(isValidDestination(location+move) && !isOutOfBounds(location+move, location, move, 1)) {
                    Square destinationSquare = board.getSquareList().get(location+move);
                    if (destinationSquare.isOccupied()) {
                        if (isOpposition(destinationSquare, piece)) {
                            legalMoves.add(destinationSquare.getSquareId());
                        }
                    }
                    else {
                        legalMoves.add(destinationSquare.getSquareId());
                    }
                }
            }
        }
        else if (piece.getType() == 'p') {
            if (piece.getTeam() == 'w') {
                if (isValidDestination(location+8)) {
                    Square destinationSquare = board.getSquareList().get(location+8);
                    if (!destinationSquare.isOccupied()) {
                        legalMoves.add(destinationSquare.getSquareId());
                        if (isValidDestination(location+16)) {
                            Square destinationSquare2 = board.getSquareList().get(location+16);
                            if (Math.floor(location/8) == 1 && !destinationSquare2.isOccupied()) {
                                legalMoves.add(destinationSquare2.getSquareId());
                            }
                        }
                    }
                }
                
            }
            else { 
                if (isValidDestination(location-8)) {
                    Square destinationSquare = board.getSquareList().get(location-8);
                    if (!destinationSquare.isOccupied()) {
                        legalMoves.add(destinationSquare.getSquareId());
                        if (isValidDestination(location-16)) {
                            Square destinationSquare2 = board.getSquareList().get(location-16);
                            if (Math.floor(location/8) == 6 && !destinationSquare2.isOccupied()) {
                                legalMoves.add(destinationSquare2.getSquareId());
                            }
                        }
                    }
                }
            }
            if (piece.getTeam() == 'w') {
                if (isValidDestination(location+7)) {
                    Square destinationSquare = board.getSquareList().get(location+7);
                    if (destinationSquare.isOccupied() && isOpposition(destinationSquare, piece) && (Math.floor((location-1)/8) == Math.floor(location/8))) {
                        legalMoves.add(destinationSquare.getSquareId());
                    }
                }
                if (isValidDestination(location+9)) {
                    Square destinationSquare2 = board.getSquareList().get(location+9);
                    if ((destinationSquare2.isOccupied() && isOpposition(destinationSquare2, piece) && (Math.floor((location+1)/8) == Math.floor(location/8)))) {
                        legalMoves.add(destinationSquare2.getSquareId());
                    }
                }
            }
            else {
                if (isValidDestination(location-9)) {
                    Square destinationSquare = board.getSquareList().get(location-9);
                    if (destinationSquare.isOccupied() && isOpposition(destinationSquare, piece) && (Math.floor((location-1)/8) == Math.floor(location/8))) {
                        legalMoves.add(destinationSquare.getSquareId());
                    }
                }
                if (isValidDestination(location-7)) {
                    Square destinationSquare2 = board.getSquareList().get(location-7);
                    if ((destinationSquare2.isOccupied() && isOpposition(destinationSquare2, piece) && (Math.floor((location+1)/8) == Math.floor(location/8)))) {
                        legalMoves.add(destinationSquare2.getSquareId());
                    }
                }
            }
        }
        else {
            for (Integer direction : legalDirections) {
                int k = 1;
                destination = location + direction*k;

                while (isValidDestination(destination) && !isOutOfBounds(destination, location, direction, k)) {
                    Square destinationSquare = board.getSquareList().get(destination);
                    if (destinationSquare.isOccupied()) {
                        if (isOpposition(destinationSquare, piece)) {
                            legalMoves.add(destinationSquare.getSquareId());
                        }
                        break;
                    }
                    else {
                        legalMoves.add(destinationSquare.getSquareId());
                    }
                    if (piece.getType() == 'k') {
                        if (checkCastling) {
                        if (canCastleLong(piece)) {
                            if (piece.getTeam() == 'w') {
                                List<Integer> castlingSquares = new ArrayList<>();
                                castlingSquares.add(2);
                                castlingSquares.add(3);
                                if (clearForSelfCheck(board, piece, castlingSquares).size() == 2) {
                                    legalMoves.add(2);
                                }
                            }
                            else {
                                List<Integer> castlingSquares = new ArrayList<>();
                                castlingSquares.add(58);
                                castlingSquares.add(59);
                                if (clearForSelfCheck(board, piece, castlingSquares).size() == 2) {
                                    legalMoves.add(58);
                                }                            
                            }
                        }
                        if (canCastleShort(piece)) {
                            if (piece.getTeam() == 'w') {
                                List<Integer> castlingSquares = new ArrayList<>();
                                castlingSquares.add(5);
                                castlingSquares.add(6);
                                if (clearForSelfCheck(board, piece, castlingSquares).size() == 2) {
                                    legalMoves.add(6);
                                }                            
                            }
                            else {
                                List<Integer> castlingSquares = new ArrayList<>();
                                castlingSquares.add(61);
                                castlingSquares.add(62);
                                if (clearForSelfCheck(board, piece, castlingSquares).size() == 2) {
                                    legalMoves.add(62);
                                }                            
                            }
                        }
                        }
                        break;
                    }
                    k++;
                    destination = location + direction*k;
                }
            }
        }
        if (checkForSelfCheck) {
            List<Integer> legalMovesChecked = new ArrayList<Integer>(clearForSelfCheck(board, piece, legalMoves));
            return new ArrayList<Integer>(legalMovesChecked);    
        }
        return new ArrayList<Integer>(legalMoves);    
    }

    
    private boolean canCastleLong(Piece piece) {
        if (piece.getTeam() == 'w') {
            if (!isCheckOnWhite(getBoard())) {
                if (!piece.getMoved()) {
                    for (int i = 1; i<4; i++) {
                        if (getBoard().getSquareList().get(i).isOccupied()) {
                            return false;
                        }
                    }
                    if (getBoard().getSquareList().get(0).isOccupied()) {
                        if (!getBoard().getSquareList().get(0).getOccupatorPiece().getMoved()) {
                            return true;
                        }
                    }

                }
            }
            return false;
        }
        else {
            if (!isCheckOnBlack(getBoard())) {
                if (!piece.getMoved()) {
                    for (int i = 57; i<60; i++) {
                        if (getBoard().getSquareList().get(i).isOccupied()) {
                            return false;
                        }
                    }
                    if (getBoard().getSquareList().get(56).isOccupied()) {
                        if (!getBoard().getSquareList().get(56).getOccupatorPiece().getMoved()) {
                            return true;
                        }
                    }

                }
            }
            return false;
        }
    }

    private boolean canCastleShort(Piece piece) {
        if (piece.getTeam() == 'w') {
            if (!isCheckOnWhite(getBoard())) {
                if (!piece.getMoved()) {
                    for (int i = 5; i<7; i++) {
                        if (getBoard().getSquareList().get(i).isOccupied()) {
                            return false;
                        }
                    }
                    if (getBoard().getSquareList().get(7).isOccupied()) {
                        if (!getBoard().getSquareList().get(7).getOccupatorPiece().getMoved()) {
                            return true;
                        }
                    }

                }
            }
            return false;
        }
        else {
            if (!isCheckOnBlack(getBoard())) {
                if (!piece.getMoved()) {
                    for (int i = 61; i<63; i++) {
                        if (getBoard().getSquareList().get(i).isOccupied()) {
                            return false;
                        }
                    }
                    if (getBoard().getSquareList().get(63).isOccupied()) {
                        if (!getBoard().getSquareList().get(63).getOccupatorPiece().getMoved()) {
                            return true;
                        }
                    }

                }
            }
            return false;
        }
    }



    private List<Integer> clearForSelfCheck(Board board, Piece piece, List<Integer> list) {
        List<Integer> dupList = new ArrayList<Integer>(list);
        List<Integer> dupList2 = new ArrayList<Integer>(list);
        if (dupList.size() == 0) {
            return new ArrayList<Integer>(dupList);
        }    
        for (Integer move : dupList) {
            Board dummyBoard = cloneBoard(board);
            dummyMove(dummyBoard, dummyBoard.getSquareList().get(piece.getLocation()).getOccupatorPiece(), move);
            if(piece.getTeam() == 'w') {
                if (isCheckOnWhite(dummyBoard)) {
                    dupList2.remove(move);
                }
            }
            else {
                if (isCheckOnBlack(dummyBoard)) {
                    dupList2.remove(move);
                }
            }
        }
        return new ArrayList<Integer>(dupList2);    
    }

    private boolean isValidDestination(int num) {
        if (num < 64 && num >= 0) {
            return true;
        }
        else {
            return false;
        }
    }

    private Board cloneBoard(Board board) {
        List<Piece> cloneWhite = new ArrayList<Piece>(board.getWhitePieces().size());
        for (Piece clone : board.getWhitePieces()) {
            cloneWhite.add(new Piece(clone));
        }
        List<Piece> cloneBlack = new ArrayList<Piece>(board.getBlackPieces().size());
        for (Piece clone : board.getBlackPieces()) {
            cloneBlack.add(new Piece(clone));
        }
        List<Square> cloneSqares = new ArrayList<Square>(board.getSquareList().size());
        for (Square clone : board.getSquareList()) {
            cloneSqares.add(new Square(clone));
        }
        return new Board(cloneBlack, cloneWhite, cloneSqares);
    }

    private void dummyMove(Board dummy, Piece dummyPiece, Integer destination) {
        if (!isValidDestination(destination)) {
            throw new IllegalArgumentException("Invalid destination");
        }
        if (dummy.getSquareList().get(destination).isOccupied() && isOpposition(dummy.getSquareList().get(destination), dummyPiece)) {
            dummy.getSquareList().get(destination).getOccupatorPiece().setCaptured();
            if (dummy.getSquareList().get(destination).getOccupatorPiece().getTeam() == 'w') {
                dummy.getWhitePieces().remove(dummy.getSquareList().get(destination).getOccupatorPiece());
            }
            else {
                dummy.getBlackPieces().remove(dummy.getSquareList().get(destination).getOccupatorPiece());
            }
            dummy.getSquareList().get(destination).removeOccupation();
        }
        dummy.getSquareList().get(dummyPiece.getLocation()).removeOccupation();
        dummyPiece.setLocation(destination);
        dummy.getSquareList().get(destination).setOccupator(dummyPiece);    
    }

    public boolean isCheckMateOnWhite() {
        if (isCheckOnWhite(getBoard())) {
            int temp = 0;
            for (Piece myPiece : board.getWhitePieces()) {
                if (getLegalMoves(board, myPiece, true, false).size() > 0) {
                    temp += 1;
                } 
            }
            if (temp == 0) {
                return true;
            }
        }
        return false;
    }

    public boolean isCheckMateOnBlack() {
        if (isCheckOnBlack(getBoard())) {
            int temp = 0;
            for (Piece myPiece : board.getBlackPieces()) {
                if (getLegalMoves(board, myPiece, true, false).size() > 0) {
                    temp += 1;
                } 
            }
            if (temp == 0) {
                return true;
            }
        }
        return false;
    }

    public boolean isStaleMateWhite() {
        int temp = 0;
        for (Piece myPiece : board.getWhitePieces()) {
            if (getLegalMoves(board, myPiece, true, false).size() > 0) {
                temp += 1;
            } 
        }
        if (temp == 0) {
            return true;
        }
        return false;
    }

    public boolean isStaleMateBlack() {
        int temp = 0;
        for (Piece myPiece : board.getBlackPieces()) {
            if (getLegalMoves(board, myPiece, true, false).size() > 0) {
                temp += 1;
            } 
        }
        if (temp == 0) {
            return true;
        }
        return false;
    }


    private boolean isOutOfBounds(int destination, int orgin, int direction, int k) {
        if (!isValidDestination(destination) || !isValidDestination(orgin)) {
            throw new IllegalArgumentException("Illegal destination or orgin");
        }
        if ((direction == -1 || direction == 1) && (Math.floor(orgin/8) != Math.floor(destination/8))) {
            return true;
        }
        if ((direction == 9 || direction == -7) && Math.floor((orgin+k)/8) != Math.floor(orgin/8)) {
            return true;
        }
        if ((direction == 7 || direction == -9) && (Math.floor((orgin-k)/8) != Math.floor(orgin/8) || (orgin-k) < 0)) {
            return true;
        }
        if ((direction == 17 || direction == -15) && Math.floor((orgin+1)/8) != Math.floor(orgin/8)) {
            return true;
        }
        if ((direction == 10 || direction == -6) && Math.floor((orgin+2)/8) != Math.floor(orgin/8)) {
            return true;
        }
        if ((direction == -17 || direction == 15) && ((Math.floor((orgin-1)/8) != Math.floor(orgin/8)) || !isValidDestination(orgin-1))) {
            return true;
        }
        if ((direction == -10 || direction == 6) && ((Math.floor((orgin-2)/8) != Math.floor(orgin/8)) || !isValidDestination(orgin-2))) {
            return true;
        }
        return false;
    }

    public boolean isOpposition(Square destinationSquare, Piece piece) {
        if (destinationSquare.isOccupied()) {
            if (destinationSquare.getOccupatorPiece().getTeam() != piece.getTeam()) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    private List<Integer> getLegalDirections(Piece piece) {
        if (piece.getType() == 'p') {
            if (piece.getTeam() == 'b') {
                return new ArrayList<Integer>(getLegalMovesBlackPawn());
            }
            if (piece.getTeam() == 'w') {
                return new ArrayList<Integer>(getLegalMovesWhitePawn());
            }
        }
        else if (piece.getType() == 'r') {
            return new ArrayList<Integer>(getLegalMovesRook());
        }
        else if (piece.getType() == 'b') {
            return new ArrayList<Integer>(getLegalMovesBishop());
        }
        else if (piece.getType() == 'h') {
            return new ArrayList<Integer>(getLegalMovesHorse());
        }
        else if (piece.getType() == 'q') {
            return new ArrayList<Integer>(getLegalMovesQueen());
        }
        else if (piece.getType() == 'k') {
            return new ArrayList<Integer>(getLegalMovesKing());
        }
        else {
            throw new IllegalArgumentException("Invalid piece detected!");
        }
        return null;
    }


    private void addLegalDirections() {
        for (Integer move : diagonally) {
            legalMovesBishop.add(move);
            legalMovesQueen.add(move);
            legalMovesKing.add(move);
        }
        for (Integer move : orthogonally) {
            legalMovesQueen.add(move);
            legalMovesRook.add(move);
            legalMovesKing.add(move);
        }
        legalMovesHorse.addAll(Arrays.asList(10,-10,15,-15,17,-17,6,-6));
        legalMovesWhitePawn.add(8);
        legalMovesBlackPawn.add(-8);
    
    }


    public boolean isCheckOnBlack(Board checkBoard) {
        for (Piece whitePiece : checkBoard.getWhitePieces()) {
            for (Integer move : getLegalMoves(checkBoard, whitePiece, false, false)) {
                if (checkBoard.getSquareList().get(move).isOccupied() && checkBoard.getSquareList().get(move).getOccupatorPiece().getType() == 'k' && isOpposition(checkBoard.getSquareList().get(move), whitePiece)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isCheckOnWhite(Board checkBoard) {
        for (Piece blackPiece : checkBoard.getBlackPieces()) {
            for (Integer move : getLegalMoves(checkBoard, blackPiece, false, false)) {
                if (checkBoard.getSquareList().get(move).isOccupied() && checkBoard.getSquareList().get(move).getOccupatorPiece().getType() == 'k' && isOpposition(checkBoard.getSquareList().get(move), blackPiece)) {
                    return true;
                } 
            }
        }
        return false;
    }

    public boolean isInsufficientMaterial() {
        if (board.getBlackPieces().size() <=2 && board.getWhitePieces().size() <= 2) {
            int insufficient = 0;
            for (Piece piece : board.getBlackPieces()) {
                if (piece.getType() == 'b' || piece.getType() == 'h') {
                    insufficient++;
                }
            }
            for (Piece piece : board.getWhitePieces()) {
                if (piece.getType() == 'b' || piece.getType() == 'h') {
                    insufficient++;
                }
            }
            if (insufficient == 2 || (board.getBlackPieces().size() == 1 && board.getWhitePieces().size() == 1)) {
                return true;
            }
        }
        return false;
    }

    public Rules(Board board) {
        this.board = board;
        addLegalDirections();
    }

    public static void main(String[] args) {

    }
    
}

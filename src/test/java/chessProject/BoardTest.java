package chessProject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;



import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BoardTest {
    static Board board;

    @BeforeAll
    public static void setUp() {
        board = new Board();
    }

    @Test
    @DisplayName("Testing that the private method createBoard, works as expected")
    public void testCreateBoard() {
        assertEquals(64, board.getSquareList().size(), "SquareList should contain 64 Square objects");

        int i = 0;
        for (Square square : board.getSquareList()) {
            assertEquals(i, square.getSquareId());
            i++;
        }
    }

    @Test
    @DisplayName("Testing that the private method createPieces, works as expected")
    public void testCreatePieces() {
        assertEquals(16, board.getBlackPieces().size(), "The list blackPieces should contain 16 Piece objects");
        assertEquals(16, board.getWhitePieces().size(), "The list whitePieces should contain 16 Piece objects");

        for (Piece piece : board.getBlackPieces()) {
            assertEquals('b', piece.getTeam(), "All Piece objects in the blackPieces list should have the state with 'b' as team");
        }
        for (Piece piece : board.getWhitePieces()) {
            assertEquals('w', piece.getTeam(), "All Piece objects in the whitePieces list should have the state with 'w' as team");
        }

        //Using lambda streams to get the count of each Piece type from each Piece in the lists blackPieces and whitePieces 
        int pawnCount = board.getBlackPieces().stream().filter(e -> (e.getType() == 'p')).toList().size() + board.getWhitePieces().stream().filter(e -> (e.getType() == 'p')).toList().size();
        int horseCount = board.getBlackPieces().stream().filter(e -> (e.getType() == 'h')).toList().size() + board.getWhitePieces().stream().filter(e -> (e.getType() == 'h')).toList().size();
        int rookCount = board.getBlackPieces().stream().filter(e -> (e.getType() == 'r')).toList().size() + board.getWhitePieces().stream().filter(e -> (e.getType() == 'r')).toList().size();
        int bishopCount = board.getBlackPieces().stream().filter(e -> (e.getType() == 'b')).toList().size() + board.getWhitePieces().stream().filter(e -> (e.getType() == 'b')).toList().size();
        int kingCount = board.getBlackPieces().stream().filter(e -> (e.getType() == 'k')).toList().size() + board.getWhitePieces().stream().filter(e -> (e.getType() == 'k')).toList().size();
        int queenCount = board.getBlackPieces().stream().filter(e -> (e.getType() == 'q')).toList().size() + board.getWhitePieces().stream().filter(e -> (e.getType() == 'q')).toList().size();

        assertEquals(16, pawnCount, "Wrong amount of pawns on the board");
        assertEquals(4, horseCount, "Wrong amount of knights on the board");
        assertEquals(4, rookCount, "Wrong amount of rooks on the board");
        assertEquals(4, bishopCount, "Wrong bishops of pawns on the board");
        assertEquals(2, kingCount, "Wrong amount of kings on the board");
        assertEquals(2, queenCount, "Wrong amount of queens on the board");
    }

    @Test
    @DisplayName("Testing that the private method setOccupied, works as expected")
    public void testSetOccupied() {
        for (int i = 0; i<16; i++) {
            assertTrue(board.getSquareList().get(i).isOccupied(), "First 16 squares should be occupied (starting at 0)");
        }
        for (int i = 16; i<48; i++) {
            assertFalse(board.getSquareList().get(i).isOccupied(), "Squares from between 16 and 48 should not be occupied");
        } 
        for (int i = 48; i<64; i++) {
            assertTrue(board.getSquareList().get(i).isOccupied(), "Last 16 squares should be occupied");
        }

        //Testing one Piece
        assertEquals('r', board.getSquareList().get(7).getOccupatorPiece().getType(), "Piece should be rook");
    }
    
}

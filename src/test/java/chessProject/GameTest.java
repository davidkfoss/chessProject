package chessProject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GameTest {
    Game game;

    @BeforeEach
    public void setUp() {
        game = new Game();
    }

    @Test
    @DisplayName("Testing the private methods: 'setWhitesTurn' and 'setBlacksTurn', and the public methods: 'isWhitesTurn' and 'isBlacksTurn'")
    public void testAlternatingTurns() throws FileNotFoundException {
        assertEquals(true, game.isWhitesTurn(), "White always begins");
        assertEquals(false, game.isBlacksTurn(), "Black never begins");

        game.move(12, 12+8);

        assertEquals(false, game.isWhitesTurn(), "White has just moved, so it is blacks turn");
        assertEquals(true, game.isBlacksTurn(), "It is blacks turn");        
    }

    @Test
    @DisplayName("Testing the method 'isGameOver'")
    public void testIsGameOver() throws FileNotFoundException {
        game.move(13, 13+8);
        game.move(52, 52-8);
        game.move(14, 14+16);
        
        assertFalse(game.isGameOver(), "Game is not over since there is not checkamte nor stalemate");

        game.move(59, 31); //Checkmate

        assertTrue(game.isGameOver(), "Game is over, as there is checkmate");
    }

    @Test
    @DisplayName("Testing the method 'move'")
    public void testMove() throws FileNotFoundException {
        assertThrows(IllegalArgumentException.class, () -> game.move(0, 0), "Should throw exception when trying to move onto the same location");
        assertThrows(IllegalArgumentException.class, () -> game.move(-1, -9), "Negative input illegal");
        assertThrows(IllegalArgumentException.class, () -> game.move(12, 21), "Illegal move for pawn on square 12");
        assertThrows(IllegalAccessError.class, () -> game.move(20, 20+8), "There is no piece on square 20, and thus there is nothing to move");

        game.move(1, 18);

        assertFalse(game.getBoard().getSquareList().get(1).isOccupied(), "Occupation from square 1 should be removed");
        assertTrue(game.getBoard().getSquareList().get(18).isOccupied(), "Piece moved onto square 18 and should therefore be occupied");

        assertThrows(IllegalAccessError.class, () -> game.move(18, 1), "Blacks turn");
        game.move(48, 40);
        assertThrows(IllegalAccessError.class, () -> game.move(40, 40-8), "Whites turn");

        Game game2 = new Game();
        game2.move(13, 13+8);
        game2.move(52, 52-8);
        game2.move(14, 14+16);
        game2.move(59, 31); //Checkmate

        assertThrows(IllegalAccessError.class, () -> game2.move(8, 8+8), "Game is over");

        Game game3 = new Game();
        game3.move(12, 12+16);
        game3.move(51, 51-16);
        game3.move(12+16, 51-16); //Capturing piece

        assertTrue(game3.getBoard().getSquareList().get(51-16).getOccupatorPiece().getTeam() == 'w', "Old occupatorpiece should be removed and new added when capturing a piece");
    }


    
}

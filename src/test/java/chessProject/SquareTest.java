package chessProject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SquareTest {

    Square square;

    @Test
    @DisplayName("Testing that constructor behaves correctly")
    public void testConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new Square(64), "Only values from between 0 and up to 63 are be allowed");

        Square s1 = new Square(0);
        assertEquals(0, s1.getSquareId(), "Wrong square id returned");
    }

    @BeforeEach
    public void setUp() {
        square = new Square(20);
    }

    @Test
    @DisplayName("Testing that setOccupator behaves correctly")
    public void testSetOccupator() {
        Piece p1 = new Piece(19, 'w', 'p');
        assertThrows(IllegalArgumentException.class, () -> square.setOccupator(p1), "The Piece trying to be set as occupator on this Square, is not located on this Square");
        assertFalse(square.isOccupied(), "Square is not occupied, but returns that it is");

        Piece p2 = new Piece(20, 'w', 'p');
        square.setOccupator(p2);
        assertTrue(square.isOccupied());
        assertEquals(p2, square.getOccupatorPiece(), "Wrong/No Piece returned when a specific Piece was expected");
    }

    @Test
    @DisplayName("Testing that 'removeOccupation' and 'getOccupatorPiece' behave correctly")
    public void testRemoveOccupation() {
        Piece p2 = new Piece(20, 'w', 'p');
        square.setOccupator(p2);

        assertEquals(p2, square.getOccupatorPiece(), "Should return the piece, after setting it as occupator of given square");
        
        square.removeOccupation();
        
        assertThrows(IllegalAccessError.class, () -> square.getOccupatorPiece(), "Square is not occupied anymore");
        assertFalse(square.isOccupied(), "There is no Piece occupying this Square anymore");
    }


    
}

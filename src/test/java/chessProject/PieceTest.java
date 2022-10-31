package chessProject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PieceTest {

    @Test
    @DisplayName("Testing that the constructor and it's private validation methods behave as exptected")
    public void testConstructor() {
        Piece p1 = new Piece(3, 'w', 'q');
        assertEquals(3, p1.getLocation());
        assertEquals(false, p1.getCaptured());
        assertEquals('w', p1.getTeam());
        assertEquals('q', p1.getType());

        assertThrows(IllegalArgumentException.class, () -> new Piece(-2, 'w', 'p'), "isValidLocation method should return false, and followingly make constructor throw illegalArgumentException");
        assertThrows(IllegalArgumentException.class, () -> new Piece(0, 'q', 'q'), "Invalid input for team parameter should make constructor throw illegalArgumentException");
        assertThrows(IllegalArgumentException.class, () -> new Piece(0, 'b', 'i'), "isValidType method should return false, and followingly make constructor throw illegalArgumentException");
    }
    
}

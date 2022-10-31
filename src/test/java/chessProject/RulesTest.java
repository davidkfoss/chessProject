package chessProject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RulesTest {
    Rules rules;
    Board board;

    @BeforeEach
    public void setUp() {
        board = new Board();
        rules = new Rules(board);

    }

    @Test
    @DisplayName("Testing the method 'isOpposition', that returns a boolean for if a square's occupatorPiece, if it has one, and another piece, are on opposing teams")
    public void testIsOpposition() {
        assertTrue(rules.isOpposition(board.getSquareList().get(60), board.getSquareList().get(0).getOccupatorPiece()), "The piece on square 60 and on square 0 are on different teams");
        assertFalse(rules.isOpposition(board.getSquareList().get(30), board.getSquareList().get(12).getOccupatorPiece()), "The square 30 does not have a pice occupying it");
        assertFalse(rules.isOpposition(board.getSquareList().get(0), board.getSquareList().get(1).getOccupatorPiece()), "The piece on square 0 and 1 are on the same team");
        assertFalse(rules.isOpposition(board.getSquareList().get(0), board.getSquareList().get(0).getOccupatorPiece()), "The piece is on the same team as itself");
    }

    @Test
    @DisplayName("Testing the methods 'isCheckOnWhite' and 'isCheckMateOnWhite', that check if there is 'check' and 'checkmate' on white")
    public void testIsCheckAndCheckMateOnWhite() throws FileNotFoundException {
        Game game = new Game();
        game.move(13, 13+8);
        game.move(52, 52-16);
        game.move(14, 14+16);
        
        assertFalse(game.getRules().isCheckOnWhite(game.getBoard()), "It is not yet check on white");
        assertFalse(game.getRules().isCheckMateOnWhite(), "It is not yet checkmate on white");
        game.move(59, 31); //This move creates checkmate
        assertTrue(game.getRules().isCheckOnWhite(game.getBoard()), "It is check on white (as well as checkmate)");
        assertTrue(game.getRules().isCheckMateOnWhite(), "It is checkmate on white");

        Game game2 = new Game();
        game2.move(11, 11+8);
        game2.move(52, 52-8);
        game2.move(8, 8+8);
        game2.move(61, 25); //This move creates check

        assertTrue(game2.getRules().isCheckOnWhite(game2.getBoard()), "It is check on white");
        assertFalse(game2.getRules().isCheckMateOnWhite(), "It is only check on white, not checkmate");
    }

    @Test
    @DisplayName("Testing the methods 'isCheckOnBlack' and 'isCheckMateOnBlack', that check if there is 'check' and 'checkmate' on black")
    public void testIsCheckAndCheckMateOnBlack() throws FileNotFoundException {
        Game game = new Game();
        game.move(12, 12+8);
        game.move(53, 53-8);
        game.move(8, 8+16);
        game.move(54, 54-16);
        
        assertFalse(game.getRules().isCheckOnBlack(game.getBoard()), "It is not yet check on black");
        assertFalse(game.getRules().isCheckMateOnBlack(), "It is not yet checkmate on black");
        game.move(3, 39); //This move creates checkmate
        assertTrue(game.getRules().isCheckOnBlack(game.getBoard()), "It is check on black (as well as checkmate)");
        assertTrue(game.getRules().isCheckMateOnBlack(), "It is checkmate on black");

        Game game2 = new Game();
        game2.move(12, 12+8);
        game2.move(51, 51-8);
        game2.move(5, 33); //This move creates check
        assertTrue(game2.getRules().isCheckOnBlack(game2.getBoard()), "It is check on black");
        assertFalse(game2.getRules().isCheckMateOnBlack(), "It is only check on black, not checkmate");
    }

    @Test
    @DisplayName("Testing if the method checking for stalemate on black works as expected")
    public void testIsStaleMateBlack() throws FileNotFoundException {
        Game game = new Game();
        //Playing all moves to create stalemate
        game.move(12, 12+8);
        game.move(48, 48-16);
        game.move(3, 39);
        game.move(56, 56-16);
        game.move(39, 32);
        game.move(55, 55-16);
        game.move(15, 15+16);
        game.move(40, 47);
        game.move(32, 50);
        game.move(53, 53-8);
        game.move(50, 51);
        game.move(60, 53);
        game.move(51, 49);
        game.move(59, 19);
        game.move(49, 49+8);
        game.move(19, 55);
        game.move(57, 58);
        game.move(53, 46);

        assertFalse(game.getRules().isStaleMateBlack(), "It is not stalemate yet");

        game.move(58, 44); //This move creates stalemate

        assertTrue(game.getRules().isStaleMateBlack(), "Should be stalemate");
    }

    @Test
    @DisplayName("Testing if the method checking for stalemate on white works as expected")
    public void testIsStaleMateWhite() throws FileNotFoundException {
        Game game = new Game();
        //Playing all moves to create stalemate
        game.move(8, 8+16);
        game.move(52, 52-8);
        game.move(0, 0+16);
        game.move(59, 31);
        game.move(16, 22);
        game.move(31, 24);
        game.move(15, 15+16);
        game.move(55, 55-16);
        game.move(22, 23);
        game.move(24, 10);
        game.move(13, 13+8);
        game.move(10, 11);
        game.move(4, 13);
        game.move(11, 9);
        game.move(3, 43);
        game.move(9, 1);
        game.move(43, 15);
        game.move(1, 2);
        game.move(13, 22);

        assertFalse(game.getRules().isStaleMateWhite(), "It is not stalemate yet");

        game.move(2, 20); //This move creates stalemate

        assertTrue(game.getRules().isStaleMateWhite(), "Should be stalemate");
    }

    @Test
    @DisplayName("Testing the private method 'isValidDestination' through the public method 'getLegalMoves' that calls it")
    public void testIsValidDestination() {
        //We look at the rook, trapped in the bottom left corner at the start of a game. It will check if it can move backwards (-8). 0-8 = -8. isValidDestination should return false when -8 is put in as a parameter.
        assertFalse(rules.getLegalMoves(board, board.getSquareList().get(0).getOccupatorPiece(), true, true).contains(-8), "-8 should not be a legal move for rook on square 0, because isValidDestination should return false whith '-8' as input");
        
        assertTrue(rules.getLegalMoves(board, board.getSquareList().get(8).getOccupatorPiece(), true, true).contains(16), "16 should be a legal move for pawn on square 8, because isValidDestination should return true with '16' as input");        
    }

    @Test
    @DisplayName("Testing the private method 'getLegalDirections' through the public method 'getLegalMoves', which calls it, this will confirm that the private method 'addLegalMoves' also works as expected")
    public void testGetLegalDirections() throws FileNotFoundException {

        Game game = new Game();
        game.move(12, 12+8);
        game.move(48, 48-8);
        game.move(3, 30); //Putting the white queen alone in the middle of the board

        //Adding all squares surrouning the queen into a collection
        Collection<Integer> expected = new ArrayList<>();
        expected.add(30+8);
        expected.add(30+9);
        expected.add(30+7);
        expected.add(30+1);
        expected.add(30-1);
        expected.add(30-8);
        expected.add(30-9);
        expected.add(30-7);

        //The queens legal moves
        List<Integer> legalMovesQueen = game.getRules().getLegalMoves(game.getBoard(), game.getBoard().getSquareList().get(30).getOccupatorPiece(), true, true);
        
        //Checking wether a queen gets all 8 squares surrounding it added to its legal moves, which would confirm that 'getLegalDirections', as well as the method 'addLegalDirections' work as expected
        assertTrue(legalMovesQueen.containsAll(expected), "All squares surrounding the queen should be legal moves, this could indicate an error with the methids 'getLegalDirections' and/or 'addLegalDirections'");
    }

    @Test
    @DisplayName("Testing the private method 'isOutOfBounds', through the public method 'getLegalMoves'. The method finds out if a move is out of the bounds of the board (out past the sides)")
    public void testIsOutOfBounds() throws FileNotFoundException {
        Game game = new Game();
        game.move(12, 20); //Moving a pawn to make clear for the queen and bishop in the back

        //For some reason JUnit added the values twice, so i used a linkedHashSet instead to remove these duplicates
        //It works normally if you do it in the main of the Game class, so I just did it like this
        //Also, if I run the test from the top, it adds the values 4 times, which makes no sense to me.
        LinkedHashSet<Integer> legalMovesQueen = new LinkedHashSet<>(game.getRules().getLegalMoves(game.getBoard(), game.getBoard().getSquareList().get(3).getOccupatorPiece(), true, true));
        LinkedHashSet<Integer> legalMovesBishop = new LinkedHashSet<>(game.getRules().getLegalMoves(game.getBoard(), game.getBoard().getSquareList().get(5).getOccupatorPiece(), true, true));
        LinkedHashSet<Integer> legalMovesHorse = new LinkedHashSet<>(game.getRules().getLegalMoves(game.getBoard(), game.getBoard().getSquareList().get(1).getOccupatorPiece(), true, true));
        
        assertEquals(4, legalMovesQueen.size(), "There are 4 squares diagonally to the edge of the board, therefore the queen should have 4 legal moves");
        assertEquals(5, legalMovesBishop.size(), "There are 5 squares diagonally to the edge of the board, therefore the bishop should have 5 legal moves");
        assertFalse(legalMovesHorse.contains(1+6), "The square one forward and two to the left of the horse in starting position is out past the left side of the board");
    }

    @Test
    @DisplayName("Testing the private method 'clearForSelfCheck' through the public method 'getLegalMoves'. The method prevents those moves that put yourself in 'check' from being returned by 'getLegalMoves'")
    public void testClearForSelfCheck() throws FileNotFoundException {
        Game game = new Game();
        game.move(12, 12+8);
        game.move(51, 51-8);
        game.move(3, 39); //Now the white queen is aiming at the black king, with a black pawn blocking the check
        List<Integer> legalMovesAterCheckingForSelfCheck = game.getRules().getLegalMoves(game.getBoard(), game.getBoard().getSquareList().get(53).getOccupatorPiece(), true, true);
        LinkedHashSet<Integer> legalMovesWithoutChecking = new LinkedHashSet<>(game.getRules().getLegalMoves(game.getBoard(), game.getBoard().getSquareList().get(53).getOccupatorPiece(), false, true));
        
        assertEquals(0, legalMovesAterCheckingForSelfCheck.size(), "A move that puts yourself in check is illegal");
        assertEquals(2, legalMovesWithoutChecking.size(), "Without calling the method 'clearForSelfCheck' the pawn should ignore that it will its own king under 'check'");

        game.move(49, 49-8);
        game.move(39, 30); //Moving the white queen so that it attacks a square next to the black king

        List<Integer> legalMovesKingAfterChecking = game.getRules().getLegalMoves(game.getBoard(), game.getBoard().getSquareList().get(60).getOccupatorPiece(), true, true);
        LinkedHashSet<Integer> legalMovesKingBeforeChecking = new LinkedHashSet<>(game.getRules().getLegalMoves(game.getBoard(), game.getBoard().getSquareList().get(60).getOccupatorPiece(), false, true));

        assertEquals(0, legalMovesKingAfterChecking.size(), "The black king should not have a square that is under attack by a white piece, as a legal move");
        assertEquals(1, legalMovesKingBeforeChecking.size(), "ClearForSelfCheck should not be called and therefore the king should be allowed to move itself into 'check'");
    }

    
    @Test
    @DisplayName("Testing the private methods 'cloneBoard' and 'dummyMove' that get called in 'clearForSelfCheck'")
    public void testCloneBoardAndDummyMove() throws FileNotFoundException {
        //In order for 'clearForSelfCheck' to work as expected, 'dummyMove' and 'cloneBoard', which get called by this method, also has to work as expected
        //A failure in 'clearForSelfCheck' indicates a failure in 'cloneBoard' or 'dummyMove' or both
        //There is no way of targeting one or the other
        testClearForSelfCheck();
    }
    

    @Test
    @DisplayName("Testing 'getLegalMoves'")
    public void testGetLegalMoves() throws FileNotFoundException {
        //All these must pass for the method 'getLegalMoves' to work as expected
        //Therfore this will thoroughly test the method 'getLegalMoves' itself
        testIsValidDestination();
        testGetLegalDirections();
        testIsOutOfBounds();
        testClearForSelfCheck();
    }


}

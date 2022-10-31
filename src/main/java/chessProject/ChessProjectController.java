package chessProject;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;

import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;






public class ChessProjectController {
    
    private Game game;
    private boolean clicked = false;
    private List<Square> legalSquares = new ArrayList<>();
    private int moveFrom;
    private IResultHandler resultHandler = new ResultHandler();
    
    
    @FXML GridPane gridBoard;
    @FXML MenuItem startNewGame;
    @FXML MenuItem showPreviousMatches;
    @FXML MenuItem clearPreviousMatches;

    //AudioClip moveSound = new AudioClip(getClass().getResource("moveSound.mp3").toExternalForm());




    
    @FXML
    private void setPreviousMatchesStage() throws FileNotFoundException {
        Text matches = new Text();
        String text;
        try {
            text = resultHandler.readResults("PlayedMatches");
        }
        catch (FileNotFoundException e) {
            text = "No previous matches found";
            e.printStackTrace();
        }
        matches.setText(text);
        BorderPane previousMatches = new BorderPane();
        previousMatches.setCenter(matches);
        previousMatches.setPadding(new Insets(20,20,20,20));
        ScrollPane scrollPane = new ScrollPane(previousMatches);
        scrollPane.setMaxHeight(500);
        Scene scene = new Scene(scrollPane); 		                        
        Stage stage = new Stage(); 		                        
        stage.setScene(scene); 		                        
        stage.setTitle("Previous matches");
        stage.showAndWait();
    }
    
    @FXML
    private EventHandler<ActionEvent> handleShowPreviousMatches = new EventHandler<ActionEvent>() { 
        @Override
        public void handle(ActionEvent event) {
            try {
                setPreviousMatchesStage();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    };

    @FXML
    private void initialize() throws FileNotFoundException {
        startNewGame.setOnAction(handleNewGame);
        showPreviousMatches.setOnAction(handleShowPreviousMatches);
        clearPreviousMatches.setOnAction(handleClearPreviousMatches);
        setGame();
        createBoard();

        
    }

    @FXML
    private EventHandler<ActionEvent> handleClearPreviousMatches = new EventHandler<ActionEvent>() { 
        @Override
        public void handle(ActionEvent event) {
            try(PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(new File("PlayedMatches.txt"), false)))) {
                writer.print("");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @FXML
    private EventHandler<ActionEvent> handleNewGame = new EventHandler<ActionEvent>() { 

        @Override
        public void handle(ActionEvent event) {
            try {
                initialize();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    
    };


    


    private void setGame() {
        this.game = new Game();
    }


    @FXML
    private void movePieceImages(Square square, int to) throws FileNotFoundException {
        ImageView cloneImage = new ImageView();
        InputStream is = new FileInputStream(ChessProjectController.class.getResource("images/").getFile() + setImageName(square));
        cloneImage.setImage(new Image(is));
        cloneImage.setFitHeight(70.0);
        cloneImage.setFitWidth(70.0);
        game.getBoard().getSquareList().get(to).setPieceImage(cloneImage);
        gridBoard.add(cloneImage, (int) (to-(8*Math.floor(to/8))), (int) (7-Math.floor(to/8)));
    }

    @FXML
    public void move(int from, int to) throws FileNotFoundException {
        movePieceImages(game.getBoard().getSquareList().get(from), to);
        game.move(from, to);
    }

    @FXML
    private void createBoard() throws FileNotFoundException {
        for (Square square : game.getBoard().getSquareList()) {
            AnchorPane pane = square.getPane();
            ImageView pieceImage = square.getImage();
            ImageView squareImage = new ImageView();
            int i = square.getSquareId();

            InputStream inputStream = new FileInputStream(ChessProjectController.class.getResource("images/").getFile() + setSquareColor(square) + ".png");
            squareImage.setImage(new Image(inputStream));
            squareImage.setFitHeight(75.0);
            squareImage.setFitWidth(75.0);
            squareImage.setDisable(true);

            InputStream iStreamWhiteQueen = new FileInputStream(ChessProjectController.class.getResource("images/").getFile() + "w_q.png");
            InputStream iStreamBlackQueen = new FileInputStream(ChessProjectController.class.getResource("images/").getFile() + "b_q.png");

            if (!square.isOccupied()) {
                pane.setDisable(true);
                pieceImage.setDisable(true);
            }
            else {
                pane.setDisable(false);
                pieceImage.setDisable(false);
                InputStream is = new FileInputStream(ChessProjectController.class.getResource("images/").getFile() + setImageName(square));
                pieceImage.setImage(new Image(is));
                pieceImage.setFitHeight(70.0);
                pieceImage.setFitWidth(70.0);
            }
            gridBoard.add(squareImage, (int) (i-(8*Math.floor(i/8))), (int) (7-Math.floor(i/8)));
            gridBoard.add(pieceImage, (int) (i-(8*Math.floor(i/8))), (int) (7-Math.floor(i/8)));
            gridBoard.add(pane, (int) (i-(8*Math.floor(i/8))), (int) (7-Math.floor(i/8)));
            
            pane.setOnMouseClicked(event -> {
                
                if(game.isGameOver()) {
                    gameIsOver();
                    return;
                }
                
                if (game.getRules().isCheckOnBlack(game.getBoard()) || game.getRules().isCheckOnWhite(game.getBoard())) {
                    game.getBoard().getSquareList()
                        .stream()
                        .filter(e -> e.isOccupied())
                        .filter(e -> e.getOccupatorPiece().getType() != 'k')
                        .forEach(e -> e.getPane().setStyle("-fx-opacity: 0%"));
                    game.getBoard().getSquareList()
                        .stream()
                        .filter(e -> !e.isOccupied())
                        .forEach(e -> e.getPane().setStyle("-fx-opacity: 0%"));
                }
                else {
                    game.getBoard().getSquareList()
                        .stream()
                        .forEach(e -> e.getPane().setStyle("-fx-opacity: 0%"));
                }

                if (game.getRules().isCheckOnBlack(game.getBoard())) {
                    game.getBoard().getBlackPieces()
                        .stream()
                        .filter(e -> (e.getType() == 'k'))
                        .forEach(e -> game.getBoard().getSquareList().get(e.getLocation()).getPane().setStyle("-fx-background-color: red; -fx-opacity: 30%"));
        
                }
                else if (game.getRules().isCheckOnWhite(game.getBoard())) {
                    game.getBoard().getWhitePieces()
                        .stream()
                        .filter(e -> (e.getType() == 'k'))
                        .forEach(e -> game.getBoard().getSquareList().get(e.getLocation()).getPane().setStyle("-fx-background-color: red; -fx-opacity: 30%"));
                }
                
                if(!clicked) {
                    if ((square.getOccupatorPiece().getTeam() == 'b' && game.isBlacksTurn()) || (square.getOccupatorPiece().getTeam() == 'w' && game.isWhitesTurn())) {
                        clicked = true;
                        if (!pane.isDisabled()) {
                            for (int move : game.getRules().getLegalMoves(game.getBoard(), game.getBoard().getSquareList().get(i).getOccupatorPiece(), true, true)) {
                                game.getBoard().getSquareList().get(move).getPane().setStyle("-fx-background-color: green; -fx-opacity: 30%");
                                legalSquares.add(game.getBoard().getSquareList().get(move));
                                moveFrom = square.getSquareId();
                            }
                            square.getPane().setStyle("-fx-background-color: blue; -fx-opacity: 10%");
                            legalSquares.stream().forEach(e -> e.getPane().setDisable(false));
                        }
                    }
                }
                else if (clicked) {
                    if (legalSquares.contains(square)) {
                        if (square.isOccupied()) {
                            square.getImage().toBack();
                        }
                        try {
                            move(moveFrom, square.getSquareId());
                        } catch (FileNotFoundException e1) {
                            e1.printStackTrace();
                        }
                        //moveSound.play();
                        if (square.getOccupatorPiece().getType() == 'k') {
                            if (Math.abs(square.getSquareId() - moveFrom) == 2) {
                                if (square.getOccupatorPiece().getTeam() == 'w') {
                                    if (square.getSquareId() == 2) {
                                        try {
                                            movePieceImages(game.getBoard().getSquareList().get(0), 3);
                                        } catch (FileNotFoundException e1) {
                                            e1.printStackTrace();
                                        }
                                        game.getBoard().getSquareList().get(0).getImage().toBack();
                                        game.getBoard().getSquareList().get(3).getPane().toFront();
                                        Piece rook = game.getBoard().getSquareList().get(0).getOccupatorPiece();
                                        game.getBoard().getSquareList().get(0).removeOccupation();
                                        rook.setLocation(3);
                                        game.getBoard().getSquareList().get(3).setOccupator(rook);
                                    }
                                    else if (square.getSquareId() == 6) {
                                        try {
                                            movePieceImages(game.getBoard().getSquareList().get(7), 5);
                                        } catch (FileNotFoundException e1) {
                                            e1.printStackTrace();
                                        }
                                        game.getBoard().getSquareList().get(7).getImage().toBack();
                                        game.getBoard().getSquareList().get(5).getPane().toFront();
                                        Piece rook = game.getBoard().getSquareList().get(7).getOccupatorPiece();
                                        game.getBoard().getSquareList().get(7).removeOccupation();
                                        rook.setLocation(5);
                                        game.getBoard().getSquareList().get(5).setOccupator(rook);
                                    }
                                }
                                else {
                                    if (square.getSquareId() == 58) {
                                        try {
                                            movePieceImages(game.getBoard().getSquareList().get(56), 59);
                                        } catch (FileNotFoundException e1) {
                                            e1.printStackTrace();
                                        }
                                        game.getBoard().getSquareList().get(56).getImage().toBack();
                                        game.getBoard().getSquareList().get(59).getPane().toFront();
                                        Piece rook = game.getBoard().getSquareList().get(56).getOccupatorPiece();
                                        game.getBoard().getSquareList().get(56).removeOccupation();
                                        rook.setLocation(59);
                                        game.getBoard().getSquareList().get(59).setOccupator(rook);
                                    }
                                    else if (square.getSquareId() == 62) {
                                        try {
                                            movePieceImages(game.getBoard().getSquareList().get(63), 61);
                                        } catch (FileNotFoundException e1) {
                                            e1.printStackTrace();
                                        }
                                        game.getBoard().getSquareList().get(63).getImage().toBack();
                                        game.getBoard().getSquareList().get(61).getPane().toFront();
                                        Piece rook = game.getBoard().getSquareList().get(63).getOccupatorPiece();
                                        game.getBoard().getSquareList().get(63).removeOccupation();
                                        rook.setLocation(61);
                                        game.getBoard().getSquareList().get(61).setOccupator(rook);
                                    }                                    
                                }
                            }
                        }
                        Piece piece = square.getOccupatorPiece();
                        int k = square.getSquareId();

                        if (piece.getType() == 'p' && piece.getTeam() == 'b' && piece.getLocation() >= 0 && piece.getLocation() < 8) {
                            Piece transformPiece = new Piece(piece.getLocation(), piece.getTeam(), 'q');
                            square.getImage().toBack();
                            ImageView queenImage = new ImageView();
                            queenImage.setImage(new Image(iStreamBlackQueen));
                            game.getBoard().getSquareList().get(piece.getLocation()).setPieceImage(queenImage);
                            queenImage.setFitHeight(70.0);
                            queenImage.setFitWidth(70.0);
                            gridBoard.add(queenImage, (int) (k-(8*Math.floor(k/8))), (int) (7-Math.floor(k/8)));
                            game.getBoard().getBlackPieces().remove(piece);
                            piece.getCaptured();
                            game.getBoard().addPiece(transformPiece);
                        }
                        else if (piece.getType() == 'p' && piece.getTeam() == 'w' && piece.getLocation() >= 56 && piece.getLocation() < 64) {
                            Piece transformPiece = new Piece(piece.getLocation(), piece.getTeam(), 'q');
                            square.getImage().toBack();
                            ImageView queenImage = new ImageView();
                            queenImage.setImage(new Image(iStreamWhiteQueen));
                            game.getBoard().getSquareList().get(piece.getLocation()).setPieceImage(queenImage);
                            queenImage.setFitHeight(70.0);
                            queenImage.setFitWidth(70.0);                            
                            gridBoard.add(queenImage, (int) (k-(8*Math.floor(k/8))), (int) (7-Math.floor(k/8)));
                            game.getBoard().getWhitePieces().remove(piece);
                            piece.getCaptured();
                            game.getBoard().addPiece(transformPiece);
                        }

                        square.getImage().toFront();
                        square.getPane().toFront();
                        game.getBoard().getSquareList().get(moveFrom).getImage().toBack();
                        if (game.getRules().isCheckOnBlack(game.getBoard())) {
                            game.getBoard().getSquareList()
                                .stream()
                                .filter(e -> e.isOccupied())
                                .filter(e -> e.getOccupatorPiece().getTeam() == 'b')
                                .filter(e -> e.getOccupatorPiece().getType() == 'k')
                                .findFirst()
                                .get()
                                .getPane()
                                .setStyle("-fx-background-color: red; -fx-opacity: 30%");
                        }
                        else if (game.getRules().isCheckOnWhite(game.getBoard())) {
                            game.getBoard().getSquareList()
                                .stream()
                                .filter(e -> e.isOccupied())
                                .filter(e -> e.getOccupatorPiece().getTeam() == 'w')
                                .filter(e -> e.getOccupatorPiece().getType() == 'k')
                                .findFirst()
                                .get()
                                .getPane()
                                .setStyle("-fx-background-color: red; -fx-opacity: 30%");
                        }
                        else {
                            game.getBoard().getSquareList()
                                .stream()
                                .forEach(e -> e.getPane().setStyle("-fx-opacity: 0%"));
                        }
                        game.getBoard().getSquareList().get(moveFrom).getPane().setDisable(true);
                        game.getBoard().getSquareList().get(moveFrom).getImage().setDisable(true);
                        clicked = false;
                        legalSquares
                            .stream()
                            .filter(e -> !e.isOccupied())
                            .forEach(e -> e.getPane()
                            .setDisable(true));
                        pane.setDisable(false);
                        pieceImage.setDisable(false);
                        legalSquares.clear();
                    }
                    else if (!pane.isDisabled()) {
                        legalSquares
                            .stream()
                            .filter(e -> !e.isOccupied())
                            .forEach(e -> e.getPane()
                            .setDisable(true));
                        legalSquares.clear();
                        if (square.isOccupied()) {
                            if (square.getOccupatorPiece().getTeam() == 'b' && game.isWhitesTurn()) {
                                clicked = false;
                                return;
                            }
                            else if (square.getOccupatorPiece().getTeam() == 'w' && game.isBlacksTurn()) {
                                clicked = false;
                                return;
                            }
                        }
                        if (!square.isOccupied()) {
                            throw new IllegalAccessError("Should not be able to click on an non occupied square!");
                        }
                        square.getPane().setStyle("-fx-background-color: blue; -fx-opacity: 10%");
                        for (int move : game.getRules().getLegalMoves(game.getBoard(), game.getBoard().getSquareList().get(square.getSquareId()).getOccupatorPiece(), true, true)) {
                            game.getBoard().getSquareList().get(move).getPane().setStyle("-fx-background-color: green; -fx-opacity: 30%");
                            legalSquares.add(game.getBoard().getSquareList().get(move));
                        }
                        clicked = true;
                        moveFrom = square.getSquareId();
                        legalSquares
                            .stream()
                            .forEach(e -> e.getPane()
                            .setDisable(false));
                    }
                }
                if(game.isGameOver()) {
                    gameIsOver();
                    return;
                }
            });
        }

    }

    private void gameIsOver() {
        Text resultText = new Text(game.getGameState());
        resultText.setStyle("-fx-font-size: 80;");
        resultText.setFill(Color.YELLOW);
        gridBoard.add(resultText, 1, 3);
        resultText.toFront();
    }


    private String setSquareColor(Square square) {
        int i = square.getSquareId();
        int collumn = (int) (i-(8*Math.floor(i/8)));
        int row = (int) (7-Math.floor(i/8));
        if (collumn%2 == row%2) {
            return "dark";
        }
        else {
            return "light";
        }
    }
    

    private String setImageName(Square square) {
        Piece piece = square.getOccupatorPiece();
        if (piece.getTeam() == 'w') {
            return "w_" + piece.getType() + ".png";
        }
        else {
            return "b_" + piece.getType() + ".png";
        }
    }

    
    
}
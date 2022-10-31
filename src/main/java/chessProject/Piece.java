package chessProject;


public class Piece {

    private int location;
    private char team;
    private char type;
    private boolean captured;
    private boolean moved;

    public char getTeam() {
        return team;
    }

    public boolean getCaptured() {
        return captured;
    }

    public char getType() {
        return type;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        if (isValidLocation(location)) {
            this.location = location;
            this.moved = true;
        }
        else {
            throw new IllegalArgumentException("Invalid location, must be from 0 up until 63");
        }
    }

    private boolean isValidLocation(int location) {
        if (location >= 0 && location < 64) {
            return true;
        }
        return false;
    }

    private boolean isValidType(char type) {
        if (type == 'p' || type == 'r' || type == 'q' || type == 'h' || type == 'b' || type == 'k') {
            return true;
        }
        return false;
    }

    public void setCaptured() {
        this.captured = true;
        this.location = -1;
    }

    public Piece(int location, char team, char type) {
        if (isValidLocation(location)){
            this.location = location;
        }
        else {
            throw new IllegalArgumentException("Invalid location. Must be from 0 up until 63");
        }
        if (team == 'b' || team == 'w') {
            this.team = team;
        }
        else {
            throw new IllegalArgumentException("Invalid team, must be 'w' or 'b'");
        }
        if (isValidType(type)) {
            this.type = type;
        }
        else {
            throw new IllegalArgumentException("Invalid type");
        }
        this.captured = false;
        this.moved = false;

    }
    public Piece(Piece piece) {
        this.location = piece.getLocation();
        this.team = piece.getTeam();
        this.type = piece.getType();
        this.captured = piece.getCaptured();
        this.moved = piece.getMoved();
    }

    public boolean getMoved() {
        return this.moved;
    }

}

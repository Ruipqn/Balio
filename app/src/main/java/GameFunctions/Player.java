package GameFunctions;

public class Player {

    private String name;
    private String color;
    private int points;
    private int lives;

    public Player(String name, String color) {
        this.name = name;
        this.color = color;
        this.points = 0;
        resetLives();
    }

    public void setPoints() {
        this.points += 1;
    }

    public String getColor() {
        return color;
    }

    public int getPoints() {
        return points;
    }

    public String getName() {
        return name;
    }

    public int getLives() {
        return lives;
    }

    public void addLives() {
        if (this.lives <3){
            this.lives += 1;
        }
    }
    public void removeLives() {
        if (this.lives >0){
            this.lives -= 1;
        }
    }

    public void resetLives() {
        this.lives = 3;
    }
}

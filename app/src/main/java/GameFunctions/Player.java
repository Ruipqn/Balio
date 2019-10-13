package GameFunctions;

public class Player {

    private String name;
    private String color;
    private int points;
    private int lifes;

    public Player(String name, String color) {
        this.name = name;
        this.color = color;
        this.points = 0;
        resetLifes();
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

    public int getLifes() {
        return lifes;
    }

    public void addLifes() {
        this.lifes += 1;
    }

    public void removeLifes() {
        this.lifes -= 1;
    }

    public void resetLifes() {
        this.lifes = 3;
    }
}

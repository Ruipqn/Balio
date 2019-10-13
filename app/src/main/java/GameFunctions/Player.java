package GameFunctions;

public class Player {

    private String name;
    private String color;
    private int points;

    public Player(String name, String color) {
        this.name = name;
        this.color = color;
        this.points = 0;
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
}

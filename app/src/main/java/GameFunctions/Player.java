package GameFunctions;

import android.widget.ImageView;

public class Player {

    private String name;
    private String color;
    private ImageView ball;
    private int points;

    public Player(String name, String color, ImageView ball) {
        this.name = name;
        this.color = color;
        this.ball = ball;
        this.points = 0;
    }

    public void setPoints() {
        this.points += 1;
    }

    public ImageView getBall() {
        return ball;
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

package GameFunctions;

import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class PositionMethods {

    private int screenWidth;
    private int screenHeigh;
    private ImageView ball;


    public PositionMethods(int screenWidth, int screenHeigh, ImageView ball) {
        this.screenWidth = screenWidth;
        this.screenHeigh = screenHeigh;
        this.ball = ball;
    }

    public List<Double> genDirectionVector(int position) {
        double angle = generateRandomDirectionAngle();

        double x = Math.cos(angle);
        double y = Math.sin(angle);

        List<Double> vector = new ArrayList<>(2);

        if (position == 1 && y < 0)
            y = -y;

        else if (position == 2 && x < 0)
            x = -x;

        else if (position == 3 && y > 0)
            y = -y;

        else if (position == 4 && x > 0)
            x = -x;

        vector.add(x);
        vector.add(y);

        return vector;
    }

    public List<Double> genDirectionVector() {
        return genDirectionVector(0);
    }

    public List<Double> genStart() {

        double rnd = Math.random();

        double x;
        double y;
        double z;

        if (rnd <= 0.25) {
            //generate on top wall
            y = 0;
            x = Math.floor(Math.random() * (screenWidth - ball.getWidth()));
            z = 1;
        } else if (0.25 < rnd && rnd <= 0.5) {
            //generate on right wall
            y = Math.floor(Math.random() * (screenHeigh - ball.getHeight()));
            x = screenWidth - ball.getWidth();
            z = 2;
        } else if (0.5 < rnd && rnd <= 0.75) {
            //generate on bottom wall
            y = screenHeigh - ball.getHeight();
            x = Math.floor(Math.random() * (screenWidth - ball.getWidth()));
            z = 3;
        } else {
            //generate on left wall
            y = Math.floor(Math.random() * (screenHeigh - ball.getHeight()));
            x = 0;
            z = 4;
        }
        List<Double> array = new ArrayList<>(3);

        array.add(x);
        array.add(y);
        array.add(z);

        return array;
    }

    private double generateRandomDirectionAngle() {
        // Makes te ball go slightly to the center of the screen so
        // it doesn't spend most of the time in the corners of the screen

        return randomNumber()*360;
    }

    private double randomNumber(){
        return Math.random();
    }
}

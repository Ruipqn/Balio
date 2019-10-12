package GameFunctions;

import java.util.ArrayList;
import java.util.List;

public class PositionMethods {

    private int screenWidth;
    private int screenHeigh;
    private double ballWidth;
    private double ballHeight;


    public PositionMethods(int screenWidth, int screenHeigh, double ballWidth, double ballHeight) {
        this.screenWidth = screenWidth;
        this.screenHeigh = screenHeigh;
        this.ballWidth = ballWidth;
        this.ballHeight = ballHeight;
    }

    public List<Double> genDirectionVector(List<Double> position, int start) {
        double angle = generateRandomDirectionAngle(position);

        double x = Math.cos(angle);
        double y = Math.sin(angle);

        List<Double> vector = new ArrayList<>(2);

        if (start == 1 && y < 0)
            y = -y;

        else if (start == 2 && x < 0)
            x = -x;

        else if (start == 3 && y > 0)
            y = -y;

        else if (start == 4 && x > 0)
            x = -x;

        vector.add(x);
        vector.add(y);

        return vector;
    }

    public List<Double> genDirectionVector(List<Double> position) {
        return genDirectionVector(position, 0);
    }

    public List<Double> genStart() {

        double rnd = randomNumber();

        double x;
        double y;
        double start_pos;

        if (rnd <= 0.25) {
            //generate on top wall
            y = 0;
            x = Math.floor(randomNumber() * (screenWidth - ballWidth));
            start_pos = 1;
        } else if (0.25 < rnd && rnd <= 0.5) {
            //generate on right wall
            y = Math.floor(randomNumber() * (screenHeigh - ballHeight));
            x = screenWidth - ballWidth;
            start_pos = 2;
        } else if (0.5 < rnd && rnd <= 0.75) {
            //generate on bottom wall
            y = screenHeigh - ballHeight;
            x = Math.floor(randomNumber() * (screenWidth - ballWidth));
            start_pos = 3;
        } else {
            //generate on left wall
            y = Math.floor(randomNumber() * (screenHeigh - ballHeight));
            x = 0;
            start_pos = 4;
        }

        List<Double> array = new ArrayList<>(3);

        array.add(x);
        array.add(y);
        array.add(start_pos);

        return array;
    }

    private double generateRandomDirectionAngle(List<Double> position) {
        // Makes te ball go slightly to the center of the screen so
        // it doesn't spend most of the time in the corners of the screen

        double direction_x = (double) screenWidth / 2 - position.get(0);
        double direction_y = position.get(1) - (double) screenHeigh / 2;

        if (randomNumber() < 0.5)
            return Math.toRadians(40 * randomNumber()) + (1 / Math.tan(direction_y / direction_x));

        else
            return Math.toRadians(-40 * randomNumber()) + (1 / Math.tan(direction_y / direction_x));
    }

    private double randomNumber(){
        return Math.random();
    }
}

package GameFunctions;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class PositionMethods {

    private int screenWidth;
    private int screenHeigh;
    private double ballWidth;
    private double ballHeight;
    private int starting_angle = 30;


    public PositionMethods(int screenWidth, int screenHeigh, double ballWidth, double ballHeight) {
        this.screenWidth = screenWidth;
        this.screenHeigh = screenHeigh;
        this.ballWidth = ballWidth;
        this.ballHeight = ballHeight;
    }

    public List<Double> genDirectionVector(List<Double> position) {

        double center_direction_x = (double) screenWidth / 2 - position.get(0);
        double center_direction_y = (double) screenHeigh / 2 - position.get(1);

        double angle_to_center = Math.atan(center_direction_y / center_direction_x);

        double new_angle = generateRandomDirectionAngle(angle_to_center);

        double x = Math.cos(new_angle) * center_direction_x / Math.cos(angle_to_center);
        double y = Math.sin(new_angle) * center_direction_y / Math.sin(angle_to_center);

        double length = Math.sqrt(x*x + y*y);

        if(length > 0){
            x /= length;
            y /= length;
        }

        List<Double> vector = new ArrayList<>(2);

        vector.add(x);
        vector.add(y);

        return vector;
    }

    public List<Double> genStart() {
        double rnd = randomNumber();
        double x;
        double y;

        if (rnd <= 0.25) {
            //generate on top wall
            y = -236;
            x = Math.floor(randomNumber() * screenWidth);
        } else if (0.25 < rnd && rnd <= 0.5) {
            //generate on right wall
            y = Math.floor(randomNumber() * screenHeigh);
            x = screenWidth;// - ballWidth;
        } else if (0.5 < rnd && rnd <= 0.75) {
            //generate on bottom wall
            y = screenHeigh;// - ballHeight;
            x = Math.floor(randomNumber() * screenWidth);
        } else {
            //generate on left wall
            y = Math.floor(randomNumber() * screenHeigh);
            x = -236;
        }

        List<Double> array = new ArrayList<>(2);

        array.add(x);
        array.add(y);

        return array;
    }

    private double generateRandomDirectionAngle(double angle) {
        // Makes te ball go slightly to the center of the screen so
        // it doesn't spend most of the time in the corners of the screen

        if (randomNumber() < 0.5)
            return Math.toRadians(starting_angle * randomNumber() + Math.toDegrees(angle));

        else
            return Math.toRadians(starting_angle * randomNumber() + Math.toDegrees(angle));
    }

    private double randomNumber(){
        return Math.random();
    }

    public void setStarting_angle(int starting_angle) {
        this.starting_angle = starting_angle;
    }
}

package GameFunctions;

import java.util.ArrayList;
import java.util.List;

public class PositionMethods {

    public PositionMethods() {}

    public List getDirectionVector() {
        double angle = generateRandomDirectionAngle();

        double x = Math.cos(angle);
        double y = Math.sin(angle);

        List<Double> vector = new ArrayList<>();

        vector.add(x);
        vector.add(y);

        return vector;
    }

    private double generateRandomDirectionAngle() {
        return randomNumber()*360;
    }

    private double randomNumber(){
        return Math.random();
    }
}

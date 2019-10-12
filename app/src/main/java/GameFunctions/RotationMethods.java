package GameFunctions;

import java.util.ArrayList;
import java.util.List;

public class RotationMethods {

    private int baseRotation = 10;

    public RotationMethods() {}

    public List changeRotation(List<Double> direction) {

        // Make the ball rotate to the other direction
        if (Math.random() < 0.5)
            setBaseRotation(getBaseRotation() * -1);

        double x = direction.get(0);
        double y = direction.get(1);

        double new_x = x * Math.cos(Math.toRadians(getBaseRotation())) +
                y * Math.sin(Math.toRadians(getBaseRotation()));
        double new_y = y * Math.cos(Math.toRadians(getBaseRotation())) -
                x * Math.sin(Math.toRadians(getBaseRotation()));

        List<Double> vector = new ArrayList<>();

        vector.add(new_x);
        vector.add(new_y);

        return vector;
    }

    private int getBaseRotation() {
        return baseRotation;
    }

    public void setBaseRotation(int baseRotation) {
        this.baseRotation = baseRotation;
    }
}

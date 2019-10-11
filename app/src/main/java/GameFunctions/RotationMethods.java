package GameFunctions;

import java.util.ArrayList;
import java.util.List;

public class RotationMethods {

    private int baseRotation;

    public RotationMethods() {}

    public List changeRotation(List<Double> direction) {

        double x = direction.get(0);
        double y = direction.get(1);

        double new_x = x * Math.cos(getBaseRotation()) + y * Math.sin(getBaseRotation());
        double new_y = y * Math.cos(getBaseRotation()) - x * Math.sin(getBaseRotation());

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

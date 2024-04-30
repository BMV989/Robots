package model;

import java.awt.geom.Point2D;

public class Target {

    private final Point2D.Double position = new Point2D.Double();

    public Target(double x, double y) {
        double rate = 2f;
        position.setLocation(x * rate, y * rate);
    }

    public Target() {}

    public Point2D.Double getPosition() {
        return position;
    }
}
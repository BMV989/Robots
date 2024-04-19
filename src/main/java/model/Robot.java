package model;

import java.awt.geom.Point2D;

public class Robot {
    private final Point2D.Double position = new Point2D.Double(100, 100);
    private double direction = 0;
    private double angularVelocity = 0;
    private final double velocity = 0.1;

    public void move(Point2D.Double dv) {
        position.setLocation(getPosition().getX() + dv.getX(),
            getPosition().getY() + dv.getY());
    }

    public double getVelocity() {
        return velocity;
    }

    public Point2D.Double getPosition() {
        return position;
    }

    public double getDirection() {
        return direction;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public void setAngularVelocity(double angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    public double getAngularVelocity() {
        return angularVelocity;
    }
}

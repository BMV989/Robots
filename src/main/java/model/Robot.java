package model;

import java.util.Observable;

public class Robot extends Observable {
    private volatile double m_robotPositionX;
    private volatile double m_robotPositionY;
    private volatile double m_robotDirection = 0;

    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.001;
    public static final String ROBOT_HAS_MOVED = "robot has moved";

    public Robot(int x, int y) {
        super();
        m_robotPositionX = x;
        m_robotPositionY = y;
    }

    public void update(double m_targetPositionX, double m_targetPositionY) {
        double distance = distance(m_targetPositionX, m_targetPositionY, m_robotPositionX, m_robotPositionY);

        if (distance < 0.5) return;

        double angleToTarget = angleTo(m_robotPositionX, m_robotPositionY, m_targetPositionX, m_targetPositionY);
        double angularVelocity = 0;
        if (angleToTarget > m_robotDirection)
        {
            angularVelocity = maxAngularVelocity;
        }
        if (angleToTarget < m_robotDirection)
        {
            angularVelocity = -maxAngularVelocity;
        }

        moveRobot(maxVelocity, angularVelocity, 10);
        setChanged();
        notifyObservers(ROBOT_HAS_MOVED);
        clearChanged();
    }

    private void moveRobot(double velocity, double angularVelocity, double duration) {
        velocity = applyLimits(velocity, 0, maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        double newX = m_robotPositionX + velocity / angularVelocity *
            (Math.sin(m_robotDirection  + angularVelocity * duration) -
                Math.sin(m_robotDirection));
        if (!Double.isFinite(newX))
        {
            newX = m_robotPositionX + velocity * duration * Math.cos(m_robotDirection);
        }
        double newY = m_robotPositionY - velocity / angularVelocity *
            (Math.cos(m_robotDirection  + angularVelocity * duration) -
                Math.cos(m_robotDirection));
        if (!Double.isFinite(newY))
        {
            newY = m_robotPositionY + velocity * duration * Math.sin(m_robotDirection);
        }
        m_robotPositionX = newX;
        m_robotPositionY = newY;
        double newDirection = asNormalizedRadians(m_robotDirection + angularVelocity * duration);
        m_robotDirection = newDirection;
    }

    private static double applyLimits(double value, double min, double max) {
        if (value < min) return min;
        return Math.min(value, max);
    }

    private static double asNormalizedRadians(double angle) {
        while (angle < 0)
            angle += 2*Math.PI;

        while (angle >= 2*Math.PI)
            angle -= 2*Math.PI;

        return angle;
    }

    private static double distance(double x1, double y1, double x2, double y2) {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    private static double angleTo(double fromX, double fromY, double toX, double toY) {
        double diffX = toX - fromX;
        double diffY = toY - fromY;

        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }
    public double getM_robotPositionX() {
        return m_robotPositionX;
    }
    public double getM_robotPositionY() {
        return m_robotPositionY;
    }
    public double getM_robotDirection() {
        return m_robotDirection;
    }

    public String log() {
        return String.format(
            "Position: (%f, %f) | Direction: %f",
            m_robotPositionX, m_robotPositionY, m_robotDirection
        );
    }
}

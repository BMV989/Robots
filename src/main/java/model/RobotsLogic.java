package model;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

public class RobotsLogic extends Observable {
    private static final double ANGULAR_VELOCITY = 0.001;
    private static final double TARGET_CLOSE_ENOUGH = 5;
    private final static double EPSILON = 0.05;

    private final Robot robot;
    private Target target;

    private final long dt = 5;
    private Timer timer;
    private Point2D.Double windowBounds = new Point2D.Double(300, 300);

    public RobotsLogic() {
        robot = new Robot();
        target = new Target(10, 10);
        setTarget(target);
        moveRobot();

    }

    public void startTimer() {
        timer = new Timer("event generator", true);
        addActionToTimer(new TimerTask() {
            @Override
            public void run() {
                moveRobot();

                setChanged();
                notifyObservers();
            }
        }, dt);
    }

    public void moveRobot() {
        if (robot.getPosition().distance(target.getPosition()) < TARGET_CLOSE_ENOUGH) {
            return;
        }

        final double angleRobotTarget = RobotsMath.angleTo(robot.getPosition(), target.getPosition());

        if (Math.abs(robot.getAngularVelocity()) < ANGULAR_VELOCITY ||
            Math.abs(robot.getDirection() - angleRobotTarget) < EPSILON) {
            robot.move(new Double(
                robot.getVelocity() * Math.cos(robot.getDirection()) * dt,
                robot.getVelocity() * Math.sin(robot.getDirection()) * dt
            ));

            return;
        }

        final double newAngle = RobotsMath.asNormalizedRadians(
            robot.getDirection() + robot.getAngularVelocity() * dt);

        final double dx = robot.getVelocity() / robot.getAngularVelocity() *
            (Math.sin(newAngle) - Math.sin(robot.getDirection()));
        final double dy = robot.getVelocity() / robot.getAngularVelocity() *
            (Math.cos(newAngle) - Math.cos(robot.getDirection()));

        robot.move(new Point2D.Double(
            dx * RobotsMath.speedFactor(robot.getPosition().getX(), windowBounds.getX()),
            - dy * RobotsMath.speedFactor(robot.getPosition().getY(), windowBounds.getY())
        ));
        robot.setDirection(newAngle);
    }

    public void addActionToTimer(TimerTask task, long timeout) {
        timer.schedule(task, 0, timeout);
    }
    public void stopTimer() {
        timer.cancel();
    }
    public Robot getRobot() {
        return robot;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;

        if (RobotsMath.angleTo(robot.getPosition(), target.getPosition()) > robot.getDirection()) {
            robot.setAngularVelocity(-ANGULAR_VELOCITY);
        } else {
            robot.setAngularVelocity(ANGULAR_VELOCITY);
        }
    }

    public void setWindowBounds(Point2D.Double windowBounds) {
        this.windowBounds = windowBounds;
    }

    private static class RobotsMath {
        public static double angleTo(Point2D.Double p0, Point2D.Double p1) {
            final double dx = p1.getX() - p0.getX();
            final double dy = p1.getY() - p0.getY();

            return asNormalizedRadians(Math.atan2(dy, dx));
        }

        private static double asNormalizedRadians(double angle) {
            final double TAU = 2 * Math.PI;

            if (angle < 0) {
                return TAU - ((-angle) % TAU);
            }

            return angle % TAU;
        }

        private static double speedFactor(double t, double upperBoundT) {
            return Math.max(1 - 2 * Math.abs((upperBoundT - t) / upperBoundT - 0.5), 0.01);
        }
    }
}

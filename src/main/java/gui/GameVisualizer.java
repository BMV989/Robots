package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.TimerTask;
import javax.swing.JPanel;
import model.Robot;
import model.RobotsLogic;
import model.Target;

public class GameVisualizer extends JPanel {

    private final RobotsLogic logic;

    public GameVisualizer(RobotsLogic logic) {
        this.logic = logic;

        logic.addActionToTimer(new TimerTask() {
            @Override
            public void run() {
                repaint();
            }
        }, 50);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point clickPoint = e.getPoint();

                logic.setTarget(new Target(clickPoint.getX(), clickPoint.getY()));
                logic.setWindowBounds(new Point2D.Double(getWidth(), getHeight()));

                repaint();
            }
        });

        setDoubleBuffered(true);
    }

    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        drawRobot(g2d, logic.getRobot());
        drawTarget(g2d, logic.getTarget());
    }

    private void drawRobot(Graphics2D g, Robot robot) {
        int robotCenterX = (int) Math.round(robot.getPosition().getX());
        int robotCenterY = (int) Math.round(robot.getPosition().getY());

        AffineTransform t = AffineTransform.getRotateInstance(robot.getDirection(), robotCenterX,
            robotCenterY);
        g.setTransform(t);

        g.setColor(Color.MAGENTA);
        fillOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX, robotCenterY, 30, 10);

        g.setColor(Color.WHITE);
        fillOval(g, robotCenterX + 10, robotCenterY, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX + 10, robotCenterY, 5, 5);
    }

    private void drawTarget(Graphics2D g, Target target) {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);

        g.setColor(Color.GREEN);
        fillOval(g, (int) target.getPosition().getX(), (int) target.getPosition().getY(), 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, (int) target.getPosition().getX(), (int) target.getPosition().getY(), 5, 5);
    }
}
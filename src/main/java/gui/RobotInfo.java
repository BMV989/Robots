package gui;

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JLabel;
import model.RobotsLogic;

public class RobotInfo extends AbstractWindow implements Observer {
    private  final JLabel label;

    public RobotInfo(RobotsLogic logic) {
        super();
        this.label = new JLabel();

        logic.addObserver(this);
        setResizable(true);
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);

        getContentPane().add(label, BorderLayout.CENTER);
        pack();
    }

    @Override
    public void update(Observable o, Object arg) {
        RobotsLogic lg = (RobotsLogic) o;
        label.setText("x=%f y=%f dir=%f".formatted(lg.getRobot().getPosition().getX(),
            lg.getRobot().getPosition().getY(), lg.getRobot().getDirection()));
    }
}

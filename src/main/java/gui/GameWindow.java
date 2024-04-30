package gui;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import model.RobotsLogic;

public class GameWindow extends AbstractWindow
{

    private final RobotsLogic logic;

    public GameWindow(RobotsLogic logic) {
        super();

        this.logic = logic;

        logic.startTimer();

        setTitle(MainApplicationFrame.bundle.getString("gameWindow.title"));
        setResizable(true);
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);

        GameVisualizer gameVisualizer = new GameVisualizer(logic);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(gameVisualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }

    public void dispose() {
        super.dispose();

        logic.stopTimer();
    }
}

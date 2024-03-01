package gui;

import java.awt.BorderLayout;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends JInternalFrame
{

    public GameWindow(String title)
    {
        super(title, true, true, true, true);
        GameVisualizer m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        setSize(400, 400);
    }
}

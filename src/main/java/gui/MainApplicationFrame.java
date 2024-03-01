package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import log.Logger;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается. 
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 *
 */
public class MainApplicationFrame extends JFrame
{
    private final ResourceBundle bundle = ResourceBundle.getBundle("resources",
        Locale.getDefault());
    private final JDesktopPane desktopPane = new JDesktopPane();
    private class MainMenuBar extends JMenuBar {

        private MainMenuBar() {
            JMenu viewModeMenu = createLookAndFeelMenu();
            JMenu testMenu = createTestMenu();
            JMenu exitMenu = createExitMenu();

            add(exitMenu);
            add(viewModeMenu);
            add(testMenu);
        }

        private JMenu createLookAndFeelMenu() {

            JMenu lookAndFeelMenu = new JMenu(bundle.getString("lookAndFeelMenu.s"));
            lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
            lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(bundle
                .getString("lookAndFeelMenu.getAccessibleContext.setAccessibleDescription"));

            {
                JMenuItem systemLookAndFeel = new JMenuItem(bundle
                    .getString("systemLookAndFeel.text"), KeyEvent.VK_S);
                systemLookAndFeel.addActionListener((event) -> {
                    MainApplicationFrame.this
                        .setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    this.invalidate();
                });
                lookAndFeelMenu.add(systemLookAndFeel);
            }

            {
                JMenuItem crossplatformLookAndFeel = new JMenuItem(bundle
                    .getString("crossplatformLookAndFeel.text"),
                    KeyEvent.VK_S);
                crossplatformLookAndFeel.addActionListener((event) -> {
                    MainApplicationFrame.this
                        .setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    this.invalidate();
                });
                lookAndFeelMenu.add(crossplatformLookAndFeel);
            }
            return lookAndFeelMenu;
        }

        private JMenu createTestMenu() {

            JMenu testMenu = new JMenu(bundle.getString("testMenu.s"));
            testMenu.setMnemonic(KeyEvent.VK_T);
            testMenu.getAccessibleContext().setAccessibleDescription(bundle
                .getString("testMenu.getAccessibleContext.setAccessibleDescription"));

            {
                JMenuItem addLogMessageItem = new JMenuItem(bundle
                    .getString("addLogMessageItem.text"),
                    KeyEvent.VK_S);
                addLogMessageItem.addActionListener((event) -> Logger
                    .debug(bundle.getString("Logger.debug.strMessage.addLine")));
                testMenu.add(addLogMessageItem);
            }
            return testMenu;
        }

        private JMenu createExitMenu() {
            JMenu exitMenu = new JMenu(bundle.getString("exitMenu.s"));
            JMenuItem exitMenuItem = new JMenuItem(bundle.getString("exitMenuItem.text"),
                KeyEvent.VK_Q);
            exitMenu.getAccessibleContext().setAccessibleDescription(bundle
                .getString("exitMenu.getAccessibleContext.setAccessibleDescription"));
            exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
                InputEvent.SHIFT_DOWN_MASK));
            exitMenuItem.addActionListener((event) -> {
                    WindowEvent closeEvent = new WindowEvent(
                        MainApplicationFrame.this, WindowEvent.WINDOW_CLOSING);
                    Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(closeEvent);
                }
            );
            exitMenu.add(exitMenuItem);
            return exitMenu;
        }
    }

    public MainApplicationFrame() {
        int inset = 50;
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
            screenSize.width  - inset*2,
            screenSize.height - inset*2);

        setContentPane(desktopPane);
        
        
        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        GameWindow gameWindow = new GameWindow(bundle.getString("gameWindow.title"));
        addWindow(gameWindow);

        setJMenuBar(new MainMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ExitConfirm();
            }
        });
    }
    
    protected LogWindow createLogWindow()
    {
        LogWindow logWindow = new LogWindow(bundle
            .getString("logWindow.title"), Logger.getDefaultLogSource());
        logWindow.setLocation(10,10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug(bundle.getString("Logger.debug.strMessage.status"));
        return logWindow;
    }
    
    protected void addWindow(JInternalFrame frame)
    {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    private void ExitConfirm() {
        int confirm = JOptionPane.showConfirmDialog(this,
            bundle.getString("confirm.message"),
            bundle.getString("confirm.title"),
            JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void setLookAndFeel(String className)
    {
        try
        {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            // just ignore
        }
    }
}
